package com.killerappz.android.spinlogo.licensing;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.killerappz.android.spinlogo.Constants;
import com.killerappz.android.spinlogo.R;

/**
 * Dialog to be displayed on wallpaper startup
 * 	if license check fails
 * 
 * TODO
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

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(10, 10, 10, 10);
        root.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        ScrollView scroll = new ScrollView(this);
        root.addView(scroll, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f));

        TextView text = new TextView(this);

        text.setText("blah");
        scroll
                .addView(text, LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT);

        LinearLayout buttons = new LinearLayout(this);
        buttons.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        buttons.setPadding(buttons.getPaddingLeft(), 10, buttons
                .getPaddingRight(), buttons.getPaddingBottom());

        Button yes = new Button(this);
        yes.setText(android.R.string.yes);
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Thou pressed OK",
                            Toast.LENGTH_SHORT).show();
                finish();
            }

        });
        buttons.addView(yes, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
        Button no = new Button(this);
        no.setText(android.R.string.no);
        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	finish();
            }

        });
        buttons.addView(no, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
        root.addView(buttons, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        setContentView(root);

        /*int resTitle = crashResources
                .getInt(ACRA.RES_DIALOG_TITLE);
        if (resTitle != 0) {
            setTitle(resTitle);
        }*/

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
    
	private void showDialog() {
		Context context = getApplicationContext();
		final Dialog dialog = new Dialog(context);

		dialog.setContentView(R.layout.invalid_license_dialog);
		dialog.setTitle("Custom Dialog");
		
		Button okButton = (Button) dialog.findViewById(R.id.check_button);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), "Thou pressed OK",
                        Toast.LENGTH_SHORT).show();
			}
		});
		Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		dialog.show();
	}

}
