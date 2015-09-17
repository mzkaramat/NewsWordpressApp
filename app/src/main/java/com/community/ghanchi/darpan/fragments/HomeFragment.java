package com.community.ghanchi.darpan.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.community.ghanchi.darpan.R;
import com.community.ghanchi.darpan.StorageSharedPref;
import com.community.ghanchi.darpan.AdvertisementNews;
import com.community.ghanchi.darpan.sections.CommunityNew;
import com.community.ghanchi.darpan.sections.EntertainmentNewsSec;
import com.community.ghanchi.darpan.sections.MembersList;
import com.community.ghanchi.darpan.sections.NewsSection;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Random;

public class HomeFragment extends Fragment {
	
	public HomeFragment(){}

    ImageView AdvImage;
    ImageLoader imageLoader;
    StorageSharedPref sharedStorage;

    ImageButton News, EnterNews, MembersSection,CommunityNews,AdvButt;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        sharedStorage = new StorageSharedPref(getActivity());
        AdvImage= (ImageView)rootView.findViewById(R.id.adv_img);
        AdvButt = (ImageButton) rootView.findViewById(R.id.advs_page);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
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

        if(isNetworkAvailable())
            new GetAdvDetails().execute();

        MembersSection = (ImageButton) rootView.findViewById(R.id.members_sec);
        News= (ImageButton) rootView.findViewById(R.id.news_sec);
        EnterNews = (ImageButton) rootView.findViewById(R.id.ent_news);
//        LatestPostsBt = (ImageButton) rootView.findViewById(R.id.latest_posts);
//        LatestPostsBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), LatestPosts.class);
//                startActivity(i);
//            }
//        });
        CommunityNews = (ImageButton) rootView.findViewById(R.id.comm_news);
        if(sharedStorage.GetPrefs("user_verified","0").equals("1")){
            ((RelativeLayout)rootView.findViewById(R.id.members_list_layout)).setVisibility(View.VISIBLE);
        }
        if(isNetworkAvailable())
            new UserVerfCheck().execute();
        MembersSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedStorage.GetPrefs("user_verified","0").equals("1")){
                    Intent i = new Intent(getActivity(), MembersList.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getActivity(),"Can't access members",Toast.LENGTH_LONG).show();
                }

            }
        });

        News.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewsSection.class);
                startActivity(i);
            }
        });

        EnterNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EntertainmentNewsSec.class);
                startActivity(i);
            }
        });
        CommunityNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CommunityNew.class);
                startActivity(i);
            }
        });
        AdvButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AdvertisementNews.class);
                startActivity(i);
            }
        });
        return rootView;
    }
    class UserVerfCheck extends AsyncTask<String, Void, Integer> {


        public UserVerfCheck() {
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... urls) {

            try {
                //------------------>>
                HttpGet httppost = new HttpGet(("http://ghanchidarpan.org/news/WpUserVerf.php?user_id=" +
                        encodeHTML(sharedStorage.GetPrefs("user_id",null))
                       ).replaceAll(" ", "%20") );
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity).trim();
                    if(!data.equals("")){
                        sharedStorage.StorePrefs("user_verified",data.trim());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }

            return 0;
        }

        @Override
        protected void onPostExecute(final Integer success) {

        }
    }


    public static String encodeHTML(String s)
    {
        StringBuffer out = new StringBuffer();
        for(int i=0; i<s.length(); i++)
        {
            char c = s.charAt(i);
            if(c > 127 || c=='"' || c=='<' || c=='>')
            {
                out.append("&#"+(int)c+";");
            }
            else
            {
                out.append(c);
            }
        }
        return out.toString();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    class GetAdvDetails extends AsyncTask<String, Void, Integer> {


        public GetAdvDetails() {
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... urls) {

            try {
                //------------------>>
                HttpGet httppost = new HttpGet(("http://www.ghanchidarpan.org/wp_site/wordpress/AdvImages.php"));
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

//                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity).trim();

                        sharedStorage.StorePrefs("AdsString",data.trim());

//                }

            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }

            return 0;
        }

        @Override
        protected void onPostExecute(final Integer success) {

        }
    }
    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand=  new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
