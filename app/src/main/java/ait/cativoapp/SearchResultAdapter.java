package ait.cativoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;
import static android.support.v7.widget.RecyclerView.*;

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
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent)
    {
        View view = convertView;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.search_result_row_layout,parent, false);

        ImageView rowImage = (ImageView) view.findViewById(R.id.search_result_img);
        TextView rowName = (TextView) view.findViewById(R.id.search_result_serieName);
        TextView rowRate = (TextView) view.findViewById(R.id.search_result_rate);
        TextView rowCountry = (TextView) view.findViewById(R.id.search_result_country);
        TextView rowStatus = (TextView) view.findViewById(R.id.search_result_status);
        final ImageButton starButton = (ImageButton) view.findViewById(R.id.search_result_favimg);

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String text = "You selected " + getItem(position).getName().toString();
                Toast.makeText(view.getContext(), text, Toast.LENGTH_LONG).show();
            }
        });

        starButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(starButton.getDrawable().getConstantState() == getContext().getResources().getDrawable(R.drawable.star_filled_white,getContext().getTheme()).getConstantState())
                {
                    starButton.setImageResource(R.drawable.star_filled_yellow);
                }
                else
                {
                    starButton.setImageResource(R.drawable.star_filled_white);
                }
            }
        });

        SearchResult sr = getItem(position);

        rowName.setText(sr.getName());
        rowRate.setText("Rate: " + sr.getRate());
        rowCountry.setText(sr.getCountry());
        rowStatus.setText(sr.getStatus());
        rowImage.setImageBitmap(sr.getImageBitmap());

        return view;
    }
}


