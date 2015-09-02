package com.raoul.socailbase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.raoul.socailbase.ImageLoadPackge.ImageLoader;
import com.raoul.socailbase.model.Adress;
import com.raoul.socailbase.model.Event;
import com.raoul.socailbase.utill.GPSTracker;
import com.raoul.socailbase.utill.HttpResponseData;
import com.raoul.socailbase.utill.HttpUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AdressSearchActivity extends ActionBarActivity {
    Button search_but;
    EditText search_content;
    Button back_but;
    ListView address_listview;

    ProgressDialog mProgressDialog;
    private Adressdapter adapter;
    public List<Adress> data = null;
    public static final String GOOGLE_PLACES_AUTOCOMPLETE = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=%s&types=%s&types=(cities)&components=country:us&key=AIzaSyD478Y5RvbosbO4s34uRaukMwiPkBxJi5A";
    public static final String PLACES_API_DATA_RESULT_TYPE_GEOCODE = "geocode";
    GPSTracker gps;
    Double position_latitude;
    Double position_longitude;
    String address;
    String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress_search);
//        search_but=(Button)findViewById(R.id.search_button);
        search_content=(EditText)findViewById(R.id.search_editText);
//        back_but=(Button)findViewById(R.id.back_button);
        address_listview=(ListView)findViewById(R.id.address_listView);

        gps = new GPSTracker(this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            position_latitude = gps.getLatitude();
            position_longitude = gps.getLongitude();



            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(position_latitude, position_longitude, 1);
                address = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getAddressLine(1);
                String country = addresses.get(0).getAddressLine(2);
            } catch (IOException e) {
                e.printStackTrace();
            }

            search_content.setText(address+","+city);

            // \n is for new line
            // Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


//        search_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String location = search_content.getText().toString();
//
//                if(location!=null && !location.equals("")){
//                    searchCity(location);
//                }
//
//
//            }
//        });



        search_content.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String location = search_content.getText().toString();

                if(location!=null && !location.equals("")){
                    searchCity(location);
//                    new GeocoderTask().execute(location);
                }
            }
        });



    }


    private void searchCity(String key) {
        final String searchKey = key.replace(" ", "+");
        data = new ArrayList<Adress>();

        new Thread() {
            public void run() {
                String reqUrl = String.format(
                        GOOGLE_PLACES_AUTOCOMPLETE, searchKey,
                        PLACES_API_DATA_RESULT_TYPE_GEOCODE);
                HttpUtility httpUtility = new HttpUtility();
                HttpResponseData responseData = httpUtility.sendGet(reqUrl);
                if (responseData != null && responseData.getStatusCode() == 200) {
                    try {
                        JSONObject jsonData = new JSONObject(
                                responseData.getResponseContent());
                        JSONArray predictions = jsonData
                                .getJSONArray("predictions");
                        data.clear();
                        JSONObject prediction;
                        for (int i = 0; i < predictions.length(); i++) {

                            final Adress adressdata=new Adress();
                            prediction = predictions.getJSONObject(i);
                            String fullAddress = prediction.getString("description");
                            int range = fullAddress.lastIndexOf(",");
                            adressdata.setAdress(fullAddress.substring(0, range));
                            Log.d("getData",fullAddress.substring(0, range));
                            data.add(adressdata);
                        }

                        for (int i=0;i<data.size();i++){



                            Log.d("adsfadfsdfadfasdf",data.get(i).getAdress());



                        }
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                adapter = new Adressdapter(AdressSearchActivity.this,
                                        data);
                                // Binds the Adapter to the ListView
                                address_listview.setAdapter(adapter);


                                address_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position, long id) {
                                        data.get(position).getAdress();
                                        Intent zoom = new Intent(AdressSearchActivity.this, CreateEventActivity.class);
                                        zoom.putExtra("address", data.get(position).getAdress());
                                        startActivity(zoom);


//                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    }
                                });

                            }
                        });
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        // e.printStackTrace();
                    }
                }
            }
        }.start();
    }





    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            mProgressDialog = new ProgressDialog(AdressSearchActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Message");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
            else{

                Adress adressdata=new Adress();
                data = new ArrayList<Adress>();
                // Adding Markers on Google Map for each matching address
                for(int i=0;i<addresses.size();i++){

                    Address address = (Address) addresses.get(i);

                    // Creating an instance of GeoPoint, to display in Google Map
//                latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    String addressText = String.format("%s, %s",
                            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                            address.getCountryName());
                    adressdata.setAdress(address.getAddressLine(0));
                    Log.d("getadress",addressText);
                    data.add(adressdata);
//                markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                markerOptions.title(addressText);
//
//                googleMap.addMarker(markerOptions);

                    // Locate the first location

                }


                adapter = new Adressdapter(AdressSearchActivity.this,
                        data);
                // Binds the Adapter to the ListView
                 address_listview.setAdapter(adapter);
                mProgressDialog.dismiss();

            }
            // Clears all the existing markers on the map


//            eventlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//                    data.get(position).getEventID();
//                    Intent zoom = new Intent(getActivity(), DetailsEventActivity.class);
//                    zoom.putExtra("eventID", data.get(position).getEventID());
//                    startActivity(zoom);
//
//
////                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                }
//            });
            // Close the progressdialog



        }
    }


  public   class Adressdapter extends BaseAdapter {
        boolean flag = true;
        Context context;
        LayoutInflater inflater;
        ImageLoader imageLoader;
        private ParseFile image;
        private List<Adress> worldpopulationlist = null;
        private ArrayList<Adress> arraylist;

        /**
         * Constructor from a list of items
         */
        public Adressdapter(Context context, List<Adress> worldpopulationlist) {

            this.context = context;
            this.worldpopulationlist = worldpopulationlist;
            inflater = LayoutInflater.from(context);
            this.arraylist = new ArrayList<Adress>();
            this.arraylist.addAll(worldpopulationlist);

        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;

            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.adress_item_layout, null);

//            holder.userimageview = (ImageView) view.findViewById(R.id.event_imageView);
//            holder.eventnametextview=(TextView)view.findViewById(R.id.event_name_textView);
                holder.adress_name_textview=(TextView)view.findViewById(R.id.adress_name_textView);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }


            holder.adress_name_textview.setText(worldpopulationlist.get(position).getAdress());



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

            //        public ImageView userimageview;
//        public TextView  eventnametextview;
            public TextView  adress_name_textview;

        }
    }




}







