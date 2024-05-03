package entity;

public class Product {
	private String productID;
	private String productName;
	private double price;
	private int quantity;
	private double purchasePrice;
	private String imageSource;
	private String productType;

	public Product() {
		super();
	}

	public Product(String productID) {
		super();
		this.productID = productID;
	}

	public Product(String productID, String productName, int quantity, double purchasePrice, String imageSource) {
		super();
		this.productID = productID;
		this.productName = productName;
		this.quantity = quantity;
		this.purchasePrice = purchasePrice;
		this.imageSource = imageSource;
	}

	public Product(String productName, double price, int quantity, double purchasePrice, String imageSource,
			String productType) {
		super();
		this.productName = productName;
		this.price = price;
		this.quantity = quantity;
		this.purchasePrice = purchasePrice;
		this.imageSource = imageSource;
		this.productType = productType;
	}

	public Product(String productID, String productName, double price, int quantity, String imageSource) {
		super();
		this.productID = productID;
		this.productName = productName;
		this.price = price;
		this.quantity = quantity;
		this.imageSource = imageSource;
	}

	public Product(String productName, int quantity, double purchasePrice, String imageSource) {
		super();
		this.productName = productName;
		this.quantity = quantity;
		this.purchasePrice = purchasePrice;
		this.imageSource = imageSource;
	}

	public void setProductID(String productID) {
		if (this.productID == null) {
			this.productID = productID;
		} else {
			System.out.println("ProductID is already set and cannot be changed.");
		}
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

	public void setPrice(double price) {
		this.price = price;
	}

	public void setPrice() {
		this.price = this.purchasePrice * 2;
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

	public String getImageSource() {
		return imageSource;
	}

	public void setImageSource(String imageSource) {
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
				+ quantity + ", purchasePrice=" + purchasePrice + ", imageSource=" + imageSource + ", productType="
				+ productType + "]";
	}

}
