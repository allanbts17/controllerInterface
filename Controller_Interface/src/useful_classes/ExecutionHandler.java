package useful_classes;

import java.util.ArrayList;

import com.fazecast.jSerialComm.SerialPort;

public class ExecutionHandler {
	private SerialPort arduinoPort;
	private String[] fileLines;
	//private ArrayList<Object[]> fileData = new ArrayList();
	private ArrayList<String> fileData = new ArrayList();
	boolean isToques;
	
	public void setSerialPort(SerialPort port) {
		this.arduinoPort = port;
	}
	
	public void setFileLines(String[] fileLines) {
		this.fileLines = fileLines;
	}
	
	public void execute() {
		defineType();
		fillFileData();
		send();
	}
	
	private void defineType() {
		isToques = fileLines[0].equals("t");
	}
	
	private void fillFileData() {
		fileData.clear();
		for(String line: fileLines) {
			if(!line.equals("t") && !line.equals("b")) {
				/*System.out.println(line);
				int time = Integer.parseInt(line.substring(1));
				Object[] data = {line.charAt(0),time};
				fileData.add(data);*/
				fileData.add(line);
			}
		}
	}
	
	private void send() {
		System.out.println("Execution: ");
		for(String line: fileData) {
			System.out.println(line);
			byte[] noteBytes = line.getBytes();
			arduinoPort.writeBytes(noteBytes,noteBytes.length);
		}
		/*for(Object[] data: fileData) {
			char note = (char)data[0];
			int time = (int)data[1];
			
			//Time
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Note
			System.out.println(note);
			byte noteByte[] = new byte[1];
			noteByte[0] = (byte)note;
			arduinoPort.writeBytes(noteByte,1);
		}*/
	}

}
