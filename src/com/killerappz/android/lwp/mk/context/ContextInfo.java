package com.killerappz.android.lwp.mk.context;

import com.killerappz.android.lwp.mk.Constants;
import com.killerappz.android.lwp.mk.TouchGesturesHandler.GestureType;

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
			case SCALE:
				/* same idea for scaling range */
				range = (mCenter.x + mCenter.y) * Constants.SCALING_RANGE_PERCENTILE / 100.0f;
				break;
			case ROTATE:
				/* same idea for scaling range */
				range = (mCenter.x + mCenter.y) * Constants.ROTATION_RANGE_PERCENTILE / 100.0f;
				break;
			default:
				return false;
		}
		// calculate the distance to the center
		double distance = Math.sqrt( (mTouchPoint.x - mCenter.x) * (mTouchPoint.x - mCenter.x) 
				+ (mTouchPoint.y - mCenter.y) * (mTouchPoint.y - mCenter.y) );
		return (float)distance < range;
	}
	
	/**
	 * Determine the rotation direction from point (x1,y1) 
	 * 	to point (x2,y2) using the center point as reference.
	 * @returns true if counter clockwise rotation
	 */
	public int rotationDirection(float x1, float y1, float x2, float y2) {
		return (x1 - mCenter.x) * (y2 - mCenter.y) > (x2 - mCenter.x) * (y1 - mCenter.y) ? -1 : 1;
	}
	
	/**
	 * Calculate the speed increment from the fling movement
	 * It is calculated via linear interpolation, 
	 * we use precomputed values for minimum and maximum velocities
	 * and for minumum/maximum speed increments and interpolate through them
	 * 
	 * @param vx, vy the velocity values on x and y axes
	 * @return the speed increment 
	 */
	public int getRotationSpeedIncrement(float vx, float vy) {
		float velocity = (float)Math.sqrt( vx * vx + vy * vy );
		float screenUnit = (mCenter.x + mCenter.y) / Constants.SCREEN_UNIT_SCREEN_SIZE_FACTOR;
		float minVelocity = (float)Constants.ROTATION_SPEED_MIN_INCREMENT * screenUnit;
		float maxVelocity = (float)Constants.ROTATION_VELOCITY_MAX_INCREMENT * screenUnit;
		float retFloat = Constants.ROTATION_SPEED_MIN_INCREMENT * ((velocity - minVelocity) / (maxVelocity - minVelocity))
			+ Constants.ROTATION_SPEED_MAX_INCREMENT * ((maxVelocity - velocity) / (maxVelocity - minVelocity)) ;
		return (int)retFloat;
	}

}
