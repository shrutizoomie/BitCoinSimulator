/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sjsu.thesis.backend;

import java.io.IOException;

import com.sjsu.thesis.framework.PeerInfo;

/**
 * Initiates proof-of-work and saves solution into a block object. Adds
 * transaction to block.
 * 
 * @author Shruti
 * @version 1.0.0
 */

public class BitFedMiner implements Runnable
{

	/**
	 * To run this you do the following: Runnable r = new BitFedMiner(); Thread t =
 new Thread(r); t.start(); //this executes the run method in BitFedMiner class.
	 */

	private BitFedBlock block;
	private String solution;
	private BitFedTransaction change;
	private BitFedTransaction unconfirmedTransaction;
	private BitFedNode peer;

	public BitFedMiner(BitFedNode peer) throws IOException
	{
		this.peer = peer;
		this.unconfirmedTransaction = peer.getTransaction();
		System.out.println("Miner.java: peer.getTransaction() called");
		block = new BitFedBlock();
		this.solution = null;
		System.out.println("Miner object created.");
	}

	public void run()
	{
		System.out.println("Mining has begun.");
		peer.setStatus("Mining has begun.");
		try
		{
			block.addTransaction(unconfirmedTransaction);
			System.out.println("Transaction added to block.");
			double total = unconfirmedTransaction.getTransactionTotal() - unconfirmedTransaction.getAmount();
			// t.setAmount(t.getAmount() - BitFedBlock.FEE);
			System.out.println("Miner.java: unconfirmedTransaction.getTransactionTotal() - unconfirmedTransaction.getAmount() " + total);
			if (total > 0)
			{
				change = new BitFedTransaction(unconfirmedTransaction.getSenderPubKey(), total);
				block.addTransaction(change);
				System.out.println("Change transaction added to block.");
			}

			System.out.println("Finding solution...");
			peer.setStatus("Finding solution...");
			solution = BitFedProofOfWork.findProof(block.toString());
			block.setSolution(solution);
			System.out.println("Solution has been set in block.");
			peer.setStatus("Solution has been found. You were awarded: " + BitFedBlock.REWARDAMOUNT);
			for (PeerInfo pid : peer.getAllPeers())
			{
				System.out.println("Broadcasting...");
				peer.connectAndSendObject(pid, BitFedNode.FOUNDSOLUTION, block);
			}
		}
		catch (Exception e)
		{
			System.err.println("Caught exception " + e.toString());
		}
	}

	public BitFedBlock getBlock()
	{
		return block;
	}

	public void addTransactionToBlock(BitFedTransaction trans)
	{
		block.addTransaction(trans);
	}

	public boolean hasATransactionToVerify()
	{
		if (unconfirmedTransaction == null)
			return false;
		else
			return true;
	}

}
