package gui.application.form.other;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

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
import entity.CustomerRanking;
import net.miginfocom.swing.MigLayout;

public class FormStatisticsCustomer extends JPanel implements ActionListener {

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

	public FormStatisticsCustomer() {
		
		setLayout(new BorderLayout());

		customerRankingDAO = new CustomerRankingDAO();

		container = new JPanel(new MigLayout("wrap, fill", "[fill]", "[fill][grow 0]"));

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

		rankCustomerTable.getTableHeader()
				.setDefaultRenderer(getAlignmentCellRender(rankCustomerTable.getTableHeader().getDefaultRenderer(), true));
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
					List<CustomerRanking> customerRankingList = customerRankingDAO.getCustomerRankingByMonthAndYear(filterCombobox.getSelectedIndex()+1, LocalDate.now().getYear());
					rankCustomerTableModel.setCustomerRankingList(customerRankingList);
					rankCustomerTableModel.fireTableDataChanged();
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
						List<CustomerRanking> customerRankingList = customerRankingDAO.getCustomerRankingByMonthAndYear(0, Integer.parseInt((String) selectedYearString));
						rankCustomerTableModel.setCustomerRankingList(customerRankingList);
						rankCustomerTableModel.fireTableDataChanged();
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
//			new FormStatisticsCustomer().setVisible(true);
//		});
//	}

}
