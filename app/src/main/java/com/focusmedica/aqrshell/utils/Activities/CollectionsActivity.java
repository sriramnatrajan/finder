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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
    int ik;
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

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.fmlogo);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
        ActionBar.LayoutParams.WRAP_CONTENT,
        ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        appListHandler = new AppListHandler(this);
        chapterList = new ArrayList<>();
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
        appList = mSqLiteHandler.getUnd("1");
        appList1=mSqLiteHandler.getUnd("2");
        Log.d("@@@", "size=" + appList.size());
        Log.d("@@@", "appList1=" + appList1.size());
        dataModel = appList.get(0);
        dataModel2 = appList1.get(0);

        final ArrayList<Object> headerList = new ArrayList<>();
        headerList.add("Understanding Diseases");
        headerList.addAll(appList);
        headerList.add("Animated Pocket Dictionary");
        headerList.addAll(appList1);
        appListAdapter=new ItemAdapter(getApplicationContext(),headerList);

        lvAppList.setAdapter(appListAdapter);

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
                    i.putExtra("app_id", dataModel.getApp_id());
                    i.putExtra("url", dataModel.getValue());
                    Log.d("APPPP_ID+++++++++++++++", dataModel.getApp_id());
                    startActivity(i);

                }
            }
        });

        lvAppList.setMenuCreator(creator);
        lvAppList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                        dataModel = (DataModel) headerList.get(position );
                        Toast.makeText(CollectionsActivity.this, "File Deleted"+position , Toast.LENGTH_SHORT).show();
                        headerList.remove(position);
                        appListAdapter.notifyDataSetChanged();
                Log.d("Position of List", ""+position );
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        lvAppList.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);

        // Left
        lvAppList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

    }
}
