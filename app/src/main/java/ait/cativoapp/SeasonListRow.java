package ait.cativoapp;

/**
 * Created by Cliver on 19/09/2017.
 */

public class SeasonListRow
{
    private String id;
    private String number;
    private String episodesQty;

    public SeasonListRow(String id, String number, String episodesQty)
    {
        this.id = id;
        this.number = number;
        this.episodesQty = episodesQty;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getEpisodesQty()
    {
        return episodesQty;
    }

    public void setEpisodesQty(String episodesQty)
    {
        this.episodesQty = episodesQty;
    }
}
