package consolebank;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DAL {

	private final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private final String URL = "jdbc:derby:bankDB;create=true";
	private final String USERNAME = "";
	private final String PASSWORD = "";

	Connection conn = null;
	Statement createStatement = null;
	DatabaseMetaData dbmd = null;
	
	private static class Loader {
		static final DAL INSTANCE = new DAL();
	}

	public static DAL getInstance() {
		return Loader.INSTANCE;
	}

	public DAL() {
		// establish db connection
		try {
			conn = DriverManager.getConnection(URL);
			System.out.println("Database connection established.\n");
		} catch (SQLException ex) {
			System.out.println("Something went wrong while esablishing database connection.");
			System.out.println("Error: " + ex);
		}

		// createStatement
		if (conn != null) {
			try {
				createStatement = conn.createStatement();
			} catch (SQLException ex) {
				System.out.println("Something went wrong (createStatement).");
				System.out.println("Error: " + ex);
			}
		}

		// get db metadata
		try {
			dbmd = conn.getMetaData();
		} catch (SQLException ex) {
			System.out.println("Something went wrong while getting db metadata.");
			System.out.println("Error: " + ex);
		}
	}
}