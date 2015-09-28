package checkpoint.andela.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import checkpoint.andela.log.LogBuffer;
import checkpoint.andela.log.LogWriter;

public class LogWriterTest {
	
	private LogWriter logWriter;
	private LogBuffer logBuffer = new LogBuffer();
	private Path pathToTarget = Paths.get("/Users/kamiye/Documents/workspace/logWriter.txt");
	private Thread logWriterThread;

	@Before
	public void setUp() throws Exception {
		logWriter = new LogWriter(pathToTarget, logBuffer);
		logWriterThread = new Thread(logWriter);
	}

	@After
	public void tearDown() throws Exception {
		deleteCreatedLog();
	}

	@Test
	public void testRunnable() {
		logBuffer.addLog("Log data 1");
		logBuffer.addLog("Log data 2");
		
		String expected1 = "Log data 1";
		String expected2 = "Log data 2";
		logWriterThread.start();
		logWriter.setDone(true);
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String actualLine1 = "";
		String actualLine2 = "";
		try {
			BufferedReader bufferedReader = Files.newBufferedReader(pathToTarget);
			actualLine1 = bufferedReader.readLine();
			actualLine2 = bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals("LogWriterThread assertion failed", expected1, actualLine1);
		assertEquals("LogWriterThread assertion failed", expected2, actualLine2);
	}
	
	@Test
	public void testFileParserLog() {
		String expected = "FileParser Thread ("+getCurrentTime()+")---- wrote SOMETHING to buffer";
		logWriter.fileParserLog("SOMETHING");
		
		logWriterThread.start();
		logWriter.setDone(true);
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String actualLine = "";
		try {
			BufferedReader bufferedReader = Files.newBufferedReader(pathToTarget);
			actualLine = bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals("LogWriterThread assertion failed", expected, actualLine);
	}
	
	@Test
	public void testDBWriterLog() {
		String expected = "DBWriter Thread ("+getCurrentTime()+")---- collected SOMETHING from buffer";
		logWriter.dbWriterLog("SOMETHING");
		
		logWriterThread.start();
		logWriter.setDone(true);
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String actualLine = "";
		try {
			BufferedReader bufferedReader = Files.newBufferedReader(pathToTarget);
			actualLine = bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals("LogWriterThread assertion failed", expected, actualLine);
	}
	
	private void deleteCreatedLog() {
		try {
			Files.delete(pathToTarget);
		}
		catch (NoSuchFileException e) {
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
		String dateString=sdf.format(date);
		return dateString;
	}

}
