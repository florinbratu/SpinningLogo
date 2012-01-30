package com.killerappz.android.live.wallpaper.superman.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.killerappz.android.live.wallpaper.superman.Constants;
import com.killerappz.android.live.wallpaper.superman.R;

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