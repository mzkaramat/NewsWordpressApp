package com.raoul.socailbase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.raoul.socailbase.ImageLoadPackge.ImageLoader;
import com.raoul.socailbase.adapter.NavDrawerListAdapter;
import com.raoul.socailbase.fragment.EventLIstFragment;
import com.raoul.socailbase.model.Event;
import com.raoul.socailbase.model.Guest;
import com.raoul.socailbase.model.NavDrawerItem;

import java.util.ArrayList;
import java.util.List;


public class GuestActivity extends ActionBarActivity {
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
    ImageButton event;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private static final int LOGIN_REQUEST = 0;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    public static FragmentManager fragmentManager;
    ProgressDialog mProgressDialog;
    private GuestAdapter guestadapter;
    public List<Guest> data = null;
    ListView guestlistView;
    Button createevent;
    List<ParseObject> ob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        event=(ImageButton)findViewById(R.id.event_guest_imageButton);
        guestlistView=(ListView)findViewById(R.id.guest_listView);
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
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GuestActivity.this,EventActivity.class);
                startActivity(intent);
//                Fragment fragment=new EventLIstFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.content_event, fragment).commit();
            }
        });

        new RemoteDataTask().execute();

//        if (savedInstanceState == null) {
//            // on first time display view for first nav item
//            Fragment fragment=new EventLIstFragment();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_event, fragment).commit();
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
                    Intent intent=new Intent(GuestActivity.this,ProfileActivity.class);
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
                    .replace(R.id.content_event, fragment).commit();

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
        Intent intent=new Intent(GuestActivity.this,Base.class);
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


    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(GuestActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Message");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
//            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
            data = new ArrayList<Guest>();

            try {
                ParseQuery<ParseObject> eventQuery = ParseQuery.getQuery("Guest");
//                photosFromCurrentUserQuery.whereEqualTo("user", ParseUser.getCurrentUser());
//                photosFromCurrentUserQuery.whereExists("image");
                eventQuery.orderByDescending("createdAt");
                eventQuery.include("user");
                ob = eventQuery.find();
                for (ParseObject guest : ob) {
                    // Locate images in flag column

                     ArrayList<String>users=new ArrayList<>();
                     users= (ArrayList<String>) guest.get("users");
                     String userstring=users.toString();
                     if (userstring.contains(ParseUser.getCurrentUser().getObjectId())||guest.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                         final Guest guestlist = new Guest();
                    ParseUser eventuser=guest.getParseUser("user");
                         ParseFile image = (ParseFile) eventuser.get("userphoto");
                         guestlist.setUserid(eventuser.getObjectId());
                         guestlist.setUsername((String)eventuser.get("name"));
//                    guest.setUserid(eventuser.getObjectId());
//                    eventlist.setEventID(event.getObjectId());
//                    eventlist.setEventNmae((String) event.get("name"));
//                    eventlist.setEventContente((String) event.get("contente"));
//                    //eventlist.setEventLocation((String) event.get("location"));
//                    eventlist.setEventPosition((String) event.get("position"));
//                    eventlist.setEventTimestart((String) event.get("Starttime"));
//                    eventlist.setEventTimeend((String) event.get("Endtime"));
                         if(image==null){

                             guestlist.setImageurl("");

                         }
                         else {
                             guestlist.setImageurl(image.getUrl());
                         }










                         data.add(guestlist);
                     }

                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml

            // Pass the results into ListViewAdapter.java
           guestadapter = new GuestAdapter(GuestActivity.this,
                    data);
            // Binds the Adapter to the ListView
            guestlistView.setAdapter(guestadapter);
            guestlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                        data.get(position).getUserid();
                        Intent zoom = new Intent(GuestActivity.this, ProfileActivity.class);
                        zoom.putExtra("userid", data.get(position).getUserid());
                        startActivity(zoom);


//                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });
            // Close the progressdialog
//            mProgressDialog.dismiss();
        }
    }


    public class GuestAdapter extends BaseAdapter {
        boolean flag = true;
        Context context;
        LayoutInflater inflater;
        ImageLoader imageLoader;
        private ParseFile image;
        private List<Guest> worldpopulationlist = null;
        private ArrayList<Guest> arraylist;

        /**
         * Constructor from a list of items
         */
        public GuestAdapter(Context context, List<Guest> worldpopulationlist) {

            this.context = context;
            this.worldpopulationlist = worldpopulationlist;
            inflater = LayoutInflater.from(context);
            this.arraylist = new ArrayList<Guest>();
            this.arraylist.addAll(worldpopulationlist);
            imageLoader = new ImageLoader(context);
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;

            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.guesglayout, null);

                holder.userimageview = (ImageView) view.findViewById(R.id.userphoto_imageView);
                holder.eventnametextview=(TextView)view.findViewById(R.id.username_textView);


                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            if(worldpopulationlist.get(position).getImageurl().equals("")){
                holder.userimageview.setImageResource(R.drawable.people);
            }
            else {

                imageLoader.DisplayImage(worldpopulationlist.get(position).getImageurl(), holder.userimageview);
            }
            holder.eventnametextview.setText(worldpopulationlist.get(position).getUsername());



            // Restore the checked state properly
            final ListView lv = (ListView) parent;
//        holder.layout.setChecked(lv.isItemChecked(position));

            return view;
        }

        @Override
        public int getCount() {
            return worldpopulationlist.size();
        }

        @Override
        public Object getItem(int position) {
            return worldpopulationlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {

            public ImageView userimageview;
            public TextView  eventnametextview;


        }
    }



//    private ActionBar getActionBar() {
//        return ((ActionBarActivity) getActivity()).getSupportActionBar();
//    }
}
