package com.killerappz.android.spinlogo.preferences;

import android.content.Context;
import android.util.AttributeSet;

import com.killerappz.android.spinlogo.R;

/**
 * The preference page for the skybox texture selection
 * 
 * @author florin
 *
 */
public class SkyboxTexturePreference extends ImageListPreference {
	public SkyboxTexturePreference(Context context, AttributeSet attrs) {
		super(context, attrs, R.string.skybox_texture_defaultValue, R.styleable.SkyboxTexturePreference);
	}
}