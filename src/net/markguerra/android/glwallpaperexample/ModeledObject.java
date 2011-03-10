package net.markguerra.android.glwallpaperexample;

import javax.microedition.khronos.opengles.GL10;

import min3d.Shared;
import min3d.core.Object3dContainer;
import min3d.core.Scene;
import android.content.res.Resources;

/**
 * A single modeled object via an obj file
 * 
 */
public class ModeledObject {

	private final Object3dContainer object;
	private final Scene scene;
	
	public ModeledObject(Resources resources, String resId, Scene scene) {
		object = new ObjLoader().load(resources, resId);
		this.scene = scene;
	}
	
	public void draw(GL10 gl){
		// draw all objects within the container
		scene.addChild(object);
		Shared.renderer().onDrawFrame(gl);
		autoRotate();
	}

	private void autoRotate() {
		object.rotation().x++;
		object.rotation().z++;
	}
	
}
