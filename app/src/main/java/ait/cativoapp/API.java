package ait.cativoapp;

/**
 * Created by Cliver on 09/09/2017.
 */

public class API
{
    private String apiRoot = "http://api.tvmaze.com";

    private String apiSearch = "/search/shows?q=";
    private String apiSingleSearch = "/singlesearch/shows?q=";
    private String apiShowById = "/shows/";
    private String apiShowSeason = "/seasons";
    private String apiShowEpisodes = "/episodes";
    private String apiSeasonEpisodes = "/seasons/";


    public String Search(String query)
    {
        return apiRoot + apiSearch + query;
    }

    public String SingleSearch(String query)
    {
        return apiRoot + apiSingleSearch + query;
    }

    public String getShowById(String id)
    {
        return apiRoot + apiShowById + id;
    }

    public String getShowSeason(String id)
    {
        return apiRoot + apiShowById + id + apiShowSeason;
    }

    public String getShowEpisodes(String id)
    {
        return apiRoot + apiShowById + id + apiShowEpisodes;
    }

    public String getSeasonEpisodes(String seasonId)
    {
        return apiRoot + apiSeasonEpisodes + seasonId + apiShowEpisodes;
    }
}
