package consolebank;

import java.sql.Date;
import java.util.Scanner;

public class UserInput {
	
	private static final Scanner INPUT = new Scanner(System.in);
	private static final Queries DB = Queries.getInstance();
	
	public static int getUserId() {
		int id = -1;
		boolean parseInt = false;
		while (!parseInt) {
			try {
				id = Integer.parseInt(INPUT.nextLine());
				// to implement: throw exception if id is more than nr of columns
				if (id < 1 || id > DB.getNrOfUsers()) {
					throw new Exception();
				}
				parseInt = true;
			} catch (NumberFormatException ex) {
				System.out.println("Wrong input, try again!" + " (" + ex + ")");
			} catch (Exception ex2) {
				System.out.println("Wrong input, try again!" + " (id is a positive whole number and has to exist in the database)");
			}
		}
		return id;
	}

	public static int getFilter() {
		int filter = -1;
		boolean parseInt = false;
		while (!parseInt) {
			try {
				filter = Integer.parseInt(INPUT.nextLine());
				if (filter < 0 || filter > 3) {
					throw new Exception();
				}
				parseInt = true;
			} catch (NumberFormatException ex) {
				System.out.println("Wrong input, try again!" + " (" + ex + ")");
			} catch (Exception ex2) {
				System.out.println("Wrong input, try again!" + " (choose from the provided options)");
			}
		}
		return filter;
	}

	public static int getFilterSpecifics() {
		int filterSpecifics = -1;
		boolean parseInt = false;
		while (!parseInt) {
			try {
				filterSpecifics = Integer.parseInt(INPUT.nextLine());
				if (filterSpecifics < 0 || filterSpecifics > 3) {
					throw new Exception();
				}
				parseInt = true;
			} catch (NumberFormatException ex) {
				System.out.println("Wrong input, try again!" + " (" + ex + ")");
			} catch (Exception ex2) {
				System.out.println("Wrong input, try again!" + " (choose from the provided options)");
			}
		}
		return filterSpecifics;
	}

	public static long getAmount() {
		long amount = -1;
		boolean parseLong = false;
		while (!parseLong) {
			try {
				amount = Long.parseLong(INPUT.nextLine());
				if (amount < 0) {
					throw new Exception();
				}
				parseLong = true;
			} catch (NumberFormatException ex) {
				System.out.println("Wrong input, try again!" + " (" + ex + ")");
			} catch (Exception ex2) {
				System.out.println("Wrong input, try again!" + " (amount must be a positive whole number)");
			}
		}
		return amount;
	}
	
	public static Date getDate(){
		Date ts = null;
		boolean parseDate = false;
		while (!parseDate) {
			try {
				ts = Date.valueOf(INPUT.nextLine());
				parseDate = true;
			} catch (NumberFormatException ex) {
				System.out.println("Wrong input, try again!" + " (" + ex + ")");
			} 
			catch (IllegalArgumentException ex2) {
				System.out.println("Wrong input, try again!" + " (format must be yyyy-mm-dd)");
			}
		}
		return ts;
	}
}
