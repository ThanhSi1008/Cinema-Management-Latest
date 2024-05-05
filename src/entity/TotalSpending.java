package entity;

import java.math.BigDecimal;

public class TotalSpending {
	private BigDecimal totalAddProdcut;
	private BigDecimal totalImportProduct;
	private BigDecimal totalAddMovie;

	public TotalSpending(BigDecimal totalAddProdcut, BigDecimal totalImportProduct, BigDecimal totalAddMovie) {
		super();
		this.totalAddProdcut = totalAddProdcut;
		this.totalImportProduct = totalImportProduct;
		this.totalAddMovie = totalAddMovie;
	}

	public BigDecimal getTotalAddProdcut() {
		return totalAddProdcut;
	}

	public void setTotalAddProdcut(BigDecimal totalAddProdcut) {
		this.totalAddProdcut = totalAddProdcut;
	}

	public BigDecimal getTotalImportProduct() {
		return totalImportProduct;
	}

	public void setTotalImportProduct(BigDecimal totalImportProduct) {
		this.totalImportProduct = totalImportProduct;
	}

	public BigDecimal getTotalAddMovie() {
		return totalAddMovie;
	}

	public void setTotalAddMovie(BigDecimal totalAddMovie) {
		this.totalAddMovie = totalAddMovie;
	}

	@Override
	public String toString() {
		return "TotalSpending [totalAddProdcut=" + totalAddProdcut + ", totalImportProduct=" + totalImportProduct
				+ ", totalAddMovie=" + totalAddMovie + "]";
	}

}
