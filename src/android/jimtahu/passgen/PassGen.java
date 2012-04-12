package android.jimtahu.passgen;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PassGen extends Activity{

    /** handles clicks */
    public void passgen(View v){
	EditText output = (EditText) this.findViewById(R.id.output);
	String txt = "";
	for(int i=0; i<16; i++)
	    txt = txt + (int)(Math.random()*10);
	output.setText(txt);
    }; 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }//end onCreate
}//end PassGen
