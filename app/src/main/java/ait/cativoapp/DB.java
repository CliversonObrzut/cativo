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
    private static final String FIELD_SERIEID = "serieId";

    public static void CreateDatabase(Context ctx)
    {
        context = ctx;
        try
        {
            // Creates OR opens existing DB by name
            seriesDB = context.openOrCreateDatabase("Cativo.db", MODE_PRIVATE, null);

            // if we just created a DB and not opened one, it wont be saved to disk until
            // at least 1 sql statement is called on it
            seriesDB.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SERIES +
                    " (id integer primary key, serieId VARCHAR UNIQUE);");
            Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_SERIES, null);
            if(cursor == null || cursor.getCount() == 0)
            {
                seriesDB.execSQL("insert into Series(id ,serieId) values(1,\"82\")");
                seriesDB.execSQL("insert into Series(id ,serieId) values(2,\"66\")");
                seriesDB.execSQL("insert into Series(id ,serieId) values(3,\"2705\")");
                seriesDB.execSQL("insert into Series(id ,serieId) values(4,\"172\")");
            }

            File database = context.getDatabasePath(DATABASE_NAME);

            // check if exists
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
        Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_SERIES + " WHERE " + FIELD_SERIEID + " = " + serieId, null);

        if (cursor == null || cursor.getCount() == 0)
        {
            try
            {
                seriesDB.execSQL("INSERT INTO "+ TABLE_SERIES +" ("+FIELD_SERIEID+")" +
                        "VALUES('"+serieId+"');");
                Toast.makeText(context, "Serie added!", Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(context, "Failed to add Serie!", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected static void DeleteSerie(String serieId)
    {
        Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_SERIES + " WHERE " + FIELD_SERIEID + " = " + serieId, null);

        if (cursor != null && cursor.getCount() > 0)
        {
            try
            {
                seriesDB.execSQL("DELETE FROM "+TABLE_SERIES+" WHERE "+FIELD_SERIEID+" = "+serieId+";");
                Toast.makeText(context, "Serie removed!", Toast.LENGTH_LONG).show();
            }
            catch(Exception e)
            {
                Toast.makeText(context, "Id not found!", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected static boolean isSerieFavorite(String serieId)
    {
        Cursor cursor = seriesDB.rawQuery("SELECT * FROM " + TABLE_SERIES + " WHERE " + FIELD_SERIEID + " = " + serieId, null);

        if (cursor != null && cursor.getCount() > 0)
            return true;
        return false;
    }

    protected static void DeleteDatabase()
    {
        context.deleteDatabase(DATABASE_NAME);
        seriesDB = null;
    }
}
