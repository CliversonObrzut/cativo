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
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Cliver on 07/09/2017.
 */

public class SeasonListAdapter extends ArrayAdapter<SeasonListRow>
{

    public SeasonListAdapter(Context context, ArrayList<SeasonListRow> value)
    {
        super(context, R.layout.show_details_season_row_layout, value);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent)
    {
        View view;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.show_details_season_row_layout,parent, false);

        TextView rowSeasonNumber = view.findViewById(R.id.show_details_seasonNumber);
        TextView rowEpisodesQty = view.findViewById(R.id.show_details_episodesQty);
        TextView rowSeasonId = view.findViewById(R.id.show_details_seasonId);
        SeasonListRow slr = getItem(position);

        rowSeasonId.setText(slr.getId());
        rowSeasonNumber.setText("Season " + slr.getNumber());
        rowEpisodesQty.setText(slr.getEpisodesQty() + " Episodes");

        return view;
    }
}


