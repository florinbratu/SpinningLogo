package com.killerappz.android.spinlogo.context;

import min3d.objectPrimitives.SkyBox.Face;

import android.util.Log;

import com.killerappz.android.spinlogo.Constants;
import com.killerappz.android.spinlogo.preferences.matrix.Projector;

/**
 * A Rectangle and some attached ops
 * @author florin
 *
 */
public class Rectangle {

	private final Point upperLeft;
	private final Point lowerRight;
	public float z;
	
	public Rectangle( float upperLeftX, float upperLeftY, 
			float lowerRightX, float lowerRightY, float z ) {
		this.upperLeft = new Point(upperLeftX, upperLeftY);
		this.lowerRight = new Point(lowerRightX, lowerRightY);
		this.z = z;
	}
	
	/**
	 * Get the screen coordinates for this rectangle.
	 * Assumes(not checked!) that this rectangle is in world coords 
	 */
	public Rectangle toScreenCoords( Projector projektor ) {
		float[] upperLeftCoords = new float[] { upperLeft.x, upperLeft.y, z, 1.0f};
		float[] upperLeftWin = new float[3];
		projektor.project(upperLeftCoords, 0, upperLeftWin, 0);
		Log.d(Constants.LOG_TAG, "upper left z coord" + upperLeftWin[2]);
		
		float[] lowerRightCoords = new float[] { lowerRight.x, lowerRight.y, z, 1.0f};
		float[] lowerRightWin = new float[3];
		projektor.project(lowerRightCoords, 0, lowerRightWin, 0);
		Log.d(Constants.LOG_TAG, "lower right z coord" + lowerRightWin[2]);
		// assert(upperLeftWin[2] == lowerRightWin[2])
		
		return new Rectangle(upperLeftWin[0], upperLeftWin[1], 
				lowerRightWin[0], lowerRightWin[1], upperLeftWin[2]);
	}
	
	/**
	 * Assess the position of the testPoint
	 * against this Rectangle, knowing that 
	 * the Center point is inside the Rectangle
	 */
	public Face position(Point testPoint, Point center) {
		// generate the other two points of the Rectangle
		Point lowerLeft = new Point(upperLeft.x, lowerRight.y);
		Point upperRight = new Point(lowerRight.x, upperLeft.y);
		
		// placement tests
		if( dotproduct(upperLeft, lowerLeft, center) 
				* dotproduct(upperLeft, lowerLeft, testPoint) < 0)
			return Face.West;
		if( dotproduct(upperRight, lowerRight, center) 
				* dotproduct(upperRight, lowerRight, testPoint) < 0)
			return Face.East;
		if( dotproduct(upperLeft, upperRight, center) 
				* dotproduct(upperLeft, upperRight, testPoint) < 0)
			return Face.Up;
		if( dotproduct(lowerLeft, lowerRight, center) 
				* dotproduct(lowerLeft, lowerRight, testPoint) < 0)
			return Face.West;
		
		return Face.South;
	}

	/**
	 * Computes sign(vector(OR) * vector(OT))
	 */
	private double dotproduct(Point O, Point R, Point T) {
		return Math.signum( ((double)R.x - O.x) * ((double)T.x - O.x)
			+ ((double)R.y - O.y) * ((double)T.y - O.y));
	}
	
	@Override
	public String toString() {
		return "Upper Left corner: " + upperLeft + "; Lower Right corner: " + lowerRight;
	}
	
}
