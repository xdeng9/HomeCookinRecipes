package com.example.android.homecookinrecipes;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.homecookinrecipes.data.FetchRecipeData;
import com.example.android.homecookinrecipes.data.Recipe;
import com.example.android.homecookinrecipes.data.RecipeRecyclerAdapter;
import com.example.android.homecookinrecipes.utility.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private String mSortOrder;
    private StaggeredGridLayoutManager mLayoutManager;
    private int mPage, mLastPosition;
    private boolean mLoading;
    private Recipe[] mAllRecipes;
    private RecipeRecyclerAdapter adapter;
    ProgressDialog progressDialog;
    private EndlessScrollList scrollList;

    private static final int MAX_REQUEST_ALLOWED = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        scrollList = new EndlessScrollList();
        mRecyclerView.addOnScrollListener(scrollList);
        mRecyclerView.setHasFixedSize(true);
        //mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(mLayoutManager);
        mSortOrder = "r";
        mPage = 1;

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Fetching more recipes");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        loadRecipeData(mSortOrder, String.valueOf(mPage));
    }

    private void loadRecipeData(String... params) {
        FetchRecipeData recipeTask = new FetchRecipeData(new FetchRecipeData.AsyncResponse() {
            @Override
            public void processResult(Recipe[] result) {
                refreshView(result);
            }
        });
        recipeTask.execute(params);
    }

    private void refreshView(Recipe[] result) {

        mAllRecipes = Util.addRecipeArrays(mAllRecipes, result);
        adapter = new RecipeRecyclerAdapter(MainActivity.this, mAllRecipes);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
       Log.d("Refresh View", "" + result.length + " isAdampterEmpty? " + adapter.getItemCount());
        //mRecyclerView.invalidate();
        mRecyclerView.scrollToPosition(mLastPosition);
        mLoading = true;
//        Log.d("Refresh view:", progressDialog ==null? "true": "false");
        progressDialog.dismiss();
        if (mPage < MAX_REQUEST_ALLOWED)
            mRecyclerView.addOnScrollListener(scrollList);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_trending) {

        } else if (id == R.id.nav_top_rated) {

        } else if (id == R.id.nav_favorite) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class EndlessScrollList extends RecyclerView.OnScrollListener {
        int pastVisibleItems, visibleItems, totalItems;

        @Override
        public void onScrolled(RecyclerView view, int dx, int dy) {
            if (dy > 0) {
                visibleItems = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                int[] firstVisiblePositions = new int[2];
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPositions(firstVisiblePositions)[0];

                Log.d("VisibleItems=", "" + visibleItems + " pastVisible=" + pastVisibleItems + " total=" + totalItems);

                if (mLoading && mPage <= MAX_REQUEST_ALLOWED) {
                    if ((visibleItems + pastVisibleItems) >= totalItems) {
                        mLoading = false;
                        mPage++;
                        mLastPosition = pastVisibleItems;
                        progressDialog.show();
                        mRecyclerView.removeOnScrollListener(scrollList);
                        loadRecipeData(mSortOrder, String.valueOf(mPage));
                    }
                }
            }
            Log.d("Total recipes=", "" + mAllRecipes.length);
        }
    }
}
