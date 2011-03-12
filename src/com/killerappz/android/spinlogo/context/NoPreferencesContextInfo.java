package com.killerappz.android.spinlogo.context;

import android.content.SharedPreferences;

/**
 * Context information container with no preferences information!
 *
 */
public class NoPreferencesContextInfo extends ContextInfo {

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// disregard
	}

}
