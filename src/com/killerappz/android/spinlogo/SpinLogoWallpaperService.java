package com.killerappz.android.spinlogo;

import net.rbgrn.android.glwallpaperservice.*;

// Original code provided by Robert Green
// http://www.rbgrn.net/content/354-glsurfaceview-adapted-3d-live-wallpapers
public class SpinLogoWallpaperService extends GLWallpaperService {
	public SpinLogoWallpaperService() {
		super();
	}

	public Engine onCreateEngine() {
		SpinLogoEngine engine = new SpinLogoEngine();
		return engine; 
	}

	class SpinLogoEngine extends GLEngine {
		SpinLogoRenderer renderer;
		public SpinLogoEngine() {
			super();
			// handle prefs, other initialization
			renderer = new SpinLogoRenderer(SpinLogoWallpaperService.this);
			setRenderer(renderer);
			setRenderMode(RENDERMODE_CONTINUOUSLY);
		}

		public void onDestroy() {
			super.onDestroy();
			if (renderer != null) {
				renderer.release();
			}
			renderer = null;
		}
	}
}
