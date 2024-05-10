package gui.application.form.other.movie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

import com.formdev.flatlaf.FlatClientProperties;
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;

import dao.MovieDAO;
import entity.Movie;
import net.miginfocom.swing.MigLayout;
import raven.crazypanel.CrazyPanel;
import raven.toast.Notifications;
import raven.toast.Notifications.Location;

public class MovieUpdateDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
//	final JFileChooser fc = new JFileChooser();
	private CrazyPanel container;
	private JLabel movieNameLabel;
	private JTextField movieNameTextField;
	private JLabel directorLabel;
	private JTextField directorTextField;
	private JLabel durationLabel;
	private JTextField durationTextField;
	private JLabel importPriceLabel;
	private JTextField importPriceTextField;
	private JLabel countryLabel;
	private JTextField countryTextField;
	private JLabel languageLabel;
	private JTextField languageTextField;
	private JLabel imageSourceLabel;
	private JLabel releasedDateLabel;
	private JTextField releasedDateTextField;
	private JLabel statusLabel;
	private JLabel trailerLabel;
	private JTextField trailerTextField;
	private JLabel genreLabel;
	private JTextField genreTextField;
	private JLabel descriptionLabel;
	private File selectedFile;
	private JLabel startDateLabel;
	private JTextField startDateTextField;
	private JLabel title;
	private JButton updateButton;
	private JTextArea descriptionTextArea;
	private JButton imageSourceButton;
	private JLabel displaypPosterLabel;
	private JButton releasedDateDateChooserButton;
	private JButton startDateDateChooserButton;
	private DateChooser releasedDateDateChooser;
	private DateChooser startDateDateChooser;
	private JComboBox<String> statusComboBox;
	private JLabel errorMessageLabel;
	private MovieDAO movieDAO;
	private Movie movie;
	private FormMovieManagement formMovieManagement;
	private FileNameExtensionFilter filter;
	private JPanel imageChooserAndFileChooserContainer;
	private JPanel releasedDateContainer;
	private JPanel startDateContainer;

	public MovieUpdateDialog(Movie movie) {
		this.movie = movie;
		movieDAO = new MovieDAO();
		setLayout(new BorderLayout());
		initComponents();
	}

	private void initComponents() {
		this.setTitle("Updating Movie");
		container = new CrazyPanel();
		title = new JLabel("UPDATE MOVIE");
		movieNameLabel = new JLabel("Name: ");
		movieNameTextField = new JTextField(40);
		directorLabel = new JLabel("Director: ");
		directorTextField = new JTextField(15);
		durationLabel = new JLabel("Duration: ");
		durationTextField = new JTextField(15);
		importPriceLabel = new JLabel("Price: ");
		importPriceTextField = new JTextField(15);
		countryLabel = new JLabel("Country: ");
		countryTextField = new JTextField(20);
		languageLabel = new JLabel("Language: ");
		languageTextField = new JTextField(20);
		imageSourceLabel = new JLabel("Image: ");
		imageSourceButton = new JButton("Choose Image");
		displaypPosterLabel = new JLabel();
		releasedDateLabel = new JLabel("Released Date: ");
		releasedDateTextField = new JTextField();
		statusLabel = new JLabel("Status: ");
		startDateLabel = new JLabel("Start Date: ");
		startDateTextField = new JTextField();
		trailerLabel = new JLabel("Trailer: ");
		trailerTextField = new JTextField(20);
		genreLabel = new JLabel("Genre: ");
		genreTextField = new JTextField(20);
		statusLabel = new JLabel("Status: ");
		statusComboBox = new JComboBox<String>();
		statusComboBox.addItem("Released");
		statusComboBox.addItem("Unreleased");
		descriptionLabel = new JLabel("Description: ");
		descriptionTextArea = new JTextArea();
		updateButton = new JButton("Update");
		releasedDateDateChooser = new DateChooser();
		startDateDateChooser = new DateChooser();
		releasedDateDateChooserButton = new JButton();
		startDateDateChooserButton = new JButton();
		errorMessageLabel = new JLabel();

		// fill input with existing values
		movieNameTextField.setText(movie.getMovieName());
		directorTextField.setText(movie.getDirector());
		durationTextField.setText(movie.getDuration() + "");
		importPriceTextField.setText(movie.getImportPrice() + "");
		countryTextField.setText(movie.getCountry());
		languageTextField.setText(movie.getLanguage());

		boolean isValid = false;
		String path = movie.getImageSource();
		System.out.println("image path is: " + path);
		if (path != null && !path.trim().isEmpty()) {
			File file = new File(path);
			isValid = file.exists();
		}

		if (!isValid) {
			displaypPosterLabel.setIcon(null);
		} else {
			Image imageIcon = new ImageIcon(path).getImage();
			displaypPosterLabel.setIcon(new ImageIcon(imageIcon.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
		}

		releasedDateDateChooser.setSelectedDate(new SelectedDate(movie.getReleasedDate().getDayOfMonth(),
				movie.getReleasedDate().getMonthValue(), movie.getReleasedDate().getYear()));
		startDateDateChooser.setSelectedDate(new SelectedDate(movie.getStartDate().getDayOfMonth(),
				movie.getStartDate().getMonthValue(), movie.getStartDate().getYear()));
		trailerTextField.setText(movie.getTrailer());
		genreTextField.setText(movie.getGenre());
		statusComboBox.setSelectedItem(movie.getGenre());
		descriptionTextArea.setText(movie.getDescription());

		title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 24));
		descriptionTextArea.setRows(3);
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		errorMessageLabel.setForeground(Color.RED);
		errorMessageLabel.setFont(errorMessageLabel.getFont().deriveFont(Font.ITALIC));

		imageChooserAndFileChooserContainer = new JPanel(
				new MigLayout("wrap, fillx, insets 0", "[grow 0, al trail][grow 0][fill]", "[]0[]0[]"));
		imageChooserAndFileChooserContainer.add(imageSourceLabel);
		imageChooserAndFileChooserContainer.add(imageSourceButton, "gapleft 8");
		imageChooserAndFileChooserContainer.add(displaypPosterLabel, "span 1 3");
		imageChooserAndFileChooserContainer.add(releasedDateLabel);

		releasedDateContainer = new JPanel(new MigLayout("wrap", "[][]", "[fill]"));
		releasedDateContainer.add(releasedDateTextField);
		releasedDateContainer.add(releasedDateDateChooserButton);
		imageChooserAndFileChooserContainer.add(releasedDateContainer);

		imageChooserAndFileChooserContainer.add(startDateLabel);
		startDateContainer = new JPanel(new MigLayout("wrap", "[][]", "[fill]"));
		startDateContainer.add(startDateTextField);
		startDateContainer.add(startDateDateChooserButton);
		imageChooserAndFileChooserContainer.add(startDateContainer);

		// set layout
		container.setLayout(new MigLayout("wrap 2,fillx,insets 8, gap 8", "[grow 0,trail]15[fill]"));

		container.add(title, "wrap, span, al center, gapbottom 8");
		container.add(movieNameLabel);
		container.add(movieNameTextField);
		container.add(directorLabel);
		container.add(directorTextField);
		container.add(durationLabel);
		container.add(durationTextField);
		container.add(importPriceLabel);
		container.add(importPriceTextField);
		container.add(countryLabel);
		container.add(countryTextField);
		container.add(languageLabel);
		container.add(languageTextField);

		container.add(imageChooserAndFileChooserContainer, "span 2, grow");

		container.add(trailerLabel);
		container.add(trailerTextField);
		container.add(genreLabel);
		container.add(genreTextField);
		container.add(statusLabel);
		container.add(statusComboBox);
		container.add(descriptionLabel);
		container.add(new JScrollPane(descriptionTextArea));
		container.add(new JLabel(""));
		container.add(errorMessageLabel, "al left");
		container.add(updateButton, "span 2, al trail");

		ImageIcon calendarIcon = new ImageIcon("images/calendar.png");
		Image image = calendarIcon.getImage();
		Image newimg = image.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
		calendarIcon = new ImageIcon(newimg);

		releasedDateDateChooserButton.setIcon(calendarIcon);
		releasedDateDateChooserButton.addActionListener(e -> {
			releasedDateDateChooser.showPopup();
		});
		releasedDateDateChooser.setTextRefernce(releasedDateTextField);
		releasedDateDateChooser.addEventDateChooser(new EventDateChooser() {
			@Override
			public void dateSelected(SelectedAction action, SelectedDate date) {
				System.out.println(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
				if (action.getAction() == SelectedAction.DAY_SELECTED) {
					releasedDateDateChooser.hidePopup();
				}
			}
		});

		startDateDateChooserButton.setIcon(calendarIcon);
		startDateDateChooserButton.addActionListener(e -> {
			startDateDateChooser.showPopup();
		});
		startDateDateChooser.setTextRefernce(startDateTextField);
		startDateDateChooser.addEventDateChooser(new EventDateChooser() {
			@Override
			public void dateSelected(SelectedAction action, SelectedDate date) {
				System.out.println(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
				if (action.getAction() == SelectedAction.DAY_SELECTED) {
					startDateDateChooser.hidePopup();
				}
			}
		});

		imageSourceButton.addActionListener(this);
		updateButton.addActionListener(this);

		// styles
		movieNameLabel.setPreferredSize(releasedDateLabel.getPreferredSize());
		movieNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		descriptionTextArea.putClientProperty(FlatClientProperties.STYLE, "border:5,5,5,5," + "#ddd" + ",2,10");

		JScrollPane scroll = (JScrollPane) descriptionTextArea.getParent().getParent();
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");

		add(container);

		pack();
		setLocationRelativeTo(null);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(imageSourceButton)) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Choose Image File");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setAcceptAllFileFilterUsed(false);

			filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif", "bmp");
			fileChooser.addChoosableFileFilter(filter);

			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				String path = String.format("images/%s", selectedFile.getName());
				Image icon = new ImageIcon(path).getImage();
				displaypPosterLabel.setIcon(new ImageIcon(icon.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
			}
		}
		if (e.getSource().equals(updateButton)) {
			// get all values from the input fields
			String name = movieNameTextField.getText().trim();
			String director = directorTextField.getText().trim();
			String duration = durationTextField.getText().trim();
			String price = importPriceTextField.getText().trim();
			String country = countryTextField.getText().trim();
			String language = languageTextField.getText().trim();

			// check image path
			String imagePath = "";
			Icon icon = displaypPosterLabel.getIcon();
			if (icon != null) {
				System.out.println("JLabel is contain the image");
			} else {
				System.out.println("JLabel is not contain the image");
			}

			String releasedDate = releasedDateTextField.getText().trim();
			String startDate = startDateTextField.getText().trim();
			String trailer = trailerTextField.getText().trim();
			String genre = genreTextField.getText().trim();
			String status = (String) statusComboBox.getSelectedItem();
			String description = descriptionTextArea.getText().trim();

			if (name.equals("")) {
				errorMessageLabel.setText("Name must not be empty");
				movieNameTextField.requestFocus();
				return;
			}

			if (!name.matches("^[A-Z][a-zA-Z]*(\\s[A-Z][a-zA-Z]*)*$")) {
				errorMessageLabel.setText("Name must start with capital letters");
				movieNameTextField.requestFocus();
				return;
			}

			if (director.equals("")) {
				errorMessageLabel.setText("Director must not be empty");
				directorTextField.requestFocus();
				return;
			}

			if (!director.matches("^[A-Z][a-zA-Z]*(\\s[A-Z][a-zA-Z]*)*$")) {
				errorMessageLabel.setText("Director must start with capital letters");
				directorTextField.requestFocus();
				return;
			}

			if (duration.equals("")) {
				errorMessageLabel.setText("Director must not be empty");
				durationTextField.requestFocus();
				return;
			}

			if (!duration.matches("^\\d+$")) {
				errorMessageLabel.setText("Duration must be number");
				durationTextField.requestFocus();
				return;
			}

			if (price.equals("")) {
				errorMessageLabel.setText("Import price must not be empty");
				importPriceTextField.requestFocus();
				return;
			}

			if (!price.matches("^[0-9]+(\\.[0-9]+)?$")) {
				errorMessageLabel.setText("Import price must be a number");
				importPriceTextField.requestFocus();
				return;
			}

			if (language.equals("")) {
				errorMessageLabel.setText("Language must not be empty");
				languageTextField.requestFocus();
				return;
			}

			if (!language.matches("^[A-Z][a-zA-Z]*$")) {
				errorMessageLabel.setText("Language must sart with a capital letter");
				languageTextField.requestFocus();
				return;
			}

			if (country.equals("")) {
				errorMessageLabel.setText("Country must not be empty");
				countryTextField.requestFocus();
				return;
			}

			if (!country.matches("^[A-Z][a-zA-Z]*(\\\\s[A-Z][a-zA-Z]*)*$")) {
				errorMessageLabel.setText("Country must sart with a capital letters");
				countryTextField.requestFocus();
				return;
			}

			if (icon == null) {
				errorMessageLabel.setText("Poster image is required");
				imageSourceButton.requestFocus();
				return;
			}

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDate releasedDateLocalDate = LocalDate.parse(releasedDate, formatter);
			if (!releasedDateLocalDate.isBefore(LocalDate.now())) {
				errorMessageLabel.setText("Released date must be before today");
				releasedDateTextField.requestFocus();
				return;
			}

			LocalDate startDateLocalDate = LocalDate.parse(startDate, formatter);
			if (!startDateLocalDate.isBefore(LocalDate.now())) {
				errorMessageLabel.setText("Start date must be before today");
				startDateTextField.requestFocus();
				return;
			}

			if (trailer.equals("")) {
				errorMessageLabel.setText("Trailer must not be empty");
				trailerTextField.requestFocus();
				return;
			}

			if (!trailer.matches("^https?://(?:www\\.)?[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}(?:/[^\\s]*)?$")) {
				errorMessageLabel.setText("Trailer must be a valid URL");
				trailerTextField.requestFocus();
				return;
			}

			if (genre.equals("")) {
				errorMessageLabel.setText("Genre must not be empty");
				genreTextField.requestFocus();
				return;
			}

			if (!genre.matches("^[A-Z][a-zA-Z]*(?:, ?[A-Z][a-zA-Z]*)*$")) {
				errorMessageLabel.setText("Genre must start with capital letters and seperated by commas");
				genreTextField.requestFocus();
				return;
			}

			if (description.equals("")) {
				errorMessageLabel.setText("Description must not be empty");
				descriptionTextArea.requestFocus();
				return;
			}

			if (!description.matches("^.{20,}$")) {
				errorMessageLabel.setText("Description must be at least 20 characters long");
				descriptionTextArea.requestFocus();
				return;
			}

			int durationInt = Integer.parseInt(duration);

			try {
				if (selectedFile != null) {
					BufferedImage image = ImageIO.read(selectedFile);
					imagePath = String.format("images/%s", selectedFile.getName());
					File destinationFile = new File(imagePath);
					String extension = FilenameUtils.getExtension(selectedFile.getName()).toLowerCase();
					boolean isValidExtension = Arrays.asList(filter.getExtensions()).contains(extension);
					if (isValidExtension) {
						try {
							ImageIO.write(image, extension, destinationFile);
							System.out.println("File has been written successfully with path: " + imagePath);
						} catch (IOException exc) {
							exc.printStackTrace();
						}
					} else {
						System.out.println("Invalid file extension!");
					}
				} else {
//					imagePath = movie.getImageSource();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				System.out.println("Error saving image: " + ex.getMessage());
				return;
			}

			double importPriceDouble = Double.parseDouble(price);

			// create a new movie object
			System.out.println("image path: " + imagePath);
			Movie updatedMovie = new Movie(name, description, genre, director, durationInt, releasedDateLocalDate,
					language, country, trailer, startDateLocalDate, status, importPriceDouble, imagePath);
			// update the movie record with this id
			int rowsAffected = movieDAO.updateMovieByID(movie.getMovieID(), updatedMovie);
			if (rowsAffected <= 0) {
				JOptionPane.showMessageDialog(this, "Update unsuccessfully", "Failed", JOptionPane.ERROR_MESSAGE);
			} else {
				Notifications.getInstance().show(Notifications.Type.INFO, Location.BOTTOM_LEFT, "Update successfully");
				// refresh the table
				formMovieManagement.searchAndFilter();
				// clear textfields
				// close the dialog
				this.dispose();
			}
		}
	}

	public void setFormMovieManagement(FormMovieManagement formMovieManagement) {
		this.formMovieManagement = formMovieManagement;
	}

}