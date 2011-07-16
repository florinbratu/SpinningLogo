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

	// the revolution speed
	private int revolutionSpeed = Constants.DEFAULT_REVOLUTION_SPEED;
	// the rotation speed
	private int rotationSpeed = Constants.DEFAULT_ROTATION_SPEED;
	// the scale factor
	private volatile int scaleFactor = Constants.DEFAULT_LOGO_SIZE;
	// the license status
	private String licenseStatus = Constants.DEFAULT_LICENSE_STATUS;
	
	public int getRevolutionSpeed() {
		return revolutionSpeed;
	}
	
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
		if(Constants.REVOLUTION_SPEED_KEY.equals(key))
			revolutionSpeed = prefs.getInt(Constants.REVOLUTION_SPEED_KEY, Constants.DEFAULT_REVOLUTION_SPEED);
		else if(Constants.ROTATION_SPEED_KEY.equals(key))
			rotationSpeed = prefs.getInt(Constants.ROTATION_SPEED_KEY, Constants.DEFAULT_ROTATION_SPEED); 
		else if(Constants.SCALING_FACTOR_KEY.equals(key))
			scaleFactor = prefs.getInt(Constants.SCALING_FACTOR_KEY, Constants.DEFAULT_LOGO_SIZE);
		else if(Constants.LICENSE_STATUS_KEY.equals(key))
			licenseStatus = prefs.getString(Constants.LICENSE_STATUS_KEY, Constants.DEFAULT_LICENSE_STATUS);
	}

	public void loadPrefs(SharedPreferences prefs) {
		this.revolutionSpeed = prefs.getInt(Constants.REVOLUTION_SPEED_KEY, Constants.DEFAULT_REVOLUTION_SPEED);
		this.rotationSpeed = prefs.getInt(Constants.ROTATION_SPEED_KEY, Constants.DEFAULT_ROTATION_SPEED);
		this.scaleFactor = prefs.getInt(Constants.SCALING_FACTOR_KEY, Constants.DEFAULT_LOGO_SIZE);
		this.licenseStatus = prefs.getString(Constants.LICENSE_STATUS_KEY, Constants.DEFAULT_LICENSE_STATUS);
	}

	public void setScaleFactor(SharedPreferences prefs, int scaleFactor) {
		this.scaleFactor = scaleFactor > Constants.MAX_LOGO_SIZE ? Constants.MAX_LOGO_SIZE : scaleFactor;
		this.scaleFactor = scaleFactor < Constants.MIN_LOGO_SIZE ? Constants.MIN_LOGO_SIZE : this.scaleFactor;
		Editor editor = prefs.edit();
		editor.putInt(Constants.SCALING_FACTOR_KEY, this.scaleFactor);
		editor.commit();
	}

	public void setRotationSpeed(SharedPreferences prefs, int rotSpeed) {
		this.rotationSpeed = rotSpeed > Constants.MAX_ROTATION_SPEED ? Constants.MAX_ROTATION_SPEED : rotSpeed;
		this.rotationSpeed = rotSpeed < Constants.MIN_ROTATION_SPEED ? Constants.MIN_ROTATION_SPEED : this.rotationSpeed;
		Editor editor = prefs.edit();
		editor.putInt(Constants.ROTATION_SPEED_KEY, this.rotationSpeed);
		editor.commit();
	}
	
}
