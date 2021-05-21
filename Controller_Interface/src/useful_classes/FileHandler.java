package useful_classes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;  // Import the File class
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors

public class FileHandler {
	boolean debug = false;
	private String filename;
	private String direction = "src/sav/";
	
	public void setFilename(String name) {
		filename = direction+name;
	}
	
	public void setDirection(String name) {
		direction = name+"/";
	}
	
	public boolean ifExist() {
		//File myObj = new File(filename);
	    
		//return myObj.exists()
		 return new File(filename).exists();
	}
	  
	  public void writeFile(String text,boolean append) {
		  try {
	            FileWriter writer = new FileWriter(filename, append); //If true it will append the text, if false replace all the text
	            BufferedWriter bufferedWriter = new BufferedWriter(writer); 
	            bufferedWriter.write(text);
	            bufferedWriter.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	  }
	  
	  public void writeFile(char[] text,boolean append) {
		  try {
	            FileWriter writer = new FileWriter(filename, append); //If true it will append the text, if false replace all the text
	            BufferedWriter bufferedWriter = new BufferedWriter(writer); 
	            bufferedWriter.write(text);
	            bufferedWriter.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	  }
	  
	  public void writeFileln(String text,boolean append) {
		  try {
	            FileWriter writer = new FileWriter(filename, append); //If true it will append the text, if false replace all the text
	            BufferedWriter bufferedWriter = new BufferedWriter(writer); 
	            bufferedWriter.write(text);
	            bufferedWriter.write("\n");
	            bufferedWriter.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	  }
	  
	  public void writeFileln(char[] text,boolean append) {
		  try {
	            FileWriter writer = new FileWriter(filename, append); //If true it will append the text, if false replace all the text
	            BufferedWriter bufferedWriter = new BufferedWriter(writer); 
	            bufferedWriter.write(text);
	            bufferedWriter.write("\n");
	            bufferedWriter.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	  }
	  
	  public String readFile() {
		  String txt="";
		  try {
	            FileReader reader = new FileReader(filename);
	            BufferedReader bufferedReader = new BufferedReader(reader);
	           
	            String line;
	            while ((line = bufferedReader.readLine()) != null) {
	                if(debug) System.out.println(line);
	                txt += line;
	            }
	            reader.close();
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		  return txt;
	  }
	  
	  public String[] readFileLine() {
		  String txt="";
		  String[] lines = null;
		  try {
	            FileReader reader = new FileReader(filename);
	            BufferedReader bufferedReader = new BufferedReader(reader);
	           
	            String line;
	            while ((line = bufferedReader.readLine()) != null) {
	                if(debug) System.out.println(line);
	                txt += line+"\n";
	            }
	            reader.close();
	            lines = txt.split("\n");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		  return lines;
	  }
	  
	  public String readFileLine(int line_number) {
		  String txt="";
		  String line="";

		  
		  try {
	            FileReader reader = new FileReader(filename);
	            BufferedReader bufferedReader = new BufferedReader(reader);
	           
	            
	            for(int i=0;i<line_number;i++){
	            	line = bufferedReader.readLine();
	                if(debug) System.out.println(line);
	            }

	             reader.close();
	                     
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		  return line;
	  }
	  
	  public String[] searchFiles(String filter) {
		  String[] nameList = new String[3];
		  FileFilter filtro = new FileFilter() {
			    @Override
			    public boolean accept(File arch) {
			    	boolean confirm = arch.getName().endsWith(filter);
			        return confirm; 
			    }
			};
		  
			File carpeta = new File(direction);
			File[] listado = carpeta.listFiles(filtro);
			
			if (listado == null || listado.length == 0) {
			    System.out.println("No hay elementos dentro de la carpeta actual");
			}
			else {
				nameList = new String[listado.length];
			    for (int i=0; i< listado.length; i++) {
			    	String name = listado[i].getName().toString().split("\\.")[0];
			    	//String name = listado[i].getName().toString();
			        //System.out.println(name);
			        nameList[i] = name;
			    }
			    
			}
		  return nameList;
	  }
	
}
