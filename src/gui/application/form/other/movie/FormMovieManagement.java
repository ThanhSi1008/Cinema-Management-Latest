package gui.application.form.other.movie;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.MovieDAO;
import dao.MovieScheduleDAO;
import entity.Movie;
import gui.other.MovieCSVReader;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;
import raven.toast.Notifications.Type;

public class FormMovieManagement extends JPanel implements ActionListener {

	// in movie adding dialog, show the image when the user choose the image
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
	private JButton importMovieByCSV;
	
	private MovieScheduleDAO movieScheduleDAO;

	public FormMovieManagement() {

		movieDAO = new MovieDAO();
		movieScheduleDAO = new MovieScheduleDAO();

		setLayout(new BorderLayout());
		container0 = new JPanel();
		container1 = new JPanel();
		searchTextField = new JTextField();
		searchTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search");
		addNewButton = new JButton("Add New");
		updateButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		importMovieByCSV = new JButton("Import Movie");
		filterComboBox = new JComboBox<String>();
		filterComboBox.addItem("All");
		filterComboBox.addItem("Released");
		filterComboBox.addItem("Unreleased");
		container1.setLayout(new MigLayout("", "[]push[][][][][]", ""));
		container1.add(searchTextField, "w 200!");
		container1.add(filterComboBox);
		container1.add(addNewButton);
		container1.add(updateButton);
		container1.add(deleteButton);
		container1.add(importMovieByCSV);

		addNewButton.setIcon(new FlatSVGIcon("gui/icon/svg/add.svg", 0.35f));
		updateButton.setIcon(new FlatSVGIcon("gui/icon/svg/edit.svg", 0.35f));
		deleteButton.setIcon(new FlatSVGIcon("gui/icon/svg/delete.svg", 0.35f));
		importMovieByCSV.setIcon(new FlatSVGIcon("gui/icon/svg/add.svg", 0.35f));

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
		filterComboBox.addActionListener(this);
		importMovieByCSV.addActionListener(this);

		// when user type something in the search text field, take out the value that
		// users type in
		searchTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				searchAndFilter();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				searchAndFilter();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				searchAndFilter();
			}
		});
		// select all the movie with that has that text in it and show it into the table

		movieTable.addMouseListener(new MouseAdapter() {
			private MovieDetailDialog movieDetailDialog;

			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				// int row = table.rowAtPoint(point);
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					int selectedRow = table.getSelectedRow();
					String movieID = (String) movieTableModel.getValueAt(selectedRow, 0);
					Movie movie = movieDAO.getMovieByID(movieID);
					movieDetailDialog = new MovieDetailDialog(movie);
					movieDetailDialog.setModal(true);
					movieDetailDialog.setVisible(true);
				}
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
				movieAddingDialog.setFormMovieManagement(this);
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
				
				if (movieScheduleDAO.checkMovieInScreening(movieID)) {
					JOptionPane.showMessageDialog(this, "This movie is being screened", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this movie?",
						"Warning", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					System.out.println(movieID);
					int rowsAffected = movieDAO.deleteMovieByID(movieID);
					if (rowsAffected > 0) {
						searchAndFilter();
					} else {
						JOptionPane.showMessageDialog(this, "Cannot delete movie", "Failed", JOptionPane.ERROR_MESSAGE);
					}
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
					movieUpdateDialog.setFormMovieManagement(this);
					movieUpdateDialog.setModal(true);
					movieUpdateDialog.setVisible(true);
				}
			});
			thread.start();
		}
		if (e.getSource().equals(filterComboBox)) {
			searchAndFilter();
		}
		if (e.getSource().equals(importMovieByCSV)) {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Import Movie File");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setAcceptAllFileFilterUsed(false);

			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
			fileChooser.addChoosableFileFilter(filter);

			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				System.out.println(selectedFile.getPath());
				String csvFile = selectedFile.getPath();
				List<Movie> movies = MovieCSVReader.readMoviesFromCSV(csvFile);
				if (movies != null) {
					for (Movie movie : movies) {
						if (!movieDAO.addNewMovie(movie)) {
							return;
						}
					}
				}
				Notifications.getInstance().show(Type.INFO, Notifications.Location.BOTTOM_LEFT,
						(movies.size() + " movie(s) imported successfully!"));
				searchAndFilter();
			}

		}
	}

	public void searchAndFilter() {
		String statusToFind = (String) filterComboBox.getSelectedItem();
		String nameToFind = searchTextField.getText();
		if (statusToFind.equals("All")) {
			List<Movie> movieList = movieDAO.findMovieByName(nameToFind);
			movieTableModel.setMovieList(movieList);
			movieTableModel.fireTableDataChanged();
		} else {
			List<Movie> movieList = movieDAO.findMovieByNameAndStatus(nameToFind, statusToFind);
			movieTableModel.setMovieList(movieList);
			movieTableModel.fireTableDataChanged();
		}
	}

}
