package com.killerappz.android.spinlogo.free;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;
import android.content.SharedPreferences;

import com.killerappz.android.spinlogo.free.context.SpinLogoContext;

// Original code provided by Robert Green
// http://www.rbgrn.net/content/354-glsurfaceview-adapted-3d-live-wallpapers
public class SpinLogoWallpaperService extends GLWallpaperService {
	private final SpinLogoContext contextInfo;
	
	public SpinLogoWallpaperService() {
		super();
		contextInfo = new SpinLogoContext();
		
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	public Engine onCreateEngine() {
		SpinLogoEngine engine = new SpinLogoEngine();
		return engine; 
	}
	
	@Override
	public void onDestroy() {
		// cleanup for upper layers
		super.onDestroy();
	}
	
	@Override
	public void onOffsetsChanged(float xOffset, float yOffset,
			float xOffsetStep, float yOffsetStep, int xPixelOffset,
			int yPixelOffset) {
		// store all info we acquired
		contextInfo.setOffset(xOffset, yOffset, xOffsetStep, 
				yOffsetStep, xPixelOffset, yPixelOffset);
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
