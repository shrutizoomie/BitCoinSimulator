package com.sjsu.thesis.connection;

import com.sjsu.thesis.backend.BitFedBlock;
import com.sjsu.thesis.backend.BitFedTransaction;
import com.sjsu.thesis.backend.BitFedVerify;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.SerializationUtils;

public class MulticastReceiver4 {
	
	public InetAddress address;
	
        private MulticastSocket socket1;
	private MulticastSocket socket2;
	private static int SOCKET1 = 4446;
        private static int SOCKET2 = 4447;

	public MulticastReceiver4() throws IOException 
        {
		socket1 = new MulticastSocket(SOCKET1);
		socket2 = new MulticastSocket(SOCKET2);
                
                address = InetAddress.getByName("230.0.0.1");
                socket1.joinGroup(address);
                socket2.joinGroup(address);
                
                
                RecieverListenerThread thread = new RecieverListenerThread(this.address, SOCKET1, SOCKET2);
                
		thread.start();
	}
	
	public static class RecieverListenerThread extends Thread
	{  
	   //Network connection details.
	   private int socketNum1;
           private int socketNum2;
	   private InetAddress address;

	   /**
	      Constructor.
	      
	      @param rbp  Process that this thread belongs to.
	      @param address  Address of the broadcast group.
	      @param socketNum  Socket on which to listen.
	    */
	   public RecieverListenerThread(InetAddress address, int socketNum1, int socketNum2)
	   {
	      this.address = address;
	      this.socketNum1 = socketNum1;
	      this.socketNum2 = socketNum2;
	   }
	   
	   /**
	      Starts the thread.  It will listen for broadcast messages and report
	      them to its corresponding process.
	      
	      @see java.lang.Runnable#run()
	    */
	   public void run()
	   {
               try {
                   MulticastSocket socket1;
                   MulticastSocket socket2;
                   
                   socket1 = new MulticastSocket(socketNum1);
                   socket2 = new MulticastSocket(socketNum2);
                   socket1.joinGroup(address);
                   socket2.joinGroup(address);
                   
                   DatagramPacket packet;
                   while (true)
                   {
                        byte[] buf = new byte[2048];
                        packet = new DatagramPacket(buf, buf.length);
                       
                        socket2.receive(packet);
                        System.out.println("Received a packet from Broadcast Receiver");
                        String bitFedData = new String(packet.getData());
                        
                        if(null != bitFedData)
                        {
                            System.out.println();
                            System.out.println("Socket 2: Bit fed data : " + bitFedData);
                        }
                        
                        socket1.receive(packet);

                        BitFedBlock b = (BitFedBlock) SerializationUtils.deserialize(packet.getData());
                        
                        if (null != b)
                        {
                            System.out.println("ID :" + b.getId());
                            System.out.println("Miner Public key" + b.getMinerPubKey());
                            System.out.println("Prev block ID" + b.getPreviousBlockID());
                            System.out.println("Solution" + b.getSolution());
                            System.out.println("Transactions : " + b.getTransactions());
                       
                            ArrayList<BitFedTransaction> transaction= new ArrayList<BitFedTransaction>();
                            transaction = b.getTransactions();
                       
                            System.out.println("Transaction details are listed below");
                            for(BitFedTransaction tran : transaction)
                            {
                                System.out.println("Transaction amt:" + tran.getAmount());
                                System.out.println("Receiver public key: " + tran.getReceiverPubKey());
                                System.out.println("Sender public key" + tran.getSenderPubKey());
                                System.out.println("To string: " + tran.toString());
                            }
                       
                            boolean solution = false;
                            System.out.println("Verifying block...");
                            solution = BitFedVerify.verifyBlock(b);
                            System.out.println("SolutionFoundHandler: Block verification: " + solution);
                            String received = new String(packet.getData());
                            System.out.println("Message Received ! \n" + received);
                        }
                   }
               } catch (IOException ex) {
                   Logger.getLogger(MulticastReceiver1.class.getName()).log(Level.SEVERE, null, ex);
               } catch (NoSuchAlgorithmException ex) {
                   Logger.getLogger(MulticastReceiver1.class.getName()).log(Level.SEVERE, null, ex);
               }
  
	   }

	}
}
