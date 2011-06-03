package com.killerappz.android.spinlogo.context;

import min3d.objectPrimitives.SkyBox.Face;

import com.killerappz.android.spinlogo.preferences.matrix.Projector;

/**
 * A Rectangle and some attached ops
 * @author florin
 *
 */
public class Rectangle {

	private final Point upperLeft;
	private final Point lowerRight;
	private float z;
	
	public Rectangle( float upperLeftX, float upperLeftY, 
			float lowerRightX, float lowerRightY, float z ) {
		this.upperLeft = new Point(upperLeftX, upperLeftY);
		this.lowerRight = new Point(lowerRightX, lowerRightY);
		this.z = z;
	}
	
	public Rectangle projection( Projector projektor ) {
		float[] upperLeftCoords = new float[] { upperLeft.x, upperLeft.y, z, 1.0f};
		float[] upperLeftWin = new float[3];
		projektor.project(upperLeftCoords, 0, upperLeftWin, 0);
		
		float[] lowerRightCoords = new float[] { lowerRight.x, lowerRight.y, z, 1.0f};
		float[] lowerRightWin = new float[3];
		projektor.project(lowerRightCoords, 0, lowerRightWin, 0);
		
		return new Rectangle(upperLeftWin[0], upperLeftWin[1], 
				lowerRightWin[0], lowerRightWin[1], z);
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
	
}
