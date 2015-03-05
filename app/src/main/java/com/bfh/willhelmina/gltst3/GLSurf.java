package com.bfh.willhelmina.gltst3;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GLSurf extends GLSurfaceView {

	private final GLRenderer mRenderer;
	int mScreenWidth;
    int mScreenHeight;
    //int curImg = 0;
    float curVal, xR, yR, vars[];
	public GLSurf(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        mScreenWidth = MainActivity.sWidth;
        mScreenHeight = MainActivity.sHeight;
        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new GLRenderer(context);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        vars = new float[]{1.0f, 0, 0, 0, 0, 0};
        //mRenderer.pushMotion(vars);
    }

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

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        //there are gonna be problems with multitouch here
        //easily sorted out with getHistorical though
        //0,0 = upper left
        //R 1,1 = lower right
        xR = e.getX()/mScreenWidth;
        yR = e.getY()/mScreenHeight;

        //set image val, this should be imediately visable
        /*
        if((xR >= 5f/6f)&&(yR<=5f/6f)){
            //this the 1/x bullshit is wrong, creates an exponential function... kinda cool but wrong
            curVal = 1 - (yR + yR*(1f/6f));// 1/(yR + yR*(1f/6f));//I think this is the right way to adjust for the offset *DRUNK MATH*
            if (curVal>1){
                curVal = 1;
            }
            System.out.println("*CUR X,Y:*"+ xR+" "+yR);
            System.out.println("DRUNK VALS:"+curVal);
            vars[curImg] = curVal;
            //since we can only actually manipulate one variable at a time sending the whole array
            //seems a little senseless
            //that statement is preposterous every time a variable changes the enter output array
            //needs to change;
            float rMod = 1/(vars[0]+vars[1]+vars[2]+vars[3]+vars[4]+vars[5]);
            float rVars[] = new float[6];
            for(int i = 0; i<vars.length;i++){
                rVars[i] = vars[i]*rMod;
            }
            mRenderer.pushMotion(rVars);
            return true;
        }
        */

        //set active image and scaler val
        if(xR >= 5f/6f){//&&yR>5f/6f){
            /*
            if(curImg!=5){
                curImg = 5;
                System.out.println("*CUR X,Y:*"+ xR+" "+yR);
                System.out.println("*DRUNK IND:*"+ curImg);
            }
            */
            curVal = 1 - (yR*2);//(yR + yR*(1f/6f));
            if (curVal>1){
                curVal = 1;
            }
            if (curVal<-1){
                curVal = -1;
            }
            //curImg = 5;//don't really need this as a variable, only used to set index
            vars[5] = curVal;
            float rMod = 1/(vars[0]+vars[1]+vars[2]+vars[3]+vars[4]+vars[5]);//(vars[0]*vars[1]*vars[2]*vars[3]*vars[4]*vars[5]);//
            float rVars[] = new float[6];
            for(int i = 0; i<vars.length;i++){
                rVars[i] = vars[i]*rMod;
            }
            System.out.println("\n***5 "+curVal+"***\n");
            mRenderer.pushMotion(rVars);
            return true;
        }
        if(xR >= 4f/6f){//&&yR>5f/6f){
            /*
            if(curImg!=4) {
                curImg = 4;
                System.out.println("*CUR X,Y:*" + xR + " " + yR);
                System.out.println("*DRUNK IND:*" + curImg);
            }
            */
            curVal = 1 - (yR*2);//(yR + yR*(1f/6f));
            if (curVal>1){
                curVal = 1;
            }
            if (curVal<-1){
                curVal = -1;
            }
            vars[4] = curVal;
            float rMod = 1/(vars[0]+vars[1]+vars[2]+vars[3]+vars[4]+vars[5]);
            float rVars[] = new float[6];
            for(int i = 0; i<vars.length;i++){
                rVars[i] = vars[i]*rMod;
            }
            System.out.println("\n***4 "+curVal+"***\n");
            mRenderer.pushMotion(rVars);
            return true;
        }
        if(xR >= 3f/6f){//&&yR>5f/6f){
            /*
            if(curImg!=3) {
                curImg = 3;
                System.out.println("*CUR X,Y:*" + xR + " " + yR);
                System.out.println("*DRUNK IND:*" + curImg);
            }
            */
            curVal = 1 - (yR*2);//(yR + yR*(1f/6f));
            if (curVal>1){
                curVal = 1;
            }
            if (curVal<-1){
                curVal = -1;
            }
            vars[3] = curVal;
            float rMod = 1/(vars[0]+vars[1]+vars[2]+vars[3]+vars[4]+vars[5]);
            float rVars[] = new float[6];
            for(int i = 0; i<vars.length;i++){
                rVars[i] = vars[i]*rMod;
            }
            System.out.println("\n***3 "+curVal+"***\n");
            mRenderer.pushMotion(rVars);
            return true;
        }
        if(xR >= 2f/6f){//&&yR>5f/6f) {
            /*
            if(curImg!=2) {
                curImg = 2;
                System.out.println("*CUR X,Y:*" + xR + " " + yR);
                System.out.println("*DRUNK IND:*" + curImg);
            }
            */
            curVal = 1 - (yR*2);//(yR + yR*(1f/6f));
            if (curVal>1){
                curVal = 1;
            }
            if (curVal<-1){
                curVal = -1;
            }
            vars[2] = curVal;
            float rMod = 1/(vars[0]+vars[1]+vars[2]+vars[3]+vars[4]+vars[5]);
            float rVars[] = new float[6];
            for(int i = 0; i<vars.length;i++){
                rVars[i] = vars[i]*rMod;
            }
            System.out.println("\n***2 "+curVal+"***\n");
            mRenderer.pushMotion(rVars);
            return true;
        }
        if(xR >= 1f/6f){//&&yR>5f/6f){
            /*
            if(curImg!=1) {
                curImg = 1;
                System.out.println("*CUR X,Y:*" + xR + " " + yR);
                System.out.println("*DRUNK IND:*" + curImg);
            }
            */
            curVal = 1 - (yR*2);//(yR + yR*(1f/6f));
            if (curVal>1){
                curVal = 1;
            }
            if (curVal<-1){
                curVal = -1;
            }
            vars[1] = curVal;
            float rMod = 1/(vars[0]+vars[1]+vars[2]+vars[3]+vars[4]+vars[5]);
            float rVars[] = new float[6];
            for(int i = 0; i<vars.length;i++){
                rVars[i] = vars[i]*rMod;
            }
            System.out.println("\n***1 "+curVal+"***\n");
            mRenderer.pushMotion(rVars);
            return true;
        }
        if(xR < 1f/6f){//&&yR>5f/6f){
            /*
            if(curImg!=0) {
                curImg = 0;
                System.out.println("*CUR X,Y:*" + xR + " " + yR);
                System.out.println("*DRUNK IND:*" + curImg);
            }
            */
            curVal = 1 - (yR*2);//(yR + yR*(1f/6f));
            if (curVal>1){
                curVal = 1;
            }
            if (curVal<-1){
                curVal = -1;
            }
            vars[0] = curVal;
            float rMod = 1/(vars[0]+vars[1]+vars[2]+vars[3]+vars[4]+vars[5]);
            float rVars[] = new float[6];
            for(int i = 0; i<vars.length;i++){
                rVars[i] = vars[i]*rMod;
            }
            System.out.println("\n***0 "+curVal+"***\n");
            mRenderer.pushMotion(rVars);
            return true;
        }


        System.out.println("***OUT OF BOUNDS***");
        System.out.println(e.getX()+" "+e.getY());
        System.out.println(xR+" "+yR);
        return true;
    }

}
