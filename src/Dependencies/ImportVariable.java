package Dependencies;

import java.util.IllegalFormatException;

public class ImportVariable implements Comparable<ImportVariable>{
	
	int location = 0;
	int numChar = 0;
	
	
	String name;
	String comment;

	public ImportVariable(int location, String name, int numChar, String comment){
		this.location = location;
		this.name = name;
		this.numChar = numChar;
		this.comment = comment;
	}
	/*
	 * Imports variables from SAS import line
	 * @ LOC VAR_NAME $char#. COMMENT
	 * @throws IllegalFormatException when not properly formatted as a SAS import line
	 */
	public ImportVariable(String line) throws IllegalFormatException{
		line = line.trim();
		String[] splitted = line.split("\\s+"); // split on whitespace
		
		this.location = Integer.parseInt(splitted[1]);
		this.name = splitted[2];
		String numCharString = splitted[3].split("char")[1];
		this.numChar = Integer.parseInt(numCharString.substring(0, numCharString.length()-1));
		this.comment = line.substring(line.indexOf("/*") + 2, line.indexOf("*/")).trim();;
	}
	
	public int getLocation(){
		return location;
	}
	public int getNumChar(){
		return numChar;
	}
	public String getName(){
		return name;
	}
	public String getComment(){
		return comment;
	}
	
	public String toString(){
		String ret = "\n";
		ret += "Location: " + location;
		ret += "\nNumChar: " + numChar;
		ret += "\nName: " + name;
		ret += "\nComment: "+comment; 
		return ret;
	}
	@Override
	public int compareTo(ImportVariable arg0) {
		return this.location - arg0.location;
	}
}
