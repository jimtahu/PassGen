package android.jimtahu.passgen;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PassGen extends Activity{

    /** handles clicks */
    public void passgen(View v){
	TextView output = (TextView) this.findViewById(R.id.output);
	output.setText("Fishy!");
    }; 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }//end onCreate
}//end PassGen
