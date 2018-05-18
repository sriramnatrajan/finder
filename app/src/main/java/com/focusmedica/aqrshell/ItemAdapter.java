package com.focusmedica.aqrshell;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class ItemAdapter extends BaseAdapter{
    ArrayList<Object> listcontent;
    Context context;
    DataModel content;
    String url; TextView tv;
    String chapterName;
    String chapterImage,appfolder;
    String chapterImage1;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
   // MyViewHolder MyViewHolder;
    private LayoutInflater mInflater;
    MyViewHolder MyViewHolder;
    public ItemAdapter(Context context, ArrayList<Object> listcontent ) {
        this.context = context;
        this.listcontent = listcontent;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
  class MyViewHolder  {

          TextView chapter;
          ImageView chapter_thumb;

       /*   MyViewHolder(View view){
            super(view);
            chapter=(TextView)  view.findViewById(R.id.chapter_tv);
            chapter_thumb=(ImageView)view.findViewById(R.id.chapter_thumb_iv);
        }*/
    }

    @Override
    public int getCount() {
        return listcontent.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Object getItem(int position) {
        return listcontent.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof  DataModel) {
            return TYPE_ITEM;
        }
        return TYPE_SEPARATOR;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position)== TYPE_ITEM);
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        int rowType = getItemViewType(position);

        if (view == null) {
            switch (rowType) {
                case TYPE_ITEM:
                    view = mInflater.inflate(R.layout.content_layout, viewGroup, false);
                    MyViewHolder = new ItemAdapter.MyViewHolder();
                    MyViewHolder.chapter = (TextView) view.findViewById(R.id.chapter_tv);
                    MyViewHolder.chapter_thumb=(ImageView)view.findViewById(R.id.chapter_thumb_iv);
                    break;
                case TYPE_SEPARATOR:
                    view = mInflater.inflate(R.layout.list_group, viewGroup, false);
                    tv = (TextView) view.findViewById(R.id.expand_title);
                    break;
            }
        }
        switch (rowType){
            case TYPE_ITEM:
                DataModel  content = (DataModel) getItem(position);

                chapterName=content.getName();
                chapterImage=content.getValue();
                chapterImage1=content.getApp_id();
                appfolder=content.getApp_id();
                MyViewHolder.chapter .setText(chapterName);
                Log.d("TAGG+++",chapterName+","+chapterImage+","+chapterImage1);
                Glide.with(context).load(chapterImage+"/"+appfolder+"_thumbnail"+".png").into( MyViewHolder.chapter_thumb);
                break;
            case TYPE_SEPARATOR:
                TextView mTextView=(TextView)view.findViewById(R.id.expand_title);
                String titleString = (String)getItem(position);
                tv.setText(titleString);
                break;
        }
        return view;
    }
}

