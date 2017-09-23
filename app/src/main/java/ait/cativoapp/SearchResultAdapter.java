package ait.cativoapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Cliver on 07/09/2017.
 */

public class SearchResultAdapter extends ArrayAdapter<SearchResult>
{

    public SearchResultAdapter(Context context, ArrayList<SearchResult> value)
    {
        super(context, R.layout.search_result_row_layout, value);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent)
    {
        View view;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.search_result_row_layout,parent, false);

        ImageView rowImage = view.findViewById(R.id.search_result_img);
        TextView rowName = view.findViewById(R.id.search_result_serieName);
        TextView rowRate = view.findViewById(R.id.search_result_rate);
        TextView rowCountry = view.findViewById(R.id.search_result_country);
        TextView rowStatus = view.findViewById(R.id.search_result_status);
        final TextView rowSerieId = view.findViewById(R.id.search_result_serieId);
        final ImageButton starButton = view.findViewById(R.id.search_result_favimg);

        SearchResult sr = getItem(position);

        starButton.setOnClickListener(new FavoriteClickListener(sr, starButton, getContext()));

        if(sr.isFavorite())
        {
            starButton.setImageResource(R.drawable.star_filled_yellow);
        }
        else
        {
            starButton.setImageResource(R.drawable.star_filled_white);
        }

        rowSerieId.setText(sr.getId());
        rowName.setText(sr.getName());
        rowRate.setText("Rate: " + sr.getRate());
        rowCountry.setText(sr.getCountry());
        rowStatus.setText(sr.getStatus());
        rowImage.setImageBitmap(sr.getImageBitmap());

        return view;
    }
}


