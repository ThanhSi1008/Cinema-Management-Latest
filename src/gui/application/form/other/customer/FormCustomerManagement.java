package gui.application.form.other.customer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.CustomerDAO;
import entity.Customer;
import net.miginfocom.swing.MigLayout;

public class FormCustomerManagement extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField searchTextField;
	private JButton updateButton;
	private JButton deleteButton;
	private JComboBox<String> filterComboBox;
	private JPanel container0;
	private JPanel container1;

	private CustomerDAO customerDAO;
	private CustomerTableModel customerTableModel;
	private JTable customerTable;
	private CustomerUpdateDialog customerUpdateDialog;

	public FormCustomerManagement() {

		customerDAO = new CustomerDAO();

		setLayout(new BorderLayout());
		container0 = new JPanel();
		container1 = new JPanel();
		searchTextField = new JTextField();
		searchTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,  "Search");
		updateButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		filterComboBox = new JComboBox<String>();
		filterComboBox.addItem("By Name");
		filterComboBox.addItem("By Phone Number");
		container1.setLayout(new MigLayout("", "[][]push[][]", ""));
		container1.add(searchTextField, "w 200!");
		container1.add(filterComboBox);
		container1.add(updateButton);
		container1.add(deleteButton);

		updateButton.setIcon(new FlatSVGIcon("gui/icon/svg/edit.svg", 0.35f));
		deleteButton.setIcon(new FlatSVGIcon("gui/icon/svg/delete.svg", 0.35f));
		searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON,
				new FlatSVGIcon("gui/icon/svg/search.svg", 0.35f));

		customerTableModel = new CustomerTableModel();
		customerTable = new JTable(customerTableModel);
		container0.setLayout(new MigLayout("wrap, fill, insets 15", "[fill]", "[grow 0][fill]"));
		container0.add(container1);
		container0.add(new JScrollPane(customerTable));

		if (customerTable.getColumnModel().getColumnCount() > 0) {
			customerTable.getColumnModel().getColumn(3).setPreferredWidth(300);
		}

		// Change scroll style
		JScrollPane scroll = (JScrollPane) customerTable.getParent().getParent();
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");

		customerTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
		customerTable.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");

		// To Create table alignment
		customerTable.getTableHeader()
				.setDefaultRenderer(getAlignmentCellRender(customerTable.getTableHeader().getDefaultRenderer(), true));
		customerTable.setDefaultRenderer(Object.class,
				getAlignmentCellRender(customerTable.getDefaultRenderer(Object.class), false));

		filterComboBox.addActionListener(this);
		updateButton.addActionListener(this);
		deleteButton.addActionListener(this);

		searchTextField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				handleSearch();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				handleSearch();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				handleSearch();
			}
		});

		add(container0);

	}

	@SuppressWarnings("serial")
	private TableCellRenderer getAlignmentCellRender(TableCellRenderer oldRender, boolean header) {
		return new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component com = oldRender.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				if (com instanceof JLabel) {
					JLabel label = (JLabel) com;
					if (column == 4) {
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
		if (e.getSource().equals(deleteButton)) {
			int selectedRow = customerTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "Please select a row to delete.");
			} else {
				int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?", "Warning", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					String customerID = (String) customerTable.getValueAt(selectedRow, 0);
					boolean isSuccessful = customerDAO.removeCustomerByID(customerID);
					if (isSuccessful) {
						handleSearch();
					} else {
						JOptionPane.showMessageDialog(this, "Cannot delete movie", "Failed", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		if (e.getSource().equals(updateButton)) {
			Thread thread = new Thread(() -> {
				int selectedRow = customerTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(this, "Please select a row to update.");
				} else {
					String customerID = (String) customerTable.getValueAt(selectedRow, 0);
					Customer customer = customerDAO.getCustomerByID(customerID);
					customerUpdateDialog = new CustomerUpdateDialog(customer);
					customerUpdateDialog.setFormCustomerManagement(this);
					customerUpdateDialog.setModal(true);
					customerUpdateDialog.setVisible(true);
				}
			});
			thread.start();
		}

	};
	
	public void handleSearch() {
		String searchBy = (String) filterComboBox.getSelectedItem();
		String searchText = searchTextField.getText().trim();
		if (searchBy.equals("By Name")) {
			List<Customer> customerList = customerDAO.findCustomerByName(searchText);
			customerTableModel.setCustomerList(customerList);
			customerTableModel.fireTableDataChanged();
		} else if (searchBy.equals("By Phone Number")) {
			List<Customer> customerList = customerDAO.findCustomerByPhoneNumber(searchText);
			customerTableModel.setCustomerList(customerList);
			customerTableModel.fireTableDataChanged();
		}
	}

}
