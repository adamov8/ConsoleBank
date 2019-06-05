package consolebank;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class AppStart {

	public static void Menu(int id) {
		Queries db = Queries.getInstance();
		Scanner input = new Scanner(System.in);

		System.out.println("\nMENU - Please choose from the options below by entering a number:");
		System.out.println("(1) - Deposit to account\n"
				+ "(2) - Withdraw from account\n"
				+ "(3) - Transfer money\n"
				+ "(4) - Get transaction history\n"
				+ "(0) - Exit application");

		int menuNr = -1;

		boolean parseInt = false;
		while (!parseInt) {
			try {
				menuNr = Integer.parseInt(input.nextLine());
				if (menuNr < 0 || menuNr > 4) throw new Exception();
				parseInt = true;
			} catch (NumberFormatException ex1) {
				System.out.println("Wrong input, try again!" + " (" + ex1 + ")");
			} catch (Exception ex2) {
				System.out.println("Wrong input, try again!" + " (no menu item is associated with the provided number)");
			}
		}

		switch (menuNr) {
			case 1:
				db.depositToAccount(id); System.out.println("\nReturning to menu..."); Menu(id);
				break;
			case 2:
				db.withdrawFromAccount(id); System.out.println("\nReturning to menu..."); Menu(id);
				break;
			case 3:
				db.transferMoney(id); System.out.println("\nReturning to menu..."); Menu(id);
				break;
			case 4:
				System.out.println("How would you like to filter your transaction history?");
				System.out.println("(0) - No filter\n"
						+ "(1) - Filter by transaction type\n"
						+ "(2) - Filter by amount (range)\n"
						+ "(3) - Filter by transaction date");
				int filter = UserInput.getFilter();	
				if (filter == 1) System.out.println("(1) - Filter deposit transactions\n"
						+ "(2) - Filter withdraw transactions\n"
						+ "(3) - Filter transfer transactions");
				long fromAmount = 0, toAmount = 0;
				if (filter == 2) {
					System.out.println("Please enter the start of the range:");
					fromAmount = UserInput.getAmount();

					System.out.println("Please enter the end of the range:");
					toAmount = UserInput.getAmount();
				}
				if (filter == 3) System.out.println("(1) - Filter transactions after a given date\n"
						+ "(2) - Filter transactions before a given date\n"
						+ "(3) - Filter transactions on a given date");
				
				int specifics = filter == 0 || filter == 2 ? 0 : UserInput.getFilterSpecifics();
				

				
				
				Date date = null;
				if (filter == 3){
					System.out.println("Please enther the date in the format of YYYY-MM-DD:");
					date = UserInput.getDate();
				}
				
				db.getTransactionHistory(id, filter, specifics, fromAmount, toAmount, date);
				
				System.out.println("\nReturning to menu...");
				Menu(id);
				break;
			case 0:
				System.out.println("Press ENTER to exit..."); input.nextLine(); input.close();
				try {
					DriverManager.getConnection("jdbc:derby:;shutdown=true");
				} catch (SQLException se) {
					// SQL State XJO15 and SQLCode 50000 mean an OK shutdown.
					if (!(se.getErrorCode() == 50000) && (se.getSQLState().equals("XJ015"))) {
						System.err.println(se);
					}
				}
				break;
		}
	}
}
