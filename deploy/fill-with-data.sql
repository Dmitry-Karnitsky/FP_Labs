use Dropbox;

delete from FoldersShares
delete from Friendships
delete from files
delete from folders
delete from users

declare @i int = 0;
declare @idsTbl table (id int)

while (@i < 10)
begin
	insert into Users(Username, Password)
	output inserted.UserId into @idsTbl
	values ('User', 'Password')

	declare @id int = (select id from @idsTbl);
	delete from @idsTbl

	update Users
	set Username = Username + (select cast (@id as nvarchar(3))),
		Password = Password + (select cast (@id as nvarchar(3)))
	where UserId = @id

	insert into Folders(FolderName, IsPrivate, IsRoot, OwnerId)
	output inserted.FolderId into @idsTbl
	values 
		('Root folder for ' + (select cast(@id as nvarchar(3))), 1, 1, @id),
		('Folder for ' + (select cast(@id as nvarchar(3))), 0, 0, @id),
		('Folder for ' + (select cast(@id as nvarchar(3))), 1, 0, @id)
	
	declare @ii int = 0;
	while (@ii < 10)
	begin
		select top 1 @id = id
		from @idsTbl
		order by newid()
		
		insert into Files(FileName, IsPrivate, FileBytes, FolderId)
		values ('File from folder ' + (select cast(@id as nvarchar(3))), @id % 2, cast('filebytes' as varbinary(max)), @id)

		set @ii = @ii + 1;
	end
	delete from @idsTbl

	set @i = @i + 1;
end

set @i = 0;
while @i < 10
begin
	declare @sharedTo table (fs int)

	select top 1 @id = UserId
	from Users  
	order by newid()

	insert into @idsTbl
	select top 3 UserId
	from Users 
	order by newid()

	insert into Friendships(UserId, FriendId)
	output inserted.FriendshipId into @sharedTo
	select @id, id
	from @idsTbl

	insert into FoldersShares(FolderId, FriendshipId)
	select top 1
		 (select top 1 folderid from Folders where OwnerId = @id and IsPrivate = 0)
		,fs 
	from @sharedTo

	set @i = @i + 1
end
