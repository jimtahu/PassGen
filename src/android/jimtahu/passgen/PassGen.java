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
	private long Secret; 

    /** 'Thread' for doing actual code exchange */
	private class CodeExchange extends AsyncTask<Integer, String, String> {
        private PassGen screen;
        private String host;
        private String result;
        private long secret;

        /**
         * @param o Main app
         * @param h Host name of server
         */
        public CodeExchange(PassGen o, String h, long s){
            this.screen=o;
            this.host=h;
            this.secret=s;
        }//end constructor

        /** equivlant to run */
        @Override
        public String doInBackground(Integer ... opt){
            String msg = "fish";
            try{
   	            Calendar now = Calendar.getInstance();
                Socket datacom = new Socket(this.host, 7310);
                Scanner input = new Scanner(datacom.getInputStream());
                OutputStreamWriter host = new OutputStreamWriter(datacom.getOutputStream());
                Generator.seed(now.get(Calendar.HOUR)*now.get(Calendar.MINUTE)*secret);
                if(opt[0]==0)
                	host.write(Generator.passcode()+"\n");
                else
                	host.write("fish\n");
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

    /** attempts to unlock */
    public void unlock(View v){
        EditText host = (EditText) this.findViewById(R.id.host);
        new CodeExchange(this,
        		host.getText().toString(),this.Secret)
        .execute(0x00);
    };
    
    /** attempts to unlock */
    public void lock(View v){
        EditText host = (EditText) this.findViewById(R.id.host);
        new CodeExchange(this, host.getText().toString(),0).execute(0xff);
    };
    
    /** handles clicks */
    public void passgen(View v){
       	Calendar now = Calendar.getInstance();
    	Generator.seed(now.get(Calendar.HOUR)*now.get(Calendar.MINUTE)*now.get(Calendar.SECOND));
    	setOutput(Generator.passcode());
    }; 

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(Menu.NONE,0,0,"Settings");
    	return super.onCreateOptionsMenu(menu);
    };
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()) {
		case 0:
			startActivity(new Intent(this,SettingScreen.class));
			return true;
		}
    	return false;
    };

    @Override
    protected void onResume() {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	EditText host = (EditText) this.findViewById(R.id.host);
        host.setText(prefs.getString("host_name","localhost"));
        this.Secret = Long.parseLong(prefs.getString("secret","1"));
        super.onResume();
    }//end onResume
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        onResume();
    }//end onCreate
}//end PassGen
