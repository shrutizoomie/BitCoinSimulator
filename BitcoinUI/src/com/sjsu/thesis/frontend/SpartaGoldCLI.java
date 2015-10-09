package com.sjsu.thesis.frontend;

import com.sjsu.thesis.framework.PeerInfo;
import com.sjsu.thesis.backend.BitFedNode;

/**
 * 
 * @author mojiarmin
 */
public class SpartaGoldCLI
{
	private BitFedNode peer;

	/**
	 * @param args
	 *            the command line arguments
	 */
	private SpartaGoldCLI(String initialhost, int initialport, int maxpeers, PeerInfo mypd)
	{
		peer = new BitFedNode(maxpeers, mypd);
		peer.buildPeers(initialhost, initialport, 2);

		(new Thread()
		{
			public void run()
			{
				peer.mainLoop();
			}
		}).start();

	}

	public static void main(String[] args) throws Exception
	{
		new SpartaGoldCLI("localhost", 9001, 5, new PeerInfo("localhost", 9002));
		Menu goldMenu = new Menu();
		goldMenu.performMenuOption();

	}
}
