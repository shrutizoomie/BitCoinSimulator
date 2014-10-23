package com.sjsu.java.sample;

public class MyRunnableEx implements Runnable {
	  private final long countUntil;

	  MyRunnableEx(long countUntil) {
	    this.countUntil = countUntil;
	  }

	  @Override
	  public void run() {
	    long sum = 0;
	    for (long i = 1; i < countUntil; i++) {
	      sum += i;
	    }
	    System.out.println(sum);
	  }
} 
