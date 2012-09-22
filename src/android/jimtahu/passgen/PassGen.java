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

	private class CodeExchange extends Thread {
        private PassGen screen;
        private String host;

        public CodeExchange(PassGen o, String h){
            this.screen=o;
            this.host=h;
        }//end constructor

        public void run(){
            String msg = "fish";
            try{
   	            Calendar now = Calendar.getInstance();
                Socket datacom = new Socket(this.host, 7310);
                Scanner input = new Scanner(datacom.getInputStream());
                OutputStreamWriter host = new OutputStreamWriter(datacom.getOutputStream());
                Generator.seed(now.get(Calendar.HOUR));
                host.write(Generator.passcode());
                host.flush();
                msg = input.nextLine();
                datacom.close();
            }catch(Exception ex){
                msg = ex.toString();
                ex.printStackTrace();
            }//end try catch
            this.screen.setOutput(msg);
            return;
        }//end run

	}//end CodeExchange

    /** sets the display output */
    public void setOutput(String text){
        EditText output = (EditText) this.findViewById(R.id.output);
        output.setText(text);
    };

    /** connects for a string */
    public void pulldata(View v){
        EditText output = (EditText) this.findViewById(R.id.output);
        CodeExchange runner = new CodeExchange(this, output.getText().toString());
        //runner.start();
        runner.run();
    };
    
    /** handles clicks */
    public void passgen(View v){
   	Calendar now = Calendar.getInstance();
	Generator.seed(now.get(Calendar.HOUR));
	setOutput(Generator.passcode());
    }; 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }//end onCreate
}//end PassGen
