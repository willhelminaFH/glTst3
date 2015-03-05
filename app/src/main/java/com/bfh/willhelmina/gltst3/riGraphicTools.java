package com.bfh.willhelmina.gltst3;

import android.opengl.GLES20;

public class riGraphicTools {

	// Program variables
	public static int sp_SolidColor;
	public static int sp_Image;
	
	
	/* SHADER Solid
	 * 
	 * This shader is for rendering a colored primitive.
	 * 
	 */
	public static final String vs_SolidColor =
		"uniform 	mat4 		uMVPMatrix;" +
		"attribute 	vec4 		vPosition;" +
	    "void main() {" +
	    "  gl_Position = uMVPMatrix * vPosition;" +
	    "}";
	
	public static final String fs_SolidColor =
		"precision mediump float;" +
	    "void main() {" +
	    "  gl_FragColor = vec4(0.5,0,0,1);" +
	    "}"; 
	
	/* SHADER Image
	 * 
	 * This shader is for rendering 2D images straight from a texture
	 * No additional effects.
	 * 
	 */
	public static final String vs_Image =
		"uniform mat4 uMVPMatrix;" +
        //"uniform float[6] uMod;"+//not used

        "uniform vec3 a0Mod;" +
        "uniform vec3 a1Mod;" +
		"attribute vec4 vPosition;" +
		"attribute vec2 a_texCoord;" +

        //"in float[6] modIn;" +
        //"varying float[6] cMod;" +//"flat varying float[6] cMod;" +//"out float[6] cMod;" +//
		"varying vec2 v_texCoord;" +

        "varying vec2 imgOff0;"+// = v_texCoord+vec2(0,1.0/3.0);"+//these should get moved to the vertex shader and declaired like: varying vec2...
        "varying vec2 imgOff1;"+// = v_texCoord+vec2(0,2.0/3.0);"+
        "varying vec2 imgOff2;"+// = v_texCoord+vec2(1/2,0);"+
        "varying vec2 imgOff3;"+// = v_texCoord+vec2(1/2,1.0/3.0);"+
        "varying vec2 imgOff4;"+// = v_texCoord+vec2(1/2,2.0/3.0);"+

        "varying vec3 b0Mod;" +
        "varying vec3 b1Mod;" +

	    "void main() {" +
        //"  cMod = modIn;"+
        /*
        "  cMod[0] = a0Mod[0];"+
        "  cMod[1] = a0Mod[1];"+
        "  cMod[2] = a0Mod[2];"+
        "  cMod[3] = a1Mod[0];"+
        "  cMod[4] = a1Mod[1];"+
        "  cMod[5] = a1Mod[2];"+
        */
	    "  gl_Position = uMVPMatrix * vPosition;" +
	    "  v_texCoord = a_texCoord;" +
        "  imgOff0 = v_texCoord+vec2(0,1.0/3.0);"+//these should get moved to the vertex shader and declaired like: varying vec2...
        "  imgOff1 = v_texCoord+vec2(0,2.0/3.0);"+
        "  imgOff2 = v_texCoord+vec2(1/2,0);"+
        "  imgOff3 = v_texCoord+vec2(1/2,1.0/3.0);"+
        "  imgOff4 = v_texCoord+vec2(1/2,2.0/3.0);"+

        "  b0Mod = a0Mod;"+
        "  b1Mod = a1Mod;"+
	    "}";
	
	public static final String fs_Image =
		"precision mediump float;" +
	    "varying vec2 v_texCoord;" +

        "varying vec2 imgOff0;"+
        "varying vec2 imgOff1;"+
        "varying vec2 imgOff2;"+
        "varying vec2 imgOff3;"+
        "varying vec2 imgOff4;"+
        "uniform sampler2D s_texture;" +

        "varying vec3 b0Mod;" +
        "varying vec3 b1Mod;" +

        //"varying float[6] cMod;"+//"flat varying float[6] cMod;"+//"in float[6] cMod;" +

        /*
        "float cMod0;"+
        "float cMod1;"+
        "float cMod2;"+
        "float cMod3;"+
        "float cMod4;"+
        "float cMod5;"+
        */

        //"float rMod;"+

        //"vec4 rCol;"+

        "vec4 tCol0 = texture2D( s_texture, v_texCoord );"+
        "vec4 tCol1 = texture2D( s_texture, imgOff0);"+
        "vec4 tCol2 = texture2D( s_texture, imgOff1);"+
        "vec4 tCol3 = texture2D( s_texture, imgOff2);"+
        "vec4 tCol4 = texture2D( s_texture, imgOff3);"+
        "vec4 tCol5 = texture2D( s_texture, imgOff4);"+

	    "void main() {" +
             /*
             //test code
                //"cMod = {1.0,0,0,0,0,0};"+
                "cMod[0] = 1.0;"+
                "cMod[1] = 0.0;"+
                "cMod[2] = 0.0;"+
                "cMod[3] = 0.0;"+
                "cMod[4] = 0.0;"+
                "cMod[5] = 0.0;"+
             //end test
        "    rMod = 1/(cMod[0]+cMod[1]+cMod[2]+cMod[3]+cMod[4]+cMod[5]);"+
            "cMod[0] = 1.0;"+
            "cMod[1] = 0.0;"+
            "cMod[2] = 0.0;"+
            "cMod[3] = 0.0;"+
            "cMod[4] = 0.0;"+
            "cMod[5] = 0.0;"+
        "    rCol = tCol0*rMod+tCol1*rMod+tCol2*rMod+tCol3*rMod+tCol4*rMod+tCol5*rMod;"+
        "    rCol = tCol0*cMod[0]+tCol1*cMod[1]+tCol2*cMod[2]+tCol3*cMod[3]+tCol4*cMod[4]+tCol5*cMod[5];"+
        "    gl_FragColor = tCol0*cMod[0]"+         //tCol0*rMod;" //"    gl_FragColor = rCol;" +//"    gl_FragColor = texture2D( s_texture, v_texCoord );" +//"    gl_FragColor = texture2D( s_texture, v_texCoord );" +//"    gl_FragColor = texture2D( s_texture, imgOff0 );" +//
                            "+tCol1*cMod[1]"+
                            "+tCol2*cMod[2]"+
                            "+tCol3*cMod[3]"+
                            "+tCol4*cMod[4]"+
                            "+tCol5*cMod[5];"+
                            */

        "    gl_FragColor = tCol0*b0Mod[0]"+         //tCol0*rMod;" //"    gl_FragColor = rCol;" +//"    gl_FragColor = texture2D( s_texture, v_texCoord );" +//"    gl_FragColor = texture2D( s_texture, v_texCoord );" +//"    gl_FragColor = texture2D( s_texture, imgOff0 );" +//"    gl_FragColor = (tCol0*b0Mod[0])"+ //
        "    +tCol1*b0Mod[1]"+//"    *(tCol1*b0Mod[1])"+//
        "    +tCol2*b0Mod[2]"+//"    *(tCol2*b0Mod[2])"+//
        "    +tCol3*b1Mod[0]"+//"    *(tCol3*b1Mod[0])"+//
        "    +tCol4*b1Mod[1]"+//"    *(tCol4*b1Mod[1])"+//
        "    +tCol5*b1Mod[2];"+//"    *(tCol5*b1Mod[2]);"+//


        //"   gl_FragColor = tCol0;"+//texture2D( s_texture, v_texCoord );"+////"    gl_FragColor = tCol0*cMod[0];"+//
        "}";
	
	
	
	public static int loadShader(int type, String shaderCode){

	    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
	    int shader = GLES20.glCreateShader(type);

	    // add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);
	    
	    // return the shader
	    return shader;
	}
}
