package entity;

import java.util.Arrays;

public class Product {
	private String productID;
	private String productName;
	private double price;
	private int quantity;
	private double purchasePrice;
	private byte[] imageSource;
	private String productType;

	public Product() {
		super();
	}

	public Product(String productName, int quantity, double purchasePrice,
			byte[] imageSource, String productType) {
		super();
		this.productName = productName;
		setPrice();
		this.quantity = quantity;
		this.purchasePrice = purchasePrice;
		this.imageSource = imageSource;
		this.productType = productType;
	}

	public String getProductID() {
		return productID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice() {
		// viet phuong thuc tinh
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public byte[] getImageSource() {
		return imageSource;
	}

	public void setImageSource(byte[] imageSource) {
		this.imageSource = imageSource;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	@Override
	public String toString() {
		return "Product [productID=" + productID + ", productName=" + productName + ", price=" + price + ", quantity="
				+ quantity + ", purchasePrice=" + purchasePrice + ", imageSource=" + Arrays.toString(imageSource)
				+ ", productType=" + productType + "]";
	}

}
