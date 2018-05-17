package com.focusmedica.aqrshell.utils.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

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
    ListView lvAppList, listdict;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        lvAppList = (ListView) findViewById(R.id.grid_view);

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

        a0 = dataModel.getName();
        a1 = dataModel.getValue();
        a2 = dataModel.getAppInfo();
        a3 = dataModel.getApp_id();
        a4 = dataModel.getApp_type();
        ik = Integer.parseInt(a4);

        b0=dataModel2.getName();
        b1=dataModel2.getValue();
        b2=dataModel2.getAppInfo();
        b3=dataModel2.getApp_id();
        b4=dataModel2.getApp_type();

        chapterList = mSqLiteHandler.getAppList();
        DataModel m1 = new DataModel(a0, a1, a2, a3, a4);
        DataModel m2 = new DataModel(b0, b1, b2, b3, b4);
        ArrayList<DataModel> model = new ArrayList<>();
        model.add(m1);
        ArrayList<DataModel> m22=new ArrayList<>();
        m22.add(m2);

        ArrayList<DataModel> headerOneList = new ArrayList<>();
        ArrayList<DataModel> headerTwoList = new ArrayList<>();

        for (DataModel model1 : model) {
                headerOneList.add(model1);
                Log.d("DATATMODEL1", "" + headerOneList.size());
        }
        for (DataModel model1 : m22) {
                headerTwoList.add(model1);
                Log.d("DATATMODEL2", "" +  headerTwoList.size());
        }

        ArrayList<Object> headerList = new ArrayList<>();
        headerList.add("Understanding Diseases");
        headerList.addAll(headerOneList);
        headerList.add("Animated Pocket Dictionary");
        headerList.addAll(headerTwoList);
        appListAdapter=new ItemAdapter(getApplicationContext(),headerList);
        lvAppList.setAdapter(appListAdapter);
        appListAdapter.notifyDataSetChanged();
        appList = mSqLiteHandler.getAppList();


        Log.d("@@@", "appList=" + appList.size());
        Log.d("@@@", "appList1=" + appList1.size());
        lvAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                dataModel = appList.get(position);

                String f1 = dataModel.getApp_type();
                int v1 = Integer.parseInt(f1);
                Log.d("TAGE", "++++++++" + f1);
                if (v1 == 1) {
                    dataModel = chapterList.get(position);
                    //String f2=dataModel.getApp_id();
                    Intent i = new Intent(getApplicationContext(), PreviewActivity.class);
                    i.putExtra("titleName", dataModel.getName());
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), Firstpage.class);
                    dataModel = chapterList.get(position);
                    i.putExtra("app_id", dataModel.getApp_id());
                    i.putExtra("url", dataModel.getValue());
                    Log.d("APPPP_ID+++++++++++++++", dataModel.getApp_id());
                    startActivity(i);

                }
            }
        });
    }


}
