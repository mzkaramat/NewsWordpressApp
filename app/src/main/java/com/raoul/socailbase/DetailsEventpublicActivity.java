package com.raoul.socailbase;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.raoul.socailbase.adapter.NavDrawerListAdapter;
import com.raoul.socailbase.fragment.DetailsFragment;
import com.raoul.socailbase.model.Event;
import com.raoul.socailbase.model.NavDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class DetailsEventpublicActivity extends ActionBarActivity {
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
    public static ParseObject parseObject;
    TextView event_time_textview;
    TextView event_location_textview;
    TextView event_description_textview;
    Button edit_button;
    Button cancle_button;
    ParseObject deleteobject;
    String eventID;
    ParseUser current_user;
    ParseUser event_user;
    ArrayList<String> users;
    List<ParseObject> ob;
    ParseObject Guest_ob;
    ParseObject My_ob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_other);
        eventbut=(ImageButton)findViewById(R.id.event_imageButton);
        eventbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailsEventpublicActivity.this,EventActivity.class);
                startActivity(intent);
            }
        });
        users=new ArrayList<String>();
        Bundle b =getIntent().getExtras();
        eventID = b.getString("eventID");
//        deleteobject = DetailsEventActivity.parseObject;
//        Toast.makeText(getActivity(),eventID,Toast.LENGTH_LONG).show();
        current_user=ParseUser.getCurrentUser();
        event_time_textview=(TextView)findViewById(R.id.event_time_textView);
        event_location_textview=(TextView)findViewById(R.id.event_location_textView);
        event_description_textview=(TextView)findViewById(R.id.event_content_textView);
        edit_button=(Button)findViewById(R.id.join_button);
        cancle_button=(Button)findViewById(R.id.guest_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
                query.include("user");
                query.getInBackground(eventID, new GetCallback<ParseObject>() {
                    public void done(ParseObject event, ParseException e) {
                        if (e == null) {
                            deleteobject=event;
                            users= (ArrayList<String>) deleteobject.get("users");
                            users.add(ParseUser.getCurrentUser().getObjectId());
                            deleteobject.put("users",users);
                            try {
                                deleteobject.save();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }



//                    ParseFile image = (ParseFile) country.get("image");
//                    image.saveInBackground();
//                    photo = country;
                            ParseUser touser = deleteobject.getParseUser("user");
                            //



                            try {
                                ParseQuery<ParseObject> eventQuery = ParseQuery.getQuery("Guest");
//                photosFromCurrentUserQuery.whereEqualTo("user", ParseUser.getCurrentUser());
//                photosFromCurrentUserQuery.whereExists("image");
                                eventQuery.orderByDescending("createdAt");
                                eventQuery.include("user");
                                ob = eventQuery.find();
                                for (ParseObject eventlist : ob) {
                                    // Locate images in flag column



                                        ParseUser eventuser=eventlist.getParseUser("user");

                                        if (eventuser.getObjectId().equals(touser.getObjectId())){
                                            Guest_ob=eventlist;

                                            ArrayList<String> follusers=new ArrayList<>();
                                            follusers= (ArrayList<String>) Guest_ob.get("users");


                                            follusers.add(ParseUser.getCurrentUser().getObjectId());
//                                            Toast.makeText(DetailsEventpublicActivity.this,follusers.toString(),Toast.LENGTH_LONG).show();
                                            Guest_ob.put("users",follusers);
                                            Guest_ob.save();

                                        }
                                    if (eventuser.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                                        My_ob=eventlist;

                                        ArrayList<String> follusers=new ArrayList<>();
                                        follusers= (ArrayList<String>)  My_ob.get("users");


                                        follusers.add(touser.getObjectId());
//                                            Toast.makeText(DetailsEventpublicActivity.this,follusers.toString(),Toast.LENGTH_LONG).show();
                                        My_ob.put("users",follusers);
                                        My_ob .save();

                                    }



                                    }


                            } catch (ParseException ex) {
                                Log.e("Error", e.getMessage());
                                ex.printStackTrace();
                            }


                            //
                            Toast.makeText(DetailsEventpublicActivity.this,"Event Join is succefully saved!",Toast.LENGTH_LONG).show();
//                    imageLoader.DisplayImage(image.getUrl(), commentimageview);

                        }

                    }
                });




//
//                ParseQuery<ParseObject> guestquery = ParseQuery.getQuery("Guest");
//                guestquery.include("user");
//                guestquery.getInBackground(eventID, new GetCallback<ParseObject>() {
//                    public void done(ParseObject event, ParseException e) {
//                        if (e == null) {
//                            deleteobject=event;
//                            event_user=event.getParseUser("user");
////                      if (!(event_user.getObjectId().equals(current_user.getObjectId()))){
////
////                          edit_button.setVisibility(View.GONE);
////                          cancle_button.setVisibility(View.GONE);
////                      }
//                            event_time_textview.setText((String)event.get("Starttime")+" - "+(String)event.get("Endtime"));
//                            event_location_textview.setText((String) event.get("position"));
//                            event_description_textview.setText((String)event.get("content"));
////                    ParseFile image = (ParseFile) country.get("image");
////                    image.saveInBackground();
////                    photo = country;
////                    touser = country.getParseUser("user");
////                    imageLoader.DisplayImage(image.getUrl(), commentimageview);
//
//                        }
//
//                    }
//                });



            }
        });
        cancle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                deleteobject.deleteInBackground();
                Intent intent=new Intent(DetailsEventpublicActivity.this,GuestActivity.class);
                startActivity(intent);
            }
        });
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.include("user");
        query.getInBackground(eventID, new GetCallback<ParseObject>() {
            public void done(ParseObject event, ParseException e) {
                if (e == null) {
                    deleteobject=event;
                    event_user=event.getParseUser("user");
//                      if (!(event_user.getObjectId().equals(current_user.getObjectId()))){
//
//                          edit_button.setVisibility(View.GONE);
//                          cancle_button.setVisibility(View.GONE);
//                      }
                    event_time_textview.setText((String)event.get("Starttime")+" - "+(String)event.get("Endtime"));
                    event_location_textview.setText((String) event.get("position"));
                    event_description_textview.setText((String)event.get("content"));
//                    ParseFile image = (ParseFile) country.get("image");
//                    image.saveInBackground();
//                    photo = country;
//                    touser = country.getParseUser("user");
//                    imageLoader.DisplayImage(image.getUrl(), commentimageview);

                }

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        fragmentManager = getSupportFragmentManager();
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
        actionBar = getSupportActionBar();
        // enabling action bar app icon and behaving it as toggle button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

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
//            Fragment fragment=new DetailsFragment();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_details, fragment).commit();
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
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Home();
                break;
            case 1:
                Intent intent=new Intent(DetailsEventpublicActivity.this,ProfileActivity.class);
                intent.putExtra("myprofile","yes");
                startActivity(intent);
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

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_details, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
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
        Intent intent=new Intent(DetailsEventpublicActivity.this,Base.class);
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
}
