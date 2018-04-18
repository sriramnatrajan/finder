package com.focusmedica.aqrshell;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;



public class Notes extends Activity implements View.OnClickListener {
    Button save,cancel;
    EditText editText;
    SharedPreferences savednotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()| ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.fmlogo);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        editText = (EditText)findViewById(R.id.etNotes);
        save =(Button)findViewById(R.id.btnSave);
        cancel =(Button)findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        savednotes = getSharedPreferences("notes", Context.MODE_PRIVATE);


        editText.setText(savednotes.getString("tag", ""));
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(editText.getText().length()>0){
            makeTag(editText.getText().toString());

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }

        switch (v.getId()){
            case R.id.btnSave:
                finish();
                break;
        }
    }

    private void makeTag(String s) {
        String or = savednotes.getString(s, null);
        SharedPreferences.Editor preferencesEditor = savednotes.edit();
        preferencesEditor.putString("tag",s); //change this line to this
        preferencesEditor.commit();
    }
}

