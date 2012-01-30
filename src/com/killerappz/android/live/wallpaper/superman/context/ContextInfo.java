package com.killerappz.android.live.wallpaper.superman.context;

import com.killerappz.android.live.wallpaper.superman.Constants;
import com.killerappz.android.live.wallpaper.superman.TouchGesturesHandler.GestureType;

import android.content.SharedPreferences;
import android.util.Log;

/**
 * Container for all the context-related information 
 * which impacts the live wallpaper rendering process:
 *  - offset-related info for multiple screens
 *  - center of the image
 *  - touch-related info
 * 
 * The informations might not be available 
 * 	=> it is up to the caller to check
 */
public abstract class ContextInfo implements
	SharedPreferences.OnSharedPreferenceChangeListener{
    // the center of the image
    private final Point mCenter;
    // the offset information
    private final OffsetInfo mOffset;
    private final TouchPoint mTouchPoint;
    
    public ContextInfo() {
		mCenter = new Point();
		mOffset = new OffsetInfo();
		mTouchPoint = new TouchPoint();
	}

	public void setCenter(float width, float height) {
		mCenter.set(width, height);
	}

	public void setOffset(float xOffset, float yOffset, float xStep,
			float yStep, int xPixels, int yPixels) {
		mOffset.set(xOffset, yOffset, xStep, yStep, xPixels, yPixels);
	}

	public void setTouchPoint(float x, float y) {
		mTouchPoint.set(x,y);
	}
	
	public Point getCenter() {
		return mCenter;
	}
	
	public Point getTouchPoint(){
		return mTouchPoint.get();
	}
	
	public OffsetInfo getOffset() {
		return mOffset;
	}
	
	/**
	 * test if the touch point is "in range"
	 * relative to the point center,
	 * so we can consider the touch event as a
	 * valid interaction with the app.
	 * The "in range" criteria is dependent on the event type 
	 */
	public boolean touchInRange(GestureType gesture) {
		float range;
		switch(gesture) {
			case DOUBLE_TAP:
				/* the double tap range is calculated as follows:
				 * - average the width and height
				 * - range will be RANGE_PERCENTILE * average
				 *  */
				range = (mCenter.x + mCenter.y) * Constants.DOUBLE_TAP_RANGE_PERCENTILE / 100.0f;
				break;
			default:
				return false;
		}
		// calculate the distance to the center
		double distance = Math.sqrt( (mTouchPoint.x - mCenter.x) * (mTouchPoint.x - mCenter.x) 
				+ (mTouchPoint.y - mCenter.y) * (mTouchPoint.y - mCenter.y) );
		return (float)distance < range;
	}

}
