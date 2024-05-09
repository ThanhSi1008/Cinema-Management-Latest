package gui.application.form.other.statistics;

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
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;

import dao.ProductRankingDAO;
import entity.Employee;
import entity.ProductRanking;
import gui.other.SimpleForm;
import net.miginfocom.swing.MigLayout;
import raven.chart.data.category.DefaultCategoryDataset;
import raven.chart.line.LineChart;

public class FormStatisticsProduct extends SimpleForm implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JPanel container;
	private JPanel topContainer;
	private JPanel topTitleContainer;
	private JPanel topMainContainer;
	private JLabel rankTitleLabel;
	private JComboBox<String> filterByCombobox;
	private JComboBox<String> filterCombobox;
	private RankProductTableModel rankProductTableModel;
	private ProductRankingDAO productRankingDAO;
	private JTable rankProductTable;
	private LineChart lineChart;
	private JPanel bottomContainer;
	private JButton exportButton;

	public FormStatisticsProduct(Employee emp) {

		setLayout(new BorderLayout());

		productRankingDAO = new ProductRankingDAO();

		container = new JPanel(new MigLayout("wrap, fill", "[fill]", "[fill][grow 0]"));

		topContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));
		topTitleContainer = new JPanel(new MigLayout("wrap, fill", "[]push[][][]", "[center]"));
		topMainContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[align top]"));
		bottomContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[fill]"));

		rankTitleLabel = new JLabel("Product Revenue Ranking");
		filterByCombobox = new JComboBox<String>();
		filterByCombobox.addItem("By month");
		filterByCombobox.addItem("By year");
		exportButton = new JButton("Export Statistical");
		filterCombobox = new JComboBox<String>();
		rankProductTableModel = new RankProductTableModel();
		List<ProductRanking> productRankingList = productRankingDAO
				.getProductRankingByMonthAndYear(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
		rankProductTableModel.setProductRankingList(productRankingList);
		rankProductTable = new JTable(rankProductTableModel);

		container.add(topContainer, "dock center");
		container.add(bottomContainer);
		topContainer.add(topTitleContainer);
		topContainer.add(topMainContainer);

		topTitleContainer.add(rankTitleLabel);
		if (emp.getRole().equals("Manager")) {
			topTitleContainer.add(exportButton);

		}
		topTitleContainer.add(filterByCombobox);
		topTitleContainer.add(filterCombobox);

		topMainContainer.add(new JScrollPane(rankProductTable));

		// styles
		JScrollPane scroll = (JScrollPane) rankProductTable.getParent().getParent();
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");
		rankProductTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
		rankProductTable.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");

		rankProductTable.getTableHeader().setDefaultRenderer(
				getAlignmentCellRender(rankProductTable.getTableHeader().getDefaultRenderer(), true));
		rankProductTable.setDefaultRenderer(Object.class,
				getAlignmentCellRender(rankProductTable.getDefaultRenderer(Object.class), false));

		if (rankProductTable.getColumnModel().getColumnCount() > 0) {
			rankProductTable.getColumnModel().getColumn(1).setPreferredWidth(300);
		}
		rankTitleLabel.putClientProperty(FlatClientProperties.STYLE, "font:$h3.font");

		createLineChart(bottomContainer);
		// action listeners
		filterByCombobox.addActionListener(this);
		exportButton.addActionListener(this);
		filterByCombobox.setSelectedItem("By month");

		// frame
		add(container);

	}

	private TableCellRenderer getAlignmentCellRender(TableCellRenderer oldRender, boolean header) {
		return new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component com = oldRender.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				if (com instanceof JLabel) {
					JLabel label = (JLabel) com;
					if (column == 2 || column == 3) {
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
	};

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
					List<ProductRanking> topProducts = createTableModelData(LocalDate.now().getYear(),
							filterCombobox.getSelectedIndex() + 1);

					setDataSet(LocalDate.now().getYear(), filterCombobox.getSelectedIndex() + 1, topProducts);
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

						List<ProductRanking> topProducts = createTableModelData(
								Integer.parseInt((String) selectedYearString), 0);

						setDataSet(Integer.parseInt((String) selectedYearString), 0, topProducts);
						formRefresh();
					}
				});
				filterCombobox.setSelectedIndex(1);
			}
		}
		if (e.getSource().equals(exportButton)) {
			JFileChooser fileChooser = new JFileChooser();

			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int result = fileChooser.showOpenDialog(null);

			if (result == JFileChooser.APPROVE_OPTION) {
				String selectedFolderPath = fileChooser.getSelectedFile().getAbsolutePath();
				if (filterByCombobox.getSelectedItem().equals("By month")) {
					System.out.println(filterCombobox.getSelectedItem());
					System.out.println(selectedFolderPath);
					boolean success = productRankingDAO
							.exportProductRankingToCSV(
									productRankingDAO.getProductRankingByMonthAndYear(
											filterCombobox.getSelectedIndex() + 1, LocalDate.now().getYear()),
									selectedFolderPath);
					if (success) {
						JOptionPane.showMessageDialog(null,
								"Data has been successfully exported to the CSV file in the directory: "
										+ selectedFolderPath);
					} else {
						JOptionPane.showMessageDialog(null,
								"There was an error while exporting the data to the CSV file.");
					}
				} else {
					System.out.println(filterCombobox.getSelectedItem());
					System.out.println(selectedFolderPath);
					boolean success = productRankingDAO
							.exportProductRankingToCSV(
									productRankingDAO.getProductRankingByMonthAndYear(0,
											Integer.valueOf((String) filterCombobox.getSelectedItem())),
									selectedFolderPath);
					if (success) {
						JOptionPane.showMessageDialog(null,
								"Data has been successfully exported to the CSV file in the directory: "
										+ selectedFolderPath);
					} else {
						JOptionPane.showMessageDialog(null,
								"There was an error while exporting the data to the CSV file.");
					}
				}
			}
		}
	};

	private List<ProductRanking> createTableModelData(int year, int month) {
		List<ProductRanking> productRankingList = productRankingDAO.getProductRankingByMonthAndYear(month, year);
		rankProductTableModel.setProductRankingList(productRankingList);
		rankProductTableModel.fireTableDataChanged();

		List<ProductRanking> topProduct = productRankingList.stream().limit(3).collect(Collectors.toList());
		return topProduct;
	}

	private void createLineChart(JPanel bottomMainContainer) {
		lineChart = new LineChart();
		lineChart.setChartType(LineChart.ChartType.CURVE);
		lineChart.putClientProperty(FlatClientProperties.STYLE, "" + "border:5,5,5,5,$Component.borderColor,,20");
		bottomMainContainer.add(lineChart);
		lineChart.getChartColor().addColor(Color.decode("#38bdf8"), Color.decode("#fb7185"), Color.decode("#34d399"));
		JLabel header = new JLabel("Detail Infomation");
		header.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font");
		lineChart.setHeader(header);
	}

	private DefaultCategoryDataset<String, String> createData(int year, int month, List<ProductRanking> topProducts) {
		DefaultCategoryDataset<String, String> categoryDataset = new DefaultCategoryDataset<>();
		Map<String, List<ProductRanking>> myMap = new HashMap<String, List<ProductRanking>>();

		if (month != 0) {
			for (int i = 1; i <= YearMonth.of(year, month).lengthOfMonth(); i++) {
				myMap.put(toMonthDay(month, i), productRankingDAO.getAllProductsSoldEachDay(year, month, i));
			}

			for (int i = 1; i <= myMap.size(); i += 2) {
				for (ProductRanking product : myMap.get(toMonthDay(month, i))) {
					for (int j = 0; j < 3; j++) {
						if (product.getProductName().equals(topProducts.get(j).getProductName())) {
							categoryDataset.addValue(product.getTotalRevenue(), product.getProductName(),
									toMonthDay(month, i));
						}
					}
				}

			}
		} else {
			for (int i = 1; i <= 12; i++) {
				myMap.put(toMonth(i), productRankingDAO.getProductRankingByMonthAndYear(i, year));
			}

			for (int i = 1; i <= 12; i++) {
				for (ProductRanking product : myMap.get(toMonth(i))) {
					for (int j = 0; j < 3; j++) {
						if (product.getProductName().equals(topProducts.get(j).getProductName())) {
							categoryDataset.addValue(product.getTotalRevenue(), product.getProductName(), toMonth(i));
						}
					}
				}
			}
		}

		return categoryDataset;
	}

	private void setDataSet(int year, int month, List<ProductRanking> topProducts) {
		lineChart.setCategoryDataset(createData(year, month, topProducts));
	}

	@Override
	public void formRefresh() {
		lineChart.startAnimation();
	}

	public String toMonthDay(int month, int day) {
		String monthStr = switch (month) {
		case 1 -> "Jan";
		case 2 -> "Feb";
		case 3 -> "Mar";
		case 4 -> "Apr";
		case 5 -> "May";
		case 6 -> "Jun";
		case 7 -> "Jul";
		case 8 -> "Aug";
		case 9 -> "Sep";
		case 10 -> "Oct";
		case 11 -> "Nov";
		case 12 -> "Dec";
		default -> "Invalid month number";
		};

		return monthStr + " " + day;
	}

	public String toMonth(int month) {
		switch (month) {
		case 1:
			return "January";
		case 2:
			return "February";
		case 3:
			return "March";
		case 4:
			return "April";
		case 5:
			return "May";
		case 6:
			return "June";
		case 7:
			return "July";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "October";
		case 11:
			return "November";
		case 12:
			return "December";
		default:
			return "Invalid month number";
		}
	}

}
