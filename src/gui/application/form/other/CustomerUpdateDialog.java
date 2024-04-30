package gui.application.form.other;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.raven.datechooser.DateChooser;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;

import dao.CustomerDAO;
import entity.Customer;
import net.miginfocom.swing.MigLayout;
import raven.crazypanel.CrazyPanel;

public class CustomerUpdateDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Customer customer;
	private CustomerDAO customerDAO;
	private CrazyPanel container;
	private JLabel title;
	private JLabel fullNameLabel;
	private JTextField fullNameTextField;
	private JLabel phoneNumberLabel;
	private JTextField phoneNumberTextField;
	private JLabel emailLabel;
	private JTextField emailTextField;
	private JLabel regDateLabel;
	private JTextField regDateTextField;
	private JLabel errorMessageLabel;
	private JButton updateButton;
	private DateChooser regDateDateChooser;
	private JButton regDateDateChooserButton;
	private FormCustomerManagement formCustomerManagement;

	public CustomerUpdateDialog(Customer customer) {
		this.customer = customer;
		customerDAO = new CustomerDAO();
		setLayout(new BorderLayout());
		initComponents();
	}

	private void initComponents() {
		this.setTitle("Updating Customer");
		container = new CrazyPanel();
		title = new JLabel("Update Customer Info");
		fullNameLabel = new JLabel("Full name: ");
		fullNameTextField = new JTextField(30);
		phoneNumberLabel = new JLabel("Phone number: ");
		phoneNumberTextField = new JTextField(30);
		emailLabel = new JLabel("Email: ");
		emailTextField = new JTextField(30);
		regDateLabel = new JLabel("Registration date: ");
		regDateTextField = new JTextField();
		regDateDateChooser = new DateChooser();
		regDateDateChooserButton = new JButton();
		errorMessageLabel = new JLabel();
		updateButton = new JButton("Update");
		
		// fill text fields with existing data
		fullNameTextField.setText(customer.getFullName());
		phoneNumberTextField.setText(customer.getPhoneNumber());
		emailTextField.setText(customer.getEmail());
		regDateDateChooser.setSelectedDate(new SelectedDate(customer.getRegDate().getDayOfMonth(), customer.getRegDate().getMonthValue(), customer.getRegDate().getYear()));
		
		// styles
		title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 18));
		errorMessageLabel.setForeground(Color.RED);

		// set layout
		container.setLayout(new MigLayout("wrap 2,fillx,insets 8, gap 8", "[grow 0,trail]15[fill]"));
		
		container.add(title, "wrap, span, al center, gapbottom 8");
		container.add(fullNameLabel);
		container.add(fullNameTextField);
		container.add(phoneNumberLabel);
		container.add(phoneNumberTextField);
		container.add(emailLabel);
		container.add(emailTextField);
		container.add(regDateLabel);
		container.add(regDateTextField, "grow 0, split 3, gapright 0");
		container.add(regDateDateChooserButton, "grow 0");
		container.add(new JLabel());
		container.add(errorMessageLabel, "span 2, al center");
		container.add(updateButton, "span 2, al trail");

		ImageIcon calendarIcon = new ImageIcon("images/calendar.png");
		Image image = calendarIcon.getImage();
		Image newimg = image.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
		calendarIcon = new ImageIcon(newimg);
		regDateDateChooserButton.setIcon(calendarIcon);
		regDateDateChooserButton.addActionListener(e -> {
			regDateDateChooser.showPopup();
		});
		regDateDateChooser.setTextRefernce(regDateTextField);
		regDateDateChooser.addEventDateChooser(new EventDateChooser() {
			@Override
			public void dateSelected(SelectedAction action, SelectedDate date) {
				System.out.println(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
				if (action.getAction() == SelectedAction.DAY_SELECTED) {
					regDateDateChooser.hidePopup();
				}
			}
		});
		
		// assign events
		updateButton.addActionListener(this);
		
		add(container);
		pack();
		setLocationRelativeTo(null);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(updateButton)) {
			// get all the value 
			String fullName = fullNameTextField.getText().trim();
			String phoneNumber = phoneNumberTextField.getText().trim();
			String email = emailTextField.getText().trim();
			String regDate = regDateTextField.getText().trim();
			// validate those values
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
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDate regDateLocalDate = LocalDate.parse(regDate, formatter);
			if (!regDateLocalDate.isBefore(LocalDate.now())) {
				errorMessageLabel.setText("Registration date must be before today");
				regDateTextField.requestFocus();
				return;
			}
			Customer updatedCustomer = new Customer(customer.getCustomerID(), fullName, phoneNumber, email, regDateLocalDate);
			// write an update query to the database with the id of this customer
			boolean isSuccessful = customerDAO.updateCustomer(updatedCustomer);
			if (isSuccessful) {
				JOptionPane.showMessageDialog(this, "Update successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
				formCustomerManagement.handleSearch();
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "An error has occured", "Error", JOptionPane.ERROR_MESSAGE);

			}
		}

	}

	public void setFormCustomerManagement(FormCustomerManagement formCustomerManagement) {
		this.formCustomerManagement = formCustomerManagement;
	}

}
