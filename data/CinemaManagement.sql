USE master
go

CREATE DATABASE CinemaManagement
go

USE CinemaManagement
go

/*
- Bảng Employee

1. Tạo Sequence (MySequence):
   - Sequence được tạo với tên là `MySequence`.
   - Nó bắt đầu với giá trị 1 (START WITH 1).
   - Mỗi lần tạo ra một giá trị mới, giá trị sẽ tăng lên một đơn vị (INCREMENT BY 1).

2. Tạo Bảng (Employee):
   - EmployeeID: Trường này được đặt là khóa chính (PRIMARY KEY).
   - Trong đó, trường `EmployeeID` được định nghĩa với giá trị mặc định (DEFAULT) là kết hợp giữa chuỗi 'Emp' và một số 
   nguyên được tạo ra từ Sequence `MySequence`. Để lấy giá trị từ Sequence, sử dụng hàm `NEXT VALUE FOR` kèm 
   theo tên của Sequence (MySequence), sau đó chuyển đổi giá trị này thành một chuỗi ký tự có độ dài 3 và điền vào 
   đuôi của chuỗi 'Emp' bằng cách sử dụng hàm `RIGHT` và chuyển đổi thành kiểu VARCHAR.
*/
go
CREATE SEQUENCE EmployeeSequence
    START WITH 1
    INCREMENT BY 1;
go

go
CREATE TABLE Employee
(
    EmployeeID CHAR(6) PRIMARY KEY DEFAULT ('Emp' + RIGHT('000' + CAST(NEXT VALUE FOR EmployeeSequence AS VARCHAR(3)), 3)),
    FullName NVARCHAR(100) NOT NULL,
    Gender bit NOT NULL,
    DateOfBirth SMALLDATETIME NOT NULL,
    Email VARCHAR(100) NOT NULL,
    PhoneNumber VARCHAR(20) NOT NULL,
    Role NVARCHAR(30) NOT NULL,
    StartingDate SMALLDATETIME NOT NULL,
    Salary MONEY NOT NULL,
    ImageSource VARBINARY(MAX),
	CONSTRAINT CK_Role CHECK (Role in ('Manager', 'Employee'))
);
go
--ảnh thẻ(sẽ dùng ảnh mặc định khi nhân viên vừa vào làm)

go
CREATE SEQUENCE AccountSequence
    START WITH 1
    INCREMENT BY 1;
go

go
CREATE TABLE Account
(
    AccountID CHAR(6) PRIMARY KEY DEFAULT ('Acc' + RIGHT('000' + CAST(NEXT VALUE FOR AccountSequence AS VARCHAR(3)), 3)),
    Username VARCHAR(40) NOT NULL UNIQUE,
    Password VARCHAR(400) NOT NULL,
	EmployeeID CHAR(6) NOT NULL,
	CONSTRAINT FK_Employee FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)
);
go

go
CREATE SEQUENCE CustomerSequence
    START WITH 1
    INCREMENT BY 1;
go

go
CREATE TABLE Customer
(
    CustomerID CHAR(6) PRIMARY KEY DEFAULT ('Cus' + RIGHT('000' + CAST(NEXT VALUE FOR CustomerSequence AS VARCHAR(3)), 3)),
    FullName NVARCHAR(50) NOT NULL,
    Gender NVARCHAR(20) NOT NULL,
    DateOfBirth SMALLDATETIME NOT NULL,
    PhoneNumber VARCHAR(10) NOT NULL UNIQUE,
    Email VARCHAR(50) NOT NULL,
    RegDate SMALLDATETIME NOT NULL
);  
go

go
CREATE SEQUENCE MovieSequence
    START WITH 1
    INCREMENT BY 1;
go

go
CREATE TABLE Movie
(
    MovieID CHAR(6) PRIMARY KEY DEFAULT ('Mov' + RIGHT('000' + CAST(NEXT VALUE FOR MovieSequence AS VARCHAR(3)), 3)),
    MovieName NVARCHAR(100) NOT NULL,
    Description NVARCHAR(500) NOT NULL,
    Genre NVARCHAR(100) NOT NULL,
    Director NVARCHAR(50) NOT NULL,
    Duration INT NOT NULL,
    ReleasedDate SMALLDATETIME NOT NULL,
    Language NVARCHAR(20) NOT NULL,
    Country NVARCHAR(20) NOT NULL,
    Trailer NVARCHAR(200) NOT NULL,
    StartDate SMALLDATETIME NOT NULL,
    Status NVARCHAR(50) NOT NULL,
    ImportPrice MONEY NOT NULL,
    ImageSource VARBINARY(MAX) NOT NULL
);
go

go
CREATE SEQUENCE ProductSequence
    START WITH 1
    INCREMENT BY 1;
go

go
CREATE TABLE Product
(
    ProductID CHAR(6) PRIMARY KEY DEFAULT ('Pro' + RIGHT('000' + CAST(NEXT VALUE FOR ProductSequence AS VARCHAR(3)), 3)),
    ProductName NVARCHAR(100) NOT NULL,
    Price MONEY NULL,
    Quantity INT NOT NULL,
    PurchasePrice MONEY NOT NULL,
    ImageSource VARBINARY(MAX) NOT NULL,
    ProductType CHAR(6) NOT NULL
);
go
-- Giá bán sẽ được tự động set cao hơn giá nhập 20%

go
CREATE SEQUENCE RoomSequence
    START WITH 1
    INCREMENT BY 1;
go

go
CREATE TABLE Room
(
    RoomID CHAR(6) PRIMARY KEY DEFAULT ('Room' + RIGHT('00' + CAST(NEXT VALUE FOR RoomSequence AS VARCHAR(2)), 2)),
    RoomName NVARCHAR(50) NOT NULL,
    NumberOfSeats INT NOT NULL
);
go

--chỉ có 7 phòng 
go
INSERT INTO Room(RoomName, NumberOfSeats)
VALUES
('Room 1',192),
('Room 2',192),
('Room 3',192),
('Room 4',192),
('Room 5',192),
('Room 6',192),
('Room 7',192);
go

go
CREATE SEQUENCE SeatTypeSequence
    START WITH 1
    INCREMENT BY 1;
go

go
CREATE TABLE SeatType
(
    SeatTypeID CHAR(6) PRIMARY KEY DEFAULT ('Type' + RIGHT('00' + CAST(NEXT VALUE FOR SeatTypeSequence AS VARCHAR(2)), 2)),
    SeatTypeName NVARCHAR(50) NOT NULL,
    DescriptionSeat NVARCHAR(500)
);
go

go
INSERT INTO SeatType(SeatTypeName, DescriptionSeat)
VALUES
	('Standard Seat', 'Comfortable seating with ample armrest space.'),
	('VIP Seat', 'Comfortable seating with ample armrest space. Central position with a perfect cinematic experience.'),
	('Sweetbox Seat', 'Comfortable seating with ample armrest space. Private space for couples or friends.');
go

go
CREATE SEQUENCE SeatSequence
    START WITH 1
    INCREMENT BY 1;
go

go
CREATE TABLE Seat (
    SeatID CHAR(6) PRIMARY KEY DEFAULT ('Se' + RIGHT('0000' + CAST(NEXT VALUE FOR SeatSequence AS VARCHAR(4)), 4)),
    SeatLocation NVARCHAR(5),
    RoomID CHAR(6),
    SeatTypeID CHAR(6),
	CONSTRAINT FK_RoomID FOREIGN KEY (RoomID) REFERENCES Room(RoomID),
	CONSTRAINT FK_SeatTypeID FOREIGN KEY (SeatTypeID) REFERENCES SeatType(SeatTypeID)
);
go

-- Phần hỗ trợ add ghế
go
CREATE PROCEDURE sp_generate_seat_for_firstName (@firstName CHAR(1), @RoomID CHAR(6))
AS
BEGIN
    DECLARE @SeatLocation NVARCHAR(5)
    DECLARE @SeatTypeID CHAR(6)
    DECLARE @i INT = 1

    IF @firstName IN ('A', 'B', 'C') -- Nếu hàng là A, B, hoặc C, tạo ghế thường
        SELECT @SeatTypeID = SeatTypeID FROM SeatType WHERE SeatTypeName = 'Standard Seat'
    ELSE IF @firstName = 'M' -- Nếu hàng là M, tạo ghế loại Ghế Sweetbox
        SELECT @SeatTypeID = SeatTypeID FROM SeatType WHERE SeatTypeName = 'Sweetbox Seat'
    ELSE
        SELECT @SeatTypeID = SeatTypeID FROM SeatType WHERE SeatTypeName = 'VIP Seat' -- Ngược lại, tạo ghế VIP

    WHILE (@i <= 16)
    BEGIN
        SET @SeatLocation = @firstName + RIGHT('00' + CAST(@i AS VARCHAR(2)), 2)
                
        INSERT INTO Seat (SeatLocation, RoomID, SeatTypeID)
        VALUES (@SeatLocation, @RoomID, @SeatTypeID)
        
        SET @i = @i + 1
    END
END
GO

go
CREATE PROCEDURE sp_generate_seat_for_room (@RoomID CHAR(6))
AS
BEGIN
    EXECUTE sp_generate_seat_for_firstName 'A', @RoomID
    EXECUTE sp_generate_seat_for_firstName 'B', @RoomID
    EXECUTE sp_generate_seat_for_firstName 'C', @RoomID
    EXECUTE sp_generate_seat_for_firstName 'D', @RoomID
    EXECUTE sp_generate_seat_for_firstName 'E', @RoomID
    EXECUTE sp_generate_seat_for_firstName 'F', @RoomID
    EXECUTE sp_generate_seat_for_firstName 'G', @RoomID
    EXECUTE sp_generate_seat_for_firstName 'H', @RoomID
    EXECUTE sp_generate_seat_for_firstName 'I', @RoomID
    EXECUTE sp_generate_seat_for_firstName 'K', @RoomID
    EXECUTE sp_generate_seat_for_firstName 'L', @RoomID
	EXECUTE sp_generate_seat_for_firstName 'M', @RoomID
END
go

go
EXECUTE sp_generate_seat_for_room 'Room01';
EXECUTE sp_generate_seat_for_room 'Room02';
EXECUTE sp_generate_seat_for_room 'Room03';
EXECUTE sp_generate_seat_for_room 'Room04';
EXECUTE sp_generate_seat_for_room 'Room05';
EXECUTE sp_generate_seat_for_room 'Room06';
EXECUTE sp_generate_seat_for_room 'Room07';
go

go
CREATE SEQUENCE MovieScheduleSequence
    START WITH 1
    INCREMENT BY 1;
go

go
CREATE TABLE MovieSchedule
(
    ScheduleID CHAR(6) PRIMARY KEY DEFAULT ('Sch' + RIGHT('000' + CAST(NEXT VALUE FOR MovieScheduleSequence AS VARCHAR(3)), 3)),
    ScreeningTime SMALLDATETIME NOT NULL, --bao gồm ngày chiếu, giờ chiếu 
    EndTime SMALLDATETIME, --tự động tính từ giờ chiếu và lenght của movie khi add 1 suất chiếu
    MovieID CHAR(6) NOT NULL, --chỉ lấy movie đang phát hành, movie đổi trạng thái thì bỏ ở bảng MovieSchedule
    RoomID CHAR(6) NOT NULL,
    CONSTRAINT FK_RoomID_MovieSchedule FOREIGN KEY (RoomID) REFERENCES Room(RoomID),
    CONSTRAINT FK_MovieID_MovieSchedule FOREIGN KEY (MovieID) REFERENCES Movie(MovieID)
);
go

go
CREATE SEQUENCE TicketSequence
    START WITH 1
    INCREMENT BY 1;
go

go
CREATE TABLE Ticket
(
    TicketID CHAR(6) PRIMARY KEY DEFAULT ('Tic' + RIGHT('000' + CAST(NEXT VALUE FOR TicketSequence AS VARCHAR(3)), 3)),
    TicketPrice MONEY NOT NULL,
    Sold BIT NOT NULL,
    SeatID CHAR(6) NOT NULL,
    ScheduleID CHAR(6) NOT NULL,
    CONSTRAINT FK_SeatID_Ticket FOREIGN KEY (SeatID) REFERENCES Seat(SeatID),
    CONSTRAINT FK_ScheduleID_Ticket FOREIGN KEY (ScheduleID) REFERENCES MovieSchedule(ScheduleID)
);
go

go
CREATE SEQUENCE OrderSequence
    START WITH 1
    INCREMENT BY 1;
go

go
CREATE TABLE [Order]
(
    OrderID CHAR(6) PRIMARY KEY DEFAULT ('Ord' + RIGHT('000' + CAST(NEXT VALUE FOR OrderSequence AS VARCHAR(3)), 3)),
    OrderDate SMALLDATETIME NOT NULL,
    QuantityTicket INT NOT NULL,
    Note NVARCHAR(300),
    Total MONEY NOT NULL,
    CustomerID CHAR(6),
    EmployeeID CHAR(6),
    ScheduleID CHAR(6),
    CONSTRAINT FK_CustomerID_Order FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    CONSTRAINT FK_EmployeeID_Order FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID),
    CONSTRAINT FK_ScheduleID_Order FOREIGN KEY (ScheduleID) REFERENCES MovieSchedule(ScheduleID)
);
go

go
CREATE TABLE OrderDetail
(
    Quantity INT NOT NULL,
    UnitPrice MONEY NOT NULL,
    Total AS (Quantity * UnitPrice),
    OrderID CHAR(6) NOT NULL,
    ProductID CHAR(6) NOT NULL,
	PRIMARY KEY (OrderID, ProductID),
    CONSTRAINT FK_OrderID_OrderDetail FOREIGN KEY (OrderID) REFERENCES [Order](OrderID),
    CONSTRAINT FK_ProductID_OrderDetail FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);
go

--Order nhập 1 phim
go
CREATE SEQUENCE OrderAddMovieSequence
    START WITH 1
    INCREMENT BY 1;
go

go
CREATE TABLE OrderAddMovie
(
    AddMovieID CHAR(6) PRIMARY KEY DEFAULT ('AMo' + RIGHT('000' + CAST(NEXT VALUE FOR OrderAddMovieSequence AS VARCHAR(3)), 3)),
    AddMovieDate SMALLDATETIME NOT NULL,
    Total MONEY NOT NULL,
    MovieID CHAR(6),
    CONSTRAINT FK_MovieID_OrderAddMovie FOREIGN KEY (MovieID) REFERENCES Movie(MovieID)
);
go

--Order nhập 1 product
go
CREATE SEQUENCE OrderAddProductSequence
    START WITH 1
    INCREMENT BY 1;
go

go
CREATE TABLE OrderAddProduct
(
    AddProductID CHAR(6) PRIMARY KEY DEFAULT ('APr' + RIGHT('000' + CAST(NEXT VALUE FOR OrderAddProductSequence AS VARCHAR(3)), 3)),
    AddProductDate SMALLDATETIME NOT NULL,
    Quantity INT NOT NULL,
    UnitPurchasePrice MONEY NOT NULL,
    Total AS (Quantity * UnitPurchasePrice),
    ProductID CHAR(6),
    CONSTRAINT FK_ProductID_OrderAddProduct FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);
go

--Order nhập thêm số lượng product
go
CREATE SEQUENCE OrderImportProductSequence
    START WITH 1
    INCREMENT BY 1;
go

go
CREATE TABLE OrderImportProduct
(
    ImportProductID CHAR(6) PRIMARY KEY DEFAULT ('IPr' + RIGHT('000' + CAST(NEXT VALUE FOR OrderImportProductSequence AS VARCHAR(3)), 3)),
    ImportProductDate SMALLDATETIME NOT NULL,
    Quantity INT NOT NULL,
    UnitPurchasePrice MONEY NOT NULL,
    Total AS (Quantity * UnitPurchasePrice),
    ProductID CHAR(6),
    CONSTRAINT FK_ProductID_OrderImportProduct FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);
go

-- Trigger khi xóa 1 Employee
go
CREATE TRIGGER TriggerDeleteEmployee
ON Employee
INSTEAD OF DELETE
AS
BEGIN
    DECLARE @EmployeeID CHAR(6)

    SELECT @EmployeeID = EmployeeID
    FROM deleted

    DELETE FROM Account
    WHERE EmployeeID  = @EmployeeID

    UPDATE [Order]
    SET EmployeeID = NULL
    WHERE EmployeeID = @EmployeeID

    DELETE FROM Employee
    WHERE EmployeeID = @EmployeeID
END
go

--Trigger xóa 1 Customer
go
CREATE TRIGGER TriggerDeleteCustomer
ON Customer
INSTEAD OF DELETE
AS
BEGIN
    DECLARE @CustomerID CHAR(6)

    SELECT @CustomerID = CustomerID
    FROM deleted

    UPDATE [Order]
    SET CustomerID = NULL
    WHERE CustomerID = @CustomerID

    DELETE FROM Customer
    WHERE CustomerID = @CustomerID
END
go

--Trigger xóa 1 Movie
go
CREATE TRIGGER TriggerDeleteMovie
ON Movie
INSTEAD OF DELETE
AS
BEGIN
    DECLARE @MovieID CHAR(6)

    SELECT @MovieID = MovieID
    FROM deleted

    UPDATE OrderAddMovie
    SET MovieID = NULL
    WHERE MovieID = @MovieID

    DELETE FROM MovieSchedule
    WHERE MovieID = @MovieID

    DELETE FROM Movie
    WHERE MovieID = @MovieID
END
go

-- Trigger tự set giá sau mỗi lần update, insert: giá bán = giá nhập + giá nhập * 20% = 120% * giá nhập
go
CREATE TRIGGER TriggerSetPriceProduct
ON Product
AFTER INSERT
AS
BEGIN
    DECLARE @ProductID CHAR(6)
    DECLARE @PurchasePrice MONEY

    SELECT @ProductID = ProductID, @PurchasePrice = PurchasePrice
    FROM inserted

    UPDATE Product
    SET Price = @PurchasePrice * 1.2
    WHERE ProductID = @ProductID
END
go

-- Trigger tự động add Order add Product
go
CREATE TRIGGER TriggerAutoAddOrderAddProduct
ON Product
AFTER INSERT
AS
BEGIN
    DECLARE @ProductID CHAR(6)
    DECLARE @Quantity INT
    DECLARE @OrderDate SMALLDATETIME
    DECLARE @UnitPurchasePrice MONEY

    SELECT @ProductID = ProductID, @Quantity = Quantity, @UnitPurchasePrice = PurchasePrice
    FROM inserted

    SET @OrderDate = GETDATE()

    INSERT INTO OrderAddProduct (ProductID, AddProductDate, Quantity, UnitPurchasePrice)
    VALUES (@ProductID, @OrderDate, @Quantity, @UnitPurchasePrice)
END
go

-- Trigger tự add Order import product
go
CREATE TRIGGER TriggerAutoAddOrderImportProduct
ON Product
AFTER UPDATE
AS
BEGIN
    DECLARE @ProductID CHAR(6)
    DECLARE @QuantityChange INT
    DECLARE @UnitPurchasePrice MONEY
    DECLARE @OldQuantity INT

    SELECT @ProductID = ProductID, @OldQuantity = Quantity
    FROM deleted

    SELECT @QuantityChange = Quantity, @UnitPurchasePrice = PurchasePrice
    FROM inserted

    SET @QuantityChange = @QuantityChange - @OldQuantity

    IF @QuantityChange != 0
    BEGIN
        INSERT INTO OrderImportProduct (ProductID, ImportProductDate, Quantity, UnitPurchasePrice)
        VALUES (@ProductID, GETDATE(), @QuantityChange, @UnitPurchasePrice)
    END
END
go

-- Trigger xóa 1 Product
go
CREATE TRIGGER TriggerDeleteProduct
ON Product
AFTER DELETE
AS
BEGIN
    DECLARE @ProductID CHAR(6)

    SELECT @ProductID = ProductID
    FROM deleted

    UPDATE OrderImportProduct
    SET ProductID = NULL
    WHERE ProductID = @ProductID

    UPDATE OrderDetail
    SET ProductID = NULL
    WHERE ProductID = @ProductID

    UPDATE OrderAddProduct
    SET ProductID = NULL
    WHERE ProductID = @ProductID

    DELETE FROM Product
    WHERE ProductID = @ProductID
END
go

-- Trigger khi insert 1 MovieSchedule thì tạo dữ liệu cho bảng Ticket
go
CREATE TRIGGER TriggerCreateTicketByMovieScheduleInsert
ON MovieSchedule
AFTER INSERT
AS
BEGIN
    DECLARE @ScheduleID CHAR(6)
    DECLARE @RoomID CHAR(6)

    SELECT @ScheduleID = ScheduleID, @RoomID = RoomID
    FROM inserted

    INSERT INTO Ticket (SeatID, ScheduleID, Sold)
    SELECT SeatID, @ScheduleID, 0
    FROM Seat
    WHERE RoomID = @RoomID
END
go

-- Trigger xóa 1 MovieSchedule
go
CREATE TRIGGER TriggerDeleteMovieSchedule
ON MovieSchedule
INSTEAD OF DELETE
AS
BEGIN
    DECLARE @ScheduleID CHAR(6)

    SELECT @ScheduleID = ScheduleID
    FROM deleted

    DELETE FROM Ticket
    WHERE ScheduleID = @ScheduleID

    UPDATE [Order]
    SET ScheduleID = NULL
    WHERE ScheduleID = @ScheduleID

    DELETE FROM MovieSchedule
    WHERE ScheduleID = @ScheduleID
END
go

-- Trigger cập nhật OrderAddMovie khi chỉnh sửa Movie
go
CREATE TRIGGER TriggerUpdateOrderAddMovieUpdate
ON Movie
AFTER UPDATE
AS
BEGIN
    DECLARE @MovieID CHAR(6)
    DECLARE @ImportPrice MONEY

    SELECT @MovieID = MovieID, @ImportPrice = ImportPrice
    FROM inserted

    UPDATE OrderAddMovie
    SET Total = @ImportPrice
    WHERE MovieID = @MovieID
END
go

-- Create a function to retrieve login permissions
CREATE FUNCTION Emp_Acc(@username VARCHAR(40))
RETURNS TABLE
AS
RETURN
SELECT Role
FROM Employee e JOIN Account a 
ON e.EmployeeID = a.EmployeeID
WHERE a.Username = @username;
go

-- Thêm dữ liệu cho Movie
INSERT INTO Movie (MovieName, Description, Genre, Director, Duration, ReleasedDate, Language, Country, Trailer, StartDate, Status, ImportPrice, ImageSource)
VALUES
('The Shawshank Redemption', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.', 'Drama', 'Frank Darabont', 142, '1994-09-23', 'English', 'USA', 'https://www.youtube.com/watch?v=6hB3S9bIaco', '1994-09-23', 'Active', 10.99, 0x),
('The Godfather', 'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.', 'Crime', 'Francis Ford Coppola', 175, '1972-03-24', 'English', 'USA', 'https://www.youtube.com/watch?v=5DO-nDW43Ik', '1972-03-24', 'Active', 12.99, 0x),
('The Dark Knight', 'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.', 'Action', 'Christopher Nolan', 152, '2008-07-18', 'English', 'USA', 'https://www.youtube.com/watch?v=EXeTwQWrcwY', '2008-07-18', 'Active', 14.99, 0x),
('Pulp Fiction', 'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.', 'Crime', 'Quentin Tarantino', 154, '1994-10-14', 'English', 'USA', 'https://www.youtube.com/watch?v=s7EdQ4FqbhY', '1994-10-14', 'Active', 11.99, 0x),
('Schindler''s List', 'In German-occupied Poland during World War II, industrialist Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution by the Nazis.', 'Biography', 'Steven Spielberg', 195, '1994-02-04', 'English', 'USA', 'https://www.youtube.com/watch?v=gG22XNhtnoY', '1994-02-04', 'Active', 13.99, 0x),
('The Lord of the Rings: The Return of the King', 'Gandalf and Aragorn lead the World of Men against Sauron''s army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.', 'Adventure', 'Peter Jackson', 201, '2003-12-17', 'English', 'New Zealand', 'https://www.youtube.com/watch?v=r5X-hFf6Bwo', '2003-12-17', 'Active', 16.99, 0x),
('Fight Club', 'An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into much more.', 'Drama', 'David Fincher', 139, '1999-10-15', 'English', 'USA', 'https://www.youtube.com/watch?v=SUXWAEX2jlg', '1999-10-15', 'Active', 10.49, 0x),
('Forrest Gump', 'The presidencies of Kennedy and Johnson, the Vietnam War, the Watergate scandal and other historical events unfold from the perspective of an Alabama man with an IQ of 75, whose only desire is to be reunited with his childhood sweetheart.', 'Drama', 'Robert Zemeckis', 142, '1994-07-06', 'English', 'USA', 'https://www.youtube.com/watch?v=uPIEn0M8su0', '1994-07-06', 'Active', 9.99, 0x),
('Inception', 'A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.', 'Action', 'Christopher Nolan', 148, '2010-07-16', 'English', 'USA', 'https://www.youtube.com/watch?v=YoHD9XEInc0', '2010-07-16', 'Active', 15.49, 0x),
('The Matrix', 'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.', 'Action', 'Lana Wachowski, Lilly Wachowski', 136, '1999-03-31', 'English', 'USA', 'https://www.youtube.com/watch?v=vKQi3bBA1y8', '1999-03-31', 'Active', 11.99, 0x);
go

-- Thêm dữ liệu cho Employee và Account
INSERT INTO Employee (FullName, DateOfBirth, Gender, Email, PhoneNumber, Salary, Role, StartingDate)
VALUES ('admin', '2004-08-10', 1, 'lathanhsi100804@gmail.com', '0398999999', 0, 'Manager', GETDATE());
go

INSERT INTO Account (Username, Password, EmployeeID)
VALUES ('admin', '$2a$10$TBId43wSoxr9Itgr.g8R0u2XZbuY7o98yBBCO6LqgqTSHj/HWYiqG', 'Emp001');
go

INSERT INTO Employee (FullName, DateOfBirth, Gender, Email, PhoneNumber, Salary, Role, StartingDate)
VALUES ('thanhsi108', '2004-08-10', 1, 'lathanhsi100804@gmail.com', '0398999999', 0, 'Employee', GETDATE());
go

INSERT INTO Account (Username, Password, EmployeeID)
VALUES ('thanhsi108', '$2a$10$Wk3Bw8CxJfhy0an/lZTlxeQOMj5h7x0HFmatxJNLbcXJ7PRdjI1Fm', 'Emp002');
go
