package com.example.administrator.newsexplorer.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.newsexplorer.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Sunny on 9/2/2015.
 */
public class GalleryFragment extends Fragment {
    WebView webDesigner;
    ImageView AdvImage;
    ImageLoader imageLoader;

    public GalleryFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.gallery_fragment, container, false);
        AdvImage= (ImageView)rootView.findViewById(R.id.adv_img);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageLoader.displayImage("http://ghanchidarpan.org/news/images/images.jpg", AdvImage);
        webDesigner = (WebView) rootView.findViewById(R.id.web_designer);
        if(isNetworkAvailable()){
            webDesigner.loadUrl("http://ghanchidarpan.org/wp_site/wordpress/sample-page/");
            webDesigner.setWebViewClient(new WebViewClient() {
                ProgressDialog progressDialog1;

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                public void onLoadResource(WebView view, String url) {

                    if (progressDialog1 == null) {
                        progressDialog1 = new ProgressDialog(getActivity());
                        progressDialog1.setMessage("Loading contents");
                        progressDialog1.show();
                    }
                }

                public void onPageFinished(WebView view, String url) {
                    if (progressDialog1 != null&&progressDialog1.isShowing()) {
                        progressDialog1.dismiss();
                        progressDialog1 = null;
                    }
                }
            });
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
}
