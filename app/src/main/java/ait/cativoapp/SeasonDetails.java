package ait.cativoapp;

import android.graphics.Bitmap;

/**
 * Created by Cliver on 19/09/2017.
 */

public class SeasonDetails
{
    private String id;
    private String number;
    private String serieId;
    private String serieName;
    private String episodesQty;
    private String imageUrl;
    private String startDate;
    private String endDate;
    private Bitmap imageBitmap = null;
    private boolean isWatched = false;

    public SeasonDetails(String id, String number, String serieId, String serieName, String episodesQty, String imageUrl, String startDate, String endDate)
    {
        this.id = id;
        this.number = number;
        this.serieId = serieId;
        this.serieName = serieName;
        this.episodesQty = episodesQty;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId()
    {
        return id;
    }

    public String getNumber()
    {
        return number;
    }

    public String getSerieId()
    {
        return serieId;
    }

    public String getSerieName()
    {
        return serieName;
    }

    public String getEpisodesQty()
    {
        return episodesQty;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public Bitmap getImageBitmap()
    {
        return imageBitmap;
    }

    public boolean isWatched()
    {
        return isWatched;
    }

    public void setEpisodesQty(String episodesQty)
    {
        this.episodesQty = episodesQty;
    }

    public void setImageBitmap(Bitmap imageBitmap)
    {
        this.imageBitmap = imageBitmap;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public void setWatched(boolean watched)
    {
        isWatched = watched;
    }
}
