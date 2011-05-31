package com.killerappz.android.lwp.donation.superman.context;

import android.content.SharedPreferences;

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
		mCenter.set(width/2.0f, height/2.0f);
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
	
}
