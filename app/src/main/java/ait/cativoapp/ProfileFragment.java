package ait.cativoapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;


public class ProfileFragment extends Fragment
{

    View view;
    ProgressBar mySeriesProgressBar;
    LinearLayout mySeriesLayout;
    RelativeLayout noSeriesLayout;
    ImageView addButtonImg;
    HorizontalScrollView mySeriesHorizontalScrollView;
    int mySeriesQty = 0;
    int count = 0;

    public static ProfileFragment newInstance()
    {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
            view = inflater.inflate(R.layout.fragment_profile, container, false);

            mySeriesProgressBar = view.findViewById(R.id.progressBar_profile);
            mySeriesLayout = view.findViewById(R.id.my_series_info_linear_layout);
            noSeriesLayout = view.findViewById(R.id.my_series_addButtonLayout);
            mySeriesHorizontalScrollView = view.findViewById(R.id.my_series_horizontal_scroll);
            addButtonImg = view.findViewById(R.id.my_series_addButtonImg);
            addButtonImg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Fragment selectedFragment = ExploreFragment.newInstance();
                    TextView toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
                    toolbarTitle.setText(R.string.title_explore);
                    BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navigation);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_bot_explore);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_explore, selectedFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
            mySeriesProgressBar.setVisibility(View.VISIBLE);
            ArrayList<String> mySeries = DB.getMySeries();
            if(mySeries.size() > 0)
            {
                mySeriesQty = mySeries.size();
                for (String serieId : mySeries)
                {
                    new ApiSearchSerieById().execute(serieId);
                }
            }
            else
            {
                mySeriesProgressBar.setVisibility(View.GONE);
                mySeriesHorizontalScrollView.setVisibility(View.GONE);
                noSeriesLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "No favorite series! Add one!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        return view;
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
                    if(object.getString("status") == "404")
                    {
                        Toast.makeText(getContext(), "Serie "+object.getString("id")+" not found!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String id = object.getString("id");

                        String name = object.getString("name");

                        String imageURL="";
                        if(!object.isNull("image"))
                            imageURL = object.getJSONObject("image").getString("medium");
                        if(imageURL == "null")
                            imageURL = "";

                        MySeries ms = new MySeries(id, name, imageURL);
                        if(ms.getImageUrl() == "")
                            ms.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/No_image_available_400_x_600.svg/2000px-No_image_available_400_x_600.svg.png");

                        ms.setImageBitmap(new DownloadImageTask().execute(ms).get());

                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        LinearLayout serieItem = (LinearLayout)inflater.inflate(R.layout.profile_my_series_row_layout, mySeriesLayout, false);
                        ImageButton serieImg = serieItem.findViewById(R.id.profile_my_series_img);
                        TextView serieName = serieItem.findViewById(R.id.profile_my_series_name);
                        TextView serieId = serieItem.findViewById(R.id.profile_mySeriesId);

                        serieName.setText(ms.getName());
                        serieId.setText(ms.getId());
                        serieImg.setImageBitmap(ms.getImageBitmap());

                        mySeriesLayout.addView(serieItem);

                        serieImg.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                LinearLayout serieLayout = (LinearLayout) view.getParent();
                                TextView id = serieLayout.findViewById(R.id.profile_mySeriesId);
                                String serieId = id.getText().toString();
                                Fragment selectedFragment = ShowDetailsFragment.newInstance();
                                Bundle args = new Bundle();
                                args.putString("serieId", serieId);
                                selectedFragment.setArguments(args);
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_profile, selectedFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                        count++;

                        if(count == mySeriesQty)
                        {
                            mySeriesProgressBar.setVisibility(View.GONE);
                            mySeriesHorizontalScrollView.setVisibility(View.VISIBLE);
                        }
                    }
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
        }
    }

    class DownloadImageTask extends AsyncTask<MySeries, Void, Bitmap>
    {
        MySeries ms;

        public DownloadImageTask(){}

        protected void onPreExecute(){}

        protected Bitmap doInBackground(MySeries... serie)
        {
            ms = serie[0];
            String imageSrc = ms.getImageUrl();
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
