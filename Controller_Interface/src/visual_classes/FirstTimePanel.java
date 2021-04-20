package visual_classes;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JPasswordField;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import useful_classes.Encryption;
import useful_classes.FileHandler;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class FirstTimePanel extends JPanel {
	JPasswordField password_fld;
	JPasswordField confirmation_fld;
	JButton show_password_img;
	JButton inicio_btn;
	boolean show_pass = false;
	ImageIcon conOn = new ImageIcon(FirstTimePanel.class.getResource("/icons/ojo_contrasena_on.png"));
	ImageIcon conOff = new ImageIcon(FirstTimePanel.class.getResource("/icons/ojo_contrasena_off.png"));
	MainPane main;

	/**
	 * Create the panel.
	 */
	public FirstTimePanel() {
		//Setting size parameters
		//Screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int)screenSize.getHeight();
		//Panel
		int panelWidth = 800;
		int panelHeight = screenHeight;
		int panelX=(screenWidth/2)-(panelWidth/2);
		int panelY=0;
		
		int componentsWidth = 300;
		int componentsHeight = 40;
		int gap = 10;
				
		//Setting this panel
		setOpaque(false);
		setBounds(panelX, panelY, panelWidth, panelHeight);
		//setBounds(0,0,400,250);
		setLayout(null);
		

		//Position and size parameters
		int componentsX = (panelWidth/2)-(componentsWidth/2);
		int passlblY = (getHeight()/2)-(componentsHeight*5+gap*2)/2;
		int passfldY = passlblY + componentsHeight;
		int conflblY = passfldY + componentsHeight + gap;
		int conffldY = conflblY + componentsHeight;
		int buttonsY = conffldY + componentsHeight+gap;
		int passFieldLimit = 16;
		
		//Password label
		JLabel password_lbl = new JLabel("Nueva contrase\u00F1a");
		password_lbl.setForeground(Color.WHITE);
		password_lbl.setFont(new Font("Alegreya Sans SC", Font.BOLD, 35));
		password_lbl.setHorizontalAlignment(SwingConstants.LEFT);
		//password_lbl.setBounds(28, 39, 320, 27);
		password_lbl.setBounds(componentsX, passlblY, componentsWidth, componentsHeight);
		add(password_lbl);
		
		//Password confirmation label
		JLabel password_confirmation_lbl = new JLabel("Confirmaci\u00F3n");
		password_confirmation_lbl.setForeground(Color.WHITE);
		password_confirmation_lbl.setHorizontalAlignment(SwingConstants.LEFT);
		password_confirmation_lbl.setFont(new Font("Alegreya Sans SC", Font.BOLD, 35));
		//password_confirmation_lbl.setBounds(29, 99, 254, 27);
		password_confirmation_lbl.setBounds(componentsX, conflblY, componentsWidth, componentsHeight);
		add(password_confirmation_lbl);
		
		//Password field
		password_fld = new JPasswordField(passFieldLimit);
		password_fld.setForeground(Color.WHITE);
		password_fld.setOpaque(false);
		password_fld.setFont(new Font("Tahoma", Font.PLAIN, 40));
		password_fld.setBorder(new LineBorder(Color.WHITE, 2, true));
		//password_fld.setBounds(28, 74, 155, 20);
		password_fld.setBounds(componentsX, passfldY, componentsWidth, componentsHeight);
		add(password_fld);
		
		//Confirmation field
		confirmation_fld = new JPasswordField(passFieldLimit);
		confirmation_fld.setForeground(Color.WHITE);
		confirmation_fld.setBorder(new LineBorder(new Color(255, 255, 255), 2, true));
		confirmation_fld.setOpaque(false);
		confirmation_fld.setFont(new Font("Tahoma", Font.PLAIN, 40));
		//confirmation_fld.setBounds(27, 124, 156, 25);
		confirmation_fld.setBounds(componentsX, conffldY, componentsWidth, componentsHeight);
		add(confirmation_fld);
		
		//Image icon and button parameters
		int scale = 15;
		int conOffWidth = (int)(conOff.getIconWidth()/scale);
		int conOffHeight = (int)(conOff.getIconHeight()/scale);
		int conOnWidth = (int)(conOn.getIconWidth()/scale);
		int conOnHeight = (int)(conOn.getIconHeight()/scale);
		
		conOn = new ImageIcon(conOn.getImage().getScaledInstance(conOnWidth, conOnHeight,java.awt.Image.SCALE_SMOOTH));
		conOff = new ImageIcon(conOff.getImage().getScaledInstance(conOffWidth, conOffHeight,java.awt.Image.SCALE_SMOOTH));
		
		//Show password button
		show_password_img = new JButton();
		show_password_img.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				show_pass = !show_pass;
				showPassword(show_pass,conOn,conOff);
			}
		});
		show_password_img.setContentAreaFilled(false);
		show_password_img.setBorder(null);
		show_password_img.setIcon(conOff);
		show_password_img.setBounds(componentsX, buttonsY, conOffWidth, conOffHeight);
		add(show_password_img);
		
		//Start button
		inicio_btn = new JButton("Inicio");
		inicio_btn.setFont(new Font("Alegreya Sans SC", Font.PLAIN, 25));
		int inicioBtnWidth = 150;
		int inicioBtnX = componentsX + componentsWidth/2 - inicioBtnWidth/2;
		inicio_btn.setBounds(inicioBtnX, buttonsY, inicioBtnWidth, componentsHeight);
		add(inicio_btn);
	}
	public void setMainPane(MainPane main) {
		this.main = main;
		setActions();
	}
	
	
	public void setActions() {
		inicio_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				boolean confirmed;
				main.dateAndHour.update();   //Originalmente estaba así: main.update(main.time);
				
				confirmed = passwordConfirmed(password_fld.getPassword(),confirmation_fld.getPassword());
				if(confirmed) {
					main.atribute.setup = false;
					main.menuNavegation.next(main.atribute);
					password_fld.setText("");
					confirmation_fld.setText("");
					//Habilitando la encriptación
					Encryption hash = new Encryption();
					String enc_pass = hash.sha1(password_fld.getPassword());
	
					//Guardando la contraseña
					FileHandler fl = new FileHandler();
					fl.setDirection("src\\sav\\");
					fl.setFilename("main_data.int");
					fl.writeFile("qzr ",false);
					fl.writeFileln(enc_pass,true);
				}
				else {
					password_fld.setText("");
					confirmation_fld.setText("");
				}
			}
		});
	}
	
	public void showPassword(boolean pass,ImageIcon on,ImageIcon off) {
		if(pass) {
			show_password_img.setIcon(on);
			password_fld.setEchoChar((char)0);
			confirmation_fld.setEchoChar((char)0);
		}
		else {
			show_password_img.setIcon(off);
			password_fld.setEchoChar('•');
			confirmation_fld.setEchoChar('•');
		}
	}
	
	public boolean passwordConfirmed(char[] pass,char [] confir) {
		   boolean confirmed = true;
		   if (pass.length != confir.length) {
		       confirmed = false;
		   } else {
			   confirmed = Arrays.equals(pass, confir);
		   }
		   return confirmed;
		}
}
