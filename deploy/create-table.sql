USE [Dropbox];


------ Users ------
IF (EXISTS (SELECT 1 
                 FROM INFORMATION_SCHEMA.TABLES 
                 WHERE TABLE_SCHEMA = 'dbo' 
                 AND  TABLE_NAME = 'Users'))
BEGIN
    DROP TABLE [dbo].[Users]
END

CREATE TABLE [dbo].[Users] (
	[UserId] [int] IDENTITY(1,1) NOT NULL,
	[Login] [nvarchar](255) NOT NULL,
	[Password] [nvarchar](255) NOT NULL,
	[RegisterDate] [datetime2](7) NOT NULL,
	CONSTRAINT [PK__Users] PRIMARY KEY CLUSTERED 
	(
		[UserId] ASC
	) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

------ Friendships ------
IF (EXISTS (SELECT 1 
                 FROM INFORMATION_SCHEMA.TABLES 
                 WHERE TABLE_SCHEMA = 'dbo' 
                 AND  TABLE_NAME = 'Friendships'))
BEGIN
    DROP TABLE [dbo].[Friendships]
END

CREATE TABLE [dbo].[Friendships] (
	[FriendshipId] [int] IDENTITY(1,1) NOT NULL,
	[UserId] [int] NOT NULL,
	[FriendId] [int] NOT NULL,
	CONSTRAINT [PK__Friendships] PRIMARY KEY CLUSTERED 
	(
		[FriendshipId] ASC
	) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]


------ Files ------
IF (EXISTS (SELECT 1 
                 FROM INFORMATION_SCHEMA.TABLES 
                 WHERE TABLE_SCHEMA = 'dbo' 
                 AND  TABLE_NAME = 'Files'))
BEGIN
    DROP TABLE [dbo].[Files]
END

CREATE TABLE [dbo].[Files] (
	[FileId] [int] IDENTITY(1,1) NOT NULL,
	[FileName] [nvarchar](255) NOT NULL,
	[FileBytes] [varbinary](MAX) NOT NULL,
	[CreateDate] [datetime2](7) NOT NULL,
	CONSTRAINT [PK__Files] PRIMARY KEY CLUSTERED 
	(
		[FileId] ASC
	) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]


------ Folders ------
IF (EXISTS (SELECT 1 
                 FROM INFORMATION_SCHEMA.TABLES 
                 WHERE TABLE_SCHEMA = 'dbo' 
                 AND  TABLE_NAME = 'Folders'))
BEGIN
    DROP TABLE [dbo].[Folders]
END

CREATE TABLE [dbo].[Folders] (
	[FolderId] [int] IDENTITY(1,1) NOT NULL,
	[FolderName] [nvarchar](255) NOT NULL,
	[IsPrivate] [bit] NOT NULL,
	[CreateDate] [datetime2](7) NOT NULL,
	[UpdateDate] [datetime2](7) NOT NULL,
	CONSTRAINT [PK__Folders] PRIMARY KEY CLUSTERED 
	(
		[FolderId] ASC
	) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]


------ FoldersContents ------
IF (EXISTS (SELECT 1 
                 FROM INFORMATION_SCHEMA.TABLES 
                 WHERE TABLE_SCHEMA = 'dbo' 
                 AND  TABLE_NAME = 'FoldersContents'))
BEGIN
    DROP TABLE [dbo].[FoldersContents]
END

CREATE TABLE [dbo].[FoldersContents] (
	[FolderContentId] [int] IDENTITY(1,1) NOT NULL,
	[FolderId] [int] NOT NULL,
	[FileId] [int] NOT NULL,
	CONSTRAINT [PK__FoldersContents] PRIMARY KEY CLUSTERED 
	(
		[FolderContentId] ASC
	) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

------ UserFiles ------
IF (EXISTS (SELECT 1 
                 FROM INFORMATION_SCHEMA.TABLES 
                 WHERE TABLE_SCHEMA = 'dbo' 
                 AND  TABLE_NAME = 'UserFiles'))
BEGIN
    DROP TABLE [dbo].[UserFiles]
END

CREATE TABLE [dbo].[UserFiles] (
	[UserFileId] [int] IDENTITY(1,1) NOT NULL,
	[UserId] [int] NOT NULL,
	[FileId] [int] NOT NULL,
	[IsPrivate] [bit] NOT NULL,
	CONSTRAINT [PK__UserFiles] PRIMARY KEY CLUSTERED 
	(
		[UserFileId] ASC
	) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

