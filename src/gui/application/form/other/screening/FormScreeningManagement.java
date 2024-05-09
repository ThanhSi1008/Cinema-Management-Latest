package gui.application.form.other.screening;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;

import dao.MovieScheduleDAO;
import dao.RoomDAO;
import entity.Employee;
import entity.MovieSchedule;
import entity.Room;
import gui.application.Application;
import net.miginfocom.swing.MigLayout;

public class FormScreeningManagement extends JPanel implements ActionListener {

	// in movie adding dialog, show the image when the user choose the image
	private static final long serialVersionUID = 1L;
	private JButton addNewButton;
	private JButton updateButton;
	private JButton deleteButton;
	private JComboBox<String> filterComboBox;
	private JPanel container0;
	private JPanel container1;

	private MovieScheduleDAO movieScheduleDAO;
	private RoomDAO roomDAO;
	private ScreeningTableModel screeningTableModel;
	private JTable screeningTable;
	private JTextField searchByDateTextField;
	private DateChooser searchByDateDateChooser;
	private JButton searchByDateDateChooserButton;
	private ScreeningAddingDialog screeningAddingDialog;
	private ScreeningUpdateDialog screeningUpdateDialog;

	public FormScreeningManagement(Employee currentEmployee) {

		movieScheduleDAO = new MovieScheduleDAO();
		roomDAO = new RoomDAO();

		setLayout(new BorderLayout());
		container0 = new JPanel();
		container1 = new JPanel();
		searchByDateTextField = new JTextField();
		searchByDateDateChooser = new DateChooser();
		searchByDateDateChooserButton = new JButton();

		addNewButton = new JButton("Add New");
		updateButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		filterComboBox = new JComboBox<String>();
		filterComboBox.addItem("All");
		List<Room> roomList = roomDAO.getAllRoom();
		roomList.forEach(room -> {
			filterComboBox.addItem(room.getRoomName());
		});
		container1.setLayout(new MigLayout("", "[][]push[][][][]", ""));
		container1.add(searchByDateTextField, "w 200!");
		container1.add(searchByDateDateChooserButton);
		container1.add(filterComboBox);
		container1.add(addNewButton);
		container1.add(updateButton);
		container1.add(deleteButton);

		addNewButton.setIcon(new FlatSVGIcon("gui/icon/svg/add.svg", 0.35f));
		updateButton.setIcon(new FlatSVGIcon("gui/icon/svg/edit.svg", 0.35f));
		deleteButton.setIcon(new FlatSVGIcon("gui/icon/svg/delete.svg", 0.35f));
		searchByDateTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON,
				new FlatSVGIcon("gui/icon/svg/search.svg", 0.35f));

		// table
		screeningTableModel = new ScreeningTableModel();
		screeningTable = new JTable(screeningTableModel);
		screeningTable.addMouseListener(new MouseAdapter() {
			private SeatingOptionDialog seatingOptioneDialog;

			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				// int row = table.rowAtPoint(point);
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					
					// Setting up the glass pane
					JPanel glassPane = new BlurGlassPane();
					Application.getInstance().setGlassPane(glassPane);
					// Make the glass pane visible
					glassPane.setVisible(true);		
					
					int selectedRow = table.getSelectedRow();
					String movieScheduleID = (String) screeningTableModel.getValueAt(selectedRow, 0);
					MovieSchedule movieSchedule = movieScheduleDAO.getMovieScheduleByID(movieScheduleID);
					seatingOptioneDialog = new SeatingOptionDialog(movieSchedule);
					seatingOptioneDialog.setCurrentEmployee(currentEmployee);
					seatingOptioneDialog.setModal(true);
	                seatingOptioneDialog.setVisible(true);
				}
			}
		});
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

		screeningTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
		screeningTable.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");

		// To Create table alignment
		screeningTable.getTableHeader()
				.setDefaultRenderer(getAlignmentCellRender(screeningTable.getTableHeader().getDefaultRenderer(), true));
		screeningTable.setDefaultRenderer(Object.class,
				getAlignmentCellRender(screeningTable.getDefaultRenderer(Object.class), false));

		// event handlers
		addNewButton.addActionListener(this);
		updateButton.addActionListener(this);
		deleteButton.addActionListener(this);
		filterComboBox.addActionListener(this);

		searchByDateTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				handleSearchAndFilter();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				handleSearchAndFilter();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				handleSearchAndFilter();
			}
		});

		// date chooser
		ImageIcon calendarIcon = new ImageIcon("images/calendar.png");
		Image image = calendarIcon.getImage();
		Image newimg = image.getScaledInstance(16, 16, Image.SCALE_SMOOTH); // scale it the smooth way
		calendarIcon = new ImageIcon(newimg);

		searchByDateDateChooserButton.setIcon(calendarIcon);
		searchByDateDateChooserButton.addActionListener(e -> {
			searchByDateDateChooser.showPopup();
		});
		searchByDateDateChooser.setTextRefernce(searchByDateTextField);
		searchByDateDateChooser.addEventDateChooser(new EventDateChooser() {
			@Override
			public void dateSelected(SelectedAction action, SelectedDate date) {
				if (action.getAction() == SelectedAction.DAY_SELECTED) {
					searchByDateDateChooser.hidePopup();
				}
			}
		});

		add(container0);

//		pack();
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		this.setLocationRelativeTo(null);

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
		if (e.getSource().equals(filterComboBox)) {
			handleSearchAndFilter();
		}
		if (e.getSource().equals(addNewButton)) {
			Thread thread = new Thread(() -> {
				screeningAddingDialog = new ScreeningAddingDialog();
				screeningAddingDialog.setFormScreeningManagement(this);
				screeningAddingDialog.setModal(true);
				screeningAddingDialog.setVisible(true);
			});
			thread.start();
		}
		if (e.getSource().equals(updateButton)) {
			Thread thread = new Thread(() -> {
				int selectedRow = screeningTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(this, "Please select a row to update.");
				} else {
					String movieScheduleID = (String) screeningTable.getValueAt(selectedRow, 0);
					MovieSchedule movieSchedule = movieScheduleDAO.getMovieScheduleByID(movieScheduleID);
					screeningUpdateDialog = new ScreeningUpdateDialog(movieSchedule);
					screeningUpdateDialog.setFormScreeningManagement(this);
					screeningUpdateDialog.setModal(true);
					screeningUpdateDialog.setVisible(true);
				}
			});
			thread.start();
		}
		if (e.getSource().equals(deleteButton)) {
			int selectedRow = screeningTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "Please select a row to delete.");
			} else {
				int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this movie schedule?",
						"Warning", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					String scheduleID = (String) screeningTable.getValueAt(selectedRow, 0);
					boolean isSuccessful = movieScheduleDAO.deleteMovieScheduleByID(scheduleID);
					if (isSuccessful) {
						handleSearchAndFilter();
					} else {
						JOptionPane.showMessageDialog(this, "Cannot delete movie", "Failed", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}

	public void handleSearchAndFilter() {
		String roomNameToFind = (String) filterComboBox.getSelectedItem();
		String searchedDate = searchByDateTextField.getText().trim();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate searchedDateLocalDate;
		try {
			searchedDateLocalDate = LocalDate.parse(searchedDate, formatter);
		} catch (DateTimeParseException e) {
			searchedDateLocalDate = LocalDate.now();
		}

		List<MovieSchedule> movieScheduleList = null;
		if (roomNameToFind.equals("All")) {
			movieScheduleList = movieScheduleDAO.getAllMovieScheduleByDate(searchedDateLocalDate);
		} else {
			movieScheduleList = movieScheduleDAO.findMovieScheduleByRoomNameAndDate(roomNameToFind,
					searchedDateLocalDate);
		}
		screeningTableModel.setMovieScheduleList(movieScheduleList);
		screeningTableModel.fireTableDataChanged();
	}

}

class BlurGlassPane extends JPanel {
    private BufferedImage blurredImage;

    public BlurGlassPane() {
        setOpaque(false); // Making the glass pane transparent
        // Create a blank translucent image
        blurredImage = new BufferedImage(Application.getInstance().getRootPane().getWidth(), Application.getInstance().getRootPane().getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = blurredImage.createGraphics();
        g2d.setColor(new Color(0, 0, 0, 128)); // Set color with alpha for translucency
        g2d.fillRect(0, 0, Application.getInstance().getRootPane().getWidth(), Application.getInstance().getRootPane().getHeight()); // Fill the image with the translucent color
        g2d.dispose();

        // Apply blur effect
        blurredImage = blurImage(blurredImage);
    }

    // Method to blur an image
    private BufferedImage blurImage(BufferedImage image) {
        // You can implement your own image blurring algorithm or use libraries like JavaFX or Apache Commons Imaging
        // Here, I'll use a simple averaging algorithm for demonstration purposes
        int blurRadius = 5;
        float weight = 1.0f / (blurRadius * blurRadius);
        float[] blurMatrix = new float[blurRadius * blurRadius];
        for (int i = 0; i < blurMatrix.length; i++) {
            blurMatrix[i] = weight;
        }
        Kernel kernel = new Kernel(blurRadius, blurRadius, blurMatrix);
        BufferedImageOp op = new ConvolveOp(kernel);
        return op.filter(image, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the blurred image onto the glass pane
        g.drawImage(blurredImage, 0, 0, Application.getInstance().getRootPane().getWidth(), Application.getInstance().getRootPane().getHeight(), null);
    }
}