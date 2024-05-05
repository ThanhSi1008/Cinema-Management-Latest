package entity;

public class Income {

	private String totalAddProduct;
	private String totalImproductProduct;
	private String totalAddMovie;

	public Income(String totalAddProduct, String totalImproductProduct, String totalAddMovie) {
		super();
		this.totalAddProduct = totalAddProduct;
		this.totalImproductProduct = totalImproductProduct;
		this.totalAddMovie = totalAddMovie;
	}

	public String getTotalAddProduct() {
		return totalAddProduct;
	}

	public void setTotalAddProduct(String totalAddProduct) {
		this.totalAddProduct = totalAddProduct;
	}

	public String getTotalImproductProduct() {
		return totalImproductProduct;
	}

	public void setTotalImproductProduct(String totalImproductProduct) {
		this.totalImproductProduct = totalImproductProduct;
	}

	public String getTotalAddMovie() {
		return totalAddMovie;
	}

	public void setTotalAddMovie(String totalAddMovie) {
		this.totalAddMovie = totalAddMovie;
	}

	@Override
	public String toString() {
		return "Income [totalAddProduct=" + totalAddProduct + ", totalImproductProduct=" + totalImproductProduct
				+ ", totalAddMovie=" + totalAddMovie + "]";
	}

}
