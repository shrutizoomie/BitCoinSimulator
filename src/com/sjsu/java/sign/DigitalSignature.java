package com.sjsu.java.sign;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;

public class DigitalSignature {
	public static void main(String[] args) throws Exception {
	    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
	    kpg.initialize(1024);
	    KeyPair keyPair = kpg.genKeyPair();
	    
	    System.out.println(keyPair.toString());

	    byte[] data = "test".getBytes("UTF8");

	    Signature sig = Signature.getInstance("MD5WithRSA");
	    sig.initSign(keyPair.getPrivate());
	    sig.update(data);
	    byte[] signatureBytes = sig.sign();
	    
	    
	    
	    //System.out.println("Signature:" + new BASE64Encoder().encode(signatureBytes));

	    sig.initVerify(keyPair.getPublic());
	    sig.update(data);

	    System.out.println(sig.verify(signatureBytes));
	  }
}
