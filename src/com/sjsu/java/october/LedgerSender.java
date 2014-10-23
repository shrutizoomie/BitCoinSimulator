package com.sjsu.java.october;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Scanner;

import javax.crypto.Cipher;

public class LedgerSender {

	public LedgerSender() throws IOException {
		
		/* this is used to create a connection between various users in the bit coin crypto system*/
		this.groupAddress = "230.0.0.1";
		socket = new MulticastSocket(SOCKET);
        address = InetAddress.getByName(groupAddress);
        socket.joinGroup(address);
	}
	
	private String groupAddress;
	private static InetAddress address;
	private static MulticastSocket socket;
	private static int SOCKET = 4446;
		
	public static HashMap<Integer, TransferData> ledgerData = new HashMap<>();
	public static HashMap <Integer, Integer> userBalance = new HashMap<>();
		  
	private static void broadcast(String messageText, String id) {
		
		String toBeSent = "shruti";
		
		byte[] buf = messageText.getBytes();
		try
	      {
	         DatagramPacket packet = new DatagramPacket(buf, buf.length, address, SOCKET);
	         socket.send(packet);
	         System.out.println("Successfully Sent !");
	      }
	      catch (Exception e)
	      {
	         e.printStackTrace();
	      }
	}
	
	public static void main(String[] args) throws IOException {
		
		LedgerSender localLed = new LedgerSender();
		// create a hashmap of 10 users with balance of 100$
		for (int i = 0; i < 10; i++)
		{
			userBalance.put(i, 100);
		}
		
	Scanner in = new Scanner(System.in);
    
	//while (true)
	//{
		// get user input
				
				System.out.println("Step 1: Who is sender of Bit coins? \n");
			    int lSender = in.nextInt();
			    
			    System.out.printf("Step 2 : Who is the receiver of Bit coins?\n");
			    int lReceiver = in.nextInt();
			    
			    System.out.printf("Step 3 : Amount to be transferred?\n");
			    int lAmount = in.nextInt();		    
			    
			    int initBalance = userBalance.get(lSender);
			    int currBalance = 0;
			    String ledger = "";
			    ObjectInputStream inputStream = null;
			    byte[] cipherText = null;
			    
			    if (lAmount > initBalance)
			    {
			    	System.out.printf("You have insufficient amount to transfer ");
			    }
			    else
			    {
			    	currBalance = initBalance - lAmount;
			    	userBalance.put(lSender, currBalance);
			    	
			    	// Add the entry to ledger
				    TransferData ledgerEntry = new TransferData(lSender, lReceiver, lAmount, currBalance);
				    
				    ledger = ledgerEntry.toString();
				    System.out.printf(ledger);
			    }  
			    
			    try
			    {
				    
				    

				    // Printing the Original, Encrypted Text
				    System.out.println("Original: " + ledger);
				    System.out.println("Encrypted: " + cipherText.toString()); 
			    }
			    catch (Exception e)
			    {
			    	e.printStackTrace();
			    }
			    
			    finally {
					if ( inputStream != null) {
						inputStream.close();
					}
			    }
			    /* A broadcast msg that A is sending 10 bit coins to B*/
			    broadcast(cipherText.toString(), Integer.toString(lSender));
		//}
	}	
}
