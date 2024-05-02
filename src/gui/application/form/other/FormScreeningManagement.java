package gui.application.form.other;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import dao.MovieDAO;
import dao.MovieScheduleDAO;
import dao.RoomDAO;
import entity.Room;
import net.miginfocom.swing.MigLayout;

public class FormScreeningManagement extends JFrame implements ActionListener {

	// in movie adding dialog, show the image when the user choose the image
	private static final long serialVersionUID = 1L;
	private JTextField searchTextField;
	private JButton addNewButton;
	private JButton updateButton;
	private JButton deleteButton;
	private JComboBox<String> filterComboBox;
	private JPanel container0;
	private JPanel container1;

	private MovieDAO movieDAO;
	private MovieUpdateDialog movieUpdateDialog;
	private MovieScheduleDAO movieScheduleDAO;
	private RoomDAO roomDAO;
	private ScreeningTableModel screeningTableModel;
	private JTable screeningTable;

	public FormScreeningManagement() {

		movieScheduleDAO = new MovieScheduleDAO();
		roomDAO = new RoomDAO();

		setLayout(new BorderLayout());
		container0 = new JPanel();
		container1 = new JPanel();
		searchTextField = new JTextField();
		addNewButton = new JButton("Add New");
		updateButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		filterComboBox = new JComboBox<String>();
		filterComboBox.addItem("All");
		List<Room> roomList = roomDAO.getAllRoom();
		roomList.forEach(room -> {
			filterComboBox.addItem(room.getRoomName());
		});
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

		screeningTableModel = new ScreeningTableModel();
		screeningTable = new JTable(screeningTableModel);
		container0.setLayout(new MigLayout("wrap, fill, insets 15", "[fill]", "[grow 0][fill]"));
		container0.add(container1);

		if (screeningTable.getColumnModel().getColumnCount() > 0) {
			screeningTable.getColumnModel().getColumn(1).setPreferredWidth(300);
		}

		container0.add(new JScrollPane(screeningTable));
		// Change scroll style
		JScrollPane scroll = (JScrollPane) screeningTable.getParent().getParent();
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");
//		scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));

		screeningTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
		screeningTable.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");

		// To Create table alignment
		screeningTable.getTableHeader()
				.setDefaultRenderer(getAlignmentCellRender(screeningTable.getTableHeader().getDefaultRenderer(), true));
		screeningTable.setDefaultRenderer(Object.class,
				getAlignmentCellRender(screeningTable.getDefaultRenderer(Object.class), false));

		addNewButton.addActionListener(this);
		updateButton.addActionListener(this);
		deleteButton.addActionListener(this);
		filterComboBox.addActionListener(this);

		// when user type something in the search text field, take out the value that
		// users type in
		searchTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
		// select all the movie with that has that text in it and show it into the table

		add(container0);
		
		pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setLocationRelativeTo(null);

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
	}

	
	public static void main(String[] args) {
		FlatRobotoFont.install();
		FlatLaf.registerCustomDefaultsSource("gui.theme");
		UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
		FlatMacLightLaf.setup();
		SwingUtilities.invokeLater(() -> {
			new FormScreeningManagement().setVisible(true);
		});


}


//pack();
//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
//this.setLocationRelativeTo(null);

//public static void main(String[] args) {
//	FlatRobotoFont.install();
//	FlatLaf.registerCustomDefaultsSource("gui.theme");
//	UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
//	FlatMacLightLaf.setup();
//	SwingUtilities.invokeLater(() -> {
//		new FormScreeningManagement().setVisible(true);
//	});
}