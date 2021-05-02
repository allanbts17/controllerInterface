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

import useful_classes.osChange;



public class DateAndHour extends JPanel {
	//Other atributes
	JLabel fecha;
	JLabel hora;
	Timer timer;
	int time = 20*1 + 60*3+60*60*1;
	String texto_hora;
	int texto_dia;
	String texto_mes;
	String texto_ano;
	String text_date;
	String mes[] = {"enero", "febrero", "marzo", "abril", 
			"mayo", "junio", "julio", "agosto", "septiembre", 
			"octubre", "noviembre", "diciembre"};
	osChange os = new osChange();
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
		fecha.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 30));
		fecha.setBounds(0, 0, date_panel_w, label_h);
		
		hora = new JLabel(texto_hora);
		hora.setForeground(Color.WHITE);
		hora.setHorizontalAlignment(SwingConstants.RIGHT);
		hora.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 30));
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
		DateFormat hourFormat = new SimpleDateFormat("h:mm aa");
		texto_hora = hourFormat.format(date);
		
		//Día
		DateFormat dayFormat = new SimpleDateFormat("dd");
		texto_dia = Integer.parseInt(dayFormat.format(date));
		
		//Mes
		DateFormat dateFormat = new SimpleDateFormat("MM");
		texto_mes = dateFormat.format(date);
		
		//Año
		DateFormat yearFormat = new SimpleDateFormat("yyyy");
		texto_ano = yearFormat.format(date);
		
		//Hora
		DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd");
		//Día semana
		text_date = diaSemana(texto_dia,
									Integer.parseInt(texto_mes),
									Integer.parseInt(texto_ano));
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
	
	private String diaSemana (int dia, int mes, int ano)
    {
        String letraD="";
        TimeZone timezone = TimeZone.getDefault();
        Calendar calendar = new GregorianCalendar(timezone);
        calendar.set(ano, mes-1, dia);
        int nD=calendar.get(Calendar.DAY_OF_WEEK);
     
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
            case 7: letraD = "Sábado";
                break;
        }

        return letraD;
    }
	
	public void time_date_update() {
	    TimerTask repeatedTask = new TimerTask() {
	        public void run() {    	
	        	getDate();
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
