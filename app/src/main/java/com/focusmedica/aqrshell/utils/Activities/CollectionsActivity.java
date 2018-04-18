package com.focusmedica.aqrshell.utils.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.focusmedica.aqrshell.AppListHandler;
import com.focusmedica.aqrshell.DataModel;
import com.focusmedica.aqrshell.ItemAdapter;
import com.focusmedica.aqrshell.MyDataBase;
import com.focusmedica.aqrshell.PreviewActivity;
import com.focusmedica.aqrshell.R;
import com.focusmedica.aqrshell.dbHandler.SQLiteHandler;
import com.focusmedica.aqrshell.dictionary.Firstpage;

import java.util.ArrayList;

public class CollectionsActivity extends Activity {
    Button btnBack,btnAdd,btnSync;
    ListView lvAppList;
    Toolbar toolbar;

    ListAdapter appListAdapter;
    ArrayList<DataModel> chapterList;
    ArrayList<DataModel> appList;
    ArrayList<DataModel> appDetails;
    MyDataBase myDataBase;
    DataModel dataModel;
    AppListHandler appListHandler;
    SQLiteHandler mSqLiteHandler;
    String a0,a1,a2,a3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        lvAppList=(ListView)findViewById(R.id.grid_view);
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
        myDataBase=new MyDataBase(getApplicationContext());
        appListHandler=new AppListHandler(this);

        chapterList=new ArrayList<>();
        appList=new ArrayList<>();
        appDetails=new ArrayList<>();
        mSqLiteHandler=new SQLiteHandler(this);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {
            String titleId = bundle.getString("name");
            Log.d("@@@", "titleid=" + titleId);
         appList = mSqLiteHandler.getDetails(titleId);

          Log.d("@@@", "size=" + appList.size());

             dataModel = appList.get(0);
                a0=dataModel.getName();
                a1=dataModel.getValue();
                a2=dataModel.getAppInfo();
                a3=dataModel.getApp_id();
                Log.d("@@@@@@@@@@@@@",a0+a1+a2+a3);
   appListHandler.addAppList(dataModel);
        }


        chapterList=appListHandler.getAppList();
        Log.d("@@@","chapSize="+chapterList.size());
        appListAdapter=new ItemAdapter(CollectionsActivity.this,chapterList);
        lvAppList.setAdapter(appListAdapter);

        lvAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                dataModel=chapterList.get(position);
                String f1=dataModel.getApp_id();
                int v1=Integer.parseInt(f1);
                if (v1==1){
                    dataModel=chapterList.get(position);
                    String f2=dataModel.getApp_id();
                    Intent i=new Intent(getApplicationContext(),PreviewActivity.class);
                    // i.putExtra("url",dataModel.getDlink());
                    //i.putExtra("titlePages",dataModel.getTitlePages());
                    i.putExtra("titleName",dataModel.getName());
                    startActivity(i);
                }else{
           Intent i=new Intent(getApplicationContext(), Firstpage.class);
                    dataModel=chapterList.get(position);
           i.putExtra("app_id",dataModel.getApp_id());
           i.putExtra("url",dataModel.getValue());
           Log.d("APPPP_ID+++++++++++++++",dataModel.getApp_id());
           startActivity(i);
                }

            }
        });
    }
}
