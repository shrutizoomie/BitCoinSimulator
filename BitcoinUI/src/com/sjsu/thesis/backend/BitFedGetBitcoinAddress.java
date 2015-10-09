/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sjsu.thesis.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.apache.commons.codec.digest.DigestUtils.sha256;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

/**
 *
 * @author Shruti
 */
public class BitFedGetBitcoinAddress {
    
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    String minerPubKey = null;
    public static final String BITCOIN_ADDRESS_FILE = "bitcoinAddress.txt";
    
    public String getPublicKey(String FILENAME)
    {
        try 
        {
            BufferedReader br = null;
            try 
            {
                br = new BufferedReader(new FileReader(FILENAME));
                minerPubKey = br.readLine();
                System.out.println("MinerPubKey :" + minerPubKey);
            }
            catch (FileNotFoundException ex) 
            {
                Logger.getLogger(BitFedGetBitcoinAddress.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex)
            {
                Logger.getLogger(BitFedGetBitcoinAddress.class.getName()).log(Level.SEVERE, null, ex);
            }
            br.close();
        }
        catch (IOException ex) {
                Logger.getLogger(BitFedGetBitcoinAddress.class.getName()).log(Level.SEVERE, null, ex);
            }
        return minerPubKey;
    }
    
    public String getBitcoinAddress()
    {    
        String ripeMDResult = null;
        try {
            BufferedReader br = null;
            
            try {
                br = new BufferedReader(new FileReader("publickey.txt"));
                minerPubKey = br.readLine();
                String bitcoinAddr = getAddress(minerPubKey);
                //System.out.println("Address is" + bitcoinAddr);
                ripeMDResult = toRIPEMD160(bitcoinAddr);
                //System.out.println("Ripe MD result is" + ripeMDResult);
                
                // write the RIPE MD result to a file
                // ref link http://stackoverflow.com/questions/12303567/cannot-output-correct-hash-in-java-what-is-wrong
                
                BufferedWriter writer1 = new BufferedWriter(new FileWriter(BITCOIN_ADDRESS_FILE));
                writer1.write(ripeMDResult);
                writer1.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(BitFedGetBitcoinAddress.class.getName()).log(Level.SEVERE, null, ex);
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(BitFedGetBitcoinAddress.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ripeMDResult;
    }
    
    String getAddress(String key)
    {
        MessageDigest md = null;
        StringBuilder sb = new StringBuilder();
        try 
        {
            md = MessageDigest.getInstance("SHA-256");
            md.update(key.getBytes());
            byte byteData[] = md.digest();
 
            //convert the byte to hex format method 1
            
            for (int i = 0; i < byteData.length; i++) 
            {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
     
            System.out.println("Hex format : " + sb.toString());
            
        } 
        catch (NoSuchAlgorithmException ex) 
        {
            Logger.getLogger(BitFedGetBitcoinAddress.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }
    
    public static String getHexString(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
}
    
    public String toRIPEMD160(String in) 
    {
        byte[] addr = in.getBytes();
        byte[] out = new byte[20];
        RIPEMD160Digest digest = new RIPEMD160Digest();
        byte[] rawSha256 = sha256(addr);
        String encodedSha256 = getHexString(rawSha256);
        
        byte[] strBytes = null;
        try {
            strBytes = encodedSha256.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BitFedGetBitcoinAddress.class.getName()).log(Level.SEVERE, null, ex);
        }
        digest.update(strBytes, 0, strBytes.length);
        digest.doFinal(out, 0);
        return getHexString(out);
    }
}
