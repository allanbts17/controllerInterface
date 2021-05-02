package visual_classes;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import useful_classes.osChange;

import java.awt.Color;
import java.awt.Font;

public class SelectDatePanel extends JPanel {
	MainPane main;
	JPanel calendar = new JPanel();
	JLabel calendarBase = new JLabel();
	JPanel calendarDay = new JPanel();
	JPanel calendarTitle = new JPanel();
	JLabel calendarMonthYear = new JLabel();
	JLabel leftArrow = new JLabel();
	JLabel rightArrow = new JLabel();
	int actualMonth;
	int actualDay;
	String actualYear;
	
	//JLabel calendarDayLabel = new JLabel();
	ArrayList<JPanel> calendarDays = new ArrayList<JPanel>();
	
	//CalendarDayConfig
	float scale = 3f;
	int sideGap = (int)Math.round(60/scale);
	int upperGap = (int)Math.round(403/scale);
	int horizontalGap = (int)Math.round(24/scale);
	int verticalGap = (int)Math.round(20/scale);
	//TItle
	String DateTitleText;
	
	//Days
	int day = 15;
	int month = 1;
	int year = 2021;
	int numberOfDays = daysInAMonth(month,year);
	int lastMonthNumberOfDays = daysInAMonth(previousMonth(month),year);
	int startingWeekDay = weekDay(1,month,year);
	int endingWeekDay = weekDay(daysInAMonth(month,year),month,year);
	int daysBeforeMonth = startingWeekDay-1;
	int daysAfterMonth = 7-endingWeekDay;
	int dayCount = 1;
	int otherMonthDayCount = 0;
	int[] otherMonthDays;
	osChange os = new osChange();

	/**
	 * Create the panel.
	 */
	public SelectDatePanel() {
		//Setting size parameters
		//Screen
		Dimension screenSize = os.setDimension();
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int)screenSize.getHeight();
		//Panel
		int panelWidth = 1000;
		int panelHeight = screenHeight;
		int panelX=(screenWidth/2)-(panelWidth/2);
		int panelY=0;
		
		//Setting this panel
		setOpaque(false);
		setBounds(panelX, panelY, panelWidth, panelHeight);
		setLayout(null);
		setBackground(Color.RED);
		
		//Icons and scale
		ImageIcon calendarBaseIco = new ImageIcon(this.getClass().getResource("/icons/calendar_base.png"));
		ImageIcon calendarDayUnpressedIco = new ImageIcon(this.getClass().getResource("/icons/calendar_day_unpressed.png"));
		ImageIcon calendarDayPressedIco = new ImageIcon(this.getClass().getResource("/icons/calendar_day_pressed.png"));
		ImageIcon calendarDayOtherIco = new ImageIcon(this.getClass().getResource("/icons/calendar_day_other.png"));
		ImageIcon calendarLeftArrowUnpressedIco = new ImageIcon(this.getClass().getResource("/icons/left_arrow_unpressed.png"));
		ImageIcon calendarRightArrowUnpressedIco = new ImageIcon(this.getClass().getResource("/icons/right_arrow_unpressed.png"));
		
		
		int calendarBaseWidth = (int)Math.round(calendarBaseIco.getIconWidth()/scale);
		int calendarBaseHeight = (int)Math.round(calendarBaseIco.getIconHeight()/scale);
		calendarBaseIco = new ImageIcon(calendarBaseIco.getImage()
				.getScaledInstance(calendarBaseWidth, calendarBaseHeight,java.awt.Image.SCALE_SMOOTH));
		
		int calendarDayWidth = (int)Math.round(calendarDayUnpressedIco.getIconWidth()/scale);
		int calendarDayHeight = (int)Math.round(calendarDayUnpressedIco.getIconHeight()/scale);
		calendarDayUnpressedIco = new ImageIcon(calendarDayUnpressedIco.getImage()
				.getScaledInstance(calendarDayWidth, calendarDayHeight,java.awt.Image.SCALE_SMOOTH));
		calendarDayPressedIco = new ImageIcon(calendarDayPressedIco.getImage()
				.getScaledInstance(calendarDayWidth, calendarDayHeight,java.awt.Image.SCALE_SMOOTH));
		calendarDayOtherIco = new ImageIcon(calendarDayOtherIco.getImage()
				.getScaledInstance(calendarDayWidth, calendarDayHeight,java.awt.Image.SCALE_SMOOTH));
		
		int calendarLeftArrowUnpressedWidth = (int)Math.round(calendarLeftArrowUnpressedIco.getIconWidth()/scale);
		int calendarLeftArrowUnpressedHeight = (int)Math.round(calendarLeftArrowUnpressedIco.getIconHeight()/scale);
		calendarLeftArrowUnpressedIco = new ImageIcon(calendarLeftArrowUnpressedIco.getImage()
				.getScaledInstance(calendarLeftArrowUnpressedWidth, calendarLeftArrowUnpressedHeight,java.awt.Image.SCALE_SMOOTH));
		int calendarRightArrowUnpressedWidth = (int)Math.round(calendarRightArrowUnpressedIco.getIconWidth()/scale);
		int calendarRightArrowUnpressedHeight = (int)Math.round(calendarRightArrowUnpressedIco.getIconHeight()/scale);
		calendarRightArrowUnpressedIco = new ImageIcon(calendarRightArrowUnpressedIco.getImage()
				.getScaledInstance(calendarRightArrowUnpressedWidth, calendarRightArrowUnpressedHeight,java.awt.Image.SCALE_SMOOTH));
		
		
		//CalendarBase config
		calendarBase.setIcon(calendarBaseIco);
		calendarBase.setBounds(0,0,calendarBaseWidth,calendarBaseHeight);
		
		//Update date info
		getActualDate();
		setDateTitleText();
		//Title config
		int titleElementsY = 15;
		int firstX=13;
		int secondX = firstX+calendarLeftArrowUnpressedWidth+12;
		int thirdX = secondX+220+12;
		leftArrow.setIcon(calendarLeftArrowUnpressedIco);
		rightArrow.setIcon(calendarRightArrowUnpressedIco);
		leftArrow.setBounds(firstX,titleElementsY,calendarLeftArrowUnpressedWidth,calendarLeftArrowUnpressedHeight);		
		rightArrow.setBounds(thirdX,titleElementsY,calendarRightArrowUnpressedWidth,calendarRightArrowUnpressedHeight);
		calendarMonthYear.setBounds(secondX,titleElementsY,220,50);
		calendarMonthYear.setText(DateTitleText);
		calendarMonthYear.setFont(new Font("Source Sans Pro", Font.BOLD, 50));
		//calendarMonthYear.setFont(new Font("Bebas", Font.PLAIN, 60));
		//calendarMonthYear.setOpaque(true);
		calendarMonthYear.setBackground(Color.BLUE);
		
		
		
		calendarTitle.add(leftArrow);
		calendarTitle.add(rightArrow);
		calendarTitle.add(calendarMonthYear);
		
		calendarTitle.setBounds(0,0,400,80);
		calendarTitle.setLayout(null);
		calendarTitle.setOpaque(false);
		
		//Days
		/*System.out.println("last month number of days: "+lastMonthNumberOfDays);
		System.out.println("starting week day: "+startingWeekDay);
		System.out.println("endingWeekDay: "+endingWeekDay);
		System.out.println("daysBeforeMonth: "+daysBeforeMonth);
		System.out.println("daysAfterMonth: "+daysAfterMonth);*/
		
		boolean startToWriteDay = false;
		monthCalendarDataSetting();	
		otherMonthDaysFill();
			
		for(int row=0;row<5;row++) {
			for(int column=0;column<7;column++) {
				JButton dayBtn = new JButton();
				dayBtn.setBounds(0,0,calendarDayWidth,calendarDayHeight);
				
				dayBtn.setBorder(null);
				dayBtn.setContentAreaFilled(false);
				
				JLabel calendarDayLabel = new JLabel();
				calendarDayLabel.setFont(new Font("Alegreya Sans SC Medium", Font.PLAIN, 25));
				//calendarDayLabel.setBackground(Color.WHITE);
				//calendarDayLabel.setOpaque(true);
				calendarDayLabel.setBounds(8,50,30,20);
				
				if(row==0 && column == startingWeekDay-1) startToWriteDay = true; 
				else if (dayCount > numberOfDays) startToWriteDay = false;
				
				if(startToWriteDay) {
					dayBtn.setIcon(calendarDayUnpressedIco);
					dayBtn.setPressedIcon(calendarDayPressedIco);
					calendarDayLabel.setText(Integer.toString(dayCount));
					dayCount++;
				}
				else {
					dayBtn.setIcon(calendarDayOtherIco);
					calendarDayLabel.setText(Integer.toString(otherMonthDays[otherMonthDayCount]));
					calendarDayLabel.setForeground(Color.WHITE);
					otherMonthDayCount++;
				}
				
				JPanel dayPane = new JPanel();
				int x = sideGap+column*(calendarDayWidth+horizontalGap);
				int y = upperGap+row*(calendarDayHeight+verticalGap);
				dayPane.setBounds(x,y,calendarDayWidth,calendarDayHeight);
				dayPane.setLayout(null);
				dayPane.setOpaque(false);
				
				dayPane.add(calendarDayLabel);
				dayPane.add(dayBtn);
				
				
				calendarDays.add(dayPane);
				calendar.add(dayPane);
				
				
			}
		}
		calendar.setBackground(Color.BLUE);
		calendar.setLayout(null);
		calendar.setOpaque(false);
		calendar.add(calendarTitle);
		calendar.add(calendarBase);
		
		calendar.setBounds((panelWidth/2)-(calendarBaseWidth/2),130,calendarBaseWidth,calendarBaseHeight);
		add(calendar);
		
	}
	
	public void setMainPane(MainPane main) {
		this.main = main;
		setActions();
	}
	
	public void setActions() {
		
	}
	private void monthCalendarDataSetting() {
		numberOfDays = daysInAMonth(actualMonth,Integer.parseInt(actualYear));
		lastMonthNumberOfDays = daysInAMonth(previousMonth(actualMonth),Integer.parseInt(actualYear));
		startingWeekDay = weekDay(1,actualMonth,Integer.parseInt(actualYear));
		endingWeekDay = weekDay(daysInAMonth(actualMonth,year),actualMonth,Integer.parseInt(actualYear));
		daysBeforeMonth = startingWeekDay-1;
		daysAfterMonth = 7-endingWeekDay;
		dayCount = 1;
		otherMonthDayCount = 0;
	}
	private void otherMonthDaysFill() {
		otherMonthDays = new int[daysBeforeMonth+daysAfterMonth];
		for(int i=0;i<daysBeforeMonth;i++) {
			otherMonthDays[i] = lastMonthNumberOfDays-daysBeforeMonth+i+1;
		}
		for(int i=0;i<daysAfterMonth;i++) {
			otherMonthDays[daysBeforeMonth+i] = i+1;
		}
	}
	private void getActualDate() {
		Date date = new Date();
		
		//D�a
		DateFormat dayFormat = new SimpleDateFormat("dd");
		actualDay = Integer.parseInt(dayFormat.format(date));
		
		//Mes
		DateFormat monthFormat = new SimpleDateFormat("MM");
		actualMonth = Integer.parseInt(monthFormat.format(date));

		//A�o
		DateFormat yearFormat = new SimpleDateFormat("yyyy");
		actualYear = yearFormat.format(date);

	}
	
	private void setDateTitleText() {
		String month = "";
		switch(actualMonth) {
		case 1:
			month = "ENE.";
			break;
		case 2:
			month = "FEB.";
			break;
		case 3:
			month = "MAR.";
			break;
		case 4:
			month = "ABR.";
			break;
		case 5:
			month = "MAY.";
			break;
		case 6:
			month = "JUN.";
			break;
		case 7:
			month = "JUL.";
			break;
		case 8:
			month = "AGO.";
			break;
		case 9:
			month = "SEP.";
			break;
		case 10:
			month = "OCT.";
			break;
		case 11:
			month = "NOV.";
			break;
		case 12:
			month = "DIC.";
			break;
		default:
			month="MES";
		}
		DateTitleText = month+" "+actualYear;
	}
	
	int previousMonth(int month) {
		int prevMonth = month-1;
		if(prevMonth == 0)
			prevMonth = 12;
		return prevMonth;
	}
	int weekDay (int dia, int mes, int ano)
    {
        String letraD="";
        TimeZone timezone = TimeZone.getDefault();
        Calendar calendar = new GregorianCalendar(timezone);
        calendar.set(ano, mes-1, dia);
        int nD=calendar.get(Calendar.DAY_OF_WEEK);
     
        /*switch (nD){
            case 1: letraD = "Domingo";
                break;
            case 2: letraD = "Lunes";
                break;
            case 3: letraD = "Martes";
                break;
            case 4: letraD = "Miercoles";
                break;
            case 5: letraD = "Jueves";
                break;
            case 6: letraD = "Viernes";
                break;
            case 7: letraD = "S�bado";
                break;
        }
       	*/
        return nD;
    }
	public static int daysInAMonth(int mes, int ano){
		mes--;
        switch(mes){
            case 0:  // Enero
            case 2:  // Marzo
            case 4:  // Mayo
            case 6:  // Julio
            case 7:  // Agosto
            case 9:  // Octubre
            case 11: // Diciembre
                return 31;
            case 3:  // Abril
            case 5:  // Junio
            case 8:  // Septiembre
            case 10: // Noviembre
                return 30;
            case 1:  // Febrero
                if ( ((ano%100 == 0) && (ano%400 == 0)) ||
                        ((ano%100 != 0) && (ano%  4 == 0))   )
                    return 29;  // A�o Bisiesto
                else
                    return 28;
            default:
                throw new java.lang.IllegalArgumentException(
                "El mes debe estar entre 0 y 11");
        }
}

}
