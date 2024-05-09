package gui.application.form.other.statistics;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.ProductRanking;

public class RankProductTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private List<ProductRanking> productRankingList;
	private String[] columnNames = { "Index", "Product Name", "Sales quantity", "Revenue" };

	public RankProductTableModel() {
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		default:
			return null;
		}
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		return productRankingList.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ProductRanking productRanking = productRankingList.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return rowIndex + 1;
			case 1:
				return productRanking.getProductName();
			case 2:
				return productRanking.getSalesQty();
			case 3:
				DecimalFormat df = new DecimalFormat("#0.00");
				return "$" + df.format(productRanking.getTotalRevenue());
		}
		return null;
	}
	
	public void setProductRankingList(List<ProductRanking> productRankingList) {
		this.productRankingList = productRankingList;
	}

}
