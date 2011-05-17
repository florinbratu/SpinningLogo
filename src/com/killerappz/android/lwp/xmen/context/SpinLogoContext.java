package com.killerappz.android.lwp.xmen.context;

import com.killerappz.android.lwp.xmen.Constants;

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

	// the rotation speed
	private int rotationSpeed = Constants.DEFAULT_ROTATION_SPEED;
	// the license status
	private String licenseStatus = Constants.DEFAULT_LICENSE_STATUS;
    // the new logo texture, if it changed
    private String logoTextureName = Constants.DEFAULT_LOGO_TEXTURE_NAME;

    // get texture && reset the texture dirty flag
    public String getLogoTextureName() {
    	return this.logoTextureName;
    }
	
	public int getRotationSpeed() {
		return rotationSpeed;
	}
	
	public String getLicenseStatus() {
		return licenseStatus;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		if(Constants.ROTATION_SPEED_KEY.equals(key))
			rotationSpeed = prefs.getInt(Constants.ROTATION_SPEED_KEY, Constants.DEFAULT_ROTATION_SPEED);
		else if(Constants.LICENSE_STATUS_KEY.equals(key))
			licenseStatus = prefs.getString(Constants.LICENSE_STATUS_KEY, Constants.DEFAULT_LICENSE_STATUS);
		else if(Constants.LOGO_TEXTURE_KEY.equals(key)) {
			logoTextureName = prefs.getString(Constants.LOGO_TEXTURE_KEY, Constants.DEFAULT_LOGO_TEXTURE_NAME);
		}
	}

	public void loadPrefs(SharedPreferences prefs) {
		this.rotationSpeed = prefs.getInt(Constants.ROTATION_SPEED_KEY, Constants.DEFAULT_ROTATION_SPEED);
		this.licenseStatus = prefs.getString(Constants.LICENSE_STATUS_KEY, Constants.DEFAULT_LICENSE_STATUS);
		this.logoTextureName = prefs.getString(Constants.LOGO_TEXTURE_KEY, Constants.DEFAULT_LOGO_TEXTURE_NAME);
	}
	
}