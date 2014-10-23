package com.sjsu.java.broadcast;

import java.util.ArrayList;
import java.util.List;

/**
   This class represents a Causal Broadcast process built on top
   of a FIFO Broadcast process.  It will guarantee that all messages
   will be delivered only after all messages that it depends upon
   have been delivered.
   
   @author Tom Austin
 */
public class CausalBroadcastProcess extends FifoBroadcastProcess
{
   //Messages C-delivered after this process's last broadcast.
   private List<String> prevDlvrs;
   
   //All messages that have been C-delivered to this process
   private List<Message> messagesDelivered;
   
   public CausalBroadcastProcess(String ID)
   {
      super(ID);
      prevDlvrs = new ArrayList<String>();
      messagesDelivered = new ArrayList<Message>();
   }
   
   /**
      Causal Broadcast (aka C-broadcast) rebroadcasts all messages
      that it received since its last broadcast message.  Because
      the other processes will deliver the messages from this process
      in FIFO order, we can guarantee that all possible message
      dependencies will be met.
       
      @see ReliableBroadcastProcess#broadcast(java.lang.String)
    */
   public void broadcast(String messageText)
   {
      for (String prevText : prevDlvrs)
      {
         super.broadcast(prevText);
      }
      super.broadcast(messageText);
      
      //We have now resubmitted all messages that may have been
      // dependencies.  We can clear the list and start fresh.
      prevDlvrs = new ArrayList<String>();
   }
   
   /**
      When a message has been F-delivered, we do not
      always want to C-deliver it, since it may have
      been received already.  (This is because
      Causal Broadcasts re-broadcast many messages).
      Messages that have been fDelivered before will
      now be suppressed.
      
      @see FifoBroadcastProcess#fDeliver(Message)
    */
   protected void fDeliver(Message m)
   {
      boolean alreadyDelivered = false;
      for (Message delivMess : messagesDelivered)
      {
         //This is cheating...  Our messages are not tagged
         // with a unique ID, so we assume that they are the
         // same if they have the same text.  In real life,
         // this would be very bad, but for a demo, it will do.
         if (m.toString().equals(delivMess.toString()))
            alreadyDelivered = true;
      }
      if(!alreadyDelivered) cDeliver(m);
   }
   
   /**
      When this method is called, the message has finally
      been C-delivered to the process.
    */
   protected void cDeliver(Message m)
   {
      super.fDeliver(m);
      messagesDelivered.add(m);
      prevDlvrs.add(m.toString());
   }
   
   
   /**
      Calls the start method for this class.
    */
   public static void main(String[] args)
   {
      if (args.length < 1)
      {
         System.out.println("Usage: java CausalBroadcastProcess <process name>");
         System.exit(0);
      }
      CausalBroadcastProcess cbp = new CausalBroadcastProcess(args[0]);
      cbp.start();
   }
}
