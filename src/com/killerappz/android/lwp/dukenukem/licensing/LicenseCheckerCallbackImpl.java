package com.killerappz.android.lwp.dukenukem.licensing;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;

import com.android.vending.licensing.LicenseCheckerCallback;
import com.killerappz.android.lwp.dukenukem.Constants;
import com.killerappz.android.lwp.dukenukem.SpinLogoWallpaperService;
import com.killerappz.android.lwp.dukenukem.R;

/**
 * Callback implementation
 * 
 * @author florin
 *
 */
public class LicenseCheckerCallbackImpl implements LicenseCheckerCallback {
	
	private final SpinLogoWallpaperService lwp;
	private final Handler mHandler;

	public LicenseCheckerCallbackImpl(SpinLogoWallpaperService lwp, Handler handler) {
		this.lwp = lwp;
		this.mHandler = handler;
	}

	/* (non-Javadoc)
	 * @see com.android.vending.licensing.LicenseCheckerCallback#allow()
	 */
	@Override
	public void allow() {
		Log.d(Constants.LOG_TAG, "License is valid");
		this.lwp.setLicenseStatus(true);
		updateLicenseStatus(Constants.OK_LICENSE_STATUS);
	}

	private void updateLicenseStatus(String licenseStatus) {
		Editor editor = this.lwp.getSharedPreferences(Constants.PREFS_NAME, 0).edit();
		editor.putString(Constants.LICENSE_STATUS_KEY, licenseStatus);
		editor.commit();
	}

	/* (non-Javadoc)
	 * @see com.android.vending.licensing.LicenseCheckerCallback#applicationError(com.android.vending.licensing.LicenseCheckerCallback.ApplicationErrorCode)
	 */
	@Override
	public void applicationError(ApplicationErrorCode errorCode) {
		Log.e(Constants.LOG_TAG, "License check error:" + Constants.licenseErrorCodes[errorCode.ordinal()]);
		// warn the user
		warnUser();
		// mark invalid license
		this.lwp.setLicenseStatus(false);
		updateLicenseStatus(Constants.licenseErrorCodes[errorCode.ordinal()]);
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
		updateLicenseStatus(Constants.INVALID_LICENSE_STATUS);
	}

	/**
	 * Notify the user the reason why we're stopping
	 * the app: running without a license!
	 * 
	 */
	private void warnUser() {
		this.mHandler.post(new Runnable() {
			public void run() {
				// show status bar notification
				Context context = LicenseCheckerCallbackImpl.this.lwp.getApplicationContext();
				NotificationManager notificationManager = (NotificationManager) 
					context.getSystemService(Context.NOTIFICATION_SERVICE);

				// Default notification icon is the warning symbol
				int icon = android.R.drawable.stat_notify_error;

				CharSequence tickerText = context.getText(R.string.license_check_notif_ticker_text);
				long when = System.currentTimeMillis();
				Notification notification = new Notification(icon, tickerText, when);

				CharSequence contentTitle = context.getText(R.string.license_check_notif_title);
				CharSequence contentText = context.getText(R.string.license_check_notif_text);

				Intent notificationIntent = new Intent(context, LicenseValidationDialog.class);
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

				notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
				notificationManager.notify(Constants.NOTIF_TICKER_ID, notification);

			}
		});
	}

}
