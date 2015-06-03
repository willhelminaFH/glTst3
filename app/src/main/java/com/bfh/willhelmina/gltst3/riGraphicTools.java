package com.bfh.willhelmina.gltst3;

import android.opengl.GLES20;

public class riGraphicTools {

	// Program
	public static int sp_Image;

    //vertex shader
	public static final String vs_Image =
		"uniform mat4 uMVPMatrix;" +

        "uniform vec3 a0Mod;" +
        "uniform vec3 a1Mod;" +
		"attribute vec4 vPosition;" +
		"attribute vec2 a_texCoord;" +
		"varying vec2 v_texCoord;" +

        "varying vec2 imgOff0;"+
        "varying vec2 imgOff1;"+
        "varying vec2 imgOff2;"+
        "varying vec2 imgOff3;"+
        "varying vec2 imgOff4;"+

        "varying vec3 b0Mod;" +
        "varying vec3 b1Mod;" +

	    "void main() {" +

	    "  gl_Position = uMVPMatrix * vPosition;" +
	    "  v_texCoord = a_texCoord;" +
        "  imgOff0 = v_texCoord+vec2(0,1.0/3.0);"+
        "  imgOff1 = v_texCoord+vec2(0,2.0/3.0);"+
        "  imgOff2 = v_texCoord+vec2(1/2,0);"+
        "  imgOff3 = v_texCoord+vec2(1/2,1.0/3.0);"+
        "  imgOff4 = v_texCoord+vec2(1/2,2.0/3.0);"+

        "  b0Mod = a0Mod;"+
        "  b1Mod = a1Mod;"+
	    "}";

    //fragment shader
	public static final String fs_Image =
		"precision highp float;" +
	    "varying vec2 v_texCoord;" +

        "varying vec2 imgOff0;"+
        "varying vec2 imgOff1;"+
        "varying vec2 imgOff2;"+
        "varying vec2 imgOff3;"+
        "varying vec2 imgOff4;"+
        "uniform sampler2D s_texture;" +

        "varying vec3 b0Mod;" +
        "varying vec3 b1Mod;" +

        "vec4 tCol0 = texture2D( s_texture, v_texCoord );"+
        "vec4 tCol1 = texture2D( s_texture, imgOff0);"+
        "vec4 tCol2 = texture2D( s_texture, imgOff1);"+
        "vec4 tCol3 = texture2D( s_texture, imgOff2);"+
        "vec4 tCol4 = texture2D( s_texture, imgOff3);"+
        "vec4 tCol5 = texture2D( s_texture, imgOff4);"+

	    "void main() {" +
        "    gl_FragColor = tCol0*b0Mod[0]"+
        "                   +tCol1*b0Mod[1]"+
        "                   +tCol2*b0Mod[2]"+
        "                   +tCol3*b1Mod[0]"+
        "                   +tCol4*b1Mod[1]"+
        "                   +tCol5*b1Mod[2];"+
        "}";
	
	
	
	public static int loadShader(int type, String shaderCode){
        //util method for compiling shaders

	    int shader = GLES20.glCreateShader(type);

	    // add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);
	    
	    // return the shader
	    return shader;
	}
}
