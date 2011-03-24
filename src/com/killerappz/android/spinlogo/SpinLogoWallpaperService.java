package com.killerappz.android.spinlogo;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;
import android.content.SharedPreferences;

import com.killerappz.android.spinlogo.context.SpinLogoContext;
import com.killerappz.android.spinlogo.licensing.MarketLicensingManager;

// Original code provided by Robert Green
// http://www.rbgrn.net/content/354-glsurfaceview-adapted-3d-live-wallpapers
public class SpinLogoWallpaperService extends GLWallpaperService {
	private final SpinLogoContext contextInfo;
	// market licensing manager
	private final MarketLicensingManager mLicenseManager;
	
	public SpinLogoWallpaperService() {
		super();
		contextInfo = new SpinLogoContext();
		mLicenseManager = new MarketLicensingManager(this);
	}

	public Engine onCreateEngine() {
		// check license OBS this is not synchronous, unless there's info in its cache
		mLicenseManager.doCheck();
		SpinLogoEngine engine = new SpinLogoEngine();
		return engine; 
	}

	class SpinLogoEngine extends GLEngine 
	{
		// access to the user preferences
		private final SharedPreferences mPreferences;
		// the renderer
		private SpinLogoRenderer renderer;
		
		public SpinLogoEngine() {
			super();
			renderer = new SpinLogoRenderer(SpinLogoWallpaperService.this, SpinLogoWallpaperService.this.contextInfo);
			setRenderer(renderer);
			setRenderMode(RENDERMODE_CONTINUOUSLY);
			// prefs
			mPreferences = SpinLogoWallpaperService.this.getSharedPreferences(Constants.PREFS_NAME, 0);
			mPreferences.registerOnSharedPreferenceChangeListener(contextInfo);
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
