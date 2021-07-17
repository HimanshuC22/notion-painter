package com.example.iitinder.profile;

import android.graphics.Bitmap;

/**
 * Class to define an image and its properties
 */

public class ImageData {
    String name, url;
    Bitmap bitmap;
    public ImageData(String name, String URL, Bitmap bitmap)
    {
        this.name = name;
        this.url = URL;
        this.bitmap = bitmap;
    }

    public String getImageName()
    {
        return name;
    }

    public String getImageURL()
    {
        return url;
    }

    public Bitmap getImageBitmap()
    {
        return bitmap;
    }

}
