package android.jimtahu.passgen;

import java.util.Calendar;
import android.app.Activity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import java.net.Socket;
import java.util.Scanner;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.UnknownHostException;

public class PassGen extends Activity{

    /** 'Thread' for doing actual code exchange */
	private class CodeExchange extends AsyncTask {
        private PassGen screen;
        private String host;
        private String result;

        /**
         * @param o Main app
         * @param h Host name of server
         */
        public CodeExchange(PassGen o, String h){
            this.screen=o;
            this.host=h;
        }//end constructor

        /** equivlant to run */
        public String doInBackground(Object ...o){
            String msg = "fish";
            try{
   	            Calendar now = Calendar.getInstance();
                Socket datacom = new Socket(this.host, 7310);
                Scanner input = new Scanner(datacom.getInputStream());
                OutputStreamWriter host = new OutputStreamWriter(datacom.getOutputStream());
                Generator.seed(now.get(Calendar.HOUR)*now.get(Calendar.MINUTE));
                host.write(Generator.passcode());
                host.flush();
                msg = input.nextLine();
                datacom.close();
            }catch(UnknownHostException ex){
                msg = ex.toString();
            }catch(IOException ex){
                msg = ex.toString();
                ex.printStackTrace();
            }//end try catch
            this.result=msg;
            return msg;
        }//end run

        /**runs in the main thread after doInBackground compleates */
        protected void onPostExecute(String msg){
            this.screen.setOutput(this.result);
        }//end onPostExecute

	}//end CodeExchange

    /** sets the display output */
    public void setOutput(String text){
        EditText output = (EditText) this.findViewById(R.id.output);
        output.setText(text);
    };

    /** connects for a string */
    public void pulldata(View v){
        EditText host = (EditText) this.findViewById(R.id.host);
        new CodeExchange(this, host.getText().toString()).execute();
    };
    
    /** handles clicks */
    public void passgen(View v){
       	Calendar now = Calendar.getInstance();
    	Generator.seed(now.get(Calendar.HOUR)*now.get(Calendar.MINUTE));
    	setOutput(Generator.passcode());
    }; 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }//end onCreate
}//end PassGen
