package com.killerappz.android.spinlogo;

public interface Constants {
	public static final String PREFS_NAME	= "spinlogo_settings";
	// the file containing the model of the logo
	public static final String LOGO_MODEL_FILE = "raw/ying_yang_obj";
	// the folder under res/ where we find textures. Android internal cuisine
	public static final String TEXTURES_LOCATION = "drawable/";
	
	// viewing params
	// field of view
	public static final float FIELD_OF_VIEW_Y = 60f;
	// the near and far clipping planes
	public static final float Z_NEAR_PLANE = 1f;
	public static final float Z_FAR_PLANE = 100f;
	// oblique projection
	public static final float MAX_OBLIQUE_ANGLE = 30;
	
	// preferences screen keys
	// revolution
	public static final String REVOLUTION_SPEED_KEY = "revolution";
	public static final float REVOLUTION_SPEED_UNIT = 0.1f;
	public static final int DEFAULT_REVOLUTION_SPEED = 25;
	// rotation
	public static final String ROTATION_KEY = "rotationEnabled";
	public static final String ROTATION_SPEED_KEY = "rotation";
	public static final float ROTATION_SPEED_UNIT = 0.25f;
	public static final int DEFAULT_ROTATION_SPEED = 0;
	public static final int MAX_ROTATION_SPEED = 90; // ugly but...works
	public static final int MIN_ROTATION_SPEED = -90;
	// logo texture
	public static final String LOGO_TEXTURE_KEY = "logoTexture";
	public static final String DEFAULT_LOGO_TEXTURE_NAME = "texture_taijitu";
	// skybox texture
	public static final String SKYBOX_TEXTURE_KEY = "skyboxTexture";
	public static final String DEFAULT_SKYBOX_TEXTURE_NAME = "skybox_awisdom";

	// scaling
	public static final String SCALING_FACTOR_KEY = "scale";
	public static final float LOGO_SIZE_UNIT = (float)2 / (float)90;
	public static final int DEFAULT_LOGO_SIZE = 45;
	public static final int MAX_LOGO_SIZE = 90; // ugly but...works
	public static final int MIN_LOGO_SIZE = 9;
	// license status
	public static final String LICENSE_STATUS_KEY = "licenseStatus";
	// the default license status message
	public static final String DEFAULT_LICENSE_STATUS = "N/A";
	public static final String OK_LICENSE_STATUS = "OK";
	public static final String INVALID_LICENSE_STATUS = "NOT_LICENSED";
	// the notification ID
	public static final int NOTIF_TICKER_ID = 42;
	public static final String RECHECK_LICENSE_ACTION = "RECHECK_LICENSE";

	// skybox params
	/*
	 * parametrul asta imi da dimensiunea fiecarei laturi a skybox-ului
	 * in fct de cat de mare e textura te poti juca cu el.
	 * Pt textura exemplu pe care o am acum, 4 e perfect ca si size
	 */
	public static final int SKYBOX_SIZE = 4;
	
	/* cica da calitatea skybox-ului. De fapt da numarul de poligoane 
	folosite pt a construi fiecare din laturile skybox-ului */
	public static final int SKYBOX_QUALITY_FACTOR = 3;
	
	/** Application licensing-related info */
	public static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBC" +
			"gKCAQEAzJc4dgyEyGSo822dyj0//KtVtd4qZk7bpmPLav2ok88rpx1g474gOP7qX4JWrUIDUqwlRHBXP" +
			"+Hohj4GitxK9KhqZGv2KoSfjKZidXKJixbqgRyyNcWod+FaYV2e9CNO+yWvXj8DdyOWRCnffm0AbLy" +
			"2msUbiYNeUmVdbH73+7wrOf0vJkQ12VjW15coNSECCMzWVEys4gJrk4xC84Z/zvvTUzLrrlxipGVgR" +
			"2s0mcN8q94/DjX0BWVVBTNJ4xty8J+K4GkMgPSg/286y9dWRHIhzGUksoI2nejsSNWmxjiJtQeg" +
			"FefCHb6xond7+cHNqMEpmAjxpSSqyVm2XFFGkwIDAQAB";
	
	public static final byte[] SALT = new byte[] {
		101, -53, -73, 36, -118, 
		31, -33, -19, 38, -95,
		12, 49, -107, 115, -92,
		-1, -107, -116, 96, -94
	};
	public static final int UID_SIZE = 16; // the size of ANDROID_ID
	
	public static final String LOG_TAG = "KILLER_LWP";
	
	public static final String[] licenseErrorCodes = new String[] {
		"INVALID_PACKAGE_NAME",
        "NON_MATCHING_UID",
        "NOT_MARKET_MANAGED",
        "CHECK_IN_PROGRESS",
        "INVALID_PUBLIC_KEY",
        "MISSING_PERMISSION",
	};
	
	public static final String DEV_EMAIL_ADDRESS = "killerappzz@gmail.com";
	
	/* Touch interaction prefs */
	// the percentile with which the model increases when user double taps the screen 
	public static final int DOUBLE_TAP_SCALE_PERCENTILE = 20;
	public static final int DOUBLE_TAP_RANGE_PERCENTILE = 20;
	// scaling percentile
	public static final int SCALING_RANGE_PERCENTILE = 15;
	// rotation 
	public static final int ROTATION_RANGE_PERCENTILE = 25;
	// minimum angle for which a fling will be taken into account
	// this is so that we can at least slide between virtual screens...
	public static final float MIN_FLING_ANGLE = 15;
	public static final float ROTATION_SPEED_MIN_INCREMENT = 5;
	public static final float ROTATION_SPEED_MAX_INCREMENT = 45;
	public static final float SCREEN_UNIT_SCREEN_SIZE_FACTOR = 5;
	// the velocity corresponding to the minimum speed increment, in "screen units"
	public static final float ROTATION_VELOCITY_MIN_INCREMENT = 0.5f;
	// the velocity corresponding to the maximum speed increment, in "screen units"
	public static final float ROTATION_VELOCITY_MAX_INCREMENT = 9.0f;
	
}
