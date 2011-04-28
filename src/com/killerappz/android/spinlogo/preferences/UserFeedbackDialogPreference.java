package com.killerappz.android.spinlogo.preferences;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
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
	
	private CheckBox logcatCheckBox; 

	public UserFeedbackDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogIcon(android.R.drawable.ic_dialog_info);
	}
	
	@Override
	protected View onCreateDialogView() {
		/*
        mReportFileName = getIntent().getStringExtra(ErrorReporter.EXTRA_REPORT_FILE_NAME);
        Log.d(LOG_TAG, "Opening CrashReportDialog for " + mReportFileName);
        if (mReportFileName == null) {
            finish();
        }
        */
        
        // TODO maybe one day... do this via an inflatable layout...
        final Context ctx = getContext();

        LinearLayout root = new LinearLayout(ctx);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(10, 10, 10, 10);
        root.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        ScrollView scroll = new ScrollView(ctx);
        root.addView(scroll, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f));

        TextView text = new TextView(ctx);
        text.setText(R.string.user_feedback_text);
        scroll.addView(text, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

        TextView label = new TextView(ctx);
        label.setText(R.string.user_feedback_details);
        label.setPadding(label.getPaddingLeft(), 10, label.getPaddingRight(), label.getPaddingBottom());
        root.addView(label, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        EditText userComment = new EditText(ctx);
        userComment.setLines(2);
        root.addView(userComment,
        		new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
        logcatCheckBox = new CheckBox(ctx);
        logcatCheckBox.setText(R.string.user_feedback_logcat_checkbox);
        root.addView(logcatCheckBox,
        		new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
        return root.getRootView();
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if(!positiveResult) {
			Toast.makeText(getContext(), "Thou pressed Cancel" , Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getContext(), "Thou pressed OK. Checkbox is " + (logcatCheckBox.isChecked() ? "checked" : "not checked") , Toast.LENGTH_SHORT).show();
		}
	}
}
