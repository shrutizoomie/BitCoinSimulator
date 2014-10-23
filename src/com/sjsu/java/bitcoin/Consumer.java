package com.sjsu.java.bitcoin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
 
class Producer extends Thread {
    static final int MAXQUEUE = 1;
    private Vector messages = new Vector();
 
    @Override
    public void run() {
        try {
                putMessage();
                sleep(1000);
        } catch (InterruptedException e) {
        }
    }
 
    private synchronized void putMessage() throws InterruptedException {
 
        while (messages.size() == MAXQUEUE)
            wait();
        
        /* Enter the string to be hashed*/
		System.out.println("Step 1: Enter the string to be hashed");
		String lsUserData = null;
		 
		try{
		    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    lsUserData = bufferRead.readLine();
		    System.out.println(lsUserData);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		
        messages.addElement(lsUserData);
        notify();
    }
 
    // Called by Consumer
    public synchronized String getMessage() throws InterruptedException {
        notify();
        while (messages.size() == 0)
            wait();
        String message = (String) messages.firstElement();
        messages.removeElement(message);
        return message;
    }
}
 
class Consumer extends Thread {
    Producer producer;
 
    Consumer(Producer p) {
        producer = p;
    }
 
    @Override
    public void run() {
    	String message = null;
        try {
                message = producer.getMessage();
                System.out.println("Got message: " + message);
                sleep(2000);
        } catch (InterruptedException e) {
        }
        
        System.out.println("Step 2: Enter the length of leading 0's required in binary string");
		String lsUserInput = null;
		 
		try{
		    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    lsUserInput = bufferRead.readLine();
		    System.out.println(lsUserInput);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		int lLeadingZero = Integer.parseInt(lsUserInput);
		
		System.out.println("Count of the leading number of zeroes required : " + lLeadingZero);
		
		int lZeroCount = 0;
		int lNonce = 0;
		
		do
		{
			lZeroCount = 0;
			
			String lNonceStr = Integer.toString(lNonce);
			lNonceStr.concat(message);
			
	        MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

    public static void main(String args[]) {
        Producer producer = new Producer();
        producer.start();
        new Consumer(producer).start();
    }
}