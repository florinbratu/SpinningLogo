package com.killerappz.android.lwp.mortal.kombat.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.killerappz.android.lwp.mortal.kombat.R;
import com.killerappz.android.lwp.mortal.kombat.Constants;

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