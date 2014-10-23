package com.sjsu.java.october;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;


public class DigitallySign {
	
	  /**
	   * String to hold name of the encryption algorithm.
	   */
	  public static final String ALGORITHM = "DSA";

	  private static final int KEYSIZE = 512;
	  
	  /**
	   * String to hold the name of the private key file.
	   */
	  public static final String PRIVATE_KEY_FILE = "C:/keys/private.key";

	  /**
	   * String to hold name of the public key file.
	   */
	  public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";

	  public static final String MESSAGE = "C:/keys/message.txt";
	  public static final String SIGNATURE = "C:/keys/signature";
	  
	  public static boolean areKeysPresent() {

		    File privateKey = new File(PRIVATE_KEY_FILE);
		    File publicKey = new File(PUBLIC_KEY_FILE);

		    if (privateKey.exists() && publicKey.exists()) {
		      return true;
		    }
		    return false;
		  }
	
	  /**
	   * Generate key which contains a pair of private and public key using 1024
	   * bytes. Store the set of keys in Prvate.key and Public.key files.
	   * 
	   * @throws NoSuchAlgorithmException
	   * @throws IOException
	   * @throws FileNotFoundException
	   */
	  public static void generateKey() {
	    try {
	      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
	      SecureRandom random = new SecureRandom();
	      
	      keyGen.initialize(KEYSIZE, random);
	      
	      final KeyPair key = keyGen.generateKeyPair();

	      File privateKeyFile = new File(PRIVATE_KEY_FILE);
	      File publicKeyFile = new File(PUBLIC_KEY_FILE);

	      // Create files to store public and private key
	      if (privateKeyFile.getParentFile() != null) {
	        privateKeyFile.getParentFile().mkdirs();
	      }
	      privateKeyFile.createNewFile();

	      if (publicKeyFile.getParentFile() != null) {
	        publicKeyFile.getParentFile().mkdirs();
	      }
	      publicKeyFile.createNewFile();

	      // Saving the Public key in a file
	      ObjectOutputStream publicKeyOS = new ObjectOutputStream(
	          new FileOutputStream(publicKeyFile));
	      publicKeyOS.writeObject(key.getPublic());
	      publicKeyOS.close();

	      // Saving the Private key in a file
	      ObjectOutputStream privateKeyOS = new ObjectOutputStream(
	          new FileOutputStream(privateKeyFile));
	      privateKeyOS.writeObject(key.getPrivate());
	      privateKeyOS.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	  
	  /*
	   * Sign a message
	   */
	  public static byte[] signMsg(byte[] message, int length) throws ClassNotFoundException, IOException
	  {
		  ObjectInputStream keyIn = null;
		  PrivateKey privkey = null;
	      
		try {
			keyIn = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
			privkey = (PrivateKey) keyIn.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
          try {
			keyIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

          Signature signalg = null;
		try {
			signalg = Signature.getInstance(ALGORITHM);
			try {
				signalg.initSign(privkey);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        try {
			signalg.update(message);
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        byte[] signature = null;
		try {
			signature = signalg.sign();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DataOutputStream out = new DataOutputStream(new FileOutputStream(SIGNATURE));
        int signlength = signature.length;
        out.writeInt(signlength);
        out.write(signature, 0, signlength);
        out.write(message, 0, length);
        out.close();
        
        return signature;
	  }  
	  
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
	  
	  public static void main(String args[])
	  {
		  /* Generate the private and public keys*/
		  generateKey();
		  
		  byte[] signature = null;
		try {
	        
			File infile = new File(MESSAGE);
			InputStream in = null;
	        try {
				 in = new FileInputStream(infile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        int length = (int) infile.length();
	        byte[] message = new byte[length];
	        try {
				in.read(message, 0, length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			signature = signMsg(message, length);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  verifySignature();		  
	  }
	  
}
