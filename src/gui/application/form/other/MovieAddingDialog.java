package gui.application.form.other;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.raven.datechooser.DateChooser;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;

import dao.MovieDAO;
import entity.Movie;
import net.miginfocom.swing.MigLayout;
import raven.crazypanel.CrazyPanel;

public class MovieAddingDialog extends JDialog implements ActionListener {

	final JFileChooser fc = new JFileChooser();
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
	private JTextField statusTextField;
	private JLabel trailerLabel;
	private JTextField trailerTextField;
	private JLabel genreLabel;
	private JTextField genreTextField;
	private JLabel descriptionLabel;
	private File selectedFile;
	private JFileChooser fileChooser;
	private JLabel startDateLabel;
	private JTextField startDateTextField;
	private JLabel title;
	private JButton saveButton;
	private JTextArea descriptionTextArea;
	private JTextField filePathField;
	private JButton imageSourceButton;
	private BufferedImage originalBI;
	private JLabel fileNameLabel;
	private JButton releasedDateDateChooserButton;
	private JButton startDateDateChooserButton;
	private DateChooser releasedDateDateChooser;
	private DateChooser startDateDateChooser;
	private JComboBox<String> statusComboBox;
	private MovieDAO movieDAO;
	private FormMovieManagement formMovieManagement;
	private JLabel errorMessageLabel;

	public MovieAddingDialog() {
		movieDAO = new MovieDAO();
		setLayout(new BorderLayout());
		initComponents();
	}

	private void initComponents() {
		this.setTitle("Adding Movie");
		container = new CrazyPanel();
		title = new JLabel("ADD MOVIE");
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
		fileNameLabel = new JLabel();
		releasedDateLabel = new JLabel("Released Date: ");
		releasedDateTextField = new JTextField();
		statusLabel = new JLabel("Status: ");
		statusTextField = new JTextField(20);
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
		errorMessageLabel = new JLabel();
		saveButton = new JButton("Save");
		releasedDateDateChooser = new DateChooser();
		startDateDateChooser = new DateChooser();
		releasedDateDateChooserButton = new JButton();
		startDateDateChooserButton = new JButton();

		title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 24));
		descriptionTextArea.setRows(3);
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		descriptionTextArea.setBorder(BorderFactory.createLineBorder(new Color(217, 217, 217), 2));
		errorMessageLabel.setForeground(Color.RED);

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
		container.add(imageSourceLabel);
		container.add(imageSourceButton, "grow 0, split 2");
		container.add(fileNameLabel);
		container.add(releasedDateLabel);
		container.add(releasedDateTextField, "grow 0, split 3, gapright 0");
		container.add(releasedDateDateChooserButton, "grow 0");
		container.add(new JLabel());
		container.add(startDateLabel);
		container.add(startDateTextField, "grow 0, split 3, gapright 0");
		container.add(startDateDateChooserButton, "grow 0");
		container.add(new JLabel());
		container.add(trailerLabel);
		container.add(trailerTextField);
		container.add(genreLabel);
		container.add(genreTextField);
		container.add(statusLabel);
		container.add(statusComboBox);
		container.add(descriptionLabel);
		container.add(descriptionTextArea);
		container.add(errorMessageLabel, "span 2, al center");
		container.add(saveButton, "span 2, al trail");

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
		saveButton.addActionListener(this);

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

			FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp");
			fileChooser.addChoosableFileFilter(filter);

			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				fileNameLabel.setText(selectedFile.getName());
			}
		}
		if (e.getSource().equals(saveButton)) {
			// get all values from the input fields
			String name = movieNameTextField.getText().trim();
			String director = directorTextField.getText().trim();
			String duration = durationTextField.getText().trim();
			String price = importPriceTextField.getText().trim();
			String country = countryTextField.getText().trim();
			String language = languageTextField.getText().trim();
			String imagePath = fileNameLabel.getText().trim();
			String releasedDate = releasedDateTextField.getText().trim();
			String startDate = startDateTextField.getText().trim();
			String trailer = trailerTextField.getText().trim();
			String genre = genreTextField.getText().trim();
			String status = (String) statusComboBox.getSelectedItem();
			String description = descriptionTextArea.getText().trim();
			
			// all fields must not be empty
			// name must start with capital letters
			// directors must start with capital letters
			// duration must be a number
			// price must be a number
			// language must start with a capital letters
			// released date must be before today
			// start date must be before today
			// trailer must be in the right format
			// description must have at least 200 letter long
			
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
			
			if (imagePath.equals("")) {
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
			
			try {
				BufferedImage image = ImageIO.read(selectedFile);
				imagePath = String.format("images/%s", selectedFile.getName());
				File destinationFile = new File(imagePath);
				ImageIO.write(image, "jpg", destinationFile);
				System.out.println("Image saved successfully!");
			} catch (IOException ex) {
				ex.printStackTrace();
				System.out.println("Error saving image: " + ex.getMessage());
				return;
			}
			
			int durationInt = Integer.parseInt(duration);
			
			double importPriceDouble = Double.parseDouble(price);
			
			
			// create a new movie object
			Movie newMovie = new Movie(name, description, genre, director, durationInt, releasedDateLocalDate, language, country, trailer, startDateLocalDate, status, importPriceDouble, imagePath);
			// add a record in the database
			movieDAO.addNewMovie(newMovie);
			JOptionPane.showMessageDialog(this, "Add successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
			// refresh the table
			// clear textfields
			// close 10 fas tfingers
			
			this.dispose();
			formMovieManagement.searchAndFilter();
		}
	}

	public void setFormMovieManagement(FormMovieManagement formMovieManagement) {
		this.formMovieManagement = formMovieManagement;
	}

}
