package ait.cativoapp;

import android.graphics.Bitmap;

/**
 * Created by Cliver on 21/09/2017.
 */

public class ShowDetails
{
    private String id;
    private String name;
    private String status;
    private String network;
    private String premiered;
    private String genre;
    private String language;
    private String seasons;
    private String officialSite;
    private String summary;
    private String rate;
    private String country;
    private String imageUrl;
    private Bitmap imageBitmap = null;
    private boolean isFavorite = false;

    public ShowDetails(String id, String name, String rate, String network, String premiered, String genre, String language, String seasons, String officialSite, String summary, String country, String status, String imageUrl)
    {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.network = network;
        this.premiered = premiered;
        this.genre = genre;
        this.language = language;
        this.seasons = seasons;
        this.officialSite = officialSite;
        this.summary = summary;
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

    public String getNetwork()
    {
        return network;
    }

    public String getPremiered()
    {
        return premiered;
    }

    public String getGenre()
    {
        return genre;
    }

    public String getLanguage()
    {
        return language;
    }

    public String getSeasons()
    {
        return seasons;
    }

    public String getOfficialSite()
    {
        return officialSite;
    }

    public String getSummary()
    {
        return summary;
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

    public boolean isFavorite()
    {
        return isFavorite;
    }

    public void setFavorite(boolean favorite)
    {
        isFavorite = favorite;
    }
}
