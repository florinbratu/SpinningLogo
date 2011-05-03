package com.killerappz.android.spinlogo.licensing;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
public class LicenseValidationDialog extends Activity {
	
    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        
        setContentView(R.layout.invalid_license_dialog);
        Button yes = (Button)findViewById(R.id.check_button);
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Thou pressed OK",
                            Toast.LENGTH_SHORT).show();
                finish();
            }

        });
        Button no = (Button)findViewById(R.id.cancel_button);
        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	finish();
            }

        });
        
        TextView statusLabel = (TextView)findViewById(R.id.status);
        Context ctx = getApplicationContext();
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

    /**
     * Disable the notification in the Status Bar.
     */
    protected void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(Constants.NOTIF_TICKER_ID);
    }
    
}
