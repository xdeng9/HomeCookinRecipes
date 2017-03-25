package com.example.android.homecookinrecipes;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.android.homecookinrecipes.data.Recipe;
import com.example.android.homecookinrecipes.utility.Util;

public class DetailActivity extends AppCompatActivity {

    private ShareActionProvider mShareActionProvider;
    private Recipe mDetailRecipe;
    private boolean mIsFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        mDetailRecipe = intent.getExtras().getParcelable("key");
        int isFav = mDetailRecipe.getFav();
        mIsFav = isFav==0 ? false : true;
        actionBarSetup(mDetailRecipe.getTitle());

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(mDetailRecipe.getSource_url());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem item = menu.findItem(R.id.share_item);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareIntent(getShareIntent());
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem fav = menu.findItem(R.id.fav_item);
        MenuItem unfav = menu.findItem(R.id.unfav_item);

        fav.setVisible(mIsFav);
        unfav.setVisible(!mIsFav);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.fav_item:
                mIsFav = false;
                invalidateOptionsMenu();
                Util.updateFavRecipe(this, mDetailRecipe.getRecipeId(), 0);
                displayToast();
                return true;
            case R.id.unfav_item:
                mIsFav = true;
                invalidateOptionsMenu();
                Util.updateFavRecipe(this, mDetailRecipe.getRecipeId(), 1);
                displayToast();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayToast(){
        if(mIsFav){
            Toast.makeText(this, "Recipe added to favorites.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Recipe removed from favorites.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private Intent getShareIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, mDetailRecipe.getTitle());
        intent.putExtra(Intent.EXTRA_TEXT,mDetailRecipe.getSource_url());
        return intent;
    }

    private void actionBarSetup(String title) {
        ActionBar ab = getSupportActionBar();
        ab.setTitle(title);
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
