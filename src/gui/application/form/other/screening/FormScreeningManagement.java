package gui.application.form.other.screening;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;

import dao.MovieScheduleDAO;
import entity.Employee;
import entity.Movie;
import entity.MovieSchedule;
import gui.application.Application;
import gui.application.form.other.movie.MovieDetailDialog;
import net.miginfocom.swing.MigLayout;

public class FormScreeningManagement extends JPanel implements ActionListener {

	// in movie adding dialog, show the image when the user choose the image

	private static final long serialVersionUID = 1L;
	private JButton addNewButton;
	private JPanel container0;
	private JPanel container1;

	private JTextField searchByDateTextField;
	private DateChooser searchByDateDateChooser;
	private JButton searchByDateDateChooserButton;
	private MovieScheduleDAO movieScheduleDAO;
	private JPanel container2;
	private JPanel movieScheduleCardContainer;
	private ScreeningAddingDialog screeningAddingDialog;
	private SeatingOptionDialog seatingOptioneDialog;
	private Employee currentEmployee;
	private MovieDetailDialog movieDetailDialog;
	private JComboBox<String> searchByNameCombobox;

	private LocalDate searchedDateLocalDate;
	private List<Movie> movieList;

	public FormScreeningManagement(Employee currentEmployee) {

		movieScheduleDAO = new MovieScheduleDAO();
		this.currentEmployee = currentEmployee;

		setLayout(new BorderLayout());
		container0 = new JPanel();
		container1 = new JPanel();
		searchByDateTextField = new JTextField();
		searchByDateDateChooser = new DateChooser();
		searchByDateDateChooserButton = new JButton();

		searchByNameCombobox = new JComboBox<String>();

		addNewButton = new JButton("Add New");
		container1.setLayout(new MigLayout("", "[][]push[][]", ""));
		container1.add(searchByDateTextField, "w 200!");
		container1.add(searchByDateDateChooserButton);
		container1.add(searchByNameCombobox);
		container1.add(addNewButton);

		addNewButton.setIcon(new FlatSVGIcon("gui/icon/svg/add.svg", 0.35f));
		searchByDateTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON,
				new FlatSVGIcon("gui/icon/svg/search.svg", 0.35f));

		container0.setLayout(new MigLayout("wrap, fill, insets 15", "[fill]", "[grow 0][fill]"));
		container0.add(container1);
		container2 = new JPanel(new MigLayout("wrap, fill", "[fill]", "[fill]"));
		movieScheduleCardContainer = new JPanel(
				new MigLayout("wrap, fillx, aligny top", "[sizegroup main, grow][sizegroup main, grow]", ""));
//		movieScheduleCardContainer = new JPanel(new MigLayout("wrap, fillx, aligny top", "[grow, shrink, 0!][grow, shrink, 0!]", ""));

		container0.add(new JScrollPane(container2));
		container2.add(movieScheduleCardContainer);

		// event handlers
		addNewButton.addActionListener(this);

		searchByDateTextField.getDocument().addDocumentListener(new DocumentListener() {

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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addNewButton)) {
			Thread thread = new Thread(() -> {
				screeningAddingDialog = new ScreeningAddingDialog();
				screeningAddingDialog.setFormScreeningManagement(this);
				screeningAddingDialog.setModal(true);
				screeningAddingDialog.setVisible(true);
			});
			thread.start();
		}
	}

	public void loadMovieSchedule(List<Movie> movieList) {
		movieScheduleCardContainer.removeAll();
		movieList.forEach(movie -> {

			JPanel filler = new JPanel(new MigLayout("wrap, fill", "[fill]"));
			JPanel movieScheduleCard = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][grow 0][fill]"));
			JPanel movieNameContainer = new JPanel(new MigLayout("wrap, fill", "[]push[]", "[fill]"));
			JPanel generalInfoContainer = new JPanel(new MigLayout("", "[left]", ""));
			JPanel movieScheduleCardBottomContainer = new JPanel(
					new MigLayout("wrap, fill", "[grow 0][fill]", "[fill]"));
			JPanel movieScheduleCardBottomLeftContainer = new JPanel(
					new MigLayout("wrap, fill", "[fill]", "[fill][grow 0]"));
			JPanel movieScheduleCardBottomRightContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[fill]"));

			movieScheduleCard.add(movieNameContainer);
			movieScheduleCard.add(generalInfoContainer);
			movieScheduleCard.add(movieScheduleCardBottomContainer);
			movieScheduleCardBottomContainer.add(movieScheduleCardBottomLeftContainer);
			movieScheduleCardBottomContainer.add(movieScheduleCardBottomRightContainer);

			JLabel movieName = new JLabel(movie.getMovieName());
			JButton viewDetailButton = new JButton("View details");
			movieNameContainer.add(movieName);
			movieNameContainer.add(viewDetailButton);

			viewDetailButton.addActionListener(e1 -> {
				movieDetailDialog = new MovieDetailDialog(movie);
				movieDetailDialog.setModal(true);
				movieDetailDialog.setVisible(true);
			});

			JLabel generalInfo = new JLabel(
					movie.getGenre() + "  |  " + movie.getCountry() + "  |  " + movie.getDuration() + " minutes");
			generalInfoContainer.add(generalInfo);

			ImageIcon icon = new ImageIcon(movie.getImageSource());
			if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
				icon = new ImageIcon("images/movie-poster-not-found.jpg");
			}
			Image img = icon.getImage();
			Image resizedImg = img.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
			ImageIcon resizedIcon = new ImageIcon(resizedImg);
			JLabel movieImage = new JLabel(resizedIcon);
			JButton trailerButton = new JButton("Trailer");

			trailerButton.addActionListener(e1 -> {
			});

			movieScheduleCardBottomLeftContainer.add(movieImage);
			movieScheduleCardBottomLeftContainer.add(trailerButton);

			DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");

			List<MovieSchedule> movieScheduleList = movieScheduleDAO
					.getMovieScheduleByMovieIDAndByDate(movie.getMovieID(), searchedDateLocalDate);

			JPanel screeningContainer = new JPanel(new MigLayout("wrap, fillx", "[][]", ""));
			movieScheduleList.forEach(movieSchedule -> {
				JLabel screeningTimeLabel = new JLabel(movieSchedule.getScreeningTime().format(hourFormatter));
				JLabel endTimeLabel = new JLabel("~ " + movieSchedule.getEndTime().format(hourFormatter));
				JButton screeningButton = new JButton();
				screeningButton.setLayout(new MigLayout("wrap, fill, insets 0", "[][]", "[fill]"));
				screeningButton.add(screeningTimeLabel);
				screeningTimeLabel.putClientProperty(FlatClientProperties.STYLE, "font:$h5.font");
				endTimeLabel.putClientProperty(FlatClientProperties.STYLE, "foreground:$muted; font:-4");
				screeningButton.add(endTimeLabel);	
				// JButton screeningButton = new JButton(movieSchedule.getScreeningTime().format(hourFormatter) + " ~ " + movieSchedule.getEndTime().format(hourFormatter));
				screeningContainer.add(screeningButton);
				screeningButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (SwingUtilities.isRightMouseButton(e)) {
							showPopupMenu(screeningButton, e.getX(), e.getY(), movieSchedule);
						}
					}

					public void showPopupMenu(Component invoker, int x, int y, MovieSchedule movieSchedule) {
						JPopupMenu popupMenu = new JPopupMenu();

						JMenuItem updateMenuItem = new JMenuItem("Update");
						JMenuItem deleteMenuItem = new JMenuItem("Delete");

						updateMenuItem.setIcon(new FlatSVGIcon("gui/icon/svg/edit.svg", 0.35f));
						deleteMenuItem.setIcon(new FlatSVGIcon("gui/icon/svg/delete.svg", 0.35f));

						MovieSchedule temp = movieSchedule;

						updateMenuItem.addActionListener(new ActionListener() {
							private ScreeningUpdateDialog screeningUpdateDialog;

							public void actionPerformed(ActionEvent e) {
								MovieSchedule movieSchedule = movieScheduleDAO
										.getMovieScheduleByID(temp.getScheduleID());
								screeningUpdateDialog = new ScreeningUpdateDialog(movieSchedule);
								screeningUpdateDialog.setFormScreeningManagement(FormScreeningManagement.this);
								screeningUpdateDialog.setModal(true);
								screeningUpdateDialog.setVisible(true);
							}
						});

						deleteMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								int option = JOptionPane.showConfirmDialog(invoker,
										"Are you sure you want to delete this movie schedule?", "Warning",
										JOptionPane.YES_NO_OPTION);
								if (option == JOptionPane.YES_OPTION) {
									boolean isSuccessful = movieScheduleDAO
											.deleteMovieScheduleByID(temp.getScheduleID());
									if (isSuccessful) {
										handleSearch();
									} else {
										JOptionPane.showMessageDialog(invoker, "Cannot delete movie", "Failed",
												JOptionPane.ERROR_MESSAGE);
									}
								}
							}
						});

						popupMenu.add(updateMenuItem);
						popupMenu.add(deleteMenuItem);

						popupMenu.show(invoker, x, y);
					}
				});

				// styles
				screeningButton.putClientProperty(FlatClientProperties.STYLE,
						"hoverBackground:$primary;hoverForeground:$clr-white");
				screeningButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						screeningButton.setCursor(new Cursor(Cursor.HAND_CURSOR));						
						screeningTimeLabel.putClientProperty(FlatClientProperties.STYLE, "font:$h5.font;foreground:$clr-white");
						endTimeLabel.putClientProperty(FlatClientProperties.STYLE, "foreground:$muted;foreground:$clr-white; font:-4");
					}

					@Override
					public void mouseExited(MouseEvent e) {
						screeningButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						screeningTimeLabel.putClientProperty(FlatClientProperties.STYLE, "font:$h5.font");
						endTimeLabel.putClientProperty(FlatClientProperties.STYLE, "foreground:$muted; font:-4");
					}
				});
				// action listener
				screeningButton.addActionListener(e1 -> {
					// Setting up the glass pane
					JPanel glassPane = new BlurGlassPane();
					Application.getInstance().setGlassPane(glassPane);
					// Make the glass pane visible
					glassPane.setVisible(true);
					seatingOptioneDialog = new SeatingOptionDialog(movieSchedule);
					seatingOptioneDialog.setCurrentEmployee(currentEmployee);
					seatingOptioneDialog.setModal(true);
					seatingOptioneDialog.setVisible(true);
				});
			});

			// styles

			movieScheduleCardBottomRightContainer.add(screeningContainer);

			movieScheduleCard.putClientProperty(FlatClientProperties.STYLE,
					"background:$white;border:0,0,0,0,$white,0,10");
			movieNameContainer.putClientProperty(FlatClientProperties.STYLE,
					"background:$white;border:0,0,0,0,$white,0,10");
			generalInfoContainer.putClientProperty(FlatClientProperties.STYLE,
					"background:$white;border:0,0,0,0,$white,0,10");
			movieScheduleCardBottomContainer.putClientProperty(FlatClientProperties.STYLE,
					"background:$white;border:0,0,0,0,$white,0,10");
			movieScheduleCardBottomLeftContainer.putClientProperty(FlatClientProperties.STYLE,
					"background:$white;border:0,0,0,0,$white,0,10");
			movieScheduleCardBottomRightContainer.putClientProperty(FlatClientProperties.STYLE,
					"background:$white;border:0,0,0,0,$white,0,10");
			screeningContainer.putClientProperty(FlatClientProperties.STYLE,
					"background:$white;border:0,0,0,0,$white,0,10");
			movieImage.putClientProperty(FlatClientProperties.STYLE, "background:$white;border:0,0,0,0,$white,0");

			movieName.putClientProperty(FlatClientProperties.STYLE, "font:$h3.font");

			filler.add(movieScheduleCard);

			movieScheduleCardContainer.add(filler, "growx, shrinkx, aligny top, width 0");

			JScrollPane scroll = (JScrollPane) container2.getParent().getParent();
			scroll.setBorder(BorderFactory.createEmptyBorder());
			scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
					"" + "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");

			trailerButton.putClientProperty(FlatClientProperties.STYLE,
					"hoverBackground:$primary;hoverForeground:$clr-white");
			viewDetailButton.putClientProperty(FlatClientProperties.STYLE,
					"background:$primary;foreground:$clr-white");

			generalInfo.putClientProperty(FlatClientProperties.STYLE, "foreground:$muted");

			// event handler

			trailerButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					trailerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					trailerButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			});

			viewDetailButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					viewDetailButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					viewDetailButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			});

		});
		repaint();
		revalidate();
	}

	public void handleSearch() {
		for (ActionListener al : searchByNameCombobox.getActionListeners()) {
			searchByNameCombobox.removeActionListener(al);
		}
		String searchedDate = searchByDateTextField.getText().trim();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		try {
			searchByNameCombobox.removeAllItems();
			searchByNameCombobox.addItem("All");

			searchedDateLocalDate = LocalDate.parse(searchedDate, formatter);
			movieList = movieScheduleDAO.getAllMovieByDate(searchedDateLocalDate);
			loadMovieSchedule(movieList);
			movieList = movieList.stream().sorted(Comparator.comparing(Movie::getMovieName)).toList();
			movieList.forEach(movie -> {
				searchByNameCombobox.addItem(movie.getMovieName());
			});

			searchByNameCombobox.addActionListener(e -> {
				String movieNameToFind = (String) searchByNameCombobox.getSelectedItem();
				System.out.println(movieNameToFind);
				if (movieNameToFind.equals("All")) {
					movieList = movieScheduleDAO.getAllMovieByDate(searchedDateLocalDate);
					loadMovieSchedule(movieList);
				} else {
					movieList = movieScheduleDAO.getAllMovieByDateAndByMovieName(searchedDateLocalDate,
							movieNameToFind);
					loadMovieSchedule(movieList);
				}
			});

			this.revalidate();
			this.repaint();
		} catch (DateTimeParseException e) {
			searchedDateLocalDate = LocalDate.now();
		}
	}
}

class BlurGlassPane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage blurredImage;

	public BlurGlassPane() {
		setOpaque(false); // Making the glass pane transparent
		// Create a blank translucent image
		blurredImage = new BufferedImage(Application.getInstance().getRootPane().getWidth(),
				Application.getInstance().getRootPane().getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = blurredImage.createGraphics();
		g2d.setColor(new Color(0, 0, 0, 128)); // Set color with alpha for translucency
		g2d.fillRect(0, 0, Application.getInstance().getRootPane().getWidth(),
				Application.getInstance().getRootPane().getHeight()); // Fill the image with the translucent color
		g2d.dispose();

		// Apply blur effect
		blurredImage = blurImage(blurredImage);
	}

	// Method to blur an image
	private BufferedImage blurImage(BufferedImage image) {
		// You can implement your own image blurring algorithm or use libraries like
		// JavaFX or Apache Commons Imaging
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
		g.drawImage(blurredImage, 0, 0, Application.getInstance().getRootPane().getWidth(),
				Application.getInstance().getRootPane().getHeight(), null);
	}
}