package com.killerappz.android.spinlogo.preferences;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import min3d.core.RenderCaps;
import min3d.core.Renderer;
import min3d.core.Scene;
import min3d.core.TextureManager;
import min3d.interfaces.ISceneController;
import min3d.objectPrimitives.SkyBox;
import net.rbgrn.android.glwallpaperservice.GLWallpaperService.GLEngine;

import com.killerappz.android.spinlogo.Constants;
import com.killerappz.android.spinlogo.R;
import com.killerappz.android.spinlogo.SpinLogoRenderer;
import com.killerappz.android.spinlogo.SpinLogoWallpaperService;
import com.killerappz.android.spinlogo.SpinningLogo;
import com.killerappz.android.spinlogo.context.OffsetInfo;
import com.killerappz.android.spinlogo.context.Point;
import com.killerappz.android.spinlogo.context.SpinLogoContext;

import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * The pref page for choosing the 
 * background images to be displayed
 * 
 * @author florin
 *
 */
public class SkyboxImagePreference extends DialogPreference {
	
	public SkyboxImagePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected View onCreateDialogView() {
		View imgChoiceView = new ImageLayoutView(getContext());
		return imgChoiceView;
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		
		// Return if change was cancelled
		if (!positiveResult) {
			return;
		}
		
		// TODO save the results
		Toast.makeText(this.getContext(), "Thou pressed OK!", Toast.LENGTH_LONG);
	}
	
	private class ImageLayoutView extends GLSurfaceView {
		
		private final ImageLayoutRenderer mRenderer;

		public ImageLayoutView(Context context) {
			super(context);
			this.mRenderer = new ImageLayoutRenderer(context);
			setRenderer(mRenderer);
		}
		
		public ImageLayoutView(Context context, AttributeSet attrs) {
			super(context, attrs);
			this.mRenderer = new ImageLayoutRenderer(context);
			setRenderer(mRenderer);
		}
	
		// TODO.
	}
	
	/**
	 * Most of the code is c/p from  @see SpinLogoRenderer 
	 */
	private class ImageLayoutRenderer implements GLSurfaceView.Renderer {
		
		// the skybox object
		private SkyBox skyBox;
		// (ab)using m3d's Scene object
		private Scene scene;
		// the context informationfor rendering 
		private final SpinLogoContext contextInfo;
		private TextureManager textureManager; // for textures
		private Renderer renderer; // min3d renderer
		
		private final Context context;
		// sync between scene init and rendering
		private volatile boolean initFinished = false;
		
		public ImageLayoutRenderer(Context ctx, SpinLogoContext contextInfo) {
			this.contextInfo = contextInfo;
			this.context = ctx;
			m3dInit();
		}

		/**
		 * Initialize m3d rendering fwk. 
		 * As m3d is authoritarian and has full control on the rendering,
		 * we want to regain that control. That's why we use a phony scene,
		 * controlled by a phony scene controller - basically, we just use 
		 * these objects' structure, and not their functionality.
		 * 
		 * It is the min3d's Renderer's functionality that we are interested in.
		 * 
		 * MANDATORY min3d init steps:
		 * 	1) Create Renderer
		 *  2) Create TextureManager with Renderer as ctor arg
		 *  3) set Renderer's Texture Mgr to be this one
		 */
		private void m3dInit() {
			// the Scene
			scene = new Scene(new PhonySceneController());
			scene.reset();
			// disable lighting, don't need it for this scenario
			scene.lightingEnabled(false);

			// the Renderer
			ActivityManager activityMgr = (ActivityManager)this.context.getSystemService( Context.ACTIVITY_SERVICE );
			this.renderer = new Renderer(scene, activityMgr);
			
			// the Texture Manager
			this.textureManager = new TextureManager(this.renderer);
			this.renderer.setTextureManager(this.textureManager);
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			// if not initialized drop frame
			if(!initFinished)
				// TODO draw a Load in progress text...
				return;
			gl.glClearColor(0.2f, 0.4f, 0.2f, 1f);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			Point center = contextInfo.getCenter();
			changeCamera(center);
			// draw the skybox
			scene.addChild(skyBox);
			// delegate to min3D renderer
			renderer.onDrawFrame(gl);
		}

		/**
		 * Change the camera to look at the object
		 *   which we are supposed to draw in the center
		 *   Also draw the objects in the center
		 *   
		 * @param center
		 */
		private void changeCamera(Point center) {
			scene.camera().position.x = 
				scene.camera().target.x = center.x;
			scene.camera().position.y = 
				scene.camera().target.y = center.y;
			// set skybox position
			skyBox.position().x = center.x;
			skyBox.position().y = center.y;
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// get the center of the screen
			contextInfo.setCenter(width/2.0f, height/2.0f);
			// register the projection parameters to the Frustum
			float aspectRatio = (float)width/(float)height;
			scene.camera().frustum.fromPerspective(Constants.FIELD_OF_VIEW_Y, aspectRatio, 
					Constants.Z_NEAR_PLANE, Constants.Z_FAR_PLANE);
			// let the renderer load the frustum
			renderer.onSurfaceChanged(gl, width, height);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			initFinished = false;
			min3dSurfaceCreated(gl);
			reset(gl);
			// create skybox
			skyBox = new SkyBox(context, textureManager, Constants.SKYBOX_SIZE, Constants.SKYBOX_QUALITY_FACTOR);
			/* skybox textures */
			skyBox.addTexture(SkyBox.Face.East,  R.drawable.skybox_right,  "east_texture");
			skyBox.addTexture(SkyBox.Face.South, R.drawable.skybox_center, "south_texture");
			skyBox.addTexture(SkyBox.Face.West,  R.drawable.skybox_left,  "west_texture");
			skyBox.addTexture(SkyBox.Face.Up,    R.drawable.skybox_up,    "up_texture");
			skyBox.addTexture(SkyBox.Face.Down,  R.drawable.skybox_down,  "down_texture");
			initFinished = true;
		}

		/**
		 * Perform all necessary actions on min3d-side when the surface changes
		 * @param gl
		 */
		private void min3dSurfaceCreated(GL10 gl) {
			textureManager.reset();
			scene.reset();
			scene.lightingEnabled(false);
			renderer.setGl(gl);
			RenderCaps.setRenderCaps(gl);
		}

		// Do OpenGL settings which we are using as defaults, or which we will not be changing on-draw
		private void reset(GL10 gl) {

			// Explicit depth settings
			gl.glEnable(GL10.GL_DEPTH_TEST);									
			gl.glClearDepthf(1.0f);
			gl.glDepthFunc(GL10.GL_LESS);										
			gl.glDepthRangef(0,1f);											
			gl.glDepthMask(true);												

			// Alpha enabled
			gl.glEnable(GL10.GL_BLEND);										
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); 	

			// Texture
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST); // (OpenGL default is GL_NEAREST_MIPMAP)
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR); // (is OpenGL default)

			// CCW frontfaces only, by default
			gl.glFrontFace(GL10.GL_CCW);
			gl.glCullFace(GL10.GL_BACK);
			gl.glEnable(GL10.GL_CULL_FACE);

			// Disable lights by default
			for (int i = GL10.GL_LIGHT0; i < GL10.GL_LIGHT0 + Renderer.NUM_GLLIGHTS; i++) {
				gl.glDisable(i);
			}
		}

		/**
		 * Called when the engine is destroyed. Do any necessary clean up because
		 * at this point your renderer instance is now done for.
		 */
		public void release() {
			// clear all objects from the scene
			scene.reset();
			// clear all textures
			textureManager.reset();
		}

		class PhonySceneController implements ISceneController{

			@Override
			public Handler getInitSceneHandler() {
				return null;
			}

			@Override
			public Runnable getInitSceneRunnable() {
				return null;
			}

			@Override
			public Handler getUpdateSceneHandler() {
				return null;
			}

			@Override
			public Runnable getUpdateSceneRunnable() {
				return null;
			}

			@Override
			public void initScene() {
			}

			@Override
			public void updateScene() {
			}
			
		}

	}

}