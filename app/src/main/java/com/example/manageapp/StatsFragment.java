package com.example.manageapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class StatsFragment extends Fragment {

    final String KEY_ID = "id";
    final String KEY_TASK = "task";
    final String KEY_DATE = "date";
    final String KEY_STATUS = "status";
//    final String KEY_DESCRIPTION = "description";

    private ArrayList<HashMap<String, String>> stats = new ArrayList<>();
    private List<PieEntry> entries = new ArrayList<>();

    BarChart barChart;
    PieChart pieChart;
    TaskDBHelper mydb;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barChart = view.findViewById(R.id.chart);
        pieChart = view.findViewById(R.id.pie_chart);
        mydb = new TaskDBHelper(this.getContext());
        new LoadTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }


//load data piechart

    class LoadDoneTask extends AsyncTask<String, Void, String>{

       @Override
        protected  void onPreExecute(){
            super.onPreExecute();
            entries.clear();
        }
        @Override
        protected String doInBackground(String... strings) {
            String xml ="";
            Cursor task_done = mydb.getStatusDataDone();
            loadDataDoneTask(task_done, entries);
            return xml;
        }
//when load done
        protected void onPostExecute(){

        }
//Load done task
        protected void loadDataDoneTask(Cursor task_done, List<PieEntry> entries) {
           ArrayList<HashMap<String, String>> list= new ArrayList<>();
            if(task_done!=null ) {
                task_done.moveToFirst();
                while (!task_done.isAfterLast()) {
                    HashMap<String, String> mapToday = new HashMap<String, String>();
                    mapToday.put("taskdone", task_done.getString(0).toString());
                    list.add(mapToday);
                    task_done.moveToNext();
                }
            }
           for (int i = 0; i < list.size(); i++) {
               entries.add(new PieEntry(Float.parseFloat(list.get(i).get("taskdone")), i+""));
           }
            PieDataSet set = new PieDataSet(entries, "Status done");
            PieData data = new PieData(set);
            pieChart.setData(data);
            pieChart.invalidate();
        }

    }


    //load data barchart
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

        //when data load done


        //
        @Override
        protected void onPostExecute(String xml) {
            Collections.sort(stats, new Comparator<HashMap<String, String>>() {
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                @Override
                public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                    try {
                        return format.parse(o1.get(KEY_DATE)).compareTo(format.parse(o2.get(KEY_DATE)));
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });

            final List<BarEntry> entries = new ArrayList<>();
            final Map<String, Integer> entriesMap = new TreeMap<>();
            final List<String> entriesLabel = new ArrayList<>();
            for(int i = 0; i < stats.size(); i++) {
                String date = stats.get(i).get(KEY_DATE);
                if(!entriesMap.containsKey(date)) {
                    entriesMap.put(date, 1);
                } else {
                    int total = entriesMap.get(date);
                    entriesMap.put(date, total+1);
                }
            }
            Iterator it = entriesMap.entrySet().iterator();
            int index = 0;
            while(it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                int total = (int)pair.getValue();
                entries.add(new BarEntry(index, total));
                entriesLabel.add((String)pair.getKey());
                index++;
            }

            BarDataSet set = new BarDataSet(entries, "Tasks");
            BarData data = new BarData(set);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    int index = (int)value;
                    return entriesLabel.get(index);
                }
            });
            barChart.getXAxis().setLabelRotationAngle(-45);
            barChart.setData(data);
            barChart.invalidate();
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
                mapToday.put(KEY_STATUS, cursor.getString(3).toString());
//                mapToday.put(KEY_DESCRIPTION,cursor.getString(4).toString());
                dataList.add(mapToday);
                cursor.moveToNext();
            }
        }
    }
}



