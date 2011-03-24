package com.killerappz.android.spinlogo.licensing;

import android.content.Context;
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
		// TODO Try to use more data here. ANDROID_ID is a single point of attack.
        String deviceId = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);

        mLicenseCheckerCallback = new LicenseCheckerCallbackImpl();
        // Construct the LicenseChecker with the default policy.
        mChecker = new LicenseChecker(
            ctx, new ServerManagedPolicy(ctx,
                new AESObfuscator(Constants.SALT, ctx.getPackageName(), deviceId)),
            Constants.BASE64_PUBLIC_KEY);
	}

}
