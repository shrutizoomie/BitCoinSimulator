package com.sjsu.java.sign;

import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;


public class MulticastSender {	
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
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		MulticastSender sender = new MulticastSender();
		
		String toBeSent = "IamSender";
		
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
	    kpg.initialize(1024);
	    KeyPair keyPair = kpg.genKeyPair();
	    
	    System.out.println(keyPair.toString());

	    byte[] data = toBeSent.getBytes("UTF8");

	    Signature sig = Signature.getInstance("MD5WithRSA");
	    sig.initSign(keyPair.getPrivate());
	    sig.update(data);
	    byte[] signatureBytes = sig.sign();

		sender.send(signatureBytes);
//		sender.send(args[0]);
	}

	private void send(byte [] signatureBytes) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
         try
         {
            String messageText = reader.readLine();
            String value = new String(signatureBytes, "UTF-8");
            broadcast(messageText, value);
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
