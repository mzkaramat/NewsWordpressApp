package com.example.administrator.newsexplorer.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.newsexplorer.ConfirmRegistration;
import com.example.administrator.newsexplorer.R;
import com.example.administrator.newsexplorer.StorageSharedPref;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Xeaphii on 9/13/2015.
 */
public class ChangeMobileFragment extends Fragment {
    Button  SubmitCode;
    EditText RegistrationCode;
    StorageSharedPref sharedStorage;


    public ChangeMobileFragment(){}
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_change_mobile, container, false);
        sharedStorage = new StorageSharedPref(getActivity());
        RegistrationCode= (EditText) rootView.findViewById(R.id.registration_code);
        SubmitCode = (Button) rootView.findViewById(R.id.submit_code);
        SubmitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegistrationCode.getText().toString().trim().length() >0) {
                    if (isNetworkAvailable()) {

                        new ConfirmCode(getActivity()).execute(new String[]{sharedStorage.GetPrefs("user_id", null), RegistrationCode.getText().toString()});

                    } else {
                        Toast.makeText(getActivity(), "No network present", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Mobile number should be greater than zero", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
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
                Intent i = new Intent(getActivity(), ConfirmRegistration.class);
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
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
