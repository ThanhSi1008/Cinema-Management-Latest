package gui.application.form.other;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private JButton[] deleteButton;
	private JButton[] updateButton;
	private JButton[] Button;
	private JPanel[] productPanelList;

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

		deleteButton = new JButton[12];
		updateButton = new JButton[12];
		Button = new JButton[12];

		// Product list panel
		JPanel container = new JPanel(new MigLayout("wrap 3, fillx, insets 20 70 20 70, gap 90"));
		container.setBorder(BorderFactory.createLineBorder(Color.red));

		productPanelList = new JPanel[12];

		// Add 12 sample products to the product list
		for (int i = 0; i < 11; i++) {
			final int index = i;
			deleteButton[index] = new JButton("Delete");
			updateButton[index] = new JButton("Update");
			Button[index] = new JButton("IDK");
			productPanelList[index] = new JPanel(new MigLayout("wrap 3, fill"));
			productPanelList[index].setPreferredSize(new Dimension(250, 500));
			productPanelList[index].setBorder(BorderFactory.createLineBorder(Color.RED));

			JLabel productLabel = new JLabel("Product: " + index, SwingConstants.CENTER);
			productLabel.setPreferredSize(new Dimension(250, 350));
			productPanelList[index].add(productLabel, "grow, span 3");
			productPanelList[index].add(deleteButton[index]);
			productPanelList[index].add(updateButton[index]);
			productPanelList[index].add(Button[index]);

			container.add(productPanelList[index], "growx");

			deleteButton[index].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("event of delete button " + index);
					productLabel.setText("control by delete button " + index);
					productPanelList[index].add(new JLabel("control by delete button " + index));
				}
			});

			updateButton[index].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("event of update button " + index);
					productLabel.setText("control by update button " + index);
					productPanelList[index].add(new JLabel("control by update button " + index));
				}
			});

			Button[index].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("event of IDk button " + index);
					productLabel.setText("control by IDK button " + index);
					productPanelList[index].add(new JLabel("control by IDK button " + index));
				}
			});
		}

		add(new JScrollPane(container), "span 2, grow");
	}
}
