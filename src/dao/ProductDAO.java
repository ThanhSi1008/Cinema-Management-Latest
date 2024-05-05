package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.Product;

public class ProductDAO {

	private ConnectDB connectDB;

	public ProductDAO() {
		connectDB = ConnectDB.getInstance();
		connectDB.connect();
	}

	public List<Product> getAllProductByType(String type) {
		List<Product> productList = new ArrayList<>();
		Connection connection = connectDB.getConnection();
		String querySQL = "SELECT * FROM Product WHERE ProductType = ?";
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = connection.prepareStatement(querySQL);
			statement.setString(1, type);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Product product = new Product(resultSet.getString("ProductID"));

				product.setProductName(resultSet.getString("ProductName"));
				product.setPrice(resultSet.getDouble("Price"));
				product.setQuantity(resultSet.getInt("Quantity"));
				product.setPurchasePrice(resultSet.getDouble("PurchasePrice"));
				product.setImageSource(resultSet.getString("ImageSource"));
				product.setProductType(type);

				productList.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(statement, resultSet);
		}
		return productList;
	}

	public List<Product> getAllProduct() {
		Connection connection = connectDB.getConnection();
		List<Product> productList = null;
		try {
			PreparedStatement s = connection.prepareStatement("select * from product");
			ResultSet rs = s.executeQuery();
			productList = new ArrayList<Product>();
			while (rs.next()) {
				String productID = rs.getString(1);
				String productName = rs.getString(2);
				double price = rs.getDouble(3);
				int quantity = rs.getInt(4);
				double purchasePrice = rs.getDouble(5);
				String imageSource = rs.getString(6);
				String productType = rs.getString(7);
				productList.add(
						new Product(productID, productName, price, quantity, purchasePrice, imageSource, productType));
			}
			return productList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productList;
	}

	public void updateQtyById(String productID, int quantity) {
		Connection connection = connectDB.getConnection();
		PreparedStatement s;
		try {
			s = connection.prepareStatement("update product set quantity = quantity - ? where productid = ?");
			s.setInt(1, quantity);
			s.setString(2, productID);
			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<Product> findFoodByName(String productName) {
		Connection connection = connectDB.getConnection();
		String querySQL = "SELECT * FROM Product WHERE ProductName LIKE ? AND ProductType = 'Food'";
		List<Product> foodList = new ArrayList<Product>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setString(1, "%" + productName + "%");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String id = resultSet.getString("ProductID");
				String name = resultSet.getString("ProductName");
				double price = resultSet.getDouble("Price");
				int quantity = resultSet.getInt("Quantity");
				String image = resultSet.getString("ImageSource");

				foodList.add(new Product(id, name, price, quantity, image));
			}
			return foodList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Product> findDrinkByName(String productName) {
		Connection connection = connectDB.getConnection();
		String querySQL = "SELECT * FROM Product WHERE ProductName LIKE ? AND ProductType = 'Drink'";
		List<Product> drinkList = new ArrayList<Product>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setString(1, "%" + productName + "%");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String id = resultSet.getString("ProductID");
				String name = resultSet.getString("ProductName");
				double price = resultSet.getDouble("Price");
				int quantity = resultSet.getInt("Quantity");
				String image = resultSet.getString("ImageSource");

				drinkList.add(new Product(id, name, price, quantity, image));
			}
			return drinkList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String addNewProduct(Product newProduct) {
		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		String productID = null;

		try {
			connection = connectDB.getConnection();
			String querySQL = "{CALL AddNewProduct(?, ?, ?, ?, ?, ?)}";
			callableStatement = connection.prepareCall(querySQL);

			callableStatement.setString(1, newProduct.getProductName());
			callableStatement.setInt(2, newProduct.getQuantity());
			callableStatement.setDouble(3, newProduct.getPurchasePrice());
			callableStatement.setString(4, newProduct.getImageSource());
			callableStatement.setString(5, newProduct.getProductType());
			callableStatement.registerOutParameter(6, Types.CHAR);

			callableStatement.execute();

			productID = callableStatement.getString(6);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(callableStatement, rs);
		}
		return productID;
	}

	public boolean addNewDrink(Product newProduct) {
		Connection connection = connectDB.getConnection();
		PreparedStatement preparedStatement = null;

		String querySQL = "INSERT INTO Product (ProductName, Quantity, PurchasePrice, ImageSource, ProductType) VALUES (?, ?, ?, ?, 'Drink')";
		try {
			preparedStatement = connection.prepareStatement(querySQL);
			preparedStatement.setString(1, newProduct.getProductID());
			preparedStatement.setInt(2, newProduct.getQuantity());
			preparedStatement.setDouble(3, newProduct.getPurchasePrice());
			preparedStatement.setString(4, newProduct.getImageSource());

			int rowsAffected = preparedStatement.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(preparedStatement, null);
		}
		return false;
	}

	public boolean removeProductByID(String productID) {
		Connection connection = connectDB.getConnection();
		String deleteSQL = "DELETE FROM Product WHERE ProductID = ?";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setString(1, productID);
			int rowsAffected = preparedStatement.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(preparedStatement, null);
		}
		return false;
	}

	public boolean updateNewProduct(Product newProduct) {
		Connection connection = connectDB.getConnection();
		PreparedStatement preparedStatement = null;

		String updateSQL = "UPDATE Product SET ProductName = ?, PurchasePrice = ?, ImageSource = ? WHERE ProductID = ?";
		try {
			preparedStatement = connection.prepareStatement(updateSQL);
			preparedStatement.setString(1, newProduct.getProductName());
			preparedStatement.setDouble(2, newProduct.getPurchasePrice());
			preparedStatement.setString(3, newProduct.getImageSource());
			preparedStatement.setString(4, newProduct.getProductID());

			int rowsAffected = preparedStatement.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(preparedStatement, null);
		}
		return false;
	}

	public boolean addQuantity(String productID, int qty) {
		Connection connection = connectDB.getConnection();
		CallableStatement callableStatement = null;

		try {
			String querySQL = "{CALL UpdateProductQuantityByID(?, ?)}";
			callableStatement = connection.prepareCall(querySQL);

			callableStatement.setString(1, productID);
			callableStatement.setInt(2, qty);

			callableStatement.execute();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectDB.close(callableStatement, null);
		}
		return false;
	}

}
