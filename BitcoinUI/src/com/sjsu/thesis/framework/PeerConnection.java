package com.sjsu.thesis.framework;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

//import peerbase.socket.SocketFactory;
//import peerbase.socket.SocketInterface;

/**
 * Encapsulates a socket connection to a peer, providing simple, reliable send
 * and receive functionality. All data sent to a peer through this class must be
 * formatted as a PeerMessage object.
 * 
 * @author Shruti
 * 
 */
public class PeerConnection
{

	private PeerInfo pd;
	private SocketInterface s;

	/**
	 * Opens a new connection to the specified peer.
	 * 
	 * @param info
	 *            the peer node to connect to
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws UnknownHostException
	 */
	public PeerConnection(PeerInfo info) throws IOException,
			UnknownHostException
	{
		pd = info;
                
                System.out.println(pd.getId());
                System.out.println(pd.getHost());
                System.out.println(pd.getPort());
		s = SocketFactory.getSocketFactory().makeSocket(pd.getHost(),
				pd.getPort());
                //s = SocketFactory.getDefault().createSocket(pd.getHost(),pd.getPort());
                
//                Socket socket = new Socket(pd.getHost(), pd.getPort());
                
//                System.out.println("Socket" + socket.toString());
//                SocketFactory.getSocketFactory().makeSocket(socket);
	}

	/**
	 * Constructs a connection for which a socket has already been opened.
	 * 
	 * @param info
	 * @param socket
	 */
	public PeerConnection(PeerInfo info, SocketInterface socket)
	{
		pd = info;
		s = socket;
	}

	/**
	 * Sends a PeerMessage to the connected peer.
	 * 
	 * @param msg
	 *            the message object to send
	 */
	public void sendData(PeerMessage msg)
	{
            try {
                s.write(msg.toBytes());
            } catch (IOException ex) {
                Logger.getLogger(PeerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	/**
	 * Receives a PeerMessage from the connected peer.
	 * 
	 * @return the message object received, or null if error
	 */
	public PeerMessage recvData()
	{
            // it is normal for EOF to occur if there is no more replies coming
            // back from this connection.
            
            PeerMessage msg = null;
            try {
                msg = new PeerMessage(s);
            } catch (IOException ex) {
                Logger.getLogger(PeerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            return msg;
	}

	public void sendObject(Object ob)
	{
            try {
                s.writeObject(ob);
            } catch (IOException ex) {
                Logger.getLogger(PeerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	/**
	 * Closes the peer connection.
	 */
	public void close()
	{
		if (s != null)
		{
                    try {
                        s.close();
                    } catch (IOException ex) {
                        Logger.getLogger(PeerConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
			s = null;
		}
	}

	public PeerInfo getPeerInfo()
	{
		return pd;
	}

	public String toString()
	{
		return "PeerConnection[" + pd + "]";
	}

}
