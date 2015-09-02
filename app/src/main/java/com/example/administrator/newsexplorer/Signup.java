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
 * Created by Administrator on 7/7/2015.
 */
public class Signup extends Activity {
    EditText Email,UserName,Password,ConfirmPassword;
    Button SignupClick;
    StorageSharedPref sharedStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_parse_signup_fragment);
        sharedStorage = new StorageSharedPref(Signup.this);
        Email= (EditText) findViewById(R.id.signup_email_input);
        UserName= (EditText) findViewById(R.id.signup_username_input);
        Password= (EditText) findViewById(R.id.signup_password_input);
        ConfirmPassword= (EditText) findViewById(R.id.signup_confirm_password_input);
        SignupClick= (Button) findViewById(R.id.create_account);
        SignupClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                if (Password.getText().toString().length()>6) {
                    if (Password.getText().toString().equals(ConfirmPassword.getText().toString())) {
                        if (!Email.getText().toString().equals("")) {
                            if (!UserName.getText().toString().equals("")) {
                                new SignUpTask(Signup.this).execute(new String[]{UserName.getText().toString(),Password.getText().toString(),Email.getText().toString()});
                            } else {
                                Toast.makeText(getApplicationContext(), "UserName should'nt be empty", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Email should'nt be empty", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Password mismatch occer", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Password must be of greater than 6 characters", Toast.LENGTH_LONG).show();
                }
            }else {
                    Toast.makeText(getApplicationContext(), "No internet connection present", Toast.LENGTH_LONG).show();
                }}
        });
    }
    class SignUpTask extends AsyncTask<String, Void, Integer> {

        private ProgressDialog dialog;
        Context context;
        public SignUpTask(Context c) {
            dialog = new ProgressDialog(c);
            context= c;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Progress start");
            this.dialog.show();
        }

        @Override
        protected Integer doInBackground(String... urls) {
            if(IsUerNameValid(urls[0])){
            try {
                    //------------------>>
                    HttpGet httppost = new HttpGet(("http://xeamphiil.co.nf/News/signup.php?proj_username=" +
                            encodeHTML(urls[0]) +
                            "&proj_password=" +
                            encodeHTML(urls[1]) +
                            "&proj_email=" +
                            encodeHTML(urls[2])).replaceAll(" ", "%20"));
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse response = httpclient.execute(httppost);

                    // StatusLine stat = response.getStatusLine();
                    int status = response.getStatusLine().getStatusCode();

                    if (status == 200) {
                        HttpEntity entity = response.getEntity();
                        String[] data = EntityUtils.toString(entity).split(":");
                        sharedStorage.StorePrefs("user_id",data[1]);
                        return 200;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    return 0;
                }
            }else{
            return 201;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(success==200){
//                Toast.makeText(context,"User Created Successfullly",Toast.LENGTH_LONG).show();
                //sharedStorage.StorePrefs("login_cred","1");
                showHomeListActivity();
            }else if(success==201){
                Toast.makeText(context,"Username already exist",Toast.LENGTH_LONG).show();
                //sharedStorage.StorePrefs("login_cred","1");
                //showHomeListActivity();
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
    Boolean IsUerNameValid(String userName){
        try {

            //------------------>>
            HttpGet httppost = new HttpGet(("http://xeamphiil.co.nf/News/userNameCheck.php?proj_username=" +
                    encodeHTML(userName)).replaceAll(" ", "%20"));
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);

            // StatusLine stat = response.getStatusLine();
            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                if(data.equals("200"))
                    return true;
                else{
                    return false;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
