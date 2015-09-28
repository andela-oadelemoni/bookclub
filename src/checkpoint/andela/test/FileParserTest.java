package checkpoint.andela.test;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import checkpoint.andela.log.LogBuffer;
import checkpoint.andela.log.LogWriter;
import checkpoint.andela.parser.FileParser;
import checkpoint.andela.parser.FileStringBuffer;

public class FileParserTest {

	private Path pathToTarget = Paths.get("/Users/kamiye/Documents/workspace/fileParser.txt");
	private String fileContent = "Test File Line 1\nTest File Line 2";
	private FileStringBuffer stringBuffer = new FileStringBuffer();
	private FileParser fileParser;
	private Thread fileParserThread;

	@Before
	public void setUp() throws Exception {
		// CREATE NEW FILE FOR FILEPARSER TO WORK WITH
		createTestFile();
		fileParser = new FileParser(pathToTarget, stringBuffer);
		fileParserThread = new Thread(fileParser);
	}

	@After
	public void tearDown() throws Exception {
		deleteTestFile();
		fileParser = null;
	}

	@Test
	public void testRunnable() {
		String expectedLine = "Test File Line 1";
		fileParserThread.start();
		String actualLine = stringBuffer.getData();
		
		assertEquals("Runnable assertion failed", expectedLine, actualLine);
	}
	
	@Test
	public void testLogWriterSetter() {
		LogWriter logWriter = new LogWriter(pathToTarget, new LogBuffer());
		fileParser.setLogWriter(logWriter);
		LogWriter actual = fileParser.getLogWriter();
		
		assertEquals("LogWriter assertion error", logWriter, actual);
	}
	
	/*@Test(expected = IOException.class)
	public void testException() {
		Path pathToTarget = Paths.get("/Users/kamiye/Documents/workspace/what.txt");
		fileParser = new FileParser(pathToTarget, stringBuffer);
		fileParserThread = new Thread(fileParser);
		
		fileParserThread.start();
		
	}*/
	
	private void createTestFile() throws IOException {
			BufferedWriter writer = Files.newBufferedWriter(pathToTarget);
			// WRITE LOG ONE LINE AT A TIME
			writer.write(fileContent);
			// CLOSE BUFFERED WRITER WHEN THROUGH
			writer.close();
	}
	
	private void deleteTestFile() throws IOException {
		    Files.delete(pathToTarget);
	}

}
