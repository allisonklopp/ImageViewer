package com.aklopp.imageviewer;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class to load the images onto the grid asynchronously.
 * Created by Allison Klopp on 20/09/15.
 */
public class PopulateGridAsyncTask extends AsyncTask<String, String, Void> {
    private static final String TAG = "PopulatePinsAsyncTask";

    // Tags to parse JSON
    private static final String ACTION_TAG = "action";
    private static final String DATA_TAG = "data";
    private static final String IMAGES_TAG = "image";
    private static final String BLANK_STRING = "";

    private Context mContext;
    private final GridAdapter mGridAdapter;

    /**
     * Constructor
     * @param context
     * @param adapter
     */
    public PopulateGridAsyncTask(Context context, GridAdapter adapter)
    {
        this.mContext = context;
        mGridAdapter = adapter;
    }

    @Override
    protected Void doInBackground(String... files) {
        String result =  extractFileContentsAsString(files[0]);

        if(!result.equals(BLANK_STRING)) {
            // Parse JSON data
            try {
                JSONObject jsonObject = new JSONObject(result);
                // Yet unused action string
                String action = jsonObject.getString(ACTION_TAG);

                // The array containing the image items
                JSONArray data = jsonObject.getJSONArray(DATA_TAG);
                ;
                if (data.length() == 0) {
                    showErrorToast("No image URLS found", "Empty or malformed JSON.");
                } else {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject image = data.getJSONObject(i);
                        String imageURL = image.getString(IMAGES_TAG);

                        addToGrid(imageURL);
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSONException: " + e);
                showErrorToast("Invalid JSON encountered", e.getMessage());
            }
        }
        return null;
    }

    /**
     * Add the designated image to the grid.
     * @param imageURL, the URL path to the image
     */
    private void addToGrid(final String imageURL) {
        final Bitmap image = getImage(imageURL);

        if (null != image) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mGridAdapter.add(image);
                }
            });
        }
    }

    /**
     * Get the image from this URL.
     * @param url
     * @return bitmap image
     */
    private Bitmap getImage(String url)
    {
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            showErrorToast("Problem encountered reading URL",url);

            Log.e(TAG, "Error getting image for URL (" + url + ") : " + e.getMessage());

            // Set to an error image if couldn't get bitmap from url
            image = ((BitmapDrawable) mContext.getResources().getDrawable(R.mipmap.ic_empty)).getBitmap();
        }
        return image;
    }

    /**
     * Pull the contents from file as a string
     * @param filename
     * @return fileContents as a string
     */
    public String extractFileContentsAsString(String filename) {
        AssetManager assetManager = mContext.getAssets();

        InputStream input;
        try {
            input = assetManager.open(filename);

            // Get number of readable bytes
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            // byte buffer into a string
            return new String(buffer);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            showErrorToast("Problem encountered reading from file", e.getMessage());
        }
        // If something when wrong when reading, return a blank string
        return BLANK_STRING;
    }

    /**
     * Display a toast to the user if anything goes wrong with parsing.
     */
    private void showErrorToast(final String errorDescription, final String stackTrace)
    {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, errorDescription + "\n" + stackTrace, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
