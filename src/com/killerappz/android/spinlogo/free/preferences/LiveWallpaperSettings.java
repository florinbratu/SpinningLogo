package com.killerappz.android.spinlogo.free.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.killerappz.android.spinlogo.free.R;
import com.killerappz.android.spinlogo.free.Constants;

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