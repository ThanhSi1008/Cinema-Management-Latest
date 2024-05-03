package gui.application.form.other;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FormScreeningManagement extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton btnAddMovie;
	private JComboBox<String> comboBoxFilter;
	private JTable tableMovies;

	public FormScreeningManagement() {
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		// Create a panel for top section
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

		// Add title label
		JLabel lblTitle = new JLabel("SCREENING MANAGEMENT");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
		lblTitle.setForeground(Color.LIGHT_GRAY);
		topPanel.add(lblTitle);
		topPanel.add(Box.createVerticalStrut(20)); // Add vertical space

		// Add search components
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		btnAddMovie = new JButton("Add Screening");
		comboBoxFilter = new JComboBox<>();
		comboBoxFilter.addItem("Room 1");
		comboBoxFilter.addItem("Room 2");
		comboBoxFilter.addItem("Room 3");
		searchPanel.add(btnAddMovie);
		searchPanel.add(comboBoxFilter);
		topPanel.add(searchPanel);
		topPanel.add(Box.createVerticalStrut(10)); // Add vertical space

		// Add top panel to the main panel
		add(topPanel, BorderLayout.NORTH);

		// Create table for movie display
		String[] columnHeaders = { "Name", "Screening Time", "End Time" };
		DefaultTableModel tableModel = new DefaultTableModel(columnHeaders, 0);
		tableMovies = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(tableMovies);
		add(scrollPane, BorderLayout.CENTER);
	}
}
