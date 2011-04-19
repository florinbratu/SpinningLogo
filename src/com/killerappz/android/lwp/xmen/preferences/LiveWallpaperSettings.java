package com.killerappz.android.lwp.xmen.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.killerappz.android.lwp.xmen.Constants;
import com.killerappz.android.lwp.xmen.R;

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