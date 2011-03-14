package com.killerappz.android.spinlogo.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.killerappz.android.spinlogo.Constants;
import com.killerappz.android.spinlogo.R;

public class LiveWallpaperSettings extends PreferenceActivity
{
	@Override
	protected void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		getPreferenceManager().setSharedPreferencesName(Constants.PREFS_NAME);
		addPreferencesFromResource(R.xml.settings);
	}
	
}