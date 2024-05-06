package gui.application.form.other;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;

import dao.CustomerRankingDAO;
import dao.CustomersByHourDAO;
import entity.CustomerRanking;
import entity.CustomersByHour;
import net.miginfocom.swing.MigLayout;
import raven.chart.ChartLegendRenderer;
import raven.chart.data.category.DefaultCategoryDataset;
import raven.chart.line.LineChart;

public class FormStatisticsCustomer extends SimpleForm implements ActionListener {

	private JPanel container;
	private JPanel topContainer;
	private JPanel bottomContainer;
	private JPanel topTitleContainer;
	private JPanel topMainContainer;
	private JPanel bottomMainContainer;
	private JPanel bottomTitleContainer;
	private JLabel rankTitleLabel;
	private JComboBox<String> filterByCombobox;
	private JComboBox<String> filterCombobox;
	private RankCustomerTableModel rankCustomerTableModel;
	private JTable rankCustomerTable;
	private CustomerRankingDAO customerRankingDAO;
	private LineChart lineChart;
	private CustomersByHourDAO customersByHourDAO;

	public FormStatisticsCustomer() {

		setLayout(new BorderLayout());

		customerRankingDAO = new CustomerRankingDAO();
		customersByHourDAO = new CustomersByHourDAO();

		container = new JPanel(new MigLayout("wrap, fill, debug", "[fill]", "[fill][grow 0]"));

		topContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));
		topTitleContainer = new JPanel(new MigLayout("wrap, fill", "[]push[][]", "[center]"));
		topMainContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[align top]"));

		bottomContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));
		bottomTitleContainer = new JPanel(new MigLayout("wrap, fill", "[left]", "[]"));
		bottomMainContainer = new JPanel(new MigLayout("wrap, fill"));

		rankTitleLabel = new JLabel("Customer's Spending Ranking");
		filterByCombobox = new JComboBox<String>();
		filterByCombobox.addItem("By month");
		filterByCombobox.addItem("By year");
		filterCombobox = new JComboBox<String>();
		rankCustomerTableModel = new RankCustomerTableModel();
		List<CustomerRanking> customerRankingList = customerRankingDAO
				.getCustomerRankingByMonthAndYear(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
		rankCustomerTableModel.setCustomerRankingList(customerRankingList);
		rankCustomerTable = new JTable(rankCustomerTableModel);

		container.add(topContainer);
		container.add(bottomContainer);
		topContainer.add(topTitleContainer);
		topContainer.add(topMainContainer);
		bottomContainer.add(bottomTitleContainer);
		bottomContainer.add(bottomMainContainer);

		topTitleContainer.add(rankTitleLabel);
		topTitleContainer.add(filterByCombobox);
		topTitleContainer.add(filterCombobox);

		topMainContainer.add(new JScrollPane(rankCustomerTable));

		// styles
		JScrollPane scroll = (JScrollPane) rankCustomerTable.getParent().getParent();
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");
		rankCustomerTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
		rankCustomerTable.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");

		rankCustomerTable.getTableHeader().setDefaultRenderer(
				getAlignmentCellRender(rankCustomerTable.getTableHeader().getDefaultRenderer(), true));
		rankCustomerTable.setDefaultRenderer(Object.class,
				getAlignmentCellRender(rankCustomerTable.getDefaultRenderer(Object.class), false));

		if (rankCustomerTable.getColumnModel().getColumnCount() > 0) {
			rankCustomerTable.getColumnModel().getColumn(1).setPreferredWidth(300);
		}
		rankTitleLabel.putClientProperty(FlatClientProperties.STYLE, "font:$h3.font");

		// action listeners
		filterByCombobox.addActionListener(this);
		filterByCombobox.setSelectedItem("By month");
		// frame
		bottomTitleContainer.add(new JLabel("Customert By Hour"));
		add(container);

		// delete me
//		pack();
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	private TableCellRenderer getAlignmentCellRender(TableCellRenderer oldRender, boolean header) {
		return new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component com = oldRender.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				if (com instanceof JLabel) {
					JLabel label = (JLabel) com;
					if (column == 3) {
						label.setHorizontalAlignment(SwingConstants.CENTER);
					} else {
						label.setHorizontalAlignment(SwingConstants.LEADING);
					}
					if (header == false) {
						if (isSelected) {
							com.setForeground(table.getSelectionForeground());
						} else {
							com.setForeground(table.getForeground());
						}
					}
				}
				return com;
			}
		};
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(filterByCombobox)) {
			// if "by month" is selected, change the items of filter to january to december
			// if "by year" is selected, change the items of filter to 2023 and 2024
			for (ActionListener al : filterCombobox.getActionListeners()) {
				filterCombobox.removeActionListener(al);
			}
			String selectedItem = (String) filterByCombobox.getSelectedItem();
			filterCombobox.removeAllItems();
			if (selectedItem.equals("By month")) {
				filterCombobox.addItem("January");
				filterCombobox.addItem("Feburary");
				filterCombobox.addItem("March");
				filterCombobox.addItem("April");
				filterCombobox.addItem("May");
				filterCombobox.addItem("June");
				filterCombobox.addItem("July");
				filterCombobox.addItem("August");
				filterCombobox.addItem("September");
				filterCombobox.addItem("October");
				filterCombobox.addItem("November");
				filterCombobox.addItem("December");
				for (ActionListener al : filterCombobox.getActionListeners()) {
					filterCombobox.removeActionListener(al);
				}
				filterCombobox.addActionListener(e1 -> {
					List<CustomerRanking> customerRankingList = customerRankingDAO.getCustomerRankingByMonthAndYear(
							filterCombobox.getSelectedIndex() + 1, LocalDate.now().getYear());
					rankCustomerTableModel.setCustomerRankingList(customerRankingList);
					rankCustomerTableModel.fireTableDataChanged();
					bottomMainContainer.removeAll();
					createLineChart(bottomMainContainer, LocalDate.now().getYear(),
							filterCombobox.getSelectedIndex() + 1);
					formRefresh();
				});
				filterCombobox.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
			} else {
				filterCombobox.addItem(LocalDate.now().getYear() - 1 + "");
				filterCombobox.addItem(LocalDate.now().getYear() + "");
				for (ActionListener al : filterCombobox.getActionListeners()) {
					filterCombobox.removeActionListener(al);
				}
				filterCombobox.addActionListener(e2 -> {
					String selectedYearString = (String) filterCombobox.getSelectedItem();
					if (selectedYearString != null) {
						List<CustomerRanking> customerRankingList = customerRankingDAO
								.getCustomerRankingByMonthAndYear(0, Integer.parseInt((String) selectedYearString));
						rankCustomerTableModel.setCustomerRankingList(customerRankingList);
						rankCustomerTableModel.fireTableDataChanged();
						bottomMainContainer.removeAll();
						createLineChart(bottomMainContainer, Integer.parseInt((String) selectedYearString), 0);
						formRefresh();
					}
				});
				filterCombobox.setSelectedIndex(1);
			}
		}
	};

	private void createLineChart(JPanel bottomMainContainer, int year, int month) {
		lineChart = new LineChart();
		lineChart.setChartType(LineChart.ChartType.CURVE);
		lineChart.putClientProperty(FlatClientProperties.STYLE, "" + "border:5,5,5,5,$Component.borderColor,,20");
		bottomMainContainer.add(lineChart);
		createLineChartData(year, month);
	}

	private void createLineChartData(int year, int month) {
		DefaultCategoryDataset<String, String> categoryDataset = new DefaultCategoryDataset<>();
		Map<Integer, Integer> totalCustomersByHour = new HashMap<>();
		Map<Integer, Integer> totalSeatByHour = new HashMap<>();
		for (int hour = 0; hour < 24; hour++) {
			totalCustomersByHour.put(hour, 0);
			totalSeatByHour.put(hour, 0);
		}

		if (month != 0) {
			for (int index = 1; index <= YearMonth.of(year, month).lengthOfMonth(); index++) {
				List<CustomersByHour> customerByHourList = customersByHourDAO.getCustomerCountPerHour(year, month,
						index);
				for (CustomersByHour customerByHour : customerByHourList) {
					int hour = customerByHour.getHour();
					int numberOfCustomers = customerByHour.getNumbersOfCustomer();
					totalCustomersByHour.put(hour, totalCustomersByHour.get(hour) + numberOfCustomers);
				}

				List<CustomersByHour> seatByHourList = customersByHourDAO.GetSeatSoldByHour(year, month, index);
				for (CustomersByHour seatByHour : seatByHourList) {
					int hour = seatByHour.getHour();
					int numberOfCustomers = seatByHour.getNumbersOfCustomer();
					totalSeatByHour.put(hour, totalSeatByHour.get(hour) + numberOfCustomers);
				}
			}
		} else {
			for (int i = 1; i < 13; i++) {
				for (int index = 1; index <= YearMonth.of(year, i).lengthOfMonth(); index++) {
					List<CustomersByHour> customerByHourList = customersByHourDAO.getCustomerCountPerHour(year, i,
							index);
					for (CustomersByHour customerByHour : customerByHourList) {
						int hour = customerByHour.getHour();
						int numberOfCustomers = customerByHour.getNumbersOfCustomer();
						totalCustomersByHour.put(hour, totalCustomersByHour.get(hour) + numberOfCustomers);
					}

					List<CustomersByHour> seatByHourList = customersByHourDAO.GetSeatSoldByHour(year, i, index);
					for (CustomersByHour seatByHour : seatByHourList) {
						int hour = seatByHour.getHour();
						int numberOfCustomers = seatByHour.getNumbersOfCustomer();
						totalSeatByHour.put(hour, totalSeatByHour.get(hour) + numberOfCustomers);
					}
				}
			}
		}

		for (int i = 0; i < totalCustomersByHour.size(); i++) {
			int valueCustomer = totalCustomersByHour.getOrDefault(i, 0);
			categoryDataset.addValue(valueCustomer, "Number of customer", i + "h");

			int valueSeat = totalSeatByHour.getOrDefault(i, 0);
			categoryDataset.addValue(valueSeat, "Number of seat", i + "h");
		}

		lineChart.setLegendRenderer(new ChartLegendRenderer() {
			@Override
			public Component getLegendComponent(Object legend, int index) {
				if (index % 2 == 0 && index <= 24) {
					return super.getLegendComponent(index + "h-" + (index + 2) + "h", index);
				} else {
					return null;
				}
			}
		});

		lineChart.setCategoryDataset(categoryDataset);
		lineChart.getChartColor().addColor(Color.decode("#38bdf8"), Color.decode("#fb7185"), Color.decode("#34d399"));
		JLabel header = new JLabel("Income Data");
		header.putClientProperty(FlatClientProperties.STYLE, "" + "font:+1;" + "border:0,0,5,0");
		lineChart.setHeader(header);
	}

	@Override
	public void formRefresh() {
		lineChart.startAnimation();
	}

}
