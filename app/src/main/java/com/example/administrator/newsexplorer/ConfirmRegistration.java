package com.example.administrator.newsexplorer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
public class ConfirmRegistration extends Activity {

    Button SubmitCode, ChangeNumber;
    EditText RegistrationCode;
    StorageSharedPref sharedStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_reg_layout);
        sharedStorage = new StorageSharedPref(ConfirmRegistration.this);
        RegistrationCode= (EditText) findViewById(R.id.registration_code);
        SubmitCode = (Button) findViewById(R.id.submit_code);
        ChangeNumber = (Button) findViewById(R.id.change_number_button);
        SubmitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RegistrationCode.getText().toString().trim().length() == 6){
                    if (isNetworkAvailable()) {

                        new ConfirmCode(ConfirmRegistration.this).execute(new String[]{RegistrationCode.getText().toString(),
                                sharedStorage.GetPrefs("user_id",null)});

                    } else {
                        Toast.makeText(getApplicationContext(), "No network present", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Code should be of lenght 6", Toast.LENGTH_LONG).show();
                }
            }
        });
        ChangeNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ConfirmRegistration.this,UpdateMobileNumber.class);
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
            this.dialog.setMessage("Submitting your code");
            this.dialog.show();
        }

        @Override
        protected Integer doInBackground(String... urls) {

            try {
                //------------------>>
                HttpGet httppost = new HttpGet(("http://ghanchidarpan.org/news/SubitCode.php?proj_code=" +
                        encodeHTML(urls[0]) +"" +
                        "&proj_user=" +
                        encodeHTML(urls[1])).replaceAll(" ", "%20") );
                Log.e("Error",("http://ghanchidarpan.org/news/SubitCode.php?proj_code=" +
                        encodeHTML(urls[0]) +"" +
                        "&proj_user=" +
                        encodeHTML(urls[1])).replaceAll(" ", "%20"));
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    if(data.equals("100:")){
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
                sharedStorage.StorePrefs("confirm_user","1");
                Toast.makeText(context, "Mobile number confirmed", Toast.LENGTH_LONG).show();
                showHomeListActivity();
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
    private void showHomeListActivity() {

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // This closes the login screen so it's not on the back stack

    }
}
