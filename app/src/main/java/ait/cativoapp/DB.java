package ait.cativoapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Cliver on 12/09/2017.
 */

public class DB
{
    public static SQLiteDatabase seriesDB = null;
    private static Context context;
    private static final String DATABASE_NAME = "Cativo.db";
    private static final String TABLE_SERIES = "Series";
    private static final String FIELD_SERIE_ID = "serieId";
    private static final String TABLE_EPISODES = "Episodes";
    private static final String FIELD_EPISODE_ID = "episodeId";
    private static final String FIELD_SEASON_NUMBER = "seasonNumber";
    private static final String FIELD_RUNTIME = "runtime";
    private static final boolean TEST_MODE = false;

    public static void CreateDatabase(Context ctx)
    {
        context = ctx;
        try
        {
            //DeleteDatabase(); // Run only for test purpose
            // Creates OR opens existing DB by name
            seriesDB = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            seriesDB.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SERIES +
                    " (id integer primary key, " +
                    FIELD_SERIE_ID + " VARCHAR NOT NULL UNIQUE);");

            seriesDB.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EPISODES +
                    " (id integer primary key, " +
                    FIELD_EPISODE_ID + " VARCHAR NOT NULL UNIQUE, " +
                    FIELD_SERIE_ID + " VARCHAR NOT NULL, " +
                    FIELD_SEASON_NUMBER + " VARCHAR, " +
                    FIELD_RUNTIME + " VARCHAR);");

            if(TEST_MODE)
            {
                Cursor cursorOne = seriesDB.rawQuery("SELECT * FROM " + TABLE_SERIES, null);
                Cursor cursorTwo = seriesDB.rawQuery("SELECT * FROM " + TABLE_EPISODES, null);

                if((cursorOne == null || cursorOne.getCount() == 0) && (cursorTwo == null || cursorTwo.getCount() == 0))
                {
                    seriesDB.execSQL("insert into "+TABLE_SERIES+"(id ,"+FIELD_SERIE_ID+") values(1,\"82\")");
                    seriesDB.execSQL("insert into "+TABLE_SERIES+"(id ,"+FIELD_SERIE_ID+") values(2,\"66\")");
                    seriesDB.execSQL("insert into "+TABLE_SERIES+"(id ,"+FIELD_SERIE_ID+") values(3,\"2705\")");

                    seriesDB.execSQL("insert into " + TABLE_EPISODES +
                            "(id ,"+FIELD_EPISODE_ID+", "+FIELD_SERIE_ID+", "+FIELD_SEASON_NUMBER+", "+FIELD_RUNTIME+") "+
                            "values(1,\"4952\",\"82\",\"1\",\"60\")");
                    seriesDB.execSQL("insert into " + TABLE_EPISODES +
                            "(id ,"+FIELD_EPISODE_ID+", "+FIELD_SERIE_ID+", "+FIELD_SEASON_NUMBER+", "+FIELD_RUNTIME+") "+
                            "values(2,\"4953\",\"82\",\"1\",\"60\")");
                    seriesDB.execSQL("insert into " + TABLE_EPISODES +
                            "(id ,"+FIELD_EPISODE_ID+", "+FIELD_SERIE_ID+", "+FIELD_SEASON_NUMBER+", "+FIELD_RUNTIME+") "+
                            "values(3,\"4954\",\"82\",\"1\",\"60\")");
                    seriesDB.execSQL("insert into " + TABLE_EPISODES +
                            "(id ,"+FIELD_EPISODE_ID+", "+FIELD_SERIE_ID+", "+FIELD_SEASON_NUMBER+", "+FIELD_RUNTIME+") "+
                            "values(4,\"2913\",\"66\",\"1\",\"30\")");
                    seriesDB.execSQL("insert into " + TABLE_EPISODES +
                            "(id ,"+FIELD_EPISODE_ID+", "+FIELD_SERIE_ID+", "+FIELD_SEASON_NUMBER+", "+FIELD_RUNTIME+") "+
                            "values(5,\"2914\",\"66\",\"1\",\"30\")");
                    seriesDB.execSQL("insert into " + TABLE_EPISODES +
                            "(id ,"+FIELD_EPISODE_ID+", "+FIELD_SERIE_ID+", "+FIELD_SEASON_NUMBER+", "+FIELD_RUNTIME+") "+
                            "values(6,\"203469\",\"2705\",\"1\",\"60\")");
                    seriesDB.execSQL("insert into " + TABLE_EPISODES +
                            "(id ,"+FIELD_EPISODE_ID+", "+FIELD_SERIE_ID+", "+FIELD_SEASON_NUMBER+", "+FIELD_RUNTIME+") "+
                            "values(7,\"208978\",\"2705\",\"1\",\"60\")");
                }
            }
            File database = context.getDatabasePath(DATABASE_NAME);

            if (!database.exists())
            {
                Toast.makeText(context, "DATABASE NOT CREATED OR FOUND!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Error creating DB: " + e, Toast.LENGTH_LONG).show();
        }
    }

    protected static ArrayList<String> getMySeries()
    {
        ArrayList<String> series = new ArrayList<String>();
        try
        {
            Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_SERIES, null);

            if(cursor != null && cursor.getCount() > 0)
            {
                int serieIdCol = cursor.getColumnIndex("serieId");

                cursor.moveToFirst();

                do
                {
                    // build row string based on current row of results
                    String row = cursor.getString(serieIdCol);
                    // add to list
                    series.add(row);
                }
                while(cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, "No database available", Toast.LENGTH_SHORT).show();
        }
        return series;
    }

    protected static void AddSerie(String serieId)
    {
        Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_SERIES + " WHERE " + FIELD_SERIE_ID + " = " + serieId, null);

        if (cursor == null || cursor.getCount() == 0)
        {
            try
            {
                seriesDB.execSQL("INSERT INTO "+ TABLE_SERIES +" ("+ FIELD_SERIE_ID +")" +
                        "VALUES('"+serieId+"');");
                Toast.makeText(context, "Serie added!", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(context, "Failed to add Serie!", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected static void DeleteSerie(String serieId)
    {
        Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_SERIES + " WHERE " + FIELD_SERIE_ID + " = " + serieId, null);

        if (cursor != null && cursor.getCount() > 0)
        {
            try
            {
                seriesDB.execSQL("DELETE FROM "+TABLE_SERIES+" WHERE "+ FIELD_SERIE_ID +" = "+serieId+";");
                Toast.makeText(context, "Serie removed!", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e)
            {
                Toast.makeText(context, "Id not found!", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected static boolean isSerieFavorite(String serieId)
    {
        Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_SERIES + " WHERE " + FIELD_SERIE_ID + " = " + serieId, null);

        if (cursor != null && cursor.getCount() > 0)
            return true;
        return false;
    }

    protected static boolean isEpisodeWatched(String episodeId)
    {
        Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_EPISODES + " WHERE " + FIELD_EPISODE_ID + " = " + episodeId, null);

        if (cursor != null && cursor.getCount() > 0)
            return true;
        return false;
    }

    protected static boolean isSeasonWatched(String serieId, String seasonNumber, int episodeQtyInSeason)
    {
        Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_EPISODES +
                " WHERE " + FIELD_SERIE_ID + " = " + serieId +
                " AND " + FIELD_SEASON_NUMBER + " = " + seasonNumber, null);

        if (cursor != null && cursor.getCount() > 0)
            if (cursor.getCount() == episodeQtyInSeason)
                return true;
            else
                return false;
        return false;
    }

    protected static ArrayList<String> getEpisodesWatched()
    {
        ArrayList<String> episodes = new ArrayList<String>();
        try
        {
            Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_EPISODES, null);

            if(cursor != null && cursor.getCount() > 0)
            {
                int episodeIdCol = cursor.getColumnIndex(FIELD_EPISODE_ID);

                cursor.moveToFirst();

                do
                {
                    // build row string based on current row of results
                    String row = cursor.getString(episodeIdCol);
                    // add to list
                    episodes.add(row);
                }
                while(cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, "No database available", Toast.LENGTH_SHORT).show();
        }
        return episodes;
    }

    protected static ArrayList<String> getEpisodesBySerie(String serieId)
    {
        ArrayList<String> episodes = new ArrayList<String>();
        try
        {
            Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_EPISODES +
                    " WHERE " + FIELD_SERIE_ID + " = " + serieId, null);

            if(cursor != null && cursor.getCount() > 0)
            {
                int episodeIdCol = cursor.getColumnIndex(FIELD_EPISODE_ID);

                cursor.moveToFirst();

                do
                {
                    // build row string based on current row of results
                    String row = cursor.getString(episodeIdCol);
                    // add to list
                    episodes.add(row);
                }
                while(cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, "No database available", Toast.LENGTH_SHORT).show();
        }
        return episodes;
    }

    protected static ArrayList<String> getEpisodesBySerieAndSeason(String serieId, String seasonNumber)
    {
        ArrayList<String> episodes = new ArrayList<String>();
        try
        {
            Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_EPISODES +
                    " WHERE " + FIELD_SERIE_ID + " = " + serieId +
                    " AND " + FIELD_SEASON_NUMBER + " = " + seasonNumber, null);

            if(cursor != null && cursor.getCount() > 0)
            {
                int episodeIdCol = cursor.getColumnIndex(FIELD_EPISODE_ID);

                cursor.moveToFirst();

                do
                {
                    // build row string based on current row of results
                    String row = cursor.getString(episodeIdCol);
                    // add to list
                    episodes.add(row);
                }
                while(cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, "No database available", Toast.LENGTH_SHORT).show();
        }
        return episodes;
    }

    // return total time in minutes
    protected static ArrayList<String> getRuntimes()
    {
        ArrayList<String> runtimes = new ArrayList<String>();
        try
        {
            Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_EPISODES, null);

            if(cursor != null && cursor.getCount() > 0)
            {
                int runtimeCol = cursor.getColumnIndex(FIELD_RUNTIME);

                cursor.moveToFirst();

                do
                {
                    // build row string based on current row of results
                    String row = cursor.getString(runtimeCol);
                    // add to list
                    runtimes.add(row);
                }
                while(cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, "No database available", Toast.LENGTH_SHORT).show();
        }
        return runtimes;
    }

    protected static void addEpisode(String episodeId, String serieId, String seasonNumber, String runtime)
    {
        Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_EPISODES +
                " WHERE " + FIELD_EPISODE_ID + " = " + episodeId, null);

        if (cursor == null || cursor.getCount() == 0)
        {
            try
            {
                seriesDB.execSQL("INSERT INTO " + TABLE_EPISODES +
                        "("+FIELD_EPISODE_ID+", "+FIELD_SERIE_ID+", "+FIELD_SEASON_NUMBER+", "+FIELD_RUNTIME+") "+
                        "VALUES('"+episodeId+"','"+serieId+"','"+seasonNumber+"','"+runtime+"')");
            }
            catch (Exception e)
            {
                Toast.makeText(context, "Failed to mark episode!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected static void deleteEpisode(String episodeId)
    {
        Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_EPISODES +
                " WHERE " + FIELD_EPISODE_ID + " = " + episodeId, null);

        if (cursor != null && cursor.getCount() > 0)
        {
            try
            {
                seriesDB.execSQL("DELETE FROM "+TABLE_EPISODES+
                        " WHERE "+ FIELD_EPISODE_ID +" = "+episodeId+";");
            }
            catch(Exception e)
            {
                Toast.makeText(context, "Id not found!", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected static void addSeason()
    {

    }

    protected static void deleteSeason(String serieId, String seasonNumber)
    {
        ArrayList<String> episodes = getEpisodesBySerieAndSeason(serieId, seasonNumber);
        for (String episode:episodes)
        {
            deleteEpisode(episode);
        }
    }

    protected static void deleteSerieSeasonEpisodes(String serieId)
    {
        ArrayList<String> episodes = getEpisodesBySerie(serieId);
        for (String episode:episodes)
        {
            deleteEpisode(episode);
        }
    }
    protected static void DeleteDatabase()
    {
        context.deleteDatabase(DATABASE_NAME);
        seriesDB = null;
    }
}
