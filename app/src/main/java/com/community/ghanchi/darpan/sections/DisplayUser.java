package com.community.ghanchi.darpan.sections;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.community.ghanchi.darpan.R;
import com.community.ghanchi.darpan.StorageSharedPref;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Xeaphii on 9/9/2015.
 */
public class DisplayUser extends Activity {
    TextView Name, FatherName, FatherAge, MotherName, MotherAge, GrandFatherName,
            GrandFatherAge, dob, sub_cast, qualification, famliy_name, famliy_sub_cast,
            family_nandial, family_qualiffication, family_dob, no_son, no_daugther,
            mobile_no, landline_number, skype_id, email_id, p_street, p_city, p_pincode,
            p_tehsil, p_district, p_state, b_shopname, b_contactnumber, b_street, b_city,
            b_pincode, b_tehsil, b_state, b_district, govt_post, govt_post_place, p_post,
            p_post_place, student_course, student_school, student_place,mobile_num;
    TextView GenderSelect, MartialStatus, Cast, Occupation;
    ImageLoader imageLoader;
    String urlImage="";

    ImageView CameraAct;
    StorageSharedPref sharedStorage;
    TextView FatherNameTv,FatherAgeTv,GrandFatheName,GrandFahterAge,FamliyName;
    TextView MotherNameTv,MotherAgeTv,FamilyDobTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_user);
        Intent i = getIntent();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(DisplayUser.this));
        String Id = i.getStringExtra("UserId");

        MotherNameTv = (TextView) findViewById(R.id.mother_name_tv);
        MotherAgeTv = (TextView) findViewById(R.id.mother_age_tv);
        FamilyDobTv = (TextView) findViewById(R.id.family_dob_tv);

        FatherNameTv = (TextView) findViewById(R.id.father_name_text);
        FatherAgeTv = (TextView) findViewById(R.id.father_age_text);
        GrandFatheName = (TextView) findViewById(R.id.grand_father_name_text);
        GrandFahterAge = (TextView) findViewById(R.id.grand_father_age_text);
        FamliyName = (TextView) findViewById(R.id.wife_name_text);
        mobile_num = (TextView) findViewById(R.id.mobile_num);
        CameraAct = (ImageView) findViewById(R.id.image_to_upload);
        Name = (TextView) findViewById(R.id.name);
        FatherName = (TextView) findViewById(R.id.father_name);
        FatherAge = (TextView) findViewById(R.id.father_age);
        MotherName = (TextView) findViewById(R.id.mothers_name);
        MotherAge = (TextView) findViewById(R.id.mothers_age);
        GrandFatherName = (TextView) findViewById(R.id.grand_father_name);
        GrandFatherAge = (TextView) findViewById(R.id.grand_father_age);
        dob = (TextView) findViewById(R.id.dob);
        sub_cast = (TextView) findViewById(R.id.personal_sub_cast);
        qualification = (TextView) findViewById(R.id.personal_qualification);

        famliy_name = (TextView) findViewById(R.id.wife_fathers_name);
        famliy_sub_cast = (TextView) findViewById(R.id.wife_fathers_sub_cast);
        family_nandial = (TextView) findViewById(R.id.nandial_place);
        family_qualiffication = (TextView) findViewById(R.id.famliy_qualification);
        family_dob = (TextView) findViewById(R.id.famliy_dob);
        no_son = (TextView) findViewById(R.id.no_of_sons);
        no_daugther = (TextView) findViewById(R.id.no_of_daughters);

        mobile_no = (TextView) findViewById(R.id.mobile_no);
        landline_number = (TextView) findViewById(R.id.landline_no);
        skype_id = (TextView) findViewById(R.id.skype_id);
        email_id = (TextView) findViewById(R.id.email_id);

        p_street = (TextView) findViewById(R.id.permanent_street);
        p_city = (TextView) findViewById(R.id.permanent_city);
        p_pincode = (TextView) findViewById(R.id.permanent_pincode);
        p_tehsil = (TextView) findViewById(R.id.permanent_tehsil);
        p_district = (TextView) findViewById(R.id.permanent_district);
        p_state = (TextView) findViewById(R.id.permanent_state);

        b_shopname = (TextView) findViewById(R.id.business_shop_name);
        b_contactnumber = (TextView) findViewById(R.id.business_contact_number);
        b_street = (TextView) findViewById(R.id.business_street);
        b_city = (TextView) findViewById(R.id.business_city);
        b_pincode = (TextView) findViewById(R.id.business_pincode);
        b_tehsil = (TextView) findViewById(R.id.business_tehsil);
        b_state = (TextView) findViewById(R.id.permanent_state);
        b_district = (TextView) findViewById(R.id.business_district);
        govt_post = (TextView) findViewById(R.id.govt_post);
        govt_post_place = (TextView) findViewById(R.id.govt_post_place);
        p_post = (TextView) findViewById(R.id.private_post);
        p_post_place = (TextView) findViewById(R.id.private_post_place);
        student_course = (TextView) findViewById(R.id.school_course);
        student_school = (TextView) findViewById(R.id.school_name);
        student_place = (TextView) findViewById(R.id.school_place);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        GenderSelect = (TextView) findViewById(R.id.select_gender);
        MartialStatus = (TextView) findViewById(R.id.select_maritial_status);
        Cast = (TextView) findViewById(R.id.personal_select_cast);
//        HouseWifeStatus = (TextView) findViewById(R.id.select_house_wifes);
        Occupation = (TextView) findViewById(R.id.select_occupation);

//        Address = (TextView) findViewById(R.id.address);
//        Dob = (TextView) findViewById(R.id.dob);
//        Age = (TextView) findViewById(R.id.age_et);
//        BriefDescp = (TextView) findViewById(R.id.descp);
        sharedStorage = new StorageSharedPref(DisplayUser.this);

        if (isNetworkAvailable()) {
            new uploadToServer().execute(new String[]{Id});
        } else {
            Toast.makeText(getApplicationContext(), "No network present", Toast.LENGTH_LONG).show();

        }
        CameraAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = new ImageView(DisplayUser.this);
                image.setMinimumWidth(200);
                image.setMinimumHeight(600);
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                imageLoader.displayImage(urlImage, image);

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(DisplayUser.this).
                                setMessage("Profile Image").
                                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).
                                setView(image);
                builder.create().show();
            }
        });
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
            if(data[1].trim().equals("Male")){
                FatherNameTv.setText("Father's Name");
                FatherAgeTv.setText("Father's Age");
                GrandFatheName.setText("Grand Father's Name");
                GrandFahterAge.setText("Grand Father's Age");
                FamliyName.setText("Wife Name");

                FatherAge.setHint("Father's Age");
                FatherName.setHint("Father's Name");
                GrandFatheName.setHint("Grand Father's Name");
                GrandFahterAge.setHint("Grand Father's Age");
                FamliyName.setHint("Wife Name");
            }else{
                FatherNameTv.setText("Husband Name");
                FatherAgeTv.setText("Husband Age");
                GrandFatheName.setText("Father in law Name");
                GrandFahterAge.setText("Father in law Age");
                FamliyName.setText("Father's Name");

                FatherAge.setHint("Husband Age");
                FatherName.setHint("Husband Name");
                GrandFatheName.setHint("Father in law Name");
                GrandFahterAge.setHint("Father in law Age");
                FamliyName.setHint("Father's Name");
            }
            Name.setText(data[2]);
            MartialStatus.setText(data[3]);
            if(data[3].trim().equals("Married")&&data[1].trim().equals("Female")){
                MotherName.setVisibility(View.GONE);
                MotherAge.setVisibility(View.GONE);
                family_dob.setVisibility(View.GONE);

                MotherNameTv.setVisibility(View.GONE);
                MotherAgeTv.setVisibility(View.GONE);
                FamilyDobTv.setVisibility(View.GONE);
            }else{
                MotherName.setVisibility(View.VISIBLE);
                MotherAge.setVisibility(View.VISIBLE);
                family_dob.setVisibility(View.VISIBLE);

                MotherNameTv.setVisibility(View.VISIBLE);
                MotherAgeTv.setVisibility(View.VISIBLE);
                FamilyDobTv.setVisibility(View.VISIBLE);
            }
            if(data[3].trim().equals("Single")){
                ((LinearLayout) findViewById(R.id.family_details)).setVisibility(View.GONE);
            }else{
                ((LinearLayout) findViewById(R.id.family_details)).setVisibility(View.VISIBLE);
            }
            if(data[3].trim().equals("Single")&&data[1].trim().equals("Female")){
//                FatherNameTv.setText("Father's Name");
//                FatherAge.setHint("Father's Name");
//                FatherAgeTv.setText("Father's Age");
//                FatherAge.setHint("Father's Age");
//
//                GrandFatheName.setText("Grand Father's Name");
//                GrandFatherName.setHint("Grand Father's Name");
//                GrandFahterAge.setText("Grand Father's Age");
//                GrandFatherAge.setHint("Grand Father's Age");


                FatherNameTv.setText("Father's Name");
                FatherAgeTv.setText("Father's Age");
                GrandFatheName.setText("Grand Father's Name");
                GrandFahterAge.setText("Grand Father's Age");
                FamliyName.setText("Father's Name");

                FatherAge.setHint("Father's Age");
                FatherName.setHint("Father's Name");
                GrandFatheName.setHint("Grand Father's Age");
                GrandFahterAge.setHint("Father in law Age");
               // FamliyName.setHint("Father's Name");
            }
            if(data[3].trim().equals("Married")&&data[1].trim().equals("Male")){
                FatherNameTv.setText("Father's Name");
                FatherAgeTv.setText("Father's Age");
                GrandFatheName.setText("Grand father's Name");
                GrandFahterAge.setText("Grand father's Age");
                FamliyName.setText("Wife's Name");

                FatherAge.setHint("Father's Age");
                FatherName.setHint("Father's Name");
                GrandFatherName.setHint("Grand father's Name");
                GrandFatherAge.setHint("Grand father's Age");
                famliy_name.setHint("Wife's Name");
//                ((LinearLayout) findViewById(R.id.family_details)).setVisibility(View.GONE);
            }
            if(data[3].trim().equals("Married")&&data[1].trim().equals("Female")){
                FatherNameTv.setText("Husband Name");
                FatherAgeTv.setText("Husband Age");
                GrandFatheName.setText("Father in law Name");
                GrandFahterAge.setText("Father in law Age");
                FamliyName.setText("Father's Name");

                FatherAge.setHint("Husband Age");
                FatherName.setHint("Husband Name");
                GrandFatherName.setHint("Father in law Name");
                GrandFatherAge.setHint("Father in law Age");
                famliy_name.setHint("Father's Name");
//                ((LinearLayout) findViewById(R.id.family_details)).setVisibility(View.VISIBLE);
            }
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
            LinearLayout Business = (LinearLayout) findViewById(R.id.business_address);
            LinearLayout Govt_Service = (LinearLayout) findViewById(R.id.govt_service);
            LinearLayout PrivateService = (LinearLayout) findViewById(R.id.private_service);
            LinearLayout Student = (LinearLayout) findViewById(R.id.student_info);
            Business.setVisibility(View.GONE);
            Govt_Service.setVisibility(View.GONE);
            PrivateService.setVisibility(View.GONE);
            Student.setVisibility(View.GONE);

            if(data[30].trim().equals("Business")){
                Business.setVisibility(View.VISIBLE);
            }else if(data[30].trim().equals("Govt. Service")){
                Govt_Service.setVisibility(View.VISIBLE);
            }else if(data[30].trim().equals("Private")){
                PrivateService.setVisibility(View.VISIBLE);
            }else if(data[30].trim().equals("Student")){
                Student.setVisibility(View.VISIBLE);
            }
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
            mobile_num.setText(data[48]);
//            HouseWifeStatus.setText(data[46]);

            imageLoader.displayImage("http://ghanchidarpan.org/news/images/" + data[47].trim() + ".jpg", CameraAct);
            urlImage = "http://ghanchidarpan.org/news/images/" + data[47].trim() + ".jpg";
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
    @Override
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

}
