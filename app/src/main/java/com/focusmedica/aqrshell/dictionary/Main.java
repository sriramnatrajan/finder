package com.focusmedica.aqrshell.dictionary;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.focusmedica.aqrshell.DataModel;
import com.focusmedica.aqrshell.InfoActivity;
import com.focusmedica.aqrshell.R;
import com.focusmedica.aqrshell.dbHandler.SQLiteHandler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Main extends Activity implements  Sorting.OnHeadlineSelectedListener{

    ImageView buy;
    static final String TAG = "FMI";
    static String SKU_PREMIUM;
    static final int RC_REQUEST = 10001;
    String thumb;
    Mydatabase handler;

    ArrayList<DIctionaryContent> AppDetails=new ArrayList<>();
    ArrayList<DIctionaryContent> videoDetails=new ArrayList<>();
    TextView mTextView;
      String  url;
    String  filterVideoName;

    Content c;Context mContext;
     String name;
    View mCustomView;
    Content mContent;
    ListView Lview;
    Content.ListAdapter adapter;
    DIctionaryContent content;
    Showpopup popup;
    Mydatabase mdatabase;
    List listcontent;
    Content.DownloadFileFromURL asyncobj ;
    String vdoname ;
    ProgressDialog pDialog;
    int img_position,flag=0;
    View mView;  String videofileName;
    String linkStr,titleName;
    ImageView iv_download;
    DataModel dataModel;
    SQLiteHandler mSqLiteHandler;
     String a0,a1,a2,a3; String thumbnail,b1_titleName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    mTextView=(TextView)findViewById(R.id.title);

    if(getResources().getBoolean(R.bool.portrait_only)){
     setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
        Intent i=getIntent();
          thumb=i.getStringExtra("thumbinfo");
        thumbnail=i.getStringExtra("Thumbnail");
        b1_titleName=i.getStringExtra("b1_titleName");
        Log.d("Thumbnail",thumbnail);
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
       mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        ImageView info=(ImageView)mCustomView.findViewById(R.id.imageView4);
        iv_download=(ImageView)mCustomView.findViewById(R.id.iv_download);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        handler=new Mydatabase(getApplicationContext(), "");
        AppDetails=handler.getAppDetail();
        content=AppDetails.get(0);
        mTextView.setText(content.getAppNme());
        url=content.getDlink();
        Log.d("URL File",content.getDlink());
        info.setOnClickListener(new View.OnClickListener() {
         @Override
        public void onClick(View view) {
          Intent in = new Intent(getApplicationContext(), InfoActivity.class);
          in.putExtra("Thumbnail",thumbnail);
          in.putExtra("b1",b1_titleName);
          startActivity(in);
            }
        });
     onArticleSelected("A");


    }

    @Override
    public void onArticleSelected(final String firstcharacter) {
        Content contfrag = (Content) getFragmentManager().findFragmentById(R.id.content);
        contfrag.test(firstcharacter);
        videoDetails = handler.getVideoFileName(firstcharacter);
        DIctionaryContent item = videoDetails.get(0);
        name = item.getVDOname();

        if (new File("data/data/com.focusmedica.aqrshell/files/" +name).exists()) {
            iv_download.setVisibility(View.INVISIBLE);

        }else {
            iv_download.setVisibility(View.VISIBLE);
        }

     try {
         videoDetails = handler.getVideoFileName(firstcharacter);

         name = item.getVDOname();
         if (!videoDetails.isEmpty()) {
             if (new File("data/data/com.focusmedica.aqrshell/files/" +name).exists()) {
                 iv_download.setVisibility(View.INVISIBLE);

             } else {
                 iv_download.setVisibility(View.VISIBLE);
             }if ((videoDetails.size()<=1)){
                 iv_download.setVisibility(View.INVISIBLE);
             }
         }
     }catch (IndexOutOfBoundsException e){
         e.printStackTrace();
         handler.close();
     }

        iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoDetails = handler.getVideoFileName(firstcharacter);
                if (!videoDetails.isEmpty()) {
                    for (int i = 0; i < videoDetails.size(); i++) {
                        DIctionaryContent item = videoDetails.get(i);
                        String name = item.getVDOname();
                        name = name.replaceAll(" ", "%20");
                        Log.d("File: ", url + name);
                        new DownloadFileFromURL(url + name, name).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
                handler.close();
             }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

      }

    void complain(String message) {

        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {

        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    public class DownloadFileFromURL extends AsyncTask<String, String, String> {
        private long total = 0L;
        private long lengthOfFile = 0L;
        private String mUrl;
        private String mName;
        public DownloadFileFromURL(String url, String name) {
            mUrl = url;
            mName = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           pDialog = new ProgressDialog(Main.this);
            pDialog.setMessage("Downloading.... Please wait...");
            pDialog.setMax(100);
            pDialog.setIndeterminate(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    flag = 1;
                }
            });
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.dismiss();
        }

        private void onDownloadCancelled() {

          pDialog.dismiss();
            File dir=getApplicationContext().getFilesDir();
            File file=new File(dir,filterVideoName);
            file.delete();
            //mListAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            if(flag==1) {
                onDownloadCancelled();
                flag=0;
            }else {
                Toast.makeText(getApplicationContext(), "Video Not Exist...", Toast.LENGTH_LONG).show();
                onDownloadCancelled();
            }
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(mUrl);
                URLConnection conection = url.openConnection();
                conection.setRequestProperty("Accept-Encoding", "Identity");
                conection.connect();
                lengthOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(getApplicationContext().getFilesDir()+"/"+ mName);
                Log.d("sriram", "output filepath: "+getApplicationContext().getFilesDir()+"/"+ mName);

                byte data[] = new byte[1024];
                while ((count = input.read(data)) != -1 && !isCancelled()) {
                if (isCancelled()) break;

                total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            while (pDialog.isShowing()){
                pDialog.dismiss();
            }
//            mAdapter.notifyDataSetChanged();
            if (!videoDetails.isEmpty()) {
                if (new File(getApplicationContext().getFilesDir()+"/"+ mName).exists()) {
                    if (iv_download.isClickable()) {
                    iv_download.setVisibility(View.INVISIBLE);
                    }
                    ImageView im=(ImageView)mCustomView.findViewById(R.id.iv_download);
                }
            }
            handler.close();
             if (!isCancelled()) {
            } else {
                onDownloadCancelled();
            }
        }

    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
         public class ListAdapter extends BaseAdapter implements View.OnClickListener{
        Context context;

        public ListAdapter(Context context,String firstchar){
            this.context = context;
            listcontent=handler.Get_ContactDetails(firstchar);
        }

        @Override
        public int getCount() {
            return listcontent.size();
        }

        @Override
        public Object getItem(int position) {
            return listcontent.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onClick(View view) {
            view.getId();
        }

        final class ViewHolder {

            ImageView img;

            TextView title,content;
        }
        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

          final   Main.ListAdapter.ViewHolder viewHolder;
            if(view == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_list,null);
                viewHolder = new Main .ListAdapter.ViewHolder();
                viewHolder.title = (TextView) view.findViewById(R.id.textView);
                viewHolder.content = (TextView) view.findViewById(R.id.textView2);
                viewHolder.img=(ImageView)view.findViewById(R.id.imageView);

                view.setTag(viewHolder);
            }else{
                viewHolder = (Main .ListAdapter.ViewHolder) view.getTag();
            }

            if(position%2==0){
                view.setBackgroundResource(R.drawable.row_even);
            }else{
                view.setBackgroundResource(R.drawable.row_odd);
            }

            content=(DIctionaryContent)listcontent.get(position);

            viewHolder.title.setText(content.getTitle());
            viewHolder.content.setText(content.getDescription());
            viewHolder.img.setTag(content.getVDOname());
            viewHolder.img.setId(position);
            String lower_case=content.getVDOname().toLowerCase().substring(0, content.getVDOname().indexOf(".")).replaceAll(" ", "").replaceAll("-","");


            if (new File("data/data/com.focusmedica.aqrshell/files/" + content.getVDOname()).exists()) {
                iv_download.setVisibility(View.INVISIBLE);
                viewHolder.img.setImageResource(R.drawable.play_pressed);
                viewHolder.title.setTextColor(Color.parseColor("#ffffff"));
                viewHolder.content.setTextColor(Color.parseColor("#ffffff"));

            }else if ( getApplicationContext().getResources().getIdentifier(lower_case,
                    "raw",getApplicationContext().getPackageName())!=0) {
                iv_download.setVisibility(View.INVISIBLE);
                viewHolder.img.setImageResource(R.drawable.play_pressed);
                viewHolder.title.setTextColor(Color.parseColor("#ffffff"));
                viewHolder.content.setTextColor(Color.parseColor("#ffffff"));
            }
            else {

                viewHolder.img.setImageResource(R.drawable.download_enable);
                viewHolder.title.setTextColor(Color.parseColor("#ffffff"));
                viewHolder.content.setTextColor(Color.parseColor("#ffffff"));
            }

            viewHolder.img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    vdoname=viewHolder.img.getTag().toString();
                    if(viewHolder.img.getDrawable().getConstantState().equals(ContextCompat.getDrawable(context, R.drawable.download_enable).getConstantState())){
                        if(!haveNetworkConnection()){
                            Toast.makeText(getApplicationContext(), "No internet connection available", Toast.LENGTH_LONG).show();
                            return;
                        }
                     //   asyncobj = new Main.DownloadFileFromURL(url,name);
                        videofileName =vdoname.replaceAll(" ", "%20");
                        img_position=viewHolder.img.getId();
                        pDialog = new ProgressDialog(getApplicationContext());
                        pDialog.setMessage("Downloading.... Please wait...");
                        pDialog.setMax(100);
                        pDialog.setIndeterminate(false);
                        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                asyncobj.cancel(true);
                                viewHolder.img.setImageResource(R.drawable.download_enable);
                                flag = 1;
                            }
                        });
                        pDialog.setCancelable(false);
                        pDialog.setCanceledOnTouchOutside(false);
                        System.out.println("checkurl:"+videofileName);

                        AppDetails=mdatabase.getAppDetail();
                        content=AppDetails.get(0);
                        linkStr=content.getDlink();
                        asyncobj.execute(linkStr+videofileName);

                    }else if(viewHolder.img.getDrawable().getConstantState().equals
                            (ContextCompat.getDrawable(context, R.drawable.play_pressed).getConstantState())){

                        Intent intent = new Intent(getApplicationContext(),VideoPlay2Activity.class);
                        intent.putExtra("vdoname",vdoname);
                        startActivity(intent);

                    }else{
                    }
                }
            });
            mdatabase.close();
            return view;
        }
        public void updateData(){
            this.notifyDataSetChanged();
        }
    }
}
