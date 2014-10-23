package com.sjsu.java.broadcast;


/**
   This class represents a process where a message is delayed.
   We use this to illustrate how other classes will respond.
   
   @author Tom Austin
 */
public class DelayedMessageProcess extends ReliableBroadcastProcess
{
   private Message delayedMessage;
   
   /**
      Constructor.
    */
   public DelayedMessageProcess(String ID)
   {
      super(ID);
      delayedMessage = null;
   }
   
   /**
      In this version of the broadcast algorithm, every other
      message will be delayed and sent after the following message.
      This is done to simulate a network delay.
      
      @see ReliableBroadcastProcess#broadcast(java.lang.String)
    */
   public void broadcast(String messageText)
   {
      Message m = new Message(messageText, super.procID, messageSentCount++);
      
      if (delayedMessage==null)
      {
         delayedMessage = m;
      }
      else
      {
         sendToAll(m);
         try
         {
            Thread.sleep(1000);
         }
         catch (InterruptedException e)
         {
            e.printStackTrace();
         }
         sendToAll(delayedMessage);
         delayedMessage = null;
      }
   }
   
   /**
      Calls the start method for this class.
    */
   public static void main(String [] args)
   {
      if (args.length < 1)
      {
         System.out.println("Usage: java DelayedMessageProcess <process name>");
         System.exit(0);
      }
      DelayedMessageProcess delayed = new DelayedMessageProcess(args[0]);
      delayed.start();
   }
}
