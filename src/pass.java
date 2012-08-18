import android.jimtahu.passgen.Generator;

import java.util.Calendar;

public class pass {
	public static void main(String args[]){
        Calendar now = Calendar.getInstance();
		Generator.seed(now.get(Calendar.HOUR));
		System.out.println(Generator.passcode());
	}//end main
}//end pass
