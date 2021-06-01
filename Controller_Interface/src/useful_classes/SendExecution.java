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
	SerialPort arduinoPort;
	Player musicFilePlayer;

	public SendExecution(){
		fl.setDirection("src/sav/");
	}
	
	public void setScheduledExecutionList(ArrayList<String> executionList) {
		this.scheduledExecutionList = executionList;
		/*for(String name: scheduledExecutionList)
			System.out.println(name);*/
	}
	
	public void compareDateStrings(String actualDateHour) {
		boolean coincide = false;
		boolean dateCoincide;
		boolean dayCoincide;
		boolean hourCoincide;
		int foundScheduledExecutionIndex;
		
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
				foundScheduledExecutionIndex=i;
				deleteExecution(i);
				
				coincide=false;
				break;
			}
		}
	}
	
	public void setMainPane(MainPane main) {
		this.main = main;
	}
	
	private void deleteExecution(int index) {
		String executionToSend = scheduledExecutionList.get(index);
		String extension = executionToSend.split(";")[2].split("\\.")[1];
		String[] fileLines;
		
		//To erase execution from file
		switch(extension) {
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
		
		fileLines = fl.readFileLine();
		fl.writeFile("",false);
		for(String line: fileLines) {
			if(!line.equals(executionToSend)) {
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
		/*
		String txt = "259";
		byte[] byteArrray = txt.getBytes();
		arduinoPort.writeBytes(byteArrray,byteArrray.length);


		if(arduinoPort.closePort()){
			System.out.println("Port is closed.");
		} else {
			System.out.println("Failed to close port.");
		}*/
	    
	}
	
	private void sendToArduino(int index) {
		String executionToSend = scheduledExecutionList.get(index);
		String executionFile = executionToSend.split(";")[2];
		String extension = executionToSend.split(";")[2].split("\\.")[1];
		String[] fileLines;
		fl.setDirection("src/files/");
		fl.setFilename(executionFile);
		
		fileLines = fl.readFileLine();
		
		String txt = "259";
		byte[] byteArrray = txt.getBytes();
		arduinoPort.writeBytes(byteArrray,byteArrray.length);
	}
	
	public void playSong() throws FileNotFoundException, JavaLayerException {
		FileInputStream relative = new FileInputStream("src/files/From The Sky.mp3");
		musicFilePlayer = new Player(relative);
	   //   apl.play();
	      boolean pausa = true;
	      new Thread() {
	          public void run() {
	             try {
	            	 musicFilePlayer.play();
	             } catch (JavaLayerException e) {
	                e.printStackTrace();
	             }
	          }
	       }.start();      
	}
	
	public void stopSong() {
		musicFilePlayer.close();
	}

}
