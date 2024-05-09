package gui.application.form.other.screening;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import dao.MovieScheduleSeatDAO;
import entity.Employee;
import entity.MovieSchedule;
import entity.MovieScheduleSeat;
import net.miginfocom.swing.MigLayout;
import raven.crazypanel.CrazyPanel;

public class SeatingOptionDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton selectedButton;
	private final JButton normalButton;
	private CrazyPanel container;
	private JPanel leftContainer;
	private JPanel rightContainer;
	private JLabel screenLabel;
	private JPanel seatsContainer;
	private JPanel noteContainer;
//	private JPanel availableContainer;
	private JPanel card;
	private JLabel image;
	private JButton continueButton;
	private MovieScheduleSeatDAO movieScheduleSeatDAO;
	private ArrayList<MovieScheduleSeat> seatChosenList;
	private JLabel movieNameLabel;
	private JLabel movieName;
	private JLabel screeningTimeLabel;
	private JLabel screeningTime;
	private JLabel roomLabel;
	private JLabel room;
	private JLabel perSeatPriceLabel;
	private JLabel perSeatPrice;
	private JLabel seatLabel;
	private JLabel seat;
	private JLabel total;
	private JLabel totalLabel;
	private String seats;
	private double totalDouble;
	private ProductOptionDialog productOptionDialog;
	private Employee currentEmployee;
//	private MovieSchedule movieSchedule;

	public SeatingOptionDialog(MovieSchedule movieSchedule) {
//		this.movieSchedule = movieSchedule;
		movieScheduleSeatDAO = new MovieScheduleSeatDAO();
		container = new CrazyPanel();
		leftContainer = new JPanel();
		rightContainer = new JPanel();

		normalButton = new JButton();
		selectedButton = new JButton();
		selectedButton.putClientProperty(FlatClientProperties.STYLE,
				"background:$App.accent.red;foreground:$App.accent.white");

		leftContainer.setLayout(new MigLayout("wrap, fill, insets 0", "[fill]", "[grow 0]15[fill][grow 0]"));
		screenLabel = new JLabel("Screen");
		screenLabel.setFont(new Font(screenLabel.getFont().getFontName(), screenLabel.getFont().getStyle(), 30));
		screenLabel.setForeground(new Color(255, 0, 0));
		screenLabel.setHorizontalAlignment(SwingConstants.CENTER);
		screenLabel.setOpaque(true);
		screenLabel.setBackground(new Color(50, 50, 50));
		leftContainer.add(screenLabel, "align center, h 50!");
		seatsContainer = new JPanel();
		leftContainer.add(new JScrollPane(seatsContainer));
		// scrolllbar
		JScrollPane scroll = (JScrollPane) seatsContainer.getParent().getParent();
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");

		// get all the buttons and show them onto the page
		seatsContainer
				.setLayout(new MigLayout("fill, wrap, insets 0", "[][][][][][][][][][][][][][][][]", "[align top]"));
		List<MovieScheduleSeat> movieScheduleSeatList = movieScheduleSeatDAO
				.getAllMovieScheduleSeatByMovieScheduleID(movieSchedule.getScheduleID());
		seatChosenList = new ArrayList<>();
		movieScheduleSeatList.forEach(movieScheduleSeat -> {
			JButton button = new JButton(movieScheduleSeat.getSeat().getSeatLocation());
			if (movieScheduleSeat.isSold()) {
				button.setOpaque(true);
				button.putClientProperty(FlatClientProperties.STYLE,
						"background:$App.accent.red;foreground:$App.accent.white");
				button.setFont(new Font(button.getFont().getFontName(), button.getFont().getStyle(), 9));
				seatsContainer.add(button, "width 30!, height 30!, grow"); // Adjust the width and height as needed
			} else {
				button.setFont(new Font(button.getFont().getFontName(), button.getFont().getStyle(), 9));
				seatsContainer.add(button, "width 30!, height 30!, grow"); // Adjust the width and height as needed
				// button.putClientProperty("forMovieScheduleSeat", movieScheduleSeat);
				// action listener
				boolean[] isChosen = new boolean[1];
				button.addActionListener(e -> {
//					if (seatChosenList.contains(movieScheduleSeat)) {
					if (isChosen[0]) {
						seatChosenList.remove(movieScheduleSeat);
						button.setBackground(normalButton.getBackground());
						button.setForeground(normalButton.getForeground());
						seats = "";
						seatChosenList.forEach(seatChosen -> {
							seats += seatChosen + ",";
						});
						if (!seatChosenList.isEmpty()) {
							seats = seats.substring(0, seats.length() - 1);
						}
						seat.setText(seats);
						totalDouble = 0;
						seatChosenList.forEach(seatChoosen -> {
							totalDouble += movieSchedule.getPerSeatPrice();
						});
						DecimalFormat df = new DecimalFormat("#0.00");
						total.setText("$" + df.format(totalDouble) + "");
					} else {
						seatChosenList.add(movieScheduleSeat);
//						button.setOpaque(true);
//						button.putClientProperty(FlatClientProperties.STYLE,
//								"background:$App.accent.red;foreground:$App.accent.white");
						button.setBackground(selectedButton.getBackground());
						button.setForeground(selectedButton.getForeground());
						seats = "";
						seatChosenList.forEach(seatChosen -> {
							seats += seatChosen + ",";
						});
						if (!seatChosenList.isEmpty()) {
							seats = seats.substring(0, seats.length() - 1);
						}
						seat.setText(seats);
						totalDouble = 0;
						seatChosenList.forEach(seatChoosen -> {
							totalDouble += movieSchedule.getPerSeatPrice();
						});
						DecimalFormat df = new DecimalFormat("#0.00");
						total.setText("$" + df.format(totalDouble) + "");
					}
					isChosen[0] = !isChosen[0];
				});
			}
		});

		noteContainer = new JPanel(new MigLayout("align center, gap 3", "[center]", ""));
		leftContainer.add(noteContainer, "dock south");
//		availableContainer = new JPanel(new MigLayout("wrap, align center", "[center]", ""));
		JPanel availablePanel = new JPanel(new MigLayout("align center, gap 10, insets 0 10 0 20", "[center]", ""));
		JLabel availableLabel = new JLabel("Available");
		JButton whiteButton = new JButton();
		availablePanel.add(availableLabel);
		availablePanel.add(whiteButton, "width 35!, height 35!");
		JPanel takenPanel = new JPanel(new MigLayout("align center, gap 10, insets 0 10 0 10", "[center]", ""));
		JLabel takenLabel = new JLabel("Taken");
		JButton redButton = new JButton();
		redButton.putClientProperty(FlatClientProperties.STYLE, "background:$App.accent.red");
		takenPanel.add(takenLabel);
		takenPanel.add(redButton, "width 35!, height 35!");
		noteContainer.add(availablePanel);
		noteContainer.add(takenPanel);

		rightContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[center]"));
		card = new JPanel();
		rightContainer.add(card);
		card.setLayout(new MigLayout("wrap 2, fill, gap 15", "[grow 0][fill]", ""));
		ImageIcon icon = new ImageIcon(movieSchedule.getMovie().getImageSource());
		if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
			icon = new ImageIcon("images/movie-poster-not-found.jpg");
		}
		// resize
		Image img = icon.getImage();
		Image resizedImg = img.getScaledInstance(200, -1, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resizedImg);
		image = new JLabel(resizedIcon);
		card.add(image, "span 2, al center, gapbottom 20");

		movieNameLabel = new JLabel("Movie name: ");
		Font bold = new Font(movieNameLabel.getFont().getFontName(), Font.BOLD, movieNameLabel.getFont().getSize());
		movieName = new JLabel();
		movieName.setFont(bold);
		screeningTimeLabel = new JLabel("Screening Time: ");
		screeningTime = new JLabel();
		screeningTime.setFont(bold);
		roomLabel = new JLabel("Room: ");
		room = new JLabel();
		room.setFont(bold);
		perSeatPriceLabel = new JLabel("PricePerSeat: ");
		perSeatPrice = new JLabel();
		perSeatPrice.setFont(bold);
		seatLabel = new JLabel("Seat: ");
		seat = new JLabel();
		seat.setFont(bold);
		totalLabel = new JLabel("Total: ");
		total = new JLabel();
		total.setFont(bold);

		continueButton = new JButton("Continue");
		continueButton.putClientProperty(FlatClientProperties.STYLE,
				"arc:5;hoverBackground:$primary;hoverForeground:$white");

		card.add(movieNameLabel);
		card.add(movieName, "gapleft push");
		card.add(screeningTimeLabel);
		card.add(screeningTime, "gapleft push");
		card.add(roomLabel);
		card.add(room, "gapleft push");
		card.add(perSeatPriceLabel);
		card.add(perSeatPrice, "gapleft push");
		card.add(seatLabel);
		card.add(seat, "gapleft push");
		card.add(totalLabel);
		card.add(total, "gapleft push");
		card.add(continueButton, "span, al center, gaptop 20");

		// fill the label with existing data
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		movieName.setText(movieSchedule.getMovie().getMovieName());
		screeningTime.setText(movieSchedule.getScreeningTime().format(formatter));
		room.setText(movieSchedule.getRoom().getRoomName());
		perSeatPrice.setText("$" + movieSchedule.getPerSeatPrice() + "");
		seat.setText("");
		total.setText("$0");

		container.setLayout(new MigLayout("fill", "[][]", "fill"));
		container.add(leftContainer, "growx, w 70%");
		container.add(rightContainer, "growx, w 30%");

		continueButton.addActionListener(e -> {

			if (seatChosenList.size() != 0) {
				productOptionDialog = new ProductOptionDialog(seatChosenList, movieSchedule);
				productOptionDialog.setCurrentEmployee(currentEmployee);
				productOptionDialog.setSeatingOptionDialog(this);
				productOptionDialog.setModal(true);
				productOptionDialog.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "Select at least one seat before continue!", "Failed",
						JOptionPane.ERROR_MESSAGE);
			}

		});

		// style
		card.setOpaque(true);
		card.putClientProperty(FlatClientProperties.STYLE, "background:$white;foreground:$black;border:20,10,20,10");
		perSeatPrice.putClientProperty(FlatClientProperties.STYLE, "foreground:$primary;font:$h5.font");
		total.putClientProperty(FlatClientProperties.STYLE, "foreground:$danger;font:$h3.font");

		add(container);
		this.setSize(1300, 800);
		setLocationRelativeTo(null);
//		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public void setCurrentEmployee(Employee currentEmployee) {
		this.currentEmployee = currentEmployee;
	}
}
