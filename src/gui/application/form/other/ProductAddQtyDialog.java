package gui.application.form.other;

import java.awt.Color;
import java.awt.Font;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import dao.ProductDAO;
import entity.Product;
import net.miginfocom.swing.MigLayout;
import raven.crazypanel.CrazyPanel;
import raven.toast.Notifications;
import raven.toast.Notifications.Location;

public class ProductAddQtyDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private FormFoodManagement formFoodManagement;
	private JLabel qtyLabel;
	private JTextField qtyTextField;
	private JLabel errorQtyMessageLabel;
	private CrazyPanel container;
	private JButton saveButton;
	private ProductDAO productDAO;
	private FormDrinkManagement formDrinkManagement;

	public ProductAddQtyDialog(Product product) {
		productDAO = new ProductDAO();
		init(product);
	}

	private void init(Product product) {
		this.setTitle("Update Product");
		container = new CrazyPanel();
		JLabel title = new JLabel("Add Quantity Product");
		qtyLabel = new JLabel("Quantity: ");
		qtyTextField = new JTextField(15);
		errorQtyMessageLabel = new JLabel("");
		saveButton = new JButton("Save");

		title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 24));
		errorQtyMessageLabel.setForeground(Color.RED);
		errorQtyMessageLabel.setFont(errorQtyMessageLabel.getFont().deriveFont(Font.ITALIC));

		container.setLayout(new MigLayout("wrap 2,fillx,insets 8, gap 8", "[grow 0,trail]15[fill]"));

		container.add(title, "wrap, span, al center, gapbottom 8");
		container.add(qtyLabel);
		container.add(qtyTextField);
		container.add(new JLabel());
		container.add(errorQtyMessageLabel);
		container.add(saveButton, "span 2, al trail");

		add(container);
		pack();
		setLocationRelativeTo(null);

		saveButton.addActionListener(e -> {
			String qty = qtyTextField.getText().trim();
			if (qty.equals("")) {
				errorQtyMessageLabel.setText("Quantity must not be empty");
				qtyTextField.requestFocus();
				return;
			}
			errorQtyMessageLabel.setText("");

			if (!qty.matches("^\\d+$")) {
				errorQtyMessageLabel.setText("Quantity must be number");
				qtyTextField.requestFocus();
				return;
			}
			errorQtyMessageLabel.setText("");

			if (productDAO.addQuantity(product.getProductID(), Integer.parseInt(qty))) {
				Notifications.getInstance().show(Notifications.Type.INFO, Location.BOTTOM_LEFT,
						"Add Quantity successfully");
				Product newProduct = product;
				newProduct.setQuantity(product.getQuantity() + Integer.valueOf(qty));
				if (newProduct.getProductType().equals("Food")) {
					formFoodManagement.getFoodList().sort(Comparator.comparing(Product::getProductID));
					formFoodManagement.search();
				}
				if (newProduct.getProductType().equals("Drink")) {
					formDrinkManagement.getDrinkList().sort(Comparator.comparing(Product::getProductID));
					formDrinkManagement.search();
				}
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Cannot Add Quantity Product!", "Failed",
						JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	public void setFormProductManagement(FormFoodManagement formFoodManagement) {
		this.formFoodManagement = formFoodManagement;
	}

	public void setFormProductManagement(FormDrinkManagement formDrinkManagement) {
		this.formDrinkManagement = formDrinkManagement;

	}

}
