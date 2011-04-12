package com.killerappz.android.lwp.mortalkombat.licensing;

import android.os.Build;
import android.os.Handler;
import android.provider.Settings.Secure;

import com.android.vending.licensing.AESObfuscator;
import com.android.vending.licensing.LicenseChecker;
import com.android.vending.licensing.LicenseCheckerCallback;
import com.android.vending.licensing.ServerManagedPolicy;
import com.killerappz.android.lwp.mortalkombat.Constants;
import com.killerappz.android.lwp.mortalkombat.SpinLogoWallpaperService;

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
	/**
	 * Marks if user has a valid license for product.
	 * Works like this:
	 * 1) The LWP Service requests license check
	 * 2) the License Checker receives the reply and
	 *  calls the Callback
	 * 3) the License Checker Callback 
	 *  sets this valid license flag accordingly
	 * 4) the Renderer will refuse to work(in onDrawFrame)
	 *  if the license flag is invalid!  
	 */
	public volatile boolean validLicense;

	public MarketLicensingManager(SpinLogoWallpaperService lwp, Handler handler) {
        String deviceId = Secure.getString(lwp.getContentResolver(), getUID(Constants.UID_SIZE));

        mLicenseCheckerCallback = new LicenseCheckerCallbackImpl(lwp, handler);
        // Construct the LicenseChecker with the default policy.
        mChecker = new LicenseChecker(
            lwp, new ServerManagedPolicy(lwp,
                new AESObfuscator(Constants.SALT, lwp.getPackageName(), deviceId)),
            Constants.BASE64_PUBLIC_KEY); 
        
        // initially we assume license == OK
        this.validLicense = false;
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
	
	public void doCheck() {
		mChecker.checkAccess(mLicenseCheckerCallback);
	}


	public void cleanup() {
		mChecker.onDestroy();
	}

}
