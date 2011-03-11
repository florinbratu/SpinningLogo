package com.killerappz.android.spinlogo;

import javax.microedition.khronos.opengles.GL10;

import min3d.Shared;
import min3d.core.Object3dContainer;
import min3d.core.Scene;
import android.content.res.Resources;

/**
 * The Spinning Logo object
 * 
 */
public class SpinningLogo {

	private final Object3dContainer object;
	private final Scene scene;
	private static final float ROTATION_SPEED_UNIT = 0.1f;
	private final float rotationSpeed;  
	
	// rotSpeed is the rotation speed measured in ROTATION_SPEED_UNIT
	public SpinningLogo(Resources resources, String resId, int rotSpeed, Scene scene) {
		object = new ObjLoader().load(resources, resId);
		this.rotationSpeed = (float)rotSpeed * ROTATION_SPEED_UNIT;
 		this.scene = scene;
	}
	
	public void draw(GL10 gl){
		// draw all objects within the container
		scene.addChild(object);
		Shared.renderer().onDrawFrame(gl);
		autoRotate();
	}

	private void autoRotate() {
		object.rotation().y += rotationSpeed;
	}
	
}
