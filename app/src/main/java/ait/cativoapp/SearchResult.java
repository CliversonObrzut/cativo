package ait.cativoapp;

import android.graphics.Bitmap;

/**
 * Created by Ana&Cliver on 07/09/2017.
 */

public class SearchResult
{
    private String id;
    private String name;
    private String status;
    private String imageUrl;
    private String rate;
    private String country;
    private Bitmap imageBitmap = null;

    public SearchResult(String id, String name, String rate, String country, String status, String imageUrl)
    {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.country = country;
        this.status = status;
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

    public String getCountry()
    {
        return country;
    }

    public String getStatus()
    {
        return status;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public String getRate()
    {
        return rate;
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
