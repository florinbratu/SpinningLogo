package com.killerappz.android.live.wallpaper.transformers;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

import com.killerappz.android.live.wallpaper.transformers.context.SpinLogoContext;

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
		contextInfo.setTouchPoint(e.getX(), e.getY());
		if(contextInfo.touchInRange(GestureType.DOUBLE_TAP))
			contextInfo.setScaleFactor( prefs,
					contextInfo.getScaleFactor() * ( 100 + Constants.DOUBLE_TAP_SCALE_PERCENTILE ) / 100 );
		return true;
	}
	
	// the list of gestures we handle
	public enum GestureType {
		DOUBLE_TAP
	}
}
