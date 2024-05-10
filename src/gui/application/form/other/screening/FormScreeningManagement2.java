package gui.application.form.other.screening;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;

import dao.MovieScheduleDAO;
import dao.RoomDAO;
import entity.Employee;
import entity.Movie;
import entity.MovieSchedule;
import entity.Room;
import net.miginfocom.swing.MigLayout;

public class FormScreeningManagement2 extends JFrame implements ActionListener {

	// in movie adding dialog, show the image when the user choose the image
	private static final long serialVersionUID = 1L;
	private JButton addNewButton;
	private JPanel container0;
	private JPanel container1;

	private RoomDAO roomDAO;
	private JTextField searchByDateTextField;
	private DateChooser searchByDateDateChooser;
	private JButton searchByDateDateChooserButton;
	private MovieScheduleDAO movieScheduleDAO;
	private JPanel container2;
	private JPanel movieScheduleCardContainer;
	private ScreeningAddingDialog screeningAddingDialog;

	public FormScreeningManagement2(Employee currentEmployee) {

		movieScheduleDAO = new MovieScheduleDAO();
		roomDAO = new RoomDAO();

		setLayout(new BorderLayout());
		container0 = new JPanel();
		container1 = new JPanel();
		searchByDateTextField = new JTextField();
		searchByDateDateChooser = new DateChooser();
		searchByDateDateChooserButton = new JButton();

		addNewButton = new JButton("Add New");
		container1.setLayout(new MigLayout("", "[]push[]", ""));
		container1.add(searchByDateTextField, "w 200!");
		container1.add(searchByDateDateChooserButton);
		container1.add(addNewButton);
		
	
		addNewButton.setIcon(new FlatSVGIcon("gui/icon/svg/add.svg", 0.35f));
		searchByDateTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON,
				new FlatSVGIcon("gui/icon/svg/search.svg", 0.35f));

		container0.setLayout(new MigLayout("wrap, fill, insets 15", "[fill]", "[grow 0][fill]"));
		container0.add(container1);
		container2 = new JPanel(new MigLayout("wrap, fill", "[fill]", "[fill]"));
		movieScheduleCardContainer = new JPanel(new MigLayout("wrap, fillx, aligny top", "[fill][fill]", ""));
		container0.add(new JScrollPane(container2));
		container2.add(movieScheduleCardContainer);

		loadMovieSchedule(LocalDate.now());		

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

		pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setLocationRelativeTo(null);

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
	
	public void loadMovieSchedule(LocalDate dateToFind) {
		List<Movie> movieList = movieScheduleDAO.getAllMovieByDate(dateToFind);
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

			JLabel generalInfo = new JLabel(
					movie.getGenre() + "  |  " + movie.getCountry() + "  |  " + movie.getDuration() + " minutes");
			generalInfoContainer.add(generalInfo);

			ImageIcon icon = new ImageIcon(movie.getImageSource());
			if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
				icon = new ImageIcon("images/movie-poster-not-found.jpg");
			}
			Image img = icon.getImage();
			Image resizedImg = img.getScaledInstance(200, -1, Image.SCALE_SMOOTH);
			ImageIcon resizedIcon = new ImageIcon(resizedImg);
			JLabel movieImage = new JLabel(resizedIcon);
			JButton trailerButton = new JButton("Trailer");

			movieScheduleCardBottomLeftContainer.add(movieImage);
			movieScheduleCardBottomLeftContainer.add(trailerButton);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

			List<MovieSchedule> movieScheduleList = movieScheduleDAO.getMovieScheduleByMovieIDAndByDate(movie.getMovieID(), LocalDate.now());

			JPanel screeningContainer = new JPanel(new MigLayout("wrap, fillx", "[][]", ""));
			movieScheduleList.forEach(movieSchedule -> {
				JButton screeningButton = new JButton(movieSchedule.getScreeningTime().format(formatter) + " - "
						+ movieSchedule.getEndTime().format(formatter));
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
								MovieSchedule movieSchedule = movieScheduleDAO.getMovieScheduleByID(temp.getScheduleID());
								screeningUpdateDialog = new ScreeningUpdateDialog(movieSchedule);
								screeningUpdateDialog.setFormScreeningManagement(FormScreeningManagement2.this);
								screeningUpdateDialog.setModal(true);
								screeningUpdateDialog.setVisible(true);
				            }
				        });
				        
				        deleteMenuItem.addActionListener(new ActionListener() {
				            public void actionPerformed(ActionEvent e) {
				            	int option = JOptionPane.showConfirmDialog(invoker, "Are you sure you want to delete this movie schedule?",
										"Warning", JOptionPane.YES_NO_OPTION);
								if (option == JOptionPane.YES_OPTION) {
									boolean isSuccessful = movieScheduleDAO.deleteMovieScheduleByID(temp.getScheduleID());
									if (isSuccessful) {
//										handleSearchAndFilter();
									} else {
										JOptionPane.showMessageDialog(invoker, "Cannot delete movie", "Failed", JOptionPane.ERROR_MESSAGE);
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
				screeningButton.putClientProperty(FlatClientProperties.STYLE, "font:$xl.font; border:10,20,10,20; hoverBackground:$white;hoverForeground:$primary");
				screeningButton.addMouseListener(new MouseAdapter() {
		            @Override
		            public void mouseEntered(MouseEvent e) {
		            	screeningButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		            }

		            @Override
		            public void mouseExited(MouseEvent e) {
		            	screeningButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		            }
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

			movieScheduleCardContainer.add(filler, "growx, aligny top");
			
			JScrollPane scroll = (JScrollPane) container2.getParent().getParent();
			scroll.setBorder(BorderFactory.createEmptyBorder());
			scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
					"" + "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");
			
			trailerButton.putClientProperty(FlatClientProperties.STYLE, "hoverBackground:$primary;hoverForeground:$clr-white");
			viewDetailButton.putClientProperty(FlatClientProperties.STYLE, "hoverBackground:$primary;hoverForeground:$clr-white");
			
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
	}
	
	public void handleSearch() {
		String searchedDate = searchByDateTextField.getText().trim();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate searchedDateLocalDate;
		try {
			searchedDateLocalDate = LocalDate.parse(searchedDate, formatter);
			movieScheduleCardContainer.removeAll();
			loadMovieSchedule(searchedDateLocalDate);			
			this.revalidate();
			this.repaint();
		} catch (DateTimeParseException e) {
			searchedDateLocalDate = LocalDate.now();
		}
	}

	public static void main(String[] args) {
		FlatRobotoFont.install();
		FlatLaf.registerCustomDefaultsSource("gui.theme");
		UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
		int now = LocalTime.now().getHour();
		FlatMacLightLaf.setup();
		SwingUtilities.invokeLater(() -> {
			new FormScreeningManagement2(new Employee()).setVisible(true);
		});
	}

}