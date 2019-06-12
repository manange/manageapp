package com.example.manageapp;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */

interface ListTaskAdapterItemClickCallback {
    void onClickCallback(String query);
}

public class ListTaskAdapter extends BaseAdapter {
    private TaskFragment activity;
    private ArrayList<HashMap<String, String>> data;
    ListTaskAdapterItemClickCallback callback;
    ListTaskAdapterItemClickCallback task_done_callback;
// delete callback
    public void setCallback(ListTaskAdapterItemClickCallback callback) {
        this.callback = callback;
    }
//task done callback
    public void setTask_done_callback(ListTaskAdapterItemClickCallback task_done_callback) {
        this.task_done_callback = task_done_callback;
    }

    public ListTaskAdapter(TaskFragment a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
    }
    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ListTaskViewHolder holder = null;
        if (convertView == null) {
            holder = new ListTaskViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_row, parent, false);
            holder.task_delete = (ImageView) convertView.findViewById(R.id.task_delete);
            holder.task_image = (TextView) convertView.findViewById(R.id.task_image);
            holder.task_name = (TextView) convertView.findViewById(R.id.task_name);
            holder.task_date = (TextView) convertView.findViewById(R.id.task_date);
            holder.task_done = (CheckBox) convertView.findViewById(R.id.task_done);
            holder.task_description = (TextView) convertView.findViewById(R.id.task_description) ;


            convertView.setTag(holder);
        } else {
            holder = (ListTaskViewHolder) convertView.getTag();
        }
        holder.task_delete.setId(position);
        holder.task_image.setId(position);
        holder.task_name.setId(position);
        holder.task_date.setId(position);
        holder.task_done.setId(position);
        holder.task_description.setId(position);

        final HashMap<String, String> song = data.get(position);

        try{
            holder.task_name.setText(song.get(TaskFragment.KEY_TASK));
            holder.task_date.setText(song.get(TaskFragment.KEY_DATE));
            holder.task_description.setText(song.get(TaskFragment.KEY_DESCRIPTION));

            /* Image */
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            holder.task_image.setTextColor(color);
            holder.task_image.setText(Html.fromHtml("&#11044;"));
            /* Image */
            holder.task_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String deleteTodoItemSql = "DELETE FROM " + TaskDBHelper.CONTACTS_TABLE_NAME +
                            " WHERE id = '" + song.get("id") + "'";
                    callback.onClickCallback(deleteTodoItemSql);
                }
            });

            //task done
            holder.task_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String doneTodoItemSql = "UPDATE " + TaskDBHelper.CONTACTS_TABLE_NAME + " SET status = 'true' " + " WHERE id = '" + song.get("id") + "' ";

                    if(task_done_callback!=null)
                        task_done_callback.onClickCallback(doneTodoItemSql);

//                    if(((CheckBox)v).isChecked()){
//                        Toast.makeText(ListTaskAdapter.this,"Task have done", Toast.LENGTH_LONG).show();
//                    }
                }

                });


        }catch(Exception e) {}
        return convertView;


    }

}

class ListTaskViewHolder {
    ImageView task_delete;
    TextView task_image;
    TextView task_name, task_date;
    TextView task_description;
    CheckBox task_done;
}