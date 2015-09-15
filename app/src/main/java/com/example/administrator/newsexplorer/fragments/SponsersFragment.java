package com.example.administrator.newsexplorer.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.newsexplorer.R;
import com.example.administrator.newsexplorer.StorageSharedPref;
import com.example.administrator.newsexplorer.AdvertisementNews;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Random;

/**
 * Created by Sunny on 9/2/2015.
 */
public class SponsersFragment extends Fragment {
    WebView webDesigner;
    ImageView AdvImage;
    ImageLoader imageLoader;
    ProgressBar progressBar;
    public SponsersFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_team, container, false);
        AdvImage= (ImageView)rootView.findViewById(R.id.adv_img);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        //imageLoader.displayImage("http://ghanchidarpan.org/news/images/images.jpg", AdvImage);
        webDesigner = (WebView) rootView.findViewById(R.id.web_designer);
        StorageSharedPref sharedStorage;
        sharedStorage = new StorageSharedPref(getActivity());


        String Adv = sharedStorage.GetPrefs("AdsString",null);
        if(Adv !=null){
            imageLoader.displayImage(Adv.split("::::")[Math.abs(randInt(0,Adv.split("::::").length)-1)], AdvImage);
            AdvImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), AdvertisementNews.class);
                    startActivity(i);
                }
            });
        }
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        webDesigner.setWebViewClient(new myWebClient());
        webDesigner.getSettings().setJavaScriptEnabled(true);

        if(isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
            webDesigner.loadUrl("http://ghanchidarpan.org/wp_site/wordpress/sponsers/");
//            webDesigner.setWebViewClient(new WebViewClient() {
//                ProgressDialog progressDialog1;
//
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(url);
//                    return true;
//                }
//
//                public void onLoadResource(WebView view, String url) {
//
//                    if (progressDialog1 == null) {
//                        progressDialog1 = new ProgressDialog(getActivity());
//                        progressDialog1.setMessage("Loading contents");
//                        progressDialog1.show();
//                    }
//                }
//
//                public void onPageFinished(WebView view, String url) {
//                    if (progressDialog1 != null&&progressDialog1.isShowing()) {
//                        progressDialog1.dismiss();
//                        progressDialog1 = null;
//                    }
//                }
//            });
        }else {
            Toast.makeText(getActivity(), "No internet connection present", Toast.LENGTH_LONG).show();
        }
        return rootView;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
            getActivity().finish();
            Toast.makeText(getActivity(), "No internet connection present", Toast.LENGTH_LONG).show();
        }
    }
    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand=new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
