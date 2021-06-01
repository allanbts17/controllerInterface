package visual_classes;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import useful_classes.*;
import visual_classes.VisualElements.*;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Color;

public class MainPane extends JPanelBackground {
	
	//JPanel classes
	SendExecution sendExecution = new SendExecution();
	FirstTimePanel firstPane = new FirstTimePanel();
	StartSessionPanel startSessionPane = new StartSessionPanel();
	ChangePasswordPanel changePasswordPane = new ChangePasswordPanel();
	public PrincipalPanel principalPane = new PrincipalPanel();
	ExecutionTypePanel executionTypePane = new ExecutionTypePanel();
	ExecutionTimePanel executionTimePane = new ExecutionTimePanel();
	SelectExecutionPanel selectExecutionPane = new SelectExecutionPanel();
	SelectDatePanel selectDatePane = new SelectDatePanel(); 
	SelectHourPanel selectHourPane = new SelectHourPanel();
	SelectDayHourPanel selectDayHourPane = new SelectDayHourPanel();
	ScreenSaver screenSaverPane = new ScreenSaver();			
	DateAndHour dateAndHour = new DateAndHour();
	
	//Keyboard
	JPanel keyPan = new JPanel();
	//
	String message;
	String dateForMessage;
	String daysOfWeekForMessage;
	String hourForMessage;
	
	//Buttons
	CustomButton back_btn = new VisualElements().new CustomButton("back");
	CustomButton lock_btn = new VisualElements().new CustomButton("lock");
	CustomButton screen_btn = new VisualElements().new CustomButton("screen");
	
	int screenWidth;
	int screenHeight;
	
	int squareButtonHorizontalGap;
	int squareButtonSize;
	int squareButtonVerticalGap;
	
	//Other classes
	MenuNavegation menuNavegation;
	Encryption hash = new Encryption();

	MenuAtributes atribute = new MenuAtributes();
	FileHandler passRead = new FileHandler();
	
	MenuOptionsType enumType;
	MenuOptionsTime enumTime;
	osChange os = new osChange();
	JFrame frame;
	boolean save_screen_on = false;
	ArrayList<String> scheduledExecutionList;
	VirtualKeyboard virtualKeyboard = new VirtualKeyboard();
	//private boolean keyboardOn = true;
	
	public MainPane(JFrame f) {
		frame = f;
		sizeSetting();
		add(executionTimePane);
		add(executionTypePane);
		add(principalPane);
		add(firstPane);
		add(startSessionPane);
		add(changePasswordPane);
		add(screenSaverPane);	
		add(selectExecutionPane);
		add(selectDatePane); 
		add(selectHourPane);
		add(selectDayHourPane);
		add(back_btn);
		add(lock_btn);
		add(screen_btn);  
		add(dateAndHour);
		add(virtualKeyboard);
		menuNavegation = new MenuNavegation(this);	
		
		setBackgroundImage();
		setBackButton();
		setLockButton();
		setScreenButton();
		setScreenSaverListenerToDeactivateIt();
		setInitialPanel();		
	
		//Sending this class to the other class
		startSessionPane.setMainPane(this);
		firstPane.setMainPane(this);
		changePasswordPane.setMainPane(this);
		principalPane.setMainPane(this);
		sendExecution.setScheduledExecutionList(this.scheduledExecutionList);
		executionTypePane.setMainPane(this);
		executionTimePane.setMainPane(this);
		selectExecutionPane.setMainPane(this);
		selectDatePane.setMainPane(this);
		selectHourPane.setMainPane(this);
		selectDayHourPane.setMainPane(this);
		dateAndHour.setMainPane(this);
		sendExecution.setMainPane(this);
		virtualKeyboard.setFrame(frame);
		
		//////////////
		/*selectDatePane.showCalendar();
		atribute.time = MenuOptionsTime.PROGRAMADAS;
		menuNavegation.goTo(atribute,4);	*/
		/////////////
		try {
			sendExecution.playSong();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JavaLayerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		back_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dateAndHour.update();
				menuNavegation.goBack(atribute);
			}
		});
		lock_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dateAndHour.update();
				menuNavegation.goToStart(atribute);
			}
		});
		
		screen_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				save_screen_on = true;
				menuNavegation.screenSaver();
			}
		});	
		
	}
	
		public void setScreenSaverListenerToDeactivateIt(){
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(save_screen_on) {
					menuNavegation.screenSaver();
					save_screen_on = false;
				}
				dateAndHour.update();
			}
		});
	}
	
	public void setInitialPanel(){
		passRead.setFilename("main_data.int");
		System.out.println("main_data: "+passRead.ifExist());
		if(passRead.ifExist()) {
			if(passRead.readFile().startsWith("qzr")) {
				atribute.first = false;
				menuNavegation.next(atribute);
			}
			else {
				atribute.first = true;
				menuNavegation.next(atribute);
			}	
		}
		else {
			atribute.first = true;
			menuNavegation.next(atribute);
		}
	}
	
	private void sizeSetting(){
		Dimension screenSize = os.setDimension();
		screenWidth = (int)screenSize.getWidth();
		screenHeight = (int)screenSize.getHeight();
		
		squareButtonHorizontalGap = 20;
		squareButtonSize = (int)(screenWidth * 0.12);
		squareButtonVerticalGap = screenHeight - squareButtonHorizontalGap - squareButtonSize;
	}
	
	public void setBackgroundImage(){
		String imageDir = "/icons/background.png";
		setBackground(imageDir);
		setLayout(null);
	}
	public void setBackButton(){
		int backX = squareButtonHorizontalGap;
		int backY = squareButtonVerticalGap;
		ImageIcon backU = new ImageIcon(this.getClass().getResource("/icons/back_btn_unpressed.png"));
		ImageIcon backP = new ImageIcon(this.getClass().getResource("/icons/back_btn_pressed.png"));
		backU = new ImageIcon(backU.getImage().getScaledInstance( squareButtonSize, squareButtonSize,java.awt.Image.SCALE_SMOOTH));
		backP = new ImageIcon(backP.getImage().getScaledInstance( squareButtonSize, squareButtonSize,java.awt.Image.SCALE_SMOOTH));
		back_btn.setBounds(backX,backY,squareButtonSize,squareButtonSize);
		back_btn.setIcon(backU);
		back_btn.setPressedIcon(backP);
		back_btn.setBorder(null);
		back_btn.setContentAreaFilled(false);
	}
	public void setLockButton() {
		int lockX = screenWidth - squareButtonHorizontalGap - squareButtonSize;
		int lockY = squareButtonVerticalGap;
		ImageIcon lockU = new ImageIcon(this.getClass().getResource("/icons/lock_btn_unpressed.png"));
		ImageIcon lockP = new ImageIcon(this.getClass().getResource("/icons/lock_btn_pressed.png"));
		lockU = new ImageIcon(lockU.getImage().getScaledInstance( squareButtonSize, squareButtonSize,java.awt.Image.SCALE_SMOOTH));
		lockP = new ImageIcon(lockP.getImage().getScaledInstance( squareButtonSize, squareButtonSize,java.awt.Image.SCALE_SMOOTH));
		lock_btn.setBounds(lockX,lockY,squareButtonSize,squareButtonSize);
		lock_btn.setIcon(lockU);
		lock_btn.setPressedIcon(lockP);
		lock_btn.setBorder(null);
		lock_btn.setContentAreaFilled(false);
	}
	public void setScreenButton() {
		int screenX = screenWidth - squareButtonHorizontalGap - squareButtonSize;
		int screenY = squareButtonVerticalGap;
		ImageIcon screenU = new ImageIcon(this.getClass().getResource("/icons/screen_btn_unpressed.png"));
		ImageIcon screenP = new ImageIcon(this.getClass().getResource("/icons/screen_btn_pressed.png"));
		screenU = new ImageIcon(screenU.getImage().getScaledInstance( squareButtonSize, squareButtonSize,java.awt.Image.SCALE_SMOOTH));
		screenP = new ImageIcon(screenP.getImage().getScaledInstance( squareButtonSize, squareButtonSize,java.awt.Image.SCALE_SMOOTH));		
		screen_btn.setBounds(screenX,screenY,squareButtonSize,squareButtonSize);
		screen_btn.setIcon(screenU);
		screen_btn.setPressedIcon(screenP);
		screen_btn.setBorder(null);
		screen_btn.setContentAreaFilled(false);
	}

	public char[] stringToChar(String str) {
		
		// Creating array of string length
        char[] ch = new char[str.length()];
  
        // Copy character by character into array
        for (int i = 0; i < str.length(); i++) {
            ch[i] = str.charAt(i);
        }
        return ch;
	}
	
	
}
