package ait.cativoapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ana&Cliver on 07/09/2017.
 */

public class SearchResultAdapter extends ArrayAdapter<SearchResult>
{
    public SearchResultAdapter(Context context, ArrayList<SearchResult> value)
    {
        super(context, R.layout.search_result_row_layout, value);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.search_result_row_layout,parent, false);

        ImageView rowImage = (ImageView) view.findViewById(R.id.search_result_img);
        TextView rowName = (TextView) view.findViewById(R.id.search_result_serieName);
        TextView rowRate = (TextView) view.findViewById(R.id.search_result_rate);
        TextView rowStatus = (TextView) view.findViewById(R.id.search_result_status);

        SearchResult sr = getItem(position);

        rowName.setText(sr.getName());
        rowRate.setText("Rate: " + sr.getRate());
        rowStatus.setText(sr.getStatus());
        rowImage.setImageBitmap(sr.getImageBitmap());

        return view;
    }
}


