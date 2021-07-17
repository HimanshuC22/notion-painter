package com.example.iitinder.profile;

import android.graphics.Bitmap;

/**
 * Class for the data of the ExpandableListView to show interests
 */

public class DataModel {
    String name, url;
    Bitmap bitmap;
    public DataModel(Bitmap bitmap, String name, String url)
    {
        this.bitmap = bitmap;
        this.name = name;
        this.url = url;
    }
    public Bitmap getBitmap()
    {
        return bitmap;
    }
    public String getImageName()
    {
        return name;
    }
    public String getURL()
    {
        return url;
    }
}
