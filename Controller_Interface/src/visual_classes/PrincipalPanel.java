package visual_classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import useful_classes.FileHandler;
import useful_classes.osChange;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PrincipalPanel extends JPanel {
	JButton nuevoBtn = new JButton();
	JButton editarBtn = new JButton();
	JButton eliminarBtn = new JButton();
	JButton cambiarContrasenaBtn = new JButton();
	JPanel container = new JPanel();
	JLabel base = new JLabel();
	JLabel execution;
	JLabel scrollLabels = new JLabel();
	JLabel scrollLabelsContainer = new JLabel();
	JLabel scrollBar = new JLabel();
	JLabel tabs = new JLabel();
	JLabel selection = new JLabel();
	
	ImageIcon baseIco = new ImageIcon(this.getClass().getResource("/icons/principal_base.png"));
	ImageIcon tabsTodo = new ImageIcon(this.getClass().getResource("/icons/principal_todo.png"));
	ImageIcon tabsToques = new ImageIcon(this.getClass().getResource("/icons/principal_toques.png"));
	ImageIcon tabsMelodias = new ImageIcon(this.getClass().getResource("/icons/principal_melodias.png"));
	ImageIcon tabsSecuencias = new ImageIcon(this.getClass().getResource("/icons/principal_secuencias.png"));
	ImageIcon selectionIco = new ImageIcon(this.getClass().getResource("/icons/element_select.png"));
	ImageIcon scrollBarIco = new ImageIcon(this.getClass().getResource("/icons/scroll_bar.png"));
	ImageIcon nuevoBtnIcoUnpressed = new ImageIcon(this.getClass().getResource("/icons/nuevo_btn_unpressed.png"));
	ImageIcon nuevoBtnIcoPressed = new ImageIcon(this.getClass().getResource("/icons/nuevo_btn_pressed.png"));
	ImageIcon editarBtnIcoUnpressed = new ImageIcon(this.getClass().getResource("/icons/editar_btn_unpressed.png"));
	ImageIcon editarBtnIcoPressed = new ImageIcon(this.getClass().getResource("/icons/editar_btn_pressed.png"));
	ImageIcon eliminarBtnIcoUnpressed = new ImageIcon(this.getClass().getResource("/icons/eliminar_btn_unpressed.png"));
	ImageIcon eliminarBtnIcoPressed = new ImageIcon(this.getClass().getResource("/icons/eliminar_btn_pressed.png"));	
	ImageIcon cambiarContrasenaIcoUnpressed = new ImageIcon(this.getClass().getResource("/icons/cambiar_contraseņa_btn_unpressed.png"));
	ImageIcon cambiarContrasenaIcoPressed = new ImageIcon(this.getClass().getResource("/icons/cambiar_contraseņa_btn_pressed.png"));
	int scale = 2;
	//Buttons
	int buttonsGap;
	int buttonsX;
	int buttonsWidth;
	int buttonsHeight;
	int nuevoBtnY;
	int editarBtnY;
	int eliminarBtnY;
	int cambiarContrasenaY;
	//Container
	int containerX;
	int containerY;
	int containerWidth;
	int containerHeight;
	//Base
	int baseX;
	int baseY;
	int baseWidth;
	int baseHeight;
	//ScrollLabelsContainer
	int scrollLabelsContainerGap;
	int scrollLabelsContainerX;
	int scrollLabelsContainerY;
	int scrollLabelsContainerWidth;
	int scrollLabelsContainerHeight;
	//ScrollLabels
	int scrollLabelsX;
	int scrollLabelsY;
	int scrollLabelsWidth;
	int scrollLabelsHeight;
	//execution
	int executionGap;
	int executionX;
	int executionY;
	int executionWidth;
	int executionHeight;
	// ScrollBar
	int scrollBarX;
	int scrollBarY;
	int scrollBarWidth;
	int scrollBarHeight;
	int scrollBarMaxHeight;
	int scrollBarMinHeight;
	//Tabs
	int tabsX;
	int tabsY;
	int tabsWidth;
	int tabsHeight;
	//Selection
	int selectionX;
	int selectionY;
	int selectionWidth;
	int selectionHeight;
	//IndividualLabel
	int individualLabelX;
	int individualLabelY;
	int individualLabelWidth;
	int individualLabelHeight;

	ArrayList<String> todoNames = new ArrayList<String>();
	ArrayList<String> toquesNames = new ArrayList<String>();
	ArrayList<String> melodiasNames = new ArrayList<String>();
	ArrayList<String> secuenciasNames = new ArrayList<String>();
	FileHandler executions = new FileHandler();
	osChange os = new osChange();
	MainPane main;
	/**
	 * Create the panel.
	 */
	public PrincipalPanel() {
		Dimension screenSize = os.setDimension();
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int)screenSize.getHeight();
		//Panel
		int panelWidth = screenWidth;
		int panelHeight = screenHeight;
		int panelX=0;
		int panelY=0;
		//Setting this panel
		setOpaque(false);
		setBounds(panelX, panelY, panelWidth, panelHeight);
		setLayout(null);
		
		//Container
		containerX = 0;
		containerY = 0;
		containerWidth = (int)Math.round(baseIco.getIconWidth()/scale);
		containerHeight = screenHeight;
		container.setLayout(null);
		container.setBorder(null);
		container.setOpaque(false);
		container.setBounds(containerX,containerY,containerWidth,containerHeight);
		
		//Tabs
		tabsX = 0;
		tabsY = 0;
		tabsWidth = (int)Math.round(tabsTodo.getIconWidth()/scale);
		tabsHeight= (int)Math.round(tabsTodo.getIconHeight()/scale);
		tabs.setBounds(tabsX,tabsY,tabsWidth,tabsHeight);
		
		//Base
		baseX = 0;
		baseY = tabsHeight;
		baseWidth = (int)Math.round(baseIco.getIconWidth()/scale);
		baseHeight = (int)Math.round(baseIco.getIconHeight()/scale);
		base.setBounds(baseX,baseY,baseWidth,baseHeight);
		
		//ScrollLabelsContainer
		scrollLabelsContainerGap = 10;
		scrollLabelsContainerX = scrollLabelsContainerGap;
		scrollLabelsContainerY = baseY+scrollLabelsContainerGap;
		scrollLabelsContainerWidth = (int) Math.round(baseWidth - scrollLabelsContainerGap * 2);
		scrollLabelsContainerHeight = (int) Math.round(baseHeight - scrollLabelsContainerGap * 2);
		scrollLabelsContainer.setBounds(scrollLabelsContainerX,scrollLabelsContainerY,scrollLabelsContainerWidth,scrollLabelsContainerHeight);
		//scrollLabelsContainer.setOpaque(true);
		
		//ScrollLabels
		scrollLabelsX = 0;
		scrollLabelsY = 0;
		scrollLabelsWidth = scrollLabelsContainerWidth;
		scrollLabelsHeight = scrollLabelsContainerHeight;
		scrollLabels.setBounds(scrollLabelsX,scrollLabelsY,scrollLabelsWidth,scrollLabelsHeight);
		
		//Buttons
		buttonsGap = 20;
		float overScale = 1.05f;
		buttonsWidth = (int)Math.round(nuevoBtnIcoUnpressed.getIconWidth()/(scale*overScale));
		buttonsHeight = (int)Math.round(nuevoBtnIcoUnpressed.getIconHeight()/(scale*overScale));
		buttonsX = (containerX + containerWidth)+((screenWidth-(containerX + containerWidth))/2)-buttonsWidth/2;
		//buttonsX=500;
		nuevoBtnY = 100;
		editarBtnY = nuevoBtnY+buttonsHeight+buttonsGap;
		eliminarBtnY = editarBtnY+buttonsHeight+buttonsGap;
		cambiarContrasenaY = eliminarBtnY+buttonsHeight+buttonsGap;
		
		//selection
		selectionX=0;
		selectionY=0;
		selectionWidth = (int)Math.round(selectionIco.getIconWidth()/scale);
		selectionHeight = (int)Math.round(selectionIco.getIconHeight()/scale);
		
		//ScrollBar
		scrollBarWidth = (int)Math.round(scrollBarIco.getIconWidth()/scale);
		scrollBarHeight = (int)Math.round(scrollBarIco.getIconHeight()/scale);
	//	scrollBarX = ;
	//	scrollBarY;
		//scrollBarMaxHeight;
		//scrollBarMinHeight;
		
		//Icon		
		baseIco = new ImageIcon(baseIco.getImage().getScaledInstance(baseWidth, baseHeight,java.awt.Image.SCALE_SMOOTH));
		tabsTodo = new ImageIcon(tabsTodo.getImage().getScaledInstance(tabsWidth, tabsHeight,java.awt.Image.SCALE_SMOOTH));
		tabsToques = new ImageIcon(tabsToques.getImage().getScaledInstance(tabsWidth, tabsHeight,java.awt.Image.SCALE_SMOOTH));
		tabsMelodias = new ImageIcon(tabsMelodias.getImage().getScaledInstance(tabsWidth, tabsHeight,java.awt.Image.SCALE_SMOOTH));
		tabsSecuencias = new ImageIcon(tabsSecuencias.getImage().getScaledInstance(tabsWidth, tabsHeight,java.awt.Image.SCALE_SMOOTH));
		
		selectionIco = new ImageIcon(selectionIco.getImage().getScaledInstance(selectionWidth, selectionHeight,java.awt.Image.SCALE_SMOOTH));
		scrollBarIco = new ImageIcon(scrollBarIco.getImage().getScaledInstance(baseWidth, baseHeight,java.awt.Image.SCALE_SMOOTH));
		nuevoBtnIcoUnpressed = new ImageIcon(nuevoBtnIcoUnpressed.getImage().getScaledInstance(buttonsWidth, buttonsHeight,java.awt.Image.SCALE_SMOOTH));
		nuevoBtnIcoPressed = new ImageIcon(nuevoBtnIcoPressed.getImage().getScaledInstance(buttonsWidth, buttonsHeight,java.awt.Image.SCALE_SMOOTH));
		editarBtnIcoUnpressed = new ImageIcon(editarBtnIcoUnpressed.getImage().getScaledInstance(buttonsWidth, buttonsHeight,java.awt.Image.SCALE_SMOOTH));
		editarBtnIcoPressed = new ImageIcon(editarBtnIcoPressed.getImage().getScaledInstance(buttonsWidth, buttonsHeight,java.awt.Image.SCALE_SMOOTH));
		eliminarBtnIcoUnpressed = new ImageIcon(eliminarBtnIcoUnpressed.getImage().getScaledInstance(buttonsWidth, buttonsHeight,java.awt.Image.SCALE_SMOOTH));
		eliminarBtnIcoPressed = new ImageIcon(eliminarBtnIcoPressed.getImage().getScaledInstance(buttonsWidth, buttonsHeight,java.awt.Image.SCALE_SMOOTH));
		cambiarContrasenaIcoUnpressed = new ImageIcon(cambiarContrasenaIcoUnpressed.getImage().getScaledInstance(buttonsWidth, buttonsHeight,java.awt.Image.SCALE_SMOOTH));
		cambiarContrasenaIcoPressed = new ImageIcon(cambiarContrasenaIcoPressed.getImage().getScaledInstance(buttonsWidth, buttonsHeight,java.awt.Image.SCALE_SMOOTH));
		
		//Icon set
		tabSelect(0);
		base.setIcon(baseIco);
		nuevoBtn.setIcon(nuevoBtnIcoUnpressed);
		nuevoBtn.setPressedIcon(nuevoBtnIcoPressed);
		editarBtn.setIcon(editarBtnIcoUnpressed);
		editarBtn.setPressedIcon(editarBtnIcoPressed);
		eliminarBtn.setIcon(eliminarBtnIcoUnpressed);
		eliminarBtn.setPressedIcon(eliminarBtnIcoPressed);
		cambiarContrasenaBtn.setIcon(cambiarContrasenaIcoUnpressed);
		cambiarContrasenaBtn.setPressedIcon(cambiarContrasenaIcoPressed);
		
		//nuevo
		nuevoBtn.setBorder(null);
		nuevoBtn.setContentAreaFilled(false);
		nuevoBtn.setBounds(buttonsX, nuevoBtnY, buttonsWidth, buttonsHeight);
		add(nuevoBtn);
		
		//editar
		editarBtn.setBorder(null);
		editarBtn.setContentAreaFilled(false);
		editarBtn.setBounds(buttonsX, editarBtnY, buttonsWidth, buttonsHeight);
		add(editarBtn);
		
		//eliminar
		eliminarBtn.setBorder(null);
		eliminarBtn.setContentAreaFilled(false);
		eliminarBtn.setBounds(buttonsX, eliminarBtnY, buttonsWidth, buttonsHeight);
		add(eliminarBtn);
		
		//contraseņa
		cambiarContrasenaBtn.setBorder(null);
		cambiarContrasenaBtn.setContentAreaFilled(false);
		cambiarContrasenaBtn.setBounds(buttonsX, cambiarContrasenaY, buttonsWidth, buttonsHeight);
		add(cambiarContrasenaBtn);
		
		fillNameList();
		showExecutionList(toquesNames);
		
		add(container);
		container.add(tabs);
		container.add(scrollLabelsContainer);
		container.add(base);
		scrollLabelsContainer.add(scrollLabels);
	}
	
	public void setMainPane(MainPane main) {
		this.main = main;
		setActions();
	}
	private void fillNameList() {
		String list;
		String[] lines;
		
		executions.setFilename("toques.int");
		list = executions.readFile();
		lines = executions.readFileLine();
		for(String line: lines)
			toquesNames.add(line);
		
		executions.setFilename("melodias.int");
		list = executions.readFile();
		lines = executions.readFileLine();
		for(String line: lines)
			toquesNames.add(line);
		
		executions.setFilename("secuencias.int");
		list = executions.readFile();
		lines = executions.readFileLine();
		for(String line: lines)
			toquesNames.add(line);
		
		todoNames.addAll(toquesNames);
		todoNames.addAll(melodiasNames);
		todoNames.addAll(secuenciasNames);
		
		Collections.sort(todoNames);
		/*for(int i=0;i<todoNames.size();i++) {
			System.out.println(todoNames.get(i));
		}*/
	}
	
	public void showExecutionList(ArrayList<String> nameList) {
		for (int i = 0; i < nameList.size(); i++) {
			executionSetting(i,nameList.get(i));
		}
	}
	
	private void executionSetting(int iteration,String text) {
		//execution
		execution = new JLabel();
		executionGap = 0;
		executionWidth=scrollLabelsWidth;
		executionHeight=45;
		executionX=0;
		executionY=(executionHeight+executionGap)*iteration;
		execution.setText(text);
		execution.setFont(new Font("Quicksand Medium", Font.PLAIN, executionHeight - 15));
		execution.setHorizontalAlignment(SwingConstants.LEFT);
		execution.setVerticalAlignment(SwingConstants.CENTER);
		execution.setForeground(Color.BLACK);
		execution.setBackground(Color.BLUE);
		//execution.setOpaque(true);
		execution.setBounds(executionX,executionY,executionWidth,executionHeight);
		scrollLabels.add(execution);
	}
	
	private void tabSelect(int tabNumber){
		switch(tabNumber) {
		case 0:
			tabs.setIcon(tabsTodo);
			break;
		case 1:
			tabs.setIcon(tabsToques);
			break;
		case 2:
			tabs.setIcon(tabsMelodias);
			break;
		case 3:
			tabs.setIcon(tabsSecuencias);
			break;
		}
	}
	
	private void setActions() {
		nuevoBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				main.menuNavegation.next(main.atribute);
			}
		});
		
		tabs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				System.out.println("x: "+x);
				int tabNumber;
				if(x<=90) {
					tabNumber = 0;
				}
				else if(x<=234) {
					tabNumber = 1;
				}
				else if(x<=407) {
					tabNumber = 2;
				}
				else {
					tabNumber = 3;
				}
				tabSelect(tabNumber);
			}
		});
	}
}
