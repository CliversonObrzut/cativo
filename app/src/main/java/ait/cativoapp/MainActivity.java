package ait.cativoapp;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ActionProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private BottomNavigationView bottomNavigationView;
    private TextView serieIdView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB.CreateDatabase(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.cativo_logo_24px);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        disableShiftMode(bottomNavigationView);
        bottomNavigationView.setItemTextColor(getResources().getColorStateList(R.color.bottom_menu_color));
        bottomNavigationView.setItemIconTintList(getResources().getColorStateList(R.color.bottom_menu_color));
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_bot_profile:
                                toolbarTitle.setText(R.string.title_profile);
                                selectedFragment = ProfileFragment.newInstance();
                                break;
                            case R.id.navigation_bot_explore:
                                toolbarTitle.setText(R.string.title_explore);
                                selectedFragment = ExploreFragment.newInstance();
                                break;
                            case R.id.navigation_bot_watch:
                                toolbarTitle.setText(R.string.title_watch);
                                selectedFragment = WatchFragment.newInstance();
                                break;
                            case R.id.navigation_bot_calendar:
                                toolbarTitle.setText(R.string.title_calendar);
                                selectedFragment = CalendarFragment.newInstance();
                                break;
                            case R.id.navigation_bot_settings:
                                toolbarTitle.setText(R.string.title_settings);
                                selectedFragment = SettingsFragment.newInstance();
                                break;

                        }
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        //transaction.addToBackStack(null);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ProfileFragment.newInstance());
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // create menu from xml to java objects, add to menu (parent group)
        getMenuInflater().inflate(R.menu.navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId(); // 16908332
        if(id == 16908332) // arrow icon on left side of toolbar id
        {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            {
                String fragmentTag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName();
                if(fragmentTag == "ExploreFragment")
                {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setIcon(R.drawable.cativo_logo_24px);
                    toolbarTitle.setText(R.string.title_explore);
                    getSupportFragmentManager().popBackStack();
                }
                if(fragmentTag == "ProfileFragment")
                {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    toolbarTitle.setText(R.string.title_profile);
                    Fragment selectedFragment = ProfileFragment.newInstance();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, selectedFragment);
                    //transaction.addToBackStack(null);
                    transaction.commit();
                }
                if(fragmentTag == "ShowDetailsFragment")
                {
                    toolbarTitle.setText(R.string.title_show_details);
                    getSupportFragmentManager().popBackStack();
                    Fragment selectedFragment = ShowDetailsFragment.newInstance();
                    serieIdView = (TextView) findViewById(R.id.show_details_serieId);
                    String serieId = serieIdView.getText().toString();
                    Bundle args = new Bundle();
                    args.putString("serieId", serieId);
                    selectedFragment.setArguments(args);
                    String fragmentTagNew = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-2).getName();
                    getSupportFragmentManager().popBackStack();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    if(fragmentTagNew == "ExploreFragment")
                        transaction.replace(R.id.fragment_explore, selectedFragment, fragmentTagNew);
                    if(fragmentTagNew == "ProfileFragment")
                        transaction.replace(R.id.fragment_profile, selectedFragment, fragmentTagNew);
                    transaction.addToBackStack(fragmentTagNew);
                    transaction.commit();
                }
            }
            else
            {
                Toast.makeText(this, "Back not available!",Toast.LENGTH_SHORT).show();
            }
        }
        else if (id != R.id.toolbar_menu)
        {
            Fragment selectedFragment = null;
            switch (item.getItemId())
            {
                case R.id.navigation_profile:
                    toolbarTitle.setText(R.string.title_profile);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_bot_profile);
                    selectedFragment = ProfileFragment.newInstance();
                    break;
                case R.id.navigation_explore:
                    toolbarTitle.setText(R.string.title_explore);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_bot_explore);
                    selectedFragment = ExploreFragment.newInstance();
                    break;
                case R.id.navigation_watch:
                    toolbarTitle.setText(R.string.title_watch);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_bot_watch);
                    selectedFragment = WatchFragment.newInstance();
                    break;
                case R.id.navigation_calendar:
                    toolbarTitle.setText(R.string.title_calendar);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_bot_calendar);
                    selectedFragment = CalendarFragment.newInstance();
                    break;
                case R.id.navigation_settings:
                    toolbarTitle.setText(R.string.title_settings);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_bot_settings);
                    selectedFragment = SettingsFragment.newInstance();
                    break;
                case R.id.navigation_logout:
                    finish();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        return true;
    }

    public static void disableShiftMode(BottomNavigationView view)
    {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try
        {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++)
            {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        }
        catch (NoSuchFieldException e)
        {
            //Timber.e(e, "Unable to get shift mode field");
        }
        catch (IllegalAccessException e)
        {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }


    @Override
    public void onBackPressed()
    {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            String fragmentTag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName();
            if(fragmentTag == "ExploreFragment")
            {
                bottomNavigationView.setSelectedItemId(R.id.navigation_bot_explore);
                bottomNavigationView.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setIcon(R.drawable.cativo_logo_24px);
                toolbarTitle.setText(R.string.title_explore);
                getSupportFragmentManager().popBackStack();

            }
            if(fragmentTag == "ProfileFragment")
            {
                getSupportFragmentManager().popBackStack();
                bottomNavigationView.setSelectedItemId(R.id.navigation_bot_profile);
                bottomNavigationView.setVisibility(View.VISIBLE);
                toolbarTitle.setText(R.string.title_profile);
                Fragment selectedFragment = ProfileFragment.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                //transaction.addToBackStack(null);
                transaction.commit();
            }
            if(fragmentTag == "ShowDetailsFragment")
            {
                toolbarTitle.setText(R.string.title_show_details);
                getSupportFragmentManager().popBackStack();
                Fragment selectedFragment = ShowDetailsFragment.newInstance();
                serieIdView = (TextView) findViewById(R.id.show_details_serieId);
                String serieId = serieIdView.getText().toString();
                Bundle args = new Bundle();
                args.putString("serieId", serieId);
                selectedFragment.setArguments(args);
                String fragmentTagNew = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-2).getName();
                getSupportFragmentManager().popBackStack();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(fragmentTagNew == "ExploreFragment")
                    transaction.replace(R.id.fragment_explore, selectedFragment, fragmentTagNew);
                if(fragmentTagNew == "ProfileFragment")
                    transaction.replace(R.id.fragment_profile, selectedFragment, fragmentTagNew);
                transaction.addToBackStack(fragmentTagNew);
                transaction.commit();
            }
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy()
    {
        try
        {
            DB.seriesDB.close();
        }
        catch (Exception e)
        {
            // nothing
        }
        super.onDestroy();
    }
}
