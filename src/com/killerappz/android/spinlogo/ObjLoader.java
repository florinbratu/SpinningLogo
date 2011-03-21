package com.killerappz.android.spinlogo;

import min3d.core.Object3dContainer;
import min3d.parser.IParser;
import min3d.parser.Parser;
import android.content.Context;

/**
 * Loads models from a .obj file using the min3d utilities
 *
 */
public class ObjLoader {
	
	private final Context ctx;

	public ObjLoader(Context ctx) {
		this.ctx = ctx;
	}
	
	/**
	 * Load the model from the project Resources
	 */
	public Object3dContainer load(String resId) {
		IParser parser = Parser.createParser(Parser.Type.OBJ,
				ctx, resId, true);
		parser.parse();

		return parser.getParsedObject();

	}
	
}
