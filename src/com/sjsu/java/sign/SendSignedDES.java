package com.sjsu.java.sign;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;

import javax.crypto.Cipher;


public class SendSignedDES {	
	private String groupAddress;
	private InetAddress address;
	private MulticastSocket socket;
	private static int SOCKET = 4446;
	

	  /**
	   * String to hold name of the encryption algorithm.
	   */
	  public static final String ALGORITHM = "RSA";

	  /**
	   * String to hold the name of the private key file.
	   */
	  public static final String PRIVATE_KEY_FILE = "C:/keys/private.key";

	  /**
	   * String to hold name of the public key file.
	   */
	  public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";


	public SendSignedDES() throws IOException {
		this.groupAddress = "230.0.0.1";
		socket = new MulticastSocket(SOCKET);
        address = InetAddress.getByName(groupAddress);
        socket.joinGroup(address);
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
	      keyGen.initialize(2048);
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
	  
	  /**
	   * The method checks if the pair of public and private key has been generated.
	   * 
	   * @return flag indicating if the pair of keys were generated.
	   */
	  public static boolean areKeysPresent() {

	    File privateKey = new File(PRIVATE_KEY_FILE);
	    File publicKey = new File(PUBLIC_KEY_FILE);

	    if (privateKey.exists() && publicKey.exists()) {
	      return true;
	    }
	    return false;
	  }

	  /**
	   * Sign the plain text using private key.
	   * 
	   * @param text
	   *          : original plain text
	   * @param key
	   *          :The private key
	   * @return Encrypted text
	   * @throws java.lang.Exception
	   */
	  public static byte[] encrypt(String text, PrivateKey key) {
	    byte[] cipherText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance(ALGORITHM);
	      // encrypt the plain text using the private key
	      cipher.init(Cipher.ENCRYPT_MODE, key);
	      cipherText = cipher.doFinal(text.getBytes());
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return cipherText;
	  }

	  public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		SendSignedDES sender = new SendSignedDES();
		ObjectInputStream inputStream = null;
		try {

		      // Check if the pair of keys are present else generate those.
		      if (!areKeysPresent()) {
		        // Method generates a pair of keys using the RSA algorithm and stores it
		        // in their respective files
		        generateKey();
		      }

		      final String originalText = args[0];

		      // Encrypt the string using the public key
		      inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
		      final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
		      final byte[] cipherText = encrypt(originalText, privateKey);

		      // Printing the Original, Encrypted Text
		      System.out.println("Original: " + originalText);
		      System.out.println("Encrypted: " +cipherText.toString());

		      sender.send(cipherText);
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		finally {
			if ( inputStream != null) {
				inputStream.close();
			}
		}
		 
		
		
//		sender.send(args[0]);
	}

	private void send(byte[] id) {
		//BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
         //String messageText = reader.readLine();
		broadcast(id);
	}

	private void broadcast(byte[] buf) {
		try
	      {
	         DatagramPacket packet = new DatagramPacket(buf, buf.length, address, SOCKET);
	         socket.send(packet);
	         System.out.println("Successfully Sent !");
	      }
	      catch (Exception e)
	      {
	         e.printStackTrace();
	      }
	}

}
