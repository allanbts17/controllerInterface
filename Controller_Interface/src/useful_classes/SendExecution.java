package useful_classes;

import java.util.ArrayList;
import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort;

import visual_classes.MainPane;

public class SendExecution {
	MainPane main;
	public ArrayList<String> scheduledExecutionList;
	FileHandler fl = new FileHandler();
	
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
	
	public void serialCommunication(){
		
	    /*
	     This returns an array of commport addresses, not useful for the client
	     but useful for iterating through to get an actual list of com parts available
	    */
	    SerialPort ports[] = SerialPort.getCommPorts();
	    int i = 1;

	    for (SerialPort port : ports) {
	      //iterator to pass through port array
	    	System.out.println(port.getSystemPortName()+": "+port.getDescriptivePortName());
	    }

	    
	}

}
