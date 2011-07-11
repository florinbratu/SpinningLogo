package com.killerappz.android.lwp.dukenukem.preferences;

import com.killerappz.android.lwp.dukenukem.Constants;
import com.killerappz.android.lwp.dukenukem.R;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Control revolution speed
 * @author florin
 *
 */
public class RevolutionSpeedPreference extends SeekBarPreference {

	public RevolutionSpeedPreference(Context context, AttributeSet attrs) {
		super(context, attrs, 
				Integer.parseInt(context.getString(R.string.revolution_speed_default_value)), 
				Integer.parseInt(context.getString(R.string.revolution_speed_max_value)), 
				Constants.REVOLUTION_SPEED_UNIT);
	}

}
