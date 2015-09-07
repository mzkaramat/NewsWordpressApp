package com.example.administrator.newsexplorer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;


/**
 * Created by Xeaphii on 9/6/2015.
 */
public class UserDetails extends Activity {

    EditText Address,Dob,Age,BriefDescp;
    Spinner GenderSelect;
    Button SubmitButton;
    ImageView CameraAct;
    CheckBox MakePrivate;
    private static final int CAMERA_REQUEST = 1888;
    StorageSharedPref sharedStorage;
    String ba1,picturePath;
    Uri selectedImage;

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

        MakePrivate = (CheckBox) findViewById(R.id.private_profile_check);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {

                    new InputUserDet(UserDetails.this).execute(new String[]{sharedStorage.GetPrefs("user_id",""),
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

        CameraAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
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
            this.dialog.setMessage("Submitting details");
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
                Toast.makeText(context, "Profile Details Saved, Now uploading Image", Toast.LENGTH_LONG).show();
                upload();

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            CameraAct.setImageBitmap(photo);
            selectedImage = data.getData();
            photo = (Bitmap) data.getExtras().get("data");

            // Cursor to get image uri to display

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
        }
    }
     class uploadToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(UserDetails.this);
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image uploading!");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("base64", ba1));
            nameValuePairs.add(new BasicNameValuePair("ImageName",sharedStorage.GetPrefs("user_id","") + ".jpg"));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ghanchidarpan.org/news/image_upload.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String st = EntityUtils.toString(response.getEntity());


            } catch (Exception e) {
               // Log.v("log_tag", "Error in http connection " + e.toString());
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
            Intent i = new Intent(UserDetails.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
     void upload() {
        // Image location URL
//        Log.e("path", "----------------" + picturePath);

        // Image
        Bitmap bm = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba,Base64.DEFAULT);

//        Log.e("base64", "-----" + ba1);

        // Upload image to server
        new uploadToServer().execute();

    }

}

