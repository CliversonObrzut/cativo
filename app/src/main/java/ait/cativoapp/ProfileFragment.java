package ait.cativoapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.util.concurrent.ExecutionException;


public class ProfileFragment extends Fragment
{

    View view;
    ProgressBar mySeriesProgressBar;
    LinearLayout mySeriesLayout;

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
        if(view == null)
        {
            view = inflater.inflate(R.layout.fragment_profile, container, false);

            mySeriesProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_profile);
            mySeriesLayout = (LinearLayout) view.findViewById(R.id.my_series_info_linear_layout);
            mySeriesProgressBar.setVisibility(View.VISIBLE);
            ArrayList<String> mySeries = DB.getMySeries();
            if(mySeries.size() > 0)
            {
                for (String serieId : mySeries)
                {
                    new ApiSearchSerieById().execute(serieId);
                }
            }
            else
            {
                mySeriesProgressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "You don't have any series", Toast.LENGTH_LONG).show();
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
        private Exception exception;

        protected void onPreExecute()
        {

        }

        protected String doInBackground(String... serieId)
        {
            try
            {
                URL url = new URL(api.SearchShowById(serieId[0]));
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
                        ImageButton serieImg = (ImageButton) serieItem.findViewById(R.id.profile_my_series_img);
                        TextView serieName = (TextView) serieItem.findViewById(R.id.profile_my_series_name);

                        serieName.setText(ms.getName());
                        serieImg.setImageBitmap(ms.getImageBitmap());

                        mySeriesLayout.addView(serieItem);
                        if(mySeriesProgressBar.getVisibility() == View.VISIBLE)
                            mySeriesProgressBar.setVisibility(View.GONE);
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

        protected void onPreExecute()
        {

        }

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
