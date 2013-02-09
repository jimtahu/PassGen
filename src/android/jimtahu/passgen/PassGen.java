package android.jimtahu.passgen;

import java.util.Calendar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.net.Socket;
import java.util.Scanner;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.UnknownHostException;

public class PassGen extends Activity{

    /** 'Thread' for doing actual code exchange */
	private class CodeExchange extends AsyncTask<String, String, String> {
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
        @Override
        public String doInBackground(String ...o){
            String msg = "fish";
            try{
   	            Calendar now = Calendar.getInstance();
                Socket datacom = new Socket(this.host, 7310);
                Scanner input = new Scanner(datacom.getInputStream());
                OutputStreamWriter host = new OutputStreamWriter(datacom.getOutputStream());
                Generator.seed(now.get(Calendar.HOUR)*now.get(Calendar.MINUTE));
                host.write(Generator.passcode()+"\n");
                host.flush();
                while(input.hasNext()){
                	msg = input.next();
                }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(Menu.NONE,0,0,"Settings");
    	return super.onCreateOptionsMenu(menu);
    };
    
    public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()) {
		case 0:
			startActivity(new Intent(this,SettingScreen.class));
			//Toast msg = Toast.makeText(this,"GoldFish",Toast.LENGTH_LONG);
			//msg.show();
			return true;
		}
    	return false;
    };

    @Override
    protected void onResume() {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	EditText host = (EditText) this.findViewById(R.id.host);
        host.setText(prefs.getString("host_name","localhost"));
        super.onResume();
    }//end onResume
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        EditText host = (EditText) this.findViewById(R.id.host);
        host.setText(prefs.getString("host_name","localhost"));
    }//end onCreate
}//end PassGen
