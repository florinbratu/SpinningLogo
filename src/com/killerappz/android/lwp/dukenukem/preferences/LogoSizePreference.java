package com.killerappz.android.lwp.dukenukem.preferences;

import com.killerappz.android.lwp.dukenukem.Constants;
import com.killerappz.android.lwp.dukenukem.R;

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
				Integer.parseInt(context.getString(R.string.logo_size_max_value)), 
				Constants.LOGO_SIZE_UNIT);
	}

}
