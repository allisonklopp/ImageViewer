package com.aklopp.imageviewer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Adapter for the grid displaying the images
 * Created by Allison Klopp on 20/09/15.
 */
public class GridAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private RelativeLayout mFocusedView;

    /**
     * Constructor
     * @param context
     * @param layoutResourceId
     * @param focusedView
     */
    public GridAdapter(Context context, int layoutResourceId, RelativeLayout focusedView) {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        mFocusedView = focusedView;
    }

    /**
     * View holder pattern
     */
    static class ViewHolderItem {
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            viewHolder = new ViewHolderItem();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }
        final Bitmap currentItem = (Bitmap)getItem(position);

        if(null != currentItem) {
            viewHolder.imageView.setImageBitmap(currentItem);
            viewHolder.imageView.setTag(currentItem.getGenerationId());
        }

        // If user clicks on this item in the grid, show a larger image.
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImageView)mFocusedView.findViewById(R.id.focused_item)).setImageBitmap(currentItem);
                mFocusedView.setVisibility(View.VISIBLE);

                mFocusedView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFocusedView.setVisibility(View.INVISIBLE); // tap on the overlay to close it
                    }
                });
            }
        });

        return convertView;
    }
}
