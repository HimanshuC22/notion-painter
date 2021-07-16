package com.example.iitinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InterestGridAdapter extends BaseAdapter {

    public Context mContext;
    public String[] interestName;
    public int[] ImageID;

    public InterestGridAdapter(Context context, String[] name, int[] image)
    {
        this.mContext = context;
        this.interestName = name;
        this.ImageID = image;
    }

    @Override
    public int getCount() {
        return interestName.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItem;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null) {
            gridItem = new View(mContext);
            gridItem = inflater.inflate(R.layout.recyclerview_item_row, null);
            TextView mTextView = gridItem.findViewById(R.id.mainText);
            ImageView mImageView = gridItem.findViewById(R.id.mainImage);
            mTextView.setText(interestName[position]);
            mImageView.setImageResource(R.drawable.movies);
        } else {
            gridItem = convertView;
        }

        return gridItem;
    }

}
