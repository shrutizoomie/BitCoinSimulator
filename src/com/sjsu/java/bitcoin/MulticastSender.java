package com.sjsu.java.bitcoin;

import java.io.*;
import java.net.*;


public class MulticastSender {
/*		  public static void main(String[] args) {
	    DatagramSocket socket = null;
	    DatagramPacket outPacket = null;
	    byte[] outBuf;
	    final int PORT = 8888;
	 
	    try {
	      socket = new DatagramSocket();
	      long counter = 0;
	      String msg;
	 
	      while (true) {
	        msg = "This is multicast! " + counter;
	        counter++;
	        outBuf = msg.getBytes();
	 
	        //Send to multicast IP address and port
	        InetAddress address = InetAddress.getByName("224.2.2.3");
	        outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
	 
	        socket.send(outPacket);
	 
	        System.out.println("Server sends : " + msg);
	        try {
	          Thread.sleep(500);
	        } catch (InterruptedException ie) {
	        }
	      }
	    } catch (IOException ioe) {
	      System.out.println(ioe);
	    }
	  }
*/	
	
	private String groupAddress;
	private InetAddress address;
	private MulticastSocket socket;
	private static int SOCKET = 4446;
	
	public MulticastSender() throws IOException {
		this.groupAddress = "230.0.0.1";
		socket = new MulticastSocket(SOCKET);
        address = InetAddress.getByName(groupAddress);
        socket.joinGroup(address);
	}
	
	public static void main(String[] args) throws IOException {
		MulticastSender sender = new MulticastSender();
		sender.send("IamSender");
//		sender.send(args[0]);
	}

	private void send(String id) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
         try
         {
            String messageText = reader.readLine();
            broadcast(messageText, id);
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
	}

	private void broadcast(String messageText, String id) {
		String toBeSent = messageText + ":::" + id;
		
		byte[] buf = toBeSent.getBytes();
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

}
