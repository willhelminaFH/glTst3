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

	// Our matrices
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


	// Our screenresolution
	float	mScreenWidth;// = 1280;
	float	mScreenHeight;// = 768;

	// Misc
	Context mContext;
	long mLastTime;
	int mProgram;
    float mVals[] = {1.0f,0,0,0,0,0};
    public Rect image;
	
	public GLRenderer(Context c)
	{
		mContext = c;
		mLastTime = System.currentTimeMillis() + 100;
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
		
	}
	
	private void Render(float[] m) {
		
		// clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
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
	    int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_SolidColor);
	    int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_SolidColor);

	    riGraphicTools.sp_SolidColor = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(riGraphicTools.sp_SolidColor, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(riGraphicTools.sp_SolidColor, fragmentShader); // add the fragment shader to program
	    GLES20.glLinkProgram(riGraphicTools.sp_SolidColor);                  // creates OpenGL ES program executables
	    
	    // Create the shaders, images
	    vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_Image);
	    fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_Image);

	    riGraphicTools.sp_Image = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(riGraphicTools.sp_Image, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(riGraphicTools.sp_Image, fragmentShader); // add the fragment shader to program
	    GLES20.glLinkProgram(riGraphicTools.sp_Image);                  // creates OpenGL ES program executables
	    
	    
	    // Set our shader programm
		GLES20.glUseProgram(riGraphicTools.sp_Image);
	}
	
	public void SetupImage()
	{
		// Create our UV coordinates.
        /*leaving this in, I maybe can just use 1 uv map since I'm always rendering to the same spot
        *
        * nm fundamentally flawed assumption.  the UVs represent where on the texture to map
        * ie. {1/2, 1/3}
        *
        * actually not sure if this is even correct any more, may still only need one uv since I've
        * only got one surface...
		uvs = new float[] {
				0.0f, 0.0f,
				0.0f, 1.0f,
				1.0f, 1.0f,			
				1.0f, 0.0f			
	    };
		*/

        //setup uvs, 6 images * 4 verts * (u,v)
        int ind = 0;
        uvs = new float[6*4*2];
        for(float i = 0; i<3; i++){
            for (float j = 0; j<2; j++) {
                //uv coords
                uvs[ind * 8 + 0] = j/2.0f;//0.0f;
                uvs[ind * 8 + 1] = i/3.0f;//0.0f;
                uvs[ind * 8 + 2] = j/2.0f;
                uvs[ind * 8 + 3] = (i+1.0f)/3.0f;
                uvs[ind * 8 + 4] = (j+1.0f)/2.0f;
                uvs[ind * 8 + 5] = (i+1.0f)/3.0f;
                uvs[ind * 8 + 6] = (j+1.0f)/2.0f;
                uvs[ind * 8 + 7] = i/3.0f;//0.0f;
                ind++;
            }
        }

		// The texture buffer
		ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
		bb.order(ByteOrder.nativeOrder());
		uvBuffer = bb.asFloatBuffer();
		uvBuffer.put(uvs);
		uvBuffer.position(0);
		
		// Generate Textures, if more needed, alter these numbers.
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


    void processTouchEvent(MotionEvent e){
        //0,0 = upper left
        //R 1,1 = lower right
        int curImg;
        float curVal, xR, yR;
        xR = e.getX()/mScreenWidth;
        yR = e.getY()/mScreenHeight;

        //set image val, this should be imediately visable
        if(xR >= 5/6&&yR<=5/6){
            curVal = yR + yR*(1/6);//I think this is the right way to adjust for the offset *DRUNK MATH*
            System.out.println("*CUR X,Y:*"+ xR+" "+yR);
            System.out.println("DRUNK VALS:"+curVal);
            return;
        }
        
        //set active image
        if(xR >= 5/6&&yR>5/6){
            curImg = 5;
            System.out.println("*CUR X,Y:*"+ xR+" "+yR);
            System.out.println("*DRUNK IND:*"+ curImg);
            return;
        }
        if(xR >= 4/6&&yR>5/6){
            curImg = 4;
            System.out.println("*CUR X,Y:*"+ xR+" "+yR);
            System.out.println("*DRUNK IND:*"+ curImg);
            return;
        }
        if(xR >= 3/6&&yR>5/6){
            curImg = 3;
            System.out.println("*CUR X,Y:*"+ xR+" "+yR);
            System.out.println("*DRUNK IND:*"+ curImg);
            return;
        }
        if(xR >= 2/6&&yR>5/6) {
            curImg = 2;
            System.out.println("*CUR X,Y:*"+ xR+" "+yR);
            System.out.println("*DRUNK IND:*"+ curImg);
            return;
        }
        if(xR >= 1/6&&yR>5/6){
            curImg = 1;
            System.out.println("*CUR X,Y:*"+ xR+" "+yR);
            System.out.println("*DRUNK IND:*"+ curImg);
            return;
        }
        if(xR < 1/6&&yR>5/6){
            curImg = 0;
            System.out.println("*CUR X,Y:*"+ xR+" "+yR);
            System.out.println("*DRUNK IND:*"+ curImg);
            return;
        }

        System.out.println(e.getX()+" "+e.getY());
        System.out.println(xR+" "+yR);

    }
}
