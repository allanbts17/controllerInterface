package useful_classes;

import java.io.IOException;
import java.util.ArrayList;

import com.fazecast.jSerialComm.SerialPort;

import visual_classes.MainPane;

public class SendExecution {
	MainPane main;
	public ArrayList<String> scheduledExecutionList;
	FileHandler fl = new FileHandler();
	SerialPort arduinoPort;

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
				send(i);
				coincide=false;
				break;
			}
		}
	}
	
	public void setMainPane(MainPane main) {
		this.main = main;
	}
	
	private void send(int index) {
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
	
	public void serialCommunication() throws IOException{
		
	    /*
	     This returns an array of commport addresses, not useful for the client
	     but useful for iterating through to get an actual list of com parts available
	    */
	    SerialPort ports[] = SerialPort.getCommPorts();
	    for (SerialPort port : ports) {
	      //iterator to pass through port array
	    	System.out.println(port.getSystemPortName()+": "+port.getDescriptivePortName());
	    }
		
		arduinoPort =SerialPort.getCommPort("/dev/ttyUSB0");
		arduinoPort = ports[0];
		System.out.println("Selected port name: "+arduinoPort.getSystemPortName()+": "+arduinoPort.getDescriptivePortName());
		arduinoPort.setComPortParameters(9600, 8, 1, 0);
		arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
		if(arduinoPort.openPort()){
			System.out.println("Port is open.");
		} else {
			System.out.println("Failed to open port.");
		}

		String msg = "259";
		/*for(int i=0;i<msg.length();i++){

				arduinoPort.getOutputStream().write(msg.charAt(i));
				arduinoPort.getOutputStream().flush();
				System.out.println("Sent number: "+(msg.charAt(i)));
		
		}*/
		int j;
		/*for(Integer i=0;i<5;i++){
			arduinoPort.getOutputStream().write(i.byteValue());
			arduinoPort.getOutputStream().flush();
			System.out.println("Sent number: "+i);
	
		}*/
		byte[] b = {4,6,8};
		System.out.println(arduinoPort.writeBytes(b, 3)+" bytes writen");

		if(arduinoPort.closePort()){
			System.out.println("Port is closed.");
		} else {
			System.out.println("Failed to close port.");
		}
	    
	}

}
