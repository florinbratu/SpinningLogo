package com.killerappz.android.spinlogo.context;

import android.content.SharedPreferences;

/**
 * Generic Context Information relevant 
 * 	to the Spin Logo Live Wallpaper
 * 
 * @author florin
 *
 */
public class SpinLogoContext extends ContextInfo {

	// the file containing the model of the logo
	private String logoModelFile = "raw/camaro_obj";
	
	/* (non-Javadoc)
	 * @see com.killerappz.android.spinlogo.context.ContextInfo#storePreferences(android.content.SharedPreferences, java.lang.String)
	 */
	@Override
	public void storePreferences(SharedPreferences prefs, String key) {
		if(key.equals("TODO_key"))
			logoModelFile = prefs.getString(key, "");
	}

	public String getLogoModelFile() {
		return logoModelFile;
	}
	
}
