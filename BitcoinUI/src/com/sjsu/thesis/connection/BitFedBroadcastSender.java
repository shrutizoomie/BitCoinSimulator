package com.sjsu.thesis.connection;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BitFedBroadcastSender 
{
	private String groupAddress;
	private InetAddress address;
	private MulticastSocket socket;
	private static int SOCKET = 4447;
        
        public static final String BITFED_RATE_FILE = "bitfedRate.txt";
	
	public BitFedBroadcastSender() throws IOException 
        {
		this.groupAddress = "230.0.0.1";
		socket = new MulticastSocket(SOCKET);
                address = InetAddress.getByName(groupAddress);
                socket.joinGroup(address);
	}
	
	public void sendBroadcast()
        {    
            BufferedWriter writer1 = null;
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                System.out.println(dateFormat.format(date));
                Random rand = new Random();
                int rate = rand.nextInt(10);
                String bitFedMsg = rate + "#" + dateFormat.format(date);
                send(rate + "#" + dateFormat.format(date));
                // write the rate to file and the time that the message was generated
                writer1 = new BufferedWriter(new FileWriter(BITFED_RATE_FILE));
                writer1.write(bitFedMsg);
                writer1.close();
            } catch (IOException ex) {
                Logger.getLogger(BitFedBroadcastSender.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    writer1.close();
                } catch (IOException ex) {
                    Logger.getLogger(BitFedBroadcastSender.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
	}

	private void send(String id) 
        {
              byte[] buf = id.getBytes();
              try
	      {
	         DatagramPacket packet = new DatagramPacket(buf, buf.length, address, SOCKET);
	         socket.send(packet);
	         System.out.println("Successfully Sent Cost to All the nodes !");
	      }
	      catch (Exception e)
	      {
	         e.printStackTrace();
	      }
	}
}
