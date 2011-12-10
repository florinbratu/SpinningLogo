package com.killerappz.android.lwp.mk.preferences;

import com.killerappz.android.lwp.mk.R;
import com.killerappz.android.lwp.mk.Constants;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Control the size of the spinning logo
 * 
 * @author florin
 *
 */
public class LogoSizePreference extends SeekBarPreference {

	public LogoSizePreference(Context context, AttributeSet attrs) {
		super(context, attrs, 
				Integer.parseInt(context.getString(R.string.logo_size_default_value)), 
				0, Integer.parseInt(context.getString(R.string.logo_size_max_value)),
				Constants.LOGO_SIZE_UNIT);
	}

}
