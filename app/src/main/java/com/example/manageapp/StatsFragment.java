package com.example.manageapp;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import im.dacer.androidcharts.BarView;


public class StatsFragment extends Fragment {

    final String KEY_ID = "id";
    final String KEY_TASK = "task";
    final String KEY_DATE = "date";

    private ArrayList<HashMap<String, String>> stats = new ArrayList<>();
    BarView barView;
    TaskDBHelper mydb;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barView = view.findViewById(R.id.chart);
        mydb = new TaskDBHelper(this.getContext());
        new LoadTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    class LoadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            stats.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            Cursor today = mydb.getDataOrderByDate();
            loadDataList(today, stats);

            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            HashMap<String, Integer> entries = new HashMap<>();
            ArrayList<String> bottomTexts = new ArrayList<>();
            ArrayList<Integer> dataList = new ArrayList<>();
            for(int i = 0; i < stats.size(); i++) {
                String date = stats.get(i).get(KEY_DATE);
                if(!entries.containsKey(date)) {
                    entries.put(date, 1);
                }
                else {
                    int total = entries.get(date);
                    entries.put(date, total+1);
                }
            }
            Iterator it =  entries.entrySet().iterator();
            int max = 0;
            while(it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                bottomTexts.add(pair.getKey().toString());
                int total = Integer.parseInt(pair.getValue().toString());
                if(max < total) {
                    max = total;
                }
                dataList.add(total);
                it.remove();
            }
            barView.setBottomTextList(bottomTexts);
            barView.setDataList(dataList, max);
        }
    }


    public void loadDataList(Cursor cursor, ArrayList<HashMap<String, String>> dataList)
    {
        if(cursor!=null ) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                HashMap<String, String> mapToday = new HashMap<String, String>();
                mapToday.put(KEY_ID, cursor.getString(0).toString());
                mapToday.put(KEY_TASK, cursor.getString(1).toString());
                mapToday.put(KEY_DATE, Function.Epoch2DateString(cursor.getString(2).toString(), "dd-MM-yyyy"));
                dataList.add(mapToday);
                cursor.moveToNext();
            }
        }
    }
}
