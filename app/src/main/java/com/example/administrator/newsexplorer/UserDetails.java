package com.example.administrator.newsexplorer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Xeaphii on 9/6/2015.
 */
public class UserDetails extends Activity {

    EditText Address,Dob,Age,BriefDescp;
    Spinner GenderSelect;
    Button SubmitButton;
    ImageView CameraAct;
    CheckBox MakePrivate;
    StorageSharedPref sharedStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);

        Address = (EditText) findViewById(R.id.address);
        Dob = (EditText) findViewById(R.id.dob);
        Age = (EditText) findViewById(R.id.age_et);
        BriefDescp = (EditText) findViewById(R.id.descp);
        sharedStorage = new StorageSharedPref(UserDetails.this);

        SubmitButton = (Button) findViewById(R.id.button_submit_details);

        GenderSelect = (Spinner) findViewById(R.id.select_gender);
        CameraAct = (ImageView) findViewById(R.id.image_to_upload);

        MakePrivate = (CheckBox) findViewById(R.id.address);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {

                    new InputUserDet(UserDetails.this).execute(new String[]{sharedStorage.GetPrefs("user_id",null),
                            Address.getText().toString().trim(),
                            Dob.getText().toString().trim(),
                            Age.getText().toString().trim(),
                            BriefDescp.getText().toString().trim(),
                            GenderSelect.getSelectedItem().toString().trim(),
                            BooltoString(MakePrivate.isChecked())
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "No network present", Toast.LENGTH_LONG).show();

                }
            }
        });

    }
    class InputUserDet extends AsyncTask<String, Void, Integer> {

        private ProgressDialog dialog;
        Context context;
        public InputUserDet(Context c) {
            dialog = new ProgressDialog(c);
            context= c;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Submitting your mobile number");
            this.dialog.show();
        }

        @Override
        protected Integer doInBackground(String... urls) {

            try {
                //------------------>>
                HttpGet httppost = new HttpGet(("http://ghanchidarpan.org/news/InputUserDetails.php?user_id=" +
                        encodeHTML(urls[0]) +
                        "&user_address=" +
                        encodeHTML(urls[1]) +
                        "&dob=" +
                        encodeHTML(urls[2]) +
                        "&age=" +
                        encodeHTML(urls[3]) +
                        "&about=" +
                        encodeHTML(urls[4]) +
                        "&gender=" +
                        encodeHTML(urls[5]) +
                        "&private_mem=" +
                        encodeHTML(urls[6])).replaceAll(" ", "%20") );
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity).trim();
                    if(data.equals("200")){
                        return 200;
                    }else{
                        return 404;
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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(success==200){
                //sharedStorage.StorePrefs("user_id",RegistrationCode.getText().toString().trim());
                Toast.makeText(context, "Profile Details Saved", Toast.LENGTH_LONG).show();
                Intent i = new Intent(UserDetails.this, MainActivity.class);
                startActivity(i);
            }else if(success==404){
                Toast.makeText(context,"Some thing missing",Toast.LENGTH_LONG).show();
            }else if(success==0){
                Toast.makeText(context,"Some error occurred",Toast.LENGTH_LONG).show();
            }
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
        public String BooltoString(boolean value) {
            return value ? "1" : "0";
        }
}

