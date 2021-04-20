package visual_classes;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PrincipalPanel extends JPanel {
	JButton btnNewButton = new JButton("New button");
	/**
	 * Create the panel.
	 */
	public PrincipalPanel() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
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
		
		
		
		btnNewButton.setBounds(0, 0, 89, 23);
		add(btnNewButton);

	}
}
