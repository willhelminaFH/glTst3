package com.bfh.willhelmina.gltst3;

import android.opengl.GLSurfaceView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

//should change this to use AppCompatActivity
public class MainActivity extends ActionBarActivity {

    private GLSurfaceView glSurfaceView;
    static int sWidth;
    static int sHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        // set to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //get metrics
        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);
        sWidth = d.widthPixels;
        sHeight = d.heightPixels;

        // make surface view
        glSurfaceView = new GLSurf(this);

        //set view
        setContentView(glSurfaceView);
    }

    //Action bar stuff, pointless at the moment but leaving it in for later
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // nothing in the menu but leaving it for the FUTURE
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }


    //transmit pause/resume
    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }
}
