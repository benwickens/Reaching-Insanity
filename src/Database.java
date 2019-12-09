
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class used to simplify connecting to the database and querying it.
 * @author Alan Tollett
 * @version 1.0 Added connection attribute and query method which returns a parsable result set.<br>
 */

public class Database {

	/**The connection to the database*/
	private static Connection connection;
	
	/**
	 * Constructs a database object
	 * @param url the url of the database to connect to (including port)
	 * @param user the name of the database user
	 * @param password the password of the database user
	 * @throws SQLException if user enters incorrect user/password or we failed to connect. 
	 */
	public Database(String url, String user, String password) throws SQLException {
		connection = DriverManager.getConnection(url, user, password);
	}
	
	/**
	 * Executes an SQL query and returns a result.
	 * @param query the SQL query
	 * @return a result set object containing the tuples in the resulting table (null if insert/update...)
	 */
	public ResultSet query(String query) {
		if (query.contains(";")) {
			System.out.println("ERROR: Query contains ';'. To protect from SQL Injection this query was aborted.");
			return null;
		} else {
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
	
	public void manipulate(String query) {
		if (query.contains(";")) {
			System.out.println("ERROR: Query contains ';'. To protect from SQL Injection this query was aborted.");
		} else {
			query += ";";
		}
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("ERROR: Failed to send query to the MySQL Database");
			e.printStackTrace();
		}
		
	}
	
	public Connection getConnection() {
		return connection;
	}
}
