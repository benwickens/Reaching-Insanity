
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

public class Database {

	private static Connection connection;
	
	public Database(String url, String user, String password) throws SQLException {
		connection = DriverManager.getConnection(url, user, password);
	}
	
	public ResultSet query(String query) {
		if(query.contains(";")) {
			System.out.println("ERROR: Query contains ';'. To protect from SQL Injection this query was aborted.");
			return null;
		}else {
			query += ";";
		}
		
		try {
			Statement statement = connection.createStatement();
			return statement.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("ERROR: Failed to send query to the MySQL Database");
			e.printStackTrace();
			return null;
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
}
