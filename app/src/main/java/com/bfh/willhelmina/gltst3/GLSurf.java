package com.bfh.willhelmina.gltst3;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GLSurf extends GLSurfaceView {

    private final GLRenderer mRenderer;
    int mScreenWidth;
    int mScreenHeight;

    public GLSurf(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        mScreenWidth = MainActivity.sWidth;
        mScreenHeight = MainActivity.sHeight;

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new GLRenderer(context);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    // touch in
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        System.out.println("touch captured");
        mRenderer.processTouch(e);
        return true;
    }

    // pause/resume
    @Override
    public void onPause() {
        super.onPause();
        mRenderer.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRenderer.onResume();
    }

}