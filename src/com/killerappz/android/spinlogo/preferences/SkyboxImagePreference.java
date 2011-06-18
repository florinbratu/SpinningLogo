package com.killerappz.android.spinlogo.preferences;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import min3d.core.RenderCaps;
import min3d.core.Renderer;
import min3d.core.Scene;
import min3d.core.TextureManager;
import min3d.interfaces.ISceneController;
import min3d.objectPrimitives.SkyBox;
import min3d.objectPrimitives.SkyBox.Face;
import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.killerappz.android.spinlogo.Constants;
import com.killerappz.android.spinlogo.R;
import com.killerappz.android.spinlogo.context.ContextInfo;
import com.killerappz.android.spinlogo.context.NoPreferencesContextInfo;
import com.killerappz.android.spinlogo.context.Point;
import com.killerappz.android.spinlogo.context.Rectangle;
import com.killerappz.android.spinlogo.preferences.matrix.MatrixTrackingGL;
import com.killerappz.android.spinlogo.preferences.matrix.Projector;

/**
 * The pref page for choosing the 
 * background images to be displayed
 * 
 * @author florin
 *
 */
public class SkyboxImagePreference extends DialogPreference {
	
	private GLSurfaceView mGLView;
	
	public SkyboxImagePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected View onCreateDialogView() {
		mGLView = new ImageLayoutView(getContext());
		// get some focus in here!
		mGLView.requestFocus();
        mGLView.setFocusableInTouchMode(true);
		return mGLView;
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
		private final ContextInfo contextInfo;

		public ImageLayoutView(Context context) {
			super(context);
			this.contextInfo = new NoPreferencesContextInfo();
			// set wrapper keeping track of the projection matrices
			setGLWrapper(new GLSurfaceView.GLWrapper() {
	            public GL wrap(GL gl) {
	                return new MatrixTrackingGL(gl);
	            }});
			this.mRenderer = new ImageLayoutRenderer(context, contextInfo);
			setRenderer(mRenderer);
			setRenderMode(RENDERMODE_WHEN_DIRTY);
		}
		
		public ImageLayoutView(Context context, AttributeSet attrs) {
			super(context, attrs);
			this.contextInfo = new NoPreferencesContextInfo();
			this.mRenderer = new ImageLayoutRenderer(context, contextInfo);
			setRenderer(mRenderer);
			setRenderMode(RENDERMODE_WHEN_DIRTY);
		}

		@Override 
		public boolean onTouchEvent(MotionEvent e) {
	        switch (e.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	        	contextInfo.setTouchPoint(e.getX(), e.getY());
	        	requestRender();
	        	break;
	        case MotionEvent.ACTION_MOVE:
	        	/* TODO
	        	float dx = x - mPreviousX;
	        	float dy = y - mPreviousY;
	        	mRenderer.mAngleX += dx * TOUCH_SCALE_FACTOR;
	        	mRenderer.mAngleY += dy * TOUCH_SCALE_FACTOR;
	        	requestRender();*/
	        	break;
	        case MotionEvent.ACTION_UP:
	        	// TODO
	        case MotionEvent.ACTION_CANCEL:
	        	contextInfo.setTouched(false);
	        	requestRender();
	        	break;
	        }
	        return true;
	    }
		
	}
	
	/**
	 * Most of the code is c/p from  @see SpinLogoRenderer 
	 */
	private class ImageLayoutRenderer implements GLSurfaceView.Renderer {
		
		// the skybox object
		private SkyBox skyBox;
		// skybox texture names. 
		private final Map<SkyBox.Face,String> faceNames;
		// (ab)using m3d's Scene object
		private Scene scene;
		// the context informationfor rendering 
		private final ContextInfo contextInfo;
		private TextureManager textureManager; // for textures
		private Renderer renderer; // min3d renderer
		
		private final Context context;
		// sync between scene init and rendering
		private volatile boolean initFinished = false;
		
		// store if texture is currently highlighted
		private Face highlightedFace = Face.North; // North is magic for None
		
		// get the current matrices(modelview, projection,...)
		private final Projector projektor; 
		
		public ImageLayoutRenderer(Context ctx, ContextInfo contextInfo) {
			this.contextInfo = contextInfo;
			this.context = ctx;
			this.projektor = new Projector();
			m3dInit();
			// names for the textures
			faceNames = new HashMap<SkyBox.Face, String>();
			faceNames.put(SkyBox.Face.East,  "east_texture");
			faceNames.put(SkyBox.Face.South, "south_texture");
			faceNames.put(SkyBox.Face.West,  "west_texture");
			faceNames.put(SkyBox.Face.Up,    "up_texture");
			faceNames.put(SkyBox.Face.Down,  "down_texture");
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
			// touch test
			if( highlightedFace.equals(Face.North) && contextInfo.isTouched()) {
				// switch from normal texture to highlighted
				this.highlightedFace = getHighlightedFace(gl);
				skyBox.highlightTexture(highlightedFace, 
						faceNames.get(highlightedFace));
			}
			else if( !highlightedFace.equals(Face.North) && !contextInfo.isTouched()) {
				// switch from highlighted to normal texture
				skyBox.unhighlightTexture(highlightedFace, 
						faceNames.get(highlightedFace));
				this.highlightedFace = Face.North;
			}
			// delegate to min3D renderer
			renderer.onDrawFrame(gl);
		}
		
		// find out which texture needs to be highlighted
		// according to the placement of the touch point
		private SkyBox.Face getHighlightedFace(GL10 gl) {
			// project the central rectangle to screen coordinates
			// only to find out the z
			float halfSize = Constants.SKYBOX_PREF_SIZE * 0.5f;
			Rectangle skyBoxFront = new Rectangle( halfSize, halfSize, 
					-halfSize, -halfSize, -halfSize);
			Log.d(Constants.LOG_TAG, "Skybox center, before projection:" + skyBoxFront);
			// load up the current modelview matrix
			projektor.getRectModelView(gl, skyBoxFront);
			// show us the matrices
			Log.d(Constants.LOG_TAG, projektor.toString());
			Rectangle projectedSkyBoxFront = skyBoxFront.toScreenCoords(projektor);
			Log.d(Constants.LOG_TAG, "Skybox center, after projection:" + projectedSkyBoxFront);
			Log.d(Constants.LOG_TAG, "Touch Point world coords:" + contextInfo.getTouchPoint());
			float[] touch = new float[] { contextInfo.getTouchPoint().x, 
					contextInfo.getTouchPoint().y, projectedSkyBoxFront.z};
			float[] objTouch = new float[4];
			projektor.unprojekt(touch, 0, objTouch, 0);
			Point unprojectedTouchPoint = new Point(objTouch[0], objTouch[1]);
			Log.d(Constants.LOG_TAG, "Unprojected Touch Point:" + unprojectedTouchPoint + " z value:" + objTouch[2]);
			return skyBoxFront.position( unprojectedTouchPoint,	new Point(0,0));
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
			contextInfo.setCenter(width, height);
			// save up the width/height in the projektor
			projektor.setCurrentView(0, 0, width, height);
			// register the projection parameters to the Frustum
			float aspectRatio = (float)width/(float)height;
			scene.camera().frustum.fromPerspective(Constants.FIELD_OF_VIEW_Y, aspectRatio, 
					Constants.Z_NEAR_PLANE, Constants.Z_FAR_PLANE);
			// let the renderer load the frustum
			renderer.onSurfaceChanged(gl, width, height);
			// save up the projection matrix
			projektor.getFrustumProjection(gl, scene.camera().frustum.frustum);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			initFinished = false;
			min3dSurfaceCreated(gl);
			reset(gl);
			// create skybox
			skyBox = new SkyBox(context, textureManager, Constants.SKYBOX_PREF_SIZE, 
					Constants.SKYBOX_PREF_QUALITY_FACTOR);
			/* skybox textures */
			skyBox.addTextureWithHighligh(SkyBox.Face.East,  R.drawable.skybox_right,  this.faceNames.get(Face.East));
			skyBox.addTextureWithHighligh(SkyBox.Face.South, R.drawable.skybox_center, this.faceNames.get(Face.South));
			skyBox.addTextureWithHighligh(SkyBox.Face.West,  R.drawable.skybox_left,   this.faceNames.get(Face.West));
			skyBox.addTextureWithHighligh(SkyBox.Face.Up,    R.drawable.skybox_up,     this.faceNames.get(Face.Up));
			skyBox.addTextureWithHighligh(SkyBox.Face.Down,  R.drawable.skybox_down,   this.faceNames.get(Face.Down));
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
