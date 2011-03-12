package com.killerappz.android.spinlogo;

import javax.microedition.khronos.opengles.GL10;

import com.killerappz.android.spinlogo.context.SpinLogoContext;

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
	private SpinLogoContext contextInfo;  
	
	// rotSpeed is the rotation speed measured in ROTATION_SPEED_UNIT
	public SpinningLogo(Resources resources, String resId, SpinLogoContext contextInfo, Scene scene) {
		object = new ObjLoader().load(resources, resId);
		this.contextInfo = contextInfo;
 		this.scene = scene;
	}
	
	public void draw(GL10 gl){
		// draw all objects within the container
		scene.addChild(object);
		Shared.renderer().onDrawFrame(gl);
		autoRotate();
	}

	private void autoRotate() {
		float rotationSpeed = (float)contextInfo.getRotationSpeed() * Constants.ROTATION_SPEED_UNIT;
		object.rotation().y += rotationSpeed;
	}
	
}
