package gui.application.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.UIScale;

import entity.Employee;
import gui.application.Application;
import gui.application.form.other.FormChangePassword;
import gui.application.form.other.FormCustomerManagement;
import gui.application.form.other.FormDrinkManagement;
import gui.application.form.other.FormFoodManagement;
import gui.application.form.other.FormMovieManagement;
import gui.application.form.other.FormProfileInfo;
import gui.application.form.other.FormScreeningManagement;
import gui.application.form.other.FormStaffManagement;
import gui.application.form.other.FormStatisticsCustomer;
import gui.application.form.other.FormStatisticsGeneral;
import gui.application.form.other.FormStatisticsMovie;
import gui.application.form.other.FormStatisticsProduct;
import gui.menu.Menu;
import gui.menu.MenuAction;

public class MainForm extends JLayeredPane {
	private static final long serialVersionUID = 1L;
	private Menu menu;
	private JPanel panelBody;
	private JButton menuButton;

	public MainForm(Employee employee) {
		init(employee);
		System.out.println(employee);
	}

	private void init(Employee employee) {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new MainFormLayout());
		menu = new Menu(employee.getRole());
		panelBody = new JPanel(new BorderLayout());
		initMenuArrowIcon();
		menuButton.putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Menu.button.background;" + "arc:999;" + "focusWidth:0;" + "borderWidth:0");
		menuButton.addActionListener((ActionEvent e) -> {
			setMenuFull(!menu.isMenuFull());
		});
		initMenuEvent(employee);
		setLayer(menuButton, JLayeredPane.POPUP_LAYER);
		add(menuButton);
		add(menu);
		add(panelBody);
	}

	@Override
	public void applyComponentOrientation(ComponentOrientation o) {
		super.applyComponentOrientation(o);
		initMenuArrowIcon();
	}

	private void initMenuArrowIcon() {
		if (menuButton == null) {
			menuButton = new JButton();
		}
		String icon = (getComponentOrientation().isLeftToRight()) ? "menu_left.svg" : "menu_right.svg";
		menuButton.setIcon(new FlatSVGIcon("gui/icon/svg/" + icon, 0.8f));
	}

	private void initMenuEvent(Employee employee) {
		menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
			if (employee.getRole().equalsIgnoreCase("Manager")) {
				switch (index) {
				case 0:
					Application.showMainForm(new FormMovieManagement());
					break;
				case 1:
					Application.showMainForm(new FormScreeningManagement(employee));
					break;
				case 2:
					Application.showMainForm(new FormStaffManagement());
					break;
				case 3:
					Application.showMainForm(new FormCustomerManagement());
					break;
				case 4:
					switch (subIndex) {
					case 1:
						Application.showMainForm(new FormFoodManagement());
						break;
					case 2:
						Application.showMainForm(new FormDrinkManagement());
						break;
					default:
						action.cancel();
						break;
					}
					break;
				case 5:
					switch (subIndex) {
					case 1:
//						Application.showMainForm(new FormDashboard());
						Application.showMainForm(new FormStatisticsGeneral());
						break;
					case 2:
						Application.showMainForm(new FormStatisticsCustomer());
						break;
					case 3:
						Application.showMainForm(new FormStatisticsMovie());
						break;
					case 4:
						Application.showMainForm(new FormStatisticsProduct());
						break;
					default:
						action.cancel();
						break;
					}
					break;
				case 6:
					switch (subIndex) {
					case 1:
						Application.showMainForm(new FormProfileInfo(employee));
						break;
					case 2:
						Application.showMainForm(new FormChangePassword(employee));
						break;
					default:
						action.cancel();
						break;
					}
					break;
				case 7:
					Application.logout();
					break;
				default:
					action.cancel();
					break;
				}
			} else {
				switch (index) {
				case 0:
					Application.showMainForm(new FormMovieManagement());
					break;
				case 1:
					Application.showMainForm(new FormScreeningManagement(employee));
					break;
				case 3:
					Application.showMainForm(new FormCustomerManagement());
					break;
				case 4:
					switch (subIndex) {
					case 1:
						Application.showMainForm(new FormFoodManagement());
						break;
					case 2:
						Application.showMainForm(new FormDrinkManagement());
						break;
					default:
						action.cancel();
						break;
					}
					break;
				case 5:
					switch (subIndex) {
					case 1:
						Application.showMainForm(new FormStatisticsGeneral());
						break;
					case 2:
						Application.showMainForm(new FormStatisticsCustomer());
						break;
					case 3:
						Application.showMainForm(new FormStatisticsMovie());
						break;
					case 4:
						Application.showMainForm(new FormStatisticsProduct());
						break;
					default:
						action.cancel();
						break;
					}
					break;
				case 6:
					switch (subIndex) {
					case 1:
						Application.showMainForm(new FormProfileInfo(employee));
						break;
					case 2:
						Application.showMainForm(new FormChangePassword(employee));
						break;
					default:
						action.cancel();
						break;
					}
					break;
				case 7:
					Application.logout();
					break;
				default:
					action.cancel();
					break;
				}
			}
		});

	}

	private void setMenuFull(boolean full) {
		String icon;
		if (getComponentOrientation().isLeftToRight()) {
			icon = (full) ? "menu_left.svg" : "menu_right.svg";
		} else {
			icon = (full) ? "menu_right.svg" : "menu_left.svg";
		}
		menuButton.setIcon(new FlatSVGIcon("gui/icon/svg/" + icon, 0.8f));
		menu.setMenuFull(full);
		revalidate();
	}

	public void hideMenu() {
		menu.hideMenuItem();
	}

	public void showForm(Component component) {
		panelBody.removeAll();
		panelBody.add(component);
		panelBody.repaint();
		panelBody.revalidate();
	}

	public void setSelectedMenu(int index, int subIndex) {
		menu.setSelectedMenu(index, subIndex);
	}

	private class MainFormLayout implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			synchronized (parent.getTreeLock()) {
				return new Dimension(5, 5);
			}
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			synchronized (parent.getTreeLock()) {
				return new Dimension(0, 0);
			}
		}

		@Override
		public void layoutContainer(Container parent) {
			synchronized (parent.getTreeLock()) {
				boolean ltr = parent.getComponentOrientation().isLeftToRight();
				Insets insets = UIScale.scale(parent.getInsets());
				int x = insets.left;
				int y = insets.top;
				int width = parent.getWidth() - (insets.left + insets.right);
				int height = parent.getHeight() - (insets.top + insets.bottom);
				int menuWidth = UIScale.scale(menu.isMenuFull() ? menu.getMenuMaxWidth() : menu.getMenuMinWidth());
				int menuX = ltr ? x : x + width - menuWidth;
				menu.setBounds(menuX, y, menuWidth, height);
				int menuButtonWidth = menuButton.getPreferredSize().width;
				int menuButtonHeight = menuButton.getPreferredSize().height;
				int menubX;
				if (ltr) {
					menubX = (int) (x + menuWidth - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.3f)));
				} else {
					menubX = (int) (menuX - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.7f)));
				}
				menuButton.setBounds(menubX, UIScale.scale(30), menuButtonWidth, menuButtonHeight);
				int gap = UIScale.scale(5);
				int bodyWidth = width - menuWidth - gap;
				int bodyHeight = height;
				int bodyx = ltr ? (x + menuWidth + gap) : x;
				int bodyy = y;
				panelBody.setBounds(bodyx, bodyy, bodyWidth, bodyHeight);
			}
		}
	}
}
