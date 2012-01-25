package com.killerappz.android.lwp.poker.preferences;

import android.content.Context;
import android.util.AttributeSet;

import com.killerappz.android.lwp.poker.R;
import com.killerappz.android.lwp.poker.Constants;

/**
 * Control the rotation speed 
 * @author florin
 *
 */
public class RotationSpeedPreference extends SeekBarPreference {
	
	public RotationSpeedPreference(Context context, AttributeSet attrs) {
		super(context, attrs, 
				Integer.parseInt(context.getString(R.string.rotation_speed_default_value)), 
				Integer.parseInt(context.getString(R.string.rotation_speed_min_value)),
				Integer.parseInt(context.getString(R.string.rotation_speed_max_value)), 
				Constants.ROTATION_SPEED_UNIT);
	}
	
}