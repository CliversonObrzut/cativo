package ait.cativoapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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


public class SeasonEpisodeFragment extends Fragment
{

    View view;
    ProgressBar seasonEpisodeProgressBar;
    LinearLayout seasonWatchedPBLayout;
    LinearLayout seasonEpisodeInfoLayout;
    LinearLayout episodesHorizontalLayout;
    ProgressBar episodeProgressBar;
    ScrollView episodeDetailsScrollView;
    ProgressBar seasonWatchedProgressBar;
    TextView seasonWatchedPercentageView;
    ImageView checkEpisodeWatchedImg;
    ImageView checkSeasonWatchedImg;
    ArrayList<LinearLayout> episodeButtonListLayout;

    TextView seasonSerieIdView;
    ImageView seasonImgView;
    TextView seasonSerieNameView;
    TextView seasonNumberAndEpisodesQtyView;
    TextView startDateView;
    TextView endDateView;

    TextView episodeSerieIdView;
    TextView episodeIdView;
    TextView episodeNumberView;
    TextView episodeTitleView;
    TextView dayTimeRuntimeView;
    ImageView episodeImgView;
    TextView episodeSummaryView;
    ArrayList<EpisodeButtonListRow> episodeButtonList;

    String seasonId;
    String serieId;
    String serieName;
    String seasonNumber;
    int seasonEpisodeQty;

    public static SeasonEpisodeFragment newInstance()
    {
        SeasonEpisodeFragment fragment = new SeasonEpisodeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        TextView toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.title_season_details);
        if(view == null)
        {
            view = inflater.inflate(R.layout.fragment_season_episode, container, false);
            seasonEpisodeProgressBar = view.findViewById(R.id.season_episode_progressBar);
            seasonWatchedPBLayout = view.findViewById(R.id.season_episode_watchedPBLayout);
            seasonEpisodeInfoLayout = view.findViewById(R.id.season_episode_infoLayout);
            episodesHorizontalLayout = view.findViewById(R.id.episodes_horizontalLayout);
            episodeProgressBar = view.findViewById(R.id.episode_details_progressBar);
            episodeDetailsScrollView = view.findViewById(R.id.episode_details_ScrollView);
            seasonWatchedProgressBar = view.findViewById(R.id.season_watchedProgressBar);
            seasonWatchedPercentageView = view.findViewById(R.id.season_watchedPercentage);
            checkEpisodeWatchedImg = view.findViewById(R.id.episode_checkWatchedImg);
            checkSeasonWatchedImg = view.findViewById(R.id.season_checkWatchedImg);
            episodeButtonList = new ArrayList<>();
            episodeButtonListLayout= new ArrayList<>();

            seasonId = getArguments().getString("seasonId");
            serieId = getArguments().getString("serieId");
            serieName = getArguments().getString("serieName");
            seasonNumber = getArguments().getString("seasonNumber");

            seasonSerieIdView = view.findViewById(R.id.season_serieIdText);
            seasonImgView = view.findViewById(R.id.season_img);
            seasonSerieNameView = view.findViewById(R.id.season_serieNameText);
            seasonNumberAndEpisodesQtyView = view.findViewById(R.id.season_number_episode_qty);
            startDateView = view.findViewById(R.id.season_startDate);
            endDateView = view.findViewById(R.id.season_endDate);

            episodeSerieIdView = view.findViewById(R.id.episode_serieIdText);
            episodeIdView = view.findViewById(R.id.episode_episodeIdText);
            episodeNumberView = view.findViewById(R.id.episode_numberText);
            episodeTitleView = view.findViewById(R.id.episode_titleText);
            dayTimeRuntimeView = view.findViewById(R.id.episode_date_time);
            episodeImgView = view.findViewById(R.id.episode_img);
            episodeSummaryView = view.findViewById(R.id.episode_summaryText);

            seasonEpisodeProgressBar.setVisibility(View.VISIBLE);
            seasonWatchedPBLayout.setVisibility(View.GONE);
            seasonEpisodeInfoLayout.setVisibility(View.GONE);

            new ApiGetShowSeason().execute(serieId);
        }
        else
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        return view;
    }

    class ApiGetShowSeason extends AsyncTask<String, Void, String>
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

        protected void onPostExecute(String response)
        {
            Log.i("INFO: ", response);
            try
            {
                if (response != null)
                {
                    JSONArray showSeasons = new JSONArray(response);

                    int seasonsTotal = showSeasons.length();
                    JSONObject lastSeason = showSeasons.getJSONObject(seasonsTotal-1);
                    if(lastSeason.getString("premiereDate") == "null")
                        seasonsTotal--;

                    JSONObject seasonObj = showSeasons.getJSONObject(0);
                    for(int i=0; i < seasonsTotal; i++)
                    {
                        seasonObj = showSeasons.getJSONObject(i);
                        if(seasonObj.getString("number").equals(seasonNumber))
                            break;
                    }

                    String id = seasonObj.getString("id");

                    String seasonSerieId = serieId;

                    String seasonSerieName = serieName;

                    String number = seasonObj.getString("number");

                    String episodesQty = "n/a";
                    boolean hasFutureEpisodes = false;
                    EpisodeButtonListRow buttonRow = new EpisodeButtonListRow("1",id);
                    int episodesCount=0;
                    String seasonEpisodesResponse = new ApiGetSeasonEpisodes().execute(buttonRow).get();
                    JSONArray seasonEpisodesJson = new JSONArray(seasonEpisodesResponse);
                    if(seasonEpisodesJson.length() > 0)
                    {
                        for(int i =0; i< seasonEpisodesJson.length(); i++)
                        {
                            JSONObject episodeObj = seasonEpisodesJson.getJSONObject(i);
                            if(episodeObj.getString("number") != "null")
                            {
                                String epId = episodeObj.getString("id");
                                String epNumber = episodeObj.getString("number");
                                String epRuntime = episodeObj.getString("runtime");
                                if(epRuntime.equals("") || epRuntime.equals("null"))
                                    epRuntime = "n/a";
                                String airDate = episodeObj.getString("airdate");
                                if(airDate == "null")
                                    airDate = "n/a";
                                else
                                    airDate = FormatDate(airDate);
                                if(isEpisodeLaterThanToday(airDate) && !hasFutureEpisodes)
                                    hasFutureEpisodes = true;
                                EpisodeButtonListRow episodeButton = new EpisodeButtonListRow(epId, epNumber, id, epRuntime);
                                episodeButtonList.add(episodeButton);
                                episodesCount++;
                            }
                        }
                        episodesQty = Integer.toString(episodesCount);
                        seasonEpisodeQty = episodesCount;
                    }

                    String startDate = seasonObj.getString("premiereDate");
                    if(startDate == "null")
                        startDate = "n/a";
                    else
                        startDate = FormatDate(startDate);

                    String endDate = seasonObj.getString("endDate");
                    if(endDate == "null")
                        endDate = "n/a";
                    else
                        endDate = FormatDate(endDate);

                    String imageURL="";
                    if(!seasonObj.isNull("image"))
                        imageURL = seasonObj.getJSONObject("image").getString("medium");
                    if(imageURL == "null")
                        imageURL = "";

                    SeasonDetails sd = new SeasonDetails(id, number, seasonSerieId, seasonSerieName, episodesQty, imageURL, startDate, endDate);
                    if(sd.getImageUrl() == "")
                        sd.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/No_image_available_400_x_600.svg/2000px-No_image_available_400_x_600.svg.png");
                    sd.setImageBitmap(new DownloadSeasonImageTask().execute(sd).get());

                    sd.setWatched(DB.isSeasonWatched(seasonSerieId, number, Integer.parseInt(episodesQty)));

                    checkSeasonWatchedImg.setOnClickListener(new SeasonWatchedClickListener(sd, checkEpisodeWatchedImg, checkSeasonWatchedImg, getContext(), episodeButtonListLayout, episodeButtonList, seasonEpisodeQty, seasonWatchedProgressBar, seasonWatchedPercentageView));

                    seasonSerieIdView.setText(sd.getSerieId());
                    seasonImgView.setImageBitmap(sd.getImageBitmap());
                    seasonSerieNameView.setText(sd.getSerieName());
                    seasonNumberAndEpisodesQtyView.setText("Season " + sd.getNumber() + " - " + sd.getEpisodesQty() + " Episodes");
                    startDateView.setText(sd.getStartDate());
                    endDateView.setText(sd.getEndDate());

                    int watchedSeasonEpisodes = 0;
                    for (EpisodeButtonListRow row:episodeButtonList)
                    {
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        LinearLayout episodeButtonLayout = (LinearLayout) inflater.inflate(R.layout.episode_button_layout, episodesHorizontalLayout, false);

                        TextView episodeButtonNumber = episodeButtonLayout.findViewById(R.id.episode_buttonText);
                        TextView episodeButtonSeasonId = episodeButtonLayout.findViewById(R.id.episode_buttonSerieId);
                        TextView episodeButtonEpId = episodeButtonLayout.findViewById(R.id.episode_buttonEpId);

                        episodeButtonEpId.setText(row.getId());
                        episodeButtonNumber.setText(row.getNumber());
                        episodeButtonSeasonId.setText(row.getSeasonId());


                        if(DB.isEpisodeWatched(row.getId()))
                        {
                            episodeButtonNumber.setBackgroundResource(R.drawable.episode_number_selected_background);
                            episodeButtonNumber.setTextColor(Color.BLACK);
                            watchedSeasonEpisodes++;
                        }
                        else
                        {
                            episodeButtonNumber.setBackgroundResource(R.drawable.episode_number_background);
                            episodeButtonNumber.setTextColor(Color.WHITE);
                        }
                        episodesHorizontalLayout.addView(episodeButtonLayout);
                        episodeButtonListLayout.add(episodeButtonLayout);

                        episodeButtonLayout.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                TextView episodeButtonNumber = view.findViewById(R.id.episode_buttonText);
                                TextView episodeButtonSeasonId = view.findViewById(R.id.episode_buttonSerieId);

                                String episNumber = episodeButtonNumber.getText().toString();
                                String episSeasonId = episodeButtonSeasonId.getText().toString();

                                EpisodeButtonListRow episSelected = new EpisodeButtonListRow(episNumber, episSeasonId);
                                try
                                {
                                    new ApiGetSeasonEpisodes().execute(episSelected).get();
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                catch (ExecutionException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    if(DB.isSerieFavorite(serieId))
                    {
                        if(!hasFutureEpisodes)
                        {
                            if(sd.isWatched())
                                checkSeasonWatchedImg.setImageResource(R.drawable.checked_icon);
                            else
                                checkSeasonWatchedImg.setImageResource(R.drawable.unchecked_icon);
                            checkSeasonWatchedImg.setVisibility(View.VISIBLE);
                        }
                        else
                            checkSeasonWatchedImg.setVisibility(View.GONE);

                        int seasonProgress = (watchedSeasonEpisodes*100)/episodesCount;
                        seasonWatchedProgressBar.setProgress(seasonProgress);
                        seasonWatchedPercentageView.setText(Integer.toString(seasonProgress)+"%");
                        seasonWatchedPBLayout.setVisibility(View.VISIBLE);
                    }
                    else
                        checkSeasonWatchedImg.setVisibility(View.GONE);

                    seasonEpisodeProgressBar.setVisibility(View.GONE);
                    seasonEpisodeInfoLayout.setVisibility(View.VISIBLE);
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
            SimpleDateFormat readFormatter = new SimpleDateFormat("dd-MM-yyyy");
            Date dateParsed = readFormatter.parse(date);
            if(dateParsed.after(Calendar.getInstance().getTime()))
                return true;
            return false;
        }
    }

    class ApiGetSeasonEpisodes extends AsyncTask<EpisodeButtonListRow, Void, String>
    {
        API api = new API();
        EpisodeButtonListRow buttonRow;

        protected void onPreExecute()
        {
            episodeProgressBar.setVisibility(View.VISIBLE);
            episodeDetailsScrollView.setVisibility(View.GONE);
        }

        protected String doInBackground(EpisodeButtonListRow... buttons)
        {
            try
            {
                buttonRow = buttons[0];
                URL url = new URL(api.getSeasonEpisodes(buttonRow.getSeasonId()));
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
                    JSONArray episodesListJA = new JSONArray(response);

                    JSONObject episodeObj = episodesListJA.getJSONObject(0);
                    for(int i=0; i < episodesListJA.length(); i++)
                    {
                        episodeObj = episodesListJA.getJSONObject(i);
                        if(episodeObj.getString("number").equals(buttonRow.getNumber()))
                            break;
                    }

                    String id = episodeObj.getString("id");

                    String number = episodeObj.getString("number");

                    String episodeSeasonNumber = seasonNumber;

                    String episodeSerieId = serieId;

                    String imageURL="";
                    if(!episodeObj.isNull("image"))
                        imageURL = episodeObj.getJSONObject("image").getString("medium");
                    if(imageURL == "null")
                        imageURL = "";

                    String episodeTitle = episodeObj.getString("name");
                    if(episodeTitle == "null")
                        episodeTitle = "n/a";

                    String airDate = episodeObj.getString("airdate");
                    if(airDate == "null")
                        airDate = "n/a";
                    else
                        airDate = FormatDate(airDate);

                    String airTime = episodeObj.getString("airtime");
                    if(airTime == "null")
                        airTime = "time n/a";

                    String runtime = episodeObj.getString("runtime");
                    if(runtime == "null")
                        runtime = "runtime n/a";

                    String summary = episodeObj.getString("summary");
                    if(summary.equals("") || summary.equals(null) || summary.equals("null"))
                        summary = "Summary n/a";
                    else
                        summary = removeHtmlTag(summary);


                    EpisodeDetails ed = new EpisodeDetails(id, number, episodeSerieId, episodeSeasonNumber, imageURL, episodeTitle, airDate, airTime, runtime, summary);
                    if(ed.getImageUrl() == "")
                        ed.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/No_image_available_400_x_600.svg/2000px-No_image_available_400_x_600.svg.png");
                    ed.setImageBitmap(new DownloadEpisodeImageTask().execute(ed).get());

                    ed.setWatched(DB.isEpisodeWatched(ed.getId()));

                    if(DB.isSerieFavorite(serieId) && !isEpisodeLaterThanToday(ed.getAirDate()))
                    {
                        if(ed.isWatched())
                            checkEpisodeWatchedImg.setImageResource(R.drawable.checked_icon);
                        else
                            checkEpisodeWatchedImg.setImageResource(R.drawable.unchecked_icon);
                        checkEpisodeWatchedImg.setVisibility(View.VISIBLE);
                    }
                    else
                        checkEpisodeWatchedImg.setVisibility(View.GONE);


                    checkEpisodeWatchedImg.setOnClickListener(new EpisodeWatchedClickListener(ed, checkEpisodeWatchedImg, checkSeasonWatchedImg, getContext(), episodeButtonListLayout, seasonEpisodeQty, seasonWatchedProgressBar, seasonWatchedPercentageView));

                    episodeIdView.setText(ed.getId());
                    episodeSerieIdView.setText(ed.getSerieId());
                    episodeNumberView.setText("Episode "+ed.getNumber()+": ");
                    episodeTitleView.setText("\""+ed.getTitle()+"\"");
                    dayTimeRuntimeView.setText(ed.getAirDate()+" at "+ed.getAirTime()+" - "+ed.getRuntime()+"min");
                    episodeImgView.setImageBitmap(ed.getImageBitmap());
                    episodeSummaryView.setText(ed.getSummary());

                    episodeProgressBar.setVisibility(View.GONE);
                    episodeDetailsScrollView.setVisibility(View.VISIBLE);
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

        private String removeHtmlTag(String summary)
        {
            String summary1 = summary.replace("<p>", "");
            String summary2 = summary1.replace("</p>", "");
            String summary3 = summary2.replace("<b>", "");
            String summary4 = summary3.replace("</b>", "");
            String summary5 = summary4.replace("<i>","\"");
            String summary6 = summary5.replace("</i>","\"");
            return summary6;
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
            SimpleDateFormat readFormatter = new SimpleDateFormat("dd-MM-yyyy");
            Date dateParsed = readFormatter.parse(date);
            if(dateParsed.after(Calendar.getInstance().getTime()))
                return true;
            return false;
        }
    }

    class DownloadSeasonImageTask extends AsyncTask<SeasonDetails, Void, Bitmap>
    {
        SeasonDetails sd;

        public DownloadSeasonImageTask(){}

        protected void onPreExecute(){}

        protected Bitmap doInBackground(SeasonDetails... season)
        {
            sd = season[0];
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

    class DownloadEpisodeImageTask extends AsyncTask<EpisodeDetails, Void, Bitmap>
    {
        EpisodeDetails ed;

        public DownloadEpisodeImageTask(){}

        protected void onPreExecute(){}

        protected Bitmap doInBackground(EpisodeDetails... episodes)
        {
            ed = episodes[0];
            String imageSrc = ed.getImageUrl();
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
