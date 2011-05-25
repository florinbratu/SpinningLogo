package com.killerappz.android.spinlogo.preferences;

import com.killerappz.android.spinlogo.R;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * This will show an option in the Preferences page
 * which will advertise the full option
 * 
 * @author florin
 *
 */
public class FullVersionPreference extends DialogPreference {
	
	public FullVersionPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setNegativeButtonText(context.getString(
				R.string.full_version_negative_btn));
		setPositiveButtonText(context.getString(
				R.string.full_version_positive_btn));
		setDialogLayoutResource(R.layout.full_version_dialog);
	}
	
}
