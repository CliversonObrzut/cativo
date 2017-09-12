package ait.cativoapp;

import android.graphics.Bitmap;

/**
 * Created by Cliver on 12/09/2017.
 */

public class MySeries
{
    private String id;
    private String name;
    private String imageUrl;
    private Bitmap imageBitmap = null;

    public MySeries(String id, String name, String imageUrl)
    {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public Bitmap getImageBitmap()
    {
        return imageBitmap;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public void setImageBitmap(Bitmap imageBitmap)
    {
        this.imageBitmap = imageBitmap;
    }

}
