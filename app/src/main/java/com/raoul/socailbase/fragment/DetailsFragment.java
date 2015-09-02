package com.raoul.socailbase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.raoul.socailbase.CreateEventActivity;
import com.raoul.socailbase.DetailsEventActivity;
import com.raoul.socailbase.EventActivity;
import com.raoul.socailbase.R;


public class DetailsFragment extends Fragment {
    TextView event_time_textview;
    TextView event_location_textview;
    TextView event_description_textview;
    Button edit_button;
    Button cancle_button;
    ParseObject deleteobject;
    String eventID;
    ParseUser current_user;
    ParseUser event_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        Bundle b = getActivity().getIntent().getExtras();
        eventID = b.getString("eventID");
//        deleteobject = DetailsEventActivity.parseObject;
//        Toast.makeText(getActivity(),eventID,Toast.LENGTH_LONG).show();
        current_user=ParseUser.getCurrentUser();
        event_time_textview=(TextView)v.findViewById(R.id.event_time_textView);
        event_location_textview=(TextView)v.findViewById(R.id.event_location_textView);
        event_description_textview=(TextView)v.findViewById(R.id.event_content_textView);
        edit_button=(Button)v.findViewById(R.id.edit_button);
        cancle_button=(Button)v.findViewById(R.id.cancle_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CreateEventActivity.class);
                intent.putExtra("eventID",eventID);
                startActivity(intent);
            }
        });
        cancle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteobject.deleteInBackground();
                Intent intent=new Intent(getActivity(),EventActivity.class);
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

        setRetainInstance(true);
        return v;
    }



}
