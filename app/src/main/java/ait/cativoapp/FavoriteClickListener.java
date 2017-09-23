package ait.cativoapp;


import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Cliver on 12/09/2017.
 */

public class FavoriteClickListener implements View.OnClickListener
{
    SearchResult result;
    ShowDetails details;
    ImageButton starButton;
    Context context;
    LinearLayout progressBarLayout;

    public FavoriteClickListener(SearchResult result, ImageButton starButton, Context context )
    {
        this.result = result;
        this.starButton = starButton;
        this.context = context;
    }

    public FavoriteClickListener(ShowDetails details, ImageButton starButton, Context context, View view )
    {
        this.details = details;
        this.starButton = starButton;
        this.context = context;
        progressBarLayout = view.findViewById(R.id.show_details_watchedPBLayout);
    }

    @Override
    public void onClick(View view)
    {
        if(result != null)
        {
            if (!result.isFavorite())
            {
                try
                {
                    DB.AddSerie(result.getId());
                    starButton.setImageResource(R.drawable.star_filled_yellow);
                    result.setFavorite(true);
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "Star Button selection error", Toast.LENGTH_LONG)
                         .show();
                }
            }
            else
            {
                try
                {
                    DB.DeleteSerie(result.getId());
                    starButton.setImageResource(R.drawable.star_filled_white);
                    result.setFavorite(false);
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "Star Button deselection error", Toast.LENGTH_LONG)
                         .show();
                }
            }
        }
        if(details != null)
        {
            if (!details.isFavorite())
            {
                try
                {
                    DB.AddSerie(details.getId());
                    starButton.setImageResource(R.drawable.star_filled_yellow);
                    details.setFavorite(true);
                    progressBarLayout.setVisibility(View.VISIBLE);
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "Star Button selection error", Toast.LENGTH_LONG)
                         .show();
                }
            }
            else
            {
                try
                {
                    DB.DeleteSerie(details.getId());
                    starButton.setImageResource(R.drawable.star_filled_white);
                    details.setFavorite(false);
                    progressBarLayout.setVisibility(View.GONE);
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "Star Button deselection error", Toast.LENGTH_LONG)
                         .show();
                }
            }
        }

       //adapter.notifyDataSetChanged();
    }
}
