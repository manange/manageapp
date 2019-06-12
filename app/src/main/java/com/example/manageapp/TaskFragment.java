package com.example.manageapp;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import android.support.v4.app.Fragment;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class TaskFragment extends Fragment  {
    TaskFragment activity;
    TaskDBHelper mydb;
    NoScrollListView taskListYesterday,taskListToday, taskListTomorrow, taskListUpcoming, taskListDone;
    NestedScrollView scrollView;
    ImageView img_add;
    ProgressBar loader;
    TextView yesterdayText,todayText,tomorrowText,upcomingText, doneText;

    ArrayList<HashMap<String, String>> todayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tomorrowList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> upcomingList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> yesterdayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> doneList = new ArrayList<HashMap<String, String>>();

    public static String KEY_ID = "id";
    public static String KEY_TASK = "task";
    public static String KEY_DATE = "date";
    public  static String KEY_DESCRIPTION = "description";
//place your activity here
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = TaskFragment.this;
        mydb = new TaskDBHelper(this.getContext());
        scrollView =  view.findViewById(R.id.scrollView);
        loader =  view.findViewById(R.id.loader);
        taskListToday = view.findViewById(R.id.taskListToday);
        taskListTomorrow = view.findViewById(R.id.taskListTomorrow);
        taskListUpcoming = view.findViewById(R.id.taskListUpcoming);
        taskListYesterday = view.findViewById(R.id.taskListYesterday);
        taskListDone = view.findViewById(R.id.taskListDone);


        todayText = view.findViewById(R.id.todayText);
        tomorrowText = view.findViewById(R.id.tomorrowText);
        upcomingText = view.findViewById(R.id.upcomingText);
        yesterdayText = view.findViewById(R.id.yesterdayText);
        doneText = view.findViewById(R.id.doneText);
        img_add = view.findViewById(R.id.img_add);
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTask();
            }
        });

    }


    public void openAddTask()
    {
        Intent i = new Intent(getActivity(), AddTask.class);
        startActivity(i);
    }

    public void populateData()
    {
        mydb = new TaskDBHelper(this.getContext());
        scrollView.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        LoadTask loadTask = new LoadTask();
        loadTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        populateData();

    }





    class LoadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            todayList.clear();
            tomorrowList.clear();
            upcomingList.clear();
            yesterdayList.clear();
            doneList.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";
            /* ===== TODAY ========*/
            Cursor today = mydb.getDataToday();
            loadDataList(today, todayList);
            /* ===== TODAY ========*/

            /* ===== TOMORROW ========*/
            Cursor tomorrow = mydb.getDataTomorrow();
            loadDataList(tomorrow, tomorrowList);
            /* ===== TOMORROW ========*/

            /* ===== UPCOMING ========*/
            Cursor upcoming = mydb.getDataUpcoming();
            loadDataList(upcoming, upcomingList);
            /* ===== UPCOMING ========*/

            Log.d("DEBUG GAN","Im here");
            /* ===== YESTERDAY ========*/
            Cursor yesterday = mydb.getDataYesterday();
            loadDataList(yesterday, yesterdayList);
            /* ===== YESTERDAY ========*/

            /* ===== DONE ========*/
            Cursor done = mydb.getDataDone();
            loadDataList(done, doneList);
            /* ===== DONE ========*/
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {


            loadListView(taskListToday,todayList);
            loadListView(taskListTomorrow,tomorrowList);
            loadListView(taskListUpcoming,upcomingList);
            loadListView(taskListYesterday,yesterdayList);
            loadListView(taskListDone,doneList);

            if(todayList.size()>0)
            {
                todayText.setVisibility(View.VISIBLE);
            }else{
                todayText.setVisibility(View.GONE);
            }

            if(tomorrowList.size()>0)
            {
                tomorrowText.setVisibility(View.VISIBLE);
            }else{
                tomorrowText.setVisibility(View.GONE);
            }

            if(upcomingList.size()>0)
            {
                upcomingText.setVisibility(View.VISIBLE);
            }else{
                upcomingText.setVisibility(View.GONE);
            }

            if (yesterdayList.size()>0){
                yesterdayText.setVisibility(View.VISIBLE);
            } else {yesterdayText.setVisibility(View.GONE);

            }

            if (doneList.size()>0){
                doneText.setVisibility(View.VISIBLE);
            } else {doneText.setVisibility(View.GONE);

            }


            loader.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }



    public void loadDataList(Cursor cursor, ArrayList<HashMap<String, String>> dataList)
    {
        if(cursor!=null ) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                HashMap<String, String> mapToday = new HashMap<String, String>();
                mapToday.put(KEY_ID, cursor.getString(0).toString());
                mapToday.put(KEY_TASK, cursor.getString(1).toString());
                mapToday.put(KEY_DATE, Function.Epoch2DateString(cursor.getString(2).toString(), "dd-MM-yyyy"));
                mapToday.put(KEY_DESCRIPTION, cursor.getString(4).toString());
                dataList.add(mapToday);
                cursor.moveToNext();
            }
            Collections.sort(dataList, new Comparator<HashMap<String, String>>() {
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
        }

    }

    public void loadListView(ListView listView, final ArrayList<HashMap<String, String>> dataList)
    {
        ListTaskAdapter adapter = new ListTaskAdapter(activity, dataList);
        adapter.setCallback(new ListTaskAdapterItemClickCallback() {
            @Override
            public void onClickCallback(String query) {
                mydb.getWritableDatabase().execSQL(query);
                populateData();
            }
        });
        adapter.setTask_done_callback(new ListTaskAdapterItemClickCallback() {
            @Override
            public void onClickCallback(String query) {
                mydb.getWritableDatabase().execSQL(query);
                populateData();
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(TaskFragment.this.getActivity(), AddTask.class);
                i.putExtra("isUpdate", true);
                i.putExtra("id", dataList.get(+position).get(KEY_ID));
                startActivity(i);
                //        Intent myIntent = new Intent(MyFragment.this.getActivity(), MyClass.class);
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        return view;
    }
}

