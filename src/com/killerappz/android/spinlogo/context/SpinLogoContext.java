package com.killerappz.android.spinlogo.context;

import android.content.SharedPreferences;

import com.killerappz.android.spinlogo.Constants;

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
	
	// the new logo texture, if it changed
	private String logoTextureName = Constants.DEFAULT_LOGO_TEXTURE_NAME;
	
	public int getRotationSpeed() {
		return rotationSpeed;
	}
	
	// get texture && reset the texture dirty flag
	public String getLogoTextureName() {
		return this.logoTextureName;
	}
	
	// will be called initially, @engine creation
	public void loadPrefs(SharedPreferences prefs) {
		this.rotationSpeed = prefs.getInt(Constants.ROTATION_SPEED_KEY, Constants.DEFAULT_ROTATION_SPEED);
		this.logoTextureName = prefs.getString(Constants.LOGO_TEXTURE_KEY, Constants.DEFAULT_LOGO_TEXTURE_NAME);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		if(Constants.ROTATION_SPEED_KEY.equals(key))
			rotationSpeed = prefs.getInt(Constants.ROTATION_SPEED_KEY, Constants.DEFAULT_ROTATION_SPEED);
		else if(Constants.LOGO_TEXTURE_KEY.equals(key)) {
			logoTextureName = prefs.getString(Constants.LOGO_TEXTURE_KEY, Constants.DEFAULT_LOGO_TEXTURE_NAME);
		}
	}
}
