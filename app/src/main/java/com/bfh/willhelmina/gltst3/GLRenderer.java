package com.bfh.willhelmina.gltst3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class GLRenderer implements Renderer {

	// projection/view matrices
	private final float[] mtrxProjection = new float[16];
	private final float[] mtrxView = new float[16];
	private final float[] mtrxProjectionAndView = new float[16];
	
	// Geometric variables
	public static float vertices[];
	public static short indices[];
	public static float uvs[];
	public FloatBuffer vertexBuffer;
	public ShortBuffer drawListBuffer;
	public FloatBuffer uvBuffer;


	// screen resolution
	float	mScreenWidth;// = 1280;
	float	mScreenHeight;// = 768;


    //motion
    float[] tValsA = new float[]{1/6,1/6,1/6,1/6,1/6,1/6};       // stores motion values for images
    float[] tValsB = new float[]{1/6,1/6,1/6,1/6,1/6,1/6};       // stores modifies for images
    float[] m0Vals = new float[]{1/6,1/6,1/6};  //vec3f for GLSL
    float[] m1Vals = new float[]{1/6,1/6,1/6};  //vec3f for GLSL
    public FloatBuffer tfBuff;                  //stores float to buffer

	// Misc
	Context mContext; //
	long mLastTime;

    public Rect image;
    int fCnt;


	
	public GLRenderer(Context c)
	{
		mContext = c;
		mLastTime = System.currentTimeMillis() + 100;
        ByteBuffer bb = ByteBuffer.allocateDirect(m0Vals.length * 4);
        bb.order(ByteOrder.nativeOrder());
        tfBuff = bb.asFloatBuffer();
        tfBuff.put(m0Vals);
        tfBuff.position(0);
	}
	
	public void onPause()
	{
		/* Do stuff to pause the renderer */
	}
	
	public void onResume()
	{
		/* Do stuff to resume the renderer */
		mLastTime = System.currentTimeMillis();
	}
	
	@Override
	public void onDrawFrame(GL10 unused) {

        //
		
		// Get the current time
    	long now = System.currentTimeMillis();
    	
    	// We should make sure we are valid and sane
    	if (mLastTime > now) return;
        
    	// Get the amount of time the last frame took.
    	long elapsed = now - mLastTime;
		
		// Update our example
		
		// Render our example
		Render(mtrxProjectionAndView);
		
		// Save the current time to see how long it took :).
        mLastTime = now;

        fCnt++;
		
	}
	
	private void Render(float[] m) {
		
		// clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        int mModAccess0 = GLES20.glGetUniformLocation(riGraphicTools.sp_Image,
                                                      "a0Mod"//"modIn"
        );

        GLES20.glUniform3f(mModAccess0, tValsB[0],tValsB[1],tValsB[2]);

        int mModAccess1 = GLES20.glGetUniformLocation(riGraphicTools.sp_Image,
                "a1Mod"//"modIn"
        );

        GLES20.glUniform3f(mModAccess1, tValsB[3],tValsB[4],tValsB[5]);

        // get handle to vertex shader's vPosition member
	    int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "vPosition");

	    // Enable generic vertex attribute array
	    GLES20.glEnableVertexAttribArray(mPositionHandle);

	    // Prepare the triangle coordinate data
	    GLES20.glVertexAttribPointer(mPositionHandle, 3,
	                                 GLES20.GL_FLOAT, false,
	                                 0, vertexBuffer);
	    
	    // Get handle to texture coordinates location
	    int mTexCoordLoc = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "a_texCoord" );
	    
	    // Enable generic vertex attribute array
	    GLES20.glEnableVertexAttribArray ( mTexCoordLoc );
	    
	    // Prepare the texturecoordinates
	    GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false, 
                0, uvBuffer);
	    
	    // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_Image, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);
        
        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (riGraphicTools.sp_Image, "s_texture" );
        
        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i ( mSamplerLoc, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
        	
	}
	

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		// We need to know the current width and height.
		mScreenWidth = width;
		mScreenHeight = height;
		
		// Redo the Viewport, making it fullscreen.
		GLES20.glViewport(0, 0, (int)mScreenWidth, (int)mScreenHeight);
		
		// Clear our matrices
	    for(int i=0;i<16;i++)
	    {
	    	mtrxProjection[i] = 0.0f;
	    	mtrxView[i] = 0.0f;
	    	mtrxProjectionAndView[i] = 0.0f;
	    }
	    
	    // Setup our screen width and height for normal sprite translation.
	    Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);
	    
	    // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        mScreenWidth = MainActivity.sWidth;//1280;//
        mScreenHeight = MainActivity.sHeight;//768;//

        System.out.println("****BITCHES"+mScreenWidth+" "+ mScreenHeight+"BITCHES****");

		// Create the triangles
		SetupTriangle();
		// Create the image information
		SetupImage();
		
		// Set the clear color to black
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

	    // Create the shaders, solid color
	    int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_Image);
	    int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_Image);

	    riGraphicTools.sp_Image = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(riGraphicTools.sp_Image, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(riGraphicTools.sp_Image, fragmentShader); // add the fragment shader to program
	    GLES20.glLinkProgram(riGraphicTools.sp_Image);                  // creates OpenGL ES program executables
	    
	    
	    // Set our shader programm
		GLES20.glUseProgram(riGraphicTools.sp_Image);
        System.out.println("***surface setup complete***");
	}
	
	public void SetupImage(){
        int ind = 0;

        //setup uvs
        uvs = new float[6*4*2];
        for(float i = 0; i<3; i++){
            for (float j = 0; j<2; j++) {
                //uv coords
                uvs[ind * 8]     = j/2.0f;
                uvs[ind * 8 + 1] = i/3.0f;
                uvs[ind * 8 + 2] = j/2.0f;
                uvs[ind * 8 + 3] = (i+1.0f)/3.0f;
                uvs[ind * 8 + 4] = (j+1.0f)/2.0f;
                uvs[ind * 8 + 5] = (i+1.0f)/3.0f;
                uvs[ind * 8 + 6] = (j+1.0f)/2.0f;
                uvs[ind * 8 + 7] = i/3.0f;
                ind++;
            }
        }

		// The texture buffer
		ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
		bb.order(ByteOrder.nativeOrder());
		uvBuffer = bb.asFloatBuffer();
		uvBuffer.put(uvs);
		uvBuffer.position(0);
		
		// Gen textures, only using one at the moment
		int[] texturenames = new int[1];
		GLES20.glGenTextures(1, texturenames, 0);
		
		// Retrieve our image from resources.
		int id = mContext.getResources().getIdentifier("drawable/atlas0", null, mContext.getPackageName());
		System.out.println("**bmp id: "+id+"**");

		// Temporary create a bitmap
		Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);
        //System.out.println("**bmp id: "+bmp.toString()+"**");

		// Bind texture to texturename
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);
		
		// Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
        
        // We are done using the bitmap so we should recycle it.
		bmp.recycle();

	}
	
	public void SetupTriangle()
	{
		// We have to create the vertices of our triangle.
		vertices = new float[]
		           {0.0f, mScreenWidth, 0.0f,
					0.0f, 100f, 0.0f,
                    mScreenWidth, 100f, 0.0f,
                    mScreenWidth, mScreenWidth, 0.0f,
		           };
		
		indices = new short[] {0, 1, 2, 0, 2, 3}; // The order of vertexrendering.

		// The vertex buffer.
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		// initialize byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(indices);
		drawListBuffer.position(0);
	}

    void processTouch(MotionEvent e){

        int curImgInd = (int) (e.getX()/mScreenWidth * 5);
        float curVal = 1 - e.getY()/mScreenHeight * 2;
        float nCoef = 0;
        float rMod = 0;

        //assign normalized y value to the image corresponding to the x
        tValsA[curImgInd] = curVal;

        //modulate all image values and assign them to the temp array
        for (int i = 0; i< tValsA.length; i++){
            rMod = ((float)(fCnt*i)%100f)/100f;
            tValsB[i] = tValsA[i]*rMod;
            nCoef += tValsB[i];
        }
        nCoef = 1/nCoef;
        for (int i = 0; i< tValsB.length; i ++){
            tValsB[i] = tValsB[i]*nCoef;
        }


    }

    //convenience method for making native buffers
    FloatBuffer toFloatBuff(float[] array){

        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        tfBuff = bb.asFloatBuffer();
        tfBuff.put(array);
        tfBuff.position(0);

        return tfBuff;
    }

}

