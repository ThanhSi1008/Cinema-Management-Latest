package gui.application.form.other.screening;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.raven.datechooser.DateChooser;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;
import com.raven.swing.TimePicker;

import dao.MovieDAO;
import dao.MovieScheduleDAO;
import dao.RoomDAO;
import entity.Movie;
import entity.MovieSchedule;
import entity.Room;
import net.miginfocom.swing.MigLayout;
import raven.crazypanel.CrazyPanel;

public class ScreeningUpdateDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Note: remember to add clear button
	private CrazyPanel container;
	private JLabel title;
	private FormScreeningManagement formScreeningManagement;
	private JButton saveButton;
	private JLabel errorMessageLabel;
	private MovieScheduleDAO movieScheduleDAO;
	private JLabel movieNameLabel;
	private JComboBox<Movie> movieNameCombobox;
	private MovieDAO movieDAO;
	private JLabel screeningDateLabel;
	private JTextField screeningDateTextField;
	private DateChooser screeningDateDateChooser;
	private JButton screeningDateDateChooserButton;
	private JLabel screeningTimeLabel;
	private JTextField screeningTimeTextField;
	private JLabel roomLabel;
	private JComboBox<Room> roomCombobox;
	private RoomDAO roomDAO;
	private JLabel perSeatPriceLabel;
	private JTextField perSeatPriceTextField;
	private TimePicker screeningTimeTimePicker;
	private JButton screeningTimeTimePickerButton;
	private MovieSchedule movieSchedule;
	private FormScreeningManagement2 formScreeningManagement2;

	public ScreeningUpdateDialog(MovieSchedule movieSchedule) {
		this.movieSchedule = movieSchedule;
		movieScheduleDAO = new MovieScheduleDAO();
		movieDAO = new MovieDAO();
		roomDAO = new RoomDAO();
		setLayout(new BorderLayout());
		initComponents();
	}

	private void initComponents() {
		this.setTitle("Updating Movie Schedule");
		container = new CrazyPanel();
		title = new JLabel("UPDATE MOVIE SCHEDULE");
		movieNameLabel = new JLabel("Movie name: ");
		movieNameCombobox = new JComboBox<Movie>();
		movieDAO.getAllMovie().forEach(movie -> {
			movieNameCombobox.addItem(movie);
		});
		screeningDateLabel = new JLabel("Screening Date: ");
		screeningDateTextField = new JTextField();
		screeningDateDateChooser = new DateChooser();
		screeningDateDateChooserButton = new JButton();

		screeningTimeLabel = new JLabel("Screen Time: ");
		screeningTimeTextField = new JTextField(20);
		screeningTimeTimePicker = new TimePicker();
		screeningTimeTimePickerButton = new JButton();

		roomLabel = new JLabel("Room: ");
		roomCombobox = new JComboBox<Room>();
		roomDAO.getAllRoom().forEach(room -> {
			roomCombobox.addItem(room);
		});
		perSeatPriceLabel = new JLabel("Per Seat Price: ");
		perSeatPriceTextField = new JTextField(30);
		errorMessageLabel = new JLabel();
		saveButton = new JButton("Save");

		// fill text fields with existing data
		movieNameCombobox.setSelectedItem(movieSchedule.getMovie());
		screeningDateDateChooser.setSelectedDate(new SelectedDate(movieSchedule.getScreeningTime().getDayOfMonth(),
				movieSchedule.getScreeningTime().getMonthValue(), movieSchedule.getScreeningTime().getYear()));

		screeningTimeTimePicker.setSelectedTime(Date.from(LocalDateTime.of(movieSchedule.getScreeningTime().getYear(),
				movieSchedule.getScreeningTime().getMonthValue(), movieSchedule.getScreeningTime().getDayOfMonth(),
				movieSchedule.getScreeningTime().getHour(), movieSchedule.getScreeningTime().getMinute())
				.atZone(ZoneId.systemDefault()).toInstant()));

		roomCombobox.setSelectedItem(movieSchedule.getRoom());
		perSeatPriceTextField.setText(movieSchedule.getPerSeatPrice() + "");

		container.setLayout(new MigLayout("wrap 2,fillx,insets 8, gap 8", "[grow 0,trail]15[fill]"));

		// styles
		title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 24));
		errorMessageLabel.setForeground(Color.RED);

		// add into container
		container.add(title, "wrap, span, al center, gapbottom 8");
		container.add(movieNameLabel);
		container.add(movieNameCombobox);
		container.add(screeningDateLabel);
		container.add(screeningDateTextField, "grow 0, split 3, gapright 0");
		container.add(screeningDateDateChooserButton, "grow 0");
		container.add(new JLabel());
		container.add(screeningTimeLabel);
		container.add(screeningTimeTextField, "grow 0, split 3, gapright 0");
		container.add(screeningTimeTimePickerButton, "grow 0");
		container.add(new JLabel());
		container.add(roomLabel);
		container.add(roomCombobox);
		container.add(perSeatPriceLabel);
		container.add(perSeatPriceTextField);
		container.add(errorMessageLabel, "span 2, al center");
		container.add(saveButton, "span 2, al trail");

		// date chooser
		ImageIcon calendarIcon = new ImageIcon("images/calendar.png");
		Image image = calendarIcon.getImage();
		Image newimg = image.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
		calendarIcon = new ImageIcon(newimg);

		screeningDateDateChooserButton.setIcon(calendarIcon);
		screeningDateDateChooserButton.addActionListener(e -> {
			screeningDateDateChooser.showPopup();
		});
		screeningDateDateChooser.setTextRefernce(screeningDateTextField);
		screeningDateDateChooser.addEventDateChooser(new EventDateChooser() {
			@Override
			public void dateSelected(SelectedAction action, SelectedDate date) {
				if (action.getAction() == SelectedAction.DAY_SELECTED) {
					screeningDateDateChooser.hidePopup();
				}
			}
		});

		// time picker
		ImageIcon clockIcon = new ImageIcon("images/clock.png");
		Image clockImage = clockIcon.getImage();
		Image newClockImage = clockImage.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
		clockIcon = new ImageIcon(newClockImage);

		screeningTimeTimePicker.setForeground(new Color(138, 48, 191));
		screeningTimeTimePicker.setDisplayText(screeningTimeTextField);
		screeningTimeTimePickerButton.setIcon(clockIcon);
		screeningTimeTimePickerButton.addActionListener(e -> {
			screeningTimeTimePicker.showPopup(this, (getWidth() - screeningTimeTimePicker.getPreferredSize().width) / 2,
					(getHeight() - screeningTimeTimePicker.getPreferredSize().height) / 2);
		});

		// event listeners
		saveButton.addActionListener(this);

		// set up frame
		add(container);
		pack();
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(saveButton)) {
			// get the the value from the text fields
			Movie movie = (Movie) movieNameCombobox.getSelectedItem();
			String screeningDate = screeningDateTextField.getText().trim();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDate screeningDateLocalDate = LocalDate.parse(screeningDate, formatter);
			if (!screeningDateLocalDate.isAfter(LocalDate.now())) {
				errorMessageLabel.setText("Screening date must be after today");
				screeningDateTextField.requestFocus();
				return;
			}

			String screeningTime = screeningTimeTextField.getText().trim();
			LocalTime screeningTimeLocalTime;
			if (screeningTime.contains("AM") || screeningTime.contains("PM")) {
				DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("hh:mm a");
				screeningTimeLocalTime = LocalTime.parse(screeningTime, inputFormat);
			} else {
				DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("HH:mm");
				screeningTimeLocalTime = LocalTime.parse(screeningTime, inputFormat);
			}

			LocalDateTime screeningDateTime = LocalDateTime.of(screeningDateLocalDate, screeningTimeLocalTime);

			Room room = (Room) roomCombobox.getSelectedItem();
			String perSeatPrice = perSeatPriceTextField.getText().trim();
			// check to see if they are valid
			if (perSeatPrice.equals("")) {
				errorMessageLabel.setText("Seat price must not be empty");
				perSeatPriceTextField.requestFocus();
				return;
			}
			if (!perSeatPrice.matches("^\\d+(\\.\\d+)?$")) {
				errorMessageLabel.setText("Seat price must not be a double");
				perSeatPriceTextField.requestFocus();
				return;
			}
			double perSeatPriceDouble = Double.parseDouble(perSeatPrice);
			// update them into the database
			boolean isSuccessful = movieScheduleDAO.updateMovieSchedule(new MovieSchedule(movieSchedule.getScheduleID(),
					screeningDateTime, movie, room, perSeatPriceDouble));
			if (isSuccessful) {
				JOptionPane.showMessageDialog(this, "Udpate movie schedule successfully", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				this.dispose();
				formScreeningManagement.handleSearchAndFilter();
			} else {
				JOptionPane.showMessageDialog(this, "An error has occured", "Failed", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void setFormScreeningManagement(FormScreeningManagement formScreeningManagement) {
		this.formScreeningManagement = formScreeningManagement;
	}

	public void setFormScreeningManagement(FormScreeningManagement2 formScreeningManagement2) {
		this.formScreeningManagement2 = formScreeningManagement2;
	}
}
