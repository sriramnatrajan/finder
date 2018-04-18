package com.focusmedica.aqrshell;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by in_sane on 7/4/17.
 */

public class LanguageAdapter extends BaseAdapter{
    String url,titleName;
    Context context;
    ArrayList<DataModel> listData;
    int english,hindi,marathi,bengali,gujarati,assamee,oriya,tamil,telugu,kannada,malayalam,punjabi,kashmiri,urdu;
    DataModel city;

    public LanguageAdapter(Context context, ArrayList list, String url, String titleName){
        this.context = context;
        this.listData=list;
        this.url=url;
        this.titleName=titleName;
        SharedPreferences sharedpreferences = context.getSharedPreferences("MyPREFERENCES.xml", Context.MODE_PRIVATE);
        english=sharedpreferences.getInt("english"+titleName, 100);
        hindi=sharedpreferences.getInt("hindi"+titleName,100);
        marathi=sharedpreferences.getInt("marathi"+titleName, 100);
        bengali=sharedpreferences.getInt("bengali"+titleName,100);
        gujarati=sharedpreferences.getInt("gujarati"+titleName,100);
        assamee=sharedpreferences.getInt("assamese"+titleName,100);
        oriya=sharedpreferences.getInt("oriya"+titleName,100);
        tamil=sharedpreferences.getInt("tamil"+titleName,100);
        telugu=sharedpreferences.getInt("telugu"+titleName,100);
        kannada=sharedpreferences.getInt("kannada"+titleName,100);
        malayalam=sharedpreferences.getInt("malayalam"+titleName,100);
        punjabi=sharedpreferences.getInt("punjabi"+titleName,100);
        kashmiri=sharedpreferences.getInt("kashmiri"+titleName, 100);
        urdu=sharedpreferences.getInt("urdu"+titleName,100);
        System.out.println("checkingposition: "+ english +"     " +hindi+"     " +gujarati+"     " +assamee+"     " +oriya+"    "+marathi+"     "+bengali+"     "+telugu+"      "
                +tamil+"        "+malayalam+"       "+kannada+"     "+punjabi+"     "+kashmiri+"        "+urdu);

    }


    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }
    public String getselected(int position) {

        DataModel city = listData.get(position);
        String cityName = city.getName();
        return cityName;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder {
        private TextView tvLanguageName;
        private ImageView ivDownload,ivAppIcon;
    }

    @Override
    public int getViewTypeCount() {
       //return 1;
         return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final  ViewHolder viewHolder;
        if(view == null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.language_adapter,null);
            viewHolder = new ViewHolder();
            viewHolder.tvLanguageName = (TextView) view.findViewById(R.id.tvLanguageName);
            //viewHolder.textView2=(TextView) view.findViewById(R.id.textView2);
            viewHolder.ivAppIcon = (ImageView) view.findViewById(R.id.ivAppIcon);
            viewHolder.ivDownload = (ImageView) view.findViewById(R.id.ivDownload);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }


        city = listData.get(position);
        String cityName = city.getName();
        viewHolder.tvLanguageName.setText(cityName);
        Log.d("@@@", " languageAdapter"+url);
        Glide.with(context).load(url+ "title.png").into(viewHolder.ivAppIcon);
        if(english==position||hindi==position||gujarati==position||assamee==position||oriya==position||tamil==position||
                telugu==position||malayalam==position||punjabi==position||kashmiri==position||kannada==position||urdu==position
                ||bengali==position||marathi==position){
            viewHolder.ivDownload.setImageResource(R.drawable.right_arrow);
        }
        return view;
    }
}
