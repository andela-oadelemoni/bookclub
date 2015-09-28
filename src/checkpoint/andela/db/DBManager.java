package checkpoint.andela.db;

import java.io.*;
import java.sql.*;
import java.util.Map.Entry;
import java.util.*;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DBManager {
	
	private String dbConfigPath;
	private MysqlDataSource mysqlDS = new MysqlDataSource();
	private Properties props = new Properties();
	private FileReader fileReader;
	private DataSource dataSource;
	private TreeMap<String, String> map = new TreeMap<>();
	private String row_end;
	private String tableName;
	
	
	public DBManager(String dbConfigPath) {
		this.dbConfigPath = dbConfigPath;
		config();
		dataSource = getDataSource();
	}
	
	private void config() {		
		File file;
		try {
			file = new File(dbConfigPath);
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DataSource getDataSource() {
        try {
            props.load(fileReader);
            tableName = props.getProperty("MYSQL_DB_TABLENAME");
            mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
            mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mysqlDS;
    }
	
	public void setRowEnd(String string) {
		this.row_end = string;
	}
	
	public void createTableData(String string) {
		final int key = 0;
		final int value = 1;
		
		if (string.equals(row_end)) {
			insertIntoTable();
			map.clear();
		}
		else {
			String[] pair = string.trim().split(" - ");
			if (pair.length > 1) {
				map.put(pair[key].trim(), pair[value].trim());
			}
		}
	}
	
	private void insertIntoTable() {
	    
	    String queryString = insertStatement();
		
		try (Connection con = dataSource.getConnection()) {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(queryString);
			
			// LOG FILE SUPPOSED TO ENTER HERE
		}
		catch (SQLException e) {
			
		}
	}
	
	private String insertStatement() {
		String columnNames = "";
		String columnValues = "";
		
		// BUILD QUERY STRING
	    for (Entry<String, String> entry: map.entrySet()) {
	    	
	    	if (entry.equals(map.lastEntry())) {	    		
	    		columnNames += "`"+entry.getKey()+"`";
	    		columnValues += "\""+entry.getValue()+"\"";
	    	}
	    	else {
	    		columnNames += "`"+entry.getKey()+"`,";
	    		columnValues += "\""+entry.getValue()+"\",";
	    	}
		}
	    
	    String queryString = "INSERT INTO "
	    		+ tableName + " "
	    		+ "("+columnNames+") VALUES"
	    		+ "("+columnValues+")";
	    
	    return queryString;
	}
	
}
