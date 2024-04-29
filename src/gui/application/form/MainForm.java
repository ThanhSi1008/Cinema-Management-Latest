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

import gui.application.Application;
import gui.application.form.other.DefaultForm;
import gui.application.form.other.FormCustomerManagement;
import gui.application.form.other.FormDashboard;
import gui.application.form.other.FormMovieManagement;
import gui.application.form.other.FormScreeningManagement;
import gui.application.form.other.FormStaffManagement;
import gui.menu.Menu;
import gui.menu.MenuAction;

public class MainForm extends JLayeredPane {
	private static final long serialVersionUID = 1L;
	private Menu menu;
	private JPanel panelBody;
	private JButton menuButton;

	public MainForm(String role) {
		System.out.println(role);
		init(role);
	}

	private void init(String role) {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new MainFormLayout());
		menu = new Menu(role);
		panelBody = new JPanel(new BorderLayout());
		initMenuArrowIcon();
		menuButton.putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Menu.button.background;" + "arc:999;" + "focusWidth:0;" + "borderWidth:0");
		menuButton.addActionListener((ActionEvent e) -> {
			setMenuFull(!menu.isMenuFull());
		});
		initMenuEvent(role);
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

	private void initMenuEvent(String role) {
		menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
			switch (index) {
			case 0:
				Application.showMainForm(new FormMovieManagement());
				break;
			case 1:
				Application.showMainForm(new FormScreeningManagement());
				break;
			case 2:
				if (role.equalsIgnoreCase("Manager")) {
					Application.showMainForm(new FormStaffManagement());
				} else {
					Application.showMainForm(new FormCustomerManagement());
				}
				break;
			case 3:
				if (role.equalsIgnoreCase("Manager")) {
					Application.showMainForm(new FormCustomerManagement());
				} else {
					switch (subIndex) {
					case 1:
						Application.showMainForm(new DefaultForm("Product - Foods"));
						break;
					case 2:
						Application.showMainForm(new DefaultForm("Product - Drinks"));
						break;
					default:
						action.cancel();
						break;
					}
				}
				break;
			case 4:
				if (role.equalsIgnoreCase("Manager")) {
					switch (subIndex) {
					case 1:
						Application.showMainForm(new DefaultForm("Product - Foods"));
						break;
					case 2:
						Application.showMainForm(new DefaultForm("Product - Drinks"));
						break;
					default:
						action.cancel();
						break;
					}
					break;
				} else {
					switch (subIndex) {
					case 1:
						Application.showMainForm(new FormDashboard());
						break;
					case 2:
						Application.showMainForm(new DefaultForm("Statistics - customer"));
						break;
					case 3:
						Application.showMainForm(new DefaultForm("Statistics - movie"));
						break;
					case 4:
						Application.showMainForm(new DefaultForm("Statistics - product"));
						break;
					default:
						action.cancel();
						break;
					}
					break;
				}
			case 5:
				if (role.equalsIgnoreCase("Manager")) {
					switch (subIndex) {
					case 1:
						Application.showMainForm(new FormDashboard());
						break;
					case 2:
						Application.showMainForm(new DefaultForm("Statistics - customer"));
						break;
					case 3:
						Application.showMainForm(new DefaultForm("Statistics - movie"));
						break;
					case 4:
						Application.showMainForm(new DefaultForm("Statistics - product"));
						break;
					default:
						action.cancel();
						break;
					}
					break;
				} else {
					switch (subIndex) {
					case 1:
						Application.showMainForm(new DefaultForm("Profile - infomation"));
						break;
					case 2:
						Application.showMainForm(new DefaultForm("Profile - change password"));
						break;
					default:
						action.cancel();
						break;
					}
					break;
				}
			case 6:
				if (role.equalsIgnoreCase("Manager")) {
					switch (subIndex) {
					case 1:
						Application.showMainForm(new DefaultForm("Profile - infomation"));
						break;
					case 2:
						Application.showMainForm(new DefaultForm("Profile - change password"));
						break;
					default:
						action.cancel();
						break;
					}
					break;
				} else {
					Application.logout();
					break;
				}
			case 7:
				Application.logout();
				break;
			default:
				action.cancel();
				break;
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
