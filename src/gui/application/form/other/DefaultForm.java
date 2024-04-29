package gui.application.form.other;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

public class DefaultForm extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel lb;

	public DefaultForm(String text) {
		initComponents();
		lb.putClientProperty(FlatClientProperties.STYLE, "" + "font:$h1.font");
		lb.setText(text);
	}

	private void initComponents() {

		lb = new JLabel();

		lb.setHorizontalAlignment(SwingConstants.CENTER);
		lb.setText("Form");

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(lb, GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(lb, GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE).addContainerGap()));
	}

}
