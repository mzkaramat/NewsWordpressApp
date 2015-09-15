package com.example.administrator.newsexplorer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.newsexplorer.sections.AdvertisementNews;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Xeaphii on 9/13/2015.
 */
public class LatestPosts extends Activity {
    ImageView AdvImage;
    ImageLoader imageLoader;
    ListView LatestPostsList;
    List<String> Posttitles;
    List<Integer> PostIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latest_posts);
        AdvImage= (ImageView)findViewById(R.id.adv_img);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(LatestPosts.this));

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
        LatestPostsList = (ListView) findViewById(R.id.latest_posts_lists);
        Posttitles = new ArrayList<>();
        PostIds = new ArrayList<>();
        if(isNetworkAvailable()){
            new UserVerfCheck().execute();
        }else{
            Toast.makeText(LatestPosts.this,"No network connection",Toast.LENGTH_LONG).show();
        }
        LatestPostsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(LatestPosts.this,ViewPost.class);
                i.putExtra("post_id",PostIds.get(position).toString());
                startActivity(i);
            }
        });
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
                HttpGet httppost = new HttpGet(("http://ghanchidarpan.org/news/latest_posts.php") );
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String[] data = EntityUtils.toString(entity).trim().split(";");
                    for(int i = 0 ; i < data.length;i++){
                        Posttitles.add(data[i].trim().split(":")[1].trim());
                        PostIds.add(Integer.parseInt(data[i].trim().split(":")[0].trim()));
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
            LatestPostsList.setAdapter(new ArrayAdapter<String>(LatestPosts.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1,Posttitles));
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
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand=null;

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
