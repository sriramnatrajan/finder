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
import java.util.TreeSet;


public class ItemAdapter extends BaseAdapter{
    ArrayList<Object> listcontent;
    Context context;
    DataModel content;
    String url;
    String chapterName;
    String chapterImage,appfolder;
    String chapterImage1;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT=TYPE_SEPARATOR+1;
      ViewHolder viewHolder;
    private TreeSet<Integer> mSeperatorSet= new TreeSet<Integer>();
    private ArrayList<String> mData = new ArrayList<String>();
    private ArrayList<DataModel> str = new ArrayList<DataModel>();
    private LayoutInflater mInflater;


    public ItemAdapter(Context context, ArrayList<Object> listcontent ) {
        this.context = context;
        this.listcontent = listcontent;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof  DataModel) {
            return TYPE_ITEM;
        }
        return TYPE_SEPARATOR;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position)==TYPE_ITEM);
    }

    class ViewHolder {
        private TextView chapter;
        private ImageView chapter_thumb;
        private ImageView chapter_arrow;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
     //   final ViewHolder viewHolder;
        int rowType = getItemViewType(position);
        viewHolder = new ViewHolder();
        if (view == null) {

            switch (rowType) {
                case TYPE_ITEM:
                    view = mInflater.inflate(R.layout.content_layout, viewGroup,false);
                    /*viewHolder.chapter = (TextView) view.findViewById(R.id.chapter_tv);
                    viewHolder.chapter_thumb=(ImageView)view.findViewById(R.id.chapter_thumb_iv);*/
                    break;
                case TYPE_SEPARATOR:
                    view = mInflater.inflate(R.layout.list_group, viewGroup,false);
                    // viewHolder.chapter=(TextView)view.findViewById(R.id.expand_title);
                    break;
            }


        switch (rowType){
            case TYPE_ITEM:
              DataModel  content = (DataModel) getItem(position);
                viewHolder.chapter = (TextView) view.findViewById(R.id.chapter_tv);
                viewHolder.chapter_thumb=(ImageView)view.findViewById(R.id.chapter_thumb_iv);
                chapterName=content.getName();
                chapterImage=content.getValue();
                chapterImage1=content.getApp_id();
                appfolder=content.getApp_id();
                viewHolder.chapter.setText(chapterName);
                Log.d("TAGG+++",chapterName+","+chapterImage+","+chapterImage1);
                Glide.with(context).load(chapterImage+"/"+appfolder+"_thumbnail"+".png").into(viewHolder.chapter_thumb);
                break;
            case TYPE_SEPARATOR:
                TextView mTextView=(TextView)view.findViewById(R.id.expand_title);
                String titleString = (String)getItem(position);
                mTextView.setText(titleString);
                break;
        }
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();

        }
        return view;

    }

}
