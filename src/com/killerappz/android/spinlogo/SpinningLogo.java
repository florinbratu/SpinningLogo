package com.killerappz.android.spinlogo;

import javax.microedition.khronos.opengles.GL10;

import min3d.Utils;
import min3d.core.Object3dContainer;
import min3d.core.Renderer;
import min3d.core.Scene;
import min3d.core.TextureManager;
import min3d.objectPrimitives.SkyBox;
import min3d.vos.TextureVo;
import android.content.Context;
import android.graphics.Bitmap;

import com.killerappz.android.spinlogo.context.Point;
import com.killerappz.android.spinlogo.context.SpinLogoContext;

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
	// for resources
	private final Context context;
	// for textures
	private final TextureManager textureManager;
	
	// rotSpeed is the rotation speed measured in ROTATION_SPEED_UNIT
	public SpinningLogo(Context context, TextureManager tm, String resId, SpinLogoContext contextInfo, Scene scene) {
		this.context = context;
		this.textureManager = tm;
		object = new ObjLoader(context,tm).load(resId);
		this.contextInfo = contextInfo;
 		this.scene = scene;
 		this.skyBox = createSkyBox();
	}
	
	public void draw(GL10 gl, Renderer renderer){
		// pre-draw: check for texture changes
		if(contextInfo.dirtyLogoTexture()) {
			updateLogoTexture(contextInfo.getLogoTextureName());
		}
		
		// draw all objects within the container
		scene.addChild(object);
		scene.addChild(skyBox);
		renderer.onDrawFrame(gl);
		
		// post-draw: rotate logo
		autoRotate();
	}

	private void updateLogoTexture(String logoTextureName) {
		
		// find res for the new texture
		int texId = context.getResources().getIdentifier(
				Constants.TEXTURES_LOCATION + logoTextureName,
				null, context.getPackageName());
		// load up the new texture
		Bitmap b = Utils.makeBitmapFromResourceId( context, texId);
		this.textureManager.addTextureId(b, logoTextureName, false);
		b.recycle();
		
		// link the new texture to the logo
		object.getChildAt(0).textures().addReplace(new TextureVo(logoTextureName));
	}

	private void autoRotate() {
		float rotationSpeed = (float)contextInfo.getRotationSpeed() * Constants.ROTATION_SPEED_UNIT;
		object.rotation().y += rotationSpeed;
	}
	
	private SkyBox createSkyBox() {
		SkyBox skyBox = new SkyBox(context, textureManager, Constants.SKYBOX_SIZE, Constants.SKYBOX_QUALITY_FACTOR);
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
