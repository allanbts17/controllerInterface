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
	FileHandler prevFl = new FileHandler();
	public boolean playSong = false;
	public boolean bellExecution = false;
	String executionToSend;
	String extension;
	//SerialPort arduinoPort;
	Player musicFilePlayer;
	Timer timer;
	Timer executionTimer;
	public boolean okMessage = false;
	private boolean waitingForArduino = false;
	ExecutionDurationHandler executionDurationHandler;
	public boolean playPrev = true;
	ExecutionHandler executionHandler = new ExecutionHandler();
	osChange os = new osChange();

	public SendExecution(){
		fl.setDirection("src/sav/");
		prevFl.setDirection("src/files");
		prevFl.setFilename("Ángelus.mp3");
		/*try {
			openSerialPort();
			initArduinoDataReception();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//executionHandler.setSerialPort(arduinoPort);
	}
	
	public void setScheduledExecutionList(ArrayList<String> executionList) {
		this.scheduledExecutionList = executionList;
	}
	
	private void playMelodiasByPhone(int melodyNumber) {
		FileHandler melodies = new FileHandler();
		melodies.setDirection("src/files/");
		String[] files = melodies.searchFiles(".mp3");
		/*for(String file: files) {
			System.out.println("Files: "+file);
		}*/
		//System.out.println("melodyNumber: "+melodyNumber);
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
				prepareForExecution(scheduledParts[2]);
				if(isDate) deleteExecution(i);
				coincide=false;
				break;
			}
		}
	}
	
	public void  prepareForExecution(String executionName) throws FileNotFoundException, JavaLayerException {
		executionToSend = executionName;
		//("To execute: "+executionToSend);
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
		//System.out.println("Filename: "+executionToSend);
	}

	private void startExecution() throws FileNotFoundException, JavaLayerException{
		main.principalPane.placeBtns(true);
		if(!playSong && !bellExecution)
			if(extension.equals("mp3")){
				//stopSong();
				//if(!playSong)
					playSong();
			} else {
				//System.out.println("Its gonna send to Arduino");
				//if(!bellExecution) {
					bellExecution = true;
					if(main.principalPane.playPrev) {
						playPrevSong();
					} else {
						sendToArduino();
					}
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
		
		for(String name: scheduledExecutionList){}
			//System.out.println(name);
	}
	
	private void openSerialPort() throws IOException{
	/*	String port = os.ifWindows()? "COM3":"/dev/ttyUSB0";
	    arduinoPort = SerialPort.getCommPort(port);
		System.out.println("Selected port name: "+arduinoPort.getSystemPortName()+": "+arduinoPort.getDescriptivePortName());
		arduinoPort.setComPortParameters(9600, 8, 1, 0);
		arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
		
		boolean opened = arduinoPort.openPort(); 
		System.out.println("Opened sucessfully: "+opened);

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
		}*/
	}

	/*private void reopenSerialPort() throws IOException{
		/*String port = os.ifWindows()? "COM3":"/dev/ttyUSB0";
	    arduinoPort = SerialPort.getCommPort(port);
		System.out.println("Selected port name: "+arduinoPort.getSystemPortName()+": "+arduinoPort.getDescriptivePortName());
		arduinoPort.setComPortParameters(9600, 8, 1, 0);
		arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
		
		boolean opened = arduinoPort.openPort(); 
		System.out.println("Reopened sucessfully: "+opened);
		try {
			Thread.sleep(3000);
			//System.out.println("after sleep");
			if(waitingForArduino){
				sendToArduino();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 if (arduinoPort.isOpen()) {
	    	System.out.println("Port reinitialized!");
			//startTimer(20000);
	    } else {
	    	System.out.println("Port not available");
	    }
	}*/
	
	/*private void initArduinoDataReception() {
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
		        
		        System.out.println("Raw: "+new String(newData));
		        if(newData[0]=='f') {
		        	bellExecution = false;
		        	main.principalPane.placeBtns(false); 
		        	System.out.println((char)newData[0]);
		        	//testTimer(2000);
		        }
		        else if(newData[0]=='?'){
		        	okMessage = true;
		        	System.out.println("Received ? from arduino");
		        }
		        else if(newData[0]=='R') {
		        	System.out.println("Arduino reseted");
		        }
		        else {
			        int num=Character.getNumericValue(newData[0]);
			        //System.out.println("Bytes number: "+numRead);
			        //System.out.println("Data: "+num);
			        
			        playMelodiasByPhone(num);
		        }
		      }
		    });
	}*/
	
	private void sendToArduino() {

		waitingForArduino = false; //lo necesito?
		String[] fileLines;
		fileLines = fl.readFileLine();
		executionHandler.playExecution(fileLines);
		/*System.out.println("Filename: "+fl.getFilePath());
		executionDurationHandler = new ExecutionDurationHandler();
		executionDurationHandler.setExecutionLines(fileLines);
		long executionDuration = executionDurationHandler.getDuration()+3000;
		//System.out.println("Total duration: "+executionDuration);
		startExecutionTimer(executionDuration);
		for(String line: fileLines){
			if(line.contains("#"))
				line = line.replace("#","");
			//System.out.print(line+" ");
			byte[] byteArray = line.getBytes();
			//System.out.println(line);
			arduinoPort.writeBytes(byteArray,byteArray.length);
			//arduinoVerify(byteArray);
		}*/		
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
							if(musicFilePlayer.isComplete())
								stopSong();
						}	
					} catch (JavaLayerException e) {
					   e.printStackTrace();
					}
	          }
	       }.start();      
	}
	
	public void playPrevSong() throws FileNotFoundException, JavaLayerException {
		FileInputStream relative = new FileInputStream(prevFl.getFilePath());
		musicFilePlayer = new Player(relative);
		playSong = true;
	      new Thread() {
	          public void run() {
					try {						
						while(playSong) {
							musicFilePlayer.play(1);
							if(musicFilePlayer.isComplete())
								finishPrevSong();
						}	
					} catch (JavaLayerException e) {
					   e.printStackTrace();
					}
	          }
	       }.start();      
	}
	
	public void buttonStopBellExecution() {
		if(bellExecution) {
			byte[] byteArrray = new byte[1];
			byteArrray[0] = 's';
			bellExecution = false; //
        	main.principalPane.placeBtns(false);
        	executionHandler.stopExecution();
			//System.out.println(byteArrray[0]);
			//arduinoVerify(byteArrray);
			//arduinoPort.writeBytes(byteArrray,byteArrray.length);
			//arduinoVerify();
			//testTimer(2000);
		}
	}
	
	public void stopBellExecution() {
		bellExecution = false; //
    	main.principalPane.placeBtns(false);
        /*if(bellExecution) {
			arduinoVerify();
		}*/
	}
	
	/*public void arduinoVerify() {
		byte[] byteArray = new byte[1];
		byteArray[0] = '?';
		arduinoPort.writeBytes(byteArray,byteArray.length);
		System.out.println("Escribiendo ? al arduino");
		startTimer(1000L);
	}
	
	public void arduinoVerify(long time) {
		byte[] byteArray = new byte[1];
		byteArray[0] = '?';
		arduinoPort.writeBytes(byteArray,byteArray.length);
		System.out.println("Escribiendo ? al arduino");
		startTimer(time);
	}*/
	
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
	
	/*public void startTimer(long mili,byte[] byteArray) {
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
	}*/
	
	public void startExecutionTimer(long mili) {
	    TimerTask task = new TimerTask() {
	        public void run() {
	        	//stopbellExecution();
	        	/*byte[] byteArrray = new byte[1];
				byteArrray[0] = 's';
				bellExecution = false; //
	        	main.principalPane.placeBtns(false);
				System.out.println(byteArrray[0]);
				//arduinoVerify(byteArrray);
				arduinoPort.writeBytes(byteArrray,byteArrray.length);*/
	        	stopBellExecution();
	        }
	    };
	    timer = new Timer("Timer");
	    
	    long delay = mili;
	    timer.schedule(task, delay);
	}
	
	/*public void testTimer(long mili) {
	    TimerTask task = new TimerTask() {
	        public void run() {
	        	arduinoVerify();
	        }
	    };
	    timer = new Timer("Timer");
	    
	    long delay = mili;
	    timer.schedule(task, delay);
	}*/
	
	public void clockPulseA() {
		byte[] byteArrray = new byte[1];
		byteArrray[0] = 'A';
		//System.out.println((char)byteArrray[0]);
		//arduinoPort.writeBytes(byteArrray,byteArrray.length);
	}
	
	public void backlightOn() {
		byte[] byteArrray = new byte[1];
		byteArrray[0] = 'L';
		//arduinoPort.writeBytes(byteArrray,byteArrray.length);
	}
	
	public void backlightOff() {
		byte[] byteArrray = new byte[1];
		byteArrray[0] = 'l';
		//arduinoPort.writeBytes(byteArrray,byteArrray.length);
	}
	
	public void stopSong() {
		//System.out.println("entré ");
		System.out.print(playSong);
		if(playSong) {
			playSong = false;
			musicFilePlayer.close();
			main.principalPane.placeBtns(false);
		}
	}
	
	public void finishPrevSong() {
		//System.out.println("entré ");
		System.out.print(playSong);
		if(playSong) {
			playSong = false;
			musicFilePlayer.close();
			sendToArduino();
			//main.principalPane.placeBtns(false);
		}
	}

}