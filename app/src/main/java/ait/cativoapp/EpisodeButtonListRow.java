package ait.cativoapp;

import android.graphics.Bitmap;

/**
 * Created by Cliver on 19/09/2017.
 */

public class EpisodeButtonListRow
{
    private String id="";
    private String number="";
    private String seasonId="";
    private String runtime="";

    public EpisodeButtonListRow(String id, String number, String seasonId, String runtime)
    {
        this.id = id;
        this.number = number;
        this.seasonId = seasonId;
        this.runtime = runtime;
    }

    public EpisodeButtonListRow(String number, String seasonId)
    {
        this.number = number;
        this.seasonId = seasonId;
    }

    public String getId()
    {
        return id;
    }

    public String getNumber()
    {
        return number;
    }

    public String getSeasonId()
    {
        return seasonId;
    }

    public String getRuntime()
    {
        return runtime;
    }
}
