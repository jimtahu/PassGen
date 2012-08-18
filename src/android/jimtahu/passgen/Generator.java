package android.jimtahu.passgen;

import java.util.Random;

public class Generator {
	private static Random pool=null;

	public static void seed(long s){
		pool = new Random(s);
	}//end seed

	public static String passcode(){
		String txt = "";
		for(int i=0; i<16; i++)
			txt = txt + (pool.nextInt(10));
		return txt;
	}//end passcode
}//end Generator
