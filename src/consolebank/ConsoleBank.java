package consolebank;

public class ConsoleBank {

	public static void main(String[] args) {
		
		System.out.println("ConsoleBank CLI\n");
		
		Queries db = Queries.getInstance();
		db.initializeDbIfEmpty();
		
		// log all users to console for testing purposes
		db.showUsersMeta();
		db.getAllUsers();
		
		// login (passwords are not implemented)
		System.out.println("\nPlease enter your identifier (id):");
		AppStart.Menu(UserInput.getUserId());
		
	}
	
}
