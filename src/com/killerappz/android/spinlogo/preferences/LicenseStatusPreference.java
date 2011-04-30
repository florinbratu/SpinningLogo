package com.killerappz.android.spinlogo.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.killerappz.android.spinlogo.Constants;
import com.killerappz.android.spinlogo.R;

/**
 * Entrance in the Preferences page for 
 * testing the license status 
 * 
 * @author florin
 *
 */
public class LicenseStatusPreference extends DialogPreference
	implements OnSharedPreferenceChangeListener {
	
	private final SharedPreferences sharedPrefs;
	// the label with the status info
	private TextView statusLabel; 
	
    public LicenseStatusPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogIcon(android.R.drawable.ic_lock_lock);
        setDialogLayoutResource(R.layout.license_check_dialog);
        // register myself as listener for shared prefs. For license status
        sharedPrefs = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);
    }
    
    @Override
    public CharSequence getSummary() {
    	String licenseStatus = getSharedPreferences().getString(
    			getKey(), Constants.DEFAULT_LICENSE_STATUS);
    	return licenseStatus;
    }

    @Override
    protected void onBindDialogView(View view) {
    	super.onBindDialogView(view);
    	
    	// add listeners for android market links
    	View.OnClickListener androidMarketLink = new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "market://details?id=" + getContext().getPackageName()));
                getContext().startActivity(marketIntent);
			}
		};
		((Button) view.findViewById(R.id.android_market_button)).setOnClickListener(androidMarketLink);
		((TextView)view.findViewById(R.id.android_market_label)).setOnClickListener(androidMarketLink);
		
		// add listeners for email link
		View.OnClickListener emailDevLink = new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setType("text/plain");
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.DEV_EMAIL_ADDRESS});
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, 
						getContext().getString(R.string.license_check_email_subject) + getContext().getPackageName());
                getContext().startActivity(
                		Intent.createChooser(emailIntent, 
                				getContext().getString(R.string.license_check_email_dev_intent)));
			}
		};
		((Button) view.findViewById(R.id.email_button)).setOnClickListener(emailDevLink);
		((TextView)view.findViewById(R.id.email_label)).setOnClickListener(emailDevLink);
		
		// set the License status
		statusLabel = (TextView)view.findViewById(R.id.status);
		String status = getSharedPreferences().getString(
    			getKey(), Constants.DEFAULT_LICENSE_STATUS);
		statusLabel.setText(status);
		statusLabel.setTextColor( getContext().getResources().
				getColor(colorForStatus(status)));
    }
    
    @Override
    protected void onDialogClosed(boolean positiveResult) {
    	super.onDialogClosed(positiveResult);

    	// Return if change was cancelled
    	if (!positiveResult) {
    		return;
    	}
    	
    	// TODO actually test! the license status

    	// Notify activity about changes (to update preference summary line)
    	notifyChanged();
    }

    private int colorForStatus(String status) {
    	if(status.equals(Constants.DEFAULT_LICENSE_STATUS))
    		return R.color.color_license_unknown;
    	else if(status.equals(Constants.OK_LICENSE_STATUS))
    		return R.color.color_license_ok;
    	else return R.color.color_license_error;
    }

    @Override
    public void onActivityDestroy() {
    	super.onActivityDestroy();
    	sharedPrefs.unregisterOnSharedPreferenceChangeListener(this);
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		if(Constants.LICENSE_STATUS_KEY.equals(key) && statusLabel != null) {
			String status = prefs.getString(Constants.LICENSE_STATUS_KEY, Constants.DEFAULT_LICENSE_STATUS);
			statusLabel.setText(status);
			statusLabel.setTextColor( getContext().getResources().
					getColor(colorForStatus(status)));
		}
    	// Notify activity about changes (to update preference summary line)
		notifyChanged();
	}
    
}
