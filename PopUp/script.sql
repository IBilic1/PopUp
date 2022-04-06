CREATE DATABASE PopUp
GO
USE PopUp

CREATE TABLE Users
(
	IDUser INT PRIMARY KEY IDENTITY,
	FirstName nvarchar(20) not null,
	LastName nvarchar(20) not null,
	BirthDate Date not null,
	Email nvarchar(30) not null,
	Password nvarchar(30) not null,
	Picture varbinary(max) null,
	Active bit null
)
GO

CREATE PROC GetUsers
AS
SELECT * FROM Users
GO

CREATE PROC GetUser
	@idUser INT
AS
SELECT * FROM Users WHERE IDUser=@idUser
GO

CREATE PROC DeleteUser
	@idUser INT
AS
UPDATE Users SET Active=1 WHERE IDUser=@idUser
GO

CREATE PROC RegisterUser
	@firstName NVARCHAR(20),
	@lastName NVARCHAR(20),
	@birthDate Date,
	@email NVARCHAR(30),
	@password NVARCHAR(30),
	@picture VARBINARY(MAX),
	@exists BIT OUTPUT
AS
BEGIN
	-- ak user s tim emailom posotjiou nemoj ga ubacit
	IF NOT EXISTS (SELECT * FROM Users WHERE Email=@email)
	BEGIN
		INSERT INTO Users(FirstName,LastName,BirthDate,Email,Password,Picture,Active)
		VALUES (@firstName,@lastName,@birthDate,@email,@password,@picture,0)
		SET @exists=0
	END
	ELSE
	BEGIN
		SET @exists=1
	END

END
GO

CREATE PROC UpdateUser
	@idUser INT,
	@firstName NVARCHAR(20),
	@lastName NVARCHAR(20),
	@birthDate Date,
	@email NVARCHAR(30),
	@password NVARCHAR(30),
	@picture VARBINARY(MAX)
AS
BEGIN
	UPDATE Users
	SET FirstName=@firstName,LastName=@lastName,BirthDate=@birthDate,Email=@email,Password=@password,Picture=@picture
	WHERE IDUser=@idUser
END
GO

CREATE PROC LogInUser
	@email NVARCHAR(30),
	@password NVARCHAR(30),
	@exists BIT OUTPUT
AS
BEGIN
	IF EXISTS (SELECT * FROM Users WHERE Email=@email AND Password=@password)
	BEGIN
		SELECT * FROM Users
		WHERE Email=@email AND Password=@password
		SET @exists=0
	END
	ELSE
	BEGIN
		SET @exists=1
	END
END
GO

CREATE TABLE Message
(
	IDMessage INT PRIMARY KEY IDENTITY,
	Text NVARCHAR(150) not null,
	DateTime DATETIME not null
)
GO



CREATE TABLE MessageUser
(
	IDMessageUser INT PRIMARY KEY IDENTITY,
	MessageID INT not null,
	SenderID INT not null,
	ReceiverID INT not null
)
GO
CREATE PROC CreateMessage
	@text NVARCHAR(150),
	@dateTime DATETIME,
	@senderID INT,
	@receiverID INT
AS
BEGIN
	DECLARE @IDMESSAGE INT

	INSERT INTO Message 
	VALUES (@text,@dateTime)
	
	SET @IDMESSAGE=SCOPE_IDENTITY()
	
	INSERT INTO MessageUser(MessageID,SenderID,ReceiverID)
	VALUES(@IDMESSAGE,@senderID,@receiverID)
END
GO


CREATE PROC GetMessages
	@senderId INT,
	@receiverId INT
AS
BEGIN
	SELECT *
	FROM Message AS M JOIN MessageUser AS MU ON M.IDMessage=MU.MessageID
	WHERE @SenderId=MU.SenderID AND @receiverId=MU.ReceiverID
END
GO

CREATE PROC GetFriends
	@senderId INT
AS
BEGIN
	SELECT DISTINCT U.*
	FROM Users as U JOIN MessageUser AS MU ON MU.SenderID=U.IDUser
	WHERE U.IDUser=@senderId
END
GO


CREATE TABLE Picture
(
	IDPicture INT PRIMARY KEY IDENTITY,
	Picture varbinary(max) not null,
	DateTime nvarchar(50) not null
)
GO
ALTER TABLE Picture
add DateTime nvarchar(50)
CREATE TABLE PictureUser
(
	IDPictureUser INT PRIMARY KEY IDENTITY,
	PictureID INT not null,
	SenderID INT not null,
	ReceiverID INT not null
)
GO

CREATE PROC GetPictures
	@senderId INT,
	@receiverId INT
AS
BEGIN
	SELECT *
	FROM Picture AS P JOIN PictureUser AS PU ON P.IDPicture=PU.PictureID
	WHERE @SenderId=PU.SenderID AND @receiverId=PU.ReceiverID
END
GO