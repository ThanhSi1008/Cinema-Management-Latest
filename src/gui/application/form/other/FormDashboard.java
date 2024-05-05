package gui.application.form.other;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;

import dao.TotalIncomeDAO;
import dao.TotalSpendingDAO;
import entity.TotalIncome;
import entity.TotalSpending;
import gui.other.DateCalculator;
import net.miginfocom.swing.MigLayout;
import raven.chart.ChartLegendRenderer;
import raven.chart.bar.HorizontalBarChart;
import raven.chart.data.category.DefaultCategoryDataset;
import raven.chart.data.pie.DefaultPieDataset;
import raven.chart.line.LineChart;
import raven.chart.pie.PieChart;

public class FormDashboard extends SimpleForm {
	private static final long serialVersionUID = 1L;
	private LineChart lineChart;
	private HorizontalBarChart barChart1;
	private HorizontalBarChart barChart2;
	private PieChart pieChart1;
	private PieChart pieChart2;
	private PieChart pieChart3;
	private TotalSpendingDAO totalSpendingDAO;
	private TotalIncomeDAO totalIncomeDAO;

	public FormDashboard() {
		totalSpendingDAO = new TotalSpendingDAO();
		totalIncomeDAO = new TotalIncomeDAO();
		System.out.println(totalIncomeDAO.getTotalIncome(2024, 5));
		init();
		formRefresh();
	}

	@Override
	public void formRefresh() {
//		lineChart.startAnimation();
//		pieChart1.startAnimation();
//		pieChart2.startAnimation();
//		pieChart3.startAnimation();
//		barChart1.startAnimation();
//		barChart2.startAnimation();
	}

	@Override
	public void formInitAndOpen() {
		System.out.println("init and open");
	}

	@Override
	public void formOpen() {
		System.out.println("Open");
	}

	private void init() {
		setLayout(new MigLayout("wrap,fill,gap 10", "fill"));
//		createPieChart();
		createLineChart();
//		createBarChart(); 
	}

	private void createPieChart() {
		pieChart1 = new PieChart();
		JLabel header1 = new JLabel("Total Spending");
		header1.putClientProperty(FlatClientProperties.STYLE, "" + "font:+1");
		pieChart1.setHeader(header1);
		pieChart1.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#22d3ee"), Color.decode("#c084fc"),
				Color.decode("#fb923c"), Color.decode("#fbbf24"), Color.decode("#818cf8"), Color.decode("#34d399"),
				Color.decode("#a3e635"));
		pieChart1.putClientProperty(FlatClientProperties.STYLE, "" + "border:5,5,5,5,$Component.borderColor,,20");
		pieChart1.setDataset(createPieDataForTotalSpending());
		add(pieChart1, "split 3,height 290");

		pieChart2 = new PieChart();
		JLabel header2 = new JLabel("Total Income");
		header2.putClientProperty(FlatClientProperties.STYLE, "" + "font:+1");
		pieChart2.setHeader(header2);
		pieChart2.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"),
				Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"),
				Color.decode("#c084fc"));
		pieChart2.putClientProperty(FlatClientProperties.STYLE, "" + "border:5,5,5,5,$Component.borderColor,,20");
		pieChart2.setDataset(createPieDataForTotalIncome());
		add(pieChart2, "height 290");
//
//		pieChart3 = new PieChart();
//		JLabel header3 = new JLabel("Product Profit");
//		header3.putClientProperty(FlatClientProperties.STYLE, "" + "font:+1");
//		pieChart3.setHeader(header3);
//		pieChart3.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"),
//				Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"),
//				Color.decode("#c084fc"));
//		pieChart3.setChartType(PieChart.ChartType.DONUT_CHART);
//		pieChart3.putClientProperty(FlatClientProperties.STYLE, "" + "border:5,5,5,5,$Component.borderColor,,20");
//		pieChart3.setDataset(createPieData());
//		add(pieChart3, "height 290");
	}

	private void createLineChart() {
		lineChart = new LineChart();
		lineChart.setChartType(LineChart.ChartType.CURVE);
		lineChart.putClientProperty(FlatClientProperties.STYLE, "" + "border:5,5,5,5,$Component.borderColor,,20");
		add(lineChart);
		createLineChartData();
	}

	private void createBarChart() {
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
		add(panel1, "split 2,gap 0 20");

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
		add(panel2);
	}

	private DefaultPieDataset<String> createData() {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

		Random random = new Random();
		dataset.addValue("July (ongoing)", random.nextInt(100));
		dataset.addValue("June", random.nextInt(100));
		dataset.addValue("May", random.nextInt(100));
		dataset.addValue("April", random.nextInt(100));
		dataset.addValue("March", random.nextInt(100));
		dataset.addValue("February", random.nextInt(100));
		return dataset;
	}

	private DefaultPieDataset<String> createPieDataForTotalSpending() {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		TotalSpending totalSpending = totalSpendingDAO.getTotalSpending(2024, 5);
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

	private void createLineChartData() {
		DefaultCategoryDataset<String, String> categoryDataset = new DefaultCategoryDataset<>();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
		Random ran = new Random();
		int randomDate = 30;
		for (int i = 1; i <= randomDate; i++) {
			String date = df.format(cal.getTime());
			System.out.println(date);
			categoryDataset.addValue(ran.nextInt(700) + 5, "Income", date);
			categoryDataset.addValue(ran.nextInt(700) + 5, "Expense", date);
			categoryDataset.addValue(ran.nextInt(700) + 5, "Profit", date);

			cal.add(Calendar.DATE, 1);
		}

		/**
		 * Control the legend we do not show all legend
		 */
		try {
			Date date = df.parse(categoryDataset.getColumnKey(0));
			Date dateEnd = df.parse(categoryDataset.getColumnKey(categoryDataset.getColumnCount() - 1));

			DateCalculator dcal = new DateCalculator(date, dateEnd);
			long diff = dcal.getDifferenceDays();

			double d = Math.ceil((diff / 10f));
			lineChart.setLegendRenderer(new ChartLegendRenderer() {
				@Override
				public Component getLegendComponent(Object legend, int index) {
					if (index % d == 0) {
						return super.getLegendComponent(legend, index);
					} else {
						return null;
					}
				}
			});
		} catch (ParseException e) {
			System.err.println(e);
		}

		lineChart.setCategoryDataset(categoryDataset);
		lineChart.getChartColor().addColor(Color.decode("#38bdf8"), Color.decode("#fb7185"), Color.decode("#34d399"));
		JLabel header = new JLabel("Income Data");
		header.putClientProperty(FlatClientProperties.STYLE, "" + "font:+1;" + "border:0,0,5,0");
		lineChart.setHeader(header);
	}

}
