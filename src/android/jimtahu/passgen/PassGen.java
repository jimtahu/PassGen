package android.jimtahu.passgen;

import java.util.Calendar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.net.Socket;
import java.util.Scanner;
import java.io.OutputStreamWriter;

public class PassGen extends Activity{

    /** connects for a string */
    public void pulldata(View v){
        String msg = "fish";
        EditText output = (EditText) this.findViewById(R.id.output);
        try{
            Socket datacom = new Socket(output.getText().toString(), 7310);
            Scanner input = new Scanner(datacom.getInputStream());
            OutputStreamWriter host = new OutputStreamWriter(datacom.getOutputStream());
            msg = input.nextLine();
            host.write("fish have landed");
            datacom.close();
        }catch(Exception ex){
            msg = ex.toString();
            ex.printStackTrace();
        }//end try catch
        output.setText(msg);
    };
    
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
