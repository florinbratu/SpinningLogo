package com.killerappz.android.spinlogo;

import com.killerappz.android.spinlogo.context.SpinLogoContext;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

/**
 * Handle touch screen interaction
 * 
 * @author florin
 *
 */
public class TouchGesturesHandler extends SimpleOnGestureListener {
	
	private final SpinLogoContext contextInfo;
	private final SharedPreferences prefs;
	
	public TouchGesturesHandler(SpinLogoContext contextInfo,
			SharedPreferences wallpaperPrefs) {
		this.contextInfo = contextInfo;
		this.prefs = wallpaperPrefs;
	}

	/**
	 * On double tap, we scale up the model with 
	 *  a fixed percentile
	 */
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO handle for main window too! Not just for the preview pane!
		// TODO do it only if pointer is inside the "center" range
		// TODO don't let the scale factor grow beyond a certain limit
		Log.d(Constants.LOG_TAG, "Double tap event!");
		contextInfo.setScaleFactor( prefs,
				contextInfo.getScaleFactor() * ( 100 + Constants.DOUBLE_TAP_SCALE_PERCENTILE ) / 100 );
		return true;
	}
}
