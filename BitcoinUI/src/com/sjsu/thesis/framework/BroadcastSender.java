package com.sjsu.thesis.framework;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.PublicKey;

public class BroadcastSender {

	public BroadcastSender() throws IOException {
		
		this.groupAddress = "230.0.0.1";
		socket = new MulticastSocket(SOCKET);
        address = InetAddress.getByName(groupAddress);
        socket.joinGroup(address);
	}
	
	private String groupAddress;
	private static InetAddress address;
	private static MulticastSocket socket;
	private static int SOCKET = 4446;
	
	/** String to hold name of the message file to be signed. */
    public static final String MESSAGE = "C:/keys/message.txt";
    
	/** String to hold name of the public key file. */
	public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";
	  
	/** String to hold name of the signature file generated after signing the message.*/
	public static final String SIGNATURE = "C:/keys/signature";
    
	private static void broadcast() throws FileNotFoundException, IOException, ClassNotFoundException 
	{
		/*
		 *  Line1  M:2-2;S:3-3;K:4-4
		 *  Line2  This is the message
		 *  Line3  My Sign - Shruti
		 *  Line4  My public key
		 *  
		 *  
		 *  class Message {
		 *  	String message;
		 *  	PublicKey publicKey
		 *  	Sign sign
		 *  }
		 */
		ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
        PublicKey pubkey = (PublicKey) keyIn.readObject();
		keyIn.close();
        
        File infile = new File(SIGNATURE);
        DataInputStream in = new DataInputStream(new FileInputStream(infile));
        int signlength = in.readInt();
       
        System.out.println("Signlength : " + signlength);
        
        byte[] signature = new byte[signlength];
        in.read(signature, 0, signlength);
        

		MessageSend m = new MessageSend("MyFakeMessage", pubkey, signature);
		String myMsg = "MyFakeMessage";
		
		byte[] buf = myMsg.getBytes();
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
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		/* A broadcast msg that A is sending 10 bit coins to B*/
		broadcast();
	}
	
	static class MessageSend {
		String message;
		PublicKey publicKey;
		byte[] signature;
		
		public MessageSend(String message, PublicKey publicKey, byte[] signature) {
			super();
			this.message = message;
			this.publicKey = publicKey;
			this.signature = signature;
		}
		
		@Override
		public String toString() {
			return message + "<EOM>\n" + publicKey + "<EOM>\n" + signature;
		}	
	}
}
