package com.killerappz.android.lwp.dukenukem;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import min3d.core.RenderCaps;
import min3d.core.Renderer;
import min3d.core.Scene;
import min3d.core.TextureManager;
import min3d.interfaces.ISceneController;
import net.rbgrn.android.glwallpaperservice.GLWallpaperService;
import net.rbgrn.android.glwallpaperservice.GLWallpaperService.GLEngine;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;

import com.killerappz.android.lwp.dukenukem.context.OffsetInfo;
import com.killerappz.android.lwp.dukenukem.context.Point;
import com.killerappz.android.lwp.dukenukem.context.SpinLogoContext;

/**
 * This is the renderer implementation class for the Wallpaper Service
 * It relies on the min3d library to do the heavyweight rendering
 * 
 * @author florin
 *
 */
public class SpinLogoRenderer implements GLWallpaperService.Renderer {
	
	// the spinning logo which needs to be displayed
	private SpinningLogo logo;
	// (ab)using m3d's Scene object
	private Scene scene;
	// the context informationfor rendering 
	private final SpinLogoContext contextInfo;
	private TextureManager textureManager; // for textures
	private final SpinLogoWallpaperService wallpaperRef; // for resources and license checking
	private Renderer renderer; // min3d renderer
	
	// sync between scene init and rendering
	private volatile boolean initFinished = false;
	
	// we need the engine, at least to know 
	// whether we're running in preview mode or not
	private final GLEngine underlyingEngine;

	public SpinLogoRenderer(SpinLogoWallpaperService wallpaper, SpinLogoContext contextInfo, GLEngine theEngine) {
		this.wallpaperRef = wallpaper;
		this.contextInfo = contextInfo;
		this.underlyingEngine = theEngine;
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
		ActivityManager activityMgr = (ActivityManager)this.wallpaperRef.getSystemService( Context.ACTIVITY_SERVICE );
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
		
		// if invalid license, refuse to work!
		if(!this.wallpaperRef.hasValidLicense())
			// TODO draw a Invalid license text
			return;

		gl.glClearColor(0.2f, 0.4f, 0.2f, 1f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		Point center = contextInfo.getCenter();
		changeCamera(center);
		// shift according to offset
		shift(contextInfo.getOffset());
		logo.draw(gl, this.renderer);
	}

	/**
	 * Shift the view according to the offset information.
	 * 	Do not perform the shift in preview mode
	 * 
	 * @param offset the Offset information
	 */
	private void shift(OffsetInfo offset) {
		/* the oblique projection angle ranges are(increasing as offset increases)
		 * <ul>
		 * <li> [- MAX_OBLIQUE_ANGLE, MAX_OBLIQUE_ANGLE] for X axis </li>
		 * <li> [0, MAX_OBLIQUE_ANGLE] for Y axis </li>
		 * </ul>
		 *  */
		if(!this.underlyingEngine.isPreview())
			scene.camera().frustum.obliqueProjectionAngles(
					Constants.MAX_OBLIQUE_ANGLE * ( 2.0f * offset.xOffset - 1),
					Constants.MAX_OBLIQUE_ANGLE * offset.yOffset );
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
		logo.setCenter(center);
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
		// create spinning logo object
		String modelResource = SpinLogoRenderer.class.getPackage().getName() 
				+ ":" + Constants.LOGO_MODEL_FILE;
		logo = new SpinningLogo(wallpaperRef, textureManager, modelResource, contextInfo, scene);
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

	@Override
	public void onSurfaceLost() {
		// unload textures. Or not?
	}
}
