package com.example.administrator.newsexplorer.sections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
    List<MemberModel> Memberslist,TempMemberlist;
    ListView Members;
    Button SearchMember;
    EditText SearchString;
    MemberListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.members_list);
        sharedStorage = new StorageSharedPref(MembersList.this);
        Memberslist = new ArrayList<>();
        SearchMember = (Button) findViewById(R.id.search_button);
        SearchString = (EditText) findViewById(R.id.member_name);
        Members = (ListView) findViewById(R.id.members_list_);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        if (isNetworkAvailable()) {
        new uploadToServer().execute();} else {
            Toast.makeText(getApplicationContext(), "No network present", Toast.LENGTH_LONG).show();

        }
        Members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Memberslist.get(position).isVisible.contains("0")){
                    Intent i = new Intent(MembersList.this,DisplayUser.class);
                    i.putExtra("UserId",Memberslist.get(position).id);
                    startActivity(i);

                }else{
                    Toast.makeText(getApplicationContext(),"This has not shared his profile",Toast.LENGTH_LONG).show();
                }
            }
        });
        SearchMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SearchString.getText().toString().trim().equals("")){
                        Memberslist.clear();
                        for(int i = 0 ; i <TempMemberlist.size();i++){
                            if(TempMemberlist.get(i).name.toLowerCase().trim().contains(SearchString.getText().toString().trim().toLowerCase())){
                                Memberslist.add((TempMemberlist.get(i)));
                            }
                        }
                        adapter.notifyDataSetChanged();
                }else{
                    Memberslist.clear();
                    for(int i = 0 ; i <TempMemberlist.size();i++){
                            Memberslist.add((TempMemberlist.get(i)));

                    }
                    adapter.notifyDataSetChanged();
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
                       Memberslist.add(new MemberModel(data[i].split(":",-1)[1],data[i].split(":",-1)[0],data[i].split(":",-1)[2].trim()));
                   }
                    TempMemberlist = new ArrayList<>(Memberslist);
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
            adapter= new MemberListAdapter(MembersList.this, Memberslist);
            Members.setAdapter(adapter);
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
