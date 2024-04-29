package entity;

import java.time.LocalDateTime;

public class OrderAddProduct {
	private String addProductID;
	private LocalDateTime addProductDate;
	private int quantity;
	private double unitPurchasePrice;
	private double total;
	private Product product;

	public OrderAddProduct(LocalDateTime addProductDate, int quantity, double unitPurchasePrice, Product product) {
		super();
		this.addProductDate = addProductDate;
		this.quantity = quantity;
		this.unitPurchasePrice = unitPurchasePrice;
		setTotal();
		this.product = product;
	}

	public OrderAddProduct() {
		super();
	}

	public String getAddProductID() {
		return addProductID;
	}

	public LocalDateTime getAddProductDate() {
		return addProductDate;
	}

	public void setAddProductDate(LocalDateTime addProductDate) {
		this.addProductDate = addProductDate;
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
		return "OrderAddProduct [addProductID=" + addProductID + ", addProductDate=" + addProductDate + ", quantity="
				+ quantity + ", unitPurchasePrice=" + unitPurchasePrice + ", total=" + total + ", product=" + product
				+ "]";
	}

}
