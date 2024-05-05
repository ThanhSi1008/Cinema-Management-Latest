package entity;

public class ProductRanking {

	private String productName;
	private int salesQty;
	private double totalRevenue;

	public ProductRanking(String productName, int salesQty, double totalRevenue) {
		super();
		this.productName = productName;
		this.salesQty = salesQty;
		this.totalRevenue = totalRevenue;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getSalesQty() {
		return salesQty;
	}

	public void setSalesQty(int salesQty) {
		this.salesQty = salesQty;
	}

	public double getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	@Override
	public String toString() {
		return "ProductRanking [productName=" + productName + ", salesQty=" + salesQty
				+ ", totalRevenue=" + totalRevenue + "]";
	}

}
