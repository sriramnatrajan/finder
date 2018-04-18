package com.focusmedica.aqrshell;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.focusmedica.aqrshell.utils.Activities.CollectionsActivity;
import com.focusmedica.aqrshell.utils.Activities.LogActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends Activity {
    GridView gridView;
    static final String TAG = "FMI";
    private RecyclerView.LayoutManager layoutManager;
    boolean mIsPremium = false;
    private Menu menu;
    private MenuItem item;
    private ItemAdapter adapter;
    private MyDataBase myDataBase;
    ArrayList<DataModel> appList;
    ArrayList<DataModel> appDetails;ArrayList<DataModel> chapterl;
    DataModel content;
    ArrayList<DataModel> AppDetails=new ArrayList<>();
    RelativeLayout footer, header;
    SearchView mSearch;
    Cursor cursor;
    AppListHandler appListHandler;
    ListAdapter appListAdapter;
    ListView mListsearch;
    int positiom=0;
    int mCurrRotation = 0;
    String[] data;
    ImageView sync;
    ArrayList<DataModel> chapterList;DataModel dataModel;
      RecyclerView rvFiltered;
    SearchAdapter mSearchAdapter;EditText etSearch;
    String url="http://focusmedica.in/aqr-shell/demo/home/home.dll";
    Animation rotate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_right);
         gridView = (GridView) findViewById(R.id.grid_view);
        footer=(RelativeLayout)findViewById(R.id.footer);
        //mSearch=(SearchView)findViewById(R.id.search_result);
        rvFiltered=(RecyclerView)findViewById(R.id.rvFiltered) ;
        etSearch = (EditText)findViewById(R.id.search_result);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Log.d("method@@@","oncreate");
          if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater=LayoutInflater.from(this);
        View mCustomView=mInflater.inflate(R.layout.custom_actionbar,null);


       ImageView imageView = (ImageView)mCustomView.findViewById(R.id.logo);
          sync=(ImageView)mCustomView.findViewById(R.id.sync);
     ImageView mHome=(ImageView)mCustomView.findViewById(R.id.home);
        mHome.setVisibility(View.INVISIBLE);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
         sync.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (!haveNetworkConnection()) {
                     Toast.makeText(MainActivity.this,"No Network Connection",Toast.LENGTH_LONG).show();
                 }else {
                     new SyncContent().execute(url);
                   rotate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
                     sync.startAnimation(rotate);
                      //  rotate.reset();
                     rotate.start();
                     Toast.makeText(MainActivity.this, "Syncing", Toast.LENGTH_SHORT).show();
                 }
             }
         });
        switch(this.getResources().getConfiguration().orientation) {

            case Configuration.ORIENTATION_PORTRAIT		:
                layoutManager=new LinearLayoutManager(this);
                // layoutManager=new GridLayoutManager(this,3);
                break;

            case Configuration.ORIENTATION_LANDSCAPE	:
                layoutManager=new LinearLayoutManager(this);
                //  layoutManager=new GridLayoutManager(this,4);
                break;

            default	:
                break;
        }

        rvFiltered.setHasFixedSize(true);
        rvFiltered.setLayoutManager(layoutManager);
        rvFiltered.setItemAnimator(new DefaultItemAnimator());
        myDataBase =new MyDataBase(getApplicationContext());

        final ArrayList<DataModel> chapter = myDataBase.getThumbnail();
         mSearchAdapter = new SearchAdapter(MainActivity.this, chapter);

        rvFiltered.setAdapter(mSearchAdapter);

        appListHandler =new AppListHandler(this);
        adapter=new ItemAdapter(MainActivity.this,chapter);
        gridView.setAdapter(adapter);

        BottomNavigationView mBottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.login_bar:
                        Intent log = new Intent(MainActivity.this, LogActivity.class);
                        startActivity(log);
                        break;

                    case R.id.validate_bar:
                        Intent notes = new Intent(MainActivity.this, CollectionsActivity.class);
                        startActivity(notes);
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

        rvFiltered.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    footer.setVisibility(View.GONE);
                    // Scrolling up
                } else {
                    footer.setVisibility(View.VISIBLE);

                    // Scrolling down
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                   // footer.setVisibility(View.VISIBLE);

                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                   // footer.setVisibility(View.GONE);
                    // Do something
                } else {
                    // Do something
                  //  footer.setVisibility(View.GONE);
                }
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
            int mPosition=0;
            int mOffset=0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                int position = gridView.getFirstVisiblePosition();
                View v = gridView.getChildAt(0);
                int offset = (v == null) ? 0 : v.getTop();

                if (mPosition < position || (mPosition == position && mOffset < offset)){
                    // Scrolled up
                    footer.setVisibility(View.GONE);

                } else {
                    // Scrolled down
                    footer.setVisibility(View.VISIBLE);

                }
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(cs.length()>0) {
                    etSearch.setPadding(10,0,10,0);
                    etSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.delete, 0);
                }else{
                    etSearch.setPadding(10,0,0,0);
                    etSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);
                    etSearch.setCompoundDrawablePadding(10);
                }
                // When user changed the Text
                mSearchAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(etSearch.getCompoundDrawables()[2]!=null){
                        if(event.getX() >= (etSearch.getRight()- etSearch.getLeft() - etSearch.getCompoundDrawables()[2].getBounds().width())) {
                            etSearch.setText("");
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {
            String titleId = bundle.getString("titleId");
            Log.d("@@@", "titleid=" + titleId);
            appList = myDataBase.getNameAndImage(titleId);
            Log.d("@@@", "size=" + appList.size());
            content = appList.get(0);
            appListHandler.addAppList(content);

        }

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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


        void alert(String message) {
            AlertDialog.Builder bld = new AlertDialog.Builder(this);
            bld.setMessage(message);
            bld.setNeutralButton("OK", null);
            bld.create().show();
            onRestart();
        }

 private class SyncContent extends AsyncTask<String, String, String> {
        Context mContext;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            File file = new File("/data/data/com.focusmedica.aqrshell/databases/home.dll");
            if (file.exists()) {
                file.delete();
            }
            Toast.makeText(MainActivity.this, "Loading....", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.setRequestProperty("Accept-Encoding", "identity");
                conection.connect();
                //this will be useful so that you can show a typical 0-100% progress bar
                int lengthOfFile = conection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());
                // Output stream
                OutputStream output = new FileOutputStream("/data/data/com.focusmedica.aqrshell/databases/home.dll");

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            File file = new File("/data/data/com.focusmedica.aqrshell/databases/home.dll");
            if (file.exists()) {
                 Toast.makeText(MainActivity.this, "Sync completed", Toast.LENGTH_SHORT).show();
               rotate.cancel();
               // sync.setImageResource(R.drawable.ic_sync);
            }else{
                rotate.cancel();
                 Toast.makeText(MainActivity.this, "Some Error occured Try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
