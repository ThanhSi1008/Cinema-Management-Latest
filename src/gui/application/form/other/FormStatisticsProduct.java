package gui.application.form.other;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import dao.ProductRankingDAO;
import entity.MovieRanking;
import entity.ProductRanking;
import net.miginfocom.swing.MigLayout;

public class FormStatisticsProduct extends JPanel implements ActionListener {

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

	public FormStatisticsProduct() {

		setLayout(new BorderLayout());

		productRankingDAO = new ProductRankingDAO();

		container = new JPanel(new MigLayout("wrap, fill", "[fill]", "[fill][grow 0]"));

		topContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));
		topTitleContainer = new JPanel(new MigLayout("wrap, fill", "[]push[][]", "[center]"));
		topMainContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[align top]"));

		rankTitleLabel = new JLabel("Product Revenue Ranking");
		filterByCombobox = new JComboBox<String>();
		filterByCombobox.addItem("By month");
		filterByCombobox.addItem("By year");
		filterCombobox = new JComboBox<String>();
		rankProductTableModel = new RankProductTableModel();
		List<ProductRanking> productRankingList = productRankingDAO
				.getProductRankingByMonthAndYear(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
		rankProductTableModel.setProductRankingList(productRankingList);
		rankProductTable = new JTable(rankProductTableModel);

		container.add(topContainer, "dock center");
		topContainer.add(topTitleContainer);
		topContainer.add(topMainContainer);

		topTitleContainer.add(rankTitleLabel);
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

		// action listeners
		filterByCombobox.addActionListener(this);
		filterByCombobox.setSelectedItem("By month");

		// frame
		add(container);
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
					List<ProductRanking> productRankingList = productRankingDAO.getProductRankingByMonthAndYear(filterCombobox.getSelectedIndex()+1, LocalDate.now().getYear());
					rankProductTableModel.setProductRankingList(productRankingList);
					rankProductTableModel.fireTableDataChanged();
				});
				filterCombobox.setSelectedIndex(LocalDate.now().getMonthValue()-1);
			} else {
				filterCombobox.addItem(LocalDate.now().getYear()-1 + "");
				filterCombobox.addItem(LocalDate.now().getYear() + "");
				for (ActionListener al : filterCombobox.getActionListeners()) {
				    filterCombobox.removeActionListener(al);
				}
				filterCombobox.addActionListener(e2 -> {
					String selectedYearString = (String) filterCombobox.getSelectedItem();
					if (selectedYearString != null) {
						List<ProductRanking> productRankingList = productRankingDAO.getProductRankingByMonthAndYear(0, Integer.parseInt((String) selectedYearString));
						rankProductTableModel.setProductRankingList(productRankingList);
						rankProductTableModel.fireTableDataChanged();
					}
				});
				filterCombobox.setSelectedIndex(1);
			}
		}
	};

//	public static void main(String args[]) {
//		FlatRobotoFont.install();
//		FlatLaf.registerCustomDefaultsSource("gui.theme");
//		UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
//		FlatMacLightLaf.setup();
//		SwingUtilities.invokeLater(() -> {
//			new FormStatisticsProduct().setVisible(true);
//		});
//	}

}
