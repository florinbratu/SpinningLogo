package com.killerappz.android.spinlogo.preferences.matrix;

import javax.microedition.khronos.opengles.GL10;

public class MatrixGrabber {
    public MatrixGrabber() {
        mModelView = new float[16];
        mProjection = new float[16];
    }

    /**
     * Record the current modelView and projection matrix state.
     * Has the side effect of setting the current matrix state to GL_MODELVIEW
     * @param gl
     */
    public void getCurrentState(GL10 gl) {
        getCurrentProjection(gl);
        getCurrentModelView(gl);
    }

    /**
     * Record the current modelView matrix state. Has the side effect of
     * setting the current matrix state to GL_MODELVIEW
     * @param gl
     */
    public void getCurrentModelView(GL10 gl) {
        getMatrix(gl, GL10.GL_MODELVIEW, mModelView);
    }

    /**
     * Record the current projection matrix state. Has the side effect of
     * setting the current matrix state to GL_PROJECTION
     * @param gl
     */
    public void getCurrentProjection(GL10 gl) {
        getMatrix(gl, GL10.GL_PROJECTION, mProjection);
    }

    private void getMatrix(GL10 gl, int mode, float[] mat) {
        MatrixTrackingGL gl2 = (MatrixTrackingGL) gl;
        gl2.glMatrixMode(mode);
        gl2.getMatrix(mat, 0);
    }
    
    @Override
    public String toString() {
    	String projectionMatrix = "[" 
    		+ mProjection[0] + "," + mProjection[1] + "," + mProjection[2] + "," + mProjection[3] + "|"
    		+ mProjection[4] + "," + mProjection[5] + "," + mProjection[6] + "," + mProjection[7] + "|"
    		+ mProjection[8] + "," + mProjection[9] + "," + mProjection[10] + "," + mProjection[11] + "|"
    		+ mProjection[12] + "," + mProjection[13] + "," + mProjection[14] + "," + mProjection[15] + "]";
    	
    	String modelViewMatrix = "[" 
    		+ mModelView[0] + "," + mModelView[1] + "," +  mModelView[2] + "," + mModelView[3] + "|"
    		+ mModelView[4] + "," + mModelView[5] + "," + mModelView[6] + "," + mModelView[7] + "|"
    		+ mModelView[8] + "," + mModelView[9] + "," + mModelView[10] + "," + mModelView[11] + "|"
    		+ mModelView[12] + "," + mModelView[13] + "," + mModelView[14] + "," + mModelView[15] + "]";
    	
    	return "Projection matrix: " +  projectionMatrix + "\nModelview matrix: " + modelViewMatrix;
    }

    public float[] mModelView;
    public float[] mProjection;
}