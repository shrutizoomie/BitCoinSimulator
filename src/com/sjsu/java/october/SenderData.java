package com.sjsu.java.october;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class SenderData {
	
	  public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";
	  public static final String MESSAGE = "C:/keys/message.txt";
	  public static final String SIGNATURE = "C:/keys/signature";
	  public static final String MERGED = "C:/keys/appended.txt";
	  
	  /* Append this data to send : Message + Signature + Public key*/
	  public void readData()
	  {
		  try
		    {
		   File file1=new File(MESSAGE);
		   File file2=new File(SIGNATURE);
		   File file3 = new File(PUBLIC_KEY_FILE);
		   
		   File merged=read(file1,file2,file3);
		   
		   Scanner sc=new Scanner(merged);
		   while(sc.hasNext())
		       System.out.print(sc.next());

		    }
		    catch(Exception e)
		    {
		        System.out.printf("Error  :%s",e);
		    }
	  }
	  
	  public void fileToBytes()
	  {
		  File file = new File(MERGED);

	         byte[] b = new byte[(int) file.length()];
	         try {
	               FileInputStream fileInputStream = new FileInputStream(file);
	               fileInputStream.read(b);
	               for (int i = 0; i < b.length; i++) {
	                           System.out.print((char)b[i]);
	                }
	          } catch (FileNotFoundException e) {
	                      System.out.println("File Not Found.");
	                      e.printStackTrace();
	          }
	          catch (IOException e1) {
	                   System.out.println("Error Reading The File.");
	                    e1.printStackTrace();
	          }
	  }
	  
	  public static File read(File f,File f1, File f2) throws FileNotFoundException
	  {
	      File file3=new File("C:/keys/appended.txt");
	      PrintWriter output=new PrintWriter(file3);
	      ArrayList arr=new ArrayList();
	      Scanner sc=new Scanner(f);
	      Scanner sc1=new Scanner(f1);
	      Scanner sc2 = new Scanner(f2);
	      while(sc.hasNext())
	      {
	          arr.add(sc.next());

	      }
	       while(sc1.hasNext())
	      {
	          arr.add(sc1.next());

	      }
	       
	       while (sc2.hasNext())
	       {
	    	   arr.add(sc2.next());
	       }
	         output.print(arr);
	     output.close();

	      return file3;
	  }
   
}
