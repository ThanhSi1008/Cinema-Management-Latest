package gui.application.form.other;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.MovieDAO;
import entity.Movie;
import net.miginfocom.swing.MigLayout;

public class FormMovieManagement extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField searchTextField;
	private JButton addNewButton;
	private JButton updateButton;
	private JButton deleteButton;
	private JComboBox<String> filterComboBox;
	private JTable movieTable;
	private MovieTableModel movieTableModel;
	private JPanel container0;
	private JPanel container1;
	private MovieAddingDialog movieAddingDialog;

	private MovieDAO movieDAO;
	private MovieUpdateDialog movieUpdateDialog;

	public FormMovieManagement() {

		movieDAO = new MovieDAO();

		setLayout(new BorderLayout());
		container0 = new JPanel();
		container1 = new JPanel();
		searchTextField = new JTextField();
		addNewButton = new JButton("Add New");
		updateButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		filterComboBox = new JComboBox<String>();
		filterComboBox.addItem("Airing");
		filterComboBox.addItem("Stopped");
		container1.setLayout(new MigLayout("", "[]push[][][][]", ""));
		container1.add(searchTextField, "w 200!");
		container1.add(filterComboBox);
		container1.add(addNewButton);
		container1.add(updateButton);
		container1.add(deleteButton);

		addNewButton.setIcon(new FlatSVGIcon("gui/icon/svg/add.svg", 0.35f));
		updateButton.setIcon(new FlatSVGIcon("gui/icon/svg/edit.svg", 0.35f));
		deleteButton.setIcon(new FlatSVGIcon("gui/icon/svg/delete.svg", 0.35f));
		searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON,
				new FlatSVGIcon("gui/icon/svg/search.svg", 0.35f));

		movieTableModel = new MovieTableModel();
		movieTable = new JTable(movieTableModel);
		container0.setLayout(new MigLayout("wrap, fill, insets 15", "[fill]", "[grow 0][fill]"));
		container0.add(container1);
		container0.add(new JScrollPane(movieTable));

		if (movieTable.getColumnModel().getColumnCount() > 0) {
			movieTable.getColumnModel().getColumn(1).setPreferredWidth(300);
		}

		// Change scroll style
		JScrollPane scroll = (JScrollPane) movieTable.getParent().getParent();
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");

		movieTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
		movieTable.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");

		// To Create table alignment
		movieTable.getTableHeader()
				.setDefaultRenderer(getAlignmentCellRender(movieTable.getTableHeader().getDefaultRenderer(), true));
		movieTable.setDefaultRenderer(Object.class,
				getAlignmentCellRender(movieTable.getDefaultRenderer(Object.class), false));

		addNewButton.addActionListener(this);
		updateButton.addActionListener(this);
		deleteButton.addActionListener(this);

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
		if (e.getSource().equals(addNewButton)) {
			Thread thread = new Thread(() -> {
				movieAddingDialog = new MovieAddingDialog();
				movieAddingDialog.setMovieTableModel(movieTableModel);
				movieAddingDialog.setModal(true);
				movieAddingDialog.setVisible(true);
			});
			thread.start();
		}
		if (e.getSource().equals(deleteButton)) {
			int selectedRow = movieTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "Please select a row to delete.");
			} else {
				String movieID = (String) movieTable.getValueAt(selectedRow, 0);
				System.out.println(movieID);
				int rowsAffected = movieDAO.deleteMovieByID(movieID);
				if (rowsAffected > 0) {
					movieTableModel.refresh();
				} else {
					JOptionPane.showMessageDialog(this, "Cannot delete movie", "Failed", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		if (e.getSource().equals(updateButton)) {
			Thread thread = new Thread(() -> {
				int selectedRow = movieTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(this, "Please select a row to update.");
				} else {
					String movieID = (String) movieTable.getValueAt(selectedRow, 0);
					Movie movie = movieDAO.getMovieByID(movieID);
					movieUpdateDialog = new MovieUpdateDialog(movie);
					movieUpdateDialog.setMovieTableModel(movieTableModel);
					movieUpdateDialog.setModal(true);
					movieUpdateDialog.setVisible(true);
				}
			});
			thread.start();
		}
	}

}
