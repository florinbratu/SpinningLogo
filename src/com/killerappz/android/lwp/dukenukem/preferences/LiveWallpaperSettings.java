package com.killerappz.android.lwp.dukenukem.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.killerappz.android.lwp.dukenukem.Constants;
import com.killerappz.android.lwp.dukenukem.R;

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