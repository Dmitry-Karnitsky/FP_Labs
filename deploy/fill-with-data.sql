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
	declare @friendships table (id int)
	declare @files table(id int)

	select top 1 @id = UserId
	from Users  
	order by newid()

	insert into Friendships (UserId, FriendId)
	select top 5
		@id, u.UserId
	from Users u
	where u.UserId != @id
	order by newid()

	insert into @friendships
	select top 3 f.FriendshipId
	from Friendships f
	where f.UserId = @id
	order by newid()

	insert into @files
	select top 5 f.FileId
	from Files f
	where f.OwnerId = @id
	order by newid()

	insert into FilesShares(FileId, FriendshipId)
	select f.id, fr.id
	from @files f
	cross join @friendships fr

	set @i = @i + 1
end
