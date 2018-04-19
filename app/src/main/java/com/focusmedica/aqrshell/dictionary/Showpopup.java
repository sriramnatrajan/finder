package com.focusmedica.aqrshell.dictionary;

import android.app.ActionBar;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.focusmedica.aqrshell.R;

/**
 * Created by Lokesh on 18-Feb-16.
 */
public class Showpopup {
    Context ctx;
    ImageView close;
    Mydatabase mdatabase;
    TextView title,content;
    String name,description;

    public Showpopup(Context ctx, String title){
        this.ctx = ctx;
        name=title;
        mdatabase=new Mydatabase(ctx,"");
        mdatabase.openDataBase();
        description=mdatabase.get_content(name);
        mdatabase.close();
    }

    public void launch(View parent){
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_layout, null, false);
        final PopupWindow popup = new PopupWindow(popUpView, ViewGroup.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        popup.setContentView(popUpView);
        title=(TextView)popUpView.findViewById(R.id.textView1);
        content=(TextView)popUpView.findViewById(R.id.textView2);
        close=(ImageView)popUpView.findViewById(R.id.close);
        content.setText(description);
        title.setText(name);
        popup.showAtLocation(parent, Gravity.CENTER_HORIZONTAL, 0, 0);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }
}
