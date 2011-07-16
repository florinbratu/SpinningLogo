package com.killerappz.android.spinlogo.context;

import com.killerappz.android.spinlogo.Constants;
import com.killerappz.android.spinlogo.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Generic Context Information relevant 
 * 	to the Spin Logo Live Wallpaper
 * 
 * @author florin
 *
 */
public class SpinLogoContext extends ContextInfo implements
	SharedPreferences.OnSharedPreferenceChangeListener{
	
	// the rotation speed
	private int rotationSpeed = Constants.DEFAULT_ROTATION_SPEED;
	// the scale factor
	private volatile int scaleFactor = Constants.DEFAULT_LOGO_SIZE;
	// the license status
	private String licenseStatus = Constants.DEFAULT_LICENSE_STATUS;
	
	public int getRotationSpeed() {
		return rotationSpeed;
	}
	
	public int getScaleFactor() {
		return scaleFactor;
	}
	
	public String getLicenseStatus() {
		return licenseStatus;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		if(Constants.ROTATION_SPEED_KEY.equals(key))
			rotationSpeed = prefs.getInt(Constants.ROTATION_SPEED_KEY, Constants.DEFAULT_ROTATION_SPEED);
		else if(Constants.SCALING_FACTOR_KEY.equals(key))
			scaleFactor = prefs.getInt(Constants.SCALING_FACTOR_KEY, Constants.DEFAULT_LOGO_SIZE);
		else if(Constants.LICENSE_STATUS_KEY.equals(key))
			licenseStatus = prefs.getString(Constants.LICENSE_STATUS_KEY, Constants.DEFAULT_LICENSE_STATUS);
	}

	public void loadPrefs(SharedPreferences prefs) {
		this.rotationSpeed = prefs.getInt(Constants.ROTATION_SPEED_KEY, Constants.DEFAULT_ROTATION_SPEED);
		this.scaleFactor = prefs.getInt(Constants.SCALING_FACTOR_KEY, Constants.DEFAULT_LOGO_SIZE);
		this.licenseStatus = prefs.getString(Constants.LICENSE_STATUS_KEY, Constants.DEFAULT_LICENSE_STATUS);
	}

	public void setScaleFactor(SharedPreferences prefs, int scaleFactor) {
		this.scaleFactor = scaleFactor;
		Editor editor = prefs.edit();
		editor.putInt(Constants.SCALING_FACTOR_KEY, scaleFactor);
		editor.commit();
	}
	
}
