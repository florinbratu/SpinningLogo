package com.killerappz.android.lwp.dukenukem;

public interface Constants {
	public static final String PREFS_NAME	= "spinlogo_settings";
	public static final float ROTATION_SPEED_UNIT = 0.1f;
	public static final int DEFAULT_ROTATION_SPEED = 25;
	// the file containing the model of the logo
	public static final String LOGO_MODEL_FILE = "raw/duke_nukem_obj";
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
	// 1) rotation
	public static final String ROTATION_SPEED_KEY = "rotation";
	// 2) license status
	public static final String LICENSE_STATUS_KEY = "licenseStatus";
	// the default license status message
	public static final String DEFAULT_LICENSE_STATUS = "N/A";
	public static final String OK_LICENSE_STATUS = "OK";
	public static final String INVALID_LICENSE_STATUS = "NOT_LICENSED";
	// the notification ID
	public static final int NOTIF_TICKER_ID = 42;
	public static final String RECHECK_LICENSE_ACTION = "RECHECK_LICENSE";
	// 4) logo texture
	public static final String LOGO_TEXTURE_KEY = "logoTexture";
	public static final String DEFAULT_LOGO_TEXTURE_NAME = "texture_taijitu";

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
	
}
