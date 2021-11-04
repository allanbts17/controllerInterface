package useful_classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import com.fazecast.jSerialComm.*;

import visual_classes.MainPane;

public class SendExecution {
	MainPane main;
	public ArrayList<String> scheduledExecutionList;
	FileHandler fl = new FileHandler();
	public boolean playSong = false;
	public boolean arduinoExecution = false;
	String executionToSend;
	String extension;
	SerialPort arduinoPort;
	Player musicFilePlayer;
	Timer timer;
	Timer executionTimer;
	public boolean okMessage = false;
	ExecutionDurationHandler executionDurationHandler;
	//ExecutionHandler executionHandler = new ExecutionHandler();
	osChange os = new osChange();

	public SendExecution(){
		fl.setDirection("src/sav/");
		try {
			openSerialPort();
			initArduinoDataReception();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//executionHandler.setSerialPort(arduinoPort);
	}
	
	public void setScheduledExecutionList(ArrayList<String> executionList) {
		this.scheduledExecutionList = executionList;
	}
	
	private void playMelodiasByPhone(int melodyNumber) {
		FileHandler melodies = new FileHandler();
		melodies.setDirection("src/files/");
		String[] files = melodies.searchFiles(".mp3");
		for(String file: files) {
			System.out.println("Files: "+file);
		}
		System.out.println("melodyNumber: "+melodyNumber);
		if(melodyNumber == 0) {
			stopSong();
		}
		else if(melodyNumber <= files.length && !playSong) {
			String melodiaName = files[melodyNumber-1];
			try {
				prepareForExecution(melodiaName);
			} catch (FileNotFoundException | JavaLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println("no hay suficientes");
		}
	}
	
	public void compareDateStrings(String actualDateHour) throws FileNotFoundException, JavaLayerException {
		boolean coincide = false;
		boolean dateCoincide;
		boolean dayCoincide;
		boolean hourCoincide;
		//System.out.println("It is in compareDateStrings");

		for(int i=0;i<scheduledExecutionList.size();i++) {
			String[] scheduledParts = scheduledExecutionList.get(i).split(";");
			String[] actualDateHourParts = actualDateHour.split(";");
			boolean isDate = scheduledParts[0].contains("-");
			if(isDate) {
				//System.out.println("sheduledList: "+scheduledExecutionList.get(i));
				//System.out.println("actualhour: "+actualDateHour);
				dateCoincide = scheduledParts[0].equals(actualDateHourParts[1]);
				hourCoincide = scheduledParts[1].equals(actualDateHourParts[2]);
				coincide = dateCoincide && hourCoincide;
			}
			else {
				dayCoincide = scheduledParts[0].contains(actualDateHourParts[0]);
				hourCoincide = scheduledParts[1].equals(actualDateHourParts[2]);
				coincide = dayCoincide && hourCoincide;
			}
			if(coincide) {
				//System.out.println("it coincided");
				prepareForExecution(scheduledParts[2]);
				if(isDate) deleteExecution(i);
				//System.out.println("Actual data:");
				//System.out.println("executionToSend: "+executionToSend);
				//System.out.println("extension: "+extension);
				//System.out.println("File path: "+fl.getFilePath());
				coincide=false;
				break;
			}
		}
	}
	
	public void  prepareForExecution(String executionName) throws FileNotFoundException, JavaLayerException {
		executionToSend = executionName;
		System.out.println("To execute: "+executionToSend);
		setExtension();
		setDirection();
		setFilename();
		startExecution();
	}
	
	public void setMainPane(MainPane main) {
		this.main = main;
	}
	
	private void setExtension(){
		extension = executionToSend.split("\\.")[1];
	}

	private void setDirection(){
		fl.setDirection("src/files/");
	}

	private void setFilename(){
		fl.setFilename(executionToSend);
		System.out.println("Filename: "+executionToSend);
	}

	private void startExecution() throws FileNotFoundException, JavaLayerException{
		main.principalPane.placeBtns(true);
		if(!playSong && !arduinoExecution)
			if(extension.equals("mp3")){
				//stopSong();
				//if(!playSong)
					playSong();
			} else {
				//System.out.println("Its gonna send to Arduino");
				//if(!arduinoExecution) {
					arduinoExecution = true;
					sendToArduino();
				//}
			}
	}

	private void deleteExecution(int index) {
		fl.setDirection("src/sav");
		switch(extension){
			case "mp3":
				fl.setFilename("melodias.int");
				break;
			case "toc":
				fl.setFilename("toques.int");
				break;
			case "sec":
				fl.setFilename("secuencias.int");
				break;
		}
		String sheduledExecution = scheduledExecutionList.get(index);
		String[] fileLines;
		
		fileLines = fl.readFileLine();
		fl.writeFile("",false);
		for(String line: fileLines) {
			if(!line.equals(sheduledExecution)) {
				fl.writeFileln(line,true);
			}
		}
		main.principalPane.fillNameList();
		main.principalPane.reset(false);
		main.principalPane.repaint();
		
		for(String name: scheduledExecutionList)
			System.out.println(name);
	}
	
	private void openSerialPort() throws IOException{
		String port = os.ifWindows()? "COM4":"/dev/ttyUSB0";
	    arduinoPort = SerialPort.getCommPort(port);
		System.out.println("Selected port name: "+arduinoPort.getSystemPortName()+": "+arduinoPort.getDescriptivePortName());
		arduinoPort.setComPortParameters(9600, 8, 1, 0);
		arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
		
		arduinoPort.openPort();
	    if (arduinoPort.isOpen()) {
	    	System.out.println("Port initialized!");
	    } else {
	    	System.out.println("Port not available");
	    }
	    
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
	}
	
	private void initArduinoDataReception() {
		arduinoPort.addDataListener(new SerialPortDataListener() {
		      @Override
		      public int getListeningEvents() {
		        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
		      }
		      
		      //Mensajes enviados por el arduino
		      public void serialEvent(SerialPortEvent event) {
		        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
		        return;
		        byte[] newData = new byte[arduinoPort.bytesAvailable()];
		        int numRead = arduinoPort.readBytes(newData, newData.length);
		        
		        if(newData[0]=='f') {
		        	arduinoExecution = false;
		        	main.principalPane.placeBtns(false); 
		        	System.out.println((char)newData[0]);
		        }
		        else if(newData[0]=='?'){
		        	okMessage = true;
		        	System.out.println("Received ? from arduino");
		        }
		        else {
			        int num=Character.getNumericValue(newData[0]);
			        System.out.println("Bytes number: "+numRead);
			        System.out.println("Data: "+num);
			        
			        playMelodiasByPhone(num);
		        }
		      }
		    });
	}
	
	private void sendToArduino() {
		String[] fileLines;
		fileLines = fl.readFileLine();
		System.out.println("Sending to arduino");
		executionDurationHandler = new ExecutionDurationHandler();
		executionDurationHandler.setExecutionLines(fileLines);
		long executionDuration = executionDurationHandler.getDuration();
		System.out.println("Total duration: "+executionDuration);
		startExecutionTimer(executionDuration);

		for(String line: fileLines){
			
			if(line.contains("#"))
				line = line.replace("#","");
			//System.out.print(line+" ");
			byte[] byteArray = line.getBytes();
			System.out.println(line);
			arduinoPort.writeBytes(byteArray,byteArray.length);
			//arduinoVerify(byteArray);
		}
		
	}
	
	/*public void testArduino(String name) {
		
		fl.setDirection("src/files");
		fl.setFilename(name);
		String[] fileLines = fl.readFileLine();
		
		executionHandler.setFileLines(fileLines);
		executionHandler.execute();
	}*/
	

	public void playSong() throws FileNotFoundException, JavaLayerException {
		FileInputStream relative = new FileInputStream(fl.getFilePath());
		musicFilePlayer = new Player(relative);
		playSong = true;
	      new Thread() {
	          public void run() {
					try {						
						while(playSong) {
							musicFilePlayer.play(1);
						}	
					} catch (JavaLayerException e) {
					   e.printStackTrace();
					}
	          }
	       }.start();      
	}
	public void stopArduinoExecution() {
		if(arduinoExecution) {
			byte[] byteArrray = new byte[1];
			byteArrray[0] = 's';
			arduinoExecution = false; //
        	main.principalPane.placeBtns(false);
			System.out.println(byteArrray[0]);
			//arduinoVerify(byteArrray);
			arduinoPort.writeBytes(byteArrray,byteArrray.length);
			//arduinoVerify();
		}
	}
	
	public void arduinoVerify() {
		byte[] byteArray = new byte[1];
		byteArray[0] = '?';
		arduinoPort.writeBytes(byteArray,byteArray.length);
		startTimer(500L);
	}
	
	public void startTimer(long mili) {
	    TimerTask task = new TimerTask() {
	        public void run() {
	            if(okMessage) {
	            	okMessage = false;
	            	System.out.println("Arduino says ok");
	            }
	            else {
	            	System.out.println("Reseting arduino");
	            	main.resetArduino();
	            }
	        }
	    };
	    timer = new Timer("Timer");
	    
	    long delay = mili;
	    timer.schedule(task, delay);
	}
	
	public void startTimer(long mili,byte[] byteArray) {
	    TimerTask task = new TimerTask() {
	        public void run() {
	            if(okMessage) {
	            	okMessage = false;
	            	arduinoPort.writeBytes(byteArray,byteArray.length);
	            }
	            else {
	            	main.resetArduino();
	            	startTimer(3000L,byteArray); //Por ahora no verifica
	            }
	        }
	    };
	    timer = new Timer("Timer");
	    
	    long delay = mili;
	    timer.schedule(task, delay);
	}
	
	public void startExecutionTimer(long mili) {
	    TimerTask task = new TimerTask() {
	        public void run() {
	        	//stopArduinoExecution();
	        	byte[] byteArrray = new byte[1];
				byteArrray[0] = 's';
				arduinoExecution = false; //
	        	main.principalPane.placeBtns(false);
				System.out.println(byteArrray[0]);
				//arduinoVerify(byteArrray);
				arduinoPort.writeBytes(byteArrray,byteArrray.length);
	        }
	    };
	    timer = new Timer("Timer");
	    
	    long delay = mili;
	    timer.schedule(task, delay);
	}
	
	public void clockPulseA() {
		byte[] byteArrray = new byte[1];
		byteArrray[0] = 'A';
		System.out.println((char)byteArrray[0]);
		arduinoPort.writeBytes(byteArrray,byteArrray.length);
	}
	
	public void backlightOn() {
		byte[] byteArrray = new byte[1];
		byteArrray[0] = 'L';
		arduinoPort.writeBytes(byteArrray,byteArrray.length);
	}
	
	public void backlightOff() {
		byte[] byteArrray = new byte[1];
		byteArrray[0] = 'l';
		arduinoPort.writeBytes(byteArrray,byteArrray.length);
	}
	
	public void stopSong() {
		System.out.println("entré ");
		System.out.print(playSong);
		if(playSong) {
			playSong = false;
			musicFilePlayer.close();
			main.principalPane.placeBtns(false);
		}
	}

}