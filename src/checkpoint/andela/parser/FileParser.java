package checkpoint.andela.parser;

import java.io.*;
import java.nio.file.*;

import checkpoint.andela.log.LogWriter;

public class FileParser implements Runnable {
	
	public static final String END_OF_FILE = "EOF";
	
	// PROPERTIES
	private BufferedReader bufferedReader;
	private Path source;
	private FileStringBuffer stringBuffer;
	private LogWriter logWriter;
	
	// CLASS CONSTRUCTOR
	public FileParser(Path sourcePath, FileStringBuffer buffer) {
		source = sourcePath;
		this.stringBuffer = buffer;
	}

	@Override
	public void run() {
		// TODO AUTO-GENERATED METHOD STUB
		readFile();
	}
	
	private synchronized void readFile() {		
		try {
			bufferedReader = Files.newBufferedReader(source);
			String line;
		    while ((line = bufferedReader.readLine()) != null) {
		        stringBuffer.putData(line);
		        if (logWriter != null)
		        	logWriter.fileParserLog(line);
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		stringBuffer.putData(END_OF_FILE);
	}
	
	// SET LOG WRITER TO LOG ACTION IN THIS CLASS
	public void setLogWriter(LogWriter logWriter) {
		this.logWriter = logWriter;
	}
	
	public LogWriter getLogWriter() {
		return logWriter;
	}

}
