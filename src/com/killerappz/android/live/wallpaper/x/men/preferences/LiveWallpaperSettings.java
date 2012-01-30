package com.killerappz.android.live.wallpaper.x.men.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.killerappz.android.live.wallpaper.x.men.Constants;
import com.killerappz.android.live.wallpaper.x.men.R;

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