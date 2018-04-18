package com.focusmedica.aqrshell.dictionary;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.focusmedica.aqrshell.R;

/**
 * Created by Lokesh on 15-Feb-16.
 */
public class Customlistforalphabets extends BaseAdapter {

    Context context;
    int selected=0;
    String[] character={"A","B","C","D","G","H",
                        "I","L","M",
                        "O","P","R","S","T","U", "W" };

    public Customlistforalphabets(Context context){
        this.context = context;

    }

    @Override
    public int getCount() {
        return character.length;
    }

    @Override
    public Object getItem(int position) {
        return character[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void listselected(int i) {
        selected=i;
        notifyDataSetChanged();
    }

    class ViewHolder {
        private TextView alpha;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final  ViewHolder viewHolder;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.customlist_alpha,null);
            viewHolder = new ViewHolder();
            viewHolder.alpha = (TextView) view.findViewById(R.id.textView3);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        if(selected==position)
            view.setBackgroundColor(Color.parseColor("#ff0000"));
        else
            view.setBackgroundColor(Color.parseColor("#413A35"));

        viewHolder.alpha.setText(character[position]);
        return view;
    }
}
