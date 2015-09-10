package com.example.administrator.newsexplorer.sections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.newsexplorer.MainActivity;
import com.example.administrator.newsexplorer.R;
import com.example.administrator.newsexplorer.StorageSharedPref;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Xeaphii on 9/9/2015.
 */
public class UpdateUser extends Activity {
    EditText Name, FatherName, FatherAge, MotherName, MotherAge, GrandFatherName,
            GrandFatherAge, dob, sub_cast, qualification, famliy_name, famliy_sub_cast,
            family_nandial, family_qualiffication, family_dob, no_son, no_daugther,
            mobile_no, landline_number, skype_id, email_id, p_street, p_city, p_pincode,
            p_tehsil, p_district, p_state, b_shopname, b_contactnumber, b_street, b_city,
            b_pincode, b_tehsil, b_state, b_district, govt_post, govt_post_place, p_post,
            p_post_place, student_course, student_school, student_place;
    ImageLoader imageLoader;

    boolean isSnap = false;
    ImageView CameraAct;
    StorageSharedPref sharedStorage;

    Spinner GenderSelect,MartialStatus,Cast,Occupation,HouseWifeStatus;

    String ba1,picturePath;
    private static final int SELECT_PHOTO = 100;
    Button BrowsePic;
    Uri selectedImage;
    Button SubmitButton;
    private static final int CAMERA_REQUEST = 1888;
    CheckBox MakePrivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        BrowsePic= (Button) findViewById(R.id.browse);
        BrowsePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);

            }
        });
        CameraAct = (ImageView) findViewById(R.id.image_to_upload);
        Name = (EditText) findViewById(R.id.name);
        FatherName = (EditText) findViewById(R.id.father_name);
        FatherAge = (EditText) findViewById(R.id.father_age);
        MotherName = (EditText) findViewById(R.id.mothers_name);
        MotherAge = (EditText) findViewById(R.id.mothers_age);
        GrandFatherName = (EditText) findViewById(R.id.grand_father_name);
        GrandFatherAge = (EditText) findViewById(R.id.grand_father_age);
        dob = (EditText) findViewById(R.id.dob);
        sub_cast = (EditText) findViewById(R.id.personal_sub_cast);
        qualification = (EditText) findViewById(R.id.personal_qualification);
        imageLoader = ImageLoader.getInstance();

        imageLoader.init(ImageLoaderConfiguration.createDefault(UpdateUser.this));

        famliy_name = (EditText) findViewById(R.id.father_name);
        famliy_sub_cast = (EditText) findViewById(R.id.wife_fathers_sub_cast);
        family_nandial = (EditText) findViewById(R.id.nandial_place);
        family_qualiffication = (EditText) findViewById(R.id.famliy_qualification);
        family_dob = (EditText) findViewById(R.id.famliy_dob);
        no_son = (EditText) findViewById(R.id.no_of_sons);
        no_daugther = (EditText) findViewById(R.id.no_of_daughters);

        mobile_no = (EditText) findViewById(R.id.mobile_no);
        landline_number = (EditText) findViewById(R.id.landline_no);
        skype_id = (EditText) findViewById(R.id.skype_id);
        email_id = (EditText) findViewById(R.id.email_id);

        p_street = (EditText) findViewById(R.id.permanent_street);
        p_city = (EditText) findViewById(R.id.permanent_city);
        p_pincode = (EditText) findViewById(R.id.permanent_pincode);
        p_tehsil = (EditText) findViewById(R.id.permanent_tehsil);
        p_district = (EditText) findViewById(R.id.permanent_district);
        p_state = (EditText) findViewById(R.id.permanent_state);

        b_shopname = (EditText) findViewById(R.id.business_shop_name);
        b_contactnumber = (EditText) findViewById(R.id.business_contact_number);
        b_street = (EditText) findViewById(R.id.business_street);
        b_city = (EditText) findViewById(R.id.business_city);
        b_pincode = (EditText) findViewById(R.id.business_pincode);
        b_tehsil = (EditText) findViewById(R.id.business_tehsil);
        b_state = (EditText) findViewById(R.id.permanent_state);
        b_district = (EditText) findViewById(R.id.business_district);
        govt_post = (EditText) findViewById(R.id.govt_post);
        govt_post_place = (EditText) findViewById(R.id.govt_post_place);
        p_post = (EditText) findViewById(R.id.private_post);
        p_post_place = (EditText) findViewById(R.id.private_post_place);
        student_course = (EditText) findViewById(R.id.school_course);
        student_school = (EditText) findViewById(R.id.school_name);
        student_place = (EditText) findViewById(R.id.school_place);

        GenderSelect= (Spinner) findViewById(R.id.select_gender);
        MartialStatus= (Spinner) findViewById(R.id.select_maritial_status);
        Cast= (Spinner) findViewById(R.id.personal_select_cast);
        HouseWifeStatus= (Spinner) findViewById(R.id.select_house_wifes);
        Occupation= (Spinner) findViewById(R.id.select_occupation);

//        Address = (EditText) findViewById(R.id.address);
//        Dob = (EditText) findViewById(R.id.dob);
//        Age = (EditText) findViewById(R.id.age_et);
//        BriefDescp = (EditText) findViewById(R.id.descp);
        sharedStorage = new StorageSharedPref(UpdateUser.this);
//
        SubmitButton = (Button) findViewById(R.id.button_submit_details);
//
//        GenderSelect = (Spinner) findViewById(R.id.select_gender);
        CameraAct = (ImageView) findViewById(R.id.image_to_upload);

        MakePrivate = (CheckBox) findViewById(R.id.private_profile_check);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {

                    new InputUserDet(UpdateUser.this).execute(new String[]{sharedStorage.GetPrefs("user_id",""),
                            "",
                            dob.getText().toString().trim(),
                            "",
                            GenderSelect.getSelectedItem().toString().trim(),
                            BooltoString(MakePrivate.isChecked()),
                            Name.getText().toString().trim(),
                            MartialStatus.getSelectedItem().toString(),
                            MotherName.getText().toString().trim(),
                            MotherAge.getText().toString().trim(),
                            FatherName.getText().toString().trim(),
                            FatherAge.getText().toString().trim(),
                            GrandFatherName.getText().toString().trim(),
                            GrandFatherAge.getText().toString().trim(),
                            Cast.getSelectedItem().toString(),
                            sub_cast.getText().toString().trim(),
                            qualification.getText().toString().trim(),
                            famliy_name.getText().toString().trim(),
                            famliy_sub_cast.getText().toString().trim(),
                            family_nandial.getText().toString().trim(),
                            family_qualiffication.getText().toString().trim(),
                            family_dob.getText().toString().trim(),
                            no_son.getText().toString().trim(),
                            no_daugther.getText().toString().trim(),
                            mobile_no.getText().toString().trim(),
                            landline_number.getText().toString().trim(),
                            email_id.getText().toString().trim(),
                            skype_id.getText().toString().trim(),
                            p_street.getText().toString().trim(),
                            p_city.getText().toString().trim(),
                            p_pincode.getText().toString().trim(),
                            p_tehsil.getText().toString().trim(),
                            p_district.getText().toString().trim(),
                            p_state.getText().toString().trim(),
                            Occupation.getSelectedItem().toString(),
                            b_shopname.getText().toString().trim(),
                            b_contactnumber.getText().toString().trim(),
                            b_street.getText().toString().trim(),
                            b_city.getText().toString().trim(),
                            b_pincode.getText().toString().trim(),
                            b_tehsil.getText().toString().trim(),
                            b_district.getText().toString().trim(),
                            b_state.getText().toString().trim(),
                            govt_post.getText().toString().trim(),
                            govt_post_place.getText().toString().trim(),
                            p_post.getText().toString().trim(),
                            p_post_place.getText().toString().trim(),
                            student_course.getText().toString().trim(),
                            student_school.getText().toString().trim(),
                            student_place.getText().toString().trim(),
                            HouseWifeStatus.getSelectedItem().toString()
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "No network present", Toast.LENGTH_LONG).show();

                }
            }
        });
//
        CameraAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        if (isNetworkAvailable()) {
            new GetData().execute();
        } else {
            Toast.makeText(getApplicationContext(), "No network present", Toast.LENGTH_LONG).show();

        }

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
                String url_ = ("http://ghanchidarpan.org/news/update_profile_details.php?id=" +
                        encodeHTML(urls[0]) +
                        "&dob=" +
                        encodeHTML(urls[2]) +
                        "&age=" +
                        encodeHTML(urls[3]) +
                        "&gender=" +
                        encodeHTML(urls[4]) +
                        "&private=" +
                        encodeHTML(urls[5]) +
                        "&Name=" +
                        encodeHTML(urls[6]) +
                        "&martial_status=" +
                        encodeHTML(urls[7]) +
                        "&mother_name=" +
                        encodeHTML(urls[8]) +
                        "&mother_age=" +
                        encodeHTML(urls[9]) +
                        "&father_huband_name=" +
                        encodeHTML(urls[10]) +
                        "&father_huband_age=" +
                        encodeHTML(urls[11]) +
                        "&grand_father_name=" +
                        encodeHTML(urls[12]) +
                        "&grand_father_age=" +
                        encodeHTML(urls[13]) +
                        "&cast=" +
                        encodeHTML(urls[14]) +
                        "&sub_cast=" +
                        encodeHTML(urls[15]) +
                        "&qualification=" +
                        encodeHTML(urls[16]) +
                        "&wife_fathers_name=" +
                        encodeHTML(urls[17]) +
                        "&wife_fathers_sub_cast=" +
                        encodeHTML(urls[18]) +
                        "&wife_fathers_nandiyal_place=" +
                        encodeHTML(urls[19]) +
                        "&wife_fathers_qualification=" +
                        encodeHTML(urls[20]) +
                        "&wife_fathers_dob=" +
                        encodeHTML(urls[21]) +
                        "&wife_fathers_no_son=" +
                        encodeHTML(urls[22]) +
                        "&wife_fathers_no_daughter=" +
                        encodeHTML(urls[23]) +
                        "&mobile_num=" +
                        encodeHTML(urls[24]) +
                        "&landline_number=" +
                        encodeHTML(urls[25]) +
                        "&email_id=" +
                        encodeHTML(urls[26]) +
                        "&skype_id=" +
                        encodeHTML(urls[27]) +
                        "&permanent_address_street=" +
                        encodeHTML(urls[28]) +
                        "&permanent_address_city=" +
                        encodeHTML(urls[29]) +
                        "&permanent_address_pin_code=" +
                        encodeHTML(urls[30]) +
                        "&permanent_address_tehsil=" +
                        encodeHTML(urls[31]) +
                        "&permanent_address_district=" +
                        encodeHTML(urls[32]) +
                        "&permanent_address_state=" +
                        encodeHTML(urls[33]) +
                        "&occupation=" +
                        encodeHTML(urls[34]) +
                        "&business_address_shop_name=" +
                        encodeHTML(urls[35]) +
                        "&business_address_contact_number=" +
                        encodeHTML(urls[36]) +
                        "&business_address_street=" +
                        encodeHTML(urls[37]) +
                        "&business_address_city=" +
                        encodeHTML(urls[38]) +
                        "&business_address_pincode=" +
                        encodeHTML(urls[39]) +
                        "&business_address_tehsil=" +
                        encodeHTML(urls[40]) +
                        "&business_address_district=" +
                        encodeHTML(urls[41]) +
                        "&business_address_sstate=" +
                        encodeHTML(urls[42]) +
                        "&govt_post=" +
                        encodeHTML(urls[43]) +
                        "&govt_place_post=" +
                        encodeHTML(urls[44]) +
                        "&private_post=" +
                        encodeHTML(urls[45]) +
                        "&private_posting_place=" +
                        encodeHTML(urls[46]) +
                        "&student_class=" +
                        encodeHTML(urls[47]) +
                        "&student_school=" +
                        encodeHTML(urls[48]) +
                        "&student_place=" +
                        encodeHTML(urls[49]) +
                        "&house_wife=" +
                        encodeHTML(urls[50])).replaceAll(" ", "%20");
                Log.e("ULR", url_);
                HttpGet httppost = new HttpGet(url_ );
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
                if(isSnap) {
                    new uploadToServer().execute();
                }
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
//            selectedImage = data.getData();
//            photo = (Bitmap) data.getExtras().get("data");
            isSnap = true;
//            // Cursor to get image uri to display
//
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            picturePath = cursor.getString(columnIndex);
////            cursor.close();
//            Bitmap bm = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 30, bao);
            byte[] ba = bao.toByteArray();
            ba1 = Base64.encodeToString(ba,Base64.DEFAULT);
        }else if(requestCode == SELECT_PHOTO){
            if(resultCode == RESULT_OK){
                Uri selectedImage = data.getData();
                Bitmap imageStream = null;
                try {
                    imageStream = decodeUri(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                isSnap = true;
                CameraAct.setImageBitmap(imageStream);
//            // Cursor to get image uri to display
//
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            picturePath = cursor.getString(columnIndex);
////            cursor.close();
//            Bitmap bm = BitmapFactory.decodeFile(picturePath);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                imageStream.compress(Bitmap.CompressFormat.JPEG, 30, bao);
                byte[] ba = bao.toByteArray();
                ba1 = Base64.encodeToString(ba,Base64.DEFAULT);
            }
        }
    }
    class uploadToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(UpdateUser.this);
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
            Intent i = new Intent(UpdateUser.this, MainActivity.class);
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
        bm.compress(Bitmap.CompressFormat.JPEG, 30, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

//        Log.e("base64", "-----" + ba1);

        // Upload image to server
        new uploadToServer().execute();

    }

    class GetData extends AsyncTask<String, Void, String> {

        private ProgressDialog pd = new ProgressDialog(UpdateUser.this);

        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Getting Members list!");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {


            try {
                //------------------>>
                HttpGet httppost = new HttpGet(("http://ghanchidarpan.org/news/GetUserDetail.php?id=" +
                        sharedStorage.GetPrefs("user_id","")).replaceAll(" ", "%20"));
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    return EntityUtils.toString(entity);
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
            if(!result.trim().equals("404:")){


            String[] data = result.split(":",-1);
            dob.setText(data[0]);
            GenderSelect.setSelection(GetArrayLoc(data[1],R.array.genders));
            Name.setText(data[2]);
            MartialStatus.setSelection(GetArrayLoc(data[3],R.array.maritial_status));
            MotherName.setText(data[4]);
            MotherAge.setText(data[5]);
            FatherName.setText(data[6]);
            FatherAge.setText(data[7]);
            GrandFatherName.setText(data[8]);
            GrandFatherAge.setText(data[9]);
            Cast.setSelection(GetArrayLoc(data[10],R.array.cast));
            sub_cast.setText(data[11]);
            qualification.setText(data[12]);
            famliy_name.setText(data[13]);
            famliy_sub_cast.setText(data[14]);
            family_nandial.setText(data[15]);
            family_qualiffication.setText(data[16]);
            family_dob.setText(data[17]);
            no_son.setText(data[18]);
            no_daugther.setText(data[19]);
            mobile_no.setText(data[20]);
            landline_number.setText(data[21]);
            email_id.setText(data[22]);
            skype_id.setText(data[23]);
            p_street.setText(data[24]);
            p_city.setText(data[25]);
            p_pincode.setText(data[26]);
            p_tehsil.setText(data[27]);
            p_district.setText(data[28]);
            p_state.setText(data[29]);
            Occupation.setSelection(GetArrayLoc(data[30],R.array.select_occupation));
            b_shopname.setText(data[31]);
            b_contactnumber.setText(data[32]);
            b_street.setText(data[33]);
            b_city.setText(data[34]);
            b_pincode.setText(data[35]);
            b_tehsil.setText(data[36]);
            b_district.setText(data[37]);
            b_state.setText(data[38]);
            govt_post.setText(data[39]);
            govt_post_place.setText(data[40]);
            p_post.setText(data[41]);
            p_post_place.setText(data[42]);
            student_course.setText(data[43]);
            student_school.setText(data[44]);
            student_place.setText(data[45]);
            HouseWifeStatus.setSelection(GetArrayLoc(data[46],R.array.house_wife_stats));

            imageLoader.displayImage("http://ghanchidarpan.org/news/images/" + data[47].trim() + ".jpg", CameraAct);
            }
        }
    }
    int GetArrayLoc(String ArrayName,int key){
        String[] testArray = getResources().getStringArray(key);
        for(int  i = 0 ; i < testArray.length;i++){
            if(testArray[i].equals(ArrayName))
                return i;
        }
        return -1;
    }@Override
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
    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }
}
