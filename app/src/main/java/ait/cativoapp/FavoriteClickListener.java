package ait.cativoapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    ProgressBar serieProgressBar;
    TextView serieProgressBarPercentage;

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
        serieProgressBar = progressBarLayout.findViewById(R.id.show_details_watchedProgressBar);
        serieProgressBarPercentage = progressBarLayout.findViewById(R.id.show_details_watchedPercentage);
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
                    showDeleteDialog(view);
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
                    showDeleteDialog(view);
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "Star Button deselection error", Toast.LENGTH_LONG)
                         .show();
                }
            }
        }
    }

    public void showDeleteDialog(View view)
    {
        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(view.getContext());
        deleteDialog.setTitle("Remove Favourite Serie");
        deleteDialog.setMessage("Are you sure you want to remove this serie and all watched episodes?");
        if(result != null)
        {
            deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    DB.DeleteSerie(result.getId());
                    DB.deleteSerieSeasonEpisodes(result.getId());
                    starButton.setImageResource(R.drawable.star_filled_white);
                    result.setFavorite(false);
                }
            });
        }
        if(details != null)
        {
            deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    DB.DeleteSerie(details.getId());
                    DB.deleteSerieSeasonEpisodes(details.getId());
                    starButton.setImageResource(R.drawable.star_filled_white);
                    details.setFavorite(false);
                    progressBarLayout.setVisibility(View.GONE);
                    serieProgressBar.setProgress(0);
                    serieProgressBarPercentage.setText("0%");
                }
            });
        }
        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.dismiss();
            }
        });

        deleteDialog.show();
    }
}
