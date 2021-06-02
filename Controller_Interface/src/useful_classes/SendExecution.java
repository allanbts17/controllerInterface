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
	boolean playingFinished = true;
	String executionToSend;
	String extension;
	SerialPort arduinoPort;
	Player musicFilePlayer;

	public SendExecution(){
		fl.setDirection("src/sav/");
		try {
			openSerialPort();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setScheduledExecutionList(ArrayList<String> executionList) {
		this.scheduledExecutionList = executionList;
		/*for(String name: scheduledExecutionList)
			System.out.println(name);*/
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
				executionToSend = scheduledParts[2];
				setExtension();
				setDirection();
				setFilename();
				startExectuion();
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
	}

	private void startExectuion() throws FileNotFoundException, JavaLayerException{
		if(extension.equals("mp3")){
			//System.out.println("Its gonna play song");
			if(!playingFinished)
				stopSong();
			playingFinished = false;
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
			case "toq":
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
	    arduinoPort = SerialPort.getCommPort("/dev/ttyUSB0");
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
	
	private void sendToArduino() {
		String[] fileLines;
		fileLines = fl.readFileLine();
		System.out.println("Sending to arduino");
		for(String line: fileLines){
			byte[] byteArrray = line.getBytes();
			arduinoPort.writeBytes(byteArrray,byteArrray.length);
		}
		
	}
	
	public void playSong() throws FileNotFoundException, JavaLayerException {
		FileInputStream relative = new FileInputStream(fl.getFilePath());
		musicFilePlayer = new Player(relative);
		//System.out.println("enter on playSong");
	      new Thread() {
	          public void run() {
					try {
						//System.out.println("playing");
						musicFilePlayer.play();
						playingFinished = true;
						//System.out.println("finished: "+playingFinished);
					} catch (JavaLayerException e) {
					   e.printStackTrace();
					}
				  
	          }
	       }.start();      
	}
	
	public void stopSong() {
		musicFilePlayer.close();
		playingFinished = true;
	}

}
