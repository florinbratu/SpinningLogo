package com.killerappz.android.spinlogo.licensing;

import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;

import com.android.vending.licensing.AESObfuscator;
import com.android.vending.licensing.LicenseChecker;
import com.android.vending.licensing.LicenseCheckerCallback;
import com.android.vending.licensing.ServerManagedPolicy;
import com.killerappz.android.spinlogo.Constants;

/**
 * Handles the licensing process for the application,
 * according to Google's market licensing service.
 * 
 * @see http://developer.android.com/guide/publishing/licensing.html
 * @author florin
 *
 */
public class MarketLicensingManager {
	
	private final LicenseCheckerCallback mLicenseCheckerCallback; 
	private final LicenseChecker mChecker;

	public MarketLicensingManager(Context ctx) {
        String deviceId = Secure.getString(ctx.getContentResolver(), getUID(Constants.UID_SIZE));

        mLicenseCheckerCallback = new LicenseCheckerCallbackImpl();
        // Construct the LicenseChecker with the default policy.
        mChecker = new LicenseChecker(
            ctx, new ServerManagedPolicy(ctx,
                new AESObfuscator(Constants.SALT, ctx.getPackageName(), deviceId)),
            Constants.BASE64_PUBLIC_KEY);
	}
	
	
	private String getUID(int size) {
		if(Secure.ANDROID_ID != null)
			return Secure.ANDROID_ID;
		
		String uniqueString = Build.FINGERPRINT + 
    		Build.BOARD + Build.BRAND + Build.CPU_ABI + Build.DEVICE +
    		Build.DISPLAY + Build.HOST + Build.ID + Build.MANUFACTURER +
    		Build.MODEL + Build.PRODUCT + Build.TAGS + Build.TYPE +
    		Build.USER; 
		return uniqueString.substring(0, size);
	}

}
