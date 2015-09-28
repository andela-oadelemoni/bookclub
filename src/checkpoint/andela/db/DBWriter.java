package checkpoint.andela.db;

import checkpoint.andela.log.LogWriter;
import checkpoint.andela.parser.FileStringBuffer;

public class DBWriter implements Runnable {
	
	// SHARED BUFFER PROPERTY
	private FileStringBuffer buffer;
	private DBManager dbObject;
	private LogWriter logWriter;
	public static final String ROW_END = "BREAK";
	
	public DBWriter(String dbConfigPath, FileStringBuffer buffer) {
		this.buffer = buffer;	
		dbObject = new DBManager(dbConfigPath);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		writeToDB();
	}
	
	// method to write file content to the db from the buffer
	private synchronized void writeToDB() {
		// SET LINE THAT SIGNIFIES ROW END
		dbObject.setRowEnd(ROW_END);
		// while loop to write from buffer goes here
		String line;
		while ((line = buffer.getData()) != null) {
			if (!line.startsWith("//") && !line.startsWith("#") && !line.isEmpty()) {
				dbObject.createTableData(line);
				if (logWriter != null)
					logWriter.dbWriterLog(line);
			}
			if (line.startsWith("//")) {
				dbObject.createTableData(ROW_END);
			}
		}
		logWriter.setDone(true);
	}
	
	// SET LOG WRITER TO LOG ACTION IN THIS CLASS
	public void setLogWriter(LogWriter logWriter) {
		this.logWriter = logWriter;
	}
	
	public LogWriter getLogWriter() {
		return logWriter;
	}
	
}
