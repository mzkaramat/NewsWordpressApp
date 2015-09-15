package com.example.administrator.newsexplorer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.newsexplorer.R;
import com.example.administrator.newsexplorer.sections.AdvertisementNews;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Random;

/**
 * Created by Xeaphii on 9/6/2015.
 */
public class ViewPost extends Activity {
    WebView webDesigner;
    ImageView AdvImage;
    ImageLoader imageLoader;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_team);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        webDesigner = (WebView) findViewById(R.id.web_designer);
        AdvImage= (ImageView)findViewById(R.id.adv_img);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        StorageSharedPref sharedStorage;
        sharedStorage = new StorageSharedPref(getApplicationContext());


        String Adv = sharedStorage.GetPrefs("AdsString",null);
        if(Adv !=null){
            imageLoader.displayImage(Adv.split("::::")[Math.abs(randInt(0,Adv.split("::::").length)-1)], AdvImage);
            AdvImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), AdvertisementNews.class);
                    startActivity(i);
                }
            });
        }
        if(isNetworkAvailable()){
            Bundle extras = getIntent().getExtras();
            String id = extras.getString("post_id");
            webDesigner.loadUrl("http://ghanchidarpan.org/wp_site/wordpress/?p="+id);
            Log.e("Error","http://ghanchidarpan.org/wp_site/wordpress/?p="+id);
            //webDesigner.getSettings().setJavaScriptEnabled(true);
            progressBar = (ProgressBar) findViewById(R.id.progressBar1);
            progressBar.setVisibility(View.VISIBLE);
            webDesigner.setWebViewClient(new myWebClient());
            webDesigner.getSettings().setJavaScriptEnabled(true);
//        webDesigner.getSettings().setLoadWithOverviewMode(true);
//        webDesigner.getSettings().setUseWideViewPort(true);
//        webDesigner.getSettings().setBuiltInZoomControls(true);
//        webDesigner.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        webDesigner.setWebViewClient(new WebViewClient() {
//
//                final AlertDialog alertDialog = new AlertDialog.Builder(NewsSection.this).create();
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            public void onLoadResource(WebView view, String url) {
//                if (progressDialog == null) {
//                    progressDialog = new ProgressDialog(NewsSection.this);
//                    progressDialog.setMessage("Loading news");
//                    progressDialog.show();
//                }
//            }
//
//            public void onPageFinished(WebView view, String url) {
//                if (progressDialog != null&&progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                    progressDialog = null;
//                }
//            }
//                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                    Toast.makeText(NewsSection.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
//                    alertDialog.setTitle("Error");
//                    alertDialog.setMessage(description);
//                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            return;
//                        }
//                    });
//                    alertDialog.show();
//                }
//        });
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
    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            finish();
            Toast.makeText(getApplicationContext(), "No internet connection present", Toast.LENGTH_LONG).show();
        }
    }
    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand=new Random();;

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
