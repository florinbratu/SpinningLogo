package com.killerappz.android.spinlogo.context;

import android.content.SharedPreferences;

/**
 * Context information container with no preferences information!
 *
 */
public class NoPreferencesContextInfo extends ContextInfo {

	/* (non-Javadoc)
	 * @see com.killerappz.lwp.context.ContextInfo#getPreferences(android.content.SharedPreferences, java.lang.String)
	 */
	@Override
	public void storePreferences(SharedPreferences prefs, String key) {
		// disregard
	}

}
