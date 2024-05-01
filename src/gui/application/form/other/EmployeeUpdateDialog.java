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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.raven.datechooser.DateChooser;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;

import dao.AccountDAO;
import dao.EmployeeDAO;
import entity.Account;
import entity.Employee;
import net.miginfocom.swing.MigLayout;
import raven.crazypanel.CrazyPanel;

public class EmployeeUpdateDialog extends JDialog implements ActionListener {
	
	// Note: remember to add clear button
	private EmployeeDAO employeeDAO;
	private CrazyPanel container;
	private JLabel title;
	private FormStaffManagement formStaffManagement;
	private JLabel basicInfoLabel;
	private JLabel fullNameLabel;
	private JTextField fullNameTextField;
	private JLabel genderLabel;
	private JLabel dateOfBirthLabel;
	private JTextField dateOfBirthTextField;
	private JLabel emailLabel;
	private JTextField emailTextField;
	private JLabel phoneNumberLabel;
	private JTextField phoneNumberTextField;
	private JLabel roleLabel;
	private JLabel startDateLabel;
	private JTextField startDateTextField;
	private JLabel salaryLabel;
	private JTextField salaryTextField;
	private JLabel loginCredentialsLabel;
	private JLabel usernameLabel;
	private JTextField usernameTextField;
	private JLabel passwordLabel;
	private JLabel reenterPasswordLabel;
	private JComboBox<String> genderCombobox;
	private JComboBox<String> roleCombobox;
	private JPasswordField passwordPasswordField;
	private JPasswordField reenterPasswordPasswordField;
	private DateChooser dateOfBirthDateChooser;
	private JButton dateOfBirthDateChooserButton;
	private DateChooser startDateDateChooser;
	private JButton startDateDateChooserButton;
	private JButton updateButton;
	private JLabel errorMessageLabel;
	private AccountDAO accountDAO;
	private Employee employee;
	private Account account;
	public EmployeeUpdateDialog(Employee employee) {
		this.employee = employee;
		employeeDAO = new EmployeeDAO();
		accountDAO = new AccountDAO();
		account = accountDAO.getAccountByEmployeeID(employee.getEmployeeID());
		setLayout(new BorderLayout());
		initComponents();
	}

	private void initComponents() {
		this.setTitle("Updating Dialog");
		container = new CrazyPanel();
		title = new JLabel("UPDATE EMPLOYEE");
		
		basicInfoLabel = new JLabel("Basic Employee Information");
		fullNameLabel = new JLabel("Full name: ");
		fullNameTextField = new JTextField(30);
		genderLabel = new JLabel("Gender: ");
		genderCombobox = new JComboBox<String>();
		genderCombobox.addItem("Male");
		genderCombobox.addItem("Female");
		dateOfBirthLabel = new JLabel("Date of birth: ");
		dateOfBirthTextField = new JTextField();
		dateOfBirthDateChooser = new DateChooser();
		dateOfBirthDateChooserButton = new JButton();
		emailLabel = new JLabel("Email: ");
		emailTextField = new JTextField(30);
		phoneNumberLabel = new JLabel("Phone Number:");
		phoneNumberTextField = new JTextField(30);
		roleLabel = new JLabel("Role:");
		roleCombobox = new JComboBox<String>();
		roleCombobox.addItem("Manager");
		roleCombobox.addItem("Employee");
		startDateLabel = new JLabel("Start Date:");
		startDateTextField = new JTextField();
		startDateDateChooser = new DateChooser();
		startDateDateChooserButton = new JButton();
		salaryLabel = new JLabel("Salary:");
		salaryTextField = new JTextField(30);
		loginCredentialsLabel = new JLabel("Login Credentials");
		usernameLabel = new JLabel("Username: ");
		usernameTextField = new JTextField(30);
		passwordLabel = new JLabel("Password: ");
		passwordPasswordField = new JPasswordField(30);
		reenterPasswordLabel = new JLabel("Re-enter Password: ");
		reenterPasswordPasswordField = new JPasswordField(30);
		errorMessageLabel = new JLabel();
		updateButton = new JButton("Update");
		
		container.setLayout(new MigLayout("wrap 2,fillx,insets 8, gap 8", "[grow 0,trail]15[fill]"));
		
		// fill text fields with existing data
		fullNameTextField.setText(employee.getFullName());
		genderCombobox.setSelectedItem(employee.isGender() ? "Male" : "Female");
		dateOfBirthDateChooser.setSelectedDate(new SelectedDate(employee.getDateOfBirth().getDayOfMonth(), employee.getDateOfBirth().getMonthValue(), employee.getDateOfBirth().getYear()));
		emailTextField.setText(employee.getEmail());
		phoneNumberTextField.setText(employee.getPhoneNumber());
		roleCombobox.setSelectedItem(employee.getRole());
		startDateDateChooser.setSelectedDate(new SelectedDate(employee.getStartingDate().getDayOfMonth(), employee.getStartingDate().getMonthValue(), employee.getStartingDate().getYear()));
		salaryTextField.setText(employee.getSalary() + "");
		usernameTextField.setText(account.getUsername());
		// styles
		title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 24));
		errorMessageLabel.setForeground(Color.RED);
		basicInfoLabel.setFont(new Font(basicInfoLabel.getFont().getFontName(), Font.BOLD, 20));
		loginCredentialsLabel.setFont(new Font(basicInfoLabel.getFont().getFontName(), Font.BOLD, 20));
		
		// add into container
		container.add(title, "wrap, span, al center, gapbottom 8");
		container.add(basicInfoLabel, "span 2, al lead, gapbottom 8");
		container.add(fullNameLabel);
		container.add(fullNameTextField);
		container.add(genderLabel);
		container.add(genderCombobox);
		container.add(dateOfBirthLabel);
		container.add(dateOfBirthTextField, "grow 0, split 3, gapright 0");
		container.add(dateOfBirthDateChooserButton, "grow 0");
		container.add(new JLabel());
		container.add(emailLabel);
		container.add(emailTextField);
		container.add(phoneNumberLabel);
		container.add(phoneNumberTextField);
		container.add(roleLabel);
		container.add(roleCombobox);
		container.add(startDateLabel);
		container.add(startDateTextField, "grow 0, split 3, gapright 0");
		container.add(startDateDateChooserButton, "grow 0");
		container.add(new JLabel());
		container.add(salaryLabel);
		container.add(salaryTextField);
		container.add(errorMessageLabel, "span 2, al center");
		container.add(loginCredentialsLabel, "span 2, al lead, gapbottom 8");
		container.add(usernameLabel);
		container.add(usernameTextField);
		container.add(passwordLabel);
		container.add(passwordPasswordField);
		container.add(reenterPasswordLabel);
		container.add(reenterPasswordPasswordField);
		container.add(errorMessageLabel, "span 2, al center");
		container.add(updateButton, "span 2, al trail");	
		
		
		// date chooser
		ImageIcon calendarIcon = new ImageIcon("images/calendar.png");
		Image image = calendarIcon.getImage();
		Image newimg = image.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
		calendarIcon = new ImageIcon(newimg);

		dateOfBirthDateChooserButton.setIcon(calendarIcon);
		dateOfBirthDateChooserButton.addActionListener(e -> {
			dateOfBirthDateChooser.showPopup();
		});
		dateOfBirthDateChooser.setTextRefernce(dateOfBirthTextField);
		dateOfBirthDateChooser.addEventDateChooser(new EventDateChooser() {
			@Override
			public void dateSelected(SelectedAction action, SelectedDate date) {
				System.out.println(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
				if (action.getAction() == SelectedAction.DAY_SELECTED) {
					dateOfBirthDateChooser.hidePopup();
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
		
		// event listeners
		updateButton.addActionListener(this);
		
		// set up frame
		add(container);
		pack();
		setLocationRelativeTo(null);
	}

	public void setFormStaffManagement(FormStaffManagement formStaffManagement) {
		this.formStaffManagement = formStaffManagement;	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(updateButton)) {
			// take all the information form the text fields
			String fullName = fullNameTextField.getText().trim();
			String gender = (String) genderCombobox.getSelectedItem();
			String dob = dateOfBirthTextField.getText().trim();
			String email = emailTextField.getText().trim();
			String phoneNumber = phoneNumberTextField.getText().trim();
			String role = (String) roleCombobox.getSelectedItem();
			String startDate = (String) startDateTextField.getText().trim();
			String salary = salaryTextField.getText().trim();
			String username = usernameTextField.getText().trim();
			String password = String.valueOf(passwordPasswordField.getPassword());
			String reenterPassword = String.valueOf(reenterPasswordPasswordField.getPassword());
			// check to see if they are valid
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
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDate dateOfBirthLocalDate = LocalDate.parse(dob, formatter);
			if (!dateOfBirthLocalDate.isBefore(LocalDate.now())) {
				errorMessageLabel.setText("Date of birthmust be before today");
				dateOfBirthTextField.requestFocus();
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
			LocalDate startDateLocalDate = LocalDate.parse(startDate, formatter);
			if (startDateLocalDate.isAfter(LocalDate.now())) {
				errorMessageLabel.setText("Start date must be before or equal to today");
				startDateTextField.requestFocus();
				return;
			}
			if (salary.equals("")) {
				errorMessageLabel.setText("Salary must not be empty");
				salaryTextField.requestFocus();
				return;
			}
			if (!salary.matches("^[0-9]+(\\.[0-9]+)?$")) {
				errorMessageLabel.setText("Salary must be a double");
				salaryTextField.requestFocus();
				return;
			}
			if (username.equals("")) {
				errorMessageLabel.setText("Username must not be empty");
				usernameTextField.requestFocus();
				return;
			}
			if (!accountDAO.checkAvalibility(username)) {
				errorMessageLabel.setText("This username is not available");
				usernameTextField.requestFocus();
				return;
			}
			if (password.equals("")) {
				errorMessageLabel.setText("Password must not be empty");
				passwordPasswordField.requestFocus();
				return;
			}
			if (!password.matches("^.{8,}$")) {
				errorMessageLabel.setText("Password must be at least 8 character long");
				passwordPasswordField.requestFocus();
				return;
			}
			if (!reenterPassword.equals(password)) {
				errorMessageLabel.setText("Re-enter password must be the same as password");
				reenterPasswordPasswordField.requestFocus();
				return;
			}
			// update it to the database
			boolean genderBoolean = gender.equals("Male") ? true : false;
			double salaryDouble = Double.parseDouble(salary);
			Employee newEmployee = new Employee(employee.getEmployeeID() ,fullName, genderBoolean, dateOfBirthLocalDate, email, phoneNumber, role, startDateLocalDate, salaryDouble);
			employeeDAO.updateEmployee(newEmployee);
			accountDAO.updateAccount(employee.getEmployeeID(), username, password);
			// refresh the table
			JOptionPane.showMessageDialog(this, "Employee has been added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
			this.dispose();
			formStaffManagement.handleSearch();
		}
	}
}
