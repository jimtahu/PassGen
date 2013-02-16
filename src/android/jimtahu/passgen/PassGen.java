package android.jimtahu.passgen;

import java.util.Calendar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class PassGen extends Activity{
	private long Secret; 

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
