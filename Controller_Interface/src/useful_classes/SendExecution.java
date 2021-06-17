package useful_classes;

import java.io.IOException;
import java.util.ArrayList;
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
	boolean playSong = false;
	String executionToSend;
	String extension;
	SerialPort arduinoPort;
	Player musicFilePlayer;
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
		if(extension.equals("mp3")){
			stopSong();
			playSong();
		} else {
			//System.out.println("Its gonna send to Arduino");
			sendToArduino();
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
		String port = os.ifWindows()? "COM3":"/dev/ttyUSB0";
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
		      public void serialEvent(SerialPortEvent event) {
		        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
		        return;
		        byte[] newData = new byte[arduinoPort.bytesAvailable()];
		        int numRead = arduinoPort.readBytes(newData, newData.length);
		        int num=Character.getNumericValue(newData[0]);
		        System.out.println("Bytes number: "+numRead);
		        System.out.println("Data: "+num);
		        
		        playMelodiasByPhone(num);
		        
		        
		      }
		    });
	}
	
	private void sendToArduino() {
		String[] fileLines;
		fileLines = fl.readFileLine();
		System.out.println("Sending to arduino");
		for(String line: fileLines){
			
			if(line.contains("#"))
				line = line.replace("#","");
			System.out.print(line+" ");
			byte[] byteArrray = line.getBytes();
			System.out.println(byteArrray);
			arduinoPort.writeBytes(byteArrray,byteArrray.length);
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
		//System.out.println("enter on playSong");
	      new Thread() {
	          public void run() {
					try {
						//System.out.println("playing");
						
						while(playSong) {
							musicFilePlayer.play(1);
						}
						
						//System.out.println("finished: "+playingFinished);
					} catch (JavaLayerException e) {
					   e.printStackTrace();
					}
				  
	          }
	       }.start();      
	}
	
	private void stopSong() {
		if(playSong) {
			playSong = false;
			musicFilePlayer.close();
		}
	}

}
