package com.killerappz.android.lwp.donation.mk.context;

import com.killerappz.android.lwp.donation.mk.Constants;
import com.killerappz.android.lwp.donation.mk.R;

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
	private boolean rotationEnabled = false;
	private volatile int rotationSpeed = Constants.DEFAULT_ROTATION_SPEED;
	// the scale factor
	private volatile int scaleFactor = Constants.DEFAULT_LOGO_SIZE;
	// the license status
	private String licenseStatus = Constants.DEFAULT_LICENSE_STATUS;
	// the new logo texture, if it changed
	private String logoTextureName = Constants.DEFAULT_LOGO_TEXTURE_NAME;
	
	public int getRevolutionSpeed() {
		return revolutionSpeed;
	}
	
	public boolean rotationEnabled() {
		return rotationEnabled;
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
	
	public String getLogoTextureName() {
		return this.logoTextureName;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		if(Constants.REVOLUTION_SPEED_KEY.equals(key))
			revolutionSpeed = prefs.getInt(Constants.REVOLUTION_SPEED_KEY, Constants.DEFAULT_REVOLUTION_SPEED);
		else if(Constants.ROTATION_KEY.equals(key))
			rotationEnabled = prefs.getBoolean(Constants.ROTATION_KEY, false);
		else if(Constants.ROTATION_SPEED_KEY.equals(key))
			rotationSpeed = prefs.getInt(Constants.ROTATION_SPEED_KEY, Constants.DEFAULT_ROTATION_SPEED); 
		else if(Constants.SCALING_FACTOR_KEY.equals(key))
			scaleFactor = prefs.getInt(Constants.SCALING_FACTOR_KEY, Constants.DEFAULT_LOGO_SIZE);
		else if(Constants.LOGO_TEXTURE_KEY.equals(key))
			logoTextureName = prefs.getString(Constants.LOGO_TEXTURE_KEY, Constants.DEFAULT_LOGO_TEXTURE_NAME);
		else if(Constants.LICENSE_STATUS_KEY.equals(key))
			licenseStatus = prefs.getString(Constants.LICENSE_STATUS_KEY, Constants.DEFAULT_LICENSE_STATUS);
	}

	public void loadPrefs(SharedPreferences prefs) {
		this.revolutionSpeed = prefs.getInt(Constants.REVOLUTION_SPEED_KEY, Constants.DEFAULT_REVOLUTION_SPEED);
		this.rotationEnabled = prefs.getBoolean(Constants.ROTATION_KEY, false);
 		this.rotationSpeed = prefs.getInt(Constants.ROTATION_SPEED_KEY, Constants.DEFAULT_ROTATION_SPEED);
		this.scaleFactor = prefs.getInt(Constants.SCALING_FACTOR_KEY, Constants.DEFAULT_LOGO_SIZE);
		this.logoTextureName = prefs.getString(Constants.LOGO_TEXTURE_KEY, Constants.DEFAULT_LOGO_TEXTURE_NAME);
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