package com.xeaphii.ghanchi.darpan;

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
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Sunny on 9/3/2015.
 */
public class UpdateMobileNumber extends Activity {
    Button SubmitCode, ChangeNumber;
    EditText RegistrationCode;
    StorageSharedPref sharedStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_number_layout);
        sharedStorage = new StorageSharedPref(UpdateMobileNumber.this);
        RegistrationCode= (EditText) findViewById(R.id.registration_code);
        SubmitCode = (Button) findViewById(R.id.submit_code);
        ChangeNumber = (Button) findViewById(R.id.change_number_button);
        SubmitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegistrationCode.getText().toString().trim().length() >0) {
                    if (isNetworkAvailable()) {

                        new ConfirmCode(UpdateMobileNumber.this).execute(new String[]{sharedStorage.GetPrefs("user_id",null),RegistrationCode.getText().toString()});

                    } else {
                        Toast.makeText(getApplicationContext(), "No network present", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Mobile number should be greater than zero", Toast.LENGTH_LONG).show();
                }
            }
        });
        ChangeNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(UpdateMobileNumber.this, ConfirmRegistration.class);
                    startActivity(i);

                }
            }
        );
    }
    class ConfirmCode extends AsyncTask<String, Void, Integer> {

        private ProgressDialog dialog;
        Context context;
        public ConfirmCode(Context c) {
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
                HttpGet httppost = new HttpGet(("http://ghanchidarpan.org/news/UpdateMobileNumber.php?proj_user=" +
                        encodeHTML(urls[0])+"" +
                        "&proj_mobile=" +
                        encodeHTML(urls[1]) ).replaceAll(" ", "%20") );
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
                Toast.makeText(context, "Sending sms to your mobile number", Toast.LENGTH_LONG).show();
                Intent i = new Intent(UpdateMobileNumber.this, ConfirmRegistration.class);
                startActivity(i);
            }else if(success==404){
                Toast.makeText(context,"Wrong code",Toast.LENGTH_LONG).show();
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
}
