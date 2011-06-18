package com.killerappz.android.spinlogo.context;

import com.killerappz.android.spinlogo.Constants;

import android.content.SharedPreferences;

/**
 * Generic Context Information relevant 
 * 	to the Spin Logo Live Wallpaper
 * 
 * @author florin
 *
 */
public class SpinLogoContext extends ContextInfo implements
	SharedPreferences.OnSharedPreferenceChangeListener{

	// the revolution speed
	private int revolutionSpeed = Constants.DEFAULT_REVOLUTION_SPEED;
	// the rotation speed
	private int rotationSpeed = Constants.DEFAULT_ROTATION_SPEED;
	// the license status
	private String licenseStatus = Constants.DEFAULT_LICENSE_STATUS;
	
	public int getRevolutionSpeed() {
		return revolutionSpeed;
	}
	
	public int getRotationSpeed() {
		return rotationSpeed;
	}
	
	public String getLicenseStatus() {
		return licenseStatus;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		if(Constants.REVOLUTION_SPEED_KEY.equals(key))
			revolutionSpeed = prefs.getInt(Constants.REVOLUTION_SPEED_KEY, Constants.DEFAULT_REVOLUTION_SPEED);
		else if(Constants.ROTATION_SPEED_KEY.equals(key))
			rotationSpeed = prefs.getInt(Constants.ROTATION_SPEED_KEY, Constants.DEFAULT_ROTATION_SPEED); 
		else if(Constants.LICENSE_STATUS_KEY.equals(key))
			licenseStatus = prefs.getString(Constants.LICENSE_STATUS_KEY, Constants.DEFAULT_LICENSE_STATUS);
	}

	public void loadPrefs(SharedPreferences prefs) {
		this.revolutionSpeed = prefs.getInt(Constants.REVOLUTION_SPEED_KEY, Constants.DEFAULT_REVOLUTION_SPEED);
		this.rotationSpeed = prefs.getInt(Constants.ROTATION_SPEED_KEY, Constants.DEFAULT_ROTATION_SPEED);
		this.licenseStatus = prefs.getString(Constants.LICENSE_STATUS_KEY, Constants.DEFAULT_LICENSE_STATUS);
	}
	
}
