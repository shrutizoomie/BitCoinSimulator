package com.sjsu.thesis.backend;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;

/**
 *  
 * @author shruti_sharma1
 * This class is used to generate a pair of private and public keys to sign a block
 */
public class BitFedGenKeyPair
{
   /* Use the Elliptic curve algorithm to generate keys*/
   public static final String ALGORITHM = "EC";
   public static final int KEYSIZE = 256;
   public static final String PRIVATE_KEY_FILE = "privatekey.txt";
   public static final String PUBLIC_KEY_FILE = "publickey.txt";

   /**
    * Generate key which contains a pair of private and public key using 1024
    * bytes. Store the set of keys in Prvate.key and Public.key files.
    * 
    * @throws NoSuchAlgorithmException
    * @throws IOException
    * @throws FileNotFoundException
    */
   public static void generateKey(String PRIVATE_KEY_FILE, String PUBLIC_KEY_FILE) 
   {
     try 
     {
       final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
       SecureRandom random = new SecureRandom();

       keyGen.initialize(KEYSIZE, random);

       final KeyPair key = keyGen.generateKeyPair();

       /* The below 2 lines will get the actual private and public keys*/
       PrivateKey priv = key.getPrivate();
       PublicKey pub = key.getPublic();

       /* Get the encoding and Save the private key in a file */
       byte[] privArray = priv.getEncoded();
       Base64 encoder1 = new Base64();
       String encodedPriv = encoder1.encodeToString(privArray);
       System.out.println("My Private Key:" + PRIVATE_KEY_FILE + " " + encodedPriv);
       BufferedWriter writer1 = new BufferedWriter(new FileWriter(PRIVATE_KEY_FILE));
       writer1.write(encodedPriv);
       writer1.close();

       /* Get the encoding and Save the public key in a file */
       byte[] pubArray = pub.getEncoded();
       Base64 encoder2 = new Base64();
       String s2encodedPub = encoder2.encodeToString(pubArray);
       System.out.println("My Public key" + PUBLIC_KEY_FILE + " " + s2encodedPub);
       BufferedWriter writer2 = new BufferedWriter(new FileWriter(PUBLIC_KEY_FILE));
       writer2.write(s2encodedPub);
       writer2.close();
       
       // Generate the bitcoin address
//       BitFedGetBitcoinAddress bitAddr = new BitFedGetBitcoinAddress();
//       bitAddr.getBitcoinAddress();
     }
     catch(Exception e)
     {
        System.err.println("Caught exception " + e.toString());
     }
   }
}
