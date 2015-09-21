package com.aklopp.imageviewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;


/**
 * Fragment to load images on a grid and display to user.
 * Created by Allison Klopp on 20/09/15.
 */
public class ImageGridFragment extends Fragment {
    // The file name of the data file
    private static final String DEFAULT_FILE_NAME = "data.json";
    private String mFileName = DEFAULT_FILE_NAME;
    private GridAdapter mGridAdapter;

    /**
     * Constructor
     */
    public ImageGridFragment() {
        // intentionally blank
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        RelativeLayout focusedView = (RelativeLayout) rootView.findViewById(R.id.focused_view);
        GridView imageGrid = (GridView) rootView.findViewById(R.id.image_grid);

        mGridAdapter = new GridAdapter(getActivity(),R.layout.grid_item, focusedView);
        imageGrid.setAdapter(mGridAdapter);

        initGrid();

        return rootView;
    }

    /**
     * Overwrite the current filename and reload the grid data.
     * @param fileName the new file name
     */
    public void setFileName(String fileName)
    {
        mFileName = fileName;
        initGrid();
    }

    /**
     * Populate the grid with images
     */
    public void initGrid()
    {
        // reset the grid
        mGridAdapter.clear();

        // Load the images onto the grid
        new PopulateGridAsyncTask(getActivity(), mGridAdapter).execute(mFileName);
    }
}
