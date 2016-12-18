use Dropbox;

delete from FilesShares
delete from Friendships
delete from files
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
	
	declare @ii int = 0;
	while (@ii < 10)
	begin
		select top 1 @id = id
		from @idsTbl
		order by newid()
		
		insert into Files(FileName, FileBytes, OwnerId)
		values ('File for user ' + (select cast(@id as nvarchar(3))), cast('filebytes' as varbinary(max)), @id)

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

	insert into FilesShares(FileId, FriendshipId)
	select top 1
		 (select top 1 fileid from Files where OwnerId = @id)
		,fs 
	from @sharedTo

	set @i = @i + 1
end
