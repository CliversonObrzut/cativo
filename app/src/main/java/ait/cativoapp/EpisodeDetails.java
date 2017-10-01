package ait.cativoapp;

import android.graphics.Bitmap;

/**
 * Created by Cliver on 19/09/2017.
 */

public class EpisodeDetails
{
    private String id;
    private String number;
    private String serieId;
    private String seasonNumber;
    private String imageUrl;
    private Bitmap imageBitmap = null;
    private String title;
    private String airDate;
    private String airTime;
    private String runtime;
    private String summary;
    private boolean isWatched = false;

    public EpisodeDetails(String id, String number, String serieId, String seasonNumber, String imageUrl, String title, String airDate, String airTime, String runtime, String summary)
    {
        this.id = id;
        this.number = number;
        this.serieId = serieId;
        this.seasonNumber = seasonNumber;
        this.imageUrl = imageUrl;
        this.title = title;
        this.airDate = airDate;
        this.airTime = airTime;
        this.runtime = runtime;
        this.summary = summary;
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

    public String getSeasonNumber()
    {
        return seasonNumber;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public Bitmap getImageBitmap()
    {
        return imageBitmap;
    }

    public String getTitle()
    {
        return title;
    }

    public String getAirDate()
    {
        return airDate;
    }

    public String getAirTime()
    {
        return airTime;
    }

    public String getRuntime()
    {
        return runtime;
    }

    public String getSummary()
    {
        return summary;
    }

    public boolean isWatched()
    {
        return isWatched;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public void setImageBitmap(Bitmap imageBitmap)
    {
        this.imageBitmap = imageBitmap;
    }

    public void setWatched(boolean watched)
    {
        isWatched = watched;
    }
}
