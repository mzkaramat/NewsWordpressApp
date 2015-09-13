package com.example.administrator.newsexplorer.sections;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.newsexplorer.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Xeaphii on 9/6/2015.
 */
public class NewsSection extends Activity {
    WebView webDesigner;
    ImageView AdvImage;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_section);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        webDesigner = (WebView) findViewById(R.id.web_designer);
        AdvImage= (ImageView)findViewById(R.id.adv_img);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        imageLoader.displayImage("http://ghanchidarpan.org/news/images/images.jpg", AdvImage);
        if(isNetworkAvailable()){
        webDesigner.loadUrl("http://ghanchidarpan.org/wp_site/wordpress/category/Uncategorized/");
        webDesigner.getSettings().setJavaScriptEnabled(true);
        webDesigner.getSettings().setLoadWithOverviewMode(true);
        webDesigner.getSettings().setUseWideViewPort(true);
        webDesigner.getSettings().setBuiltInZoomControls(true);
            webDesigner.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

            webDesigner.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;
                final AlertDialog alertDialog = new AlertDialog.Builder(NewsSection.this).create();
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onLoadResource(WebView view, String url) {

                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(NewsSection.this);
                    progressDialog.setMessage("Loading news");
                    progressDialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                if (progressDialog != null&&progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(NewsSection.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage(description);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    alertDialog.show();
                }
        });
        }else {
            Toast.makeText(getApplicationContext(), "No internet connection present", Toast.LENGTH_LONG).show();
        }
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
    @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(event.getAction() == KeyEvent.ACTION_DOWN){
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                if(webDesigner.canGoBack()){
                    webDesigner.goBack();
                }else{
                    finish();
                }
                return true;
        }

    }
    return super.onKeyDown(keyCode, event);
}
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
