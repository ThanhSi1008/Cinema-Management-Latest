package gui.application.form.other.screening;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.ProductDAO;
import entity.Employee;
import entity.MovieSchedule;
import entity.MovieScheduleSeat;
import entity.OrderDetail;
import entity.Product;
import net.miginfocom.swing.MigLayout;

public class ProductOptionDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel container;
	private JPanel leftContainer;
	private JPanel rightContainer;
	private JPanel categoryContainer;
	private JPanel productContainer;
	private JPanel checkoutContainer;
	private JPanel productChosenContainer;
	private JPanel totalContainer;
	private JPanel continueContainer;
	private ProductDAO productDAO;
	private JButton allButton;
	private JButton foodButton;
	private JButton drinkButton;
	private JLabel checkOutText;
	private JButton removeAllButton;
	private JLabel totalText;
	private JLabel total;
	private JButton continueButton;
	private List<OrderDetail> chosenProductOrderDetailList;
	private boolean exist;
	private CheckoutDialog checkoutDialog;
	private Employee currentEmployee;
	private SeatingOptionDialog seatingOptionDialog;
	private String activeButtonStyle;
	private String normalButtonStyle;

	public ProductOptionDialog(ArrayList<MovieScheduleSeat> seatChosenList, MovieSchedule movieSchedule) {
		chosenProductOrderDetailList = new ArrayList<OrderDetail>();
		this.productDAO = new ProductDAO();
		container = new JPanel();
		leftContainer = new JPanel();
		rightContainer = new JPanel();
		categoryContainer = new JPanel();
		productContainer = new JPanel();
		checkoutContainer = new JPanel();
		productChosenContainer = new JPanel();
		totalContainer = new JPanel();
		continueContainer = new JPanel();

		allButton = new JButton("All");
		foodButton = new JButton("Food");
		drinkButton = new JButton("Drink");

		container.setLayout(new MigLayout("wrap, fill", "[][]", "[fill]"));
		container.add(leftContainer, "grow, w 70%");
		container.add(rightContainer, "grow, w 30%");
		categoryContainer.setLayout(new MigLayout("wrap, fill", "[fill][fill][fill]", "[grow 0]"));
		categoryContainer.add(allButton);
		categoryContainer.add(foodButton);
		categoryContainer.add(drinkButton);
		leftContainer.setLayout(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));
		leftContainer.add(categoryContainer);
		leftContainer.add(new JScrollPane(productContainer));
		// scrolllbar
		JScrollPane scroll = (JScrollPane) productContainer.getParent().getParent();
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");
		// get all the food and drinks from the database
		List<Product> productList = productDAO.getAllProduct();
		showProduct(productList);
		rightContainer.setLayout(new MigLayout("wrap, fill, insets 15", "[fill]", "[grow 0][fill][grow 0][grow 0]"));
		checkoutContainer.setLayout(new MigLayout("wrap, fill", "[][]", "[]"));
		checkOutText = new JLabel("Check out");
		removeAllButton = new JButton("Remove all");
		removeAllButton.setIcon(new FlatSVGIcon("gui/icon/svg/delete.svg", 0.35f));
		checkoutContainer.add(checkOutText);
		checkoutContainer.add(removeAllButton, "gapleft push");
		productChosenContainer.setLayout(new MigLayout("wrap, fillx, aligny top", "[fill]", ""));
		productContainer.setLayout(new MigLayout("wrap, fill", "[fill][fill][fill]", "[al top]"));

		totalContainer.setLayout(new MigLayout("wrap, fill", "[][]", "[]"));
		totalText = new JLabel("Total: ");
		total = new JLabel();
		total.setText("$0");
		totalContainer.add(totalText);
		totalContainer.add(total, "gapleft push");

		continueContainer.setLayout(new MigLayout("wrap, fill", "[center]", ""));
		continueButton = new JButton("Continue");
		continueButton.putClientProperty(FlatClientProperties.STYLE,
				"arc:5;hoverBackground:$primary;hoverForeground:$white");
		continueContainer.add(continueButton);

		rightContainer.add(checkoutContainer);
		rightContainer.add(new JScrollPane(productChosenContainer));
		rightContainer.add(totalContainer);
		rightContainer.add(continueContainer);

		// scrolllbar
		JScrollPane chosenScroll = (JScrollPane) productChosenContainer.getParent().getParent();
		chosenScroll.setBorder(BorderFactory.createEmptyBorder());
		chosenScroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");

		total.putClientProperty(FlatClientProperties.STYLE, "" + "font:$h3.font;" + "foreground:$danger;");
		
		// style
		activeButtonStyle = "background:$primary; foreground:$white; border:10,0,10,0,$background; hoverBackground:$primary; hoverForeground:$white; font:$h5.font";
		normalButtonStyle = "background:$white; border:10,0,10,0,$background; hoverBackground:$primary; hoverForeground:$white; font:$h5.font";
		allButton.putClientProperty(FlatClientProperties.STYLE, activeButtonStyle);
		foodButton.putClientProperty(FlatClientProperties.STYLE, normalButtonStyle);
		drinkButton.putClientProperty(FlatClientProperties.STYLE, normalButtonStyle);
		
		// event handlers
		allButton.addActionListener(this);
		foodButton.addActionListener(this);
		drinkButton.addActionListener(this);
		removeAllButton.addActionListener(e -> {
			chosenProductOrderDetailList.clear();
			showChosenProductOrderDetail();
		});
		continueButton.addActionListener(e -> {
			Thread thread = new Thread(() -> {
				checkoutDialog = new CheckoutDialog(seatChosenList, chosenProductOrderDetailList, movieSchedule);
				checkoutDialog.setCurrentEmployee(currentEmployee);
				checkoutDialog.setProductOptionDialog(this);
				checkoutDialog.setModal(true);
				checkoutDialog.setVisible(true);
			});
			thread.start();
		});

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				int option = JOptionPane.showConfirmDialog(ProductOptionDialog.this,
						"Are you sure you want to discard all the changes", "Warning", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				} else {
					setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				}
			}

		});

		add(container);
		this.setSize(1300, 800);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public void showProduct(List<Product> productList) {
		productContainer.removeAll();
		productList.forEach(product -> {			
			JButton productCard = new JButton();
			productCard.setLayout(new MigLayout("wrap, fill", "[][]", "[][][]"));
			ImageIcon icon = new ImageIcon(product.getImageSource());
			if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
				icon = new ImageIcon("images/movie-poster-not-found.jpg");
			}
			Image img = icon.getImage();
			Image resizedImg = img.getScaledInstance(200, -1, Image.SCALE_SMOOTH);
			ImageIcon resizedIcon = new ImageIcon(resizedImg);
			JLabel image = new JLabel(resizedIcon);
			JLabel productName = new JLabel(product.getProductName());
			productName.putClientProperty(FlatClientProperties.STYLE, "" + "font:$h4.font;");
			JLabel quantity = new JLabel();
			quantity.setText("Qty:" + product.getQuantity() + "");
			JLabel price = new JLabel("$" + product.getPrice() + "");
			price.putClientProperty(FlatClientProperties.STYLE, "" + "font:$h5.font;" + "foreground:$primary;");
			productCard.add(image, "span 2, al center");
			productCard.add(productName, "span 2, al center");
			productCard.add(quantity);
			productCard.add(price, "gapleft push");
			productCard.putClientProperty(FlatClientProperties.STYLE,
					"default.background:$white;default.background:$black");
			productCard.addActionListener(e -> {
				exist = false;
				chosenProductOrderDetailList.forEach(chosenProductOrderDeatail -> {
					if (chosenProductOrderDeatail.getProduct().equals(product)) {
						chosenProductOrderDeatail.setQuantity(chosenProductOrderDeatail.getQuantity() + 1);
						exist = true;
						showChosenProductOrderDetail();
						return;
					}
				});
				if (!exist) {
					chosenProductOrderDetailList.add(new OrderDetail(1, null, product));
					showChosenProductOrderDetail();
				}
			});
			productContainer.add(productCard);
		});
		this.revalidate();
		this.repaint();
	}

	public void showChosenProductOrderDetail() {
		productChosenContainer.removeAll();
		if (chosenProductOrderDetailList.size() == 0) {
			double totalDouble = 0;
			for (OrderDetail od : chosenProductOrderDetailList) {
				od.setTotal();
				totalDouble += od.getProduct().getPrice() * od.getQuantity();
			}
			DecimalFormat df = new DecimalFormat("#0.00");
			total.setText("$" + df.format(totalDouble) + "");
		}
		chosenProductOrderDetailList.forEach(chosenProductOrderDeatail -> {
			JPanel productChosenCard = new JPanel();
			productChosenCard.setLayout(new MigLayout("wrap, fill", "[grow0][grow 0][fill]", "[][]"));
			ImageIcon chosenIcon = new ImageIcon(chosenProductOrderDeatail.getProduct().getImageSource());
			Image chosenImg = chosenIcon.getImage();
			Image chosenResizedImg = chosenImg.getScaledInstance(80, -1, Image.SCALE_SMOOTH);
			ImageIcon chosenResizedIcon = new ImageIcon(chosenResizedImg);
			JLabel chosenImage = new JLabel(chosenResizedIcon);
			JLabel productChosenName = new JLabel(chosenProductOrderDeatail.getProduct().getProductName());
			SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
			JSpinner productChosenQuantitySpinner = new JSpinner(spinnerModel);
			productChosenQuantitySpinner.setValue(chosenProductOrderDeatail.getQuantity());
			JButton removeButton = new JButton();
			removeButton.setIcon(new ImageIcon("images/delete.png"));
			JLabel priceLabel = new JLabel();
			DecimalFormat df = new DecimalFormat("#0.00");
			priceLabel.setText("$"
					+df.format(chosenProductOrderDeatail.getProduct().getPrice() * chosenProductOrderDeatail.getQuantity()) + "");
			productChosenCard.add(chosenImage, "span 1 2");
			productChosenCard.add(productChosenName);
			productChosenCard.add(removeButton, "gapleft push");
			productChosenCard.add(productChosenQuantitySpinner);
			productChosenCard.add(priceLabel, "gapleft push");
			productChosenContainer.add(productChosenCard);

			double totalDouble = 0;
			for (OrderDetail od : chosenProductOrderDetailList) {
				od.setTotal();
				totalDouble += od.getProduct().getPrice() * od.getQuantity();
			}
			total.setText("$" + df.format(totalDouble) + "");
			productChosenQuantitySpinner.addChangeListener(e -> {
				int newQty = (int) productChosenQuantitySpinner.getValue();
				chosenProductOrderDeatail.setQuantity(newQty);
				showChosenProductOrderDetail();
			});
			removeButton.addActionListener(e -> {
				chosenProductOrderDetailList.remove(chosenProductOrderDeatail);
				showChosenProductOrderDetail();
			});
		});
		this.revalidate();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(allButton)) {
			List<Product> productList = productDAO.getAllProduct();
			showProduct(productList);
			setActive(allButton);
		}
		if (e.getSource().equals(foodButton)) {
			List<Product> productList = productDAO.getAllProductByType("Food");
			showProduct(productList);
			setActive(foodButton);
		}
		if (e.getSource().equals(drinkButton)) {
			List<Product> productList = productDAO.getAllProductByType("Drink");
			showProduct(productList);
			setActive(drinkButton);
		}
	}

	private void setActive(JButton activeButton) {
		
		allButton.putClientProperty(FlatClientProperties.STYLE, normalButtonStyle);
		foodButton.putClientProperty(FlatClientProperties.STYLE, normalButtonStyle);
		drinkButton.putClientProperty(FlatClientProperties.STYLE, normalButtonStyle);
		
		activeButton.putClientProperty(FlatClientProperties.STYLE, activeButtonStyle);
	}

	public void setCurrentEmployee(Employee currentEmployee) {
		this.currentEmployee = currentEmployee;
	}

	public void disposeSeatOptionDialog() {
		seatingOptionDialog.dispose();
	}

	public void setSeatingOptionDialog(SeatingOptionDialog seatingOptionDialog) {
		this.seatingOptionDialog = seatingOptionDialog;
	}
}