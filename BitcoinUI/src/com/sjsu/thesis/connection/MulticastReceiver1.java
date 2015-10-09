package com.sjsu.thesis.connection;

import com.sjsu.thesis.backend.BitFedBlockChain;
import com.sjsu.thesis.backend.BitFedTransaction;
import com.sjsu.thesis.backend.BitFedUnconfirmedBlock;
import com.sjsu.thesis.backend.BitFedVerifySignature;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.SerializationUtils;

public class MulticastReceiver1 {
	
	public InetAddress address;
	
        private MulticastSocket socket1;
	private MulticastSocket socket2;
	private MulticastSocket socket3;
	private static int SOCKET1 = 4446;
        private static int SOCKET2 = 4447;
        private static int SOCKET3 = 4448;
        
	public MulticastReceiver1() throws IOException 
        {
		socket1 = new MulticastSocket(SOCKET1);
		socket2 = new MulticastSocket(SOCKET2);
                socket3 = new MulticastSocket(SOCKET3);
                
                address = InetAddress.getByName("230.0.0.1");
                socket1.joinGroup(address);
                socket2.joinGroup(address);
                socket3.joinGroup(address);
                
                RecieverListenerThread thread = new RecieverListenerThread(this.address, SOCKET1, SOCKET2, SOCKET3); 
		thread.start();
	}
	
	public static class RecieverListenerThread extends Thread
	{  
	   //Network connection details.
	   private int socketNum1;
           private int socketNum2;
           private int socketNum3;
	   private InetAddress address;
           
            // File Names
            private String blockFileName = "blockchain_miner1";
        
            // each miner has his block chain which he sends to other miners to verify.
            //Once it is verified to be correct the block chain is added to the block
            private BitFedBlockChain blockChainMiner1;


	   /**
	      Constructor.
	      
	      @param rbp  Process that this thread belongs to.
	      @param address  Address of the broadcast group.
	      @param socketNum  Socket on which to listen.
	    */
	   public RecieverListenerThread(InetAddress address, int socketNum1, int socketNum2, int socketNum3)
	   {
	      this.address = address;
	      this.socketNum1 = socketNum1;
	      this.socketNum2 = socketNum2;
	      this.socketNum3 = socketNum3;
              
                try
		{
                    boolean checkBlockchainFile = new File(blockFileName).exists();
                    if (checkBlockchainFile)
                    {
                            ObjectInputStream in = new ObjectInputStream(new FileInputStream(blockFileName));
                            this.blockChainMiner1 = (BitFedBlockChain) in.readObject();
                            in.close();
                    }
                    else
                    {
                            blockChainMiner1 = new BitFedBlockChain();
                    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
                   MulticastSocket socket3;
                   
                   socket1 = new MulticastSocket(socketNum1);
                   socket2 = new MulticastSocket(socketNum2);
                   socket3 = new MulticastSocket(socketNum3);
                   
                   socket1.joinGroup(address);
                   socket2.joinGroup(address);
                   socket3.joinGroup(address);
                   
                   DatagramPacket packet;
                   while (true)
                   {
                        System.out.println("Inside Multicast receiver 1");
                        byte[] buf = new byte[2048];
                        packet = new DatagramPacket(buf, buf.length);
                       
                        // broadcast packet from BitFed
                        socket2.receive(packet);
                        System.out.println("Received a packet from Broadcast Receiver");
                        String bitFedData = new String(packet.getData());
                        
                        if(null != bitFedData)
                        {
                            System.out.println();
                            System.out.println("Socket 2: Bit fed data : " + bitFedData);
                        }
                        
                        // Transaction socket
                        socket1.receive(packet);

                        BitFedTransaction transaction = (BitFedTransaction) SerializationUtils.deserialize(packet.getData());
                        
                        if (null != transaction)
                        {
                            System.out.println("-------------------------------------------------------------");

                            String senderKey = transaction.getSenderPubKey();
                            byte[] signed = transaction.getSigned();
                            String trans = transaction.toString();
                            
                            BitFedVerifySignature verifySign = new BitFedVerifySignature(senderKey, signed, trans);
                            
                            if (verifySign.isVerified())
                            {
                                 System.out.println("Signature Verified");
                                 
                                 // create an uncomfirmed block
                                 BitFedUnconfirmedBlock unconfirmedBlock = new BitFedUnconfirmedBlock(transaction);
                                 boolean result = unconfirmedBlock.verifyUnconfirmedBlock();
                                 
                                 if (result)
                                 {
                                    blockChainMiner1.addBlock(unconfirmedBlock.block);
                                     
                                    // save the block to block chain once it is confirmed
                                    saveBlockchain();
                                    System.out.println("Block chain is verified");
                                    
                                    // broadcast the block chain to others to verify
                                    
                                    //retrieve and send complete block chain
                                    
                                    byte[] data = SerializationUtils.serialize((Serializable) blockChainMiner1);
                                    DatagramPacket blockPacket = new DatagramPacket(data, data.length, address, SOCKET3);
                                    socket3.send(blockPacket);
                                    
                                    System.out.println("Block broadcasted to all");
                                      
                        
                                 }  
                            }
                            
                            System.out.println("-------------------------------------------------------------");
                            
                            
                        }
                   }
               } catch (IOException ex) {
                   Logger.getLogger(MulticastReceiver1.class.getName()).log(Level.SEVERE, null, ex);
               }
	   }
           
           public void saveBlockchain()
           {
		try
		{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(blockFileName));
			out.writeObject(this.blockChainMiner1);
			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
            }

	}
}
