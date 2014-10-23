package com.sjsu.java.transaction;

/* This class is used to maintain the user ID and the balance for each user*/
public class Balance {
	private int userID;
	private int userBalance;
	
	public Balance(int userID, int userBalance) {
		super();
		this.userID = userID;
		this.userBalance = userBalance;
	}
}
