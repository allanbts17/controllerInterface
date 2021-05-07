package visual_classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import useful_classes.*;

import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class StartSessionPanel extends JPanel {
	JLabel password_lbl;
	JPasswordField password_fld;
	JButton show_password_img;
	JButton inicio_btn;
	boolean show_pass = false;
	ImageIcon conOn = new ImageIcon(StartSessionPanel.class.getResource("/icons/ojo_contrasena_on.png"));
	ImageIcon conOff = new ImageIcon(StartSessionPanel.class.getResource("/icons/ojo_contrasena_off.png"));
	MainPane main;
	FileHandler passRead = new FileHandler();
	osChange os = new osChange();
	
	//Position and size parameters
	int componentsX;
	int passlblY;
	int passfldY;
	int buttonsY;
	int passFieldLimit;
	
	int scale = 15;
	int conOffWidth = (int)(conOff.getIconWidth()/scale);
	int conOffHeight = (int)(conOff.getIconHeight()/scale);
	int conOnWidth = (int)(conOn.getIconWidth()/scale);
	int conOnHeight = (int)(conOn.getIconHeight()/scale);
	
	int inicioBtnWidth;
	int inicioBtnX;
	
	int componentsWidth = 300;
	int componentsHeight = 40;
	int gap = 10;
	
	private boolean componentsUp = false;
	/**
	 * Create the panel.
	 */
	public StartSessionPanel() {
		//Setting size parameters
		//Screen
		Dimension screenSize = os.setDimension();
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int)screenSize.getHeight();
		
		//Panel
		int panelWidth = 800;
		int panelHeight = screenHeight;
		int panelX=(screenWidth/2)-(panelWidth/2);
		int panelY=0;
			
		//Setting this panel
		setOpaque(false);
		setBounds(panelX, panelY, panelWidth, panelHeight);
		setLayout(null);
		

		//Position and size parameters
		componentsX = (panelWidth/2)-(componentsWidth/2);
		passlblY = (getHeight()/2)-(componentsHeight*3+gap)/2;
		passfldY = passlblY + componentsHeight;
		buttonsY = passfldY + componentsHeight+gap;
		passFieldLimit = 16;
		
		//Password label
		password_lbl = new JLabel("Ingrese la contrase\u00F1a");
		password_lbl.setForeground(Color.WHITE);
		password_lbl.setFont(new Font("Alegreya Sans SC", Font.BOLD, 30));
		password_lbl.setHorizontalAlignment(SwingConstants.LEFT);
		password_lbl.setBounds(componentsX, passlblY, componentsWidth, componentsHeight);
		add(password_lbl);
					
		//Password field
		password_fld = new JPasswordField(passFieldLimit);											
		password_fld.setForeground(Color.WHITE);
		password_fld.setOpaque(false);
		password_fld.setFont(new Font("Tahoma", Font.PLAIN, 40));
		password_fld.setBorder(new LineBorder(Color.WHITE, 2, true));
		password_fld.setBounds(componentsX, passfldY, componentsWidth, componentsHeight);
		add(password_fld);
		
		conOn = new ImageIcon(conOn.getImage().getScaledInstance(conOnWidth, conOnHeight,java.awt.Image.SCALE_SMOOTH));
		conOff = new ImageIcon(conOff.getImage().getScaledInstance(conOffWidth, conOffHeight,java.awt.Image.SCALE_SMOOTH));
		
		//Show password button
		show_password_img = new JButton();
		show_password_img.setContentAreaFilled(false);
		show_password_img.setBorder(null);
		show_password_img.setIcon(conOff);
		show_password_img.setBounds(componentsX, buttonsY, conOffWidth, conOffHeight);
		add(show_password_img);
		
		//Start button
		inicio_btn = new JButton("Inicio");		
		inicio_btn.setFont(new Font("Alegreya Sans SC", Font.PLAIN, 25));
		inicioBtnWidth = 150;
		inicioBtnX = componentsX + componentsWidth/2 - inicioBtnWidth/2;
		inicio_btn.setBounds(inicioBtnX, buttonsY, inicioBtnWidth, componentsHeight);
		add(inicio_btn);		
	}
	
	public void setMainPane(MainPane main) {
		this.main = main;
		passRead = this.main.passRead;
		setActions();
	}
	
	public void moveComponents() {
		componentsUp = !componentsUp;
		int move = (componentsUp)? -200:0;
		
		password_lbl.setBounds(componentsX, passlblY+move, componentsWidth, componentsHeight);
		password_fld.setBounds(componentsX, passfldY+move, componentsWidth, componentsHeight);
		show_password_img.setBounds(componentsX, buttonsY+move, conOffWidth, conOffHeight);
		inicio_btn.setBounds(inicioBtnX, buttonsY+move, inicioBtnWidth, componentsHeight);
	}
	
	private void setActions() {
		inicio_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("clicked");
				boolean correct;
				main.dateAndHour.update();
				
				//Habilitando la encriptación
				Encryption hash = new Encryption();
				String enc_pass = hash.sha1(password_fld.getPassword());
				String[] passLine = passRead.readFileLine(1).trim().split("\\s+");
				
				correct = passwordCorrect(stringToChar(passLine[1]),stringToChar(enc_pass));
				if(correct) {
					System.out.println("correct");
					main.atribute.setup = false;
					main.menuNavegation.next(main.atribute);
					password_fld.setText("");
				}
				else
					password_fld.setText("");					
			}
		});
		
		password_fld.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				main.dateAndHour.update();
			}
		    });
		
		show_password_img.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				main.dateAndHour.update();
				show_pass = !show_pass;
				showPassword(show_pass,conOn,conOff);
			}
		});
	}
	
	public void showPassword(boolean pass,ImageIcon on,ImageIcon off) {
		if(pass) {
			show_password_img.setIcon(on);
			password_fld.setEchoChar((char)0);
		}
		else {
			show_password_img.setIcon(off);
			password_fld.setEchoChar('•');
		}
	}
	
	public boolean passwordCorrect(char[] pass,char [] key) {
		   boolean correct = true;
		   if (pass.length != key.length) {
		       correct = false;
		   } else {
			   correct = Arrays.equals(pass, key);
		   }
		   return correct;
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
