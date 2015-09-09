package com.example.administrator.newsexplorer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.newsexplorer.R;
import com.example.administrator.newsexplorer.model.MemberModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by Administrator on 9/9/2015.
 */
public class MemberListAdapter  extends ArrayAdapter<MemberModel> {

    public final Context context;
    private final List<MemberModel> values;
    ImageLoader imageLoader;

    public MemberListAdapter(Context context, List<MemberModel>  values) {
        super(context, R.layout.member_list_item, values);
        this.context = context;
        this.values = values;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.member_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        textView.setText(values.get(position).name);
        imageLoader.displayImage("http://ghanchidarpan.org/news/images/" + values.get(position).id + ".jpg", imageView);
        // Change icon based on name
      //  String s = values[position];

//        System.out.println(s);
//
//        if (s.equals("WindowsMobile")) {
//            imageView.setImageResource(R.drawable.windowsmobile_logo);
//        } else if (s.equals("iOS")) {
//            imageView.setImageResource(R.drawable.ios_logo);
//        } else if (s.equals("Blackberry")) {
//            imageView.setImageResource(R.drawable.blackberry_logo);
//        } else {
//            imageView.setImageResource(R.drawable.android_logo);
//        }

        return rowView;
    }

}