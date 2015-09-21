package com.aklopp.imageviewer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Using the JSON data provided, create an application that will display a list of images.
 * Created by Allison Klopp on 20/09/15.
 */
public class MainActivity extends ActionBarActivity {
    public static final String APP_NAME = "Image Viewer";
    private static final String DATA_FILE_NAME = "data.json";
    private static final String CORRECTED_DATA_FILE_NAME = "corrected_data.json";

    private ImageGridFragment mImageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(APP_NAME);
        setContentView(R.layout.activity_main);

        mImageFragment = (ImageGridFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id)
        {
            case R.id.action_data:
                mImageFragment.setFileName(DATA_FILE_NAME);
                return true;
            case R.id.action_data_corrected:
                mImageFragment.setFileName(CORRECTED_DATA_FILE_NAME);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
