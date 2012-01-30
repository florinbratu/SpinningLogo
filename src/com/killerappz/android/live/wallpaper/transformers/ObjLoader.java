package com.killerappz.android.live.wallpaper.transformers;

import min3d.core.Object3dContainer;
import min3d.core.TextureManager;
import min3d.parser.IParser;
import min3d.parser.Parser;
import android.content.Context;

/**
 * Loads models from a .obj file using the min3d utilities
 *
 */
public class ObjLoader {
	
	private final Context ctx;
	private final TextureManager tm;

	public ObjLoader(Context ctx, TextureManager tm) {
		this.ctx = ctx;
		this.tm = tm;
	}
	
	/**
	 * Load the model from the project Resources
	 */
	public Object3dContainer load(String resId) {
		IParser parser = Parser.createParser(Parser.Type.OBJ,
				ctx, tm, resId, true);
		parser.parse();

		return parser.getParsedObject();

	}
	
}
