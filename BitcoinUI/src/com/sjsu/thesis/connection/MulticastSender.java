package com.sjsu.thesis.connection;

import java.io.*;
import java.net.*;
import org.apache.commons.lang3.SerializationUtils;


public class MulticastSender 
{
	private String groupAddress;
	private InetAddress address;
	private MulticastSocket socket;
	private static int SOCKET = 4446;
	
	public MulticastSender() throws IOException 
        {
		this.groupAddress = "230.0.0.1";
		socket = new MulticastSocket(SOCKET);
                address = InetAddress.getByName(groupAddress);
                socket.joinGroup(address);
	}
	

	public void send(Object objdata) 
        {
            try 
            {
                byte[] data = SerializationUtils.serialize((Serializable) objdata);
                DatagramPacket packet = new DatagramPacket(data, data.length, address, SOCKET);
                socket.send(packet);
                System.out.println("Successfully Sent !");
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
	}
}
