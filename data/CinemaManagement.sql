/*
USE Master
go

CREATE DATABASE CinemaManagement
go

USE CinemaManagement
go
*/

/*
1. Tạo Sequence (MySequence):
   - Sequence được tạo với tên là `MySequence`.
   - Nó bắt đầu với giá trị 1 (START WITH 1).
   - Mỗi lần tạo ra một giá trị mới, giá trị sẽ tăng lên một đơn vị (INCREMENT BY 1).

2. Tạo Bảng (MyTable):
   - MyTableID: Trường này được đặt là khóa chính (PRIMARY KEY).
   - Trong đó, trường `MyTableID` được định nghĩa với giá trị mặc định (DEFAULT) là kết hợp giữa chuỗi 'MyT' và một số 
   nguyên được tạo ra từ Sequence `MySequence`. Để lấy giá trị từ Sequence, sử dụng hàm `NEXT VALUE FOR` kèm 
   theo tên của Sequence (MySequence), sau đó chuyển đổi giá trị này thành một chuỗi ký tự có độ dài 3 và điền vào 
   đuôi của chuỗi 'MyT' bằng cách sử dụng hàm `RIGHT` và chuyển đổi thành kiểu VARCHAR.
*/

CREATE SEQUENCE EmployeeSequence
    START WITH 1
    INCREMENT BY 1;
go

CREATE TABLE Employee
(
    EmployeeID CHAR(6) PRIMARY KEY DEFAULT ('Emp' + RIGHT('000' + CAST(NEXT VALUE FOR EmployeeSequence AS VARCHAR(3)), 3)),
    FullName NVARCHAR(100) NOT NULL,
    Gender bit NOT NULL,
    DateOfBirth SMALLDATETIME NOT NULL,
    Email VARCHAR(100) NOT NULL,
    PhoneNumber CHAR(10) NOT NULL UNIQUE,
    Role NVARCHAR(30) NOT NULL,
			CONSTRAINT CK_Role CHECK (Role in ('Manager', 'Employee')),
    StartingDate SMALLDATETIME NOT NULL,
    Salary MONEY NOT NULL,
    ImageSource NVARCHAR(100)
);
go
--ảnh thẻ(sẽ dùng ảnh mặc định khi nhân viên vừa vào làm)

CREATE SEQUENCE AccountSequence
    START WITH 1
    INCREMENT BY 1;
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

CREATE SEQUENCE CustomerSequence
    START WITH 1
    INCREMENT BY 1;
go

CREATE TABLE Customer
(
    CustomerID CHAR(7) PRIMARY KEY DEFAULT ('Cus' + RIGHT('0000' + CAST(NEXT VALUE FOR CustomerSequence AS VARCHAR(4)), 4)),
    FullName NVARCHAR(50) NOT NULL,
    PhoneNumber CHAR(10) NOT NULL UNIQUE,
    Email VARCHAR(50) NOT NULL,
    RegDate SMALLDATETIME NOT NULL
);  
go

CREATE SEQUENCE MovieSequence
    START WITH 1
    INCREMENT BY 1;
go

CREATE TABLE Movie
(
    MovieID CHAR(6) PRIMARY KEY DEFAULT ('Mov' + RIGHT('000' + CAST(NEXT VALUE FOR MovieSequence AS VARCHAR(3)), 3)),
    MovieName NVARCHAR(100) NOT NULL,
    Genre NVARCHAR(100) NOT NULL,
    Director NVARCHAR(50) NOT NULL,
    Duration INT NOT NULL,
    ReleasedDate SMALLDATETIME NOT NULL,
    Language NVARCHAR(20) NOT NULL,
    Country NVARCHAR(20) NOT NULL,
    StartDate SMALLDATETIME NOT NULL,
    Status NVARCHAR(50) NOT NULL
        CONSTRAINT CK_Status CHECK (Status IN ('Released', 'Unreleased')),
    ImportPrice MONEY NOT NULL,
    ImageSource NVARCHAR(100) NOT NULL,
	Trailer NVARCHAR(200) NOT NULL,
	Description NVARCHAR(500) NOT NULL
);
go

CREATE SEQUENCE ProductSequence
    START WITH 1
    INCREMENT BY 1;
go

CREATE TABLE Product
(
    ProductID CHAR(6) PRIMARY KEY DEFAULT ('Pro' + RIGHT('000' + CAST(NEXT VALUE FOR ProductSequence AS VARCHAR(3)), 3)),
    ProductName NVARCHAR(100) NOT NULL,
    Price MONEY NULL,
    Quantity INT NOT NULL,
    PurchasePrice MONEY NOT NULL,
    ImageSource NVARCHAR(100) NOT NULL,
    ProductType CHAR(6) NOT NULL
			CONSTRAINT CK_ProductType CHECK (ProductType in ('Drink', 'Food'))	
);
go
-- Giá bán sẽ được tự động set cao hơn giá nhập 100%

CREATE SEQUENCE RoomSequence
    START WITH 1
    INCREMENT BY 1;
go

CREATE TABLE Room
(
    RoomID CHAR(6) PRIMARY KEY DEFAULT ('Room' + RIGHT('00' + CAST(NEXT VALUE FOR RoomSequence AS VARCHAR(2)), 2)),
    RoomName NVARCHAR(50) NOT NULL UNIQUE,
    NumberOfSeats INT NOT NULL
);
go

--chỉ có 7 phòng 
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

CREATE SEQUENCE SeatTypeSequence
    START WITH 1
    INCREMENT BY 1;
go

CREATE TABLE SeatType
(
    SeatTypeID CHAR(6) PRIMARY KEY DEFAULT ('Type' + RIGHT('00' + CAST(NEXT VALUE FOR SeatTypeSequence AS VARCHAR(2)), 2)),
    SeatTypeName NVARCHAR(50) NOT NULL,
    DescriptionSeat NVARCHAR(500)
);
go

INSERT INTO SeatType(SeatTypeName, DescriptionSeat)
VALUES
	('Standard Seat', 'Comfortable seating with ample armrest space.'),
	('VIP Seat', 'Comfortable seating with ample armrest space. Central position with a perfect cinematic experience.'),
	('Sweetbox Seat', 'Comfortable seating with ample armrest space. Private space for couples or friends.');
go

CREATE SEQUENCE SeatSequence
    START WITH 1
    INCREMENT BY 1;
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

EXECUTE sp_generate_seat_for_room 'Room01';
EXECUTE sp_generate_seat_for_room 'Room02';
EXECUTE sp_generate_seat_for_room 'Room03';
EXECUTE sp_generate_seat_for_room 'Room04';
EXECUTE sp_generate_seat_for_room 'Room05';
EXECUTE sp_generate_seat_for_room 'Room06';
EXECUTE sp_generate_seat_for_room 'Room07';
go

CREATE SEQUENCE MovieScheduleSequence
    START WITH 1
    INCREMENT BY 1;
go

CREATE TABLE MovieSchedule
(
    ScheduleID CHAR(8) PRIMARY KEY DEFAULT ('Sch' + RIGHT('00000' + CAST(NEXT VALUE FOR MovieScheduleSequence AS VARCHAR(5)), 5)),
    ScreeningTime SMALLDATETIME NOT NULL,
    EndTime SMALLDATETIME,
	PricePerSeat MONEY NOT NULL,
    MovieID CHAR(6) NOT NULL,
    RoomID CHAR(6) NOT NULL,
    CONSTRAINT FK_RoomID_MovieSchedule FOREIGN KEY (RoomID) REFERENCES Room(RoomID),
    CONSTRAINT FK_MovieID_MovieSchedule FOREIGN KEY (MovieID) REFERENCES Movie(MovieID)
);
go

CREATE SEQUENCE MovieScheduleSeatSequence
    START WITH 1
    INCREMENT BY 1;
go

CREATE TABLE MovieScheduleSeat
(
    Sold BIT NOT NULL,
    SeatID CHAR(6) NOT NULL,
    ScheduleID CHAR(8) NOT NULL,
	CONSTRAINT PK_MovieScheduleSeat PRIMARY KEY (SeatID, ScheduleID),
    CONSTRAINT FK_SeatID_MovieScheduleSeat FOREIGN KEY (SeatID) REFERENCES Seat(SeatID),
    CONSTRAINT FK_ScheduleID_MovieScheduleSeat FOREIGN KEY (ScheduleID) REFERENCES MovieSchedule(ScheduleID)
);
go

CREATE SEQUENCE OrderSequence
    START WITH 1
    INCREMENT BY 1;
go

CREATE TABLE [Order]
(
    OrderID CHAR(7) PRIMARY KEY DEFAULT ('Ord' + RIGHT('0000' + CAST(NEXT VALUE FOR OrderSequence AS VARCHAR(4)), 4)),
    OrderDate SMALLDATETIME NOT NULL,
    QuantitySeat INT NOT NULL,
    Note NVARCHAR(300),
    Total MONEY,
    CustomerID CHAR(7),
    EmployeeID CHAR(6),
    ScheduleID CHAR(8),
    CONSTRAINT FK_CustomerID_Order FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    CONSTRAINT FK_EmployeeID_Order FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID),
    CONSTRAINT FK_ScheduleID_Order FOREIGN KEY (ScheduleID) REFERENCES MovieSchedule(ScheduleID)
);
go

CREATE TABLE OrderDetail
(
    Quantity INT NOT NULL,
    LineTotal MONEY,
    OrderID CHAR(7) NOT NULL,
    ProductID CHAR(6) NOT NULL,
	PRIMARY KEY (OrderID, ProductID),
    CONSTRAINT FK_OrderID_OrderDetail FOREIGN KEY (OrderID) REFERENCES [Order](OrderID),
    CONSTRAINT FK_ProductID_OrderDetail FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);
go

--Order nhập 1 phim
CREATE SEQUENCE OrderAddMovieSequence
    START WITH 1
    INCREMENT BY 1;
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
CREATE SEQUENCE OrderAddProductSequence
    START WITH 1
    INCREMENT BY 1;
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
CREATE SEQUENCE OrderImportProductSequence
    START WITH 1
    INCREMENT BY 1;
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
CREATE OR ALTER TRIGGER TriggerDeleteEmployee
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
CREATE OR ALTER TRIGGER TriggerDeleteCustomer
ON Customer
INSTEAD OF DELETE
AS
BEGIN
    DECLARE @CustomerID CHAR(7)

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
CREATE OR ALTER TRIGGER TriggerDeleteMovie
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

CREATE OR ALTER TRIGGER TriggerSetPriceProduct
ON Product
AFTER INSERT, UPDATE
AS
BEGIN
    IF (UPDATE(PurchasePrice))
    BEGIN
        UPDATE p
        SET Price = i.PurchasePrice * 2
        FROM Product p
        INNER JOIN inserted i ON p.ProductID = i.ProductID
    END
END
go

-- Trigger tự động add OrderAddProduct
CREATE OR ALTER TRIGGER TriggerAutoAddOrderAddProduct
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

-- Trigger tự add OrderImportProduct
CREATE OR ALTER TRIGGER TriggerAutoAddOrderImportProduct
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
CREATE OR ALTER TRIGGER TriggerDeleteProduct
ON Product
INSTEAD OF DELETE
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

-- Trigger khi insert 1 MovieSchedule thì tạo dữ liệu cho bảng MovieScheduleSeat
CREATE OR ALTER TRIGGER TriggerCreateMovieScheduleSeatByMovieScheduleInsert
ON MovieSchedule
AFTER INSERT
AS
BEGIN
    DECLARE @ScheduleID CHAR(8)
    DECLARE @RoomID CHAR(6)

    SELECT @ScheduleID = ScheduleID, @RoomID = RoomID
    FROM inserted

    INSERT INTO MovieScheduleSeat (SeatID, ScheduleID, Sold)
    SELECT SeatID, @ScheduleID, 0
    FROM Seat
    WHERE RoomID = @RoomID
END
go

-- Trigger xóa 1 MovieSchedule
CREATE OR ALTER TRIGGER TriggerDeleteMovieSchedule
ON MovieSchedule
INSTEAD OF DELETE
AS
BEGIN
    DECLARE @ScheduleID CHAR(8)

    SELECT @ScheduleID = ScheduleID
    FROM deleted

    DELETE FROM MovieScheduleSeat
    WHERE ScheduleID = @ScheduleID

    UPDATE [Order]
    SET ScheduleID = NULL
    WHERE ScheduleID = @ScheduleID

    DELETE FROM MovieSchedule
    WHERE ScheduleID = @ScheduleID
END
go

-- Trigger cập nhật OrderAddMovie
CREATE OR ALTER TRIGGER TriggerUpdateOrderAddMovieUpdate
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

-- -- Trigger thêm OrderAddMovie
CREATE OR ALTER TRIGGER TriggerAddOrderAddMovie
ON Movie
AFTER INSERT
AS
BEGIN
	DECLARE @MovieID CHAR(6)
    DECLARE @ImportPrice MONEY

    SELECT @MovieID = MovieID, @ImportPrice = ImportPrice
    FROM inserted

    INSERT INTO OrderAddMovie (AddMovieDate, Total, MovieID)
    VALUES (GETDATE(), @ImportPrice, @MovieID)
END
GO

CREATE OR ALTER FUNCTION getEmployeeByAccount(@user VARCHAR(40))
RETURNS TABLE
AS
RETURN
SELECT e.*
FROM Employee e JOIN Account a 
ON e.EmployeeID = a.EmployeeID
WHERE a.Username = @user;
go

CREATE OR ALTER FUNCTION fn_CustomerSpendingStats (@Year INT, @Month INT = NULL)
RETURNS @StatsTable TABLE (
    CustomerID CHAR(7),
    CustomerName NVARCHAR(50),
    PhoneNumber VARCHAR(10),
    TotalSpending MONEY
)
AS
BEGIN
    INSERT INTO @StatsTable
    SELECT o.CustomerID, c.FullName, c.PhoneNumber, SUM(o.Total) AS TotalSpending
    FROM [Order] o
    JOIN Customer c ON o.CustomerID = c.CustomerID
    WHERE YEAR(o.OrderDate) = @Year
        AND (@Month IS NULL OR MONTH(o.OrderDate) = @Month)
    GROUP BY o.CustomerID, c.FullName, c.PhoneNumber
    RETURN;
END;
go


CREATE OR ALTER TRIGGER CalculateEndTime
ON MovieSchedule
AFTER INSERT, UPDATE
AS
BEGIN
    DECLARE @ScheduleID CHAR(8);
    DECLARE @MovieDuration INT;
    DECLARE @ScreeningTime SMALLDATETIME;
    DECLARE @EndTime SMALLDATETIME;

    SELECT @ScheduleID = ScheduleID, @MovieDuration = m.Duration, @ScreeningTime = i.ScreeningTime
    FROM inserted i INNER JOIN Movie m 
	ON i.MovieID = m.MovieID;

    SET @EndTime = DATEADD(MINUTE, @MovieDuration, @ScreeningTime);
    UPDATE MovieSchedule
    SET EndTime = @EndTime
    WHERE ScheduleID = @ScheduleID;
END;
go

CREATE OR ALTER TRIGGER CheckRoomAvailability
ON MovieSchedule
INSTEAD OF INSERT
AS
BEGIN
    DECLARE @NewScreeningTime SMALLDATETIME;
    DECLARE @NewEndTime SMALLDATETIME;
    DECLARE @RoomID CHAR(6);

    SELECT @NewScreeningTime = DATEADD(MINUTE, -30, i.ScreeningTime),
           @NewEndTime = DATEADD(MINUTE, m.Duration + 30, i.ScreeningTime),
           @RoomID = i.RoomID
    FROM inserted i INNER JOIN Movie m 
	ON i.MovieID = m.MovieID;

    IF EXISTS (
      SELECT 1
        FROM MovieSchedule ms
        WHERE ms.RoomID = @RoomID
        AND ((ms.EndTime > @NewScreeningTime AND ms.EndTime < @NewEndTime) OR (ms.ScreeningTime > @NewScreeningTime AND ms.ScreeningTime < @NewEndTime))
    )
    BEGIN
        RAISERROR('Room is not available!!! {CREATE BY THANHSIX108}', 16, 1);
        ROLLBACK TRANSACTION;
    END;
    ELSE
    BEGIN
        INSERT INTO MovieSchedule (ScreeningTime, MovieID, RoomID, PricePerSeat)
        SELECT ScreeningTime, MovieID, RoomID, PricePerSeat
        FROM inserted;
    END;
END;
go

CREATE OR ALTER PROCEDURE UpdateProductQuantityByID
    @ProductID CHAR(6),
    @Quantity INT
AS
BEGIN
    DECLARE @UpdatedQuantity INT;

    SELECT @UpdatedQuantity = Quantity
    FROM Product
    WHERE ProductID = @ProductID;

    SET @UpdatedQuantity = @UpdatedQuantity + @Quantity;

    UPDATE Product
    SET Quantity = @UpdatedQuantity
    WHERE ProductID = @ProductID;
END;
go

CREATE OR ALTER PROCEDURE AddNewProduct
    @ProductName NVARCHAR(100),
    @Quantity INT,
    @PurchasePrice MONEY,
    @ImageSource NVARCHAR(100),
    @ProductType CHAR(6),
    @ProductID CHAR(6) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @InsertedProductID CHAR(6);

    INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
    VALUES (@ProductName, @Quantity, @PurchasePrice, @ImageSource, @ProductType);

    SET @InsertedProductID = (SELECT TOP 1 ProductID FROM Product WHERE @@ROWCOUNT > 0 ORDER BY ProductID DESC);

    SET @ProductID = @InsertedProductID;
END;
go

CREATE OR ALTER FUNCTION fn_ProductSalesByYearMonth (@Year INT, @Month INT = NULL)
RETURNS @ProductSales TABLE (
    ProductName NVARCHAR(MAX),
    TotalQuantity INT,
    TotalPrice MONEY)
AS
BEGIN
    INSERT INTO @ProductSales
    SELECT p.ProductName, 
           SUM(od.Quantity) AS TotalQuantity,
           SUM(od.Quantity) * p.Price AS TotalPrice
    FROM [dbo].[OrderDetail] od 
    JOIN [dbo].[Product] p ON od.ProductID = p.ProductID
    JOIN [dbo].[Order] o ON o.OrderID = od.OrderID
    WHERE YEAR(o.OrderDate) = @Year
      AND (@Month IS NULL OR MONTH(o.OrderDate) = @Month)
    GROUP BY p.ProductName, p.Price;
    
    RETURN;
END;
go

CREATE OR ALTER FUNCTION fn_MovieSalesByYearMonth (@Year INT, @Month INT = NULL
)
RETURNS @MovieSales TABLE (
    MovieID CHAR(6),
    MovieName NVARCHAR(100),
    ViewTotal INT,
    Total MONEY)
AS
BEGIN
    INSERT INTO @MovieSales
    SELECT
        m.MovieID,
        m.MovieName,
        SUM(o.QuantitySeat) AS ViewTotal,
        SUM(o.QuantitySeat * ms.PricePerSeat) AS Total
    FROM
        [dbo].[Order] o
    JOIN
        [dbo].[MovieSchedule] ms ON o.ScheduleID = ms.ScheduleID
    JOIN
        [dbo].[Movie] m ON ms.MovieID = m.MovieID
    WHERE
        YEAR(o.OrderDate) = @Year
        AND (@Month IS NULL OR MONTH(o.OrderDate) = @Month)
    GROUP BY
        m.MovieID,
        m.MovieName
    ORDER BY
        m.MovieID;
    
    RETURN;
END;
go

CREATE OR ALTER TRIGGER CalculateLineTotal
ON OrderDetail
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS (SELECT * FROM inserted)
    BEGIN
        UPDATE od
        SET LineTotal = p.Price * i.Quantity
        FROM OrderDetail od
        INNER JOIN inserted i ON od.OrderID = i.OrderID AND od.ProductID = i.ProductID
        INNER JOIN Product p ON i.ProductID = p.ProductID;
    END
END;
go

CREATE OR ALTER FUNCTION fn_CalculateTotalRevenueByMonthYear (@year INT, @month INT)
RETURNS MONEY
AS
BEGIN
    DECLARE @totalAddProduct MONEY;
    DECLARE @totalImportProduct MONEY;
    DECLARE @totalAddMovie MONEY;
    DECLARE @totalDoanhThu MONEY;

    SELECT @totalAddProduct = SUM(Total)
    FROM OrderAddProduct
	WHERE YEAR(AddProductDate) = @year
		AND (@month IS NULL OR MONTH(AddProductDate) = @month);

    SELECT @totalImportProduct = SUM(Total)
    FROM OrderImportProduct
	WHERE YEAR(ImportProductDate) = @year
		AND (@month IS NULL OR MONTH(ImportProductDate) = @month);

    SELECT @totalAddMovie = SUM(Total)
    FROM [dbo].[OrderAddMovie]
	WHERE YEAR(AddMovieDate) = @year
		AND (@month IS NULL OR MONTH(AddMovieDate) = @month);

    SET @totalDoanhThu = ISNULL(@totalAddProduct, 0) + ISNULL(@totalImportProduct, 0) + ISNULL(@totalAddMovie, 0);

    RETURN @totalDoanhThu;
END;
go

CREATE OR ALTER FUNCTION fn_CountCustomersPerHour (@Year INT, @Month INT, @Day INT)
RETURNS @HourlyCounts TABLE (Hour INT, NumberOfCustomers INT)
AS
BEGIN
    WITH Hours AS (SELECT 0 AS Hour UNION ALL SELECT Hour + 1 FROM Hours WHERE Hour < 23)
    INSERT INTO @HourlyCounts
    SELECT h.Hour, COUNT(o.CustomerID) AS NumberOfCustomers 
	FROM Hours h  LEFT JOIN [Order] o 
	ON DATEPART(hour, o.OrderDate) = h.Hour 
	AND YEAR(o.OrderDate) = @Year AND MONTH(o.OrderDate) = @Month AND DAY(o.OrderDate) = @Day
    GROUP BY h.Hour
    ORDER BY h.Hour;

    RETURN;
END;
go

CREATE OR ALTER TRIGGER CalculateOrderTotal
ON [Order]
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE o
    SET o.Total = ISNULL(o.QuantitySeat, 0) * ISNULL(ms.PricePerSeat, 0) + ISNULL((SELECT SUM(od.LineTotal) FROM OrderDetail od WHERE od.OrderID = o.OrderID), 0)
    FROM [Order] o
    INNER JOIN inserted i ON o.OrderID = i.OrderID
    LEFT JOIN MovieSchedule ms ON o.ScheduleID = ms.ScheduleID;
END;
go


CREATE OR ALTER TRIGGER CalculateOrderTotalFromOrderDetail
ON OrderDetail
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE [Order]
    SET Total = 0
    FROM [dbo].[Order]
END;

-- Thêm dữ liệu cho bảng Employee
INSERT INTO Employee (FullName, Gender, DateOfBirth, Email, PhoneNumber, Role, StartingDate, Salary, ImageSource)
VALUES
('admin', 1, '2004-08-10', 'lathanhsi100804@gmail.com', '0398999999', 'Manager', GETDATE(), 0, 'images/profile.png'),
(N'Thanh Sĩ', 1, '2004-08-10', 'lathanhsi100804@gmail.com', '0398888888', 'Employee', GETDATE(), 0, 'images/thanhsi108.png'),
('Tai Lionel', 1, '2003-10-27', 'tailionel@example.com', '1234567899', 'Employee', GETDATE(), 0, NULL),
('Alice Smith', 0, '1995-05-15', 'alicesmith@example.com', '9876543210', 'Employee', '2024-04-30', 1800, NULL),
('Bob Johnson', 1, '1988-11-20', 'bjohnson@example.com', '1357924680', 'Employee', '2024-04-30', 2200, NULL),
('Emily Davis', 0, '1993-09-10', 'emilydavis@example.com', '2468013579', 'Employee', '2024-04-30', 1900, NULL),
('Michael Wilson', 1, '1992-04-05', 'mwilson@example.com', '0123456789', 'Employee', '2024-04-30', 2100, NULL),
('Jennifer Brown', 0, '1985-12-25', 'jbrown@example.com', '9870123456', 'Employee', '2024-04-30', 2300, NULL),
('David Miller', 1, '1998-07-30', 'dmiller@example.com', '9876543211', 'Employee', '2024-04-30', 2000, NULL),
('Sarah Anderson', 0, '1994-08-10', 'sanderson@example.com', '0123456788', 'Employee', '2024-04-30', 1900, NULL);
go

-- Thêm dữ liệu cho bảng Account
INSERT INTO Account (Username, Password, EmployeeID)
VALUES 
('admin', '$2a$10$TBId43wSoxr9Itgr.g8R0u2XZbuY7o98yBBCO6LqgqTSHj/HWYiqG', 'Emp001'), 
('thanhsi108', '$2a$10$Wk3Bw8CxJfhy0an/lZTlxeQOMj5h7x0HFmatxJNLbcXJ7PRdjI1Fm', 'Emp002'),
('tailionel', '$2a$10$TBId43wSoxr9Itgr.g8R0u2XZbuY7o98yBBCO6LqgqTSHj/HWYiqG', 'Emp003');
go

-- Thêm dữ liệu cho bảng Customer
INSERT INTO Customer (FullName, PhoneNumber, Email, RegDate)
VALUES
('Emma Johnson',  '1234567890', 'emma@example.com', '2024-04-30'),
('William Smith', '9876543212', 'william@example.com', '2024-04-30'),
('Olivia Brown', '1357924681', 'olivia@example.com', '2024-04-30'),
('James Wilson',  '2468013578', 'james@example.com', '2024-04-30'),
('Sophia Taylor',  '0123456787', 'sophia@example.com', '2024-04-30'),
('Alexander Martinez', '9870123455', 'alexander@example.com', '2024-04-30'),
('Ava Anderson',  '9876543213', 'ava@example.com', '2024-04-30'),
('Michael Thomas',  '1234567898', 'michael@example.com', '2024-04-30'),
('Isabella Garcia', '2468013570', 'isabella@example.com', '2024-04-30'),
('Ethan Rodriguez', '2468013571', 'ethan@example.com', '2024-04-30');
go

-- Thêm dữ liệu cho bảng Movie
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Shawshank Redemption', 'Drama', 'Frank Darabont', 142, '1994-09-23', 'English', 'USA', '1994-09-23', 'Released', 10.99, 'images/shawshank_redemption.jpg', 'https://www.youtube.com/watch?v=6hB3S9bIaco', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Godfather', 'Crime', 'Francis Ford Coppola', 175, '1972-03-24', 'English', 'USA', '1972-03-24', 'Unreleased', 12.99, 'images/the_godfather.jpg', 'https://www.youtube.com/watch?v=5DO-nDW43Ik', 'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Dark Knight', 'Action', 'Christopher Nolan', 152, '2008-07-18', 'English', 'USA', '2008-07-18', 'Released', 14.99, 'images/the_dark_knight.jpg', 'https://www.youtube.com/watch?v=EXeTwQWrcwY', 'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Pulp Fiction', 'Crime', 'Quentin Tarantino', 154, '1994-10-14', 'English', 'USA', '1994-10-14', 'Unreleased', 11.99, 'images/pulp_fiction.jpg', 'https://www.youtube.com/watch?v=s7EdQ4FqbhY', 'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Schindler''s List', 'Biography', 'Steven Spielberg', 195, '1994-02-04', 'English', 'USA', '1994-02-04', 'Released', 13.99, 'images/schindlers_list.jpg', 'https://www.youtube.com/watch?v=gG22XNhtnoY', 'In German-occupied Poland during World War II, industrialist Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution by the Nazis.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Lord of the Rings: The Return of the King', 'Adventure', 'Peter Jackson', 201, '2003-12-17', 'English', 'New Zealand', '2003-12-17', 'Released', 16.99, 'images/lotr_return_of_the_king.jpg', 'https://www.youtube.com/watch?v=r5X-hFf6Bwo', 'Gandalf and Aragorn lead the World of Men against Sauron''s army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Fight Club', 'Drama', 'David Fincher', 139, '1999-10-15', 'English', 'USA', '1999-10-15', 'Unreleased', 10.49, 'images/fight_club.jpg', 'https://www.youtube.com/watch?v=SUXWAEX2jlg', 'An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into much more.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Forrest Gump', 'Drama', 'Robert Zemeckis', 142, '1994-07-06', 'English', 'USA', '1994-07-06', 'Released', 9.99, 'images/forrest_gump.jpg', 'https://www.youtube.com/watch?v=uPIEn0M8su0', 'The presidencies of Kennedy and Johnson, the Vietnam War, the Watergate scandal and other historical events unfold from the perspective of an Alabama man with an IQ of 75, whose only desire is to be reunited with his childhood sweetheart.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Inception', 'Action', 'Christopher Nolan', 148, '2010-07-16', 'English', 'USA', '2010-07-16', 'Unreleased', 15.49, 'images/inception.jpg', 'https://www.youtube.com/watch?v=YoHD9XEInc0', 'A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Matrix', 'Action', 'Lana Wachowski, Lilly Wachowski', 136, '1999-03-31', 'English', 'USA', '1999-03-31', 'Released', 11.99, 'images/the_matrix.jpg', 'https://www.youtube.com/watch?v=vKQi3bBA1y8', 'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Green Mile', 'Drama', 'Frank Darabont', 189, '1999-12-10', 'English', 'USA', '1999-12-10', 'Released', 12.49, 'images/the_green_mile.jpg', 'https://www.youtube.com/watch?v=Ki4haFrqSrw', 'The lives of guards on Death Row are affected by one of their charges: a black man accused of child murder and rape, yet who has a mysterious gift.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Departed', 'Crime', 'Martin Scorsese', 151, '2006-10-06', 'English', 'USA', '2006-10-06', 'Unreleased', 13.99, 'images/the_departed.jpg', 'https://www.youtube.com/watch?v=auYbpnEwBBg', 'An undercover cop and a mole in the police attempt to identify each other while infiltrating an Irish gang in South Boston.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Goodfellas', 'Biography', 'Martin Scorsese', 146, '1990-09-19', 'English', 'USA', '1990-09-19', 'Released', 11.49, 'images/goodfellas.jpg', 'https://www.youtube.com/watch?v=qo5jJpHtI1Y', 'The story of Henry Hill and his life in the mob, covering his relationship with his wife Karen Hill and his mob partners Jimmy Conway and Tommy DeVito in the Italian-American crime syndicate.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Silence of the Lambs', 'Crime', 'Jonathan Demme', 118, '1991-02-14', 'English', 'USA', '1991-02-14', 'Unreleased', 14.99, 'images/the_silence_of_the_lambs.jpg', 'https://www.youtube.com/watch?v=ZWCAf-xLV2c', 'A young F.B.I. cadet must receive the help of an incarcerated and manipulative cannibal killer to help catch another serial killer, a madman who skins his victims.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Spirited Away', 'Animation, Adventure, Family', 'Hayao Miyazaki', 125, '2001-07-20', 'Japanese', 'Japan', '2001-07-20', 'Released', 14.99, 'images/spirited_away.jpg', 'https://www.youtube.com/watch?v=D_SLY-IuDlI', 'A young girl wanders into a world of spirits and demons after her parents are turned into pigs by a witch.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Big Lebowski', 'Comedy, Crime', 'Joel Coen', 117, '1998-03-06', 'English', 'USA', '1998-03-06', 'Released', 9.99, 'images/the_big_lebowski.jpg', 'https://www.youtube.com/watch?v=wJrQSUe5ccc', 'A mistaken identity throws a carefree bowler into the path of a bunch of angry thugs who believe he stole a rug from a millionaire.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Princess Bride', 'Adventure, Comedy, Family', 'Rob Reiner', 98, '1987-09-25', 'English', 'USA', '1987-09-25', 'Released', 8.99, 'images/the_princess_bride.jpg', 'https://www.youtube.com/watch?v=JFrKyGFNM2Y', 'Westley must rescue his princess bride from the evil Prince Humperdinck in this comedic fantasy.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Amélie', 'Comedy, Romance', 'Jean-Pierre Jeunet', 113, '2001-04-25', 'French', 'France', '2001-04-25', 'Released', 10.99, 'images/amelie.jpg', 'https://www.youtube.com/watch?v=hNK_iqFDuws', 'A shy waitress in Montmartre decides to anonymously improve the lives of those around her.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Your Name', 'Animation, Drama, Fantasy', 'Makoto Shinkai', 102, '2016-08-26', 'Japanese', 'Japan', '2016-08-26', 'Released', 12.99, 'images/your_name.jpg', 'https://www.youtube.com/watch?v=xTJzS7_JqSQ', 'Two teenagers discover they are linked in a strange dream-sharing phenomenon and set out to meet in the waking world.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Grand Budapest Hotel', 'Comedy, Crime', 'Wes Anderson', 100, '2014-02-21', 'English', 'USA', '2014-02-21', 'Released', 14.99, 'images/the_grand_budapest_hotel.jpg', 'https://www.youtube.com/watch?v=1y_0KkvRsXk', 'The concierge of a famous European hotel during the interwar period takes a young employee under his wing.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Blade Runner', 'Sci-Fi, Neo-Noir', 'Ridley Scott', 117, '1982-06-25', 'English', 'USA', '1982-06-25', 'Released', 13.99, 'images/blade_runner.jpg', 'https://www.youtube.com/watch?v=KP9kbYphvxo', 'A blade runner, a retired police officer who hunts rogue replicants (artificial humans), is tasked with retiring a group of recently arrived replicants.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Seven Samurai', 'Drama', 'Akira Kurosawa', 203, '1954-04-26', 'Japanese', 'Japan', '1954-04-26', 'Released', 14.49, 'images/seven_samurai.jpg', 'https://www.youtube.com/watch?v=ByfZ63y-VkQ', 'A poor village under constant bandit attacks hires seven unemployed samurai to protect them for the upcoming harvest.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Shining', 'Horror', 'Stanley Kubrick', 146, '1980-05-23', 'English', 'USA', '1980-05-23', 'Released', 11.99, 'images/the_shining.jpg', 'https://www.youtube.com/watch?v=NZleC1Y3HdM', 'A family heads to an isolated hotel for the winter where a sinister presence influences the father into violence, while his psychic son sees horrific forebodings from both past and future.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Rear Window', 'Mystery, Thriller', 'Alfred Hitchcock', 112, '1954-08-01', 'English', 'USA', '1954-08-01', 'Released', 12.49, 'images/rear_window.jpg', 'https://www.youtube.com/watch?v=cXcY9zvYOO0', 'A wheelchair-bound photographer spies on his neighbors from his apartment window and becomes convinced one of them has committed murder.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Raiders of the Lost Ark', 'Action, Adventure', 'Steven Spielberg', 115, '1981-06-12', 'English', 'USA', '1981-06-12', 'Released', 15.99, 'images/raiders_of_the_lost_ark.jpg', 'https://www.youtube.com/watch?v=XSL3YwGNKUw', 'Indiana Jones is hired by the U.S. government to find the Ark of the Covenant before Nazis can obtain its powers.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Breakfast Club', 'Comedy, Drama', 'John Hughes', 97, '1985-02-22', 'English', 'USA', '1985-02-22', 'Released', 9.99, 'images/the_breakfast_club.jpg', 'https://www.youtube.com/watch?v=TJW41_t-5mc', 'Five high school students from different social groups are forced to spend a Saturday in detention together, where they bond over their shared experiences and discover that they have more in common than they thought.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Back to the Future', 'Adventure, Comedy, Sci-Fi', 'Robert Zemeckis', 116, '1985-07-26', 'English', 'USA', '1985-07-26', 'Released', 10.99, 'images/back_to_the_future.jpg', 'https://www.youtube.com/watch?v=hO4tjXzBlCQ', 'Marty McFly, a teenager who travels back in time to 1955 in a time machine built by his friend Doc Brown, must ensure his parents meet and fall in love in order to save his own existence.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Terminator', 'Action, Sci-Fi', 'James Cameron', 108, '1984-10-26', 'English', 'USA', '1984-10-26', 'Released', 12.99, 'images/the_terminator.jpg', 'https://www.youtube.com/watch?v=d0_qZ2Zx7ZM', 'A cyborg assassin is sent back in time to kill Sarah Connor, a young woman whose unborn son is destined to save humanity from a future dominated by machines.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Goonies', 'Adventure, Comedy', 'Richard Donner', 114, '1985-06-07', 'English', 'USA', '1985-06-07', 'Released', 8.99, 'images/the_goonies.jpg', 'https://www.youtube.com/watch?v=7A_g4R11y3Q', 'A group of misfit kids embark on an adventure to find a hidden pirate treasure.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Princess Bride', 'Adventure, Comedy, Fantasy', 'Rob Reiner', 98, '1987-09-25', 'English', 'USA', '1987-09-25', 'Released', 8.99, 'images/the_princess_bride.jpg', 'https://www.youtube.com/watch?v=JFrKyGFNM2Y', 'Westley must rescue his princess bride from the evil Prince Humperdinck in this comedic fantasy.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Pulp Fiction', 'Crime', 'Quentin Tarantino', 154, '1994-10-14', 'English', 'USA', '1994-10-14', 'Released', 11.99, 'images/pulp_fiction.jpg', 'https://www.youtube.com/watch?v=s7EdQ4FqbhY', 'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Godfather: Part II', 'Crime, Drama', 'Francis Ford Coppola', 202, '1974-12-18', 'English', 'USA', '1974-12-18', 'Released', 14.99, 'images/the_godfather_part_ii.jpg', 'https://www.youtube.com/watch?v=9VpU_T155J4', 'The story of the young Vito Corleone in 1900s New York is told in parallel to the story of his son Michael as he expands and protects the family business in the postwar years.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Dark Knight Rises', 'Action, Crime, Drama', 'Christopher Nolan', 165, '2012-07-20', 'English', 'USA', '2012-07-20', 'Released', 16.99, 'images/the_dark_knight_rises.jpg', 'https://www.youtube.com/watch?v=gUEU1Fg_7lI', 'Eight years after the events of The Dark Knight, Batman is forced out of hiding and back into the streets to confront a ruthless terrorist named Bane.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('12 Angry Men', 'Crime, Drama', 'Sidney Lumet', 89, '1957-07-15', 'English', 'USA', '1957-07-15', 'Released', 9.99, 'images/12_angry_men.jpg', 'https://www.youtube.com/watch?v=V7Dt-y4oWxA', 'A jury in New York City is tasked with deciding the fate of a young man accused of murder. As the jurors deliberate, their personal biases and prejudices emerge, leading to a tense and dramatic confrontation.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Schindler''s List', 'Biography, Drama, History', 'Steven Spielberg', 195, '1993-12-15', 'English', 'USA', '1993-12-15', 'Released', 13.99, 'images/schindlers_list.jpg', 'https://www.youtube.com/watch?v=gG22XNhtnoY', 'In German-occupied Poland during World War II, industrialist Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution by the Nazis.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Lord of the Rings: The Fellowship of the Ring', 'Adventure, Drama, Fantasy', 'Peter Jackson', 178, '2001-12-19', 'English', 'New Zealand', '2001-12-19', 'Released', 16.99, 'images/lotr_fellowship_of_the_ring.jpg', 'https://www.youtube.com/watch?v=h0E2v5z_ZSs', 'A hobbit named Frodo inherits the One Ring from his uncle Bilbo, and with the help of a fellowship embarks on a quest to destroy it in the fires of Mount Doom.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Casablanca', 'Drama, Romance, War', 'Michael Curtiz', 102, '1942-11-25', 'English', 'USA', '1942-11-25', 'Released', 10.99, 'images/casablanca.jpg', 'https://www.youtube.com/watch?v=0618_pHRL4c', 'Rick Blaine, an American expatriate who runs a nightclub in Casablanca during World War II, must choose between his love for a woman and helping her husband, a Czech resistance leader, escape from the Nazis.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Shawshank Redemption', 'Drama', 'Frank Darabont', 142, '1994-09-23', 'English', 'USA', '1994-09-23', 'Released', 10.99, 'images/shawshank_redemption.jpg', 'https://www.youtube.com/watch?v=6hB3S9bIaco', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Big Lebowski', 'Comedy, Crime', 'Joel Coen', 117, '1998-03-06', 'English', 'USA', '1998-03-06', 'Released', 9.99, 'images/the_big_lebowski.jpg', 'https://www.youtube.com/watch?v=wJrQSUe5ccc', 'A mistaken identity throws a carefree bowler into the path of a bunch of angry thugs who believe he stole a rug from a millionaire.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Matrix', 'Action, Sci-Fi', 'Lana Wachowski, Lilly Wachowski', 136, '1999-03-31', 'English', 'USA', '1999-03-31', 'Released', 11.99, 'images/the_matrix.jpg', 'https://www.youtube.com/watch?v=vKQi3bBA1y8', 'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Memento', 'Crime, Mystery, Thriller', 'Christopher Nolan', 113, '2000-09-05', 'English', 'USA', '2000-09-05', 'Released', 12.99, 'images/memento.jpg', 'https://www.youtube.com/watch?v=8_rT1T3z1sQ', 'A man with short-term memory loss tries to find his wife''s killer, but the fragments of memory he can recall are not as reliable as he thinks.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Departed', 'Crime, Drama, Thriller', 'Martin Scorsese', 151, '2006-10-06', 'English', 'USA', '2006-10-06', 'Released', 13.99, 'images/the_departed.jpg', 'https://www.youtube.com/watch?v=auYbpnEwBBg', 'An undercover cop and a mole in the police attempt to identify each other while infiltrating an Irish gang in South Boston.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Apocalypse Now', 'Drama, War', 'Francis Ford Coppola', 153, '1979-08-03', 'English', 'USA', '1979-08-03', 'Released', 14.99, 'images/apocalypse_now.jpg', 'https://www.youtube.com/watch?v=14z_230z19g', 'An American captain during the Vietnam War is sent on a dangerous mission to assassinate the renegade Colonel Kurtz, who has gone AWOL and is waging his own war in Cambodia.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Good, the Bad and the Ugly', 'Western', 'Sergio Leone', 178, '1966-12-30', 'Italian', 'Italy', '1966-12-30', 'Released', 13.99, 'images/the_good_the_bad_and_the_ugly.jpg', 'https://www.youtube.com/watch?v=3_6z-AOz1UA', 'During the Civil War, three gunslingers compete to find a hidden cache of Confederate gold.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Princess Bride', 'Adventure, Comedy, Fantasy', 'Rob Reiner', 98, '1987-09-25', 'English', 'USA', '1987-09-25', 'Released', 8.99, 'images/the_princess_bride.jpg', 'https://www.youtube.com/watch?v=JFrKyGFNM2Y', 'Westley must rescue his princess bride from the evil Prince Humperdinck in this comedic fantasy.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Lord of the Rings: The Return of the King', 'Adventure, Drama, Fantasy', 'Peter Jackson', 201, '2003-12-17', 'English', 'New Zealand', '2003-12-17', 'Released', 16.99, 'images/lotr_return_of_the_king.jpg', 'https://www.youtube.com/watch?v=r5X-hFf6Bwo', 'Gandalf and Aragorn lead the World of Men against Sauron''s army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Departed', 'Crime, Drama, Thriller', 'Martin Scorsese', 151, '2006-10-06', 'English', 'USA', '2006-10-06', 'Released', 13.99, 'images/the_departed.jpg', 'https://www.youtube.com/watch?v=auYbpnEwBBg', 'An undercover cop and a mole in the police attempt to identify each other while infiltrating an Irish gang in South Boston.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Goodfellas', 'Biography, Crime, Drama', 'Martin Scorsese', 146, '1990-09-19', 'English', 'USA', '1990-09-19', 'Released', 11.49, 'images/goodfellas.jpg', 'https://www.youtube.com/watch?v=qo5jJpHtI1Y', 'The story of Henry Hill and his life in the mob, covering his relationship with his wife Karen Hill and his mob partners Jimmy Conway and Tommy.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Forrest Gump (1994)', 'Comedy, Drama, Romance', 'Robert Zemeckis', 142, '1994-07-06', 'English', 'USA', '1994-07-06', 'Released', 12.99, 'images/forrest_gump.jpg', 'https://www.youtube.com/watch?v=y4B2N452uE0', 'The story of Forrest Gump, a man with an IQ of 75, whose innocent life is accidentally intertwined with some of the most significant events of the 20th century.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Silence of the Lambs (1991)', 'Crime, Drama, Thriller', 'Jonathan Demme', 118, '1991-02-14', 'English', 'USA', '1991-02-14', 'Released', 13.99, 'images/the_silence_of_the_lambs.jpg', 'https://www.youtube.com/watch?v=Rd15_dF-7bw', 'A young FBI trainee is sent to interview an imprisoned cannibal and former psychiatrist for help catching an active serial killer.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Pulp Fiction (1994)', 'Crime', 'Quentin Tarantino', 154, '1994-10-14', 'English', 'USA', '1994-10-14', 'Released', 11.99, 'images/pulp_fiction.jpg', 'https://www.youtube.com/watch?v=s7EdQ4FqbhY', 'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Shawshank Redemption (1994)', 'Drama', 'Frank Darabont', 142, '1994-09-23', 'English', 'USA', '1994-09-23', 'Released', 10.99, 'images/shawshank_redemption.jpg', 'https://www.youtube.com/watch?v=6hB3S9bIaco', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Lord of the Rings: The Fellowship of the Ring (2001)', 'Adventure, Drama, Fantasy', 'Peter Jackson', 178, '2001-12-19', 'English', 'New Zealand', '2001-12-19', 'Released', 16.99, 'images/lotr_fellowship_of_the_ring.jpg', 'https://www.youtube.com/watch?v=h0E2v5z_ZSs', 'A hobbit named Frodo inherits the One Ring from his uncle Bilbo, and with the help of a fellowship embarks on a quest to destroy it in the fires of Mount Doom.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Lord of the Rings: The Two Towers', 'Adventure, Drama, Fantasy', 'Peter Jackson', 179, '2002-12-18', 'English', 'New Zealand', '2002-12-18', 'Released', 16.99, 'images/lotr_the_two_towers.jpg', 'https://www.youtube.com/watch?v=L9sfz2_5N9s', 'Frodo and Sam continue their journey to Mordor to destroy the One Ring, while Aragorn, Legolas, and Gimli track Merry and Pippin who have been captured by Orcs.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Godfather (1972)', 'Crime, Drama', 'Francis Ford Coppola', 175, '1972-03-24', 'English', 'USA', '1972-03-24', 'Released', 14.99, 'images/the_godfather.jpg', 'https://www.youtube.com/watch?v=9Wm41V1SE7E', 'The story of the Corleone family under the patriarch Vito Corleone, focusing on the transformation of his youngest son, Michael, from reluctant family outsider to ruthless mafia boss.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Dark Knight (2008)', 'Action, Crime, Drama', 'Christopher Nolan', 152, '2008-07-18', 'English', 'USA', '2008-07-18', 'Released', 16.99, 'images/the_dark_knight.jpg', 'https://www.youtube.com/watch?v=J-7z1z9O7sM', 'With the help of Lieutenant Jim Gordon and Batman, Gotham City tries to confront the Joker, a psychopathic criminal who plans to bring chaos upon the city.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Pulp Fiction (1994)', 'Crime', 'Quentin Tarantino', 154, '1994-10-14', 'English', 'USA', '1994-10-14', 'Released', 11.99, 'images/pulp_fiction.jpg', 'https://www.youtube.com/watch?v=s7EdQ4FqbhY', 'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Lord of the Rings: The Return of the King (2003)', 'Adventure, Drama, Fantasy', 'Peter Jackson', 201, '2003-12-17', 'English', 'New Zealand', '2003-12-17', 'Released', 16.99, 'images/lotr_return_of_the_king.jpg', 'https://www.youtube.com/watch?v=r5X-hFf6Bwo', 'Gandalf and Aragorn lead the World of Men against Sauron''s army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Shawshank Redemption (1994)', 'Drama', 'Frank Darabont', 142, '1994-09-23', 'English', 'USA', '1994-09-23', 'Released', 10.99, 'images/shawshank_redemption.jpg', 'https://www.youtube.com/watch?v=6hB3S9bIaco', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Lord of the Rings: The Fellowship of the Ring (2001)', 'Adventure, Drama, Fantasy', 'Peter Jackson', 178, '2001-12-19', 'English', 'New Zealand', '2001-12-19', 'Released', 16.99, 'images/lotr_fellowship_of_the_ring.jpg', 'https://www.youtube.com/watch?v=h0E2v5z_ZSs', 'The story of the young Vito Corleone in 1900s New York is told in parallel to the story of his son Michael as he expands and protects the family business in the postwar years.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Princess Bride (1987)', 'Adventure, Comedy, Fantasy', 'Rob Reiner', 98, '1987-09-25', 'English', 'USA', '1987-09-25', 'Released', 8.99, 'images/the_princess_bride.jpg', 'https://www.youtube.com/watch?v=JFrKyGFNM2Y', 'Westley must rescue his princess bride from the evil Prince Humperdinck in this comedic fantasy.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Departed (2006)', 'Crime, Drama, Thriller', 'Martin Scorsese', 151, '2006-10-06', 'English', 'USA', '2006-10-06', 'Released', 13.99, 'images/the_departed.jpg', 'https://www.youtube.com/watch?v=auYbpnEwBBg', 'An undercover cop and a mole in the police attempt to identify each other while infiltrating an Irish gang in South Boston.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('Goodfellas (1990)', 'Biography, Crime, Drama', 'Martin Scorsese', 146, '1990-09-19', 'English', 'USA', '1990-09-19', 'Released', 11.49, 'images/goodfellas.jpg', 'https://www.youtube.com/watch?v=qo5jJpHtI1Y', 'The story of Henry Hill and his life in the mob, covering his relationship with his wife Karen Hill and his mob partners Jimmy Conway and Tommy DeVito.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Big Lebowski (1998)', 'Comedy, Crime', 'Joel Coen', 117, '1998-03-06', 'English', 'USA', '1998-03-06', 'Released', 9.99, 'images/the_big_lebowski.jpg', 'https://www.youtube.com/watch?v=wJrQSUe5ccc', 'A mistaken identity throws a carefree bowler into the path of a bunch of angry thugs who believe he stole a rug from a millionaire.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Shawshank Redemption (1994)', 'Drama', 'Frank Darabont', 142, '1994-09-23', 'English', 'USA', '1994-09-23', 'Released', 10.99, 'images/shawshank_redemption.jpg', 'https://www.youtube.com/watch?v=6hB3S9bIaco', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.');
INSERT INTO Movie (MovieName, Genre, Director, Duration, ReleasedDate, Language, Country, StartDate, Status, ImportPrice, ImageSource, Trailer, Description)
VALUES ('The Lord of the Rings: The Return of the King (2003)', 'Adventure, Drama, Fantasy', 'Peter Jackson', 201, '2003-12-17', 'English', 'New Zealand', '2003-12-17', 'Released', 16.99, 'images/lotr_return_of_the_king.jpg', 'https://www.youtube.com/watch?v=r5X-hFf6Bwo', 'Gandalf and Aragorn lead the World of Men against Sauron''s army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.');
go


-- movieschedule
insert into movieschedule (screeningTime, movieid, roomid, PricePerSeat) 
values 
('2024-05-03 20:00:00', 'Mov001', 'Room01', 14.9);

insert into movieschedule (screeningTime, movieid, roomid, PricePerSeat) 
values 
('2024-05-03 00:00:00', 'Mov002', 'Room02', 19.9);

insert into movieschedule (screeningTime, movieid, roomid, PricePerSeat) 
values 
('2024-05-03 00:00:00', 'Mov003', 'Room03', 24.9);

insert into movieschedule (screeningTime, movieid, roomid, PricePerSeat) 
values 
('2024-05-03 00:00:00', 'Mov004', 'Room04', 29.9);

insert into movieschedule (screeningTime, movieid, roomid, PricePerSeat) 
values 
('2024-05-03 00:00:00', 'Mov005', 'Room05', 34.9);

insert into movieschedule (screeningTime, movieid, roomid, PricePerSeat) 
values 
('2024-05-03 00:00:00', 'Mov006', 'Room06', 39.9);

insert into movieschedule (screeningTime, movieid, roomid, PricePerSeat) 
values 
('2024-05-03 00:00:00', 'Mov007', 'Room07', 44.9);
go

insert into movieschedule (screeningTime, movieid, roomid, PricePerSeat) 
values 
('2024-05-04 12:00:00', 'Mov008', 'Room01', 49.9);

insert into movieschedule (screeningTime, movieid, roomid, PricePerSeat) 
values 
('2024-05-04 12:00:00', 'Mov009', 'Room02', 44.9);

insert into movieschedule (screeningTime, movieid, roomid, PricePerSeat) 
values 
('2024-05-04 12:00:00', 'Mov010', 'Room03', 39.9);

insert into movieschedule (screeningTime, movieid, roomid, PricePerSeat) 
values 
('2024-05-04 12:00:00', 'Mov011', 'Room04', 34.9);

insert into movieschedule (screeningTime, movieid, roomid, PricePerSeat) 
values 
('2024-05-04 12:00:00', 'Mov012', 'Room05', 29.9);

insert into movieschedule (screeningTime, movieid, roomid, PricePerSeat) 
values 
('2024-05-04 12:00:00', 'Mov013', 'Room06', 24.9);

insert into movieschedule (screeningTime, movieid, roomid, PricePerSeat) 
values 
('2024-05-04 12:00:00', 'Mov014', 'Room07', 19.9);
go

-- Thêm dữ liệu cho bảng Product
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('Popcorn', 100, 3.50, 'images/popcorn.jpg', 'Food');
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('Large Popcorn', 190, 4.50, 'images/popcorn.jpg', 'Food');
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('Medium Popcorn', 180, 4.00, 'images/popcorn.jpg', 'Food');
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('Pepsi', 110, 1.99, 'images/pepsi.jpg', 'Drink');
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('Coca-Cola', 120, 1.99, 'images/cocacola.png', 'Drink');
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('Mineral Water', 130, 1.50, 'images/mineral-water.png', 'Drink');
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('7 Up', 140, 1.99, 'images/7up.jpg', 'Drink');
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('Combo x2 Popcorn', 100, 5.50, 'images/popcorn.jpg', 'Food');
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('Combo x2 Large Popcorn', 195, 7.50, 'images/popcorn.jpg', 'Food');
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('Combo x2 Medium Popcorn', 120, 7.00, 'images/popcorn.jpg', 'Food');
go
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('Combo x2 Pepsi', 135, 2.99, 'images/pepsi.jpg', 'Drink');
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('Combo x2 Coca-Cola', 140, 2.99, 'images/cocacola.png', 'Drink');
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('Combo x2 Mineral Water', 165, 2.50, 'images/mineral-water.png', 'Drink');
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('Combo x2 7 Up', 185, 2.99, 'images/7up.jpg', 'Drink');
go

EXECUTE UpdateProductQuantityByID 'Pro001', 80;
EXECUTE UpdateProductQuantityByID 'Pro003', 70;
EXECUTE UpdateProductQuantityByID 'Pro004', 60;
EXECUTE UpdateProductQuantityByID 'Pro007', 50;
EXECUTE UpdateProductQuantityByID 'Pro001', 40;
go

-- Order
-- Customer 1:
INSERT INTO [Order] (OrderDate, QuantitySeat, Note, CustomerID, EmployeeID, ScheduleID)
VALUES
('2024-05-03 18:45:00', 2, N'Extra butter on popcorn', 'Cus0001', 'Emp004', 'Sch00008'),
('2024-03-21 14:20:00', 1, N'No notes', 'Cus0001', 'Emp001', 'Sch00010'),
('2023-09-17 09:40:00', 2, N'Celebrating birthday!', 'Cus0001', 'Emp005', 'Sch00006');

-- Customer 2:
INSERT INTO [Order] (OrderDate, QuantitySeat, Note, CustomerID, EmployeeID, ScheduleID)
VALUES
('2024-05-01 10:30:00', 4, N'Extra butter on popcorn', 'Cus0002', 'Emp008', 'Sch00006'),
('2024-04-07 17:50:00', 1, N'Need wheelchair access', 'Cus0002', 'Emp005', 'Sch00002'),
('2023-08-22 21:10:00', 2, N'No notes', 'Cus0002', 'Emp003', 'Sch00004');

-- Customer 3:
INSERT INTO [Order] (OrderDate, QuantitySeat, Note, CustomerID, EmployeeID, ScheduleID)
VALUES
('2024-04-02 20:10:00', 3, N'No notes', 'Cus0003', 'Emp003', 'Sch00004'),
('2024-02-20 08:35:00', 5, N'No notes', 'Cus0003', 'Emp009', 'Sch00002'),
('2023-10-14 20:55:00', 2, N'No notes', 'Cus0003', 'Emp001', 'Sch00006');

-- Customer 4:
INSERT INTO [Order] (OrderDate, QuantitySeat, Note, CustomerID, EmployeeID, ScheduleID)
VALUES
('2024-04-12 22:05:00', 2, N'Celebrating birthday!', 'Cus0004', 'Emp006', 'Sch00010'),
('2024-01-28 18:15:00', 2, N'No notes', 'Cus0004', 'Emp008', 'Sch00004'),
('2023-11-14 16:40:00', 2, N'No notes', 'Cus0004', 'Emp004', 'Sch00002');

-- OrderDetail
 -- Order 1
INSERT INTO OrderDetail (Quantity, OrderID, ProductID)
VALUES
(3, 'Ord0001', 'Pro002');
INSERT INTO OrderDetail (Quantity, OrderID, ProductID)
VALUES
(2, 'Ord0001', 'Pro004');

-- Order 2
INSERT INTO OrderDetail (Quantity, OrderID, ProductID)
VALUES
(2, 'Ord0002', 'Pro002');
INSERT INTO OrderDetail (Quantity, OrderID, ProductID)
VALUES
(5, 'Ord0002', 'Pro004');
INSERT INTO OrderDetail (Quantity, OrderID, ProductID)
VALUES
(3, 'Ord0002', 'Pro001'); 

-- Order 3
INSERT INTO OrderDetail (Quantity, OrderID, ProductID)
VALUES
(4, 'Ord0003', 'Pro001'); 

-- Order 4
INSERT INTO OrderDetail (Quantity, OrderID, ProductID)
VALUES
(1, 'Ord0004', 'Pro007'); 

-- Order 5
INSERT INTO OrderDetail (Quantity, OrderID, ProductID)
VALUES
(4, 'Ord0005', 'Pro002'); 
INSERT INTO OrderDetail (Quantity, OrderID, ProductID)
VALUES
(6, 'Ord0005', 'Pro005');
go

/*
------------*** Customer ***
DECLARE @i INT = 1;
WHILE @i <= 6999
BEGIN
    DECLARE @PhoneNumber NVARCHAR(20);
    DECLARE @Email NVARCHAR(100);
    DECLARE @FullName NVARCHAR(100);
    SET @PhoneNumber = CONCAT('555', RIGHT('0000000000' + CAST(ABS(CHECKSUM(NEWID())) % 1000000000 AS NVARCHAR(10)), 7));
    IF NOT EXISTS (SELECT 1 FROM Customer WHERE PhoneNumber = @PhoneNumber)
    BEGIN
        SET @Email = CONCAT(LEFT(@PhoneNumber, LEN(@PhoneNumber) - 4), '@example.com');
        SET @FullName = CONCAT('Customer ', @i);
        INSERT INTO Customer (FullName, PhoneNumber, Email, RegDate)
        VALUES
        (@FullName, @PhoneNumber, @Email, GETDATE());
        SET @i = @i + 1;
    END;
END;
go

-------------*** Order *** 
DECLARE @i INT = 1;
WHILE @i <= 8999
BEGIN
	DECLARE @RandomCustomerID CHAR(7);
	DECLARE @RandomEmployeeID CHAR(6);
	DECLARE @RandomScheduleID CHAR(8);
	DECLARE @RandomQuantitySeat INT;
	DECLARE @RandomOrderDate DATETIME;
	SET @RandomOrderDate = DATEADD(DAY, -ROUND(RAND() * 365, 0), GETDATE());
	DECLARE @RandomHour INT = ROUND(RAND() * 23, 0);
	DECLARE @RandomMinute INT = ROUND(RAND() * 59, 0);
	DECLARE @RandomSecond INT = ROUND(RAND() * 59, 0);
	SET @RandomOrderDate = DATEADD(HOUR, @RandomHour, @RandomOrderDate);
	SET @RandomOrderDate = DATEADD(MINUTE, @RandomMinute, @RandomOrderDate);
	SET @RandomOrderDate = DATEADD(SECOND, @RandomSecond, @RandomOrderDate);
	SELECT TOP 1 @RandomCustomerID = CustomerID
    FROM Customer
    ORDER BY NEWID();
    SELECT TOP 1 @RandomEmployeeID = EmployeeID
    FROM Employee
    ORDER BY NEWID();
    SELECT TOP 1 @RandomScheduleID = ScheduleID
    FROM MovieSchedule
    ORDER BY NEWID();
    SET @RandomQuantitySeat = 1 + FLOOR(RAND() * 5);
    INSERT INTO [Order] (OrderDate, QuantitySeat, Note, CustomerID, EmployeeID, ScheduleID)
    VALUES (@RandomOrderDate, @RandomQuantitySeat, 'No Note', @RandomCustomerID, @RandomEmployeeID, @RandomScheduleID);
    SET @i = @i + 1;
END;
go

-------------------*** OrderDetail ***
DECLARE @i INT = 1;
WHILE @i <= 24999
BEGIN
    DECLARE @Quantity INT = ABS(CHECKSUM(NEWID())) % 10 + 1;
    DECLARE @RandomProductID CHAR(6);
    SELECT TOP 1 @RandomProductID = ProductID
    FROM Product
    ORDER BY NEWID();
	DECLARE @RandomOrderID CHAR(7);
    SELECT TOP 1 @RandomOrderID = OrderID
    FROM [Order]
    ORDER BY NEWID();
    IF NOT EXISTS (SELECT 1 FROM OrderDetail WHERE OrderID = @RandomOrderID AND ProductID = @RandomProductID)
    BEGIN
        INSERT INTO OrderDetail (Quantity, OrderID, ProductID)
        VALUES (@Quantity, @RandomOrderID, @RandomProductID);
    END;
    SET @i = @i + 1;
END;
go

----------------*** MovieSchedule ***
DECLARE @i INT = 1;
WHILE @i <= 8000
BEGIN
    DECLARE @RandomScreeningTime DATETIME;
    SET @RandomScreeningTime = DATEADD(DAY, -ROUND(RAND() * 365, 0), GETDATE());
    DECLARE @RandomHour INT = ROUND(RAND() * 23, 0);
    DECLARE @RandomMinute INT = ROUND(RAND() * 59, 0);
    DECLARE @RandomSecond INT = ROUND(RAND() * 59, 0);
    SET @RandomScreeningTime = DATEADD(HOUR, @RandomHour, @RandomScreeningTime);
    SET @RandomScreeningTime = DATEADD(MINUTE, @RandomMinute, @RandomScreeningTime);
    SET @RandomScreeningTime = DATEADD(SECOND, @RandomSecond, @RandomScreeningTime);
    DECLARE @RandomMovieID CHAR(6);
    SELECT TOP 1 @RandomMovieID = MovieID FROM Movie ORDER BY NEWID();
    DECLARE @RandomRoomID CHAR(6);
    SELECT TOP 1 @RandomRoomID = RoomID FROM Room ORDER BY NEWID();
    DECLARE @MovieDuration INT;
    SELECT @MovieDuration = Duration FROM Movie WHERE MovieID = @RandomMovieID;
    DECLARE @NewScreeningTime SMALLDATETIME = DATEADD(MINUTE, -30, @RandomScreeningTime);
    DECLARE @NewEndTime SMALLDATETIME = DATEADD(MINUTE, @MovieDuration + 30, @RandomScreeningTime);
    IF NOT EXISTS (
        SELECT 1
        FROM MovieSchedule ms
        WHERE ms.RoomID = @RandomRoomID
        AND ((ms.EndTime > @NewScreeningTime AND ms.EndTime < @NewEndTime)
             OR (ms.ScreeningTime > @NewScreeningTime AND ms.ScreeningTime < @NewEndTime))
    )
    BEGIN
		DECLARE @PricePerSeat MONEY;
        SET @PricePerSeat = 10.00 + RAND() * 50.00;
        INSERT INTO MovieSchedule (ScreeningTime, MovieID, RoomID, PricePerSeat)
        VALUES (@RandomScreeningTime, @RandomMovieID, @RandomRoomID, @PricePerSeat);
    END;
    SET @i = @i + 1;
END;
go

-------------------*** Importproduct ***
DECLARE @i INT = 1;
WHILE @i <= 899
BEGIN
    DECLARE @RandomProductID CHAR(6);
    SELECT TOP 1 @RandomProductID = ProductID FROM Product ORDER BY NEWID();

    DECLARE @RandomQuantityImport INT;
    SET @RandomQuantityImport = CAST((RAND() * (150 - 100 + 1) + 100) AS INT);

    EXECUTE UpdateProductQuantityByID @RandomProductID, @RandomQuantityImport;

    SET @i = @i + 1;
END;
GO
*/
