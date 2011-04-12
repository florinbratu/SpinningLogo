package com.killerappz.android.lwp.mortalkombat.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.killerappz.android.lwp.mortalkombat.Constants;
import com.killerappz.android.lwp.mortalkombat.R;

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