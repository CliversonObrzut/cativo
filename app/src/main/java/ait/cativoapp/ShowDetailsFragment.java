package ait.cativoapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class ShowDetailsFragment extends Fragment
{

    View view;
    ProgressBar showDetailsProgressBar;
    LinearLayout watchedProgressBarLayout;
    ProgressBar watchedProgressBar;
    TextView watchedProgressPercentage;
    ScrollView showDetailsScrollView;
    LinearLayout seasonListLayout;
    ArrayList<SeasonListRow> seasonList;
    BottomNavigationView bottomNav;

    TextView sId;
    TextView sName;
    ImageButton sFavIcon;
    ImageView sImage;
    TextView sNetwork;
    TextView sCountry;
    TextView sPremiered;
    TextView sGenre;
    TextView sLanguage;
    TextView sSeasons;
    TextView sStatus;
    TextView sRating;
    TextView sOfficialSite;
    TextView sSummary;

    public static ShowDetailsFragment newInstance()
    {
        ShowDetailsFragment fragment = new ShowDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(null);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.title_show_details);
        if(view == null)
        {
            view = inflater.inflate(R.layout.fragment_show_details, container, false);

            showDetailsProgressBar = view.findViewById(R.id.progressBar_search_details);
            watchedProgressBarLayout = view.findViewById(R.id.show_details_watchedPBLayout);
            watchedProgressBar = view.findViewById(R.id.show_details_watchedProgressBar);
            watchedProgressPercentage = view.findViewById(R.id.show_details_watchedPercentage);
            showDetailsScrollView = view.findViewById(R.id.show_details_ScrollLayout);
            seasonListLayout = view.findViewById(R.id.show_details_seasonsList);
            bottomNav = getActivity().findViewById(R.id.navigation);
            bottomNav.setVisibility(View.GONE);

            sId = view.findViewById(R.id.show_details_serieId);
            sName = view.findViewById(R.id.show_details_serieName);
            sFavIcon = view.findViewById(R.id.show_details_favimg);
            sImage = view.findViewById(R.id.show_details_img);
            sNetwork = view.findViewById(R.id.show_details_network);
            sCountry = view.findViewById(R.id.show_details_country);
            sPremiered = view.findViewById(R.id.show_details_premiered);
            sGenre = view.findViewById(R.id.show_details_genre);
            sLanguage = view.findViewById(R.id.show_details_language);
            sSeasons = view.findViewById(R.id.show_details_seasonsText);
            sStatus = view.findViewById(R.id.show_details_status);
            sRating = view.findViewById(R.id.show_details_rating);
            sOfficialSite = view.findViewById(R.id.show_details_officialSite);
            sSummary = view.findViewById(R.id.show_details_summary);

            seasonList = new ArrayList<>();

            String serieId = getArguments().getString("serieId");

            watchedProgressBarLayout.setVisibility(View.GONE);
            showDetailsScrollView.setVisibility(View.GONE);
            showDetailsProgressBar.setVisibility(View.VISIBLE);

            new ApiSearchSerieById().execute(serieId);
        }
        else
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    class ApiSearchSerieById extends AsyncTask<String, Void, String>
    {
        API api = new API();

        protected void onPreExecute(){}

        protected String doInBackground(String... serieId)
        {
            try
            {
                URL url = new URL(api.getShowById(serieId[0]));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally
                {
                    urlConnection.disconnect();
                }
            }
            catch(Exception e)
            {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response)
        {
            Log.i("INFO: ", response);
            try
            {
                if (response != null)
                {
                    JSONObject object = new JSONObject(response);

                    String id = object.getString("id");

                    String name = object.getString("name");

                    String language = object.getString("language");
                    if(language == "null")
                        language = "n/a";

                    String network = "n/a";
                    if(!object.isNull("network"))
                        network = object.getJSONObject("network").getString("name");
                    else if(!object.isNull("webChannel"))
                        network = object.getJSONObject("webChannel").getString("name");
                    if(network == "null")
                        network = "n/a";

                    String premiered = object.getString("premiered");
                    if(premiered == "null")
                        premiered = "n/a";
                    else
                        premiered = FormatDate(premiered);

                    String genres = "";
                    JSONArray genresArray = object.getJSONArray("genres");
                    if(genresArray.length() > 0)
                        for (int i = 0; i < genresArray.length(); i++)
                            if(i == 0)
                                genres = genres + genresArray.getString(i);
                            else
                                genres = genres + ", " + genresArray.getString(i);
                    else
                        genres = "n/a";

                    String officialSite = object.getString("officialSite");
                    if(officialSite == "null")
                        officialSite = "n/a";

                    String summary = object.getString("summary");
                    if(summary == "null")
                        summary = "n/a";
                    else
                    {
                        String summary1 = summary.replace("<p>", "");
                        String summary2 = summary1.replace("</p>", "");
                        String summary3 = summary2.replace("<b>", "");
                        String summary4 = summary3.replace("</b>", "");
                        String summary5 = summary4.replace("<i>","\"");
                        String summary6 = summary5.replace("</i>","\"");
                        summary = summary6;
                    }

                    String rate = object.getJSONObject("rating")
                                        .getString("average") + " / 10";
                    if(rate == "null")
                        rate = "n/a";

                    String country = "n/a";
                    if(!object.isNull("network"))
                        if(!object.getJSONObject("network").isNull("country"))
                            country = object.getJSONObject("network").getJSONObject("country").getString("name");
                        else if(!object.isNull("webChannel"))
                            if(!object.getJSONObject("webChannel").isNull("country"))
                                country = object.getJSONObject("webChannel").getJSONObject("country").getString("name");
                    if(country == "null")
                        country = "n/a";

                    String status = object.getString("status");
                    if(status == "null")
                        status = "n/a";

                    String imageURL="";
                    if(!object.isNull("image"))
                        imageURL = object.getJSONObject("image").getString("medium");
                    if(imageURL == "null")
                        imageURL = "";

                    String seasons = "n/a";
                    String seasonResponse = new ApiGetShowSeasons().execute(id).get();
                    JSONArray seasonsJson = new JSONArray(seasonResponse);
                    if(seasonsJson.length() > 0)
                    {
                        try
                        {
                            int seasonsTotal = seasonsJson.length();
                            JSONObject lastSeason = seasonsJson.getJSONObject(seasonsTotal-1);
                            if(lastSeason.getString("premiereDate") == "null")
                                seasonsTotal--;
                            seasons = Integer.toString(seasonsTotal);
                            for (int i = 0; i < seasonsTotal; i++)
                            {
                                JSONObject seasonObj = seasonsJson.getJSONObject(i);
                                String seasonId = seasonObj.getString("id");
                                String seasonNumber = seasonObj.getString("number");
                                String seasonEpisQty = seasonObj.getString("episodeOrder");
                                if (seasonEpisQty == "null")
                                    seasonEpisQty = "n/a";
                                SeasonListRow slr = new SeasonListRow(seasonId, seasonNumber, seasonEpisQty);
                                seasonList.add(slr);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    int serieTotalEpisodes =0;
                    String episodeResponse = new ApiGetShowEpisodes().execute(id).get();
                    JSONArray episodesJson = new JSONArray(episodeResponse);
                    if(episodesJson.length() > 0)
                    {
                        try
                        {
                            int episodesTotal = episodesJson.length();
                            for(int i = 1; i <= episodesJson.length(); i++)
                            {
                                JSONObject lastValidEpisode = episodesJson.getJSONObject(episodesTotal-1);
                                String airDate = lastValidEpisode.getString("airdate");
                                if(isEpisodeLaterThanToday(airDate))
                                    episodesTotal--;
                                else
                                    break;
                            }
                            serieTotalEpisodes = episodesTotal;
                            for (SeasonListRow season:seasonList)
                            {
                                if(season.getEpisodesQty() == "n/a")
                                {
                                    int count = 0;
                                    for(int i = 0; i < episodesJson.length(); i++)
                                    {
                                        JSONObject episodeObject = episodesJson.getJSONObject(i);
                                        if(episodeObject.getString("season") == season.getNumber())
                                            count++;
                                    }
                                    season.setEpisodesQty(Integer.toString(count));
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    ShowDetails sd = new ShowDetails(id, name, rate, network, premiered, genres, language, seasons, officialSite, summary, country, status, imageURL);
                    if(sd.getImageUrl() == "")
                        sd.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/No_image_available_400_x_600.svg/2000px-No_image_available_400_x_600.svg.png");

                    int progress = 0;
                    if(serieTotalEpisodes > 0)
                    {
                        ArrayList<String> episodesWatchedList = DB.getEpisodesBySerie(id);
                        if(episodesWatchedList != null)
                        {
                            int episodesWatched = episodesWatchedList.size();
                            progress = (int) (episodesWatched*100)/serieTotalEpisodes;
                        }
                    }
                    watchedProgressBar.setProgress(progress);
                    watchedProgressPercentage.setText(progress + "%");

                    if(DB.isSerieFavorite(id))
                    {
                        sd.setFavorite(true);
                        sFavIcon.setImageResource(R.drawable.star_filled_yellow);
                        watchedProgressBarLayout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        sd.setFavorite(false);
                        sFavIcon.setImageResource(R.drawable.star_filled_white);
                        watchedProgressBarLayout.setVisibility(View.GONE);
                    }

                    sFavIcon.setOnClickListener(new FavoriteClickListener(sd, sFavIcon, getContext(), getView()));

                    sd.setImageBitmap(new DownloadImageTask().execute(sd).get());

                    sId.setText(sd.getId());
                    sName.setText(sd.getName());
                    sImage.setImageBitmap(sd.getImageBitmap());
                    sNetwork.setText(sd.getNetwork());
                    sCountry.setText(sd.getCountry());
                    sPremiered.setText(sd.getPremiered());
                    sGenre.setText(sd.getGenre());
                    sLanguage.setText(sd.getLanguage());
                    sSeasons.setText(sd.getSeasons());
                    sStatus.setText(sd.getStatus());
                    sRating.setText(sd.getRate());
                    sOfficialSite.setText(sd.getOfficialSite());
                    sSummary.setText(sd.getSummary());

                    for (SeasonListRow row: seasonList)
                    {
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        LinearLayout seasonRow = (LinearLayout) inflater.inflate(R.layout.show_details_season_row_layout, seasonListLayout, false);

                        TextView rowSeasonNumber = seasonRow.findViewById(R.id.show_details_seasonNumber);
                        TextView rowEpisodesQty = seasonRow.findViewById(R.id.show_details_episodesQty);
                        TextView rowSeasonId = seasonRow.findViewById(R.id.show_details_seasonId);
                        TextView rowShowName = seasonRow.findViewById(R.id.show_details_showName);

                        rowSeasonId.setText(row.getId());
                        rowSeasonNumber.setText("Season " + row.getNumber());
                        rowEpisodesQty.setText(row.getEpisodesQty() + " Episodes");
                        rowShowName.setText(sd.getName());

                        seasonRow.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                LinearLayout showDetailsView = (LinearLayout) view.getParent().getParent();
                                TextView serieIdTV = showDetailsView.findViewById(R.id.show_details_serieId);
                                TextView seasonIdTV = view.findViewById(R.id.show_details_seasonId);
                                TextView seasonNumberTV = view.findViewById(R.id.show_details_seasonNumber);
                                TextView showNameTV = view.findViewById(R.id.show_details_showName);
                                String seasonNumber = seasonNumberTV.getText().toString();
                                String replace = seasonNumber.replace("Season ","");
                                seasonNumber = replace;
                                String serieId = serieIdTV.getText().toString();
                                String seasonId = seasonIdTV.getText().toString();
                                String showName = showNameTV.getText().toString();
                                Fragment selectedFragment = SeasonEpisodeFragment.newInstance();
                                Bundle args = new Bundle();
                                args.putString("serieId", serieId);
                                args.putString("seasonId", seasonId);
                                args.putString("seasonNumber", seasonNumber);
                                args.putString("serieName", showName);
                                selectedFragment.setArguments(args);
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_showDetails, selectedFragment,"ShowDetailsFragment");
                                transaction.addToBackStack("ShowDetailsFragment");
                                transaction.commit();
                            }
                        });
                        seasonListLayout.addView(seasonRow);
                    }
                    showDetailsScrollView.setVisibility(View.VISIBLE);
                    showDetailsProgressBar.setVisibility(View.GONE);
                }
                else
                {
                    Toast.makeText(getContext(), "Web service not found!", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        private String FormatDate(String date) throws ParseException
        {
            SimpleDateFormat readFormatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat writeFormatter = new SimpleDateFormat("dd-MM-yyyy");
            Date dateParsed = readFormatter.parse(date);
            String result = writeFormatter.format(dateParsed);
            return result;
        }
        private boolean isEpisodeLaterThanToday(String date) throws ParseException
        {
            SimpleDateFormat readFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date dateParsed = readFormatter.parse(date);
            if(dateParsed.after(Calendar.getInstance().getTime()))
                return true;
            return false;
        }
    }

    class ApiGetShowSeasons extends AsyncTask<String, Void, String>
    {
        API api = new API();

        protected void onPreExecute(){}

        protected String doInBackground(String... serieId)
        {
            try
            {
                URL url = new URL(api.getShowSeason(serieId[0]));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally
                {
                    urlConnection.disconnect();
                }
            }
            catch(Exception e)
            {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response){}
    }

    class ApiGetShowEpisodes extends AsyncTask<String, Void, String>
    {
        API api = new API();

        protected void onPreExecute(){}

        protected String doInBackground(String... serieId)
        {
            try
            {
                URL url = new URL(api.getShowEpisodes(serieId[0]));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally
                {
                    urlConnection.disconnect();
                }
            }
            catch(Exception e)
            {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response){}
    }

    class DownloadImageTask extends AsyncTask<ShowDetails, Void, Bitmap>
    {
        ShowDetails sd;

        public DownloadImageTask(){}

        protected void onPreExecute(){}

        protected Bitmap doInBackground(ShowDetails... serie)
        {
            sd = serie[0];
            String imageSrc = sd.getImageUrl();
            Bitmap imageBmp = null;
            if(imageSrc != "")
            {
                try
                {
                    InputStream in = new URL(imageSrc).openStream();
                    imageBmp = BitmapFactory.decodeStream(in);
                }
                catch (Exception e)
                {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
            return imageBmp;
        }

        protected void onPostExecute(Bitmap result){}
    }
}
