package checkpoint.andela.test;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import checkpoint.andela.db.DBManager;
import checkpoint.andela.db.DBWriter;
import checkpoint.andela.log.LogBuffer;
import checkpoint.andela.log.LogWriter;
import checkpoint.andela.parser.FileStringBuffer;

public class DBWriterTest {
	
	private String config = "/Users/kamiye/Documents/workspace/dbWriterTest.properties";
	private FileStringBuffer stringBuffer = new FileStringBuffer();
	private DBWriter dbWriter;
	private DBManager dbManager = new DBManager(config);
	private Thread dbWriterThread;

	@Before
	public void setUp() throws Exception {
		createTestDBTable();
		stringBuffer.putData("NAME - DBWriterTest");
		dbWriter = new DBWriter(config, stringBuffer);
		dbWriterThread = new Thread(dbWriter);
		
	}

	@After
	public void tearDown() throws Exception {
		deleteTestDBTable();
	}

	@Test
	public void testRunnable() {
		String dbData = "DBWriterTest";
		dbWriterThread.start();
		stringBuffer.putData("//");
		String actualData = getTestDBData();
		
		assertEquals("DBWriter assertion error", dbData, actualData);
	}
	
	@Test
	public void testLogWriterSetter() {
		LogWriter logWriter = new LogWriter(Paths.get("dummyfile"), new LogBuffer());
		dbWriter.setLogWriter(logWriter);
		
		LogWriter actual = dbWriter.getLogWriter();
		
		assertEquals("DBWriter assertion error", logWriter, actual);
	}
	
	private void createTestDBTable() {
		DataSource dataSource = dbManager.getDataSource();
		
		String queryString = "CREATE TABLE DBWRITERTEST (NAME VARCHAR(255))";
		
		try (Connection con = dataSource.getConnection()) {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(queryString);
		}
		catch (SQLException e) {
			
		}
	}
	
	private void deleteTestDBTable() {
		DataSource dataSource = dbManager.getDataSource();
		
		String queryString = "DROP TABLE DBWRITERTEST";
		
		try (Connection con = dataSource.getConnection()) {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(queryString);
			
			// LOG FILE SUPPOSED TO ENTER HERE
		}
		catch (SQLException e) {
			
		}
	}
	
	private String getTestDBData() {
		DataSource dataSource = dbManager.getDataSource();
		
		String name = "";
		
		String queryString = "SELECT * FROM DBWRITERTEST";
		
		try (Connection con = dataSource.getConnection()) {
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery(queryString);
			if (result.next()) {
				name = result.getString("name");
			}
		}
		catch (SQLException e) {
			
		}
		return name;
	}

}
