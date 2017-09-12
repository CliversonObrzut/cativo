package ait.cativoapp;


import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Cliver on 12/09/2017.
 */

public class FavoriteClickListener implements View.OnClickListener
{
    SearchResult result;
    ImageButton starButton;
    Context context;

    public FavoriteClickListener(SearchResult result, ImageButton starButton, Context context )
    {
        this.result = result;
        this.starButton = starButton;
        this.context = context;
    }

    @Override
    public void onClick(View view)
    {
        //if(starButton.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.star_filled_white,context.getTheme()).getConstantState())
        if(!result.isFavorite())
        {
            try
            {
                DB.AddSerie(result.getId());
                starButton.setImageResource(R.drawable.star_filled_yellow);
                result.setFavorite(true);
            }
            catch (Exception e)
            {
                Toast.makeText(context, "Star Button selection error", Toast.LENGTH_LONG).show();
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
                Toast.makeText(context, "Star Button deselection error", Toast.LENGTH_LONG).show();
            }
        }

       //adapter.notifyDataSetChanged();
    }
}
