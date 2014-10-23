package com.sjsu.java.sign;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MulticastReceiver {
	private InetAddress address;
	private MulticastSocket socket;
	private static int SOCKET = 4446;
	
	public MulticastReceiver() throws IOException {
		socket = new MulticastSocket(SOCKET);
        address = InetAddress.getByName("230.0.0.1");
        socket.joinGroup(address);
	}
	
	public static void main(String[] args) throws IOException {
		MulticastReceiver receiver = new MulticastReceiver();
		RecieverListenerThread thread = new RecieverListenerThread(receiver.address, SOCKET);
		thread.start();
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
	         while (true)
	         {
	            byte[] buf = new byte[256];
	            packet = new DatagramPacket(buf, buf.length);
	            socket.receive(packet);
	            String received = new String(packet.getData());
	            
	            System.out.println("Message Received ! \n" + received);
	            
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
