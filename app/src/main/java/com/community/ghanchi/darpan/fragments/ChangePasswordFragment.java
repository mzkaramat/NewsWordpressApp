package com.community.ghanchi.darpan.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.community.ghanchi.darpan.R;
import com.community.ghanchi.darpan.StorageSharedPref;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by Xeaphii on 9/13/2015.
 */
public class ChangePasswordFragment extends Fragment {
    EditText Password,ConfirmPassword;
    Button SignupClick;
    StorageSharedPref sharedStorage;

    public ChangePasswordFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
        sharedStorage = new StorageSharedPref(getActivity());

        Password= (EditText) rootView.findViewById(R.id.password_et);
        ConfirmPassword= (EditText) rootView.findViewById(R.id.confirm_password_et);
        SignupClick= (Button) rootView.findViewById(R.id.create_account);
        SignupClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    if (Password.getText().toString().length()>6) {
                        if (Password.getText().toString().equals(ConfirmPassword.getText().toString())) {

                                    new SignUpTask(getActivity()).execute(new String[]{sharedStorage.GetPrefs("user_id","")
                                            ,Password.getText().toString()});

                        } else {
                            Toast.makeText(getActivity(), "Password mismatch occer", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Password must be of greater than 6 characters", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "No internet connection present", Toast.LENGTH_LONG).show();
                }}
        });
        return rootView;
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

                try {
                    //------------------>>
                    HttpGet httppost = new HttpGet(("http://ghanchidarpan.org/news/ModifyPassword.php?user_id=" +
                            encodeHTML(urls[0]) +
                            "&password=" +
                            encodeHTML(urls[1])).replaceAll(" ", "%20"));
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse response = httpclient.execute(httppost);

                    // StatusLine stat = response.getStatusLine();
                    int status = response.getStatusLine().getStatusCode();

                    if (status == 200) {
                        HttpEntity entity = response.getEntity();

                        return 200;
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
//                Toast.makeText(context,"User Created Successfullly",Toast.LENGTH_LONG).show();
                //sharedStorage.StorePrefs("login_cred","1");

//                new ConfirmRegistration(Signup.this).execute(new String[]{sharedStorage.GetPrefs("user_id","")});
                Toast.makeText(context,"Password Changed",Toast.LENGTH_LONG).show();
                Fragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }else if(success==201){
                Toast.makeText(context,"Some error occurs",Toast.LENGTH_LONG).show();
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
