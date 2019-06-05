package consolebank;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Queries {	
	private static class Loader {
		static final Queries INSTANCE = new Queries();
	}

	public static Queries getInstance() {
		return Loader.INSTANCE;
	}
	
	private Queries() {}
	
	private final DAL db = DAL.getInstance();

	private final Connection conn = db.conn;
	private final Statement createStatement = db.createStatement;
	private final DatabaseMetaData dbmd = db.dbmd;
	
	public void initializeDbIfEmpty(){
		try {
			ResultSet rs = dbmd.getTables(null, "APP", "USERS", null);
			// create tables if db is empty
			if (!rs.next()) {
				createStatement.execute("CREATE TABLE users (id INT NOT NULL UNIQUE, name VARCHAR(20) NOT NULL, balance BIGINT DEFAULT 0)");
				createStatement.execute("CREATE TABLE transactions (id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), userId INT NOT NULL, type VARCHAR(20) NOT NULL, amount BIGINT NOT NULL, trTime TIMESTAMP NOT NULL)");
				
				// inserting some dummy records into 'users' for testing purposes
				User John = new User(1, "John Doe", 100000);
				User Jane = new User(2, "Jane Doe", 150000);
				User Jakab = new User(3, "Gipsz Jakab", 340000);
				User Andrew = new User(4, "Andrew Smith", 40000);
				addUser(John);
				addUser(Jane);
				addUser(Jakab);
				addUser(Andrew);

				//  creating and inserting some dummy records into 'transactions' for testing purposes
				Transaction tr1 = new Transaction(1, "deposit", 250);
				Transaction tr2 = new Transaction(1, "withdraw", 220);

				Transaction tr3 = new Transaction(2, "deposit", 230);
				Transaction tr4 = new Transaction(2, "transfer(-)", 250);

				Transaction tr5 = new Transaction(3, "withdraw", 250);
				Transaction tr6 = new Transaction(3, "deposit", 260);

				Transaction tr7 = new Transaction(4, "transfer(+)", 280);
				Transaction tr8 = new Transaction(4, "withdraw", 250);

				addTransaction(tr1);
				addTransaction(tr2);
				addTransaction(tr3);
				addTransaction(tr4);
				addTransaction(tr5);
				addTransaction(tr6);
				addTransaction(tr7);
				addTransaction(tr8);
			}
		} catch (SQLException ex) {
			System.out.println("Something went wrong while inserting tables.");
			System.out.println("Error: " + ex);
		}
	}
	
	// SPECIFIC QUERIES
	
	// get table specific metadata
	public void showUsersMeta() {
		System.out.println("User(s):");
		
		String sql = "SELECT * FROM users";
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			rs = createStatement.executeQuery(sql);
			rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int x = 1; x <= columnCount; x++) {
				if (rsmd.getColumnName(x).equals("ID")) {
					System.out.printf("| %-10s|", rsmd.getColumnName(x));
				} else {
					System.out.printf(" %-30s|", rsmd.getColumnName(x));
				}
			}
			System.out.println("");
		} catch (SQLException ex) {
			System.out.println("Something went wrong (showUsersMeta()).");
			System.out.println("Error: " + ex);
		}
	}

	private void showTransactionsMeta() {
		System.out.println("Transactions:");
		
		String sql = "SELECT * FROM transactions";
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			rs = createStatement.executeQuery(sql);
			rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int x = 1; x <= columnCount; x++) {
				if (rsmd.getColumnName(x).equals("ID")) {
					System.out.printf("| %-10s|", rsmd.getColumnName(x));
				} else if (rsmd.getColumnName(x).equals("USERID")) {
					System.out.printf(" %-10s|", rsmd.getColumnName(x));
				} else if (rsmd.getColumnName(x).equals("TYPE")) {
					System.out.printf(" %-15s|", rsmd.getColumnName(x));
				}else {
					System.out.printf(" %-30s|", rsmd.getColumnName(x));
				}
			}
			System.out.println("");
		} catch (SQLException ex) {
			System.out.println("Something went wrong (showTransactionMeta()).");
			System.out.println("Error: " + ex);
		}
	}

	// get all user records
	public void getAllUsers() {
		String sql = "SELECT * FROM users";
		try {
			ResultSet rs = createStatement.executeQuery(sql);
			while (rs.next()) {
				System.out.printf("| %-10s|", rs.getInt("id"));
				System.out.printf(" %-30s|", rs.getString("name"));
				System.out.printf(" %-30s|\n", rs.getLong("balance"));
			}
		} catch (SQLException ex) {
			System.out.println("Something went wrong (getAllUsers) .");
			System.out.println("Error: " + ex);
		}
	}
	
	public int getNrOfUsers(){
		int count = 0;
		String sql = "SELECT * FROM users";
		try {
			ResultSet rs = createStatement.executeQuery(sql);
			while (rs.next()) {
				count++;
			}
		} catch (SQLException ex) {
			System.out.println("Something went wrong (getAllUsers) .");
			System.out.println("Error: " + ex);
		}
		return count;
	}

	// some basic functions
	private void getUser(int id) {
		try {
			String sql = "SELECT * FROM users WHERE id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.execute();
			ResultSet rs = preparedStatement.getResultSet();
			showUsersMeta();
			while (rs.next()) {
				System.out.printf("| %-10s|", rs.getInt("id"));
				System.out.printf(" %-30s|", rs.getString("name"));
				System.out.printf(" %-30s|\n", rs.getLong("balance"));
			}
		} catch (SQLException ex) {
			System.out.println("Something went wrong (getUser(int id)) .");
			System.out.println("Error: " + ex);
		}
	}

	public void addUser(User user) {
		try {
			String sql = "INSERT INTO users VALUES (?, ?, ?)";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, user.getId());
			preparedStatement.setString(2, user.getName());
			preparedStatement.setLong(3, user.getBalance());
			preparedStatement.execute();
		} catch (SQLException ex) {
			System.out.println("Something went wrong (addUser(User user)).");
			System.out.println("Error: " + ex);
		}
	}

	private void deleteUser(int id) {
		try {
			String sql = "DELETE FROM users WHERE id=?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.execute();
		} catch (SQLException ex) {
			System.out.println("Something went wrong (deleteUser(String name)).");
			System.out.println("Error: " + ex);
		}
	}

	private void updateUser(User user) {
		try {
			String sql = "UPDATE users SET balance=? WHERE id=?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setLong(1, user.getBalance());
			preparedStatement.setInt(2, user.getId());
			preparedStatement.execute();
		} catch (SQLException ex) {
			System.out.println("Something went wrong (updateUser(User user)).");
			System.out.println("Error: " + ex);
		}
	}

	// save transactions, pass this method to any transaction (with successful outcome)
	public void addTransaction(Transaction transaction) {
		try {
			String sql = "INSERT INTO transactions (userId, type, amount, trTime) VALUES (?, ?, ?, ?)";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, transaction.getUserId());
			preparedStatement.setString(2, transaction.getType());
			preparedStatement.setLong(3, transaction.getAmount());
			preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			preparedStatement.execute();
		} catch (SQLException ex) {
			System.out.println("Something went wrong (addTransaction(Transaction transaction)).");
			System.out.println("Error: " + ex);
		}
	}
	
	// transaction printer
	private void trPrinter(ResultSet rs) throws SQLException{
		while (rs.next()) {
			System.out.printf("| %-10s|", rs.getInt("id"));
			System.out.printf(" %-10s|", rs.getInt("userId"));
			System.out.printf(" %-15s|", rs.getString("type"));
			System.out.printf(" %-30s|", rs.getLong("amount"));
			System.out.printf(" %-30s|\n", rs.getTimestamp("trTime"));
		}
	}
	
	// helper functions
	private void depositWithdrawHelper(String sql, int id, long amount) throws SQLException{
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		preparedStatement.setLong(2, amount);
		preparedStatement.setInt(3, id);
		preparedStatement.execute();
		System.out.println("\nSuccesful transaction, see results below.");
	}
	
	private void transferMoneyHelper(String sql, int id, long amount) throws SQLException{
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		preparedStatement.setLong(2, amount);
		preparedStatement.setInt(3, id);
		preparedStatement.execute();
	}
	
	private void filterAmountHelper(String sql, int id, long fromAmount, long toAmount){
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.setLong(2, fromAmount);
			preparedStatement.setLong(3, toAmount);
			preparedStatement.execute();
			ResultSet rs = preparedStatement.getResultSet();
			trPrinter(rs);
		} catch (SQLException ex) {
			System.out.println("Something went wrong (filterAmount(String sql, int id, long amount)) .");
			System.out.println("Error: " + ex);
		}
	}
	
	private void filterDateHelper(String sql, int id, Date date){
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.setDate(2, date);
			preparedStatement.execute();
			ResultSet rs = preparedStatement.getResultSet();
			trPrinter(rs);
		} catch (SQLException ex) {
			System.out.println("Valami baj van a  (filterDate(String sql, int id, Timestamp ts)) .");
			System.out.println("Hiba: " + ex);
		}
	}

	// main functionality
	public void depositToAccount(int id) {
		try {
			System.out.println("Please enter the amount you want to deposit to your account:");
			long amount = UserInput.getAmount();

			String sql = "UPDATE users SET balance= (SELECT balance FROM users WHERE id = ?) + ? WHERE id = ?";
			depositWithdrawHelper(sql, id, amount);
			getUser(id);
			
			// saving transaction
			Transaction transaction = new Transaction(id, "deposit", amount);
			addTransaction(transaction);
		} catch (SQLException ex) {
			System.out.println("Something went wrong (depositToAccount(int id)).");
			System.out.println("Error: " + ex);
		}
	}

	// overdraft from money withdraw allowed
	public void withdrawFromAccount(int id) {
		try {
			System.out.println("Please enter the amount you want to withdraw from your account:");
			long amount = UserInput.getAmount();

			String sql = "UPDATE users SET balance= (SELECT balance FROM users WHERE id = ?) - ? WHERE id = ?";
			depositWithdrawHelper(sql, id, amount);
			getUser(id);
			
			// saving transaction
			Transaction transaction = new Transaction(id, "withdraw", amount);
			addTransaction(transaction);
		} catch (SQLException ex) {
			System.out.println("Something went wrong (withdrawFromAccount(int id)).");
			System.out.println("Error: " + ex);
		}
	}
	
	// overdraft from money transfer allowed
	public void transferMoney(int senderId) {
		try {
			System.out.println("Please enter the identifier (id) of the person to whom you want to transfer money:");
			int receiverId = UserInput.getUserId();
			
			System.out.println("Please enter the amount you would like to transfer:");
			long amount = UserInput.getAmount();

			String sqlSubtract = "UPDATE users SET balance= (SELECT balance FROM users WHERE id = ?) - ? WHERE id = ?";
			transferMoneyHelper(sqlSubtract, senderId, amount);

			String sqlAdd = "UPDATE users SET balance= (SELECT balance FROM users WHERE id = ?) + ? WHERE id = ?";
			transferMoneyHelper(sqlAdd, receiverId, amount);

			System.out.println("\nSuccesful transaction, see results below.");
			System.out.println("Your account:");
			getUser(senderId);
			System.out.println("The receiving party's account:");
			getUser(receiverId);

			// saving transaction history for both parties
			Transaction senderTransaction = new Transaction(senderId, "transfer(-)", amount);
			addTransaction(senderTransaction);
			
			Transaction receiverTransaction = new Transaction(receiverId, "transfer(+)", amount);
			addTransaction(receiverTransaction);
		} catch (SQLException ex) {
			System.out.println("Something went wrong (transferMoney(int senderId)).");
			System.out.println("Error: " + ex);
		}
	}

	public void getTransactionHistory(int id, int filter, int specifics, long fromAmount, long toAmount, Date date) {
		showTransactionsMeta();
		
		// 0: no filter
		if (filter == 0) {
			try {
				String sql = "SELECT * FROM transactions WHERE userId = ?";
				PreparedStatement preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setInt(1, id);
				preparedStatement.execute();
				ResultSet rs = preparedStatement.getResultSet();
				trPrinter(rs);
			} catch (SQLException ex) {
				System.out.println("Something went wrong (getTransactionHistory(int id, int filter, int specifics, long amount, Date date)) .");
				System.out.println("Error: " + ex);
			}
		}
		
		// 1: filter transactions by type
		if (filter == 1) {
			String specificsStr = "";
			if (specifics == 1) specificsStr = "deposit";
			if (specifics == 2) specificsStr = "withdraw";
			if (specifics == 3) specificsStr = "transfer";
			try {
				String sql = "SELECT * FROM transactions WHERE userId = ? AND type = ?";
				if (specifics == 3) sql = "SELECT * FROM transactions WHERE userId = ? AND SUBSTR(type, 1, 8) = ?";
				PreparedStatement preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setInt(1, id);
				preparedStatement.setString(2, specificsStr);
				preparedStatement.execute();
				ResultSet rs = preparedStatement.getResultSet();
				trPrinter(rs);
			} catch (SQLException ex) {
				System.out.println("Something went wrong (getTransactionHistory(int id, int filter, int specifics, long amount, Date date)) .");
				System.out.println("Error: " + ex);
			}
		}
		
		// 2: filter transactions by amount
		if (filter == 2) {
			// filter a given range
			String sql = "SELECT * FROM transactions WHERE userId = ? AND amount > ? AND amount < ?";
			filterAmountHelper(sql, id, fromAmount, toAmount);
		}
		
		// 3: filter transactions by date
		if(filter == 3){
			// filter transactions AFTER a given date
			if (specifics == 1){
				String sql = "SELECT * FROM transactions WHERE userId = ? AND trTime > ?";
				filterDateHelper(sql, id, date);
			}
			// filter transactions BEFORE a given date
			if (specifics == 2){
				String sql = "SELECT * FROM transactions WHERE userId = ? AND trTime < ?";
				filterDateHelper(sql, id, date);
			}
			// filter transactions ON a given date
			if (specifics == 3){
				String sql = "SELECT * FROM transactions WHERE userId = ? AND CAST(trTime AS DATE) = ?";
				filterDateHelper(sql, id, date);
			}
		}
	}
}