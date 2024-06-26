package gui.application.form.other.staff;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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

import dao.EmployeeDAO;
import entity.Employee;
import net.miginfocom.swing.MigLayout;

public class FormStaffManagement extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField searchTextField;
	private JButton updateButton;
	private JButton deleteButton;
	private JPanel container0;
	private JPanel container1;

	private EmployeeDAO employeeDAO;
	private JButton addNewButton;
	private StaffTableModel staffTableModel;
	private JTable employeeTable;
	private StaffAddingDialog staffAddingDialog;
	private StaffUpdateDialog staffUpdateDialog;
	private Employee currentEmployee;

	public FormStaffManagement(Employee currentEmployee) {
		
		this.currentEmployee = currentEmployee;
		
		employeeDAO = new EmployeeDAO();

		setLayout(new BorderLayout());
		container0 = new JPanel();
		container1 = new JPanel();
		searchTextField = new JTextField();
		searchTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,  "Search");
		addNewButton = new JButton("Add New");
		updateButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		container1.setLayout(new MigLayout("", "[]push[][][]", ""));
		container1.add(searchTextField, "w 200!");
		container1.add(addNewButton);
		container1.add(updateButton);
		container1.add(deleteButton);

		addNewButton.setIcon(new FlatSVGIcon("gui/icon/svg/add.svg", 0.35f));
		updateButton.setIcon(new FlatSVGIcon("gui/icon/svg/edit.svg", 0.35f));
		deleteButton.setIcon(new FlatSVGIcon("gui/icon/svg/delete.svg", 0.35f));
		searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON,
				new FlatSVGIcon("gui/icon/svg/search.svg", 0.35f));

		staffTableModel = new StaffTableModel();
		employeeTable = new JTable(staffTableModel);
		container0.setLayout(new MigLayout("wrap, fill, insets 15", "[fill]", "[grow 0][fill]"));
		container0.add(container1);
		container0.add(new JScrollPane(employeeTable));

		if (employeeTable.getColumnModel().getColumnCount() > 0) {
			employeeTable.getColumnModel().getColumn(1).setPreferredWidth(150);
			employeeTable.getColumnModel().getColumn(4).setPreferredWidth(300);
		}

		// Change scroll style
		JScrollPane scroll = (JScrollPane) employeeTable.getParent().getParent();
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");

		employeeTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
		employeeTable.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");

		// To Create table alignment
		employeeTable.getTableHeader()
				.setDefaultRenderer(getAlignmentCellRender(employeeTable.getTableHeader().getDefaultRenderer(), true));
		employeeTable.setDefaultRenderer(Object.class,
				getAlignmentCellRender(employeeTable.getDefaultRenderer(Object.class), false));

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

		// event handler
		addNewButton.addActionListener(this);
		updateButton.addActionListener(this);
		deleteButton.addActionListener(this);

		add(container0);

		this.setVisible(true);

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
					label.setHorizontalAlignment(SwingConstants.LEADING);
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
		if (e.getSource().equals(addNewButton)) {
			Thread thread = new Thread(() -> {
				staffAddingDialog = new StaffAddingDialog();
				staffAddingDialog.setFormStaffManagement(this);
				staffAddingDialog.setModal(true);
				staffAddingDialog.setVisible(true);
			});
			thread.start();
		}
		if (e.getSource().equals(updateButton)) {
			Thread thread = new Thread(() -> {
				int selectedRow = employeeTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(this, "Please select a row to update.");
				} else {
					String employeeID = (String) employeeTable.getValueAt(selectedRow, 0);
					Employee employee = employeeDAO.getEmployeeByID(employeeID);
					staffUpdateDialog = new StaffUpdateDialog(employee);
					staffUpdateDialog.setFormStaffManagement(this);
					staffUpdateDialog.setModal(true);
					staffUpdateDialog.setVisible(true);
				}
			});
			thread.start();
		}
		if (e.getSource().equals(deleteButton)) {
			int selectedRow = employeeTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "Please select a row to delete.");
			} else {				
				
				// if employee that is being requested to be deleted is the current employee, show an error message that says "You can not delete yourself"
				String employeeID = (String) employeeTable.getValueAt(selectedRow, 0);
				if (currentEmployee.getEmployeeID().equals(employeeID)) {
					JOptionPane.showMessageDialog(this, "You can not delete yourself!!!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?",
						"Warning", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					boolean isSuccessful = employeeDAO.removeEmployeeByID(employeeID);
					if (isSuccessful) {
						handleSearch();
					} else {
						JOptionPane.showMessageDialog(this, "Cannot delete movie", "Failed", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					return;
				}
			}
		}
	}

	public void handleSearch() {
		String nameToFind = searchTextField.getText().trim();
		List<Employee> employeeList = employeeDAO.findEmployeByName(nameToFind);
		staffTableModel.setEmployeeList(employeeList);
		staffTableModel.fireTableDataChanged();
	}

}
