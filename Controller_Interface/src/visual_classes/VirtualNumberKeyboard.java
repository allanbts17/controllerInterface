package visual_classes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import useful_classes.osChange;

/**
 * A simple virtual keyboard in the Brazilian ABNT2 layout.
 *
 * In order to use this class you must:
 *
 * 1. Create a new instance providing the size of the virtual keyboard; <br>
 * 2. Provide a text component that will be used to store the the keys typed
 * (this is performed with a separate call to setCurrentTextComponent; <br>
 * 3. Call the show method in order to show the virtual keyboard in a given
 * JFrame.
 *
 * @author Wilson de Carvalho
 */
public class VirtualNumberKeyboard  extends JPanel{
	private int rowY = 0;
	private int alpha = 255;
	private JPanel keyPanel;
	private JLabel[] hourData = new JLabel[3];
	private int select=0;
	private String dataBuffer="";
	osChange os = new osChange();
    /**
     * Private class for storing key specification.
     */
	//Special keys
	private final String LEFT_ARROW = "<";
	private final String RIGHT_ARROW = ">";
	private final String UP_ARROW = "^";
	private final String DOWN_ARROW = "v";
	private final String CLEAR = "X";
	private final String READY = "Ok";
	
    // First key row
    private String[] row1 = new String[]{
        "7","8","9",UP_ARROW
    };
    
    // Second key row
    private String[] row2 = new String[]{
            "4","5","6",DOWN_ARROW
    };

    // Third key row
    private String[] row3 = new String[]{
            "1","2","3",CLEAR
    };

    // Fourth key row
    private String[] row4 = new String[]{
    		LEFT_ARROW,"0",RIGHT_ARROW,READY
    };

    private Component currentComponent;
    private JTextComponent lastFocusedTextComponent;
    private JFrame frame;
    private boolean isCapsLockPressed = false;
    private boolean isShiftPressed = false;
    private Color defaultColor;


    public VirtualNumberKeyboard() {
    	Dimension screenSize = os.setDimension();
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int)screenSize.getHeight();
		int keyPanWidth = 400;
		int keyPanHeight = 350;
		int keyPanX = screenWidth/2 - keyPanWidth/2;
		int keyPanY = screenHeight - keyPanHeight;
		setBackground(new Color(0,0,0,200));
		setBounds(keyPanX,keyPanY,keyPanWidth,keyPanHeight);	
		setLayout(null);
    }

    /**
     * Initializes the virtual keyboard and shows in the informed JFrame.
     *
     * @param frame JFrame that will be used to show the virtual keyboard.
     * @param keyboardPanel The panel where this keyboard will be held.
     * 
     */
    public void setParent(JLabel[] hourData) {
    	this.hourData = hourData;
        Color keyColor = getBackground();
        select=0;
        //keyColor = new Color(0,0,0,);
        add(initRow(row1, getSize(),keyColor));
        add(initRow(row2, getSize(),keyColor));
        add(initRow(row3, getSize(),keyColor));
        add(initRow(row4, getSize(),keyColor));
    }
        
    public void setAlpha(int alpha) {
    	alpha = Math.max(0,alpha);
    	alpha = Math.min(255,alpha);
    	this.alpha = alpha;
    }
    
    public Color getColorWithAlpha(Color color) {
    	Color colorWithAlpha;
    	colorWithAlpha = new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha);
    	return colorWithAlpha;
    }

    private JPanel initRow(String[] keys, Dimension dimensions, Color panelColor) {
    	int outerButtonsGap = 10;
    	int innerButtonsGap = 10;
    	
        JPanel p = new JPanel();
        p.setLayout(null);
        int buttonWidth = (dimensions.width - outerButtonsGap*2 - innerButtonsGap*(keys.length-1) ) / keys.length;
        int buttonHeight = (dimensions.height - outerButtonsGap*2 - innerButtonsGap*3) / 4; // number of rows
                
        int compensationButtonX = Math.round(dimensions.width-outerButtonsGap*2)/2-(buttonWidth*keys.length+innerButtonsGap*(keys.length-1))/2;
        for (int i = 0; i < keys.length; ++i) {
            String key = keys[i];
            JButton button;
            button = new JButton(key);
            int lineThickness = 2;
            
            Color btnLineColor = getColorWithAlpha(Color.WHITE);
            Color btnColor = getColorWithAlpha(Color.BLACK);
            Color btnTextColor = getColorWithAlpha(Color.WHITE);
            /*
            button.setBorder(new MatteBorder(lineThickness, lineThickness, lineThickness, lineThickness, (Color) btnLineColor));
            button.setForeground(btnTextColor);
            button.setBackground(btnColor);*/
            button.setBorder(new MatteBorder(lineThickness, lineThickness, lineThickness, lineThickness, (Color) Color.WHITE));
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(0,0,0,255));
            //button.setContentAreaFilled(true);
            //button.setOpaque(false);
            button.addMouseListener(new MouseAdapter() {
    			@Override
    			public void mousePressed(MouseEvent e) {
    				button.setBackground(new Color(255,255,255));
    			}
    			@Override
    			public void mouseReleased(MouseEvent e) {
    				button.setBackground(new Color(0,0,0));
    			}
    		});
            
            button.setFont(new Font("Tahoma", Font.PLAIN, 20));
            int buttonX = compensationButtonX+i*(buttonWidth+innerButtonsGap);
            button.setBounds(buttonX,0,buttonWidth, buttonHeight);
            //button.addFocusListener(this);
            button.addActionListener(e -> actionListener(key));
            
  
            p.add(button);
        }
        int pX = outerButtonsGap;
        int pY = outerButtonsGap+rowY;
        int pWidth = dimensions.width-outerButtonsGap*2;
        int pHeight = buttonHeight;
        p.setBounds(pX,pY,pWidth,pHeight);
       // p.setBackground(panelColor);
        p.setOpaque(false);
        rowY += pHeight + innerButtonsGap;
        return p;
    }

    private void actionListener(String key) {
    	int numData;
    	if(isNumeric(key)) {
	    	dataBuffer += key;
	    	writeData();
    	}
    	else {
    		switch(key) {
    		case UP_ARROW:
    			numData= Integer.parseInt(hourData[select].getText());
    			dataBuffer = String.valueOf(numData+1);
    			writeData();
    			break;
    		case DOWN_ARROW:
    			numData = Integer.parseInt(hourData[select].getText());
    			dataBuffer = String.valueOf(numData-1);
    			writeData();
    			break;
    		case LEFT_ARROW:
    			select=Math.max(select--,0);
    			dataBuffer="";
    			selectChange();
    			break;
    		case RIGHT_ARROW:
    			select=Math.min(select++,2);
    			dataBuffer="";
    			selectChange();
    			break;
    		case CLEAR:
    			hourData[select].setText("00");
    			dataBuffer="";
    			break;
    		case READY:
    			break;
    		}
    	}
    }
    private void writeData() {
    	int numData;
    	switch(select) {
    	case 0:
    		numData = Math.min(Integer.parseInt(dataBuffer),12);
        	numData = dataBuffer.length() > 1? Math.max(numData,1):Math.max(numData,0);
        	dataBuffer = String.valueOf(numData);
        	dataBuffer = dataBuffer.length() < 2? "0"+dataBuffer:dataBuffer;
        	hourData[select].setText(dataBuffer);
    		break;
    	case 1:
    		numData = Math.min(Integer.parseInt(dataBuffer),30);
        	numData = Math.max(numData,0);
        	dataBuffer = String.valueOf(numData);
        	dataBuffer = dataBuffer.length() < 2? "0"+dataBuffer:dataBuffer;
        	hourData[select].setText(dataBuffer);
    		break;
    	case 2:
    		break;	
    	}
    }
    
    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    private void selectChange() {
		
	}


}
