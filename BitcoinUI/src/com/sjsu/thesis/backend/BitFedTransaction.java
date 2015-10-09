/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sjsu.thesis.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;


/**
 * BitFedTransaction object which contains all necessary information to conduct a
 valid transaction. Contains a get method for all instance variables, and a
 * set function for variables allowed to change.
 * 
 * @author Shruti
 * @version 1.0.0
 */

@SuppressWarnings("serial")
public class BitFedTransaction implements Serializable
{
	private String trans;
	private String senderPubKey;
	private byte[] signed;
	private String id;
	private double amount;
	private String receiverPubKey;
	private boolean isSpent;
	private ArrayList<String> unspentIds;
	private double transactionTotal;
	private String date;

	/**
	 * Constructor initializes instance variables with parameters.
	 * 
	 * @param receiverPubKey
	 *            String of receiver's public key
	 * @param amount
	 *            double number of amount being sent
	 */

	public BitFedTransaction(String receiverPubKey, double amount)
	{
		this.transactionTotal = 0;
		this.unspentIds = new ArrayList<String>();
		this.receiverPubKey = receiverPubKey;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date d = new Date();
		date = dateFormat.format(d);
                System.out.println("BitFedTransaction : Receiver public key :" + receiverPubKey);
		Scanner in;
		try
		{
			in = new Scanner(new File("publickey.txt"));
			senderPubKey = in.next();
                        
                        System.out.println("Senders Public Key:" + senderPubKey);
			in.close();
			this.amount = amount;
			isSpent = false;
			id = UUID.randomUUID().toString();
			trans = id + this.receiverPubKey + amount;
			signed = BitFedSignTransaction.sign(trans);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		//LoggerUtil.getLogger().fine("New transaction created - ID: " + id);
                System.out.println("New transaction created - ID" + id);
	}

	public String getID()
	{
		return id;
	}

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(Double a)
	{
		this.amount = a;
	}

	public String getReceiverPubKey()
	{
		return receiverPubKey;
	}

	public boolean isSpent()
	{
		return isSpent;
	}

	public String getSenderPubKey()
	{
		return senderPubKey;
	}

	public String toString()
	{
		return trans;
	}

	public byte[] getSigned()
	{
		return signed;
	}

	public void setSenderPubKey(String senderPubKey)
	{
		this.senderPubKey = senderPubKey;
	}

	public void setSpent(boolean isSpent)
	{
		this.isSpent = isSpent;
	}

	public void addUnspentIds(BitFedBlockChain bc)
	{
		if (amount > 0)
		{
			for (int i = 0; i < bc.getChainSize(); i++)
			{
				BitFedBlock tempBlock = bc.getChain().get(i);
				for (BitFedTransaction t : tempBlock.getTransactions())
				{
					System.out.println("Transaction.java transaction amount: ------------------->" + t.getAmount());
					System.out.println("Transaction.java receiver pub: " + t.getReceiverPubKey());
					System.out.println("Transaction.java my pub: " + senderPubKey);
					System.out.println("Transaction.java  is transaction spent?" + t.isSpent);
					if (t.getReceiverPubKey().equals(senderPubKey) && t.isSpent() == false)
					{
						System.out.println("Transaction.java TransactionHandler: if statement chekcking pub");
						System.out.println(t.getAmount());
						transactionTotal += t.getAmount();
						unspentIds.add(t.getID());
					}
					if (transactionTotal >= amount)
					{
						i = bc.getChainSize();
						System.out.println("Transaction.java transactionTotal >= amount");
					}
				}
			}
			System.out.println("Transaction.java amount:" + amount);
			System.out.println("Transaction.java transactionTotal:" + transactionTotal);
			if (transactionTotal < amount)
			{
				System.out.println("SpartagoldNode.java TransactionHandler transactionTotal < amount");
				unspentIds = null;
			}
		}
		System.out.println("Transaction.java unspendIds:" + unspentIds);
	}

	public ArrayList<String> getUnspentIds()
	{
		return unspentIds;
	}

	public double getTransactionTotal()
	{
		return transactionTotal;
	}

	public void setTransactionTotal(double transactionTotal)
	{
		this.transactionTotal = transactionTotal;
	}

	public String getDate()
	{
		return date;
	}

	public boolean equals(Object object)
	{
		boolean sameSame = false;

		if (object != null && object instanceof BitFedTransaction)
		{
			sameSame = this.id.equals(((BitFedTransaction) object).id);
		}

		return sameSame;
	}
}