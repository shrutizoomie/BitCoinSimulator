package com.sjsu.thesis.framework;



/**
 * Interface for objects that may be used to 'stabilize' the state
 * of a peer-to-peer network, or that, in general, may be periodically 
 * invoked by a protocol.
 * 
 * @author Shruti
 *
 **/
public interface StabilizerInterface {
	public void stabilizer();
}
