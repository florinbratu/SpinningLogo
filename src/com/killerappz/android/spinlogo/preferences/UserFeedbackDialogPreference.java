package com.killerappz.android.spinlogo.preferences;

import org.acra.ACRA;
import org.acra.ErrorReporter;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.killerappz.android.spinlogo.Constants;
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
@ReportsCrashes(formKey = "dC1xZ1lxZVV3SkJ4aE1nOFpnMTRXR0E6MQ",
		formUri = "https://spreadsheets.google.com/formResponse?formkey=dC1xZ1lxZVV3SkJ4aE1nOFpnMTRXR0E6MQ&amp;ifq",
		mode = ReportingInteractionMode.NOTIFICATION,
		sharedPreferencesName = Constants.PREFS_NAME,
		logcatArguments = { "-t", "300", "-v", "long", "ActivityManager:I", "ACRA:I", "KILLER_LWP:D", "Min3D:D", "GLThread:D", "*:S" },
		logcatTimeout = 3,
        resNotifTickerText = R.string.crash_notif_ticker_text,
        resNotifTitle = R.string.crash_notif_title,
        resNotifText = R.string.crash_notif_text,
        resNotifIcon = android.R.drawable.stat_notify_error, // optional. default is a warning sign
        resDialogText = R.string.crash_dialog_text,
        resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
        resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. when defined, adds a user text field input with this text resource as a label
        resDialogOkToast = R.string.crash_dialog_ok_toast // optional. displays a Toast message when the user accepts to send a report.
	)
public class UserFeedbackDialogPreference extends DialogPreference {
	
	private volatile boolean includeLogcat;
	
	// ref to the edit text
	private EditText userCommentView;
	
	public UserFeedbackDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.includeLogcat = false;
		setDialogIcon(android.R.drawable.ic_dialog_info);
		setDialogLayoutResource(R.layout.user_feedback);
		// fwkin ACRA
		ACRA.init(this, context);
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
		
		if(positiveResult) {
			ErrorReporter err = ErrorReporter.getInstance();
			String userComment = userCommentView.getText().toString();
			if( null != userComment && !userComment.equals("")) {
				err.setUserComment(userComment);
			}
			err.includeLogcat(includeLogcat);
			err.handleSilentException(new Exception("User feedback"));
			Toast.makeText(getContext(), R.string.user_feedback_report_sent , 
					Toast.LENGTH_SHORT).show();
		}
	}
	
}
