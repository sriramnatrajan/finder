
package com.focusmedica.aqrshell.dictionary;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.focusmedica.aqrshell.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class Content extends Fragment {

    ListView Lview;
    ListAdapter adapter;
    DIctionaryContent content;
    Showpopup popup;
    Mydatabase mdatabase;
    List listcontent;
    DownloadFileFromURL asyncobj ;
    String vdoname,firstchar;
    ProgressDialog pDialog;
    int img_position,flag=0;
 //   Boolean ispremium=true;
    View mView;  String videofileName;
    ArrayList<DIctionaryContent> AppDetails=new ArrayList<>();
    String linkStr;
    String name,description; ListAdapter.ViewHolder mViewHolder; String a0,a1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content, container, false);

        mdatabase = new Mydatabase(getActivity().getApplicationContext(), "");
        mdatabase.openDataBase();
      ;
        Lview=(ListView)rootView.findViewById(R.id.listView);
        adapter=new ListAdapter(getActivity(),"A");

        SharedPreferences appInfo = getActivity().getSharedPreferences("appInfo", getActivity().MODE_PRIVATE);
        //ispremium= appInfo.getBoolean("isPremium",false);
        Lview.setAdapter(adapter);
        Lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                content = (DIctionaryContent) adapter.getItem(i);
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popUpView = inflater.inflate(R.layout.popup_layout, null, false);
                final PopupWindow popup = new PopupWindow(popUpView, ViewGroup.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                popup.setContentView(popUpView);
                TextView title=(TextView)popUpView.findViewById(R.id.textView1);
                TextView  contentq=(TextView)popUpView.findViewById(R.id.textView2);
                ImageView  close=(ImageView)popUpView.findViewById(R.id.close);

                TextView tv=(TextView)view.findViewById(R.id.textView);
                TextView tv1=(TextView)view.findViewById(R.id.textView2);
                contentq.setText(tv1.getText());
                title.setText(tv.getText());
                popup.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                });

            }
        });
        mdatabase.close();
        return rootView;
    }

    public  void test(String alpha){
        firstchar=alpha;
        adapter=new ListAdapter(getActivity(),alpha);
        Lview.setAdapter(adapter);
    }

  /*@Override
    public void onResume() {
        super.onResume();
        final Handler handler = new Handler();
        do while(true){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                  Log.d("This is","Refreshing");
                    //  Toast.makeText(getActivity(), "check", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    handler.postDelayed(this, 2000);
                }
            }, 1500);
        }
    }*/

    public class ListAdapter extends BaseAdapter implements View.OnClickListener{
        Context context;

        public ListAdapter(Context context,String firstchar){
            this.context = context;
            listcontent=mdatabase.Get_ContactDetails(firstchar);
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

            final  ViewHolder viewHolder;
            if(view == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_list,null);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) view.findViewById(R.id.textView);
                viewHolder.content = (TextView) view.findViewById(R.id.textView2);


                viewHolder.img=(ImageView)view.findViewById(R.id.imageView);

                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
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

        /*    if(ispremium==true) {
                if (content.getAlphaflsg().equals("A")) {
                    if(position<10) {
                        if (new File("data/data/com.focusmedica.aqrshell/files/" + content.getVDOname()).exists()) {
                            viewHolder.img.setImageResource(R.drawable.play_pressed);
                            viewHolder.title.setTextColor(Color.parseColor("#ffffff"));
                            viewHolder.content.setTextColor(Color.parseColor("#ffffff"));

                        }else if (getActivity().getResources().getIdentifier(lower_case,
                                "raw", getActivity().getPackageName()) != 0) {
                            viewHolder.img.setImageResource(R.drawable.play_pressed);
                            viewHolder.title.setTextColor(Color.parseColor("#ffffff"));
                            viewHolder.content.setTextColor(Color.parseColor("#ffffff"));
                        } else {

                            viewHolder.img.setImageResource(R.drawable.download_enable);
                            viewHolder.title.setTextColor(Color.parseColor("#ffffff"));
                            viewHolder.content.setTextColor(Color.parseColor("#ffffff"));

                        }
                    }
                    else {
                        viewHolder.img.setImageResource(R.drawable.download_disable);
                        viewHolder.title.setTextColor(Color.parseColor("#808080"));
                        viewHolder.content.setTextColor(Color.parseColor("#808080"));
                    }

                }  else {
                    viewHolder.img.setImageResource(R.drawable.download_disable);
                    viewHolder.title.setTextColor(Color.parseColor("#808080"));
                    viewHolder.content.setTextColor(Color.parseColor("#808080"));
                }
            }else{*/
                if (new File("data/data/com.focusmedica.aqrshell/files/" + content.getVDOname()).exists()) {
                    viewHolder.img.setImageResource(R.drawable.play_pressed);
                    viewHolder.title.setTextColor(Color.parseColor("#ffffff"));
                    viewHolder.content.setTextColor(Color.parseColor("#ffffff"));

                }else if ( getActivity().getResources().getIdentifier(lower_case,
                        "raw",getActivity().getPackageName())!=0) {

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
                            Toast.makeText(getActivity(), "No internet connection available", Toast.LENGTH_LONG).show();
                            return;
                        }
                        asyncobj = new DownloadFileFromURL();
                          videofileName =vdoname.replaceAll(" ", "%20");
                        img_position=viewHolder.img.getId();
                        pDialog = new ProgressDialog(getActivity());
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

                        Intent intent = new Intent(getActivity(),VideoPlay2Activity.class);
                        intent.putExtra("vdoname",vdoname);
                        startActivity(intent);

                    }else{
                       }
                }
            });
            mdatabase.close();
            return view;
        }
    }

  /*  public void datachangeinform() {
        SharedPreferences appInfo = getActivity().getSharedPreferences("appInfo", getActivity().MODE_PRIVATE);
        ispremium= appInfo.getBoolean("isPremium",false);
        adapter.notifyDataSetChanged();
    }*/


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public  class DownloadFileFromURL extends AsyncTask<String, String, String> {
        private long total = 0L;
        private long lengthOfFile = 0L;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           pDialog.show();
        }

        private void onDownloadCancelled() {
            pDialog.dismiss();
            File dir=getActivity().getFilesDir();
            File file=new File(dir,vdoname);
            file.delete();
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            if(flag==1) {
                onDownloadCancelled();
                flag=0;
            }else {
                Toast.makeText(getActivity(), "Video Not Exist...", Toast.LENGTH_LONG).show();
                onDownloadCancelled();
            }
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;

            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.setRequestProperty("Accept-Encoding", "Identity");
                conection.connect();
                lengthOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(getActivity().getFilesDir()+"/"+vdoname);
                byte data[] = new byte[1024];
                while ((count = input.read(data)) != -1 && !isCancelled()) {
                    if (isCancelled()) break;
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                   // System.out.println("checking:" + (total * 100) / lengthOfFile);
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
            if (!isCancelled()) {
             pDialog.dismiss();
                adapter.notifyDataSetChanged();
            } else {
                onDownloadCancelled();
            }
        }
    }
}


