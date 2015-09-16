package com.xeaphii.ghanchi.darpan.adapter;


        import android.graphics.Bitmap;
        import android.net.ConnectivityManager;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.net.NetworkInfo;
        import android.os.AsyncTask;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.xeaphii.ghanchi.darpan.R;
        import com.xeaphii.ghanchi.darpan.StorageSharedPref;
        import com.xeaphii.ghanchi.darpan.model.MemberModel;
        import com.nostra13.universalimageloader.core.DisplayImageOptions;
        import com.nostra13.universalimageloader.core.ImageLoader;
        import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
        import com.nostra13.universalimageloader.core.assist.FailReason;
        import com.nostra13.universalimageloader.core.imageaware.ImageAware;
        import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
        import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.util.EntityUtils;

        import java.io.IOException;
        import java.util.List;

/**
 * Created by Administrator on 9/9/2015.
 */
public class MemberListAdapter  extends ArrayAdapter<MemberModel> {

    public final Context context;
    private final List<MemberModel> values;
    ImageLoader imageLoader;
    StorageSharedPref sharedStorage;
    public boolean IsMoreData = true;
    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
            .cacheOnDisc(true).resetViewBeforeLoading(true).build();

    public MemberListAdapter(Context context, List<MemberModel>  values) {
        super(context, R.layout.member_list_item, values);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        this.context = context;
        this.values = values;

        sharedStorage = new StorageSharedPref(context);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

//        isProcessed = new ArrayList<>();
//        for(int i = 0 ; i < values.size();i++){
//            isProcessed.add(false);
//        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
                      LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.member_list_item,null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.label);
            holder.city = (TextView) convertView.findViewById(R.id.city);
            holder.imageView = (ImageView) convertView.findViewById(R.id.logo);

            convertView.setTag(holder);

        } else {
                holder = (ViewHolder) convertView.getTag();

        }
        holder = (ViewHolder) convertView.getTag();


            holder.textView.setText(values.get(position).name);
             holder.city.setText("City: "+values.get(position).city);
           // holder.imageView.setTag("http://ghanchidarpan.org/news/images/" + values.get(position).id + ".jpg");
        if (holder.imageView.getTag() == null ||
                !holder.imageView.getTag().equals("http://ghanchidarpan.org/news/images/" + values.get(position).id + ".jpg")) {
            ImageAware imageAware = new ImageViewAware(holder.imageView, false);
            imageLoader.displayImage("http://ghanchidarpan.org/news/images/" + values.get(position).id + ".jpg", imageAware, options, new ImageLoadingListener() {



                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    ((ImageView) view).setBackgroundResource(R.drawable.user);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    ((ImageView) view).setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }


            });
            holder.imageView.setTag("http://ghanchidarpan.org/news/images/" + values.get(position).id + ".jpg");
        }
//            isProcessed.set(position,true);

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
        if(IsMoreData&&position == values.size()-1){
            if (isNetworkAvailable()) {
                new uploadToServer().execute(new String[]{values.get(position).id});} else {
                Toast.makeText(context, "No network present", Toast.LENGTH_LONG).show();

            }
        }
        return convertView;
    }
    class uploadToServer extends AsyncTask<String, Void, String> {

        private ProgressDialog pd = new ProgressDialog(context);
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Getting more members!");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {


            try {
                //------------------>>
                HttpGet httppost = new HttpGet(("http://ghanchidarpan.org/news/get_members.php?user_id=" +
                        encodeHTML(sharedStorage.GetPrefs("user_id","")+"" +
                                "&id_from=" +
                                params[0]) ).replaceAll(" ", "%20") );
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String resp = EntityUtils.toString(entity).trim();
                    if(resp.equals("")){
                        IsMoreData = false;
                    }else{
                        String[] data = resp.split(";",-1);
                        for(int i = 0 ; i < data.length;i++){
                            values.add(new MemberModel(data[i].split(":",-1)[1],data[i].split(":",-1)[0],data[i].split(":",-1)[2].trim(),data[i].split(":",-1)[3].trim()));
//                            isProcessed.add(false);
                        }
                    }

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
            notifyDataSetChanged();
        }
    }
    public static String encodeHTML(String s)
    {
        StringBuffer out = new StringBuffer();
        for(int i=0; i<s.length(); i++)
        {
            char c = s.charAt(i);
            if(c > 127 || c=='"' || c=='<' || c=='>')
            {
                out.append("&#"+(int)c+";");
            }
            else
            {
                out.append(c);
            }
        }
        return out.toString();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private class ViewHolder {
        TextView textView;
        ImageView imageView;
        TextView city;
    }

}
