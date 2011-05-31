package com.killerappz.android.lwp.donation.batman.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Handler;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.killerappz.android.lwp.donation.batman.Constants;
import com.killerappz.android.lwp.donation.batman.R;

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
	// the UI needs to be updated via this handler
	private final Handler mHandler;
	
    public LicenseStatusPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogIcon(android.R.drawable.ic_lock_lock);
        setNegativeButtonText(context.getString(
        		R.string.license_check_negative_button));
        setDialogLayoutResource(R.layout.license_check_dialog);
        // register myself as listener for shared prefs. For license status
        sharedPrefs = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);
        
        this.mHandler = new Handler();
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
				marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
				emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
		setPositiveButtonText(getContext().getString(
				positiveButtonForStatus(status)));
    }
    
	@Override
    protected void onDialogClosed(boolean positiveResult) {
    	super.onDialogClosed(positiveResult);

    	if (positiveResult) {
    		// if invalid license, trigger license check
    		if(!Constants.OK_LICENSE_STATUS.equals(
    				getSharedPreferences().getString(
    					getKey(), Constants.DEFAULT_LICENSE_STATUS)))
    		getContext().sendBroadcast(
    				new Intent(Constants.RECHECK_LICENSE_ACTION));
    	}
    	
    }

    public static int colorForStatus(String status) {
    	if(status.equals(Constants.DEFAULT_LICENSE_STATUS))
    		return R.color.color_license_unknown;
    	else if(status.equals(Constants.OK_LICENSE_STATUS))
    		return R.color.color_license_ok;
    	else if(status.equals(Constants.INVALID_LICENSE_STATUS))
    		return R.color.color_license_invalid;
    	else return R.color.color_license_error;
    }
    
    public static int positiveButtonForStatus(String status) {
    	if(status.equals(Constants.OK_LICENSE_STATUS))
    		return R.string.license_check_positive_button_ok;
    	else
    		return R.string.license_check_positive_button_check_license;
	}

    @Override
    public void onActivityDestroy() {
    	super.onActivityDestroy();
    	sharedPrefs.unregisterOnSharedPreferenceChangeListener(this);
    }

	@Override
	public void onSharedPreferenceChanged(final SharedPreferences prefs, 
			final String key) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if(Constants.LICENSE_STATUS_KEY.equals(key)) {
					String status = prefs.getString(Constants.LICENSE_STATUS_KEY, Constants.DEFAULT_LICENSE_STATUS);
					if(statusLabel != null) {
						statusLabel.setText(status);
						statusLabel.setTextColor( getContext().getResources().
								getColor(colorForStatus(status)));
					}
					// update positive button text
					setPositiveButtonText(getContext().getString(
							positiveButtonForStatus(status)));
					// Notify activity about changes (to update preference summary line)
					notifyChanged();
				}
			}
		});
	}
    
}
