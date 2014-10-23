package com.sjsu.java.sign;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class ReceiveSignedDES {
	private InetAddress address;
	private MulticastSocket socket;
	private static int SOCKET = 4446;

	/**
	   * String to hold name of the encryption algorithm.
	   */
	  public static final String ALGORITHM = "RSA";

	  /**
	   * String to hold the name of the private key file.
	   */
	  public static final String PRIVATE_KEY_FILE = "C:/keys/private.key";

	  /**
	   * String to hold name of the public key file.
	   */
	  public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";


	public ReceiveSignedDES() throws IOException {
		socket = new MulticastSocket(SOCKET);
        address = InetAddress.getByName("230.0.0.1");
        socket.joinGroup(address);
	}
	
	public static void main(String[] args) throws IOException {
		ReceiveSignedDES receiver = new ReceiveSignedDES();
		System.out.println("Inside the Main of ReceiveSigned");
		RecieverListenerThread thread = new RecieverListenerThread(receiver.address, SOCKET);
		thread.start();
	}
	
	/**
	   * Decrypt text using private key.
	   * 
	   * @param text
	   *          :encrypted text
	   * @param key
	   *          :The private key
	   * @return plain text
	   * @throws java.lang.Exception
	   */
	  public static String decrypt(byte[] text, PublicKey key) {
	    byte[] dectyptedText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance(ALGORITHM);

	      // decrypt the text using the private key
	      cipher.init(Cipher.DECRYPT_MODE, key);
	      dectyptedText = cipher.doFinal(text);

	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }

	    return new String(dectyptedText);
	  }

	public static class RecieverListenerThread extends Thread
	{  
	   //Network connection details.
	   private int socketNum;
	   private InetAddress address;

	   /**
	      Constructor.
	      
	      @param rbp  Process that this thread belongs to.
	      @param address  Address of the broadcast group.
	      @param socketNum  Socket on which to listen.
	    */
	   public RecieverListenerThread(InetAddress address, int socketNum)
	   {
	      this.address = address;
	      this.socketNum = socketNum;
	   }
	   
	   /**
	      Starts the thread.  It will listen for broadcast messages and report
	      them to its corresponding process.
	      
	      @see java.lang.Runnable#run()
	    */
	   public void run()
	   {
	      MulticastSocket socket;
	      try
	      {
	         socket = new MulticastSocket(socketNum);
	         socket.joinGroup(address);
	   
	         DatagramPacket packet;
	         
	         
	         System.out.println("Run method");
	         
	         while (true)
	         {
	            byte[] buf = new byte[256];
	            packet = new DatagramPacket(buf, buf.length);
	            socket.receive(packet);
	            String received = new String(packet.getData());
	            
	            System.out.println("Message Received ! \n" + received);
	            
	            ObjectInputStream inputStream = null;
	            
	            // Decrypt the cipher text using the public key.
	            inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
	            final PublicKey publicKey = (PublicKey) inputStream.readObject();
	            
	            byte[] bRecBytes = received.getBytes();
	            final String plainText = decrypt(bRecBytes, publicKey);

	            System.out.println("Decrypted: " + plainText);

	            
	            
	            
	            int lLeadingZero = 5;
	            int lZeroCount = 0;
	    		int lNonce = 0;
	    		
	    		do
	    		{
	    			lZeroCount = 0;
	    			
	    			String lNonceStr = Integer.toString(lNonce);
	    			lNonceStr.concat(received);
	    			
	    	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	    	        md.update(lNonceStr.getBytes());
	    	 
	    	        byte byteData[] = md.digest();
	    	 
	    	        //convert the byte to hex format method 1
	    	        StringBuffer sb = new StringBuffer();
	    	        for (int i = 0; i < byteData.length; i++) {
	    	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	    	        }
	    	 
	    	        System.out.println("Hex format : " + sb.toString());
	    	 
	    	        //convert the byte to hex format method 2
	    	        StringBuffer hexString = new StringBuffer();
	    	    	for (int i=0;i<byteData.length;i++) {
	    	    		String hex=Integer.toHexString(0xff & byteData[i]);
	    	   	     	if(hex.length()==1) hexString.append('0');
	    	   	     	hexString.append(hex);
	    	    	}
	    	    	
	    	    	String hashBlock = hexString.toString();
	    	    	//System.out.println("Hex format : " + hashBlock);
	    	
	    	    	String binConv = hexToBin(hashBlock);
	    	    	System.out.println("Bin format : " + binConv);
	    	    	
	    	    	/* Check the number of leading 0's in the binary string*/
	    	    	
	    	    	for(int i = 0; i < binConv.length(); i++)
	    	    	{
	    	    		if('1' != binConv.charAt(i))
	    	    		{
	    	    			lZeroCount++;
	    	    			
	    	    		}
	    	    		else
	    	    		{
	    	    			break;
	    	    		}
	    	    	}
	    	    	
	    	    	lNonce++;
	    	    	
	    	    	System.out.println("Incremented Nonce : " + lNonce);
	    	    	System.out.println("Current 0 count : " + lZeroCount);
	    		}while(lLeadingZero != lZeroCount);

	            
	            
	            
	            stop();
	         }
	      }
	      catch (IOException e)
	      {
	         e.printStackTrace();
	      } catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }

	}
	
	static String hexToBin(String hex){
	    String bin = "";
	    String binFragment = "";
	    int iHex;
	    hex = hex.trim();
	    hex = hex.replaceFirst("0x", "");

	    for(int i = 0; i < hex.length(); i++){
	        iHex = Integer.parseInt(""+hex.charAt(i),16);
	        binFragment = Integer.toBinaryString(iHex);

	        while(binFragment.length() < 4){
	            binFragment = "0" + binFragment;
	        }
	        bin += binFragment;
	    }
	    return bin;
	}
}
