package com.sjsu.java.transaction;

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
	
	/* String to hold name of the encryption algorithm.*/
	public static final String ALGORITHM = "RSA";

	/* String to hold the name of the private key file.*/
	public static final String PRIVATE_KEY_FILE = "C:/keys/private.key";

	/* String to hold name of the public key file.*/
	public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";
	
	public static HashMap<Integer, TransferData> ledgerData = new HashMap<>();
	public static HashMap <Integer, Integer> userBalance = new HashMap<>();
	
	  /**
	   * Generate key which contains a pair of private and public key using 1024
	   * bytes. Store the set of keys in Prvate.key and Public.key files.
	   * 
	   * @throws NoSuchAlgorithmException
	   * @throws IOException
	   * @throws FileNotFoundException
	   */
	  public static void generateKey() {
	    try {
	      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
	      keyGen.initialize(2048);
	      final KeyPair key = keyGen.generateKeyPair();

	      File privateKeyFile = new File(PRIVATE_KEY_FILE);
	      File publicKeyFile = new File(PUBLIC_KEY_FILE);

	      // Create files to store public and private key
	      if (privateKeyFile.getParentFile() != null) {
	        privateKeyFile.getParentFile().mkdirs();
	      }
	      privateKeyFile.createNewFile();

	      if (publicKeyFile.getParentFile() != null) {
	        publicKeyFile.getParentFile().mkdirs();
	      }
	      publicKeyFile.createNewFile();

	      // Saving the Public key in a file
	      ObjectOutputStream publicKeyOS = new ObjectOutputStream(
	          new FileOutputStream(publicKeyFile));
	      publicKeyOS.writeObject(key.getPublic());
	      publicKeyOS.close();

	      // Saving the Private key in a file
	      ObjectOutputStream privateKeyOS = new ObjectOutputStream(
	          new FileOutputStream(privateKeyFile));
	      privateKeyOS.writeObject(key.getPrivate());
	      privateKeyOS.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	  
	  /**
	   * The method checks if the pair of public and private key has been generated.
	   * 
	   * @return flag indicating if the pair of keys were generated.
	   */
	  public static boolean areKeysPresent() {

	    File privateKey = new File(PRIVATE_KEY_FILE);
	    File publicKey = new File(PUBLIC_KEY_FILE);

	    if (privateKey.exists() && publicKey.exists()) {
	      return true;
	    }
	    return false;
	  }

	  /**
	   * Sign the plain text using private key.
	   * 
	   * @param text
	   *          : original plain text
	   * @param key
	   *          :The private key
	   * @return Encrypted text
	   * @throws java.lang.Exception
	   */
	  public static byte[] encrypt(String text, PrivateKey key) {
	    byte[] cipherText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance(ALGORITHM);
	      // encrypt the plain text using the private key
	      cipher.init(Cipher.ENCRYPT_MODE, key);
	      cipherText = cipher.doFinal(text.getBytes());
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return cipherText;
	  }
	  
	  
	private static void broadcast(String messageText, String id) {
		//String toBeSent = messageText + ":::" + id;
		
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
				    // Check if the pair of keys are present else generate those.
				    if (!areKeysPresent()) {
				      // Method generates a pair of keys using the RSA algorithm and stores it
				      // in their respective files
				      generateKey();
				    }
				    
				    inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
				    final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
				    
				    cipherText = encrypt(ledger, privateKey);

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
