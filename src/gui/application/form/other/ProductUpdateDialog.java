package gui.application.form.other;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

import dao.ProductDAO;
import entity.Product;
import net.miginfocom.swing.MigLayout;
import raven.crazypanel.CrazyPanel;
import raven.toast.Notifications;
import raven.toast.Notifications.Location;

public class ProductUpdateDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FormFoodManagement formFoodManagement;
	private ProductDAO productDAO;
	private CrazyPanel container;
	private JLabel imageSourceLabel;
	private JButton imageSourceButton;
	private JLabel displaypPosterLabel;
	private JButton updateButton;
	private JLabel errorNameMessageLabel;
	private JLabel errorPriceMessageLabel;
	private JLabel errorQtyMessageLabel;
	private JLabel errorImageMessageLabel;
	private JLabel productNameLabel;
	private JTextField productNameTextField;
	private JLabel priceLabel;
	private JTextField priceTextField;
	private JLabel qtyLabel;
	private JTextField qtyTextField;
	private final FileNameExtensionFilter filter;
	private File selectedFile;
	private File file;
	private FormDrinkManagement formDrinkManagement;

	public ProductUpdateDialog(Product product) {
		productDAO = new ProductDAO();
		filter = new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp");
//		setLayout(new BorderLayout());
		initComponents(product);
	}

	private void initComponents(Product product) {
		this.setTitle("Update Product");
		container = new CrazyPanel();
		JLabel title = new JLabel("UPDATE PRODUCT");
		productNameLabel = new JLabel("Name: ");
		productNameTextField = new JTextField(15);
		errorNameMessageLabel = new JLabel("");
		priceLabel = new JLabel("Purchase Price: ");
		priceTextField = new JTextField(15);
		errorPriceMessageLabel = new JLabel("");
		qtyLabel = new JLabel("Quantity: ");
		qtyTextField = new JTextField(15);
		errorQtyMessageLabel = new JLabel("");
		imageSourceLabel = new JLabel("Image: ");
		imageSourceButton = new JButton("Choose Image");
		displaypPosterLabel = new JLabel();
		errorImageMessageLabel = new JLabel("");
		updateButton = new JButton("Update");

		title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 24));
		errorNameMessageLabel.setForeground(Color.RED);
		errorNameMessageLabel.setFont(errorNameMessageLabel.getFont().deriveFont(Font.ITALIC));
		errorPriceMessageLabel.setForeground(Color.RED);
		errorPriceMessageLabel.setFont(errorPriceMessageLabel.getFont().deriveFont(Font.ITALIC));
		errorQtyMessageLabel.setForeground(Color.RED);
		errorQtyMessageLabel.setFont(errorQtyMessageLabel.getFont().deriveFont(Font.ITALIC));
		errorImageMessageLabel.setForeground(Color.RED);
		errorImageMessageLabel.setFont(errorImageMessageLabel.getFont().deriveFont(Font.ITALIC));

		container.setLayout(new MigLayout("wrap 2,fillx,insets 8, gap 8", "[grow 0,trail]15[fill]"));

		container.add(title, "wrap, span, al center, gapbottom 8");
		container.add(displaypPosterLabel, "wrap, span, al center, gapbottom 8");
		container.add(imageSourceLabel);
		container.add(imageSourceButton, "grow 0, split 2");
		container.add(new JLabel());
		container.add(new JLabel());
		container.add(errorImageMessageLabel);
		container.add(productNameLabel);
		container.add(productNameTextField);
		container.add(new JLabel());
		container.add(errorNameMessageLabel);
		container.add(priceLabel);
		container.add(priceTextField);
		container.add(new JLabel());
		container.add(errorPriceMessageLabel);
		container.add(qtyLabel);
		container.add(qtyTextField);
		container.add(new JLabel());
		container.add(errorQtyMessageLabel);
		container.add(updateButton, "span 2, al trail");

		add(container);
		setSize(700, 700);
		setLocationRelativeTo(null);

		productNameTextField.setText(product.getProductName());
		priceTextField.setText(String.valueOf(product.getPurchasePrice()));
		qtyTextField.setText(String.valueOf(product.getQuantity()));
		qtyTextField.setEditable(false);

		boolean isValid = false;
		String path = product.getImageSource();
		System.out.println("image path is: " + path);
		if (path != null && !path.trim().isEmpty()) {
			file = new File(path);
			isValid = file.exists();
		}

		if (!isValid) {
			displaypPosterLabel.setIcon(null);
		} else {
			Image imageIcon = new ImageIcon(path).getImage();
			displaypPosterLabel.setIcon(new ImageIcon(imageIcon.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
			selectedFile = file;
		}

		imageSourceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Choose Image File");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);

				fileChooser.addChoosableFileFilter(filter);

				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					selectedFile = fileChooser.getSelectedFile();
					Image icon = new ImageIcon(String.format("images/%s", selectedFile.getName())).getImage();
					displaypPosterLabel.setIcon(new ImageIcon(icon.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
				}
			}
		});

		updateButton.addActionListener(e -> {

		});

		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// get all values from the input fields
				String name = productNameTextField.getText().trim();
				String purchasePrice = priceTextField.getText().trim();
				String qty = qtyTextField.getText().trim();
				String imagePath = "";
				Icon icon = displaypPosterLabel.getIcon();
				if (icon == null) {
					System.out.println("icon is null");
				} else {
					System.out.println("icon is not null");
				}

				if (name.equals("")) {
					errorNameMessageLabel.setText("Name must not be empty");
					productNameTextField.requestFocus();
					return;
				}
				errorNameMessageLabel.setText("");

				if (!name.matches("^[A-Z][a-zA-Z]*(\\s[A-Z][a-zA-Z]*)*$")) {
					errorNameMessageLabel.setText("Name must start with capital letters");
					productNameTextField.requestFocus();
					return;
				}
				errorNameMessageLabel.setText("");

				if (purchasePrice.equals("")) {
					errorPriceMessageLabel.setText("Purchase price must not be empty");
					priceTextField.requestFocus();
					return;
				}
				errorPriceMessageLabel.setText("");

				if (!purchasePrice.matches("^[0-9]+(\\.[0-9]+)?$")) {
					errorPriceMessageLabel.setText("Purchase price must be a number");
					priceTextField.requestFocus();
					return;
				}
				errorPriceMessageLabel.setText("");

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

				if (icon == null) {
					errorImageMessageLabel.setText("Product image is required");
					imageSourceButton.requestFocus();
					return;
				}
				errorImageMessageLabel.setText("");

				try {
					BufferedImage image = ImageIO.read(selectedFile);
					imagePath = String.format("images/%s", selectedFile.getName());
					File destinationFile = new File(imagePath);
					String extension = FilenameUtils.getExtension(selectedFile.getName()).toLowerCase();
					boolean isValidExtension = Arrays.asList(filter.getExtensions()).contains(extension);
					if (isValidExtension) {
						try {
							ImageIO.write(image, extension, destinationFile);
							System.out.println("File has been written successfully with path: " + imagePath);
						} catch (IOException exc) {
							exc.printStackTrace();
						}
					} else {
						System.out.println("Invalid file extension!");
					}
				} catch (IOException ex) {
					ex.printStackTrace();
					System.out.println("Error saving image: " + ex.getMessage());
					return;
				}

				// create a new movie object
				Product newProduct = new Product(product.getProductID(), name, Integer.parseInt(qty),
						Double.parseDouble(purchasePrice), imagePath);
				
				// UPDATE a record in the database
				if (productDAO.updateNewProduct(newProduct)) {
					Notifications.getInstance().show(Notifications.Type.INFO, Location.BOTTOM_LEFT,
							"Update successfully");
					newProduct.setPrice();
					newProduct.setProductType(product.getProductType());
					System.out.println(newProduct);

					if (newProduct.getProductType().equals("Food")) {
						formFoodManagement.getFoodList().remove(product);
						formFoodManagement.getFoodList().add(newProduct);
						formFoodManagement.getFoodList().sort(Comparator.comparing(Product::getProductID));
						formFoodManagement.search();
					}
					if (newProduct.getProductType().equals("Drink")) {
						formDrinkManagement.getDrinkList().remove(product);
						formDrinkManagement.getDrinkList().add(newProduct);
						formDrinkManagement.getDrinkList().sort(Comparator.comparing(Product::getProductID));
						formDrinkManagement.search();
					}
					dispose();

				} else {
					JOptionPane.showMessageDialog(null, "Cannot Update Product!", "Failed", JOptionPane.ERROR_MESSAGE);
				}

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
