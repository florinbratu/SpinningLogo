package com.killerappz.android.spinlogo.preferences;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.killerappz.android.spinlogo.R;

/**
 * This is not really a preference. It will simply be
 * another option in the LWP Preferences page.
 * When the user clicks it, the ACRA feedback form
 * will be displayed, so that the user can
 * report something...
 * 
 * @author florin
 *
 */
public class UserFeedbackDialogPreference extends DialogPreference {
	
	public UserFeedbackDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogIcon(android.R.drawable.ic_dialog_info);
		setDialogLayoutResource(R.layout.user_feedback);
	}
	
	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		
		final CheckBox checkbox = (CheckBox) view.findViewById(R.id.logcat);
		checkbox.setOnClickListener(new View.OnClickListener() {
			@Override
		    public void onClick(View v) {
		        // Perform action on clicks, depending on whether it's now checked
		        if (((CheckBox) v).isChecked()) {
		            Toast.makeText(getContext(), "Selected", Toast.LENGTH_SHORT).show();
		        } else {
		            Toast.makeText(getContext(), "Not selected", Toast.LENGTH_SHORT).show();
		        }
		    }
		});
		
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if(!positiveResult) {
			Toast.makeText(getContext(), "Thou pressed Cancel" , Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getContext(), "Thou pressed OK" , Toast.LENGTH_SHORT).show();
		}
	}
}
