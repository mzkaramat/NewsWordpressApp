package com.example.administrator.newsexplorer.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
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

public class AboutFragment extends Fragment {
//    WebView webDesigner;
	public AboutFragment(){}
    ImageView AdvImage;
    ImageLoader imageLoader;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        AdvImage= (ImageView)rootView.findViewById(R.id.adv_img);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageLoader.displayImage("http://ghanchidarpan.org/news/images/images.jpg", AdvImage);
//        webDesigner = (WebView) rootView.findViewById(R.id.web_designer);
//        if(isNetworkAvailable()){
//            webDesigner.loadUrl("http://ghanchidarpan.org/wp_site/wordpress/about-us/");
//            webDesigner.setWebViewClient(new WebViewClient() {
//                ProgressDialog progressDialog;
//
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(url);
//                    return true;
//                }
//
//                public void onLoadResource(WebView view, String url) {
//
//                    if (progressDialog == null) {
//                        progressDialog = new ProgressDialog(getActivity());
//                        progressDialog.setMessage("Loading contents");
//                        progressDialog.show();
//                    }
//                }
//
//                public void onPageFinished(WebView view, String url) {
//                    if (progressDialog != null&&progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                        progressDialog = null;
//                    }
//                }
//            });
//        }else {
//            Toast.makeText(getActivity(), "No internet connection present", Toast.LENGTH_LONG).show();
//        }
        return rootView;
    }
//
//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
//    public void myOnKeyDownTemp(int keycode){
////        if(webDesigner.canGoBack()){
////            webDesigner.goBack();
////        }else{
////            getActivity().finish();
////        }
//
//    }
}
