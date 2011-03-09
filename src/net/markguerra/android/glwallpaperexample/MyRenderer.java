package net.markguerra.android.glwallpaperexample;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import min3d.Shared;
import min3d.core.RenderCaps;
import min3d.core.Renderer;
import min3d.core.TextureManager;
import net.rbgrn.android.glwallpaperservice.*;
import android.content.res.Resources;
import android.opengl.GLU;

// Original code provided by Robert Green
// http://www.rbgrn.net/content/354-glsurfaceview-adapted-3d-live-wallpapers
public class MyRenderer implements GLWallpaperService.Renderer {
	
	// the spinning logo which needs to be displayed
	private ModeledObject logo;
	private final Resources res;
	// TODO should be a config option
	private static final String MODEL_RESOURCE = "net.markguerra.android.glwallpaperexample:raw/camaro_obj";

	public MyRenderer(GLWallpaperService lwpSvc) {
		res = lwpSvc.getResources();
		init_m3d(lwpSvc);
	}

	private void init_m3d(GLWallpaperService lwpSvc) {
		// ugly m3d hack
		Shared.context(lwpSvc);
		Shared.textureManager(new TextureManager());
		// this is the ugliest of them all hacks!
		Shared.renderer(new Renderer(null));
	}

	public void onDrawFrame(GL10 gl) {
		gl.glClearColor(0.2f, 0.4f, 0.2f, 1f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		autoRotate(gl);
		gl.glColor4f(.2f, 0f, .5f, 1f);
		logo.draw(gl); 
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 60f, (float)width/(float)height, 1f, 100f);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -5);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// once again, ugly m3d hack
		surfaceCreated_min3d(gl);
		logo = new ModeledObject(res, MODEL_RESOURCE);

		reset(gl);
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

		// "Transparency is best implemented using glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA) 
		// with primitives sorted from farthest to nearest."

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

	private void surfaceCreated_min3d(GL10 gl) {
		Shared.renderer().setGl(gl);
		RenderCaps.setRenderCaps(gl);
		// Reset TextureManager
		Shared.textureManager().reset();
	}

	/**
	 * Called when the engine is destroyed. Do any necessary clean up because
	 * at this point your renderer instance is now done for.
	 */
	public void release() {

	}

	private void autoRotate(GL10 gl) {
		gl.glRotatef(1, 0, 1, 0);
		gl.glRotatef(0.5f, 1, 0, 0);
	}
}
