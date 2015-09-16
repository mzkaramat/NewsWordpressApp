package com.xeaphii.ghanchi.darpan.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xeaphii.ghanchi.darpan.R;
import com.xeaphii.ghanchi.darpan.StorageSharedPref;
import com.xeaphii.ghanchi.darpan.AdvertisementNews;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Random;

public class ContactFragment extends Fragment {
    ImageView AdvImage;
    ImageLoader imageLoader;
	public ContactFragment(){}
    EditText MessageBody;
    Button SendMail;
    StorageSharedPref sharedStorage;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        AdvImage= (ImageView)rootView.findViewById(R.id.adv_img);
        sharedStorage = new StorageSharedPref(getActivity());
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        final StorageSharedPref sharedStorage;
        sharedStorage = new StorageSharedPref(getActivity());


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
        MessageBody = (EditText) rootView.findViewById(R.id.body_edittext);
        SendMail = (Button) rootView.findViewById(R.id.send_mail);
        SendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    if (MessageBody.getText().toString().length()>6) {

                            new SignUpTask(getActivity()).execute(new String[]{sharedStorage.GetPrefs("user_id","")
                                    ,MessageBody.getText().toString()});



                    } else {
                        Toast.makeText(getActivity(), "Message must be of greater than 6 characters", Toast.LENGTH_LONG).show();
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
            context = c;
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
                HttpGet httppost = new HttpGet(("http://ghanchidarpan.org/news/SendMail.php?user_id=" +
                        encodeHTML(urls[0]) +
                        "&msg=" +
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
            if (success == 200) {
//                Toast.makeText(context,"User Created Successfullly",Toast.LENGTH_LONG).show();
                //sharedStorage.StorePrefs("login_cred","1");

//                new ConfirmRegistration(Signup.this).execute(new String[]{sharedStorage.GetPrefs("user_id","")});
                Toast.makeText(context, "Message send to admin", Toast.LENGTH_LONG).show();
                Fragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            } else if (success == 201) {
                Toast.makeText(context, "Some error occurs", Toast.LENGTH_LONG).show();
                //sharedStorage.StorePrefs("login_cred","1");
                //showHomeListActivity();
            } else if (success == 0) {
                Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show();
            }
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );
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
    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand=new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
