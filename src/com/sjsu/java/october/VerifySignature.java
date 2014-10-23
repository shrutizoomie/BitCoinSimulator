package com.sjsu.java.october;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class VerifySignature {

	/**
	 * String to hold name of the public key file.
	 */
	public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";

	public static final String SIGNATURE = "C:/keys/signature";
	  
	public static void verifySignature()
	  {
        ObjectInputStream keyIn = null;
        PublicKey pubkey = null;
        Signature verifyalg = null;
        
		try {
			keyIn = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
			pubkey = (PublicKey) keyIn.readObject();
			keyIn.close();
			
			verifyalg = Signature.getInstance("DSA");
			verifyalg.initVerify(pubkey);
			
		  File infile = new File(SIGNATURE);
          DataInputStream in = new DataInputStream(new FileInputStream(infile));
          int signlength = in.readInt();
          byte[] signature = new byte[signlength];
          in.read(signature, 0, signlength);

          int length = (int) infile.length() - signlength - 4;
          byte[] message = new byte[length];
          in.read(message, 0, length);
          in.close();
          
          verifyalg.update(message);
          if (!verifyalg.verify(signature)) System.out.print("not ");
          System.out.println("verified");
		} catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	  }
}
