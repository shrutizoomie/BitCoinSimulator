package com.sjsu.java.transaction;

public class TransferData {
	private int senderID;
	private int receiverID;
	private int amount;
	private int balance;

	public TransferData(int senderID, int receiverID, int amount, int balance) {
		super();
		this.senderID = senderID;
		this.receiverID = receiverID;
		this.amount = amount;
		this.balance = balance;
		
		/* Write the ledger to a block chain*/
	}
	
	@Override
	public String toString() {
		//return senderID + " " + receiverID + " " + amount + " " + balance;
		return Integer.toString(senderID);
				}
}
