package com.killerappz.android.lwp.peace.preferences;

import com.killerappz.android.lwp.peace.R;

import android.content.Context;
import android.util.AttributeSet;

/**
 * The preference for the spinning logo textures
 * 
 * @author florin
 *
 */
public class SpinningLogoTexturePreference extends ImageListPreference {

	public SpinningLogoTexturePreference(Context context, AttributeSet attrs) {
		super(context, attrs, R.string.logo_texture_defaultValue, R.styleable.SpinningLogoTexturePreference);
	}
}
