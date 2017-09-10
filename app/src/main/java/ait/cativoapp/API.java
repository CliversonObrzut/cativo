package ait.cativoapp;

/**
 * Created by Ana&Cliver on 09/09/2017.
 */

public class API
{
    private String apiRroot = "http://api.tvmaze.com";

    private String apiSearch = "/search/shows?q=:";
    private String apiSingleSearch = "/singlesearch/shows?q=:";

    public String Search(String query)
    {
        return apiRroot + apiSearch + query;
    }

    public String SingleSearch(String query)
    {
        return apiRroot + apiSingleSearch + query;
    }
}
