package gui.application.form.other.profile;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dao.AccountDAO;
import entity.Employee;
import gui.application.Application;
import gui.other.CtrlLoginForm;
import net.miginfocom.swing.MigLayout;
import raven.crazypanel.CrazyPanel;
import raven.toast.Notifications;
import raven.toast.Notifications.Location;

public class FormChangePassword extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel currentPasswordLabel;
	private JLabel newPasswordLabel;
	private JLabel confirmNewPasswordLabel;
	private JTextField currentPasswordTextField;
	private JTextField newPasswordTextField;
	private JTextField confirmNewPasswordTextField;
	private CrazyPanel container;
	private JLabel imageSourceDisplay;
	private JButton changePasswordButton;
	private JLabel imforamtionLabel;
	private JLabel errorLabel;
	private AccountDAO accountDAO;
	private CtrlLoginForm control;

	public FormChangePassword(Employee employee) {
		control = new CtrlLoginForm();
		initComponents(employee);
		accountDAO = new AccountDAO();
	}

	private void initComponents(Employee employee) {
		container = new CrazyPanel();
		currentPasswordLabel = new JLabel("Current Password:");
		newPasswordLabel = new JLabel("New Password:");
		confirmNewPasswordLabel = new JLabel("Confirm New Password:");
		currentPasswordTextField = new JPasswordField();
		newPasswordTextField = new JPasswordField();
		confirmNewPasswordTextField = new JPasswordField(55);
		changePasswordButton = new JButton("Change Password");
		imforamtionLabel = new JLabel(employee.getFullName() + " - " + employee.getRole());
		errorLabel = new JLabel();

		File file = new File(employee.getImageSource());
		boolean isValid = file.exists();
		imageSourceDisplay = new JLabel();

		if (!isValid || employee.getImageSource().trim().isEmpty()) {
			Image defaultImageIcon = new ImageIcon("images/default.png").getImage();
			imageSourceDisplay.setIcon(new ImageIcon(defaultImageIcon.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
		} else {
			Image imageIcon = new ImageIcon(employee.getImageSource()).getImage();
			imageSourceDisplay.setIcon(new ImageIcon(imageIcon.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
		}

		// Title label
		JLabel titleLabel = new JLabel("Change Password");
		titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 25));
		imforamtionLabel.setFont(new Font(imforamtionLabel.getFont().getName(), Font.ITALIC, 20));
		errorLabel.setBorder(BorderFactory.createEmptyBorder());
		errorLabel.setForeground(Color.RED);
		errorLabel.setFont(errorLabel.getFont().deriveFont(Font.ITALIC));

		container.setLayout(new MigLayout("wrap 2, fillx, insets 8 50 8 50, gap 20", "[grow 0,trail]15[fill]"));

		container.add(titleLabel, "wrap, span, al left, gapbottom 8");
		container.add(imforamtionLabel, "span 2, al left");
		container.add(imageSourceDisplay, "wrap, span, al center, gapbottom 8");
		container.add(currentPasswordLabel, "skip 6");
		container.add(currentPasswordTextField);
		container.add(newPasswordLabel);
		container.add(newPasswordTextField);
		container.add(confirmNewPasswordLabel);
		container.add(confirmNewPasswordTextField);
		container.add(new JLabel(""));
		container.add(errorLabel, "al left");
		container.add(changePasswordButton, "span 2, al right");

		add(container);

		changePasswordButton.addActionListener(e -> {
			String currentPassword = currentPasswordTextField.getText();
			String newPassword = newPasswordTextField.getText();
			String confirmNewPassword = confirmNewPasswordTextField.getText();

			if (currentPassword.isEmpty()) {
				errorLabel.setText("Please enter your current password.");
				currentPasswordTextField.requestFocus();
				return;
			}

			if (newPassword.isEmpty()) {
				errorLabel.setText("Please enter a new password.");
				newPasswordTextField.requestFocus();
				return;
			}

			if (confirmNewPassword.isEmpty()) {
				errorLabel.setText("Please confirm your new password.");
				confirmNewPasswordTextField.requestFocus();
				return;
			}

			if (!newPassword.equals(confirmNewPassword)) {
				errorLabel.setText("New password and confirm password do not match.");
				confirmNewPasswordTextField.requestFocus();
				return;
			}
			String username = accountDAO.getUserByEmployeeID(employee.getEmployeeID());
			if (!control.checkCredentials(username, currentPassword)) {
				errorLabel.setText("Failed to update password. Please check your current password.");
				currentPasswordTextField.requestFocus();
				return;
			}

			if (accountDAO.updatePassword(employee.getEmployeeID(), newPassword)) {
				errorLabel.setText("");
				Notifications.getInstance().show(Notifications.Type.INFO, Location.TOP_CENTER,
						"Password updated successfully.");
				Application.logout();
				currentPasswordTextField.setText("");
				newPasswordTextField.setText("");
				confirmNewPasswordTextField.setText("");
			} else {
				errorLabel.setText("Failed to update password. Please check your current password.");
				currentPasswordTextField.requestFocus();
			}
		});

		KeyListener enterKeyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					changePasswordButton.doClick();
				}
			}
		};

		currentPasswordTextField.addKeyListener(enterKeyListener);
		newPasswordTextField.addKeyListener(enterKeyListener);
		confirmNewPasswordTextField.addKeyListener(enterKeyListener);

	}

}
