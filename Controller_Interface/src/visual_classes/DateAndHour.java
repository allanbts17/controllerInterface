package visual_classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import useful_classes.SendExecution;
import useful_classes.osChange;


public class DateAndHour extends JPanel {
	//Other atributes
	JLabel fecha;
	JLabel hora;
	Timer timer;
	int time = 20*1 + 60*3+60*60*1;
	String dateForCompare = "";
	String texto_hora;
	int texto_dia;
	String texto_mes;
	String texto_ano;
	String text_date;
	String mes[] = {"enero", "febrero", "marzo", "abril", 
			"mayo", "junio", "julio", "agosto", "septiembre", 
			"octubre", "noviembre", "diciembre"};
	boolean minuteChange = false;
	osChange os = new osChange();
	SendExecution sendExecution = new SendExecution();
	MainPane main;
	/**
	 * Create the panel.
	 */
	public DateAndHour() {
		Dimension screenSize = os.setDimension();
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int)screenSize.getHeight();
		
		//Texto fecha y hora
		int date_panel_w = 700;
		int label_h = 35;
		int date_gap = 10;
		int date_panel_h = label_h * 2;
		
		setOpaque(false);
		setBorder(null);
		setBounds(screenWidth - date_panel_w - date_gap, date_gap, date_panel_w, date_panel_h);
		
		getDate();
		fecha = new JLabel(text_date+", "+texto_dia+" de "+mes[Integer.parseInt(texto_mes)-1]);
		fecha.setForeground(Color.WHITE);
		fecha.setHorizontalAlignment(SwingConstants.RIGHT);
		fecha.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 30));
		fecha.setBounds(0, 0, date_panel_w, label_h);
		
		hora = new JLabel(texto_hora);
		hora.setForeground(Color.WHITE);
		hora.setHorizontalAlignment(SwingConstants.RIGHT);
		hora.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 30));
		hora.setBounds(0, label_h, date_panel_w, label_h);
		setLayout(null);
		add(fecha);
		add(hora);
	}
	
	public void setMainPane(MainPane main) {
		this.main = main;
		startTimer(time);
		time_date_update();
		
	}
	
	private void getDate() {
		Date date = new Date();
		
		//Hora
		DateFormat hourFormat = new SimpleDateFormat("hh:mm aa");
		minuteChange = !hourFormat.format(date).replace(String.valueOf((char)160),"").equals(texto_hora);
		texto_hora = hourFormat.format(date).replace(String.valueOf((char)160),"");
		
		//D�a
		DateFormat dayFormat = new SimpleDateFormat("d");
		texto_dia = Integer.parseInt(dayFormat.format(date));
		
		//Mes
		DateFormat dateFormat = new SimpleDateFormat("M");
		texto_mes = dateFormat.format(date);
		//System.out.println("dia: "+dayFormat.format(date)+", month: "+dateFormat.format(date));
		//A�o
		DateFormat yearFormat = new SimpleDateFormat("yyyy");
		texto_ano = yearFormat.format(date);
		
		//D�a semana
		DateFormat dayOfWeekFormat = new SimpleDateFormat("u");
		int weekCorrect = Integer.parseInt(dayOfWeekFormat.format(date))+1;
		weekCorrect = weekCorrect==8? 1:weekCorrect;
		text_date = diaSemana(weekCorrect);
		
		DateFormat forCompareHourFormat = new SimpleDateFormat("hh:mmaa");
		DateFormat forCompareDateFormat = new SimpleDateFormat("d-M-yyyy");
		String hourCompare = forCompareHourFormat.format(date).replace(String.valueOf((char)160),"");
		String dateCompare = forCompareDateFormat.format(date);
		
		dateForCompare = weekCorrect+";"+dateCompare+";"+hourCompare;
		//System.out.println(dateForCompare);
	}
	
	public void startTimer(int seg) {
	    TimerTask task = new TimerTask() {
	        public void run() {
	            main.save_screen_on = true;
				main.menuNavegation.screenSaver();
	        	System.out.println("Timer finished");
	        }
	    };
	    timer = new Timer("Timer");
	    
	    long delay = seg*1000L;
	    timer.schedule(task, delay);
	}
	
	public void update() {
		System.out.println("update");
		timer.cancel();
		timer.purge();
		startTimer(time);
	}
	
	private String diaSemana (int nD)
    {
		String letraD = "";		
        switch (nD){
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
        return letraD;
    }
	
	public void time_date_update() {
	    TimerTask repeatedTask = new TimerTask() {
	        public void run() {    	
	        	getDate();
	        	if(minuteChange) {
	        		System.out.println(minuteChange);
	        		main.sendExecution.compareDateStrings(dateForCompare);
	        		minuteChange=false;
	        	}
	    		fecha.setText(text_date+", "+texto_dia+" de "+mes[Integer.parseInt(texto_mes)-1]); 		
	    		hora.setText(texto_hora);
	    		
	    		
	        }
	    };
	    Timer timer = new Timer("Timer");
	    
	    long delay = 0L;
	    long period = 1000L;
	    timer.scheduleAtFixedRate(repeatedTask, delay, period);
	}

}
