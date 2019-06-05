package consolebank;

import java.sql.Timestamp;

public class Transaction {
	private int id;
	private int userId;
	private String type;
	private long amount;
	private Timestamp trTime;

	public Transaction(int id, int userId, String type, long amount, Timestamp trTime) {
		this.id = id;
		this.userId = userId;
		this.type = type;
		this.amount = amount;
		this.trTime = trTime;
	}
	
	public Transaction(int userId, String type, long amount) {
		this.userId = userId;
		this.type = type;
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public Timestamp getTrTime() {
		return trTime;
	}

	public void setTrTime(Timestamp trTime) {
		this.trTime = trTime;
	}

	
}
