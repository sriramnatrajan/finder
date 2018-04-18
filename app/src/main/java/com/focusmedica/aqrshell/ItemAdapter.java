package com.focusmedica.aqrshell;

import android.content.Context;
import android.database.Cursor;
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
    ArrayList<DataModel> listcontent;
    Context context;
    DataModel content;
    String url;
    String chapterName;
    String chapterImage;
    String chapterImage1;
    Cursor mCursor;
    public ItemAdapter(Context context, ArrayList listcontent) {
        this.context = context;

        this.listcontent = listcontent;
    }


    @Override
    public int getCount() {
        return listcontent.size();
    }

    @Override
    public Object getItem(int position) {
        return listcontent.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        private TextView chapter;
        private ImageView chapter_thumb;
        private ImageView chapter_arrow;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.content_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.chapter = (TextView) view.findViewById(R.id.chapter_tv);
            viewHolder.chapter_thumb=(ImageView)view.findViewById(R.id.chapter_thumb_iv);
         //   viewHolder.chapter_arrow=(ImageView)view.findViewById(R.id.image_arrow);

            //  viewHolder.chapter.getResources().getColor(R.color.md_black_1000);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        content = (DataModel) listcontent.get(position);
        Log.d("TAGG+++",chapterName+","+chapterImage+","+chapterImage1);
        chapterName=content.getName();
        chapterImage=content.getValue();
        chapterImage1=content.getApp_id();
        viewHolder.chapter.setText(chapterName);
        Log.d("TAGG+++",chapterName+","+chapterImage+","+chapterImage1);

        // viewHolder.chapter.setTextColor(000000);
        Log.d("TAGG+++",chapterImage+"/"+chapterImage1+"_thumbnail.png");
        Glide.with(context).load(chapterImage+"/"+chapterImage1+"_thumbnail"+".png").into(viewHolder.chapter_thumb);
        return view;
    }
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }
}
