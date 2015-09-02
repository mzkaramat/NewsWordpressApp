package com.raoul.socailbase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.raoul.socailbase.fragment.DetailsFragment;
import com.raoul.socailbase.model.Event;
import com.raoul.socailbase.utill.GPSTracker;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Home extends Fragment {
	//this class is class that is include google map.
	public Home(){}
    MapView mapView;
    GoogleMap map;
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    Hashtable<String, Integer> table;
     // Might be null if Google Play services APK is not available.
    GPSTracker gps;
    ProgressDialog mProgressDialog;
    public List<Event> data = null;
    List<ParseObject> ob;
    private LocationManager locationManager;
    private String provider;
    View rootView;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        rootView  = inflater.inflate(R.layout.fragment_home, container, false);
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView)  rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        gps = new GPSTracker(getActivity());
        table = new Hashtable<String, Integer>();

        // check if GPS enabled
//        if(gps.canGetLocation()){
//
//            position_latitude = gps.getLatitude();
//            position_longitude = gps.getLongitude();
//
//            // \n is for new line
//            // Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//        }else{
//            // can't get location
//            // GPS or Network is not enabled
//            // Ask user to enable GPS/network in settings
//            gps.showSettingsAlert();
//        }
        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        // map.getUiSettings().setMyLocationButtonEnabled(false);
      //  map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls

        MapsInitializer.initialize(this.getActivity());


        // Updates the location and zoom of the MapView
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
//        map.animateCamera(cameraUpdate);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 15));
        //map.addMarker(new MarkerOptions().position(new LatLng(gps.getLatitude(), gps.getLongitude())).title("This is me!").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.man)));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int index = table.get(marker.getId());
                String id = data.get(index).getEventID();
////                DetailsEventActivity.parseObject = ob.get(index);
//                Intent in = new Intent(getActivity(), DetailsEventActivity.class);
//                in.putExtra("eventID",id);
//                startActivity(in);






                if (ParseUser.getCurrentUser().getObjectId().equals(data.get(index).getUserid())){

                    Intent zoom = new Intent(getActivity(), DetailsEventActivity.class);
                    zoom.putExtra("eventID", id);
                    startActivity(zoom);
                }

                else {


                    Intent zoom = new Intent(getActivity(), DetailsEventpublicActivity.class);
                    zoom.putExtra("eventID", id);
                    startActivity(zoom);

                }




               // Toast.makeText(getActivity(), "" + index , Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        new RemoteDataTask().execute();


        return  rootView;
    }



    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Message");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
            data = new ArrayList<Event>();

            try {
                ParseQuery<ParseObject> eventQuery = ParseQuery.getQuery("Event");
//                photosFromCurrentUserQuery.whereEqualTo("user", ParseUser.getCurrentUser());
//                photosFromCurrentUserQuery.whereExists("image");
                eventQuery.orderByDescending("createdAt");
                eventQuery.include("user");
                ob = eventQuery.find();
                for (ParseObject event : ob) {
                    // Locate images in flag column


                    final Event eventlist = new Event();
                    ParseUser eventuser=event.getParseUser("user");
                    ParseFile image = (ParseFile) eventuser.get("userphoto");
                    eventlist.setEventID(event.getObjectId());
                    eventlist.setEventNmae((String) event.get("name"));
                    eventlist.setEventContente((String) event.get("content"));
                    eventlist.setUserid(eventuser.getObjectId());
                    //eventlist.setEventLocation((String) event.get("location"));
                    eventlist.setEventPosition((String) event.get("position"));
                    eventlist.setEventTimestart((String) event.get("Starttime"));
                    eventlist.setEventTimeend((String) event.get("Endtime"));
                    eventlist.setPosition_latitude(event.getDouble("latitude"));
                    eventlist.setPosition_longitude(event.getDouble("longitude"));
                    if(image==null){

                        eventlist.setEventUserimage("");

                    }
                    else {
                        eventlist.setEventUserimage(image.getUrl());
                    }










                    data.add(eventlist);
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
            setDispalyEvent(data);

            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }

    public  void setDispalyEvent(List<Event> worldpopulationlist){


        List<Event> mapposition=worldpopulationlist;
        if(!(mapposition ==null)){

            for (int i=0;i<mapposition.size();i++){
                String content=mapposition.get(i).getEventPosition();
               Marker mark = map.addMarker(new MarkerOptions().position(new LatLng(mapposition.get(i).getPosition_latitude(), mapposition.get(i).getPosition_longitude())).title(mapposition.get(i).getEventNmae()).snippet(content).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.man)).draggable(true));
                table.put(mark.getId(), Integer.valueOf(i));
            }

        }

    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}