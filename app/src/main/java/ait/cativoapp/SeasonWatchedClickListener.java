package ait.cativoapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Cliver on 12/09/2017.
 */

public class SeasonWatchedClickListener implements View.OnClickListener
{
    SeasonDetails season;
    ImageView watchedButton;
    ImageView watchedSeasonButton;
    Context context;
    ArrayList<LinearLayout> episodeButtonLayoutList;
    ArrayList<EpisodeButtonListRow> episodeButtonList;
    TextView selectedButtonTV;
    ProgressBar seasonProgressBar;
    TextView seasonProgressBarPercentage;
    int seasonEpisodesQty;

    public SeasonWatchedClickListener(SeasonDetails season, ImageView watchedButton, ImageView watchedSeasonButton, Context context, ArrayList<LinearLayout> episodeButtonLayoutList, ArrayList<EpisodeButtonListRow> episodeButtonList, int seasonEpisodesQty, ProgressBar seasonProgressBar, TextView seasonProgressPercentage)
    {
        this.season = season;
        this.watchedButton = watchedButton;
        this.watchedSeasonButton = watchedSeasonButton;
        this.context = context;
        this.episodeButtonLayoutList = episodeButtonLayoutList;
        this.episodeButtonList = episodeButtonList;
        this.seasonEpisodesQty = seasonEpisodesQty;
        this.seasonProgressBar = seasonProgressBar;
        this.seasonProgressBarPercentage = seasonProgressPercentage;
    }

    @Override
    public void onClick(View view)
    {
        if(season != null)
        {
            if (!season.isWatched())
            {
                try
                {
                    watchedSeasonButton.setImageResource(R.drawable.checked_icon);
                    watchedButton.setImageResource(R.drawable.checked_icon);
                    for (LinearLayout layout:episodeButtonLayoutList)
                    {
                        selectedButtonTV = layout.findViewById(R.id.episode_buttonText);
                        selectedButtonTV.setBackgroundResource(R.drawable.episode_number_selected_background);
                        selectedButtonTV.setTextColor(Color.BLACK);
                    }
                    seasonProgressBar.setProgress(100);
                    seasonProgressBarPercentage.setText("100%");
                    for (EpisodeButtonListRow episode:episodeButtonList)
                    {
                        DB.addEpisode(episode.getId(), season.getSerieId(), season.getNumber(), episode.getRuntime());
                    }
                    season.setWatched(true);
                    Toast.makeText(context, "Season Watched", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "Watched Season Button selection error", Toast.LENGTH_LONG)
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
                    Toast.makeText(context, "Watched Season Button deselection error", Toast.LENGTH_LONG)
                         .show();
                }
            }
        }
    }
    public void showDeleteDialog(View view)
    {
        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(view.getContext());
        deleteDialog.setTitle("Remove Watched Season");
        deleteDialog.setMessage("Are you sure you want to remove this season and all watched episodes?");
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                watchedSeasonButton.setImageResource(R.drawable.unchecked_icon);
                watchedButton.setImageResource(R.drawable.unchecked_icon);
                for (LinearLayout layout:episodeButtonLayoutList)
                {
                    selectedButtonTV = layout.findViewById(R.id.episode_buttonText);
                    selectedButtonTV.setBackgroundResource(R.drawable.episode_number_background);
                    selectedButtonTV.setTextColor(Color.WHITE);
                }
                seasonProgressBar.setProgress(0);
                seasonProgressBarPercentage.setText("0%");
                DB.deleteSeason(season.getSerieId(), season.getNumber());
                season.setWatched(false);
                Toast.makeText(context, "Season Unwatched", Toast.LENGTH_SHORT).show();
            }
        });
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
