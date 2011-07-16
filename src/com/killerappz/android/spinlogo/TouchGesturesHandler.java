package com.killerappz.android.spinlogo;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

import com.killerappz.android.spinlogo.ScaleGestureDetector.OnScaleGestureListener;
import com.killerappz.android.spinlogo.context.SpinLogoContext;

/**
 * Handle touch screen interaction
 * 
 * @author florin
 *
 */
public class TouchGesturesHandler extends SimpleOnGestureListener 
	implements OnScaleGestureListener{
	
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
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.d(Constants.LOG_TAG, "Fling detected; velocities: " + velocityX + ";" + velocityY);
		contextInfo.setTouchPoint(e1.getX(), e1.getY());
		// TODO rotation direction according to velocity vektor
		// TODO we can improve, calculate rot speed increment according to velocity values
		int rotationIncrement = Constants.ROTATION_SPEED_INCREMENT;
		if(contextInfo.touchInRange(GestureType.ROTATE))
			contextInfo.setRotationSpeed( prefs , 
					contextInfo.getRotationSpeed() + rotationIncrement );
		return true;
	}

	/**
	 * On scale, well... scale the model! :) */
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		contextInfo.setScaleFactor( prefs, (int)((float)contextInfo.getScaleFactor() * detector.getScaleFactor()));
		return true;
	}

	public boolean onScaleBegin(ScaleGestureDetector detector) {
		contextInfo.setTouchPoint( (detector.getTopFingerX() + detector.getBottomFingerX()) * 0.5f , 
				(detector.getTopFingerY() + detector.getBottomFingerY() ) * 0.5f );
		return contextInfo.touchInRange(GestureType.SCALE);
	}

	public void onScaleEnd(ScaleGestureDetector detector, boolean blah) {
	}
    
    // the list of gestures we handle
	public enum GestureType {
		DOUBLE_TAP,
		SCALE,
		ROTATE
	}
}