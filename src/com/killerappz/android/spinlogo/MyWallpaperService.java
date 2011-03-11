package com.killerappz.android.spinlogo;

import net.rbgrn.android.glwallpaperservice.*;

// Original code provided by Robert Green
// http://www.rbgrn.net/content/354-glsurfaceview-adapted-3d-live-wallpapers
public class MyWallpaperService extends GLWallpaperService {
	public MyWallpaperService() {
		super();
	}

	public Engine onCreateEngine() {
		MyEngine engine = new MyEngine();
		return engine; 
	}

	class MyEngine extends GLEngine {
		SpinLogoRenderer renderer;
		public MyEngine() {
			super();
			// handle prefs, other initialization
			renderer = new SpinLogoRenderer(MyWallpaperService.this);
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
