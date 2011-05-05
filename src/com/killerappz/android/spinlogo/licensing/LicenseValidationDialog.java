package com.killerappz.android.spinlogo.licensing;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.killerappz.android.spinlogo.Constants;
import com.killerappz.android.spinlogo.R;
import com.killerappz.android.spinlogo.preferences.LicenseStatusPreference;

/**
 * Dialog to be displayed on wallpaper startup
 * 	if license check fails
 * 
 * @author florin
 *
 */
public class LicenseValidationDialog extends Activity 
	implements OnSharedPreferenceChangeListener{
	
	// the label with the status info
	private TextView statusLabel; 
	// the UI needs to be updated via this handler
	private final Handler mHandler = new Handler();
	private SharedPreferences sharedPrefs;
	
	private Button checkLicenseButton;
	
    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final Context ctx = getApplicationContext();
        sharedPrefs = ctx.getSharedPreferences(Constants.PREFS_NAME, 0);
    	sharedPrefs.registerOnSharedPreferenceChangeListener(this);
        
        requestWindowFeature(Window.FEATURE_LEFT_ICON); 
        setContentView(R.layout.invalid_license_dialog);
        
        // add listeners for android market links
    	View.OnClickListener androidMarketLink = new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "market://details?id=" + ctx.getPackageName()));
				marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(marketIntent);
			}
		};
		((Button) findViewById(R.id.android_market_button)).setOnClickListener(androidMarketLink);
		((TextView) findViewById(R.id.android_market_label)).setOnClickListener(androidMarketLink);
		
        checkLicenseButton = (Button)findViewById(R.id.check_button);
        checkLicenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(Constants.OK_LICENSE_STATUS.equals(
            			ctx.getSharedPreferences(Constants.PREFS_NAME, 0)
            			.getString(Constants.LICENSE_STATUS_KEY, Constants.DEFAULT_LICENSE_STATUS))) {
            		finish();
            	} else {
            		checkLicenseButton.setEnabled(false);
            		statusLabel.setText(R.string.license_check_in_progress);
            		statusLabel.setTextColor(Color.WHITE);
            		ctx.sendBroadcast(new Intent(Constants.RECHECK_LICENSE_ACTION));

            		// just to make sure, in case something bad happens
            		// register a TimerTask to re-enable the interface
            		/* TODO new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
	
						}
					}, 0, Constants.RECHECK_LICENSE_TIMEOUT);*/
            	}
            }

        });
        Button no = (Button)findViewById(R.id.cancel_button);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	finish();
            }
        });
        
        statusLabel = (TextView)findViewById(R.id.status);
        String status = ctx.getSharedPreferences(Constants.PREFS_NAME, 0)
        	.getString(Constants.LICENSE_STATUS_KEY, Constants.DEFAULT_LICENSE_STATUS);
        statusLabel.setText(status);
        statusLabel.setTextColor( ctx.getResources().
        		getColor(LicenseStatusPreference.colorForStatus(status)));
        
        setTitle(R.string.license_check_dialog_title);

        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
        		android.R.drawable.ic_dialog_alert);

        cancelNotification();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	sharedPrefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Disable the notification in the Status Bar.
     */
    protected void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(Constants.NOTIF_TICKER_ID);
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
						statusLabel.setTextColor( getApplicationContext().getResources().
								getColor(LicenseStatusPreference.colorForStatus(status)));
					}
					checkLicenseButton.setEnabled(true);
					checkLicenseButton.setText(getApplicationContext().getString(
							LicenseStatusPreference.positiveButtonForStatus(status)));
					
					// cancel notification that will be put in status bar by LicenseCheckerCallbak
					cancelNotification();
				}
			}
		});
	}
    
}
