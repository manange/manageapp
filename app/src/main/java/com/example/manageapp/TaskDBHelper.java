package com.example.manageapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 */

public class TaskDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ToDoDBHelper.db";
    public static final String CONTACTS_TABLE_NAME = "todo";

    public TaskDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL(
                "CREATE TABLE "+CONTACTS_TABLE_NAME +
                        "(id INTEGER PRIMARY KEY, task TEXT, dateStr INTEGER, status TEXT DEFAULT ('false'), description TEXT, alarm INTEGER )"
//                CREATE TABLE foo(mycolumn BOOLEAN NOT NULL CHECK (mycolumn IN (0,1)));
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        onCreate(db);
    }


    private long getDate(String day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        try {
            date = dateFormat.parse(day);
        } catch (ParseException e) {
        }
        return date.getTime();
    }


    public boolean insertContact(String task, String dateStr, String description) {
        Date date;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("dateStr", getDate(dateStr));
        contentValues.put("description", description);
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateContact(String id, String task, String dateStr, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("dateStr", getDate(dateStr));
        contentValues.put("description", description);

        db.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ", new String[]{id});
        return true;
    }

    public Cursor getDataOrderByDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " order by date(dateStr) desc", null);
        return res;
    }



        public Cursor getData () {

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " order by id desc", null);
            return res;
        }



        public Cursor getDataSpecific (String id){
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " WHERE id = '" + id + "' order by date(dateStr) asc", null);

            return res;

        }

        public Cursor getDataYesterday () {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME +
                    " WHERE status = 'false' AND date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) <= date('now','-1 day' , 'localtime') order by date(dateStr) desc", null);

            return res;

        }

        public Cursor getDataToday () {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME +
                    "  WHERE status = 'false' AND date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', 'localtime') order by date(dateStr) desc", null);

            return res;

        }


        public Cursor getDataTomorrow () {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME +
                    " WHERE status = 'false' AND date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', '+1 day', 'localtime')  order by date(dateStr) desc", null);

            return res;

        }


        public Cursor getDataUpcoming () {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME +
                    " WHERE status = 'false' AND date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) > date('now', '+1 day', 'localtime') order by date(dateStr) desc", null);
            return res;

        }

        public Cursor getDataDone () {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME +
                    " WHERE status = 'true' order by date(dateStr) desc", null);

            return res;

        }

    public Cursor getStatusDataDone () {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(status) AS taskdone from " + CONTACTS_TABLE_NAME +
                " WHERE status = 'true' ", null);

        return res;

    }

    }

