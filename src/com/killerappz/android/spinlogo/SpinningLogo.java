package com.killerappz.android.spinlogo;

import javax.microedition.khronos.opengles.GL10;

import com.killerappz.android.spinlogo.context.Point;
import com.killerappz.android.spinlogo.context.SpinLogoContext;

import min3d.Shared;
import min3d.core.Object3dContainer;
import min3d.core.Scene;
import min3d.objectPrimitives.SkyBox;
import android.content.res.Resources;

/**
 * The Spinning Logo object
 * 
 */
public class SpinningLogo {

	private final Object3dContainer object;
	private final Scene scene;
	private SpinLogoContext contextInfo;
	// the skybox
	private SkyBox skyBox;
	
	// rotSpeed is the rotation speed measured in ROTATION_SPEED_UNIT
	public SpinningLogo(Resources resources, String resId, SpinLogoContext contextInfo, Scene scene) {
		object = new ObjLoader().load(resources, resId);
		this.contextInfo = contextInfo;
 		this.scene = scene;
 		this.skyBox = createSkyBox();
	}
	
	public void draw(GL10 gl){
		// draw all objects within the container
		scene.addChild(object);
		scene.addChild(skyBox);
		Shared.renderer().onDrawFrame(gl);
		autoRotate();
	}

	private void autoRotate() {
		float rotationSpeed = (float)contextInfo.getRotationSpeed() * Constants.ROTATION_SPEED_UNIT;
		object.rotation().y += rotationSpeed;
	}
	
	private SkyBox createSkyBox() {
		SkyBox skyBox = new SkyBox(Constants.SKYBOX_SIZE, Constants.SKYBOX_QUALITY_FACTOR);
		/* textures */
		skyBox.addTexture(SkyBox.Face.East,  R.drawable.skybox_right,  "east_texture");
		skyBox.addTexture(SkyBox.Face.South, R.drawable.skybox_center, "south_texture");
		skyBox.addTexture(SkyBox.Face.West,  R.drawable.skybox_left,  "west_texture");
		skyBox.addTexture(SkyBox.Face.Up,    R.drawable.skybox_up,    "up_texture");
		skyBox.addTexture(SkyBox.Face.Down,  R.drawable.skybox_down,  "down_texture");
		return skyBox;
	}

	public void setCenter(Point center) {
		this.object.position().x = this.skyBox.position().x = center.x;
		this.object.position().y = this.skyBox.position().y = center.y;
		
	}
	
}
