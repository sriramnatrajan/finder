package com.focusmedica.aqrshell.utils.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.focusmedica.aqrshell.AppListHandler;
import com.focusmedica.aqrshell.DataModel;
import com.focusmedica.aqrshell.ExpandableAdapter;
import com.focusmedica.aqrshell.Item2Adapter;
import com.focusmedica.aqrshell.ItemAdapter;
import com.focusmedica.aqrshell.R;
import com.focusmedica.aqrshell.dbHandler.SQLiteHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionsActivity extends Activity {
    Button btnBack,btnAdd,btnSync;
    ListView lvAppList,listdict;
    Toolbar toolbar;

    ListAdapter appListAdapter;
    Item2Adapter mItem2Adapter;
    ArrayList<DataModel> chapterList;
    ArrayList<DataModel> appList;
    ArrayList<DataModel> appDetails;
    ArrayList<DataModel> chooseList=new ArrayList<>();
    ArrayList<DataModel> chooseList2=new ArrayList<DataModel>();
   // MyDataBase myDataBase;
    DataModel dataModel,dataModel2;
    AppListHandler appListHandler; DataModel content;
    SQLiteHandler mSqLiteHandler;
    ExpandableAdapter mExpandableAdapter;ExpandableListView mExpandableListView;
    String a0,a1,a2,a3,a4,a5; int f0;  List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
      lvAppList=(ListView)findViewById(R.id.grid_view);
        listdict=(ListView)findViewById(R.id.listview2);
      //   mExpandableListView=(ExpandableListView)findViewById(R.id.elistview);
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
      //  myDataBase=new MyDataBase(getApplicationContext());
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
                a4=dataModel.getApp_type();
                //a5=dataModel.getAppFolder();
              f0=Integer.parseInt(a4);
              Log.d("@@@@@@@@@@@@@",a0+a1+a2+a3+a4+a5);
                appListHandler.addAppList(dataModel);
        }

            chapterList=mSqLiteHandler.getUnd("1");

            Log.d("@@@","chapSize="+chapterList.size());
            appListAdapter=new ItemAdapter(CollectionsActivity.this,chapterList);

        lvAppList.setAdapter(appListAdapter);

        chapterList=mSqLiteHandler.getUnd("2");
        mItem2Adapter=new Item2Adapter(CollectionsActivity.this,chapterList);
        listdict.setAdapter(mItem2Adapter);
       /* preparedList();
        mExpandableAdapter=new ExpandableAdapter(this,listDataHeader,listDataChild);
        mExpandableListView.setAdapter(mExpandableAdapter);*/
       /* }else {
            chapterList=appListHandler.getAppList();
            Log.d("@@@","chapSize="+chapterList.size());
            mItem2Adapter=new Item2Adapter(CollectionsActivity.this,chapterList);
            listdict.setAdapter(mItem2Adapter);
        }*/


       /* lvAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                dataModel=chapterList.get(position);
                String f1=dataModel.getApp_type();
                int v1=Integer.parseInt(f1);
                Log.d("TAGE","++++++++"+f1);
                if (v1==1){
                    dataModel=chapterList.get(position);
                   // String f2=dataModel.getApp_id();
                    Intent i=new Intent(getApplicationContext(),PreviewActivity.class);
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
        });*/

    }
    void preparedList(){
    listDataHeader=new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();
        listDataHeader.add("Understanding disease");
        listDataHeader.add("Dicitionary");
    List<String> und=new ArrayList<>();

        chooseList=mSqLiteHandler.getUnd("1");
        if (!chooseList.isEmpty()){
            for (int i=0 ; i<chooseList.size();i++){
                List<String> t1=new ArrayList<String>();
                DataModel model=chooseList.get(i);
                String f0=model.getName();

                t1.add(f0);
                listDataChild.put(listDataHeader.get(0),t1);
                Log.d("F1_F2_F3_F4", t1.toString());
                List<DataModel> list =new ArrayList<>();
               // Log.d("TAHAHAAH",list.toString());
            }
        }


      //dataModel=chooseList.get(0);
      //String f0=dataModel.getName();
        //
        /*String f1=dataModel.getValue();
        String f2=dataModel.getAppInfo();
        String f3=dataModel.getApp_id();
        String f4=dataModel.getApp_type();*/
     //   Log.d("F1_F2_F3_F4",f0+f1+f2+f3+"  "+f4);

        chooseList2=mSqLiteHandler.getUnd("2");
        dataModel2=chooseList2.get(0);
        List<String> t2=new ArrayList<String>();
        t2.add(dataModel2.getName());
        listDataChild.put(listDataHeader.get(1),t2 );
    }
}
