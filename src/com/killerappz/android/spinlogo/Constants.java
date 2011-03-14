package com.killerappz.android.spinlogo;

public interface Constants {
	public static final String PREFS_NAME	= "spinlogo_settings";
	public static final float ROTATION_SPEED_UNIT = 0.1f;
	public static final int DEFAULT_ROTATION_SPEED = 10;
	public static final int ROTATION_MAX_SPEED = 100;
	// the file containing the model of the logo
	public static final String LOGO_MODEL_FILE = "raw/camaro_obj";
	
	// preferences screen keys
	// 1) rotation
	public static final String ROTATION_SPEED_KEY = "rotation";
	// Namespaces to read attributes
	public static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";

	// Attribute names
	public static final String ATTR_DEFAULT_VALUE = "defaultValue";
	public static final String ATTR_MAX_VALUE = "max";

}
