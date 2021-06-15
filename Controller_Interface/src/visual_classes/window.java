package visual_classes;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import useful_classes.Movement;
import useful_classes.osChange;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.MatteBorder;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class window extends JFrame {
	osChange os = new osChange();
	MainPane mainPane;
	//VirtualKeyboard key = new VirtualKeyboard(); 
	private JPanel contentPane;
	private JTextField textField;
	private JPanel PaneWithText;
	private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window frame = new window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public window() {
		//Setting size parameters
		//Screen
		Dimension screenSize = os.setDimension();
		System.out.println(screenSize);
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int)screenSize.getHeight();
		
		
		

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 676, 628);
		
		if(!os.ifWindows()){
			setExtendedState(MAXIMIZED_BOTH);
			setUndecorated(true);
			//Cursor hiding
			setCursor(getToolkit().createCustomCursor(
		            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
		            "null"));
		}
		else
			setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-screenWidth/2,
					(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2-screenHeight/2,
					screenWidth+16, screenHeight+39);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setLayout(null);
		

		mainPane = new MainPane(this);
		
		//Movement m = new Movement();
		//Movement m1 = new Movement();
		
		
		contentPane.add(mainPane);
		mainPane.setBounds(0,0, screenWidth,screenHeight);
		setContentPane(contentPane);
	}
}
