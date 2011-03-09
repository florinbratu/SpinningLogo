package net.markguerra.android.glwallpaperexample;

import android.content.res.Resources;
import min3d.core.Object3dContainer;
import min3d.parser.IParser;
import min3d.parser.Parser;

/**
 * Loads models from a .obj file
 * using the min3d utilities
 *
 */
public class ObjLoader {

	public ObjLoader() {
	}
	
	/**
	 * Load the model from the project Resources
	 */
	public Object3dContainer load(Resources resources, String resId) {
		IParser parser = Parser.createParser(Parser.Type.OBJ,
				resources, resId, true);
		parser.parse();

		return parser.getParsedObject();

	}
	
}
