package com.focusmedica.aqrshell;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by windev on 8/23/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable {

    private ArrayList<DataModel> dataSet;
    private ArrayList<DataModel> orig;
    Context context;
    String url;DataModel content;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvChapterName;
        ImageView ivChapterIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tvChapterName = (TextView) itemView.findViewById(R.id.chapter_tv);
            this.ivChapterIcon = (ImageView) itemView.findViewById(R.id.chapter_thumb_iv);
        }
    }
    public SearchAdapter(Context context, ArrayList<DataModel> data) {
        this.dataSet = data;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_layout, parent, false);
      //  view.setOnClickListener(MainActivity.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public long getItemId(int position) {
        int itemID;
        // orig will be null only if we haven't filtered yet:
        if (orig == null) {
            itemID = position;
        } else {
            itemID = orig.indexOf(dataSet.get(position));
        }
        return itemID;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView tvChapterName = holder.tvChapterName;
        ImageView ivChapterIcon = holder.ivChapterIcon;
        content = (DataModel) dataSet.get(listPosition);
        String chapterName=content.getTitleName();
        String chapterImage=content.getImageName();
        String iconName = content.getDlink();
        tvChapterName.setText(chapterName);
        Picasso.with(context).load(iconName+"/thumbnail"+".png").into(ivChapterIcon);
        Log.d("TAG",iconName+"/thumbnail"+".png");
        //ivChapterIcon.setImageResource(R.drawable.caticon);
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<DataModel> results = new ArrayList<DataModel>();
                if (orig == null)
                    orig = dataSet;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final DataModel g : orig) {
                            if (g.getTitleName().toLowerCase().contains(constraint.toString().toLowerCase())||
                                    g.getTitleName().toUpperCase().contains(constraint.toString().toUpperCase()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                dataSet = (ArrayList<DataModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}

