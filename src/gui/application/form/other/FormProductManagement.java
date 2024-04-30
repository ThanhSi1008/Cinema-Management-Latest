package gui.application.form.other;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import net.miginfocom.swing.MigLayout;

public class FormProductManagement extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField searchTextField;
	private JButton addButton;

	public FormProductManagement() {
		init();
	}

	private void init() {
		setLayout(new MigLayout("fill, wrap 2"));

		// Search text field
		searchTextField = new JTextField(50);
		searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON,
				new FlatSVGIcon("gui/icon/svg/search.svg", 0.35f));
		add(searchTextField, "alignx right");

		// Add button
		addButton = new JButton("Add");
		addButton.setIcon(new FlatSVGIcon("gui/icon/svg/add.svg", 0.35f));
		add(addButton, "alignx center");

		// Product list panel
		JPanel productListPanel = new JPanel(new MigLayout("wrap 3, fillx, insets 20 70 20 70, gap 90"));
		productListPanel.setBorder(BorderFactory.createLineBorder(Color.red));

		// Add 12 sample products to the product list
		for (int i = 1; i <= 12; i++) {
			JPanel productPanel = new JPanel(new MigLayout("fill"));
			productPanel.setPreferredSize(new Dimension(250, 400));
			productPanel.setBorder(BorderFactory.createLineBorder(Color.RED));

			JLabel productNameLabel = new JLabel("Product: " + i, SwingConstants.CENTER);
			productPanel.add(productNameLabel, "grow");

			productListPanel.add(productPanel, "growx"); // Use growx to expand productPanel horizontally
		}

		add(new JScrollPane(productListPanel), "span 2, grow");
	}
}
