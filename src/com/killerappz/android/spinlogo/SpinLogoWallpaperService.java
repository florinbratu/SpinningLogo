package com.killerappz.android.spinlogo;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;
import android.content.SharedPreferences;
import android.os.Handler;

import com.killerappz.android.spinlogo.context.SpinLogoContext;
import com.killerappz.android.spinlogo.licensing.MarketLicensingManager;

// Original code provided by Robert Green
// http://www.rbgrn.net/content/354-glsurfaceview-adapted-3d-live-wallpapers
public class SpinLogoWallpaperService extends GLWallpaperService {
	private final SpinLogoContext contextInfo;
	// market licensing manager
	private MarketLicensingManager mLicenseManager;
	
	public SpinLogoWallpaperService() {
		super();
		contextInfo = new SpinLogoContext();
		
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		// create the market license manager
		mLicenseManager = new MarketLicensingManager(this, new Handler());
		// check license OBS this is not synchronous, unless there's info in its cache
		mLicenseManager.doCheck();
	}

	public Engine onCreateEngine() {
		SpinLogoEngine engine = new SpinLogoEngine();
		return engine; 
	}
	
	@Override
	public void onDestroy() {
		// cleanup for upper layers
		super.onDestroy();
		// cleanup the License Manager
		mLicenseManager.cleanup();
	}
	
	@Override
	public void onOffsetsChanged(float xOffset, float yOffset,
			float xOffsetStep, float yOffsetStep, int xPixelOffset,
			int yPixelOffset) {
		// store all info we acquired
		contextInfo.setOffset(xOffset, yOffset, xOffsetStep, 
				yOffsetStep, xPixelOffset, yPixelOffset);
	}
	
	// license related ops
	public boolean hasValidLicense() {
		return mLicenseManager.validLicense;
	}
	
	public void setLicenseStatus(boolean validLicense) {
		mLicenseManager.validLicense = validLicense;
	}

	class SpinLogoEngine extends GLEngine 
	{
		// access to the user preferences
		private final SharedPreferences mPreferences;
		// the renderer
		private SpinLogoRenderer renderer;
		
		public SpinLogoEngine() {
			super();
			renderer = new SpinLogoRenderer(SpinLogoWallpaperService.this, 
					SpinLogoWallpaperService.this.contextInfo,
					this);
			setRenderer(renderer);
			setRenderMode(RENDERMODE_CONTINUOUSLY);
			// prefs
			mPreferences = SpinLogoWallpaperService.this.getSharedPreferences(Constants.PREFS_NAME, 0);
			mPreferences.registerOnSharedPreferenceChangeListener(contextInfo);
			SpinLogoWallpaperService.this.contextInfo.loadPrefs(mPreferences);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			if (renderer != null) {
				renderer.release();
			}
			renderer = null;
			mPreferences.unregisterOnSharedPreferenceChangeListener(contextInfo);
		}
	}
}
