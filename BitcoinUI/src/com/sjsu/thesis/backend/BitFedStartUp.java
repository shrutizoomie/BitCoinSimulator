/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sjsu.thesis.backend;

import com.sjsu.thesis.connection.BitFedBroadcastSender;
import com.sjsu.thesis.connection.MulticastReceiver1;
import com.sjsu.thesis.connection.MulticastReceiver2;
import com.sjsu.thesis.connection.MulticastReceiver3;
import com.sjsu.thesis.connection.MulticastReceiver4;
import com.sjsu.thesis.connection.MulticastReceiver5;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shruti
 * This file will startup all the Miner nodes which act as the main receivers of the data.
 * It will also create the BitFedBroadcastSender
 */
public class BitFedStartUp {
    
    public static final String PRIVATE_KEY_FILE = "privatekey.txt";
    public static final String PUBLIC_KEY_FILE = "publickey.txt";
   
    public static void main(String[] args)
    {
        try {
            //Create Bit Fed Broadcast Sender
            BitFedBroadcastSender sender = new BitFedBroadcastSender();
            
            // Create a group of Miners : Receivers of the Broadcast msg
            MulticastReceiver1 receiver1 = new MulticastReceiver1();
	    MulticastReceiver2 receiver2 = new MulticastReceiver2();
//            MulticastReceiver3 receiver3 = new MulticastReceiver3();
//            MulticastReceiver4 receiver4 = new MulticastReceiver4();
//            MulticastReceiver5 receiver5 = new MulticastReceiver5();
//            
            //Create a set of Nodes: By creating their keys
            
            BitFedGenKeyPair generateKeyPair = new BitFedGenKeyPair();
            generateKeyPair.generateKey(PRIVATE_KEY_FILE, PUBLIC_KEY_FILE);
      
            // generate keys for 5 more nodes 
            for (int i = 0; i < 5; i ++)
            {
                 BitFedGenKeyPair genKeyPair = new BitFedGenKeyPair();
                 genKeyPair.generateKey( i + "private.txt", i + "public.txt");
            }
		
            sender.sendBroadcast();
            
        } catch (IOException ex) {
            Logger.getLogger(BitFedStartUp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
}
