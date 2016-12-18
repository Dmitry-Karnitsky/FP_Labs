use Dropbox;

delete from FilesShares
delete from Friendships
delete from files
delete from users

declare @i int = 0;
declare @idsTbl table (id nvarchar(255))

while (@i < 10)
begin
	declare @id nvarchar(255) = 'User' + cast(@i as nvarchar(3));
	
	insert into Users(Username, Password)
	values (@id, 'bcrypt+sha512$88b5686afb87c28650350ecc88ec2177$12$5d020c90ebe6cf79e220d56a8664edec35bdb9e9de2ff96f')

	delete from @idsTbl

	declare @ii int = 0;
	while (@ii < 10)
	begin		
		insert into Files(FileName, FileBytes, OwnerName)
		values ('File for user ' + @id, cast('filebytes' as varbinary(max)), @id)

		set @ii = @ii + 1;
	end
	delete from @idsTbl

	set @i = @i + 1;
end

set @i = 0;
while @i < 10
begin
	declare @friendships table (id int)
	declare @files table(id int)

	select top 1 @id = UserName
	from Users  
	order by newid()

	insert into Friendships (UserName, FriendName)
	select top 5
		@id, u.UserName
	from Users u
	where u.UserName != @id
	order by newid()

	insert into @friendships
	select top 3 f.FriendshipId
	from Friendships f
	where f.UserName = @id
	order by newid()

	insert into @files
	select top 5 f.FileId
	from Files f
	where f.OwnerName = @id
	order by newid()

	insert into FilesShares(FileId, FriendshipId)
	select f.id, fr.id
	from @files f
	cross join @friendships fr

	set @i = @i + 1
end