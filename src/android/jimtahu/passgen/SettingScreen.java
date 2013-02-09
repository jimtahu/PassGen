package android.jimtahu.passgen;

import android.preference.PreferenceActivity;
import android.os.Bundle;

public class SettingScreen extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}//end onCreate

}//end SettingScreen
