package gui.application.form.other;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.formdev.flatlaf.FlatClientProperties;

import dao.CustomerDAO;
import dao.MovieScheduleSeatDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.ProductDAO;
import entity.Customer;
import entity.Employee;
import entity.MovieSchedule;
import entity.MovieScheduleSeat;
import entity.Order;
import entity.OrderDetail;
import net.miginfocom.swing.MigLayout;

public class CheckoutDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel container;
	private JPanel customerInfoContainer;
	private JPanel leftContainer;
	private JPanel rightContainer;
	private JPanel checkoutContainer;
	private JPanel movieContainer;
	private JPanel productContainer;
	private JPanel noteContainer;
	private JLabel customerInfoTitle;
	private JLabel phoneNumberLabel;
	private JTextField phoneNumberTextField;
	private JLabel fullNameLabel;
	private JTextField fullNameTextField;
	private JLabel emailLabel;
	private JTextField emailTextField;
	private JLabel subtotalLabel;
	private JLabel subtotal;
	private JLabel totalLabel;
	private JLabel total;
	private JButton checkoutButton;
	private JLabel orderTitleLabel;
	private JPanel rightMainContainer;
	private JLabel movieTitleLabel;
	private JLabel movieLabel;
	private JLabel movie;
	private JLabel dateLabel;
	private JLabel date;
	private JLabel screeningTimeLabel;
	private JLabel screeningTime;
	private JLabel roomLabel;
	private JLabel room;
	private JLabel seatLabel;
	private JLabel totalTicketPriceLabel;
	private JLabel totalTickerPrice;
	private JLabel movieTotalLabel;
	private JLabel movieTotal;
	private JLabel seats;
	private JPanel productOrderDetailContainer;
	private JLabel productNameLabel;
	private JLabel multiplyLabel;
	private JLabel productLineTotalLabel;
	private JPanel productTotalContainer;
	private JLabel productTotalLabel;
	private JLabel productTotal;
	private JLabel productTitleLabel;
	private JPanel productTitleContainer;
	private JTextArea noteTextArea;
	private JLabel noteLabel;
	private JPanel movieTotalContainer;
	private JPanel movieDetailContainer;
	private JPanel movieTitleContainer;
	private JPanel customerInforForm;
	private JPanel customerTitleContainer;
	private JLabel errorMessageLabel;
	private ArrayList<MovieScheduleSeat> seatChosenList;
	private List<OrderDetail> chosenProductOrderDetailList;
	private MovieSchedule movieSchedule;
	private double totalDouble;
	private CustomerDAO customerDAO;
	private ProductDAO productDAO;
	private MovieScheduleSeatDAO movieScheduleSeatDAO;
	private Employee currentEmployee;
	private OrderDAO orderDAO;
	private OrderDetailDAO orderDetailDAO;
	private ProductOptionDialog productOptionDialog;

	public CheckoutDialog(ArrayList<MovieScheduleSeat> seatChosenList, List<OrderDetail> chosenProductOrderDetailList,
			MovieSchedule movieSchedule) {

		this.movieSchedule = movieSchedule;
		this.seatChosenList = seatChosenList;
		this.chosenProductOrderDetailList = chosenProductOrderDetailList;

		customerDAO = new CustomerDAO();
		productDAO = new ProductDAO();
		orderDetailDAO = new OrderDetailDAO();
		movieScheduleSeatDAO = new MovieScheduleSeatDAO();
		orderDAO = new OrderDAO();

		Border commonBorder = BorderFactory.createLineBorder(new Color(200, 200, 200));
		container = new JPanel();
		leftContainer = new JPanel();
		rightContainer = new JPanel();
		customerInfoContainer = new JPanel();
		checkoutContainer = new JPanel();
		rightMainContainer = new JPanel();
		movieContainer = new JPanel();
		productContainer = new JPanel();
		noteContainer = new JPanel();

		container.setLayout(new MigLayout("wrap, fill", "[][]", "[fill]"));
		container.add(leftContainer, "grow, w 30%");
		container.add(rightContainer, "grow, w 70%");

		leftContainer.setLayout(new MigLayout("wrap, fill", "[fill]", "[fill][grow 0]"));
		leftContainer.add(customerInfoContainer, "grow");
		leftContainer.add(checkoutContainer, "dock south");

		customerTitleContainer = new JPanel(new MigLayout("wrap, fill", "[left]", ""));
		customerInfoTitle = new JLabel("Customer Info");
		customerTitleContainer.add(customerInfoTitle);
		customerTitleContainer.setBorder(commonBorder);

		customerInfoContainer.setLayout(new MigLayout("wrap, fill, insets 0", "[fill]", "[grow 0][top]"));

		customerInforForm = new JPanel(new MigLayout("wrap, fillx", "[fill]", "[]"));
		phoneNumberLabel = new JLabel("Phone number: ");
		phoneNumberTextField = new JTextField(20);
		fullNameLabel = new JLabel("Full name: ");
		fullNameTextField = new JTextField(20);
		emailLabel = new JLabel("Email: ");
		emailTextField = new JTextField(20);
		errorMessageLabel = new JLabel();
		errorMessageLabel.setForeground(new Color(240, 0, 0));
		customerInforForm.add(phoneNumberLabel);
		customerInforForm.add(phoneNumberTextField, "gapbottom 15");
		customerInforForm.add(fullNameLabel);
		customerInforForm.add(fullNameTextField, "gapbottom 15");
		customerInforForm.add(emailLabel);
		customerInforForm.add(emailTextField, "gapbottom 15");
		customerInforForm.add(errorMessageLabel);
		customerInforForm.setBorder(commonBorder);

		customerInfoContainer.add(customerTitleContainer);
		customerInfoContainer.add(customerInforForm);

		orderTitleLabel = new JLabel("Order");
		subtotalLabel = new JLabel("Subtotal: ");
		subtotal = new JLabel();
		totalLabel = new JLabel("Total: ");
		total = new JLabel();
		checkoutButton = new JButton("Check out");
		checkoutContainer.setLayout(new MigLayout("wrap 2, fill", "[grow 0][fill]", "[grow]"));
		checkoutContainer.setBorder(commonBorder);
		checkoutContainer.add(orderTitleLabel, "span 2, align center");
		checkoutContainer.add(subtotalLabel);
		checkoutContainer.add(subtotal, "gapleft push");
		checkoutContainer.add(totalLabel);
		checkoutContainer.add(total, "gapleft push");
		checkoutContainer.add(checkoutButton, "span2, al trail");

		rightContainer.setLayout(new MigLayout("wrap, fill", "[fill]", "[fill][grow 0]"));
		rightContainer.add(rightMainContainer);
		rightContainer.add(noteContainer, "dock south");
		rightMainContainer.setLayout(new MigLayout("wrap, fill, insets 0", "[][]", "[fill]"));
		rightMainContainer.add(movieContainer, "grow, w 50%");
		rightMainContainer.add(productContainer, "grow, w 50%");

		movieContainer.setLayout(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill][grow 0]"));

		movieTitleContainer = new JPanel(new MigLayout("fill", "[left]", ""));
		movieTitleContainer.setBorder(commonBorder);
		movieTitleLabel = new JLabel("Movie");
		movieTitleContainer.add(movieTitleLabel);

		movieDetailContainer = new JPanel(new MigLayout("wrap 2, gap 15, fillx", "[grow 0][fill]", "[]"));
		movieDetailContainer.setBorder(commonBorder);
		movieLabel = new JLabel("Movie:");
		movie = new JLabel();
		dateLabel = new JLabel("Date:");
		date = new JLabel();
		screeningTimeLabel = new JLabel("Screening time:");
		screeningTime = new JLabel();
		roomLabel = new JLabel("Room:");
		room = new JLabel();
		seatLabel = new JLabel("Seat(s):");
		seats = new JLabel();
		totalTicketPriceLabel = new JLabel("Total ticket price:");
		totalTickerPrice = new JLabel();

		movieDetailContainer.add(movieLabel);
		movieDetailContainer.add(movie, "gapleft push");
		movieDetailContainer.add(dateLabel);
		movieDetailContainer.add(date, "gapleft push");
		movieDetailContainer.add(screeningTimeLabel);
		movieDetailContainer.add(screeningTime, "gapleft push");
		movieDetailContainer.add(roomLabel);
		movieDetailContainer.add(room, "gapleft push");
		movieDetailContainer.add(seatLabel);
		movieDetailContainer.add(seats, "gapleft push");
		movieDetailContainer.add(totalTicketPriceLabel);
		movieDetailContainer.add(totalTickerPrice, "gapleft push");

		movieTotalContainer = new JPanel(new MigLayout("fill", "[left][right]", "[fill]"));
		movieTotalContainer.setBorder(commonBorder);
		movieTotalLabel = new JLabel("Total:");
		movieTotal = new JLabel();
		movieTotalContainer.add(movieTotalLabel);
		movieTotalContainer.add(movieTotal);

		movie.setText(movieSchedule.getMovie().getMovieName());
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		date.setText(movieSchedule.getScreeningTime().format(dateFormatter));
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		screeningTime.setText(movieSchedule.getScreeningTime().format(timeFormatter) + " - "
				+ movieSchedule.getEndTime().format(timeFormatter));
		room.setText(movieSchedule.getRoom().getRoomName());
		String seatString = "";
		for (MovieScheduleSeat s : seatChosenList) {
			seatString += s + ",";
		}
		if (!seatChosenList.isEmpty()) {
			seatString = seatString.substring(0, seatString.length() - 1);
		}
		seats.setText(seatString);
		double movieTotalDouble = 0;
		for (MovieScheduleSeat s : seatChosenList) {
			movieTotalDouble += movieSchedule.getPerSeatPrice();
			System.out.println(s);
		}
		DecimalFormat df = new DecimalFormat("#0.00");
		totalTickerPrice.setText("$" + movieSchedule.getPerSeatPrice() + " x " + seatChosenList.size());
		movieTotal.setText("$" + df.format(movieTotalDouble) + "");

		movieContainer.add(movieTitleContainer);
		movieContainer.add(movieDetailContainer);
		movieContainer.add(movieTotalContainer, "dock south");

		productContainer.setLayout(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill][grow 0]"));

		productTitleContainer = new JPanel(new MigLayout("wrap", "[center]", ""));
		productTitleContainer.setBorder(commonBorder);
		productTitleLabel = new JLabel("Food and drinks");
		productTitleContainer.add(productTitleLabel);

		productOrderDetailContainer = new JPanel(new MigLayout("wrap, gap 10, fillx", "[fill]", ""));
		productOrderDetailContainer.setBorder(commonBorder);

		for (OrderDetail od : chosenProductOrderDetailList) {
			JPanel productOrderDetailCard = new JPanel(new MigLayout("wrap, fillx", "[left][right]", "[][]"));
			productNameLabel = new JLabel();
			productNameLabel.setText(od.getProduct().getProductName());
			multiplyLabel = new JLabel();
			multiplyLabel.setText("$" + od.getQuantity() + " x " + od.getProduct().getPrice());
			productLineTotalLabel = new JLabel();
			productLineTotalLabel.setText("$" + od.getQuantity() * od.getProduct().getPrice() + "");
			productOrderDetailCard.add(productNameLabel, "span 2");
			productOrderDetailCard.add(multiplyLabel);
			productOrderDetailCard.add(productLineTotalLabel);
			productOrderDetailContainer.add(productOrderDetailCard);
		}

		productTotalContainer = new JPanel(new MigLayout("wrap, fillx", "[left][right]", "[fill]"));
		productTotalContainer.setBorder(commonBorder);
		productTotalLabel = new JLabel("Total");
		double productTotalDouble = 0;
		for (OrderDetail od : chosenProductOrderDetailList) {
			od.setTotal();
			productTotalDouble += od.getTotal();
		}
		productTotal = new JLabel("$" + productTotalDouble + "");
		productTotalContainer.add(productTotalLabel);
		productTotalContainer.add(productTotal);

		totalDouble = movieTotalDouble + productTotalDouble;
		subtotal.setText("$" + df.format(totalDouble) + "");
		total.setText("$" + df.format(totalDouble) + "");

		productContainer.add(productTitleContainer, "span 2, align center");
		productContainer.add(new JScrollPane(productOrderDetailContainer));
		productContainer.add(productTotalContainer, "dock south");

		// scrolllbar
		JScrollPane chosenScroll = (JScrollPane) productOrderDetailContainer.getParent().getParent();
		chosenScroll.setBorder(BorderFactory.createEmptyBorder());
		chosenScroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");

		noteTextArea = new JTextArea();
		noteTextArea.setRows(5);
		noteContainer.setLayout(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));
		noteLabel = new JLabel("Comment");
		noteContainer.add(noteLabel);
		noteContainer.add(noteTextArea);
		noteContainer.setBorder(commonBorder);

		// additional styles
		JLabel normalLabel = new JLabel();
		Font commonFont = new Font(normalLabel.getFont().getFontName(), Font.BOLD, 24);
		customerInfoTitle.setFont(commonFont);
		movieTitleLabel.setFont(commonFont);
		productTitleLabel.setFont(commonFont);
		noteLabel.setFont(commonFont);
		orderTitleLabel.setFont(commonFont);

		// handle event listener
		checkoutButton.addActionListener(e -> {
			// add one more customer into the database
			String phoneNumber = phoneNumberTextField.getText().trim();
			String fullName = fullNameTextField.getText().trim();
			String email = emailTextField.getText().trim();
			if (phoneNumber.equals("")) {
				errorMessageLabel.setText("Phone number must not be empty");
				phoneNumberTextField.requestFocus();
				return;
			}
			if (!phoneNumber.matches("\\d{10}")) {
				errorMessageLabel.setText("Phone number must have 10 digits");
				phoneNumberTextField.requestFocus();
				return;
			}
			if (fullName.equals("")) {
				errorMessageLabel.setText("Full name must not be empty");
				fullNameTextField.requestFocus();
				return;
			}
			if (!fullName.matches("^[A-Z][a-zA-Z]*(\\s[A-Z][a-zA-Z]*)*$")) {
				errorMessageLabel.setText("Full name must start with capital letters");
				fullNameTextField.requestFocus();
				return;
			}
			if (email.equals("")) {
				errorMessageLabel.setText("Email must not be empty");
				emailTextField.requestFocus();
				return;
			}
			if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
				errorMessageLabel.setText("Email must be in the right format");
				emailTextField.requestFocus();
				return;
			}

			errorMessageLabel.setText("");

			boolean isCustomerExist = customerDAO.checkDuplicatePhoneNumber(phoneNumber);
			String customerID;
			if (!isCustomerExist) {
				customerID = customerDAO.addNewCustomer(new Customer(phoneNumber, fullName, email, LocalDate.now()));
			} else {
				customerID = customerDAO.getCustomerByPhoneNumber(phoneNumber);
			}
			// update the quantity of the products in the database
			for (OrderDetail od : chosenProductOrderDetailList) {
				productDAO.updateQtyById(od.getProduct().getProductID(), od.getQuantity());
			}
			// update those seats that are be taken
			for (MovieScheduleSeat seat : seatChosenList) {
				String seatID = seat.getSeat().getSeatID();
				String scheduleID = seat.getMovieSchedule().getScheduleID();
				movieScheduleSeatDAO.updateSeatTaken(seatID, scheduleID);
			}
			// store everything in the order
			LocalDateTime orderDate = LocalDateTime.now();
			int quantitySeat = seatChosenList.size();
			String note = noteTextArea.getText().trim();
			// totalDouble
			// customerID
			Order newOrder = new Order(orderDate, quantitySeat, note, totalDouble, new Customer(customerID),
					currentEmployee, movieSchedule);
			String newOrderID = orderDAO.addNewOrder(newOrder);
			newOrder.setOrderID(newOrderID);
			chosenProductOrderDetailList.forEach(chosenProductOrderDetail -> {
				chosenProductOrderDetail.setOrder(newOrder);
			});

			if (newOrderID != null) {
				chosenProductOrderDetailList.forEach(chosenProductOrderDetail -> {
					orderDetailDAO.addNewOrderDetail(chosenProductOrderDetail);
				});

				JOptionPane.showMessageDialog(this, "Order has been added successfully", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				this.dispose();
				productOptionDialog.dispose();
				productOptionDialog.disposeSeatOptionDialog();
			} else {
				JOptionPane.showMessageDialog(this, "An error has occurred", "Failure", JOptionPane.ERROR_MESSAGE);
			}
		});

		add(container);

		this.setSize(1300, 800);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public void setCurrentEmployee(Employee currentEmployee) {
		this.currentEmployee = currentEmployee;
	}

	public void setProductOptionDialog(ProductOptionDialog productOptionDialog) {
		this.productOptionDialog = productOptionDialog;
	}
}
