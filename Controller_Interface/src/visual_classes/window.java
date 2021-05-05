package visual_classes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import useful_classes.osChange;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.MatteBorder;
import java.awt.GridLayout;
import java.awt.Font;

public class window extends JFrame {
	osChange os = new osChange();
	MainPane mainPane = new MainPane();
	VirtualKeyboard key = new VirtualKeyboard(); 
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
		}
		else
			setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-screenWidth/2,
					(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2-screenHeight/2,
					screenWidth+16, screenHeight+39);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setLayout(null);
		
		//JPanel panCon = new JPanel();
		//panCon.setBounds(10, 10, 1020, 420);
		//panCon.setLayout(null);
		//panCon.setBackground(Color.BLACK);
		
		JPanel pan = new JPanel();
		pan.setBackground(Color.BLACK);
		//pan.setOpaque(false);
		pan.setBounds(10,10,1000,350);
		//pan.setBackground(Color.BLUE);
		
		contentPane.add(pan);
		//contentPane.add(mainPane);
		mainPane.setBounds(0,0, screenWidth,screenHeight);
		setContentPane(contentPane);
		
		key.show(this,pan);
		
		btnNewButton = new JButton("New button");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.WHITE));
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(Color.BLACK);
		btnNewButton.setLocation(20, 440);
		btnNewButton.setSize(100, 30);
		contentPane.add(btnNewButton);
		
		PaneWithText = new JPanel();
		PaneWithText.setBounds(0, 500, 600, 25);
		contentPane.add(PaneWithText);
		PaneWithText.setLayout(new GridLayout(0, 1, 0, 0));
		
		textField = new JTextField();
		PaneWithText.add(textField);
		textField.setVisible(true);
		textField.setColumns(10);
		
		
		//contentPane.add(panCon);
		
		//Incluir: & % # $ !�?� * -> ; : Tab SpaceBar no son muy necesarios
	}
}
