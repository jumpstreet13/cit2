package com.cit.abakar.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class MyAdapter extends BaseAdapter {

    ArrayList<String> data = new ArrayList<String>();
    Context context;

    public MyAdapter(Context context, ArrayList<String> data){
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.mylist, parent, false);
            //convertView = inflater.inflate(R.layout.mylist_forequipments, parent, false);
        }
        TextView main = (TextView) convertView.findViewById(R.id.myListTextView);

        main.setText(data.get(position));

        return convertView;
    }
}
