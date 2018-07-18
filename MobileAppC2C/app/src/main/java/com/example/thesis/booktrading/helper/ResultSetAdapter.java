package com.example.thesis.booktrading.helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.thesis.booktrading.R;
import com.example.thesis.booktrading.gnutellaprotocol.ResultSet;

public class ResultSetAdapter extends BaseAdapter {

    Activity activity;

    private ResultSet rs;

    public ResultSetAdapter(Activity activity, ResultSet rs)
    {
        super();
        this.activity = activity;
        this.rs = rs;
    }
    @Override
    public int getCount() {
        return rs.getSize();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView textView_index;
        TextView textView_filename;
        TextView textView_ip;
        TextView textView_Size;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_resultset_gnutella, null);
            holder = new ViewHolder();
            holder.textView_index = (TextView) convertView.findViewById(R.id.textView_index);
            holder.textView_filename = (TextView) convertView.findViewById(R.id.textView_filename);
            holder.textView_ip = (TextView) convertView.findViewById(R.id.textView_ip);
            holder.textView_Size = (TextView) convertView.findViewById(R.id.textView_Size);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView_index.setText(rs.getIndex());
        holder.textView_filename.setText(rs.getFilesize());


        return convertView;
    }
}
