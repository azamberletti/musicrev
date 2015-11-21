package com.example.andrea.musicreview.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.example.andrea.musicreview.R;
import com.example.andrea.musicreview.fragments.ArtistSearchFragment;
import com.example.andrea.musicreview.fragments.BestAlbumsOfMonthFragment;
import com.example.andrea.musicreview.fragments.FavoriteFragment;
import com.example.andrea.musicreview.fragments.LastReviewsFragment;
import com.example.andrea.musicreview.fragments.RecommendedFragment;

public abstract class MyBaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()){
            case R.id.home:
                setTitle("MusicReview");
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new LastReviewsFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.favorites:
                setTitle("Favorites");
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new FavoriteFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.best_of_month:
                setTitle("Best of the month");
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new BestAlbumsOfMonthFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.recommended:
                setTitle("Recommended");
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new RecommendedFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.artist:
                setTitle("Search artist");
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new ArtistSearchFragment())
                        .addToBackStack(null)
                        .commit();
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}