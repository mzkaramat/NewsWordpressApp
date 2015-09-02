package com.raoul.socailbase;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.raoul.socailbase.ImageLoadPackge.ImageLoader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Profile extends Fragment {
	ParseUser curentusername;
	public Profile(){}
	Button upgrade;
    EditText dispalsyname_textview;
    EditText name_textview;
    EditText age_textview;
    EditText about_textview;
    String displayname;
    String name;
    String age;
    String about;
    ImageLoader imageLoader;
    ImageView profile_picuture;
    String fileuri;
    Bitmap bitmap;
    byte[] saveData;
    ParseFile images;
    private ProgressDialog progressDialog;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Intent intent=getActivity().getIntent();
        fileuri=intent.getStringExtra("serchresult");
        imageLoader=new ImageLoader(getActivity());
        ParseUser currentuser=ParseUser.getCurrentUser();
        ParseFile image=currentuser.getParseFile("userphoto");
        upgrade=(Button)rootView.findViewById(R.id.join_button);
        dispalsyname_textview=(EditText)rootView.findViewById(R.id.username_editText);
        name_textview=(EditText)rootView.findViewById(R.id.first_editText);
        age_textview=(EditText)rootView.findViewById(R.id.age_editText);
        about_textview=(EditText)rootView.findViewById(R.id.about_editText);
        profile_picuture=(ImageView)rootView.findViewById(R.id.profile_picture_imageview);
        //display image




        if(!(fileuri ==null)){

            setProgressDialog( ProgressDialog.show(getActivity(), "",
                    "uploading  data ...", true, true) );


            BitmapFactory.Options options = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(fileuri,
                    options);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            saveData = bos.toByteArray();
            ParseUser user = ParseUser.getCurrentUser();
            if(!(saveData ==null)){
                images = new ParseFile("photo.jpg", saveData);

                images.saveInBackground();

                user.put("userphoto",images);
            }

            user.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException e) {
                    getProgressDialog().dismiss();

                    if (e == null) {

                    } else {

                    }
                }

            });

            profile_picuture.setImageBitmap(bitmap);

        }
        else if(!(image ==null)){
            imageLoader.DisplayImage(image.getUrl(), profile_picuture);
        }
        else if(image==null){

            Session session = ParseFacebookUtils.getSession();
            if (session != null && session.isOpened()) {
                makeMeRequest();
            }

        }

        //

        profile_picuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),CameraActivity.class);
                startActivity(intent);
            }
        });
        final ParseUser currentUser=ParseUser.getCurrentUser();
        if (!(currentUser.get("DisplayName") ==null)){

            displayname= (String) currentUser.get("DisplayName");
        }
        else {

            displayname="";
        }
        if (!(currentUser.get("name") ==null)){

            name= (String) currentUser.get("name");
        }
        else {

            name="undefined";
        }
        if (!(currentUser.get("age") ==null)){

            age= (String) currentUser.get("age");
        }
        else {

            age="";
        }

        if (!(currentUser.get("about") ==null)){

            about= (String) currentUser.get("about");
        }
        else {

            about="";
        }
        name_textview.setText(name);
        age_textview.setText(age);
        about_textview.setText(about);
        dispalsyname_textview.setText(displayname);
        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setProgressDialog( ProgressDialog.show(getActivity(), "",
                        "uploading  data ...", true, true) );
                currentUser.put("name",name_textview.getText().toString());
                currentUser.put("age",age_textview.getText().toString());
                currentUser.put("DisplayName",dispalsyname_textview.getText().toString());
                currentUser.put("about",about_textview.getText().toString());
                currentUser.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        getProgressDialog().dismiss();

//                        Intent intent=new Intent(getActivity(),EventActivity.class);
//                        startActivity(intent);

                        if (e == null) {

                        } else {
                            try {
                                //showDialog(e.getMessage());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }

                });
            }
        });
//        logout=(Button)rootView.findViewById(R.id.logout_button);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onLogoutButtonClicked();
//            }
//        });
//
        return rootView;
    }
    private void onLogoutButtonClicked() {
        // close this user's session
        curentusername=ParseUser.getCurrentUser();
        if (!(curentusername == null)) {
            ParseFacebookUtils.getSession().closeAndClearTokenInformation();
            ParseUser.logOut();
        } else {
            ParseUser.logOut();
        }

        // Log the user out

        // Go to the login view
        startLoginActivity();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    private void makeMeRequest() {
        Session session =  ParseFacebookUtils.getSession();

        Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

            @Override
            public void onCompleted(final GraphUser user, Response response) {


                if (user != null) {
//                                          imageLoader.DisplayImage("https://graph.facebook.com/"+user.getId()+"/picture?type=large",userphoto_imageview);
                    AsyncTask<Void, Void, Bitmap> t = new AsyncTask<Void, Void, Bitmap>() {
                        protected Bitmap doInBackground(Void... p) {
                            Bitmap bm = null;
                            try {
                                URL aURL = new URL("https://graph.facebook.com/" + user.getId() + "/picture?type=large");
                                URLConnection conn = aURL.openConnection();
                                conn.setUseCaches(true);
                                conn.connect();
                                InputStream is = conn.getInputStream();
                                BufferedInputStream bis = new BufferedInputStream(is);
                                bm = BitmapFactory.decodeStream(bis);
                                bis.close();
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return bm;
                        }

                        protected void onPostExecute(Bitmap bm) {

//                                                Drawable drawable = new BitmapDrawable(getResources(), bm);

                            profile_picuture.setImageBitmap(bm);


                        }
                    };
                    t.execute();


                }

            }
        });

    }
    public void setProgressDialog(ProgressDialog pd){
        progressDialog = pd;
    }
    public ProgressDialog getProgressDialog(){
        return progressDialog;
    }
}
