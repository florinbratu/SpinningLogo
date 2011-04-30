package com.killerappz.android.spinlogo.preferences;

import com.killerappz.android.spinlogo.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Entrance in the Preferences page for 
 * testing the license status 
 * 
 * @author florin
 *
 */
public class LicenseStatusPreference extends DialogPreference{

    public LicenseStatusPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogIcon(android.R.drawable.ic_lock_lock);
        setDialogLayoutResource(R.layout.license_check_dialog);
    }
    
    @Override
    protected void onBindDialogView(View view) {
    	super.onBindDialogView(view);
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
    }
    
}
