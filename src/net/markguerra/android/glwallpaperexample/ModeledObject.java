package net.markguerra.android.glwallpaperexample;

import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import min3d.Shared;
import min3d.core.FacesBufferedList;
import min3d.core.Object3d;
import min3d.core.Object3dContainer;
import min3d.core.RenderCaps;
import min3d.core.Scene;
import min3d.vos.RenderType;
import min3d.vos.TextureVo;
import android.content.res.Resources;

/**
 * A single modeled object via an obj file
 * 
 */
public class ModeledObject {

	private final Object3dContainer object;
	// says if the object is to be drawn within a light-aware environment. true by default
	private final Scene scene;
	private IntBuffer scratchIntBuffer;
	
	public ModeledObject(Resources resources, String resId, Scene scene) {
		object = new ObjLoader().load(resources, resId);
		this.scene = scene;
		this.scratchIntBuffer = IntBuffer.allocate(4);
	}
	
	public void draw(GL10 gl){
		// draw all objects within the container
		/*scene.addChild(object);
		Shared.renderer().onDrawFrame(gl);*/
		for(int i = 0 ; i < object.numChildren(); i++) {
			Object3d obj = object.getChildAt(i);
			drawObject(gl, obj);
		}
	}

	private void drawObject(GL10 gl, Object3d obj) {
		// Normals
		if (obj.hasNormals() && obj.normalsEnabled()) {
			obj.vertices().normals().buffer().position(0);
			gl.glNormalPointer(GL10.GL_FLOAT, 0, obj.vertices().normals().buffer());
			gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		}
		else {
			gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		}

		// lighting
		boolean useLighting = (scene.lightingEnabled() && obj.hasNormals() && obj.normalsEnabled() && obj.lightingEnabled());
		if (useLighting) {
			gl.glEnable(GL10.GL_LIGHTING);
		} else {
			gl.glDisable(GL10.GL_LIGHTING);
		}
		
		// Shademodel
		gl.glGetIntegerv(GL11.GL_SHADE_MODEL, scratchIntBuffer);
		if (obj.shadeModel().glConstant() != scratchIntBuffer.get(0)) {
			gl.glShadeModel(obj.shadeModel().glConstant());
		}
		
		// Colors: either per-vertex, or per-object
		if (obj.hasVertexColors() && obj.vertexColorsEnabled()) {
			obj.vertices().colors().buffer().position(0);
			gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, obj.vertices().colors().buffer());
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY); 
		}
		else {
			gl.glColor4f(
				(float)obj.defaultColor().r / 255f, 
				(float)obj.defaultColor().g / 255f, 
				(float)obj.defaultColor().b / 255f, 
				(float)obj.defaultColor().a / 255f
			);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}
		
		// Colormaterial
		gl.glGetIntegerv(GL10.GL_COLOR_MATERIAL, scratchIntBuffer);
		boolean scratchB = (scratchIntBuffer.get(0) != 0);
		if (obj.colorMaterialEnabled() != scratchB) {
			if (obj.colorMaterialEnabled())
				gl.glEnable(GL10.GL_COLOR_MATERIAL);
			else
				gl.glDisable(GL10.GL_COLOR_MATERIAL);
		}
		
		// Point size
		if (obj.renderType() == RenderType.POINTS) 
		{
			if (obj.pointSmoothing()) 
				gl.glEnable(GL10.GL_POINT_SMOOTH);
			else
				gl.glDisable(GL10.GL_POINT_SMOOTH);
			gl.glPointSize(obj.pointSize());
		}

		// Line properties
		if (obj.renderType() == RenderType.LINES || obj.renderType() == RenderType.LINE_STRIP || obj.renderType() == RenderType.LINE_LOOP) 
		{
			if ( obj.lineSmoothing() == true) {
				gl.glEnable(GL10.GL_LINE_SMOOTH);
			}
			else {
				gl.glDisable(GL10.GL_LINE_SMOOTH);
			}
			gl.glLineWidth(obj.lineWidth());
		}

		// Backface culling 
		if (obj.doubleSidedEnabled()) {
		    gl.glDisable(GL10.GL_CULL_FACE);
		} 
		else {
		    gl.glEnable(GL10.GL_CULL_FACE);
		}
		
		// textures
		drawTextures(gl, obj);
		
		// Matrix operations in modelview
		gl.glPushMatrix();
		
		gl.glTranslatef(obj.position().x, obj.position().y, obj.position().z);
		
		gl.glRotatef(obj.rotation().x, 1,0,0);
		gl.glRotatef(obj.rotation().y, 0,1,0);
		gl.glRotatef(obj.rotation().z, 0,0,1);
		
		gl.glScalef(obj.scale().x, obj.scale().y, obj.scale().z);
		
		// Draw
		obj.vertices().points().buffer().position(0);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, obj.vertices().points().buffer());

		if (! obj.ignoreFaces())
		{
			int pos, len;
			
			if (! obj.faces().renderSubsetEnabled()) {
				pos = 0;
				len = obj.faces().size();
			}
			else {
				pos = obj.faces().renderSubsetStartIndex() * FacesBufferedList.PROPERTIES_PER_ELEMENT;
				len = obj.faces().renderSubsetLength();
			}

			obj.faces().buffer().position(pos);

			gl.glDrawElements(
					obj.renderType().glValue(),
					len * FacesBufferedList.PROPERTIES_PER_ELEMENT, 
					GL10.GL_UNSIGNED_SHORT, 
					obj.faces().buffer());
		}
		else
		{
			gl.glDrawArrays(obj.renderType().glValue(), 0, obj.vertices().size());
		}
		
		//
		// Recurse on children
		//
		
		if (obj instanceof Object3dContainer)
		{
			Object3dContainer container = (Object3dContainer)obj;
			
			for (int i = 0; i < container.children().size(); i++)
			{
				Object3d o = container.children().get(i);
				drawObject(gl,o);
			}
		}
		
		// Restore matrix
		gl.glPopMatrix();

	}

	private void drawTextures(GL10 gl, Object3d obj) {
		
		// iterate thru object's textures
		for (int i = 0; i < RenderCaps.maxTextureUnits(); i++)
		{
			gl.glActiveTexture(GL10.GL_TEXTURE0 + i);
			gl.glClientActiveTexture(GL10.GL_TEXTURE0 + i); 

			if (obj.hasUvs() && obj.texturesEnabled())
			{
				obj.vertices().uvs().buffer().position(0);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, obj.vertices().uvs().buffer());

				TextureVo textureVo = ((i < obj.textures().size())) ? textureVo = obj.textures().get(i) : null;

				if (textureVo != null)
				{
					// activate texture
					int glId = Shared.textureManager().getGlTextureId(textureVo.textureId);
					gl.glBindTexture(GL10.GL_TEXTURE_2D, glId);
				    gl.glEnable(GL10.GL_TEXTURE_2D);
					gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

					int minFilterType = Shared.textureManager().hasMipMap(textureVo.textureId) ? GL10.GL_LINEAR_MIPMAP_NEAREST : GL10.GL_NEAREST; 
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilterType);
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR); // (OpenGL default)
					
					// do texture environment settings
					for (int j = 0; j < textureVo.textureEnvs.size(); j++)
					{
						gl.glTexEnvx(GL10.GL_TEXTURE_ENV, textureVo.textureEnvs.get(j).pname, textureVo.textureEnvs.get(j).param);
					}
					
					// texture wrapping settings
					gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, (textureVo.repeatU ? GL10.GL_REPEAT : GL10.GL_CLAMP_TO_EDGE));
					gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, (textureVo.repeatV ? GL10.GL_REPEAT : GL10.GL_CLAMP_TO_EDGE));		

					// texture offset, if any
					if (textureVo.offsetU != 0 || textureVo.offsetV != 0)
					{
						gl.glMatrixMode(GL10.GL_TEXTURE);
						gl.glLoadIdentity();
						gl.glTranslatef(textureVo.offsetU, textureVo.offsetV, 0);
						gl.glMatrixMode(GL10.GL_MODELVIEW); // .. restore matrixmode
					}
				}
				else
				{
					gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
				    gl.glDisable(GL10.GL_TEXTURE_2D);
					gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				}
			}
			else
			{
				gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
			    gl.glDisable(GL10.GL_TEXTURE_2D);
				gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			}
		}
	}
	
}
