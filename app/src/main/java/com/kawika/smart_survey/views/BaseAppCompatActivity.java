package com.kawika.smart_survey.views;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.utils.ActivityTransitionHelper;

/*
 * Created by senthiljs on 08/02/18.
 */

public class BaseAppCompatActivity extends AppCompatActivity {
    /**
     * back press
     *
     * @param item menu item
     * @return item selected option
     */
    protected static final String EXTRA_SAMPLE = "sample";
    protected static final String EXTRA_TYPE = "type";
    protected static final int TYPE_PROGRAMMATICALLY = 0;
    static final int TYPE_XML = 1;

    void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressWarnings("unchecked")
    protected void transitionTo(Intent i) {
        final Pair<View, String>[] pairs = ActivityTransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
