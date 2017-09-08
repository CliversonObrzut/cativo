package ait.cativoapp;

import android.graphics.Bitmap;

/**
 * Created by Ana&Cliver on 07/09/2017.
 */

public class SearchResult
{
    private int id;
    private String name;
    private String status;
    private String imageUrl;
    private double rate;
    private Bitmap imageBitmap;

    public SearchResult(int id, String name, String status, String imageUrl, double rate)
    {
        this.id = id;
        this.name = name;
        this.status = status;
        this.imageUrl = imageUrl;
        this.rate = rate;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getStatus()
    {
        return status;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public double getRate()
    {
        return rate;
    }

    public Bitmap getImageBitmap()
    {
        return imageBitmap;
    }
}
