package com.focusmedica.aqrshell;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PageActivity extends Activity implements View.OnClickListener{

    private int sizeBucket;
    private MenuItem buyButton;
    private View view;
    private RelativeLayout homeLayout;
    private Button btnBack;
    private ImageView home,info;
    private WebView webView;
    int i=0,glossaryflag=0,page;
    Toast mToast;
    JavaScriptInterface javaScriptInterface;
    String ziplanguage,titleName,titlePages,url;
    Toolbar toolbar;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

ImageView mHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_activity);
       /* toolbar=(Toolbar)findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        home=(ImageView)toolbar.findViewById(R.id.ivHome);
        info=(ImageView)toolbar.findViewById(R.id.ivInfo);
        btnBack=(Button)toolbar.findViewById(R.id.btnBack);*/
/*     ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater=LayoutInflater.from(this);
        View mCustomView=mInflater.inflate(R.layout.custom_actionbar,null);

        ImageView imageView = (ImageView)mCustomView.findViewById(R.id.logo);
        */
         ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater=LayoutInflater.from(this);
        View mCustomView=mInflater.inflate(R.layout.custom_actionbar,null);
        ImageView imageView = (ImageView)mCustomView.findViewById(R.id.logo);
        mHome=(ImageView)mCustomView.findViewById(R.id.home);
        ImageView sync=(ImageView)mCustomView.findViewById(R.id.sync);
        sync.setVisibility(View.INVISIBLE);
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);


     /*   actionBar.setDisplayOptions(actionBar.getDisplayOptions() | android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.fmlogo);
        android.app.ActionBar.LayoutParams layoutParams = new android.app.ActionBar.LayoutParams(
                android.app.ActionBar.LayoutParams.WRAP_CONTENT,
                android.app.ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);*/

      /*  if(i==0){
            home.setVisibility(View.GONE);
            btnBack.setVisibility(View.VISIBLE);
        }else {
            home.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.GONE);
        }*/

        url=getIntent().getStringExtra("url");
        ziplanguage=getIntent().getStringExtra("ziplang");
        Log.d("Archu","glossary"+ziplanguage);
        titleName=getIntent().getStringExtra("titleName");
        titlePages=getIntent().getStringExtra("titlePages");
        page=Integer.parseInt(titlePages);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        gestureDetector = new GestureDetector(PageActivity.this.getApplicationContext(), new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

               gestureDetector.onTouchEvent(event);
                return false;
            }
        };

        sizeBucket =this.getResources().getInteger(R.integer.sizeBucket);

        webView = (WebView) findViewById(R.id.webView);
        //ref=(Button)findViewById(R.id.bt_quick_ref);
        //glossary=(Button)findViewById(R.id.bt_glossary);
        webView.setWebViewClient(new myWebClient());
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        javaScriptInterface=new JavaScriptInterface(this);
        webView.addJavascriptInterface(javaScriptInterface, "Android");
        webView.loadUrl("file:///data/data/com.focusmedica.aqrshell/files/"+titleName+"/"  + ziplanguage + "/index.html");
        WebGesture webges=new WebGesture(getApplicationContext(),webView);
        webView.setOnClickListener(PageActivity.this);
        webView.setOnTouchListener(gestureListener);

        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //*glossary.setTextColor(Color.WHITE);
               // ref.setTextColor(Color.BLACK);*//*
                glossaryflag = 0;
                webView.loadUrl("file:///data/data/com.focusmedica.aqrshell/files/"+titleName+"/"  + ziplanguage + "/index.html");
                i = 0;
               /* if(i==0){
                    mHome.setVisibility(View.GONE);

                }else {
                    mHome.setVisibility(View.VISIBLE);

                }*/
            }
        });

       /* btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
      */  /*glossary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(glossaryflag==0) {
                webView.loadUrl("file:///data/data/com.focusmedica.aqrshell/files/"+titleName +"/"  + ziplanguage + "/glossary.html");
                glossaryflag = 1;
                glossary.setTextColor(Color.BLACK);
                ref.setTextColor(Color.WHITE);
                //}
            }
        });

        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(glossaryflag==1) {
                    glossaryflag = 0;
                    glossary.setTextColor(Color.WHITE);
                    ref.setTextColor(Color.BLACK);
                    if (i == 0)
                        webView.loadUrl("file:///data/data/com.focusmedica.aqrshell/files/"+titleName +"/"  + ziplanguage + "/index.html");
                    else {
                        webView.loadUrl("file:///data/data/com.focusmedica.aqrshell/files/"+titleName +"/"  + ziplanguage + "/page_" + i + ".html");
                        mToast.setText(i + "/9");
                        mToast.show();
                    }
               // }
            }
        });*/

       /* info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PageActivity.this, Info.class);
                startActivity(intent);

            }
        });*/

        homeLayout = (RelativeLayout) findViewById(R.id.homeLayout);

    }
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                //if (glossaryflag == 1) {
                /*if (glossaryflag == 1) {
                    return false;
                }*/
                if(Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    // Toast.makeText(PageActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                    if(i<page) {
                        i++;
                    }
                    mToast.setText(i + "/"+page);
                    mToast.show();
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("file:///data/data/com.focusmedica.aqrshell/files/"+titleName+"/"   + ziplanguage + "/page_" + i + ".html");

                        }
                    });

                    //   home.setVisibility(View.VISIBLE);
                    // System.out.println("urlofbew22222" + webView.getUrl());
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //Toast.makeText(PageActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                    i--;
                    if (i == 0) {
                        webView.loadUrl("file:///data/data/com.focusmedica.aqrshell/files/"+titleName +"/"  + ziplanguage + "/index.html");

                    } else if (i < 0) {
                        i = 0;
                    } else {
                        mToast.setText(i + "/"+page);
                        mToast.show();
                        // Toast.makeText(PageActivity.this, i+"/30", Toast.LENGTH_SHORT).show();
                        webView.loadUrl("file:///data/data/com.focusmedica.aqrshell/files/"+titleName +"/"  + ziplanguage + "/page_" + i + ".html");
                        //  home.setVisibility(View.VISIBLE);

                    }

                }
                if(i==0){
                    home.setVisibility(View.GONE);
                    btnBack.setVisibility(View.VISIBLE);
                }else {
                    home.setVisibility(View.VISIBLE);
                    btnBack.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                // nothing
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

    }
    @Override
    public void onClick(View view) {
        if(i==0){
           // home.setVisibility(View.GONE);
           // btnBack.setVisibility(View.VISIBLE);
        }else {
          ////  home.setVisibility(View.VISIBLE);
           // btnBack.setVisibility(View.GONE);
        }
        //  home.setVisibility(View.VISIBLE);
            //  home.setVisibility(View.VISIBLE);
       /* Filter f = (Filter) v.getTag();
        FilterFullscreenActivity.show(this, input, f);*/
    }

    //to call javascript function from html
    public class JavaScriptInterface{
        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;

        }


        @JavascriptInterface
        public void notify(String PageId) {
            if(i==0){
               // home.setVisibility(View.GONE);
              //  btnBack.setVisibility(View.VISIBLE);
            }else {
               // home.setVisibility(View.VISIBLE);
               // btnBack.setVisibility(View.GONE);
            }
            Intent In = new Intent(getApplicationContext(), VideoChecker2Activity.class);
            In.putExtra("vdoname",PageId);
            In.putExtra("language",ziplanguage);
            In.putExtra("titleName",titleName);
            In.putExtra("url",url);
            startActivity(In);
        }

        @JavascriptInterface
        public void gotopage(final String PageId){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    i = Integer.parseInt(PageId);
                    if(i==0){
                    //    home.setVisibility(View.GONE);
                     //   btnBack.setVisibility(View.VISIBLE);
                    }else {
                    //    home.setVisibility(View.VISIBLE);
                    //    btnBack.setVisibility(View.GONE);
                    }
                    webView.loadUrl("file:///data/data/com.focusmedica.aqrshell/files/"+titleName +"/"  + ziplanguage + "/page_" + i + ".html");
                    mToast.setText(i + "/"+page);
                    mToast.show();

                }
            });

        }



        @JavascriptInterface
        public void index(final String PageId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    glossaryflag = 0;
                    i = Integer.parseInt(PageId);
                   /* if(i==0){
                        home.setVisibility(View.GONE);
                        btnBack.setVisibility(View.VISIBLE);
                    }else {
                        home.setVisibility(View.VISIBLE);
                        btnBack.setVisibility(View.GONE);
                    }*/
                    webView.loadUrl("file:///data/data/com.focusmedica.aqrshell/files/"+titleName +"/" + ziplanguage + "/page_" + i + ".html");
                    mToast.setText(i + "/"+page);
                    mToast.show();
                    /*ref.setTextColor(Color.BLACK);
                    glossary.setTextColor(Color.WHITE);*/



                }
            });


        }
    }
    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
           /* if(i==0){
                home.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
            }else {
                home.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.GONE);
            }*/
        }

    }

    private void unbindDrawables(View view)
    {
        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unbindDrawables(homeLayout);
        System.gc();
    }
}













