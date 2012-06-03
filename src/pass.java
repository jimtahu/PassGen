import android.jimtahu.passgen.Generator;

public class pass {
	public static void main(String args[]){
		Generator.seed(0x10);
		System.out.println(Generator.passcode());
	}//end main
}//end pass
