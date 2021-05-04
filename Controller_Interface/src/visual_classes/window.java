package visual_classes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import useful_classes.osChange;
import javax.swing.JTextField;

public class window extends JFrame {
	osChange os = new osChange();
	MainPane mainPane = new MainPane();
	VirtualKeyboard key = new VirtualKeyboard(); 
	private JPanel contentPane;

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
		setBounds(0, 0, 136, 358);
		
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
		JPanel pan = new JPanel();
		pan.setBounds(0,0,1000,500);
		key.show(this,pan);
		
		contentPane.add(pan);
		//contentPane.add(mainPane);
		mainPane.setBounds(0,0, screenWidth,screenHeight);
		setContentPane(contentPane);
		
	}

}
