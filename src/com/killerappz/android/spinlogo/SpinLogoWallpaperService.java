package com.killerappz.android.spinlogo;

import com.killerappz.android.spinlogo.context.ContextInfo;
import com.killerappz.android.spinlogo.context.SpinLogoContext;

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
		private SpinLogoRenderer renderer;
		private ContextInfo contextInfo;
		
		public SpinLogoEngine() {
			super();
			// handle prefs, other initialization
			SpinLogoContext theContext = new SpinLogoContext();
			renderer = new SpinLogoRenderer(SpinLogoWallpaperService.this, theContext);
			contextInfo = theContext;
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
