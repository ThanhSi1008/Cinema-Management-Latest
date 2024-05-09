package entity;

public class OrderDetail {

	private int quantity;
	private double lineTotal;
	private Order order;
	private Product product;

	public OrderDetail(int quantity, Order order, Product product) {
		super();
		this.quantity = quantity;
		this.order = order;
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotal() {
		return lineTotal;
	}

	public void setTotal() {
		this.lineTotal = this.quantity * product.getPrice();
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
		return "OrderDetail [quantity=" + quantity + ", lineTotal=" + lineTotal + ", order=" + order + ", product="
				+ product + "]";
	}

}
