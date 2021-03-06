package com.example.android.homecookinrecipes;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.homecookinrecipes.data.RecipeContract;
import com.example.android.homecookinrecipes.data.RecipeRecyclerAdapter;
import com.example.android.homecookinrecipes.ui.SpinnerActivity;
import com.example.android.homecookinrecipes.utility.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    private DrawerLayout mDrawer;
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private String mSelection, mSortOrder;
    private String[] mSelectionArgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((RecipeAnalytics) getApplication()).startTracking();
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTextView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager =
                    new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }else{
            layoutManager =
                    new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        }
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mSelection = null;
        mSelectionArgs = null;
        mSortOrder = "RANDOM() LIMIT 100";

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        showLoading();
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processQuery(query.trim());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                return false;
            }
        });
        return true;
    }

    private void processQuery(String query) {
        mSelection = RecipeContract.RecipeEntry.COLUMN_TITLE + " LIKE ?";
        mSelectionArgs = new String[]{"%" + query + "%"};
        reload();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_trending) {
            mSelection = RecipeContract.RecipeEntry.COLUMN_SORT + " =?";
            mSelectionArgs = new String[]{"t"};
        } else if (id == R.id.nav_top_rated) {
            mSelection = RecipeContract.RecipeEntry.COLUMN_SORT + " =?";
            mSelectionArgs = new String[]{"r"};
        } else if (id == R.id.nav_favorite) {
            mSelection = RecipeContract.RecipeEntry.COLUMN_ISFAV + " =?";
            mSelectionArgs = new String[]{"1"};
        } else if (id == R.id.nav_recommended) {
            mSelection = RecipeContract.RecipeEntry.COLUMN_RATING + " = ?";
            mSelectionArgs = new String[]{"100"};
        } else if (id == R.id.nav_spinner) {
            Intent intent = new Intent(this, SpinnerActivity.class);
            startActivity(intent);
            mDrawer.closeDrawer(GravityCompat.START);
            return true;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        reload();
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                RecipeContract.RecipeEntry.CONTENT_URI,
                null,
                mSelection,
                mSelectionArgs,
                mSortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        mTextView.setVisibility(View.INVISIBLE);
        if (data.getCount() != 0) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else if (data.getCount() == 0) {
            if (!Util.isConnected(this)) {
                mRecyclerView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                mTextView.setText(R.string.internet_connection_message);
                mTextView.setVisibility(View.VISIBLE);
            } else if (mSelection != null && (mSelection.equals(RecipeContract.RecipeEntry.COLUMN_ISFAV + " =?")
                    || mSelection.equals(RecipeContract.RecipeEntry.COLUMN_TITLE + " LIKE ?"))) {
                mRecyclerView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                mTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void reload() {
        showLoading();
        getLoaderManager().restartLoader(0, null, this);
    }

}
