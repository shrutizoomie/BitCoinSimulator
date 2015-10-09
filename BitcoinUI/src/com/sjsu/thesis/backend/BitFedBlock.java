/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sjsu.thesis.backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Contains a reward transaction awarded to the specified miner, the solution
 * found, and an ArrayList of transactions solved. Get, set, add, and toString
 * methods provide access to instance variables.
 * 
 * @author Shruti
 * @version 1.0.0
 */

@SuppressWarnings("serial")
public class BitFedBlock implements Serializable
{
	private String id;
	private String solution;
	private String minerPubKey;
	private String previousBlockID;
	private ArrayList<BitFedTransaction> transactionList;
	public static final double FEE = 0.1;
	public static final double REWARDAMOUNT = 50;

	public BitFedBlock() throws IOException
	{
		super();
		id = UUID.randomUUID().toString();
		BufferedReader br = new BufferedReader(new FileReader("publickey.txt"));
		minerPubKey = br.readLine();
		br.close();
		transactionList = new ArrayList<BitFedTransaction>();
		// transactionList.add(new BitFedTransaction(minerPubKey, REWARDAMOUNT +
		// FEE));
//		transactionList.add(new BitFedTransaction(minerPubKey, REWARDAMOUNT));
//		System.out.println("New block created - ID: " + id);
	}

	public String getPreviousBlockID()
	{
		return previousBlockID;
	}

	public void setPreviousBlockID(String previousBlockID)
	{
		this.previousBlockID = previousBlockID;
	}

	public String getMinerPubKey()
	{
		return minerPubKey;
	}

	public void setMinerPubKey(String minerPubKey)
	{
		this.minerPubKey = minerPubKey;
	}

	public String getSolution()
	{
		return solution;
	}

	public void setSolution(String solution)
	{
		this.solution = solution;
	}

	public String getId()
	{
		return id;
	}

	public ArrayList<BitFedTransaction> getTransactions()
	{
		return transactionList;
	}

        public void addTransaction(BitFedTransaction t)
	{
		transactionList.add(t);
	}

	@Override
	public String toString()
	{
		return "Block [id=" + id + ", minerPubKey=" + minerPubKey + ", previousBlockID=" + previousBlockID + ", trans=" + transactionList + "]";
	}

	@Override
	public boolean equals(Object object)
	{
		boolean sameSame = false;

		if (object != null && object instanceof BitFedBlock)
		{
			sameSame = this.id.equals(((BitFedBlock) object).id);
		}

		return sameSame;
	}
}