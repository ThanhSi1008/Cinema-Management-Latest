package entity;

import java.time.LocalDateTime;

public class OrderImportProduct {
	private String importProductID;
	private LocalDateTime importProductDate;
	private int quantity;
	private double unitPurchasePrice;
	private double total;
	private Product product;

	public OrderImportProduct(LocalDateTime importProductDate, int quantity,
			double unitPurchasePrice, Product product) {
		super();
		this.importProductDate = importProductDate;
		this.quantity = quantity;
		this.unitPurchasePrice = unitPurchasePrice;
		setTotal();
		this.product = product;
	}

	public OrderImportProduct() {
		super();
	}

	public String getImportProductID() {
		return importProductID;
	}

	public LocalDateTime getImportProductDate() {
		return importProductDate;
	}

	public void setImportProductDate(LocalDateTime importProductDate) {
		this.importProductDate = importProductDate;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getUnitPurchasePrice() {
		return unitPurchasePrice;
	}

	public void setUnitPurchasePrice(double unitPurchasePrice) {
		this.unitPurchasePrice = unitPurchasePrice;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal() {
		// viet phuong thuc
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "OrderImportProduct [importProductID=" + importProductID + ", importProductDate=" + importProductDate
				+ ", quantity=" + quantity + ", unitPurchasePrice=" + unitPurchasePrice + ", total=" + total
				+ ", product=" + product + "]";
	}

}
