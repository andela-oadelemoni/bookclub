package checkpoint.andela.log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LogWriter implements Runnable {

	private Path target;
	private LogBuffer logBuffer;
	private boolean done = false;
	
	public LogWriter (Path targetPath, LogBuffer logBuffer) {
		this.target = targetPath;
		this.logBuffer = logBuffer;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		logToFile();
	}
	
	public synchronized void setDone(boolean done) {
		this.done = done;
		notifyAll();
	}
	
	public void fileParserLog(String string) {
		String logString = "FileParser Thread ("+getCurrentTime()+")---- wrote "+string+" to buffer";
		logBuffer.addLog(logString);
	}
	
	public void dbWriterLog(String string) {
		String logString = "DBWriter Thread ("+getCurrentTime()+")---- collected "+string+" from buffer";
		logBuffer.addLog(logString);
	}
	
	public synchronized void logToFile() {
		while (!done) {
			try {
				wait();
			}
			catch (InterruptedException e) {
				// ignore
			}
		}
		writeFile();
		notifyAll();
	}
	
	private String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
		String dateString=sdf.format(date);
		return dateString;
	}
	
	private void writeFile() {
		List<String> log_data = logBuffer.getLog();
		try {
			BufferedWriter writer = Files.newBufferedWriter(target);
			
			// WRITE LOG ONE LINE AT A TIME
			for (String line: log_data)
				writer.write(line+"\n");
			// CLOSE BUFFERED WRITER WHEN THROUGH
			writer.close();
			System.out.println("done!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
