package com.killerappz.android.lwp.mk;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

import com.killerappz.android.lwp.mk.ScaleGestureDetector.OnScaleGestureListener;
import com.killerappz.android.lwp.mk.context.SpinLogoContext;

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
		contextInfo.setTouchPoint(e1.getX(), e1.getY());
		int ccw = contextInfo.rotationDirection( e1.getX(), e1.getY(), e2.getX(), e2.getY() );
		int rotationIncrement = contextInfo.getRotationSpeedIncrement(
				velocityX, velocityY);
		if(contextInfo.touchInRange(GestureType.ROTATE)
				&& minimumAngle(e1,e2))
			contextInfo.setRotationSpeed( prefs , 
					contextInfo.getRotationSpeed() + ccw * rotationIncrement );
		return true;
	}

	/**
	 * Test if the fling gesture is not performed on a horizontal direction
	 * Horizontal flings are reserved for the virtual screen switch op
	 * 
	 * It's difficult to test for perfect horizontal flings
	 * so instead we test if the movement angle is above a certain
	 * threshold - stored in {@link Constants.MIN_FLING_ANGLE}
	 * 
	 * @param e1, e2 the motion events for the start and endpoint of fling
	 * @return true if not horizontal fling
	 */
	private boolean minimumAngle(MotionEvent e1, MotionEvent e2) {
		double minAngle = Math.tan(Math.toRadians(Constants.MIN_FLING_ANGLE));
		double flingAngle = Math.abs(e1.getY() - e2.getY()) / Math.abs(e1.getX() - e2.getX());
		/*Log.d(Constants.LOG_TAG, "Minimim angle: " + Constants.MIN_FLING_ANGLE + 
				"; actual angle: " + Math.toDegrees(Math.atan(flingAngle)) );*/
		return minAngle < flingAngle;
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
