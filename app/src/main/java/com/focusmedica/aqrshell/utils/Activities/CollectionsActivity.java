package com.focusmedica.aqrshell.utils.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.focusmedica.aqrshell.AppListHandler;
import com.focusmedica.aqrshell.DataModel;
import com.focusmedica.aqrshell.Item2Adapter;
import com.focusmedica.aqrshell.ItemAdapter;
import com.focusmedica.aqrshell.PreviewActivity;
import com.focusmedica.aqrshell.R;
import com.focusmedica.aqrshell.dbHandler.SQLiteHandler;
import com.focusmedica.aqrshell.dictionary.Firstpage;

import java.io.File;
import java.util.ArrayList;

public class CollectionsActivity extends Activity {
    Button btnBack, btnAdd, btnSync;
    SwipeMenuListView lvAppList, listdict;
    Toolbar toolbar;
    ViewGroup mViewGroup;
    ItemAdapter appListAdapter;
    Item2Adapter mItem2Adapter;
    ArrayList<DataModel> chapterList, chapterList2;
    ArrayList<DataModel> appList,appList1,appList3;
    ArrayList<DataModel> appDetails;
    int ik;  View mCustomView;
    DataModel dataModel, dataModel2;
    AppListHandler appListHandler;
    SQLiteHandler mSqLiteHandler;
    String a0, a1, a2, a3, a4, a5;
    String b0,b1,b2,b3,b4,b5;
    int position1;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        lvAppList = (SwipeMenuListView) findViewById(R.id.rc_view);
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        ImageView info=(ImageView)mCustomView.findViewById(R.id.imageView4);
        mCustomView.setBackground(new ColorDrawable(Color.parseColor("#34495C")));
       // mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3d4f6d")));
        info.setVisibility(View.INVISIBLE);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        ImageView i=(ImageView)mCustomView.findViewById(R.id.iv_download) ;
        i.setVisibility(View.INVISIBLE);
        appListHandler = new AppListHandler(this);
        appList = new ArrayList<>();
        appDetails = new ArrayList<>();
        mSqLiteHandler = new SQLiteHandler(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String titleId = bundle.getString("name");
            Log.d("@@@", "titleid=" + titleId);
            appList = mSqLiteHandler.getAppList();
            Log.d("@@@", "size=" + appList.size());

            dataModel = appList.get(0);
            a0 = dataModel.getName();
            a1 = dataModel.getValue();
            a2 = dataModel.getAppInfo();
            a3 = dataModel.getApp_id();
            a4 = dataModel.getApp_type();
            ik = Integer.parseInt(a4);
            Log.d("@@@@@@@@@@@@@", a0 + a1 + a2 + a3 + a4 );
            appListHandler.addAppList(dataModel);
        }
        try{
            appList = mSqLiteHandler.getUnd("1");
            appList1=mSqLiteHandler.getUnd("2");
            Log.d("@@@", "size=" + appList.size());
            Log.d("@@@", "appList1=" + appList1.size());
            dataModel = appList.get(0);
            dataModel2 = appList1.get(0);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }


        final ArrayList<Object> headerList = new ArrayList<>();
        headerList.add("Understanding Diseases");
        headerList.addAll(appList);
        headerList.add("Animated Pocket Dictionary");
        headerList.addAll(appList1);
        appListAdapter=new ItemAdapter(getApplicationContext(),headerList);

        lvAppList.setAdapter(appListAdapter);
        final ArrayList<Object> headerList1 = new ArrayList<>();
        headerList1.addAll(appList);
        headerList1.addAll(appList1);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth( (90));
                // set a icon
                deleteItem.setIcon(R.drawable.delete_black);
                // add to menu
                menu.addMenuItem(deleteItem);

            }
        };
        if (creator.equals(headerList.contains("Understanding Diseases"))){
            lvAppList.setSwipeDirection(0);
        }if (creator.equals(headerList.contains("Animated Pocket Dictionary"))){
            lvAppList.setSwipeDirection(0);
        }
        lvAppList.setCloseInterpolator(new BounceInterpolator());
        lvAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,   int position , long id) {

                dataModel = (DataModel) headerList.get(position);

                String f1 = dataModel.getApp_type();
                int v1 = Integer.parseInt(f1);
                Log.d("TAGE", "++++++++" + f1);
                if (v1 == 1) {
                    Intent i = new Intent(getApplicationContext(), PreviewActivity.class);
                    i.putExtra("titleName", dataModel.getName());
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), Firstpage.class);
                    i.putExtra("titleName", dataModel.getName());
                    i.putExtra("app_id", dataModel.getApp_id());
                    i.putExtra("url", dataModel.getValue());
                    startActivity(i);

                }
            }
        });

        lvAppList.setMenuCreator(creator);

        lvAppList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

            dataModel = (DataModel) headerList.get(position);
                Toast.makeText(CollectionsActivity.this, "File Deleted" + position + dataModel.getName(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "File Closed" + dataModel);
                Log.d("TAG", "Index====" +  index);
                headerList.remove(dataModel);
                mSqLiteHandler.getDetails(dataModel.getName());
                mSqLiteHandler.removeSingleContent(dataModel.getName());
                appListAdapter.notifyDataSetChanged();
                lvAppList.setAdapter(appListAdapter);
                String tn0=dataModel.getName();
                File mFile=new File(getApplicationContext().getFilesDir()+"/"+tn0+"/"+"video.mp4");
                if (mFile.exists()){
                    mFile.delete();
                }
                return false;
            }
        });

        lvAppList.setOpenInterpolator(new BounceInterpolator());

        lvAppList.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        lvAppList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }
/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),LogActivity.class);
        startActivity(i);
    }*/
}
