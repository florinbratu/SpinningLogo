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
	// it's static, as this info is fixed per-package
	private static final String logoModelFile = "raw/camaro_obj";
	// the rotation speed
	private int rotationSpeed = 10;
	
	/* (non-Javadoc)
	 * @see com.killerappz.android.spinlogo.context.ContextInfo#storePreferences(android.content.SharedPreferences, java.lang.String)
	 */
	@Override
	public void storePreferences(SharedPreferences prefs, String key) {
		// TODO set rotation speed
	}

	public String getLogoModelFile() {
		return logoModelFile;
	}
	
	public int getRotationSpeed() {
		return rotationSpeed;
	}
	
}
