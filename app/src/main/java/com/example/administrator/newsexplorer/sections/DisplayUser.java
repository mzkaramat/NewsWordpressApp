package com.example.administrator.newsexplorer.sections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.newsexplorer.MainActivity;
import com.example.administrator.newsexplorer.R;
import com.example.administrator.newsexplorer.StorageSharedPref;
import com.example.administrator.newsexplorer.adapter.MemberListAdapter;
import com.example.administrator.newsexplorer.model.MemberModel;
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

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Xeaphii on 9/9/2015.
 */
public class DisplayUser extends Activity {
    EditText Name, FatherName, FatherAge, MotherName, MotherAge, GrandFatherName,
            GrandFatherAge, dob, sub_cast, qualification, famliy_name, famliy_sub_cast,
            family_nandial, family_qualiffication, family_dob, no_son, no_daugther,
            mobile_no, landline_number, skype_id, email_id, p_street, p_city, p_pincode,
            p_tehsil, p_district, p_state, b_shopname, b_contactnumber, b_street, b_city,
            b_pincode, b_tehsil, b_state, b_district, govt_post, govt_post_place, p_post,
            p_post_place, student_course, student_school, student_place;
    EditText GenderSelect, MartialStatus, Cast, Occupation, HouseWifeStatus;
    ImageLoader imageLoader;

    ImageView CameraAct;
    StorageSharedPref sharedStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_user);
        Intent i = getIntent();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(DisplayUser.this));
        String Id = i.getStringExtra("UserId");
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


        GenderSelect = (EditText) findViewById(R.id.select_gender);
        MartialStatus = (EditText) findViewById(R.id.select_maritial_status);
        Cast = (EditText) findViewById(R.id.personal_select_cast);
        HouseWifeStatus = (EditText) findViewById(R.id.select_house_wifes);
        Occupation = (EditText) findViewById(R.id.select_occupation);

//        Address = (EditText) findViewById(R.id.address);
//        Dob = (EditText) findViewById(R.id.dob);
//        Age = (EditText) findViewById(R.id.age_et);
//        BriefDescp = (EditText) findViewById(R.id.descp);
        sharedStorage = new StorageSharedPref(DisplayUser.this);

        if (isNetworkAvailable()) {
            new uploadToServer().execute(new String[]{Id});
        } else {
            Toast.makeText(getApplicationContext(), "No network present", Toast.LENGTH_LONG).show();

        }
      //  hideKeyboard();
//
    }

    class uploadToServer extends AsyncTask<String, Void, String> {

        private ProgressDialog pd = new ProgressDialog(DisplayUser.this);

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
                        params[0]).replaceAll(" ", "%20"));
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
            String[] data = result.split(":",-1);
            dob.setText(data[0]);
            GenderSelect.setText(data[1]);
            Name.setText(data[2]);
            MartialStatus.setText(data[3]);
            MotherName.setText(data[4]);
            MotherAge.setText(data[5]);
            FatherName.setText(data[6]);
            FatherAge.setText(data[7]);
            GrandFatherName.setText(data[8]);
            GrandFatherAge.setText(data[9]);
            Cast.setText(data[10]);
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
            Occupation.setText(data[30]);
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
            HouseWifeStatus.setText(data[46]);

            imageLoader.displayImage("http://ghanchidarpan.org/news/images/" + data[47].trim() + ".jpg", CameraAct);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );
        }

    }

    public static String encodeHTML(String s) {
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c > 127 || c == '"' || c == '<' || c == '>') {
                out.append("&#" + (int) c + ";");
            } else {
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
