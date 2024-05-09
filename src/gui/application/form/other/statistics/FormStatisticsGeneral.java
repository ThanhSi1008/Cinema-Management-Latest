package gui.application.form.other.statistics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import dao.TotalIncomeDAO;
import dao.TotalSpendingDAO;
import entity.Employee;
import entity.TotalIncome;
import entity.TotalSpending;
import gui.other.SimpleForm;
import net.miginfocom.swing.MigLayout;
import raven.chart.bar.HorizontalBarChart;
import raven.chart.data.pie.DefaultPieDataset;
import raven.chart.pie.PieChart;

public class FormStatisticsGeneral extends SimpleForm implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel container;
	private JPanel topContainer;
	private JPanel bottomContainer;
	private JPanel comboboxContainer;
	private JPanel dataContainer;
	private JPanel bottomLeftContainer;
	private JPanel bottomRightContainer;
	private JPanel bottomLeftMainContainer;
	private JPanel bottomRightMainContainer;
	private JPanel topLeftContainer;
	private JComboBox<String> filterByCombobox;
	private JComboBox<String> filterCombobox;
	private JLabel totalIncomeLabel;
	private JLabel totalSpendingLabel;
	private JLabel incomeTitleLabel;
	private JLabel spendingTitleLabel;
	private PieChart pieChart1;
	private PieChart pieChart2;
	private TotalSpendingDAO totalSpendingDAO;
	private TotalIncomeDAO totalIncomeDAO;
	private JPanel southContainer;
	private HorizontalBarChart barChart;
	private JButton totalIncome;
	private JButton totalSpending;
	private JButton exportButton;

	public FormStatisticsGeneral(Employee emp) {
		totalSpendingDAO = new TotalSpendingDAO();
		totalIncomeDAO = new TotalIncomeDAO();
		setLayout(new BorderLayout());

		container = new JPanel(new MigLayout("wrap, fill", "[fill]", "[min!][fill][fill]"));
		topContainer = new JPanel(new MigLayout("wrap, fill", "[fill][right]", "[fill]"));
		bottomContainer = new JPanel(new MigLayout("wrap, fill", "[fill][fill]", "[fill]"));
		southContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[fill]"));

		topLeftContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));
		comboboxContainer = new JPanel(new MigLayout("wrap", "[fill][fill][fill]", "[fill]"));
		dataContainer = new JPanel(new MigLayout("wrap", "[fill][fill]", "[][fill]"));
		bottomLeftContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));
		bottomRightContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));

		bottomLeftMainContainer = new JPanel(new MigLayout("wrap, fill", "[fill][grow 0]", "[]"));

		bottomRightMainContainer = new JPanel(new MigLayout("wrap, fill", "[fill][grow 0]", "[]"));

		container.add(topContainer);
		container.add(bottomContainer);
		container.add(southContainer);
		topContainer.add(topLeftContainer);
		topContainer.add(comboboxContainer);
		topLeftContainer.add(dataContainer);
		bottomContainer.add(bottomLeftContainer);
		bottomContainer.add(bottomRightContainer);
		bottomLeftContainer.add(bottomLeftMainContainer);
		bottomRightContainer.add(bottomRightMainContainer);

		filterByCombobox = new JComboBox<String>();
		filterByCombobox.addItem("By month");
		filterByCombobox.addItem("By year");
		filterCombobox = new JComboBox<String>();

		totalIncomeLabel = new JLabel("Total Income: ");
		totalSpendingLabel = new JLabel("Total Spending: ");
		totalIncome = new JButton();
		totalSpending = new JButton();
		exportButton = new JButton("Export Statistical");

		if (emp.getRole().equals("Manager")) {
			comboboxContainer.add(exportButton);
		}
		comboboxContainer.add(filterByCombobox);
		comboboxContainer.add(filterCombobox);

		dataContainer.add(totalIncomeLabel);
		dataContainer.add(totalSpendingLabel);
		dataContainer.add(totalIncome);
		dataContainer.add(totalSpending);

		incomeTitleLabel = new JLabel("Income Pie Chart");

		spendingTitleLabel = new JLabel("Spending Pie Chart");

		// style
		totalIncomeLabel.putClientProperty(FlatClientProperties.STYLE, "font:$h5.font");
		totalSpendingLabel.putClientProperty(FlatClientProperties.STYLE, "font:$h5.font");
		totalIncome.setHorizontalAlignment(SwingConstants.CENTER);
		totalSpending.setHorizontalAlignment(SwingConstants.CENTER);

		totalIncome.setOpaque(true);
		totalSpending.setOpaque(true);
		totalIncome.putClientProperty(FlatClientProperties.STYLE,
				"font:$h1.font; background:#3b934b; arc:10; margin:10,20,10,20; foreground:#fff;");
		totalSpending.putClientProperty(FlatClientProperties.STYLE,
				"font:$h1.font; background:#b44244; arc:10; margin:10,20,10,20; foreground:#fff;");

		incomeTitleLabel.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font;");
		spendingTitleLabel.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font;");

		createLeftPieChart(bottomRightMainContainer);
		createRightPieChart(bottomLeftMainContainer);
		createBarChart(southContainer);
		formRefresh();

		// action listener
		filterByCombobox.addActionListener(this);
		exportButton.addActionListener(this);
		filterByCombobox.setSelectedItem("By month");

		// frame
		add(container);
	}

	private void createBarChart(JPanel panel) {
		barChart = new HorizontalBarChart();
		JLabel header = new JLabel("Compare Total Spending And Total Income");
		barChart.setHeader(header);
		barChart.setBarColor(Color.decode("#f97316"));
		JPanel panel1 = new JPanel(new BorderLayout());
		header.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font");
		panel1.add(barChart);
		panel.add(panel1, "split 2,gap 0 20");
	}

	private void createLeftPieChart(JPanel panel) {
		pieChart1 = new PieChart();
		JLabel header1 = new JLabel("Total Spending");
		header1.putClientProperty(FlatClientProperties.STYLE, "font:$h5.font");
		pieChart1.setHeader(header1);
		pieChart1.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#22d3ee"), Color.decode("#c084fc"),
				Color.decode("#fb923c"), Color.decode("#fbbf24"), Color.decode("#818cf8"), Color.decode("#34d399"),
				Color.decode("#a3e635"));
		pieChart1.putClientProperty(FlatClientProperties.STYLE, "" + "border:5,5,5,5,$Component.borderColor,,20");
		panel.add(pieChart1, "split 3,height 290");
	}

	private void createRightPieChart(JPanel panel) {
		pieChart2 = new PieChart();
		JLabel header2 = new JLabel("Total Income");
		header2.putClientProperty(FlatClientProperties.STYLE, "" + "font:+1");
		header2.putClientProperty(FlatClientProperties.STYLE, "font:$h5.font");
		pieChart2.setHeader(header2);
		pieChart2.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"),
				Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"),
				Color.decode("#c084fc"));
		pieChart2.putClientProperty(FlatClientProperties.STYLE, "" + "border:5,5,5,5,$Component.borderColor,,20");
		panel.add(pieChart2, "height 290");
	}

	private DefaultPieDataset<String> createData(TotalIncome income, TotalSpending spending) {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

		dataset.addValue("TotalIcome", income.getTotalProduct().add(income.getTotalSeat()));
		dataset.addValue("TotalSpending",
				spending.getTotalAddMovie().add(spending.getTotalAddProdcut()).add(spending.getTotalImportProduct()));
		return dataset;
	}

	private DefaultPieDataset<String> createPieDataForTotalSpending(TotalSpending totalSpending) {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		dataset.addValue("Total Add Product", totalSpending.getTotalAddProdcut());
		dataset.addValue("Total Import Product", totalSpending.getTotalImportProduct());
		dataset.addValue("Total Add Movie", totalSpending.getTotalAddMovie());
		return dataset;
	}

	private DefaultPieDataset<String> createPieDataForTotalIncome(TotalIncome totalIncome) {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		dataset.addValue("Total Product", totalIncome.getTotalProduct());
		dataset.addValue("Total Seat", totalIncome.getTotalSeat());
		return dataset;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(filterByCombobox)) {
			formRefresh();
			// if "by month" is selected, change the items of filter to january to december
			// if "by year" is selected, change the items of filter to 2023 and 2024
			for (ActionListener al : filterCombobox.getActionListeners()) {
				filterCombobox.removeActionListener(al);
			}
			String selectedItem = (String) filterByCombobox.getSelectedItem();
			filterCombobox.removeAllItems();
			if (selectedItem.equals("By month")) {
				formRefresh();
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

					TotalIncome returnedTotalIncome = totalIncomeDAO.getTotalIncome(LocalDate.now().getYear(),
							filterCombobox.getSelectedIndex() + 1);
					TotalSpending returnedTotalSpending = totalSpendingDAO.getTotalSpending(LocalDate.now().getYear(),
							filterCombobox.getSelectedIndex() + 1);

					totalIncome.setText(
							"$" + returnedTotalIncome.getTotalProduct().add(returnedTotalIncome.getTotalSeat()) + "");
					totalSpending
							.setText("$"
									+ returnedTotalSpending.getTotalAddMovie().add(returnedTotalSpending
											.getTotalAddProdcut().add(returnedTotalSpending.getTotalImportProduct()))
									+ "");

					pieChart1.setDataset(createPieDataForTotalSpending(returnedTotalSpending));
					pieChart2.setDataset(createPieDataForTotalIncome(returnedTotalIncome));
					barChart.setDataset(createData(returnedTotalIncome, returnedTotalSpending));
					if (returnedTotalSpending.getTotalAddMovie().compareTo(BigDecimal.valueOf(0)) == 0
							&& returnedTotalSpending.getTotalAddProdcut().compareTo(BigDecimal.valueOf(0)) == 0
							&& returnedTotalSpending.getTotalImportProduct().compareTo(BigDecimal.valueOf(0)) == 0) {
						pieChart1.setVisible(false);
					} else {
						pieChart1.setVisible(true);
					}
					if (returnedTotalIncome.getTotalProduct().compareTo(BigDecimal.valueOf(0)) == 0
							&& returnedTotalIncome.getTotalSeat().compareTo(BigDecimal.valueOf(0)) == 0) {
						pieChart2.setVisible(false);
					} else {
						pieChart2.setVisible(true);
					}
					repaint();
					revalidate();
					formRefresh();
				});
				filterCombobox.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
			} else {
				formRefresh();
				filterCombobox.addItem(LocalDate.now().getYear() - 1 + "");
				filterCombobox.addItem(LocalDate.now().getYear() + "");
				for (ActionListener al : filterCombobox.getActionListeners()) {
					filterCombobox.removeActionListener(al);
				}
				filterCombobox.addActionListener(e2 -> {
					String selectedYearString = (String) filterCombobox.getSelectedItem();
					if (selectedYearString != null) {
						TotalIncome returnedTotalIncome = totalIncomeDAO
								.getTotalIncome(Integer.parseInt((String) selectedYearString), 0);
						TotalSpending returnedTotalSpending = totalSpendingDAO
								.getTotalSpending(Integer.parseInt((String) selectedYearString), 0);
						totalIncome.setText("$"
								+ returnedTotalIncome.getTotalProduct().add(returnedTotalIncome.getTotalSeat()) + "");
						totalSpending.setText("$" + returnedTotalSpending.getTotalAddMovie()
								.add(returnedTotalSpending.getTotalAddProdcut())
								.add(returnedTotalSpending.getTotalImportProduct()) + "");
						pieChart1.setDataset(createPieDataForTotalSpending(returnedTotalSpending));
						pieChart2.setDataset(createPieDataForTotalIncome(returnedTotalIncome));
						barChart.setDataset(createData(returnedTotalIncome, returnedTotalSpending));
						if (returnedTotalIncome.getTotalProduct().compareTo(BigDecimal.valueOf(0)) == 0
								&& returnedTotalIncome.getTotalSeat().compareTo(BigDecimal.valueOf(0)) == 0) {
							pieChart2.setVisible(false);
						} else {
							pieChart2.setVisible(true);
						}
						if (returnedTotalSpending.getTotalAddMovie().compareTo(BigDecimal.valueOf(0)) == 0
								&& returnedTotalSpending.getTotalAddProdcut().compareTo(BigDecimal.valueOf(0)) == 0
								&& returnedTotalSpending.getTotalImportProduct()
										.compareTo(BigDecimal.valueOf(0)) == 0) {
							pieChart1.setVisible(false);
						} else {
							pieChart1.setVisible(true);
						}
						repaint();
						revalidate();
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

				boolean exportOrderAddProduct = totalSpendingDAO.exportOrderAddProduct(selectedFolderPath);
				boolean exportOrderImportProduct = totalSpendingDAO.exportOrderImportProduct(selectedFolderPath);
				boolean exportOrderAddMovie = totalSpendingDAO.exportOrderAddMovie(selectedFolderPath);
				boolean exportOrderSummary = totalSpendingDAO.exportOrderSummary(selectedFolderPath);

				if (exportOrderAddProduct && exportOrderImportProduct && exportOrderAddMovie && exportOrderSummary) {
					JOptionPane.showMessageDialog(null,
							"Data has been successfully exported to the CSV file in the directory: "
									+ selectedFolderPath);
				} else {
					JOptionPane.showMessageDialog(null, "There was an error while exporting the data to the CSV file.");
				}
			}
		}

	};

	@Override
	public void formRefresh() {
		pieChart1.startAnimation();
		pieChart2.startAnimation();
		barChart.startAnimation();
	}
}
