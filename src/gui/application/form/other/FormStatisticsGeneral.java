package gui.application.form.other;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import net.miginfocom.swing.MigLayout;

public class FormStatisticsGeneral extends JPanel {

	private JPanel container;
	private JPanel topContainer;
	private JPanel bottomContainer;
	private JPanel comboboxContainer;
	private JPanel dataContainer;
	private JPanel bottomLeftContainer;
	private JPanel bottomRightContainer;
	private JPanel incomeTitleContainer;
	private JPanel bottomLeftMainContainer;
	private JPanel spendingTitleContainer;
	private JPanel bottomRightMainContainer;
	private JPanel topLeftContainer;
	private JComboBox<String> filterByCombobox;
	private JComboBox<String> filterCombobox;
	private JLabel totalIncomeLabel;
	private JLabel totalSpending;
	private JLabel totalIncome;
	private JLabel totalSpendingLabel;
	private JLabel incomeTitleLabel;
	private JLabel spendingTitleLabel;

	public FormStatisticsGeneral() {
		
		setLayout(new BorderLayout());
		
		container = new JPanel(new MigLayout("wrap, fill", "[fill]", "[min!][fill]"));
		topContainer = new JPanel(new MigLayout("wrap, fill", "[fill][fill]", "[fill]"));
		bottomContainer = new JPanel(new MigLayout("wrap, fill", "[fill][fill]", "[fill]"));
		topLeftContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]")); 
		comboboxContainer = new JPanel(new MigLayout("wrap", "[fill][fill]", "[fill]"));
		dataContainer = new JPanel(new MigLayout("wrap", "[fill][fill]", "[][fill]"));
		bottomLeftContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));
		bottomRightContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));
		
		incomeTitleContainer = new JPanel(new MigLayout("wrap, fill", "[center]", "[]"));
		bottomLeftMainContainer= new JPanel(new MigLayout("wrap, fill", "[fill][grow 0]", "[]"));
//		incomePieChartContainer = new JPanel(new MigLayout("wrap, fill", "[center]", "[fill]"));
//		incomeCommentContainer = new JPanel(new MigLayout("wrap, fill", "[center]", "[]"));
//		incomeCommentContainerContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[][]")); 
		
		spendingTitleContainer = new JPanel(new MigLayout("wrap, fill", "[center]", "[]"));
		bottomRightMainContainer= new JPanel(new MigLayout("wrap, fill", "[fill][grow 0]", "[]"));
//		spendingPieChartContainer = new JPanel(new MigLayout("wrap, fill", "[center]", "[fill]"));
//		spendingCommentContainer = new JPanel(new MigLayout("wrap, fill", "[center]", "[]"));
//		spendingCommentContainerContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[][][][][]")); 
		
		container.add(topContainer);
		container.add(bottomContainer);
		topContainer.add(topLeftContainer);
		topLeftContainer.add(comboboxContainer);
		topLeftContainer.add(dataContainer);
		bottomContainer.add(bottomLeftContainer);
		bottomContainer.add(bottomRightContainer);
		bottomLeftContainer.add(incomeTitleContainer);
		bottomLeftContainer.add(bottomLeftMainContainer);
		bottomRightContainer.add(spendingTitleContainer);
		bottomRightContainer.add(bottomRightMainContainer);
		
		filterByCombobox = new JComboBox<String>();
		filterByCombobox.addItem("By month");
		filterByCombobox.addItem("By year");
		filterCombobox = new JComboBox<String>();
		filterCombobox.addItem("January");
		filterByCombobox.addActionListener(e -> {
			String selectedItem = (String) filterByCombobox.getSelectedItem();
			if (selectedItem.equals("By month")) {
				filterCombobox.addItem("January");
				filterCombobox.addItem("Feburary");
				filterCombobox.addItem("March");
				filterCombobox.addItem("April");
				filterCombobox.addItem("May");
				filterCombobox.addItem("June");
				filterCombobox.addItem("July");
				filterCombobox.addItem("August");
				filterCombobox.addItem("September");
				filterCombobox.addItem("October");
				filterCombobox.addItem("November");
				filterCombobox.addItem("December");
			} else {
				filterCombobox.addItem("2023");
				filterCombobox.addItem("2024");
			}
		});
		
		totalIncomeLabel = new JLabel("Total income: ");
		totalSpendingLabel = new JLabel("Total spending: ");
		totalIncome = new JLabel("$7584");
		totalSpending = new JLabel("$12584");
		
		comboboxContainer.add(filterByCombobox);
		comboboxContainer.add(filterCombobox);
		
		dataContainer.add(totalIncomeLabel);
		dataContainer.add(totalSpendingLabel);
		dataContainer.add(totalIncome);
		dataContainer.add(totalSpending);
		
		incomeTitleLabel = new JLabel("Income Pie Chart");
		incomeTitleContainer.add(incomeTitleLabel);		
		
		spendingTitleLabel = new JLabel("Spending Pie Chart");
		spendingTitleContainer.add(spendingTitleLabel);
		
		// style
		totalIncomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalSpendingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalIncome.setHorizontalAlignment(SwingConstants.CENTER);
		totalSpending.setHorizontalAlignment(SwingConstants.CENTER);
		
		totalIncome.setOpaque(true);
		totalSpending.setOpaque(true);
		totalIncome.putClientProperty(FlatClientProperties.STYLE, "font:$h1.font; background:$App.accent.green; border:10,20,10,20");
		totalSpending.putClientProperty(FlatClientProperties.STYLE, "font:$h1.font; background:$App.accent.red; border:10,20,10,20");
		
		bottomLeftContainer.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		bottomRightContainer.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		
		incomeTitleLabel.putClientProperty(FlatClientProperties.STYLE, "font:$h3.font;");
		spendingTitleLabel.putClientProperty(FlatClientProperties.STYLE, "font:$h3.font;");
		
		bottomLeftMainContainer.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		bottomRightMainContainer.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

		// frame
		add(container);
	}

//	public static void main(String args[]) {
//		FlatRobotoFont.install();
//		FlatLaf.registerCustomDefaultsSource("gui.theme");
//		UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
//		FlatMacLightLaf.setup();
//		SwingUtilities.invokeLater(() -> {
//			new FormStatisticsGeneral().setVisible(true);
//		});
//	}
}
