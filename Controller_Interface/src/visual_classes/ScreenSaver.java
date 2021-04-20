package visual_classes;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

public class ScreenSaver extends JPanel {

	/**
	 * Create the panel.
	 */
	public ScreenSaver() {
		//Setting size parameters
		//Screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int)screenSize.getHeight();
		
		setBounds(0,0,screenWidth,screenHeight);
		setBackground(Color.BLACK);
		
	}

}
