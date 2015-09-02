package com.raoul.socailbase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.raoul.socailbase.ImageLoadPackge.ImageLoader;
import com.raoul.socailbase.adapter.NavDrawerListAdapter;
import com.raoul.socailbase.fragment.CreateventFragment;
import com.raoul.socailbase.model.NavDrawerItem;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class ProfileActivity extends ActionBarActivity {
    //This class is class that is include slider menu.
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    ParseUser currentUser;
    // nav drawer title
    private CharSequence mDrawerTitle;
    ActionBar actionBar;
    // used to store app title
    private CharSequence mTitle;
    ImageButton eventbut;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private static final int LOGIN_REQUEST = 0;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    public static FragmentManager fragmentManager;
    String photo;


    ImageButton upgrade;
    Button joinbutton;
    EditText dispalsyname_textview;
    EditText name_textview;
    EditText age_textview;
    EditText about_textview;
    String displayname;
    String name;
    String age;
    String about;
    String userid;
    String myprofile;
    ImageLoader imageLoader;
    ImageView profile_picuture;
    String fileuri;
    Bitmap bitmap;
    byte[] saveData;
    ParseFile images;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfprofile);
        Intent intent=getIntent();
        photo=intent.getStringExtra("takephoto");
        userid=intent.getStringExtra("userid");
        myprofile=intent.getStringExtra("myprofile");
        //
        fileuri=intent.getStringExtra("serchresult");
        imageLoader=new ImageLoader(this);
        ParseUser currentuser=ParseUser.getCurrentUser();
        ParseFile image=currentuser.getParseFile("userphoto");
        joinbutton=(Button)findViewById(R.id.join_button);
        upgrade=(ImageButton)findViewById(R.id.edit_profile_imageButton);
        dispalsyname_textview=(EditText)findViewById(R.id.username_editText);
        name_textview=(EditText)findViewById(R.id.firstprotile_editText);
        age_textview=(EditText)findViewById(R.id.age_editText);
        about_textview=(EditText)findViewById(R.id.about_editText);
        profile_picuture=(ImageView)findViewById(R.id.profile_picture_imageview);
        //display image




        if(!(fileuri ==null)){

            setProgressDialog( ProgressDialog.show(this, "",
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


        if (!(userid ==null) &&!(userid .equals(ParseUser.getCurrentUser().getObjectId()))){

            upgrade.setVisibility(View.GONE);
            name_textview.setEnabled(false);
            age_textview.setEnabled(false);
            about_textview.setEnabled(false);
            dispalsyname_textview.setEnabled(false);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");

            query.getInBackground(userid, new GetCallback<ParseObject>() {
                public void done(ParseObject event, ParseException e) {
                    if (e == null) {
                        if (!(event.get("DisplayName") == null)) {

                            displayname = (String) event.get("DisplayName");
                        } else {

                            displayname = "";
                        }
                        if (!(event.get("name") == null)) {

                            name = (String) event.get("name");
                        } else {

                            name = "undefined";
                        }
                        if (!(event.get("age") == null)) {

                            age = (String) event.get("age");
                        } else {

                            age = "";
                        }

                        if (!(event.get("about") == null)) {

                            about = (String) event.get("about");
                        } else {

                            about = "";
                        }
                        name_textview.setText(name);
                        age_textview.setText(age);
                        about_textview.setText(about);
                        dispalsyname_textview.setText(displayname);
//                        event_user=event.getParseUser("user");
////                      if (!(event_user.getObjectId().equals(current_user.getObjectId()))){
////
////                          edit_button.setVisibility(View.GONE);
////                          cancle_button.setVisibility(View.GONE);
////                      }
//                        event_time_textview.setText((String)event.get("Starttime")+" - "+(String)event.get("Endtime"));
//                        event_location_textview.setText((String) event.get("position"));
//                        event_description_textview.setText((String)event.get("content"));
                        ParseFile image = (ParseFile) event.get("userphoto");
                        if (!(image == null)) {
                            imageLoader.DisplayImage(image.getUrl(), profile_picuture);
                        }
////                    image.saveInBackground();
////                    photo = country;
////                    touser = country.getParseUser("user");
////                    imageLoader.DisplayImage(image.getUrl(), commentimageview);

                    }

                }
            });

        }
        else if (!(myprofile ==null) &&myprofile.equals("yes")){

            joinbutton.setVisibility(View.GONE);
            if(!(image ==null)){
                imageLoader.DisplayImage(image.getUrl(), profile_picuture);
            }
            else if(image==null){

                Session session = ParseFacebookUtils.getSession();
                if (session != null && session.isOpened()) {
                    makeMeRequest();
                }

            }
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

        }

        else {
            joinbutton.setVisibility(View.GONE);
            if(!(image ==null)){
                imageLoader.DisplayImage(image.getUrl(), profile_picuture);
            }
            else if(image==null){

                Session session = ParseFacebookUtils.getSession();
                if (session != null && session.isOpened()) {
                    makeMeRequest();
                }

            }
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



        }







        //

        profile_picuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ParseUser.getCurrentUser().getObjectId().equals(userid)){
                    Intent intent =new Intent(ProfileActivity.this,CameraActivity.class);
                    startActivity(intent);
                }
                else if (!(myprofile ==null) &&myprofile.equals("yes")){

                    Intent intent =new Intent(ProfileActivity.this,CameraActivity.class);
                    startActivity(intent);
                }

            }
        });

        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setProgressDialog( ProgressDialog.show(ProfileActivity.this, "",
                        "uploading  data ...", true, true) );
                ParseUser currentuser=ParseUser.getCurrentUser();
                currentuser.put("name",name_textview.getText().toString());
                currentuser.put("age",age_textview.getText().toString());
                currentuser.put("DisplayName",dispalsyname_textview.getText().toString());
                currentuser.put("about",about_textview.getText().toString());
                currentuser.saveInBackground(new SaveCallback() {

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



        //

//        eventbut=(ImageButton)findViewById(R.id.event_imageButton);
//        eventbut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(ProfileActivity.this,EventActivity.class);
//                startActivity(intent);
//            }
//        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


//        fragmentManager = getSupportFragmentManager();
        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here



        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
//        actionBar = getSupportActionBar();
//        // enabling action bar app icon and behaving it as toggle button
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                //actionBar.setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                // actionBar.setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

//        if (savedInstanceState == null) {
//            // on first time display view for first nav item
//            Fragment fragment=new Profile();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_myself, fragment).commit();
//        }
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
//        Fragment fragment = null;
        switch (position) {
            case 0:
                  Intent intent=new Intent(ProfileActivity.this,Base.class);
                  startActivity(intent);
                break;
            case 1:
//                fragment = new Profile();
                break;
//            case 2:
//                fragment = new Message();
//                break;
//            case 3:
//                fragment = new Setting();
//                break;
            case 4:
                onLogoutButtonClicked();
                break;


            default:
                break;
        }

//        if (fragment != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_create, fragment).commit();
//
//            // update selected item and title, then close the drawer
//            mDrawerList.setItemChecked(position, true);
//            mDrawerList.setSelection(position);
//            setTitle(navMenuTitles[position]);
//            mDrawerLayout.closeDrawer(mDrawerList);
//        } else {
//            // error in creating fragment
//            Log.e("MainActivity", "Error in creating fragment");
//        }
    }
    private void onLogoutButtonClicked() {
        // close this user's session
        ParseUser curentusername=ParseUser.getCurrentUser();
        if (!(curentusername == null)) {
            ParseFacebookUtils.getSession().closeAndClearTokenInformation();
            ParseUser.logOut();
        } else {
            ParseUser.logOut();
        }
        Intent intent=new Intent(ProfileActivity.this,Base.class);
        startActivity(intent);
        // Log the user out

        // Go to the login view

    }
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        actionBar.setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
//    private ActionBar getActionBar() {
//        return ((ActionBarActivity) getActivity()).getSupportActionBar();
//    }


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
