package fr.epita.services;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class WriteToFile {
	private final static String fileName = "consoleResults.txt";

	public WriteToFile(String content) {
		try(PrintWriter printWriter = new PrintWriter(fileName)){
			printWriter.println(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
