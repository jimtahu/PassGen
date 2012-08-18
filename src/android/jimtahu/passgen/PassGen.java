package android.jimtahu.passgen;

import java.util.Calendar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PassGen extends Activity{

    /** handles clicks */
    public void passgen(View v){
   	Calendar now = Calendar.getInstance();
	EditText output = (EditText) this.findViewById(R.id.output);
	Generator.seed(now.get(Calendar.HOUR));
	output.setText(Generator.passcode());
    }; 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }//end onCreate
}//end PassGen
