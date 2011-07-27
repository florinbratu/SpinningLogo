package com.killerappz.android.lwp.donation.bat.context;

public class OffsetInfo {

	public float xOffset;
	public float yOffset;
    public float xStep;
    public float yStep;
    public int xPixels;
    public int yPixels;
	
    public void set(float xOffset, float yOffset, float xStep, float yStep,
			int xPixels, int yPixels) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.xStep = xStep;
		this.yStep = yStep;
		this.xPixels = xPixels;
		this.yPixels = yPixels;
	}
    
    
}
