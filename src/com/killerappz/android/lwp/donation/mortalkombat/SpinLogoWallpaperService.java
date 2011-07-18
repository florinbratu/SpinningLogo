package com.killerappz.android.lwp.donation.mortalkombat;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.killerappz.android.lwp.donation.mortalkombat.context.SpinLogoContext;
import com.killerappz.android.lwp.donation.mortalkombat.licensing.MarketLicensingManager;

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
		// touch gestures detektor
		private final GestureDetector gestureDetector;
		// the scale detector
		private final ScaleGestureDetector scaleDetector;
		
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
			// touch gesture detector
			TouchGesturesHandler touchHandler = new TouchGesturesHandler(contextInfo, mPreferences);
			gestureDetector = new GestureDetector(SpinLogoWallpaperService.this, touchHandler);
			scaleDetector = new ScaleGestureDetector(SpinLogoWallpaperService.this, touchHandler);
		}
		
		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			// By default we don't get touch events, so enable them. Android is fucked up!
	        setTouchEventsEnabled(true);
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
		
		// touch impl
		@Override
		public void onTouchEvent(MotionEvent event) {
			gestureDetector.onTouchEvent(event);
			scaleDetector.onTouchEvent(event);
		}
	}
}
