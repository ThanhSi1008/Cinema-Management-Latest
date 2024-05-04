package gui.application.form.other;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import dao.CustomerDAO;
import entity.Customer;

public class RankCustomerTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private List<CustomerRanking> customerRankingList;
	private String[] columnNames = { "CustomerID", "Customer Name", "Phone Number", "Total Spending"};

	public RankCustomerTableModel() {
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
		return customerRankingList.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		CustomerRanking customerRanking = customerRankingList.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return customerRanking.getCustomerID();
			case 1:
				return customerRanking.getCustomerName();
			case 2:
				return customerRanking.getPhoneNumber();
			case 3:
				return "$" + customerRanking.getTotal();
		}
		return null;
	}
	
	public void setCustomerRankingList(List<CustomerRanking> customerRankingList) {
		this.customerRankingList = customerRankingList;
	}

}
