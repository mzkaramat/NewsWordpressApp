package com.example.administrator.newsexplorer.sections;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.administrator.newsexplorer.R;

/**
 * Created by Xeaphii on 9/6/2015.
 */
public class NewsSection extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_section);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
