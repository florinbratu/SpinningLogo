package com.killerappz.android.lwp.mortalkombat.licensing;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.vending.licensing.LicenseCheckerCallback;
import com.killerappz.android.lwp.mortalkombat.Constants;
import com.killerappz.android.lwp.mortalkombat.SpinLogoWallpaperService;
import com.killerappz.android.lwp.mortalkombat.R;

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
		Log.d(Constants.LOG_TAG, "License is valid");
		this.lwp.setLicenseStatus(true);
	}

	/* (non-Javadoc)
	 * @see com.android.vending.licensing.LicenseCheckerCallback#applicationError(com.android.vending.licensing.LicenseCheckerCallback.ApplicationErrorCode)
	 */
	@Override
	public void applicationError(ApplicationErrorCode errorCode) {
		Log.e(Constants.LOG_TAG, "License check error:" + Constants.licenseErrorCodes[errorCode.ordinal()]);
		invalidLicenseToast.setText( this.lwp.getString(R.string.license_check_error)
				+ Constants.licenseErrorCodes[errorCode.ordinal()]);
		dontAllow();
	}

	/* (non-Javadoc)
	 * @see com.android.vending.licensing.LicenseCheckerCallback#dontAllow()
	 */
	@Override
	public void dontAllow() {
		Log.d(Constants.LOG_TAG, "License is invalid!");
		// warn the user
		warnUser();
		// mark invalid license
		this.lwp.setLicenseStatus(false);
	}

	/**
	 * Notify the user the reason why we're stopping
	 * the app: running without a license!
	 * 
	 * TODO more fancy stuff: display dialog with
	 * 1) retry button
	 * 2) link to Android Market page button
	 * you can use the MainActivity from andorid sample
	 * as a starting point
	 * Potential pitfall: http://stackoverflow.com/questions/4131619/alertdialog-show-silently-ignored-within-a-service
	 */
	private void warnUser() {
		this.mHandler.post(new Runnable() {
			public void run() {
				invalidLicenseToast.show();
			}
		});
	}

}
