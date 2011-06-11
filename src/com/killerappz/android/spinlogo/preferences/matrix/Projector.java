/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.killerappz.android.spinlogo.preferences.matrix;

import javax.microedition.khronos.opengles.GL10;

import com.killerappz.android.spinlogo.context.Rectangle;

import android.opengl.Matrix;

/**
 * A utility that projects
 *
 */
public class Projector {
    public Projector() {
        mMVP = new float[16];
        mV = new float[4];
        mGrabber = new MatrixGrabber();
    }

    public void setCurrentView(int x, int y, int width, int height) {
        mX = x;
        mY = y;
        mViewWidth = width;
        mViewHeight = height;
    }

    public void project(float[] obj, int objOffset, float[] win, int winOffset) {
        if (!mMVPComputed) {
            Matrix.multiplyMM(mMVP, 0, mGrabber.mProjection, 0, mGrabber.mModelView, 0);
            mMVPComputed = true;
        }

        Matrix.multiplyMV(mV, 0, mMVP, 0, obj, objOffset);

        float rw = 1.0f / mV[3];

        win[winOffset] = mX + mViewWidth * (mV[0] * rw + 1.0f) * 0.5f;
        win[winOffset + 1] = mY + mViewHeight * (mV[1] * rw + 1.0f) * 0.5f;
        win[winOffset + 2] = (mV[2] * rw + 1.0f) * 0.5f;
    }
   
    public void unproject(float[] win, int winOffset, float[] obj, int objOffset) {
    	
    	if (!mMVPComputed) {
            Matrix.multiplyMM(mMVP, 0, mGrabber.mProjection, 0, mGrabber.mModelView, 0);
            mMVPComputed = true;
        }
    	
    	float[] invertedMatrix = new float[16];
    	Matrix.invertM(invertedMatrix, 0, mMVP, 0);
    	
    	float[] normalizedInPoint = new float[4];
    	
    	normalizedInPoint[0] = ((win[winOffset] - mX) * 2.0f / mViewWidth - 1.0f);
    	normalizedInPoint[1] = ((mViewHeight - win[winOffset + 1]) * 2.0f / mViewHeight - 1.0f);
    	normalizedInPoint[2] = 2 * win[winOffset + 2] - 1.0f;
    	normalizedInPoint[3] = norm(normalizedInPoint); 
    		   
    	Matrix.multiplyMV(obj, objOffset, invertedMatrix, 0, normalizedInPoint, 0);
    	
    }

    private float norm(float[] win) {
		return (float)Math.sqrt(win[0] * win[0] + win[1] * win[1] + win[2] * win[2]);
	}

	/**
     * Get the current projection matrix. Has the side-effect of
     * setting current matrix mode to GL_PROJECTION
     * @param gl
     */
    public void getCurrentProjection(GL10 gl) {
        mGrabber.getCurrentProjection(gl);
        mMVPComputed = false;
    }

    /**
     * Get the current model view matrix. Has the side-effect of
     * setting current matrix mode to GL_MODELVIEW
     * @param gl
     */
    public void getCurrentModelView(GL10 gl) { 
        mGrabber.getCurrentModelView(gl);
        mMVPComputed = false;
    }
    
    public void getRectModelView(GL10 gl, Rectangle targetPlane) { 
        mGrabber.getRectangleModelView(gl, targetPlane);
        mMVPComputed = false;
    }
    
    @Override
    public String toString() {
    	return mGrabber.toString() + " width:" + mViewWidth + " height:" +mViewHeight;
    }
    
    private MatrixGrabber mGrabber;
    private boolean mMVPComputed;
    private float[] mMVP;
    private float[] mV;
    private int mX;
    private int mY;
    private int mViewWidth;
    private int mViewHeight;
}
