package com.focusmedica.aqrshell.dictionary;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.focusmedica.aqrshell.R;

import java.util.ArrayList;


public class Sorting extends Fragment {
    ListView alphabetview;
    OnHeadlineSelectedListener mCallback;
    Customlistforalphabets adapter;
    String first;
Mydatabase mydatabase;
    ArrayList<DIctionaryContent> alphabets=new ArrayList<>();
    String s;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sorting, container, false);
        alphabetview=(ListView)rootView.findViewById(R.id.alpha);
        mydatabase=new Mydatabase(getActivity().getApplicationContext(),"");
        mydatabase.openDataBase();

        final ArrayList<DIctionaryContent> chapter = mydatabase.getAlphabets();
        adapter=new Customlistforalphabets(getActivity(),chapter);
        alphabetview.setAdapter(adapter);
        alphabetview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                first=adapter.getItem(i).toString();
                TextView tv=(TextView)view.findViewById(R.id.textView3);
                String st=tv.getText().toString();

                mCallback.onArticleSelected(st);
                adapter.listselected(i);
            }
        });
        mydatabase.close();
        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(String firstcharacter);
    }
}
