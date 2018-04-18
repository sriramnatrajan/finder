package com.focusmedica.aqrshell;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by Lokesh on 08-Feb-16.
 */
public class WebGesture extends WebView {
Context context;
    GestureDetector gd;
    WebView webview;

    public WebGesture(Context context, WebView webView) {
        super(context);
        this.context=context;
        this.webview=webView;
        gd= new GestureDetector(context,geslitesner );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return (gd.onTouchEvent(event));
    }

    @Override
    public void goForward() {
        super.goForward();
        Toast.makeText(context,"working",Toast.LENGTH_LONG);
        webview.loadUrl("file:///data/data/com.focusmedica.aqrshell/files/english/page_0.html");

    }

    GestureDetector.SimpleOnGestureListener geslitesner=new GestureDetector.SimpleOnGestureListener(){


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float deltax,deltay,velo;
            deltax = Math.abs(e1.getRawX()-e2.getRawX());
            deltay = Math.abs(e1.getRawY()-e2.getRawY());
            velo = Math.abs(velocityX);

            //pref_browser_gesturevelo is how fast finger moves.
            //pref_browser_gesturevelo set to 350 as default in my app
            if (deltax > 200 && deltay < 90 && velo > 350) {
                if (e1.getRawX() > e2.getRawX()) {
                    if (canGoForward()){
                      /* */
                        goForward();
                    }
                    else{
                        //Gesture : no more forward history
                    }
                } else if(e1.getRawX() < e2.getRawX()){
                    if (canGoBack()){
                        //Gesture : go back
                        goBack();
                    }
                    else{
                        //Gesture : no more backward history, end browser
                       // CallNotifyBackTakenHandler();
                    }
                }

            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }


    };




}
