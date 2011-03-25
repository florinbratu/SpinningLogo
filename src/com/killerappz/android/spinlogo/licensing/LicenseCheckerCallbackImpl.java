package com.killerappz.android.spinlogo.licensing;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.vending.licensing.LicenseCheckerCallback;
import com.killerappz.android.spinlogo.Constants;
import com.killerappz.android.spinlogo.R;
import com.killerappz.android.spinlogo.SpinLogoWallpaperService;

/**
 * Callback implementation
 * 
 * @author florin
 *
 */
public class LicenseCheckerCallbackImpl implements LicenseCheckerCallback {
	
	private final SpinLogoWallpaperService lwp;
	private final Handler mHandler;
	private final Toast invalidLicenseToast;

	public LicenseCheckerCallbackImpl(SpinLogoWallpaperService lwp, Handler handler) {
		this.lwp = lwp;
		this.mHandler = handler;
		invalidLicenseToast = Toast.makeText(this.lwp, 
				R.string.invalid_license, Toast.LENGTH_LONG);
	}

	/* (non-Javadoc)
	 * @see com.android.vending.licensing.LicenseCheckerCallback#allow()
	 */
	@Override
	public void allow() {
		// TODO should I do anything here?
		Log.d(Constants.LOG_TAG, "App is allowed");
	}

	/* (non-Javadoc)
	 * @see com.android.vending.licensing.LicenseCheckerCallback#applicationError(com.android.vending.licensing.LicenseCheckerCallback.ApplicationErrorCode)
	 */
	@Override
	public void applicationError(ApplicationErrorCode errorCode) {
		Log.e(Constants.LOG_TAG, Constants.licenseErrorCodes[errorCode.ordinal()]);
		dontAllow();
	}

	/* (non-Javadoc)
	 * @see com.android.vending.licensing.LicenseCheckerCallback#dontAllow()
	 */
	@Override
	public void dontAllow() {
		Log.d(Constants.LOG_TAG, "App is NOT allowed");
		// warn the user
		warnUser();
		// stop the service
		this.lwp.stopSelf();
	}

	/**
	 * Notify the user the reason why we're stopping
	 * the app: running without a license!
	 * 
	 * TODO more fancy stuff: display dialog with
	 * 1) retry button
	 * 2) link to Android Market page button
	 */
	private void warnUser() {
		this.mHandler.post(new Runnable() {
			public void run() {
				invalidLicenseToast.show();
			}
		});
	}

}
