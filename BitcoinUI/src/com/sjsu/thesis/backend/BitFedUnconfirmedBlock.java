/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sjsu.thesis.backend;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Niranjan
 * This class will create an unconfirmed block and add it to the block chain
 */
public class BitFedUnconfirmedBlock {
    
    public BitFedBlock block;
    private String solution;
    private BitFedTransaction unconfirmedTransaction;
    
    public BitFedUnconfirmedBlock(BitFedTransaction trans) throws IOException
    {
            this.unconfirmedTransaction = trans;
            block = new BitFedBlock();
            this.solution = null;
            System.out.println("Unconfirmed block created.");
    }
    
    public boolean verifyUnconfirmedBlock()
    {
        try {
            block.addTransaction(unconfirmedTransaction);
            solution = BitFedProofOfWork.findProof(block.toString());
            block.setSolution(solution);
            System.out.println("Solution has been set in block.");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(BitFedUnconfirmedBlock.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
			
    }
    
    
}
