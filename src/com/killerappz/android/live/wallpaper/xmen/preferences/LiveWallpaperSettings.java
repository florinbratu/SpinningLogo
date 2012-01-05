package com.killerappz.android.live.wallpaper.xmen.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.killerappz.android.live.wallpaper.xmen.Constants;
import com.killerappz.android.live.wallpaper.xmen.R;

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