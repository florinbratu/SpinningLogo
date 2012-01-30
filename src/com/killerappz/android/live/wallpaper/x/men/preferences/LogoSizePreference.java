package com.killerappz.android.live.wallpaper.x.men.preferences;

import com.killerappz.android.live.wallpaper.x.men.Constants;
import com.killerappz.android.live.wallpaper.x.men.R;

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
