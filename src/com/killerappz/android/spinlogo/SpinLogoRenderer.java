package com.killerappz.android.spinlogo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import min3d.Shared;
import min3d.core.RenderCaps;
import min3d.core.Renderer;
import min3d.core.Scene;
import min3d.core.TextureManager;
import min3d.interfaces.ISceneController;
import net.rbgrn.android.glwallpaperservice.GLWallpaperService;
import android.content.res.Resources;
import android.opengl.GLU;
import android.os.Handler;

import com.killerappz.android.spinlogo.context.Point;
import com.killerappz.android.spinlogo.context.SpinLogoContext;

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
	private final Resources res;
	// (ab)using m3d's Scene object
	private Scene scene;
	// the context informationfor rendering 
	private final SpinLogoContext contextInfo;
	
	// sync between scene init and rendering
	private volatile boolean initFinished = false;

	public SpinLogoRenderer(GLWallpaperService lwpSvc, SpinLogoContext contextInfo) {
		res = lwpSvc.getResources();
		this.contextInfo = contextInfo;
		m3dInit(lwpSvc);
	}

	/**
	 * Initialize m3d rendering fwk. 
	 * As m3d is authoritarian and has full control on the rendering,
	 * we want to regain that control. That's why we use a phony scene,
	 * controlled by a phony scene controller - basically, we just use 
	 * these objects' structure, and not their functionality.
	 * 
	 * It is the min3d's Renderer's functionality that we are interested in.
	 */
	private void m3dInit(GLWallpaperService lwpSvc) {
		Shared.context(lwpSvc);
		Shared.textureManager(new TextureManager());
		// Reset TextureManager
		Shared.textureManager().reset();
		scene = new Scene(new PhonySceneController());
		scene.reset();
		// disable lighting, don't need it for this scenario
		scene.lightingEnabled(false);
		// the new Renderer
		Renderer renderer = new Renderer(scene);
		if(Shared.renderer()!=null)
			// temp set; will be overriden onSurfaceCreate
			renderer.setGl(Shared.renderer().gl());
		Shared.renderer(renderer);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// if not initialized drop frame
		if(!initFinished)
			// TODO show a Load in progress screen...
			return;

		gl.glClearColor(0.2f, 0.4f, 0.2f, 1f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		Point center = contextInfo.getCenter();
		changeCamera(center);
		logo.draw(gl);
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
		// This part sets the perspective matrix. 
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 60f, (float)width/(float)height, 1f, 100f);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		initFinished = false;
		min3dSurfaceCreated(gl);
		reset(gl);
		// create spinning logo object
		String modelResource = SpinLogoRenderer.class.getPackage().getName() 
				+ ":" + Constants.LOGO_MODEL_FILE;
		logo = new SpinningLogo(res, modelResource, contextInfo, scene);
		initFinished = true;
	}

	/**
	 * Perform all necessary actions on min3d-side when the surface changes
	 * @param gl
	 */
	private void min3dSurfaceCreated(GL10 gl) {
		Shared.textureManager().reset();
		scene.reset();
		scene.lightingEnabled(false);
		Shared.renderer().setGl(gl);
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
