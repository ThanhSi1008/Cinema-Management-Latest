package gui.application.form.other;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.ProductDAO;
import entity.Product;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;
import raven.toast.Notifications.Location;

public class FormFoodManagement extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField searchTextField;
	private JButton addButton;
	private JButton[] deleteButton;
	private JButton[] updateButton;
	private JButton[] addQuantityButton;
	private JPanel[] productPanelList;
	private JPanel container;
	private List<Product> foodList;
	private ProductDAO foodDAO;
	private JScrollPane scroll;
	private ProductAddingDialog productAddingDialog;
	private ProductUpdateDialog productUpdateDialog;
	private ProductAddQtyDialog productAddQtyDialog;

	public FormFoodManagement() {
		foodList = new ArrayList<>();
		foodDAO = new ProductDAO();
		foodList = foodDAO.getAllProductByType("Food");
		init();
	}

	private void init() {

		foodList.forEach(food -> {
			System.out.println(food);
		});

		setLayout(new MigLayout("fill, wrap 2"));

		JPanel topPanel = new JPanel(new MigLayout("fill, wrap 2"));

		add(topPanel, "span 2, al trail");

		// Search text field
		searchTextField = new JTextField(50);
		searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON,
				new FlatSVGIcon("gui/icon/svg/search.svg", 0.35f));
		topPanel.add(searchTextField);

		// Add button
		addButton = new JButton("Add Product");
		addButton.setIcon(new FlatSVGIcon("gui/icon/svg/add.svg", 0.35f));
		topPanel.add(addButton);

		deleteButton = new JButton[99];
		updateButton = new JButton[99];
		addQuantityButton = new JButton[99];
		productPanelList = new JPanel[99];

		container = initPanel(foodList);
		scroll = new JScrollPane(container);
		add(scroll, "span 2, grow, al left");

		searchTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				search();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				search();

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				search();

			}

		});

		addButton.addActionListener(e -> {
			Thread thread = new Thread(() -> {
				productAddingDialog = new ProductAddingDialog("Food");
				productAddingDialog.setFormProductManagement(this);
				productAddingDialog.setModal(true);
				productAddingDialog.setVisible(true);
			});
			thread.start();
		});
	}

	public void search() {
		List<Product> foodListSearch = new ArrayList<Product>();
		foodListSearch = foodDAO.findFoodByName(searchTextField.getText().trim());
		if (foodListSearch.size() == 0) {
			remove(scroll);
			revalidate();
			repaint();

			container = initPanelEmpty();
			scroll = new JScrollPane(container);
			add(scroll, "span 2, grow");
		} else {
			if (!searchTextField.getText().trim().equalsIgnoreCase("")) {
				remove(scroll);
				revalidate();
				repaint();

				container = initPanel(foodListSearch);
				scroll = new JScrollPane(container);
				add(scroll, "span 2, grow");
			} else {
				remove(scroll);
				revalidate();
				repaint();

				container = initPanel(foodList);
				scroll = new JScrollPane(container);
				add(scroll, "span 2, grow, al left");
			}
		}
	}

	public List<Product> getFoodList() {
		return foodList;
	}

	public void setFoodList(List<Product> foodList) {
		this.foodList = foodList;
	}

	private JPanel initPanel(List<Product> foodList) {
		// Product list panel
		JPanel container = new JPanel(new MigLayout("wrap 3, insets 20 50 20 50, gap 20"));

		int i = 0;
		for (Product product : foodList) {
			final int index = i;
			deleteButton[index] = new JButton("Delete");
			updateButton[index] = new JButton("Update");
			addQuantityButton[index] = new JButton("Add");
			deleteButton[index].setIcon(new FlatSVGIcon("gui/icon/svg/delete.svg", 0.35f));
			updateButton[index].setIcon(new FlatSVGIcon("gui/icon/svg/edit.svg", 0.35f));
			addQuantityButton[index].setIcon(new FlatSVGIcon("gui/icon/svg/add.svg", 0.35f));
			productPanelList[index] = new JPanel(new MigLayout("wrap 6, fill"));
			productPanelList[index].setPreferredSize(new Dimension(250, 500));
			productPanelList[index].setBorder(BorderFactory.createLineBorder(Color.BLACK));

			JLabel productPosterLabel = new JLabel("", SwingConstants.CENTER);
			JLabel productNameLabel = new JLabel();
			JLabel productQuantityLabel = new JLabel("Quantity: " + product.getQuantity());
			JLabel productPriceLabel = new JLabel("Unit Price: " + product.getPrice());

			boolean isValid = false;
			String path = product.getImageSource();
			if (path != null && !path.trim().isEmpty()) {
				File file = new File(path);
				isValid = file.exists();
			}

			if (!isValid) {
				productPosterLabel.setIcon(null);
			} else {
				Image imageIcon = new ImageIcon(path).getImage();
				productPosterLabel.setIcon(new ImageIcon(imageIcon.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
			}
			productQuantityLabel.setHorizontalAlignment(SwingConstants.LEFT);
			productPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			productNameLabel.setText(product.getProductName());
			productNameLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			productNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			productPosterLabel.setPreferredSize(new Dimension(250, 350));
			productPanelList[index].add(productPosterLabel, "grow, span 6");
			productPanelList[index].add(productNameLabel, "grow, span 6");
			productPanelList[index].add(productQuantityLabel, "grow, span 3");
			productPanelList[index].add(productPriceLabel, "grow, span 3");
			productPanelList[index].add(deleteButton[index], "span 2");
			productPanelList[index].add(updateButton[index], "span 2");
			productPanelList[index].add(addQuantityButton[index], "span 2");

			container.add(productPanelList[index], "growx");

			deleteButton[index].addActionListener(e -> {
				int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this prduct?",
						"Warning", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					String productID = product.getProductID();
					if (foodDAO.removeProductByID(productID)) {
						Notifications.getInstance().show(Notifications.Type.INFO, Location.BOTTOM_LEFT,
								"Delete successfully");
						foodList.remove(product);
						search();
					} else {
						JOptionPane.showMessageDialog(null, "Cannot delete product", "Failed",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			updateButton[index].addActionListener(e -> {
				Thread thread = new Thread(() -> {
					productUpdateDialog = new ProductUpdateDialog(product);
					productUpdateDialog.setFormProductManagement(this);
					productUpdateDialog.setModal(true);
					productUpdateDialog.setVisible(true);
				});
				thread.start();
			});

			addQuantityButton[index].addActionListener(e -> {
				Thread thread = new Thread(() -> {
					productAddQtyDialog = new ProductAddQtyDialog(product);
					productAddQtyDialog.setFormProductManagement(this);
					productAddQtyDialog.setModal(true);
					productAddQtyDialog.setVisible(true);
				});
				thread.start();
			});
			i++;
		}
		return container;

	}

	private JPanel initPanelEmpty() {
		JPanel container = new JPanel(new MigLayout("wrap 3, insets 20 50 20 50, gap 20"));
		int index = 0;
		productPanelList[index] = new JPanel(new MigLayout("wrap 6, fill"));
		productPanelList[index].setPreferredSize(new Dimension(250, 500));
		productPanelList[index].setBorder(BorderFactory.createEmptyBorder());
		JLabel productPosterLabel = new JLabel("", SwingConstants.CENTER);
		JLabel productNameLabel = new JLabel();
		JLabel productQuantityLabel = new JLabel("");
		JLabel productPriceLabel = new JLabel("");
		productPosterLabel.setIcon(null);
		productQuantityLabel.setHorizontalAlignment(SwingConstants.LEFT);
		productPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		productNameLabel.setText("");
		productNameLabel.setBorder(BorderFactory.createEmptyBorder());
		productNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		productPosterLabel.setPreferredSize(new Dimension(250, 350));
		productPanelList[index].add(productPosterLabel, "grow, span 6");
		productPanelList[index].add(productNameLabel, "grow, span 6");
		productPanelList[index].add(productQuantityLabel, "grow, span 3");
		productPanelList[index].add(productPriceLabel, "grow, span 3");
		container.add(productPanelList[index], "growx");
		return container;
	}
}
