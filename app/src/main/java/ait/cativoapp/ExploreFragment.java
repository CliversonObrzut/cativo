package ait.cativoapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class ExploreFragment extends Fragment
{
    private View view;
    private SearchView searchViewEditText;
    private ListView searchResultList;
    private ProgressBar searchResultProgressBar;
    private TextView searchResultLabel;
    private ArrayList<SearchResult> resultList;

    public static ExploreFragment newInstance()
    {
        ExploreFragment fragment = new ExploreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(R.drawable.cativo_logo_24px);
        if(view == null)
        {
            view = inflater.inflate(R.layout.fragment_explore, container, false);
            searchViewEditText = (SearchView) view.findViewById(R.id.search_edit_text);
            searchResultList = (ListView) view.findViewById(R.id.search_result_list_view);
            searchResultProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_explore);
            searchResultLabel = (TextView) view.findViewById(R.id.search_result_label);
            searchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    TextView id = view.findViewById(R.id.search_result_serieId);
                    String serieId = id.getText().toString();
                    Fragment selectedFragment = ShowDetailsFragment.newInstance();
                    Bundle args = new Bundle();
                    args.putString("serieId", serieId);
                    selectedFragment.setArguments(args);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_explore, selectedFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
            searchViewEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener()
            {
                @Override
                public boolean onQueryTextSubmit(String s)
                {
                    resultList = new ArrayList<>();
                    searchResultLabel.setVisibility(View.GONE);
                    searchResultList.setVisibility(View.GONE);
                    searchResultProgressBar.setVisibility(View.VISIBLE);
                    new ApiSearchSerie().execute();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s)
                {
                    return false;
                }
            });
        }
        else
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        return view;
    }

    class ApiSearchSerie extends AsyncTask<Void, Void, String>
    {
        String search = searchViewEditText.getQuery().toString();
        API api = new API();
        private Exception exception;

        protected void onPreExecute()
        {

        }

        protected String doInBackground(Void... urls)
        {
            try
            {
                URL url = new URL(api.Search(search));
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
                JSONArray jsonArray = new JSONArray(response);
                if(jsonArray.length() == 0)
                {
                    searchResultLabel.setText("No matches!");
                    searchResultProgressBar.setVisibility(View.GONE);
                    searchResultLabel.setVisibility(View.VISIBLE);
                }
                else
                {
                    searchResultLabel.setText("Search Result: " + jsonArray.length() + " matches");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getJSONObject("show")
                                          .getString("id");

                        String name = object.getJSONObject("show")
                                            .getString("name");

                        String rate = object.getJSONObject("show")
                                            .getJSONObject("rating")
                                            .getString("average");
                        if(rate == "null")
                            rate = "n/a";

                        String country = "Country n/a";
                        if(!object.getJSONObject("show").isNull("network"))
                            if(!object.getJSONObject("show").getJSONObject("network").isNull("country"))
                                country = object.getJSONObject("show").getJSONObject("network").getJSONObject("country").getString("name");
                        else if(!object.getJSONObject("show").isNull("webChannel"))
                            if(!object.getJSONObject("show").getJSONObject("webChannel").isNull("country"))
                                country = object.getJSONObject("show").getJSONObject("webChannel").getJSONObject("country").getString("name");
                        if(country == "null")
                            country = "Country n/a";

                        String status = object.getJSONObject("show")
                                              .getString("status");
                        if(status == "null")
                            status = "n/a";

                        String imageURL="";
                        if(!object.getJSONObject("show").isNull("image"))
                            imageURL = object.getJSONObject("show").getJSONObject("image").getString("medium");
                        if(imageURL == "null")
                            imageURL = "";

                        SearchResult sr = new SearchResult(id, name, rate, country, status, imageURL);
                        if(sr.getImageUrl() == "")
                            sr.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/No_image_available_400_x_600.svg/2000px-No_image_available_400_x_600.svg.png");

                        if(DB.isSerieFavorite(id))
                            sr.setFavorite(true);
                        resultList.add(sr);
                    }

                    for (SearchResult item : resultList)
                    {
                        item.setImageBitmap(new DownloadImageTask().execute(item).get());
                    }

                    SearchResultAdapter adapter = new SearchResultAdapter(getContext(), resultList);
                    searchResultList.setAdapter(adapter);
                    searchResultProgressBar.setVisibility(View.GONE);
                    searchResultLabel.setVisibility(View.VISIBLE);
                    searchResultList.setVisibility(View.VISIBLE);
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
        }
    }

    class DownloadImageTask extends AsyncTask<SearchResult, Void, Bitmap>
    {
        SearchResult sr;

        public DownloadImageTask(){}

        protected void onPreExecute()
        {
            searchResultProgressBar.setVisibility(View.VISIBLE);
        }

        protected Bitmap doInBackground(SearchResult... item)
        {
            sr = item[0];
            String imageSrc = sr.getImageUrl();
            Bitmap imageBmp = null;
            if(imageSrc != "")
            {
                try
                {
                    InputStream in = new java.net.URL(imageSrc).openStream();
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
