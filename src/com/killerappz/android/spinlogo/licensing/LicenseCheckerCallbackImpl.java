package com.killerappz.android.spinlogo.licensing;

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

	public LicenseCheckerCallbackImpl(SpinLogoWallpaperService lwp) {
		this.lwp = lwp;
	}

	/* (non-Javadoc)
	 * @see com.android.vending.licensing.LicenseCheckerCallback#allow()
	 */
	@Override
	public void allow() {
		// TODO should I do anything here?
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
		/* TODO more fancy stuff: display dialog with
		 * 1) retry button
		 * 2) link to Android Market page button
		 */
		Toast.makeText(this.lwp, 
				R.string.invalid_license, Toast.LENGTH_SHORT).show();
		this.lwp.stopSelf();
	}

}
