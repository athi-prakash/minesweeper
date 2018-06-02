package com.example.sstto.myapplication;

/**
 * Adapter to handle the grid activities.
 * Initializes and populates cell values (images)
 * Created by sstto on 15-10-2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;


public class CustomGridAdapter extends BaseAdapter {
    public String[] items;
    LayoutInflater inflater;
    Map<String, Integer> image = new HashMap<String, Integer>();
    private Context context;

    /**
     * Constructor to initializes grid
     * Create a map variable of images to be used in the cells
     */
    public CustomGridAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        image.put("", R.mipmap.i_x);
        image.put("0", R.mipmap.i_0);
        image.put("1", R.mipmap.i_1);
        image.put("2", R.mipmap.i_2);
        image.put("3", R.mipmap.i_3);
        image.put("4", R.mipmap.i_4);
        image.put("5", R.mipmap.i_5);
        image.put("6", R.mipmap.i_6);
        image.put("7", R.mipmap.i_7);
        image.put("8", R.mipmap.i_8);
        image.put("b", R.mipmap.i_b);
        image.put("f", R.mipmap.i_f);
        image.put("e", R.mipmap.i_e);
    }

    /**
     * Returns length of grid
     */
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Populate the cell with respective image in map
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null & position < items.length) {
            convertView = inflater.inflate(R.layout.cell, null);
        }
        ImageView iv = (ImageView) convertView.findViewById(R.id.imageview);
        iv.setImageResource(image.get(items[position]));
        return convertView;
    }
}
