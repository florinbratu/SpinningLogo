package com.killerappz.android.live.wallpaper.x.men.context;


// a Point where the user touched the screen
// small trick. when calling a set, we assume it was from a touch
// when calling a get, we assume it is drawing code, so set touch to false
// in order to prepare for the next touch event!
public class TouchPoint extends Point {

	private boolean touched;
	
	public TouchPoint() {
		super();
		this.touched = false;
	}

	public boolean isTouched() {
		return touched;
	}

	public void setTouched(boolean touched) {
		this.touched = touched;
	}
	
	@Override
	public void set(float x, float y) {
		super.set(x, y);
		this.touched = true;
	}
	
	public Point get() {
		this.touched = false;
		return this;
	}
	
}
