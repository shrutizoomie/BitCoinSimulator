/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sjsu.thesis.backend;


import com.sjsu.thesis.framework.LoggerUtil;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Performs the proof-of-work algorithm on a block string. Creates a random
 * string, then concatenates random string to block string and an increasing
 * long number. If a hash of this concatenation contains a number of zeroes
 * equal to NUMBER_OF_ZEROES, solution is found.
 * 
 * @author Shruti
 * @version 1.0.0
 */

public class BitFedProofOfWork extends Thread
{

	public static String findProof(String blockString) throws UnsupportedEncodingException, Exception
	{
		String randomString = UUID.randomUUID().toString();

		long i = 0;
		boolean isFound = false;
		String zeroes = String.format(String.format("%%%ds", BitFedVerify.NUMBER_OF_ZEROES), " ").replace(" ", "0");

		String concatString = blockString + randomString;

		while (!isFound)
		{
			String hashedString = BitFedVerify.hash(concatString + i).substring(0, BitFedVerify.NUMBER_OF_ZEROES);
			System.out.println("Checking hash: " + hashedString);
			if (hashedString.equals(zeroes))
			{
				isFound = true;
			}
			else
			{
				i++;
			}
		}
		LoggerUtil.getLogger().fine("Solution found: " + randomString + i);
                System.out.println("Solution found" + randomString + i);
		return randomString + i;
	}
}