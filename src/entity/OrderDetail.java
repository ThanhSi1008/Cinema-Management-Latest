package entity;

public class OrderDetail {
	private double unitPrice;
	private double linetotal;
	private Order order;
	private Product product;

	public OrderDetail() {
		super();
	}

	public OrderDetail(double unitPrice, Order order, Product product) {
		super();
		this.unitPrice = unitPrice;
		setLinetotal();
		this.order = order;
		this.product = product;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getLinetotal() {
		return linetotal;
	}

	public void setLinetotal() {
		// viết method tính lineTotal
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "OrderDetail [unitPrice=" + unitPrice + ", linetotal=" + linetotal + ", order=" + order + ", product="
				+ product + "]";
	}

}
