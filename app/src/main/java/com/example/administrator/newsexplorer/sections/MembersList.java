package com.example.administrator.newsexplorer.sections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.newsexplorer.R;
import com.example.administrator.newsexplorer.StorageSharedPref;
import com.example.administrator.newsexplorer.adapter.MemberListAdapter;
import com.example.administrator.newsexplorer.model.MemberModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xeaphii on 9/6/2015.
 */
public class MembersList extends Activity {
    StorageSharedPref sharedStorage;
    List<MemberModel> Memberslist;
    ListView Members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.members_list);
        sharedStorage = new StorageSharedPref(MembersList.this);
        Memberslist = new ArrayList<>();
        Members = (ListView) findViewById(R.id.members_list_);
        if (isNetworkAvailable()) {
        new uploadToServer().execute();} else {
            Toast.makeText(getApplicationContext(), "No network present", Toast.LENGTH_LONG).show();

        }
        Members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Memberslist.get(position).isVisible.equals("1")){
                    Intent i = new Intent(MembersList.this,DisplayUser.class);
                    i.putExtra("UserId",Memberslist.get(position).id);
                    startActivity(i);

                }else{
                    Toast.makeText(getApplicationContext(),"This has not shared his profile",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    class uploadToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(MembersList.this);
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Getting Members list!");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {


            try {
                //------------------>>
                HttpGet httppost = new HttpGet(("http://ghanchidarpan.org/news/get_members.php?user_id=" +
                        encodeHTML(sharedStorage.GetPrefs("user_id","")) ).replaceAll(" ", "%20") );
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String[] data = EntityUtils.toString(entity).split(";",-1);
                   for(int i = 0 ; i < data.length;i++){
                       Memberslist.add(new MemberModel(data[i].split(":",-1)[1],data[i].split(":",-1)[0],data[i].split(":",-1)[2]));
                   }
                }

            } catch (IOException e) {
                e.printStackTrace();

            }
            return "";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
            Members.setAdapter(new MemberListAdapter(MembersList.this, Memberslist));
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
}
