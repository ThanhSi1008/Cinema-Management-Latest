package gui.application.form.other;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import dao.TotalIncomeDAO;
import dao.TotalSpendingDAO;
import entity.TotalIncome;
import entity.TotalSpending;
import net.miginfocom.swing.MigLayout;
import raven.chart.bar.HorizontalBarChart;
import raven.chart.data.pie.DefaultPieDataset;
import raven.chart.pie.PieChart;

public class FormStatisticsGeneral extends SimpleForm {

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
	private JLabel totalSpending;
	private JLabel totalIncome;
	private JLabel totalSpendingLabel;
	private JLabel incomeTitleLabel;
	private JLabel spendingTitleLabel;
	private PieChart pieChart1;
	private PieChart pieChart2;
	private HorizontalBarChart barChart1;
	private HorizontalBarChart barChart2;
	private TotalSpendingDAO totalSpendingDAO;
	private TotalIncomeDAO totalIncomeDAO;
	private JPanel southContainer;
	private JPanel southLeftContainer;
	private JPanel southRightContainer;

	public FormStatisticsGeneral() {
		totalSpendingDAO = new TotalSpendingDAO();
		totalIncomeDAO = new TotalIncomeDAO();
		setLayout(new BorderLayout());

		container = new JPanel(new MigLayout("wrap, fill", "[fill]", "[min!][fill][fill]"));
		topContainer = new JPanel(new MigLayout("wrap, fill", "[fill][right]", "[fill]"));
		bottomContainer = new JPanel(new MigLayout("wrap, fill", "[fill][fill]", "[fill]"));
		southContainer = new JPanel(new MigLayout("wrap, fill", "[center][center]", "[fill]"));
		southLeftContainer = new JPanel(new MigLayout("wrap, fill"));
		southRightContainer = new JPanel(new MigLayout("wrap, fill"));

		southContainer.add(southLeftContainer);
		southContainer.add(southRightContainer);

		topLeftContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));
		comboboxContainer = new JPanel(new MigLayout("wrap", "[fill][fill]", "[fill]"));
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
		filterCombobox.addItem("January");
		filterByCombobox.addActionListener(e -> {
			String selectedItem = (String) filterByCombobox.getSelectedItem();
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
			} else {
				filterCombobox.addItem("2023");
				filterCombobox.addItem("2024");
			}
		});

		totalIncomeLabel = new JLabel("Total income: ");
		totalSpendingLabel = new JLabel("Total spending: ");
		totalIncome = new JLabel("$7584");
		totalSpending = new JLabel("$12584");

		comboboxContainer.add(filterByCombobox);
		comboboxContainer.add(filterCombobox);

		dataContainer.add(totalIncomeLabel);
		dataContainer.add(totalSpendingLabel);
		dataContainer.add(totalIncome);
		dataContainer.add(totalSpending);

		incomeTitleLabel = new JLabel("Income Pie Chart");

		spendingTitleLabel = new JLabel("Spending Pie Chart");

		// style
		totalIncomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalSpendingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalIncome.setHorizontalAlignment(SwingConstants.CENTER);
		totalSpending.setHorizontalAlignment(SwingConstants.CENTER);

		totalIncome.setOpaque(true);
		totalSpending.setOpaque(true);
		totalIncome.putClientProperty(FlatClientProperties.STYLE,
				"font:$h1.font; background:$App.accent.green; border:10,20,10,20");
		totalSpending.putClientProperty(FlatClientProperties.STYLE,
				"font:$h1.font; background:$App.accent.red; border:10,20,10,20");

		incomeTitleLabel.putClientProperty(FlatClientProperties.STYLE, "font:$h3.font;");
		spendingTitleLabel.putClientProperty(FlatClientProperties.STYLE, "font:$h3.font;");

		createPieChart(bottomLeftMainContainer, bottomRightMainContainer);
		createBarChart(southLeftContainer, southRightContainer);

		// frame
		add(container);
		formRefresh();
	}

	private void createBarChart(JPanel l, JPanel r) {
		// BarChart 1
		barChart1 = new HorizontalBarChart();
		JLabel header1 = new JLabel("Monthly Income");
		header1.putClientProperty(FlatClientProperties.STYLE, "" + "font:+1;" + "border:0,0,5,0");
		barChart1.setHeader(header1);
		barChart1.setBarColor(Color.decode("#f97316"));
		barChart1.setDataset(createData());
		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.putClientProperty(FlatClientProperties.STYLE, "" + "border:5,5,5,5,$Component.borderColor,,20");
		panel1.add(barChart1);
		l.add(panel1, "split 2,gap 0 20");

		// BarChart 2
		barChart2 = new HorizontalBarChart();
		JLabel header2 = new JLabel("Monthly Expense");
		header2.putClientProperty(FlatClientProperties.STYLE, "" + "font:+1;" + "border:0,0,5,0");
		barChart2.setHeader(header2);
		barChart2.setBarColor(Color.decode("#10b981"));
		barChart2.setDataset(createData());
		JPanel panel2 = new JPanel(new BorderLayout());
		panel2.putClientProperty(FlatClientProperties.STYLE, "" + "border:5,5,5,5,$Component.borderColor,,20");
		panel2.add(barChart2);
		r.add(panel2);
	}

	private void createPieChart(JPanel l, JPanel r) {
		pieChart1 = new PieChart();
		JLabel header1 = new JLabel("Total Spending");
		header1.putClientProperty(FlatClientProperties.STYLE, "" + "font:+1");
		pieChart1.setHeader(header1);
		pieChart1.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#22d3ee"), Color.decode("#c084fc"),
				Color.decode("#fb923c"), Color.decode("#fbbf24"), Color.decode("#818cf8"), Color.decode("#34d399"),
				Color.decode("#a3e635"));
		pieChart1.putClientProperty(FlatClientProperties.STYLE, "" + "border:5,5,5,5,$Component.borderColor,,20");
		pieChart1.setDataset(createPieDataForTotalSpending());
		l.add(pieChart1, "split 3,height 290");

		pieChart2 = new PieChart();
		JLabel header2 = new JLabel("Total Income");
		header2.putClientProperty(FlatClientProperties.STYLE, "" + "font:+1");
		pieChart2.setHeader(header2);
		pieChart2.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"),
				Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"),
				Color.decode("#c084fc"));
		pieChart2.putClientProperty(FlatClientProperties.STYLE, "" + "border:5,5,5,5,$Component.borderColor,,20");
		pieChart2.setDataset(createPieDataForTotalIncome());
		r.add(pieChart2, "height 290");
	}

	private DefaultPieDataset<String> createData() {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

		Random random = new Random();
		dataset.addValue("July (ongoing)", random.nextInt(100));
		dataset.addValue("June", random.nextInt(100));
		dataset.addValue("May", random.nextInt(100));
		dataset.addValue("April", random.nextInt(100));
		dataset.addValue("March", random.nextInt(100));
//		dataset.addValue("February", random.nextInt(100));
		return dataset;
	}

	private DefaultPieDataset<String> createPieDataForTotalSpending() {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		TotalSpending totalSpending = totalSpendingDAO.getTotalIncome(2024, 5);
		dataset.addValue("Total Add Product", totalSpending.getTotalAddProdcut());
		dataset.addValue("Total Import Product", totalSpending.getTotalImportProduct());
		dataset.addValue("Total Add Movie", totalSpending.getTotalAddMovie());
		return dataset;
	}

	private DefaultPieDataset<String> createPieDataForTotalIncome() {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		TotalIncome totalIncome = totalIncomeDAO.getTotalIncome(2024, 5);
		dataset.addValue("Total Product", totalIncome.getTotalProduct());
		dataset.addValue("Total Seat", totalIncome.getTotalSeat());
		return dataset;
	}

	@Override
	public void formRefresh() {
//		lineChart.startAnimation();
		pieChart1.startAnimation();
		pieChart2.startAnimation();
//		pieChart3.startAnimation();
		barChart1.startAnimation();
		barChart2.startAnimation();
	}

}
