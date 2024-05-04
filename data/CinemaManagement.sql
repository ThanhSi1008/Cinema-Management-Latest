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

CREATE OR ALTER TRIGGER CaculateOrderTotal
ON [Order]
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE o
    SET o.Total = ISNULL(o.QuantitySeat, 0) * ISNULL(ms.PricePerSeat, 0)
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

    DECLARE @OrderIDs TABLE (OrderID CHAR(7));
    INSERT INTO @OrderIDs (OrderID)
    SELECT DISTINCT OrderID FROM inserted;

    UPDATE o
    SET o.Total = ISNULL(od.TotalPrice, 0) + ISNULL(o.Total, 0)
    FROM [Order] o
    LEFT JOIN (
        SELECT OrderID, SUM(LineTotal) AS TotalPrice
        FROM OrderDetail
        WHERE OrderID IN (SELECT OrderID FROM @OrderIDs) -- Chỉ tính tổng cho các Order liên quan
        GROUP BY OrderID
    ) od ON o.OrderID = od.OrderID
    WHERE o.OrderID IN (SELECT OrderID FROM @OrderIDs);
END;
go

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
('Large Popcorn', 90, 4.50, 'images/popcorn.jpg', 'Food');
INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType)
VALUES
('Medium Popcorn', 80, 4.00, 'images/popcorn.jpg', 'Food');
go

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
*******-------- Customer ------*******
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
*/

/*
*******-------- Order ------*******
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
*/

--*************************---------------------- There ----------------------*************************
/*
*******--------OrderDetail-------********
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
*/

--*******-------- MovieSchedule ------*******
/*
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
*/
