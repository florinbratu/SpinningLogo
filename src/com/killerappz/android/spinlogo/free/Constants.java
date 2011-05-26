package com.killerappz.android.spinlogo.free;

public interface Constants {
	public static final String PREFS_NAME	= "spinlogo_free_settings";
	public static final float ROTATION_SPEED_UNIT = 0.1f;
	public static final int DEFAULT_ROTATION_SPEED = 25;
	// the file containing the model of the logo
	public static final String LOGO_MODEL_FILE = "raw/ying_yang_obj";
	
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
	
	public static final String LOG_TAG = "KILLER_LWP";
	
}
