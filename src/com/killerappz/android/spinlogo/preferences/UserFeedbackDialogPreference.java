package com.killerappz.android.spinlogo.preferences;

import org.acra.ACRA;
import org.acra.ErrorReporter;
import org.acra.LogCatCollector;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
	
	private boolean includeLogcat;
	
	// ref to the edit text
	private EditText userCommentView;
	
	public UserFeedbackDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.includeLogcat = false;
		setDialogIcon(android.R.drawable.ic_dialog_info);
		setDialogLayoutResource(R.layout.user_feedback);
	}
	
	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		
		final CheckBox checkbox = (CheckBox) view.findViewById(R.id.logcat);
		// set value from last run(if any)
		checkbox.setChecked(includeLogcat);
		checkbox.setOnClickListener(new View.OnClickListener() {
			@Override
		    public void onClick(View v) {
				includeLogcat = ((CheckBox) v).isChecked();
		    }
		});
		
		// save the ref to the user comments page
		userCommentView = (EditText) view.findViewById(R.id.usercomment);
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if(!positiveResult) {
			Toast.makeText(getContext(), "Thou pressed Cancel" , Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getContext(), "Thou pressed OK. Logs need " + (includeLogcat ? "" : " not ") + " to be included" + 
					"User comment:" + userCommentView.getText().toString() , Toast.LENGTH_SHORT).show();
		}
	}
	
}
