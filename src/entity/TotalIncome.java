package entity;

import java.math.BigDecimal;

public class TotalIncome {
	private BigDecimal totalProduct;
	private BigDecimal totalSeat;

	public TotalIncome(BigDecimal totalProduct, BigDecimal totalSeat) {
		super();
		this.totalProduct = totalProduct;
		this.totalSeat = totalSeat;
	}

	public BigDecimal getTotalProduct() {
		return totalProduct;
	}

	public void setTotalProduct(BigDecimal totalProduct) {
		this.totalProduct = totalProduct;
	}

	public BigDecimal getTotalSeat() {
		return totalSeat;
	}

	public void setTotalSeat(BigDecimal totalSeat) {
		this.totalSeat = totalSeat;
	}

	@Override
	public String toString() {
		return "TotalIncome [totalProduct=" + totalProduct + ", totalSeat=" + totalSeat + "]";
	}

}
