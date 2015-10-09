/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sjsu.thesis.backend;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Creates ArrayList of blocks with add, get, set, and toString methods.
 * 
 * @author Shruti
 * @version 1.0.0
 */

@SuppressWarnings("serial")
public class BitFedBlockChain implements Serializable
{
	private ArrayList<BitFedBlock> chain;

	public BitFedBlockChain()
	{
		super();
		chain = new ArrayList<>();
	}

	public void addBlock(BitFedBlock b)
	{
		chain.add(b);
	}

	public int getChainSize()
	{
		return chain.size();
	}

	public ArrayList<BitFedBlock> getChain()
	{
		return chain;
	}

	public void setChain(ArrayList<BitFedBlock> chain)
	{
		this.chain = chain;
	}

	public boolean contains(BitFedBlock b)
	{
		return this.chain.contains(b);

	}

	@Override
	public String toString()
	{
		return "BlockChain [chain=" + chain + "]";
	}

}
