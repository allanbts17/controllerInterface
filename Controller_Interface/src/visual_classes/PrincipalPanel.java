package visual_classes;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;

import useful_classes.osChange;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PrincipalPanel extends JPanel {
	JButton btnNewButton = new JButton("New button");
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
		int panelWidth = 800;
		int panelHeight = screenHeight;
		int panelX=(screenWidth/2)-(panelWidth/2);
		int panelY=0;
		//Setting this panel
		setOpaque(false);
		setBounds(panelX, panelY, panelWidth, panelHeight);
		setLayout(null);
		
		
		
		btnNewButton.setBounds(0, 0, 200, 70);
		add(btnNewButton);

	}
	
	public void setMainPane(MainPane main) {
		this.main = main;
		setActions();
	}
	
	private void setActions() {
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				main.menuNavegation.next(main.atribute);
			}
		});
	}
}
