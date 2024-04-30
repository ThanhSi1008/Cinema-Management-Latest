package gui.application.form.other;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entity.Employee;
import net.miginfocom.swing.MigLayout;
import raven.crazypanel.CrazyPanel;

public class FormProfileInfo extends JPanel {

	private static final long serialVersionUID = 1L;
	private CrazyPanel container;
	private JLabel employeeIDLabel;
	private JTextField employeeIDTextField;
	private JLabel fullNameLabel;
	private JTextField fullNameTextField;
	private JLabel genderLabel;
	private JTextField genderTextField;
	private JLabel dateOfBirthLabel;
	private JTextField dateOfBirthTextField;
	private JLabel emailLabel;
	private JTextField emailTextField;
	private JLabel phoneNumberLabel;
	private JTextField phoneNumberTextField;
	private JLabel roleLabel;
	private JTextField roleTextField;
	private JLabel startingDateLabel;
	private JTextField startingDateTextField;
	private JLabel salaryLabel;
	private JTextField salaryTextField;
	private JLabel imageSourceDisplay;
	private JLabel title;

	public FormProfileInfo(Employee employee) {
		setLayout(new BorderLayout());
		initComponents(employee);
	}

	private void initComponents(Employee employee) {
		String path = employee.getImageSource();

		container = new CrazyPanel();
		title = new JLabel("Personal Information");
		employeeIDTextField = new JTextField(40);
		fullNameTextField = new JTextField(15);
		genderTextField = new JTextField(15);
		dateOfBirthTextField = new JTextField(15);
		emailTextField = new JTextField(20);
		phoneNumberTextField = new JTextField(20);
		roleTextField = new JTextField(20);
		startingDateTextField = new JTextField();
		salaryTextField = new JTextField();

		if (path == null) {
			System.out.println("Image is null");
			ImageIcon images = new ImageIcon(
					new ImageIcon("images/default.png").getImage().getScaledInstance(150, 150, 4));
			imageSourceDisplay = new JLabel(images);
		} else {
			ImageIcon images = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(150, 150, 4));
			imageSourceDisplay = new JLabel(images);
		}

		employeeIDTextField.setText(employee.getEmployeeID());
		fullNameTextField.setText(employee.getFullName());
		genderTextField.setText(employee.isGender() ? "Male" : "FeMale");
//		dateOfBirthTextField.setText(employee.getDateOfBirth().toString());
		emailTextField.setText(employee.getEmail());
		phoneNumberTextField.setText(employee.getPhoneNumber());
		roleTextField.setText(employee.getRole());
//		startingDateTextField.setText(employee.getStartingDate().toString());
		salaryTextField.setText(Double.toString(employee.getSalary()));

		employeeIDLabel = new JLabel("EmployeeID: ");
		fullNameLabel = new JLabel("Full Name: ");
		genderLabel = new JLabel("Gender: ");
		dateOfBirthLabel = new JLabel("Date of Birth: ");
		emailLabel = new JLabel("Email: ");
		phoneNumberLabel = new JLabel("Phone Number: ");
		roleLabel = new JLabel("Role: ");
		startingDateLabel = new JLabel("Starting Date: ");
		salaryLabel = new JLabel("Salary: ");

		title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 24));

		container.setLayout(new MigLayout("wrap 2, fillx, insets 8 50 8 50, gap 20", "[grow 0,trail]15[fill]"));

		container.add(title, "wrap, span, al left, gapbottom 8");
		container.add(imageSourceDisplay, "wrap, span, al center, gapbottom 8");
		container.add(employeeIDLabel);
		container.add(employeeIDTextField);
		container.add(fullNameLabel);
		container.add(fullNameTextField);
		container.add(genderLabel);
		container.add(genderTextField);
		container.add(dateOfBirthLabel);
		container.add(dateOfBirthTextField);
		container.add(emailLabel);
		container.add(emailTextField);
		container.add(phoneNumberLabel);
		container.add(phoneNumberTextField);
		container.add(roleLabel);
		container.add(roleTextField);
		container.add(startingDateLabel);
		container.add(startingDateTextField);
		container.add(salaryLabel);
		container.add(salaryTextField);

		add(container);
	}

}
