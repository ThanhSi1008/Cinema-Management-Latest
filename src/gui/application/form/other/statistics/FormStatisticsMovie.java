package gui.application.form.other.statistics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
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

import dao.MovieRankingDAO;
import entity.Employee;
import entity.MovieRanking;
import gui.other.SimpleForm;
import net.miginfocom.swing.MigLayout;
import raven.chart.bar.HorizontalBarChart;
import raven.chart.data.pie.DefaultPieDataset;

public class FormStatisticsMovie extends SimpleForm implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JPanel container;
	private JPanel topContainer;
	private JPanel topTitleContainer;
	private JPanel topMainContainer;
	private JLabel rankTitleLabel;
	private JComboBox<String> filterByCombobox;
	private JComboBox<String> filterCombobox;
	private RankMovieTableModel rankMovieTableModel;
	private MovieRankingDAO movieRankingDAO;
	private JTable rankMovieTable;
	private JPanel bottomContainer;
	private HorizontalBarChart barChart;
	private JButton exportButton;

	public FormStatisticsMovie(Employee emp) {

		setLayout(new BorderLayout());

		movieRankingDAO = new MovieRankingDAO();

		container = new JPanel(new MigLayout("wrap, fill", "[fill]", "[fill][grow 0]"));

		topContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));
		topTitleContainer = new JPanel(new MigLayout("wrap, fill", "[]push[][][]", "[center]"));
		topMainContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[align top]"));

		bottomContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));

		rankTitleLabel = new JLabel("Movie View Ranking");
		filterByCombobox = new JComboBox<String>();
		filterByCombobox.addItem("By month");
		filterByCombobox.addItem("By year");
		exportButton = new JButton("Export Statistical");
		filterCombobox = new JComboBox<String>();
		rankMovieTableModel = new RankMovieTableModel();
		List<MovieRanking> movieRankingList = movieRankingDAO
				.getMovieRankingByMonthAndYear(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
		rankMovieTableModel.setMovieRankingList(movieRankingList);
		rankMovieTable = new JTable(rankMovieTableModel);

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

		topMainContainer.add(new JScrollPane(rankMovieTable));

		// styles
		JScrollPane scroll = (JScrollPane) rankMovieTable.getParent().getParent();
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");
		rankMovieTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
		rankMovieTable.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");

		rankMovieTable.getTableHeader()
				.setDefaultRenderer(getAlignmentCellRender(rankMovieTable.getTableHeader().getDefaultRenderer(), true));
		rankMovieTable.setDefaultRenderer(Object.class,
				getAlignmentCellRender(rankMovieTable.getDefaultRenderer(Object.class), false));

		if (rankMovieTable.getColumnModel().getColumnCount() > 0) {
			rankMovieTable.getColumnModel().getColumn(1).setPreferredWidth(300);
		}
		rankTitleLabel.putClientProperty(FlatClientProperties.STYLE, "font:$h3.font");

		createBarChart(bottomContainer);

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
			formRefresh();
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
					List<MovieRanking> movieRankingList = movieRankingDAO.getMovieRankingByMonthAndYear(
							filterCombobox.getSelectedIndex() + 1, LocalDate.now().getYear());
					rankMovieTableModel.setMovieRankingList(movieRankingList);
					rankMovieTableModel.fireTableDataChanged();
					barChart.setDataset(createData(movieRankingList));
					repaint();
					revalidate();
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
						List<MovieRanking> movieRankingList = movieRankingDAO.getMovieRankingByMonthAndYear(0,
								Integer.parseInt((String) selectedYearString));
						rankMovieTableModel.setMovieRankingList(movieRankingList);
						rankMovieTableModel.fireTableDataChanged();
						barChart.setDataset(createData(movieRankingList));
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
				if (filterByCombobox.getSelectedItem().equals("By month")) {
					System.out.println(filterCombobox.getSelectedItem());
					System.out.println(selectedFolderPath);
					boolean success = movieRankingDAO.exportMovieRankingToCSV(
							movieRankingDAO.getMovieRankingByMonthAndYear(filterCombobox.getSelectedIndex() + 1,
									LocalDate.now().getYear()),
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
					boolean success = movieRankingDAO
							.exportMovieRankingToCSV(
									movieRankingDAO.getMovieRankingByMonthAndYear(0,
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

	private void createBarChart(JPanel panel) {
		barChart = new HorizontalBarChart();
		JLabel header = new JLabel("Compare Revenue of Movie");
		header.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font");
		barChart.setHeader(header);
		barChart.setBarColor(Color.decode("#f97316"));
		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.putClientProperty(FlatClientProperties.STYLE, "" + "border:5,5,5,5,$Component.borderColor,,20");
		panel1.add(barChart);
		panel.add(panel1, "split 2,gap 0 20");
	}

	private DefaultPieDataset<String> createData(List<MovieRanking> movieRankingList) {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

		List<MovieRanking> topMovies = movieRankingList.stream()
				.sorted(Comparator.comparingDouble(MovieRanking::getRevenue).reversed()).limit(7)
				.collect(Collectors.toList());

		for (MovieRanking movieRanking : topMovies) {
			dataset.setValue(movieRanking.getMovieName(), movieRanking.getRevenue());
		}

		return dataset;
	}

	@Override
	public void formRefresh() {
		barChart.startAnimation();
	}

}
