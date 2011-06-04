package com.killerappz.android.spinlogo.context;

// a Point with floating-point precision
public class Point {
	
	public float x;
	public float y;

	public Point(){
		x = y = 0;
	}
	
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		Point po = (Point)o;
		return po.x == this.x && po.y == this.y;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

}
