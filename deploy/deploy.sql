-- It's OK if following errors occur when script is excuting:
-- -- Database 'Dropbox' does not exist. Make sure that the name is entered correctly.
-- -- Could not drop object 'dbo.Users' because it is referenced by a FOREIGN KEY constraint.
-- -- RegQueryValueEx() returned error 2, 'The system cannot find the file specified.'

USE [Dropbox]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[usp_SaveFile]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[usp_SaveFile]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[usp_CreateUser]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[usp_CreateUser]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[usp_CreateFolder]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[usp_CreateFolder]
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_UserFolders_Users_UserId]') AND parent_object_id = OBJECT_ID(N'[dbo].[UserFolders]'))
ALTER TABLE [dbo].[UserFolders] DROP CONSTRAINT [FK_UserFolders_Users_UserId]
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_UserFolders_Folders_FolderId]') AND parent_object_id = OBJECT_ID(N'[dbo].[UserFolders]'))
ALTER TABLE [dbo].[UserFolders] DROP CONSTRAINT [FK_UserFolders_Folders_FolderId]
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Friendships_Users_UserId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Friendships]'))
ALTER TABLE [dbo].[Friendships] DROP CONSTRAINT [FK_Friendships_Users_UserId]
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Friendships_Users_FriendId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Friendships]'))
ALTER TABLE [dbo].[Friendships] DROP CONSTRAINT [FK_Friendships_Users_FriendId]
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_FoldersContents_Folders_FolderId]') AND parent_object_id = OBJECT_ID(N'[dbo].[FoldersContents]'))
ALTER TABLE [dbo].[FoldersContents] DROP CONSTRAINT [FK_FoldersContents_Folders_FolderId]
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_FoldersContents_Files_FileId]') AND parent_object_id = OBJECT_ID(N'[dbo].[FoldersContents]'))
ALTER TABLE [dbo].[FoldersContents] DROP CONSTRAINT [FK_FoldersContents_Files_FileId]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_Users_RegisterDate]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Users] DROP CONSTRAINT [DF_Users_RegisterDate]
END

GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_FoldersContents_IsPrivate]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[FoldersContents] DROP CONSTRAINT [DF_FoldersContents_IsPrivate]
END

GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_Folders_IsRoot]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Folders] DROP CONSTRAINT [DF_Folders_IsRoot]
END

GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_Folders_UpdateDate]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Folders] DROP CONSTRAINT [DF_Folders_UpdateDate]
END

GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_Folders_CreateDate]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Folders] DROP CONSTRAINT [DF_Folders_CreateDate]
END

GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_Files_CreateDate]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Files] DROP CONSTRAINT [DF_Files_CreateDate]
END

GO
IF  EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[Users]') AND name = N'IX_Users_Username')
DROP INDEX [IX_Users_Username] ON [dbo].[Users]
GO
IF  EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[UserFolders]') AND name = N'IX__UserFolders__UserId')
DROP INDEX [IX__UserFolders__UserId] ON [dbo].[UserFolders]
GO
IF  EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[UserFolders]') AND name = N'IX__UserFolders__FolderId')
DROP INDEX [IX__UserFolders__FolderId] ON [dbo].[UserFolders]
GO
IF  EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[Friendships]') AND name = N'IX_Friendships_UserId')
DROP INDEX [IX_Friendships_UserId] ON [dbo].[Friendships]
GO
IF  EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[Friendships]') AND name = N'IX_Friendships_FriendId')
DROP INDEX [IX_Friendships_FriendId] ON [dbo].[Friendships]
GO
IF  EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[FoldersContents]') AND name = N'IX_FoldersContents_FolderId')
DROP INDEX [IX_FoldersContents_FolderId] ON [dbo].[FoldersContents]
GO
IF  EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[FoldersContents]') AND name = N'IX_FoldersContents_FileId')
DROP INDEX [IX_FoldersContents_FileId] ON [dbo].[FoldersContents]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Users]') AND type in (N'U'))
DROP TABLE [dbo].[Users]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[UserFolders]') AND type in (N'U'))
DROP TABLE [dbo].[UserFolders]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Friendships]') AND type in (N'U'))
DROP TABLE [dbo].[Friendships]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[FoldersContents]') AND type in (N'U'))
DROP TABLE [dbo].[FoldersContents]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Folders]') AND type in (N'U'))
DROP TABLE [dbo].[Folders]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Files]') AND type in (N'U'))
DROP TABLE [dbo].[Files]
GO
USE [master]
GO
IF  EXISTS (SELECT name FROM sys.databases WHERE name = N'Dropbox')
BEGIN
	ALTER DATABASE [Dropbox] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
	DROP DATABASE [Dropbox];
END
GO
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'Dropbox')
BEGIN

DECLARE @DefaultData NVARCHAR(512)
EXEC master.dbo.xp_instance_regread N'HKEY_LOCAL_MACHINE', N'Software\Microsoft\MSSQLServer\MSSQLServer', N'DefaultData', @DefaultData OUTPUT

DECLARE @DefaultLog NVARCHAR(512)
EXEC master.dbo.xp_instance_regread N'HKEY_LOCAL_MACHINE', N'Software\Microsoft\MSSQLServer\MSSQLServer', N'DefaultLog', @DefaultLog OUTPUT

DECLARE @DefaultBackup NVARCHAR(512)
EXEC master.dbo.xp_instance_regread N'HKEY_LOCAL_MACHINE', N'Software\Microsoft\MSSQLServer\MSSQLServer', N'BackupDirectory', @DefaultBackup OUTPUT

DECLARE @MasterData NVARCHAR(512)
EXEC master.dbo.xp_instance_regread N'HKEY_LOCAL_MACHINE', N'Software\Microsoft\MSSQLServer\MSSQLServer\Parameters', N'SqlArg0', @MasterData OUTPUT
SELECT @MasterData = SUBSTRING(@MasterData, 3, 255)
SELECT @MasterData = SUBSTRING(@MasterData, 1, LEN(@MasterData) - CHARINDEX('\', REVERSE(@MasterData)))

DECLARE @MasterLog NVARCHAR(512)
EXEC master.dbo.xp_instance_regread N'HKEY_LOCAL_MACHINE', N'Software\Microsoft\MSSQLServer\MSSQLServer\Parameters', N'SqlArg2', @MasterLog OUTPUT
SELECT @MasterLog = SUBSTRING(@MasterLog, 3, 255)
SELECT @MasterLog = SUBSTRING(@MasterLog, 1, LEN(@MasterLog) - CHARINDEX('\', REVERSE(@MasterLog)))

DECLARE @DbDataPath NVARCHAR(max);
DECLARE @DbLogPath NVARCHAR(max);
SELECT 
    @DbDataPath = ISNULL(@DefaultData, @MasterData) + N'\Dropbox.mdf', 
    @DbLogPath = ISNULL(@DefaultLog, @MasterLog) + N'\Dropbox_log.ldf'

DECLARE @Sql NVARCHAR(MAX) = 
'CREATE DATABASE [Dropbox]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N''Dropbox'', FILENAME = N''' + @DbDataPath + ''' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N''Dropbox_log'', FILENAME = N''' + @DbLogPath + ''' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 COLLATE SQL_Latin1_General_CP1_CI_AS'

EXEC sp_executesql @Sql
END

GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [Dropbox].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [Dropbox] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [Dropbox] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [Dropbox] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [Dropbox] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [Dropbox] SET ARITHABORT OFF 
GO
ALTER DATABASE [Dropbox] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [Dropbox] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [Dropbox] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [Dropbox] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [Dropbox] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [Dropbox] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [Dropbox] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [Dropbox] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [Dropbox] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [Dropbox] SET  DISABLE_BROKER 
GO
ALTER DATABASE [Dropbox] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [Dropbox] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [Dropbox] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [Dropbox] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [Dropbox] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [Dropbox] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [Dropbox] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [Dropbox] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [Dropbox] SET  MULTI_USER 
GO
ALTER DATABASE [Dropbox] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [Dropbox] SET DB_CHAINING OFF 
GO
ALTER DATABASE [Dropbox] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [Dropbox] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [Dropbox] SET DELAYED_DURABILITY = DISABLED 
GO
USE [Dropbox]
GO
ALTER DATABASE SCOPED CONFIGURATION SET MAXDOP = 0;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET MAXDOP = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET LEGACY_CARDINALITY_ESTIMATION = OFF;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET LEGACY_CARDINALITY_ESTIMATION = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET PARAMETER_SNIFFING = ON;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET PARAMETER_SNIFFING = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET QUERY_OPTIMIZER_HOTFIXES = OFF;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET QUERY_OPTIMIZER_HOTFIXES = PRIMARY;
GO
USE [Dropbox]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Files]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Files](
	[FileId] [int] IDENTITY(1,1) NOT NULL,
	[FileName] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CS_AS NOT NULL,
	[FileBytes] [varbinary](max) NOT NULL,
	[CreateDate] [datetime2](7) NOT NULL,
 CONSTRAINT [PK__Files] PRIMARY KEY CLUSTERED 
(
	[FileId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Folders]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Folders](
	[FolderId] [int] IDENTITY(1,1) NOT NULL,
	[FolderName] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CS_AS NOT NULL,
	[IsPrivate] [bit] NOT NULL,
	[CreateDate] [datetime2](7) NOT NULL,
	[UpdateDate] [datetime2](7) NOT NULL,
	[IsRoot] [bit] NOT NULL,
 CONSTRAINT [PK__Folders] PRIMARY KEY CLUSTERED 
(
	[FolderId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[FoldersContents]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[FoldersContents](
	[FolderContentId] [int] IDENTITY(1,1) NOT NULL,
	[FolderId] [int] NOT NULL,
	[FileId] [int] NOT NULL,
	[IsPrivate] [bit] NOT NULL,
 CONSTRAINT [PK_FoldersContents] PRIMARY KEY CLUSTERED 
(
	[FolderContentId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Friendships]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Friendships](
	[FriendshipId] [int] IDENTITY(1,1) NOT NULL,
	[UserId] [int] NOT NULL,
	[FriendId] [int] NOT NULL,
 CONSTRAINT [PK_Friendships] PRIMARY KEY CLUSTERED 
(
	[FriendshipId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[UserFolders]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[UserFolders](
	[UserFolderId] [int] IDENTITY(1,1) NOT NULL,
	[UserId] [int] NOT NULL,
	[FolderId] [int] NOT NULL,
 CONSTRAINT [PK_UserFolders] PRIMARY KEY CLUSTERED 
(
	[UserFolderId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Users]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Users](
	[UserId] [int] IDENTITY(1,1) NOT NULL,
	[Username] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CS_AS NOT NULL,
	[Password] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CS_AS NOT NULL,
	[RegisterDate] [datetime2](7) NOT NULL,
 CONSTRAINT [PK__Users] PRIMARY KEY CLUSTERED 
(
	[UserId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[FoldersContents]') AND name = N'IX_FoldersContents_FileId')
CREATE NONCLUSTERED INDEX [IX_FoldersContents_FileId] ON [dbo].[FoldersContents]
(
	[FileId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[FoldersContents]') AND name = N'IX_FoldersContents_FolderId')
CREATE NONCLUSTERED INDEX [IX_FoldersContents_FolderId] ON [dbo].[FoldersContents]
(
	[FolderId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[Friendships]') AND name = N'IX_Friendships_FriendId')
CREATE NONCLUSTERED INDEX [IX_Friendships_FriendId] ON [dbo].[Friendships]
(
	[FriendId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[Friendships]') AND name = N'IX_Friendships_UserId')
CREATE NONCLUSTERED INDEX [IX_Friendships_UserId] ON [dbo].[Friendships]
(
	[UserId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[UserFolders]') AND name = N'IX__UserFolders__FolderId')
CREATE NONCLUSTERED INDEX [IX__UserFolders__FolderId] ON [dbo].[UserFolders]
(
	[FolderId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[UserFolders]') AND name = N'IX__UserFolders__UserId')
CREATE NONCLUSTERED INDEX [IX__UserFolders__UserId] ON [dbo].[UserFolders]
(
	[UserId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[Users]') AND name = N'IX_Users_Username')
CREATE UNIQUE NONCLUSTERED INDEX [IX_Users_Username] ON [dbo].[Users]
(
	[Username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_Files_CreateDate]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Files] ADD  CONSTRAINT [DF_Files_CreateDate]  DEFAULT (getutcdate()) FOR [CreateDate]
END

GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_Folders_CreateDate]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Folders] ADD  CONSTRAINT [DF_Folders_CreateDate]  DEFAULT (getutcdate()) FOR [CreateDate]
END

GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_Folders_UpdateDate]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Folders] ADD  CONSTRAINT [DF_Folders_UpdateDate]  DEFAULT (getutcdate()) FOR [UpdateDate]
END

GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_Folders_IsRoot]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Folders] ADD  CONSTRAINT [DF_Folders_IsRoot]  DEFAULT ((0)) FOR [IsRoot]
END

GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_FoldersContents_IsPrivate]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[FoldersContents] ADD  CONSTRAINT [DF_FoldersContents_IsPrivate]  DEFAULT ((0)) FOR [IsPrivate]
END

GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_Users_RegisterDate]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Users] ADD  CONSTRAINT [DF_Users_RegisterDate]  DEFAULT (getutcdate()) FOR [RegisterDate]
END

GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_FoldersContents_Files_FileId]') AND parent_object_id = OBJECT_ID(N'[dbo].[FoldersContents]'))
ALTER TABLE [dbo].[FoldersContents]  WITH CHECK ADD  CONSTRAINT [FK_FoldersContents_Files_FileId] FOREIGN KEY([FileId])
REFERENCES [dbo].[Files] ([FileId])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_FoldersContents_Files_FileId]') AND parent_object_id = OBJECT_ID(N'[dbo].[FoldersContents]'))
ALTER TABLE [dbo].[FoldersContents] CHECK CONSTRAINT [FK_FoldersContents_Files_FileId]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_FoldersContents_Folders_FolderId]') AND parent_object_id = OBJECT_ID(N'[dbo].[FoldersContents]'))
ALTER TABLE [dbo].[FoldersContents]  WITH CHECK ADD  CONSTRAINT [FK_FoldersContents_Folders_FolderId] FOREIGN KEY([FolderId])
REFERENCES [dbo].[Folders] ([FolderId])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_FoldersContents_Folders_FolderId]') AND parent_object_id = OBJECT_ID(N'[dbo].[FoldersContents]'))
ALTER TABLE [dbo].[FoldersContents] CHECK CONSTRAINT [FK_FoldersContents_Folders_FolderId]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Friendships_Users_FriendId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Friendships]'))
ALTER TABLE [dbo].[Friendships]  WITH CHECK ADD  CONSTRAINT [FK_Friendships_Users_FriendId] FOREIGN KEY([FriendId])
REFERENCES [dbo].[Users] ([UserId])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Friendships_Users_FriendId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Friendships]'))
ALTER TABLE [dbo].[Friendships] CHECK CONSTRAINT [FK_Friendships_Users_FriendId]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Friendships_Users_UserId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Friendships]'))
ALTER TABLE [dbo].[Friendships]  WITH CHECK ADD  CONSTRAINT [FK_Friendships_Users_UserId] FOREIGN KEY([UserId])
REFERENCES [dbo].[Users] ([UserId])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Friendships_Users_UserId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Friendships]'))
ALTER TABLE [dbo].[Friendships] CHECK CONSTRAINT [FK_Friendships_Users_UserId]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_UserFolders_Folders_FolderId]') AND parent_object_id = OBJECT_ID(N'[dbo].[UserFolders]'))
ALTER TABLE [dbo].[UserFolders]  WITH CHECK ADD  CONSTRAINT [FK_UserFolders_Folders_FolderId] FOREIGN KEY([FolderId])
REFERENCES [dbo].[Folders] ([FolderId])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_UserFolders_Folders_FolderId]') AND parent_object_id = OBJECT_ID(N'[dbo].[UserFolders]'))
ALTER TABLE [dbo].[UserFolders] CHECK CONSTRAINT [FK_UserFolders_Folders_FolderId]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_UserFolders_Users_UserId]') AND parent_object_id = OBJECT_ID(N'[dbo].[UserFolders]'))
ALTER TABLE [dbo].[UserFolders]  WITH CHECK ADD  CONSTRAINT [FK_UserFolders_Users_UserId] FOREIGN KEY([UserId])
REFERENCES [dbo].[Users] ([UserId])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_UserFolders_Users_UserId]') AND parent_object_id = OBJECT_ID(N'[dbo].[UserFolders]'))
ALTER TABLE [dbo].[UserFolders] CHECK CONSTRAINT [FK_UserFolders_Users_UserId]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[usp_CreateFolder]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'CREATE PROCEDURE [dbo].[usp_CreateFolder] AS' 
END
GO
ALTER PROCEDURE [dbo].[usp_CreateFolder] 
	@FolderName NVARCHAR(MAX), 
	@UserId INT,
	@IsPrivate BIT
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @IdTbl TABLE(Id INT);

	IF NOT EXISTS (SELECT 1
		FROM UserFolders UF
		JOIN Folders F
		ON UF.FolderId = F.FolderId
		WHERE UF.UserId = @UserId
			AND F.FolderName = @FolderName)
	BEGIN
		INSERT INTO Folders(FolderName, IsPrivate, IsRoot)
		OUTPUT inserted.FolderId INTO @IdTbl
		VALUES(@FolderName, @IsPrivate, 0)

		INSERT INTO UserFolders(FolderId, UserId)
		VALUES ((SELECT Id FROM @IdTbl), @UserId)

		RETURN (SELECT Id FROM @IdTbl)
	END
	ELSE
		RETURN (-1)
END

GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[usp_CreateUser]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'CREATE PROCEDURE [dbo].[usp_CreateUser] AS' 
END
GO

ALTER PROCEDURE [dbo].[usp_CreateUser] 
	@UserName NVARCHAR(255), 
	@Password NVARCHAR(255)
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @IdTbl TABLE(Id INT);
	DECLARE @UserId INT;

	BEGIN TRY
		INSERT INTO Users(Username, Password)
		OUTPUT inserted.UserId INTO @IdTbl
		VALUES (@UserName, @Password)
	END TRY
	BEGIN CATCH
		IF ERROR_NUMBER() = 2601
			RETURN (0)	
		RETURN (-1) 
	END CATCH

	SELECT @UserId = Id
	FROM @IdTbl

	DELETE FROM @IdTbl

	INSERT INTO Folders(FolderName, IsPrivate, IsRoot)
	OUTPUT inserted.FolderId INTO @IdTbl
	VALUES (@UserName, 1, 1)

	INSERT INTO UserFolders(UserId, FolderId)
	VALUES (@UserId, (SELECT Id FROM @IdTbl))

	RETURN (@UserId)
END

GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[usp_SaveFile]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'CREATE PROCEDURE [dbo].[usp_SaveFile] AS' 
END
GO

ALTER PROCEDURE [dbo].[usp_SaveFile]
	@FolderId INT,
	@FileName NVARCHAR(255),
	@FileContent VARBINARY(MAX)
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @FileIdTbl TABLE (FileId INT);

	IF NOT EXISTS(
		SELECT 1
		FROM FoldersContents FC
		JOIN Files F
		ON FC.FileId = F.FileId
		WHERE FC.FolderId = @FolderId
			AND F.FileName = @FileName)
	BEGIN
		INSERT INTO Files(FileName, FileBytes)
		OUTPUT inserted.FileId INTO @FileIdTbl
		VALUES (@FileName, @FileContent)

		INSERT INTO FoldersContents(FileId, FolderId)
		VALUES((SELECT FileId FROM @FileIdTbl), @FolderId)

		RETURN ((SELECT FileId FROM @FileIdTbl))
	END
	ELSE
	RETURN (-1)
END

GO
USE [master]
GO
ALTER DATABASE [Dropbox] SET  READ_WRITE 
GO
