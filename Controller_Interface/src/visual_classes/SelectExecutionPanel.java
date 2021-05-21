package visual_classes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import useful_classes.*;

import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.MouseInfo;

public class SelectExecutionPanel extends JPanel {
	JPanelBackground window = new JPanelBackground();
	JPanel scrollLabelsContainer = new JPanel();
	JPanel scrollButtons = new JPanel();
	JLabel scrollBar = new JLabel();
	JLabel title = new JLabel();
	JLabel executionSelected = new JLabel();
	JLabel executionButton;

	// Window
	int windowY;
	int windowX;
	int windowWidth;
	int windowHeight;
	// ScrollLabelsContainer
	int scrollLabelsContainerX;
	int scrollLabelsContainerY;
	int scrollLabelsContainerWidth;
	int scrollLabelsContainerHeight;
	// ScrollButtons
	int scrollButtonsX;
	int scrollButtonsY;
	int scrollButtonsWidth;
	int scrollButtonsHeight;
	// ScrollBar
	int scrollBarX;
	int scrollBarY;
	int scrollBarWidth;
	int scrollBarHeight;
	int scrollBarMaxHeight;
	int scrollBarMinHeight;
	// executionButton
	int executionButtonX;
	int executionButtonY;
	int executionButtonWidth;
	int executionButtonHeight;
	// executionSelected
	int executionSelectedX;
	int executionSelectedY;
	int executionSelectedWidth;
	int executionSelectedHeight;
	// Title
	int titleX;
	int titleY;
	int titleWidth;
	int titleHeight;
	// ImageIcons
	ImageIcon windowIco;
	ImageIcon scrollBarIco;
	ImageIcon executionSelectedIco;

	// Other
	float scale = 3.7f;
	int size[] = new int[2];
	int labelMoveX;
	int moveY;
	int gap = 10;
	int buttonGap = 0;

	private String type = "la melodía";
	private String[] nameList;
	FileHandler fl = new FileHandler();

	MainPane main;
	String test = "Selecione la melodía";
	osChange os = new osChange();
	boolean labelPressed = false;
	boolean barPressed = false;
	String extension;

	public SelectExecutionPanel() {
		// Setting size parameters
		// Screen
		Dimension screenSize = os.setDimension();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		// Panel
		int panelWidth = 800;
		int panelHeight = screenHeight;
		int panelX = (screenWidth / 2) - (panelWidth / 2);
		int panelY = 0;

		// Setting this panel
		setOpaque(false);
		setBounds(panelX, panelY, panelWidth, panelHeight);
		setLayout(null);

		// Window
		windowSetting(panelWidth, panelHeight);

		// scrollLabelsContainer
		scrollLabelsContainerSetting();

		// scrollButtons
		scrollButtonsSetting();

		// Title
		titleSetting();

		add(window);
	}

	public void setMainPane(MainPane main) {
		this.main = main;
	}

	public void setExtensionNameList(String extension) {
		this.extension = extension;
		fl.setDirection("src/files");
		nameList = fl.searchFiles(extension);
	}

	public void setType(String type) {
		this.type = type;
	}

	private void setMessage(String filename) {
		switch(main.atribute.time) {
		case INMEDIATAS:
			//iniciar ejecución ya
			break;
		case PROGRAMADAS:
			main.message = main.dateForMessage+" "+main.hourForMessage+" "+filename;
			break;
		case REPETITIVAS:
			main.message = main.daysOfWeekForMessage+" "+main.hourForMessage+" "+filename;
			break;
		}	
	}

	public void cleanButtonList() {
		title.setText("Seleccione " + type);
		scrollButtons.removeAll();
		scrollLabelsContainer.removeAll();
	}

	public void showButtonListAndSelectionSetting() {
		for (int i = 0; i < nameList.length; i++) {
			executionButtonSetting(i);
			executionSelectedSetting();
			executionButtonActionListener();
		}
		scrollButtons.add(executionSelected);
		scrollButtons.setBounds(scrollButtonsX, scrollButtonsY, scrollButtonsWidth, scrollButtonsHeight);
		scrollBarSetting();
		scrollLabelsContainer.add(scrollBar);
		scrollLabelsContainer.add(scrollButtons);

		scrollButtons.setLocation(0, 0);
	}

	private void windowSetting(int panelWidth, int panelHeight) {
		windowIco = iconScale(windowIco, "/icons/execution_selection.png", scale);
		windowWidth = windowIco.getIconWidth();
		windowHeight = windowIco.getIconHeight();
		windowX = panelWidth / 2 - windowWidth / 2;
		windowY = panelHeight / 2 - windowHeight / 2 + 30;
		window.setLayout(null);
		window.setBounds(windowX, windowY, windowWidth, windowHeight);
		window.setBackground("/icons/execution_selection.png");
	}

	private void scrollLabelsContainerSetting() {
		scrollLabelsContainerWidth = (int) Math.round(windowWidth - gap * 2);
		scrollLabelsContainerHeight = (int) Math.round(windowHeight * 0.89 - gap * 2);
		scrollLabelsContainerX = gap;
		scrollLabelsContainerY = windowHeight - scrollLabelsContainerHeight - gap;
		scrollLabelsContainer.setBounds(scrollLabelsContainerX, scrollLabelsContainerY, scrollLabelsContainerWidth,
				scrollLabelsContainerHeight);
		scrollLabelsContainer.setLayout(null);
		scrollLabelsContainer.setOpaque(false);
		window.add(scrollLabelsContainer);
	}

	private void scrollButtonsSetting() {
		scrollButtonsX = 0;
		scrollButtonsY = 0;
		scrollButtonsWidth = scrollLabelsContainerWidth;
		scrollButtonsHeight = 400;
		scrollButtons.setBackground(Color.BLUE);
		scrollButtons.setLayout(null);
		scrollButtons.setOpaque(false);
	}

	private void executionButtonSetting(int iteration) {
		executionButton = new JLabel();
		executionButton.setText(nameList[iteration]);

		executionButtonWidth = scrollButtonsWidth;
		executionButtonHeight = 55;
		executionButtonX = 0;
		executionButtonY = (executionButtonHeight + buttonGap) * iteration;
		executionButton.setBounds(executionButtonX, executionButtonY, executionButtonWidth, executionButtonHeight);
		executionButton.setFont(new Font("Quicksand Medium", Font.PLAIN, executionButtonHeight - 15));
		executionButton.setHorizontalAlignment(SwingConstants.LEFT);
		executionButton.setVerticalAlignment(SwingConstants.CENTER);
		executionButton.setForeground(Color.WHITE);
		executionButton.setBorder(null);
		// executionButton.setBackground(Color.BLUE);
	}

	private void executionSelectedSetting() {
		// Selected
		executionSelectedX = executionButtonX;
		executionSelectedY = executionButtonY;
		executionSelectedWidth = executionButtonWidth;
		executionSelectedHeight = executionButtonHeight;
		executionSelected.setBounds(executionSelectedX, executionSelectedY, executionSelectedWidth,
				executionSelectedHeight);
		executionSelectedIco = iconSize(executionSelectedIco, "/icons/element_select.png", executionSelectedWidth,
				executionSelectedHeight);
		executionSelected.setIcon(executionSelectedIco);
		executionSelected.setVisible(false);
		executionSelected.setOpaque(false);
		scrollButtons.add(executionButton);
		// scrollButtons.add(executionSelected);
		scrollButtonsHeight = (executionButtonHeight + buttonGap) * nameList.length;
	}

	private void executionButtonActionListener() {
		executionButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				labelPressed = true;
				moveY = MouseInfo.getPointerInfo().getLocation().y;
				// System.out.println("Touch pressed: "+ moveY);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				labelPressed = false;
				// System.out.println("Touch released:
				// "+MouseInfo.getPointerInfo().getLocation().y);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// System.out.println("Touch clicked");
				int relativeMouse = Math.abs(MouseInfo.getPointerInfo().getLocation().y
						- (scrollButtons.getY() + scrollLabelsContainer.getY() + window.getY()));

				for (int i = 0; i < nameList.length; i++) {
					if (relativeMouse <= (executionButtonHeight + buttonGap) * i + executionButtonHeight) {
						executionSelectedY = (executionButtonHeight + buttonGap) * i;
						break;
					}
				}
				setMessage(executionButton.getText());
				System.out.println(main.message);
				main.menuNavegation.goToMain(main.atribute);
				executionSelectedX = executionButton.getX();
				executionSelectedWidth = executionButton.getWidth();
				executionSelectedHeight = executionButton.getHeight();
				executionSelected.setBounds(executionSelectedX, executionSelectedY, executionSelectedWidth,
						executionSelectedHeight);
				executionSelected.setVisible(true);
			}
		});
		executionButton.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (labelPressed) {
					// System.out.println("Touch dragged:
					// "+MouseInfo.getPointerInfo().getLocation().y);
					scrollButtonsY = MouseInfo.getPointerInfo().getLocation().y - moveY + scrollButtons.getY();
					moveY = MouseInfo.getPointerInfo().getLocation().y;
					executionSelected.setVisible(false);
					int limit = scrollLabelsContainerHeight - scrollButtonsHeight;
					scrollButtonsY = Math.max(scrollButtonsY, limit);
					scrollButtonsY = Math.min(scrollButtonsY, 0);
					scrollButtons.setBounds(scrollButtonsX, scrollButtonsY, scrollButtonsWidth, scrollButtonsHeight);

					int scrollBarYMin = 0;
					int scrollBarYMax = scrollLabelsContainerHeight - scrollBarHeight;
					int scrollButtonsYMin = 0;
					int scrollButtonsYMax = scrollLabelsContainerHeight - scrollButtonsHeight;
					scrollBarY = (int) Math
							.round((scrollButtonsY - scrollButtonsYMin) * 1f / (scrollButtonsYMax - scrollButtonsYMin)
									* (scrollBarYMax - scrollBarYMin) + scrollBarYMin);
					scrollBar.setBounds(scrollBarX, scrollBarY, scrollBarWidth, scrollBarHeight);
				}
			}
		});
	}

	private void scrollBarSetting() {
		// ScrollBar
		scrollBarMaxHeight = scrollLabelsContainerHeight;
		scrollBarMinHeight = (int) Math.round(scrollLabelsContainerHeight * 0.2);
		scrollBarWidth = 50;
		scrollBarHeight = scrollBarMaxHeight - (scrollButtonsHeight - scrollLabelsContainerHeight);
		scrollBarHeight = Math.max(scrollBarHeight, scrollBarMinHeight);
		scrollBarX = scrollLabelsContainerWidth - scrollBarWidth;
		scrollBarY = 0;
		scrollBarIco = iconSize(scrollBarIco, "/icons/scroll_bar.png", scrollBarWidth, scrollBarHeight);
		scrollBar.setIcon(scrollBarIco);
		scrollBar.setBounds(scrollBarX, scrollBarY, scrollBarWidth, scrollBarHeight);
		// If it appears
		if (scrollButtonsHeight <= scrollLabelsContainerHeight)
			scrollBar.setVisible(false);
		else
			scrollBar.setVisible(true);

		scrollBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				barPressed = true;
				moveY = MouseInfo.getPointerInfo().getLocation().y;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				barPressed = false;
			}
		});
		scrollBar.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (barPressed) {
					scrollBarY = MouseInfo.getPointerInfo().getLocation().y - moveY + scrollBar.getY();
					moveY = MouseInfo.getPointerInfo().getLocation().y;
					executionSelected.setVisible(false);
					int limit = scrollLabelsContainerHeight - scrollBarHeight;
					scrollBarY = Math.min(scrollBarY, limit);
					scrollBarY = Math.max(scrollBarY, 0);
					scrollBar.setBounds(scrollBarX, scrollBarY, scrollBarWidth, scrollBarHeight);

					int scrollBarYMin = 0;
					int scrollBarYMax = limit;
					int scrollButtonsYMin = 0;
					int scrollButtonsYMax = scrollLabelsContainerHeight - scrollButtonsHeight;
					scrollButtonsY = (int) Math
							.round((scrollBarY - scrollBarYMin) * 1f / (scrollBarYMax - scrollBarYMin)
									* (scrollButtonsYMax - scrollButtonsYMin) + scrollButtonsYMin);
					scrollButtons.setBounds(scrollButtonsX, scrollButtonsY, scrollButtonsWidth, scrollButtonsHeight);
				}
			}
		});
	}

	private void titleSetting() {
		// Title
		int titleSpace = (int) Math.round(windowHeight * 0.11);
		titleWidth = 700;
		titleHeight = 45;
		titleX = gap;
		titleY = titleSpace / 2 - titleHeight / 2;
		title.setBounds(titleX, titleY, titleWidth, titleHeight);
		title.setText("Seleccione " + type);
		title.setFont(new Font("Alegreya Sans SC", Font.BOLD, titleHeight));
		title.setForeground(Color.WHITE);
		title.setBackground(Color.BLACK);
		title.setOpaque(false);
		window.add(title);
	}

	private ImageIcon iconScale(ImageIcon icon, String resource, float scale) {
		int[] size = new int[2];
		icon = new ImageIcon(this.getClass().getResource(resource));
		size[0] = (int) Math.round(icon.getIconWidth() / scale);
		size[1] = (int) Math.round(icon.getIconHeight() / scale);
		icon = new ImageIcon(icon.getImage().getScaledInstance(size[0], size[1], java.awt.Image.SCALE_SMOOTH));
		// System.out.println("Size: "+size[0]+", "+size[1]);
		return icon;
	}

	private ImageIcon iconSize(ImageIcon icon, String resource, int width, int height) {
		icon = new ImageIcon(this.getClass().getResource(resource));
		icon = new ImageIcon(icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
		// System.out.println("Size: "+size[0]+", "+size[1]);
		return icon;
	}

}
