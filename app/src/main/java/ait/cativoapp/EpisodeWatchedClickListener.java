package ait.cativoapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Cliver on 12/09/2017.
 */

public class EpisodeWatchedClickListener implements View.OnClickListener
{
    EpisodeDetails episode;
    ImageView watchedButton;
    ImageView watchedSeasonButton;
    Context context;
    ArrayList<LinearLayout> episodeButtonLayoutList;
    TextView selectedButtonTV;
    ProgressBar seasonProgressBar;
    TextView seasonProgressBarPercentage;
    int seasonEpisodesQty;

    public EpisodeWatchedClickListener(EpisodeDetails episode, ImageView watchedButton, ImageView watchedSeasonButton, Context context, ArrayList<LinearLayout> episodeButtonList, int seasonEpisodesQty, ProgressBar seasonProgressBar, TextView seasonProgressPercentage)
    {
        this.episode = episode;
        this.watchedButton = watchedButton;
        this.watchedSeasonButton = watchedSeasonButton;
        this.context = context;
        this.episodeButtonLayoutList = episodeButtonList;
        this.seasonEpisodesQty = seasonEpisodesQty;
        this.seasonProgressBar = seasonProgressBar;
        this.seasonProgressBarPercentage = seasonProgressPercentage;
    }

    @Override
    public void onClick(View view)
    {
        if(episode != null)
        {
            for (LinearLayout layout:episodeButtonLayoutList)
            {
                selectedButtonTV = layout.findViewById(R.id.episode_buttonText);
                String episodeNumber = selectedButtonTV.getText().toString();
                if(episodeNumber.equals(episode.getNumber()))
                    break;
            }
            if (!episode.isWatched())
            {
                try
                {
                    DB.addEpisode(episode.getId(),episode.getSerieId(),episode.getSeasonNumber(),episode.getRuntime());
                    watchedButton.setImageResource(R.drawable.checked_icon);
                    selectedButtonTV.setBackgroundResource(R.drawable.episode_number_selected_background);
                    selectedButtonTV.setTextColor(Color.BLACK);
                    episode.setWatched(true);
                    Toast.makeText(context, "Episode Watched", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "Watched Button selection error", Toast.LENGTH_LONG)
                         .show();
                }
            }
            else
            {
                try
                {
                    DB.deleteEpisode(episode.getId());
                    watchedButton.setImageResource(R.drawable.unchecked_icon);
                    selectedButtonTV.setBackgroundResource(R.drawable.episode_number_background);
                    selectedButtonTV.setTextColor(Color.WHITE);
                    episode.setWatched(false);
                    Toast.makeText(context, "Episode Unwatched", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "Watch Button deselection error", Toast.LENGTH_LONG)
                         .show();
                }
            }
            ArrayList<String> watchedEpisodes = DB.getEpisodesBySerieAndSeason(episode.getSerieId(), episode.getSeasonNumber());
            int watchedSeasonEpisodes = watchedEpisodes.size();
            int seasonProgress = (watchedSeasonEpisodes*100)/seasonEpisodesQty;
            seasonProgressBar.setProgress(seasonProgress);
            seasonProgressBarPercentage.setText(Integer.toString(seasonProgress)+"%");
            if(seasonProgress == 100)
                watchedSeasonButton.setImageResource(R.drawable.checked_icon);
            else
                watchedSeasonButton.setImageResource(R.drawable.unchecked_icon);
        }
    }
}
