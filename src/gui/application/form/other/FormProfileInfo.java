package gui.application.form.other;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

import dao.EmployeeDAO;
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
	private JLabel imageSourceDisplay = new JLabel();
	private JLabel title;
	private JButton changeAvatarButton;
	private JFileChooser fileChooser;
	private FileNameExtensionFilter filter;
	private File selectedFile;
	private String imagePath;
	private EmployeeDAO employeeDAO;

	public FormProfileInfo(Employee employee) {
		setLayout(new BorderLayout());
		employeeDAO = new EmployeeDAO();
		fileChooser = new JFileChooser();
		initComponents(employee);
	}

	private void initComponents(Employee employee) {
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

		boolean isValid = false;
		if (employee.getImageSource() != null) {
			File file = new File(employee.getImageSource());
			isValid = file.exists();
		}

		if (!isValid || employee.getImageSource().trim().isEmpty()) {
			Image defaultImageIcon = new ImageIcon("images/default.png").getImage();
			imageSourceDisplay.setIcon(new ImageIcon(defaultImageIcon.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
		} else {
			Image imageIcon = new ImageIcon(employee.getImageSource()).getImage();
			imageSourceDisplay.setIcon(new ImageIcon(imageIcon.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
		}

		employeeIDTextField.setText(employee.getEmployeeID());
		fullNameTextField.setText(employee.getFullName());
		genderTextField.setText(employee.isGender() ? "Male" : "FeMale");
		dateOfBirthTextField.setText(employee.getDateOfBirth().toString());
		emailTextField.setText(employee.getEmail());
		phoneNumberTextField.setText(employee.getPhoneNumber());
		roleTextField.setText(employee.getRole());
		startingDateTextField.setText(employee.getStartingDate().toString());
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
		changeAvatarButton = new JButton("Change Avatar");

		title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 24));

		container.setLayout(new MigLayout("wrap 2, fillx, insets 5 50 5 50, gap 20", "[grow 0,trail]15[fill]"));

		container.add(title, "wrap, span, al left, gapbottom 8");
		container.add(imageSourceDisplay, "wrap, span, al center, gapbottom 4");
		container.add(changeAvatarButton, "span 2, align center");
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

		employeeIDTextField.setEditable(false);
		fullNameTextField.setEditable(false);
		genderTextField.setEditable(false);
		dateOfBirthTextField.setEditable(false);
		emailTextField.setEditable(false);
		phoneNumberTextField.setEditable(false);
		roleTextField.setEditable(false);
		startingDateTextField.setEditable(false);
		salaryTextField.setEditable(false);

		changeAvatarButton.addActionListener(e -> {
			fileChooser.setDialogTitle("Choose Image File");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setAcceptAllFileFilterUsed(false);

			filter = new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp");
			fileChooser.addChoosableFileFilter(filter);

			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();

				imagePath = saveImage(selectedFile, employee);

				displayImage(imagePath);
			}
		});

	}

	private String saveImage(File imageFile, Employee employee) {
		try {
			BufferedImage image = ImageIO.read(imageFile);
			String imagePath = String.format("images/%s", imageFile.getName());
			File destinationFile = new File(imagePath);
			String extension = FilenameUtils.getExtension(imageFile.getName()).toLowerCase();
			boolean isValidExtension = Arrays.asList("jpg", "png", "gif", "bmp").contains(extension);
			if (isValidExtension) {
				ImageIO.write(image, extension, destinationFile);
				System.out.println("File has been written successfully with path: " + imagePath);
				updateImagePathInDatabase(imagePath, employee);
				return imagePath;
			} else {
				System.out.println("Invalid file extension!");
				return null;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("Error saving image: " + ex.getMessage());
			return null;
		}
	}

	private void updateImagePathInDatabase(String imagePath, Employee employee) {
		if (employeeDAO.updateAvatar(imagePath, employee.getEmployeeID())) {
			employee.setImageSource(imagePath);
		}
	}

	private void displayImage(String imagePath) {
		if (imagePath != null) {
			ImageIcon newImageIcon = new ImageIcon(imagePath);
			Image originalImage = newImageIcon.getImage();
			Image scaledImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
			ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
			imageSourceDisplay.setIcon(scaledImageIcon);
			System.out.println(imagePath);
		}
	}

}
