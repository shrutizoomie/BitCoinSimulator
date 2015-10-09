/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sjsu.thesis.backend;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Niranjan
 */
public class ShrutiNode implements Serializable{
    
    // File Names
	private String blockFileName = "blockchain";
	private BitFedBlockChain blockChain;
        
    public ShrutiNode(BitFedBlockChain bc)
    {
        blockChain = bc;
    }
    
    public void saveBlockchain()
	{
            try
            {
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(blockFileName));
                    out.writeObject(this.blockChain);
                    out.close();
            }
            catch (Exception e)
            {
                    e.printStackTrace();
            }
	}    
}
