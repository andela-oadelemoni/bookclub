import java.nio.file.Path;
import java.nio.file.Paths;

import checkpoint.andela.db.DBWriter;
import checkpoint.andela.log.LogBuffer;
import checkpoint.andela.log.LogWriter;
import checkpoint.andela.parser.FileParser;
import checkpoint.andela.parser.FileStringBuffer;

public class TestRun {
	
	private static final String dirPath = "/Users/kamiye/Documents/workspace/";
	private static final Path sourcePath = Paths.get(dirPath+"reactions.dat");
	private static final Path targetPath = Paths.get(dirPath+"log.txt");
	private static final String dbConfigPath = dirPath+"db.properties";
	
	
	// logbuffer object
	LogBuffer logBuffer = new LogBuffer();
	// create shared buffer object
	FileStringBuffer stringbuffer = new FileStringBuffer();
	
	// create writer and parser objects
	FileParser fileParser = new FileParser(sourcePath, stringbuffer);
	DBWriter dbWriter = new DBWriter(dbConfigPath, stringbuffer);
	LogWriter logWriter = new LogWriter(targetPath, logBuffer);
	{
		fileParser.setLogWriter(logWriter);
		dbWriter.setLogWriter(logWriter);
	}
	
	// create writer and parser threads
	Thread fileParserThread = new Thread(fileParser);
	Thread dbWriterThread = new Thread(dbWriter);
	Thread logWriterThread = new Thread(logWriter);
	
	// main method to start the threads above
	public static void main (String[] args) {
		TestRun testRun = new TestRun();
		testRun.fileParserThread.start();
		testRun.dbWriterThread.start();
		testRun.logWriterThread.start();
	}
}
