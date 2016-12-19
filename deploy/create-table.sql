﻿USE [Dropbox]
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Friendships_Users_Username]') AND parent_object_id = OBJECT_ID(N'[dbo].[Friendships]'))
ALTER TABLE [dbo].[Friendships] DROP CONSTRAINT [FK_Friendships_Users_Username]
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Friendships_Users_Friendname]') AND parent_object_id = OBJECT_ID(N'[dbo].[Friendships]'))
ALTER TABLE [dbo].[Friendships] DROP CONSTRAINT [FK_Friendships_Users_Friendname]
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_UserFiles_Friendships_FriendshipId]') AND parent_object_id = OBJECT_ID(N'[dbo].[FileShares]'))
ALTER TABLE [dbo].[FileShares] DROP CONSTRAINT [FK_UserFiles_Friendships_FriendshipId]
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_UserFiles_Files_FileId]') AND parent_object_id = OBJECT_ID(N'[dbo].[FileShares]'))
ALTER TABLE [dbo].[FileShares] DROP CONSTRAINT [FK_UserFiles_Files_FileId]
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Files_Users]') AND parent_object_id = OBJECT_ID(N'[dbo].[Files]'))
ALTER TABLE [dbo].[Files] DROP CONSTRAINT [FK_Files_Users]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_Users_RegisterDate]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Users] DROP CONSTRAINT [DF_Users_RegisterDate]
END

GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_Files_CreateDate]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Files] DROP CONSTRAINT [DF_Files_CreateDate]
END

GO
/****** Object:  Table [dbo].[Users]    Script Date: 12/18/2016 11:15:27 AM ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Users]') AND type in (N'U'))
DROP TABLE [dbo].[Users]
GO
/****** Object:  Table [dbo].[Friendships]    Script Date: 12/18/2016 11:15:27 AM ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Friendships]') AND type in (N'U'))
DROP TABLE [dbo].[Friendships]
GO
/****** Object:  Table [dbo].[FileShares]    Script Date: 12/18/2016 11:15:27 AM ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[FileShares]') AND type in (N'U'))
DROP TABLE [dbo].[FileShares]
GO
/****** Object:  Table [dbo].[Files]    Script Date: 12/18/2016 11:15:27 AM ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Files]') AND type in (N'U'))
DROP TABLE [dbo].[Files]
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
USE [Dropbox]
GO
/****** Object:  Table [dbo].[Files]    Script Date: 12/18/2016 11:15:27 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Files]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Files](
	[FileId] [int] IDENTITY(1,1) NOT NULL,
	[FileName] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	[FileBytes] [varbinary](max) NOT NULL,
	[CreateDate] [datetime2](7) NOT NULL,
	[OwnerName] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CS_AS NOT NULL,
 CONSTRAINT [PK__Files] PRIMARY KEY CLUSTERED 
(
	[FileId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[FileShares]    Script Date: 12/18/2016 11:15:27 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[FileShares]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[FileShares](
	[FileShareId] [int] IDENTITY(1,1) NOT NULL,
	[FileId] [int] NOT NULL,
	[FriendshipId] [int] NOT NULL,
 CONSTRAINT [PK_UserFiles] PRIMARY KEY CLUSTERED 
(
	[FileShareId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Friendships]    Script Date: 12/18/2016 11:15:27 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Friendships]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Friendships](
	[FriendshipId] [int] IDENTITY(1,1) NOT NULL,
	[UserName] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CS_AS NOT NULL,
	[FriendName] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CS_AS NOT NULL,
 CONSTRAINT [PK_Friendships] PRIMARY KEY CLUSTERED 
(
	[FriendshipId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Users]    Script Date: 12/18/2016 11:15:27 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Users]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Users](
	[UserName] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CS_AS NOT NULL,
	[Password] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	[RegisterDate] [datetime2](7) NOT NULL,
 CONSTRAINT [PK__Users] PRIMARY KEY CLUSTERED 
(
	[UserName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_Files_CreateDate]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Files] ADD  CONSTRAINT [DF_Files_CreateDate]  DEFAULT (getutcdate()) FOR [CreateDate]
END

GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF_Users_RegisterDate]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Users] ADD  CONSTRAINT [DF_Users_RegisterDate]  DEFAULT (getutcdate()) FOR [RegisterDate]
END

GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Files_Users]') AND parent_object_id = OBJECT_ID(N'[dbo].[Files]'))
ALTER TABLE [dbo].[Files]  WITH CHECK ADD  CONSTRAINT [FK_Files_Users] FOREIGN KEY([OwnerName])
REFERENCES [dbo].[Users] ([UserName])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Files_Users]') AND parent_object_id = OBJECT_ID(N'[dbo].[Files]'))
ALTER TABLE [dbo].[Files] CHECK CONSTRAINT [FK_Files_Users]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_UserFiles_Files_FileId]') AND parent_object_id = OBJECT_ID(N'[dbo].[FileShares]'))
ALTER TABLE [dbo].[FileShares]  WITH CHECK ADD  CONSTRAINT [FK_UserFiles_Files_FileId] FOREIGN KEY([FileId])
REFERENCES [dbo].[Files] ([FileId])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_UserFiles_Files_FileId]') AND parent_object_id = OBJECT_ID(N'[dbo].[FileShares]'))
ALTER TABLE [dbo].[FileShares] CHECK CONSTRAINT [FK_UserFiles_Files_FileId]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_UserFiles_Friendships_FriendshipId]') AND parent_object_id = OBJECT_ID(N'[dbo].[FileShares]'))
ALTER TABLE [dbo].[FileShares]  WITH CHECK ADD  CONSTRAINT [FK_UserFiles_Friendships_FriendshipId] FOREIGN KEY([FriendshipId])
REFERENCES [dbo].[Friendships] ([FriendshipId])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_UserFiles_Friendships_FriendshipId]') AND parent_object_id = OBJECT_ID(N'[dbo].[FileShares]'))
ALTER TABLE [dbo].[FileShares] CHECK CONSTRAINT [FK_UserFiles_Friendships_FriendshipId]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Friendships_Users_Friendname]') AND parent_object_id = OBJECT_ID(N'[dbo].[Friendships]'))
ALTER TABLE [dbo].[Friendships]  WITH CHECK ADD  CONSTRAINT [FK_Friendships_Users_Friendname] FOREIGN KEY([FriendName])
REFERENCES [dbo].[Users] ([UserName])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Friendships_Users_Friendname]') AND parent_object_id = OBJECT_ID(N'[dbo].[Friendships]'))
ALTER TABLE [dbo].[Friendships] CHECK CONSTRAINT [FK_Friendships_Users_Friendname]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Friendships_Users_Username]') AND parent_object_id = OBJECT_ID(N'[dbo].[Friendships]'))
ALTER TABLE [dbo].[Friendships]  WITH CHECK ADD  CONSTRAINT [FK_Friendships_Users_Username] FOREIGN KEY([UserName])
REFERENCES [dbo].[Users] ([UserName])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Friendships_Users_Username]') AND parent_object_id = OBJECT_ID(N'[dbo].[Friendships]'))
ALTER TABLE [dbo].[Friendships] CHECK CONSTRAINT [FK_Friendships_Users_Username]
GO


