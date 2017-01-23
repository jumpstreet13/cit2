package com.cit.abakar.application;

import android.app.Activity;
import android.content.Context;
import android.icu.text.UnicodeSetSpanner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.TooManyListenersException;


public class MyAdapter extends BaseAdapter {

    private ArrayList<String> data = new ArrayList<String>();
    private Context context;
    private Button buttonInstallation;
    private Button buttondeinstallation;
    private AdapterInterface adapterInterface;

    public void setActivity(AdapterInterface adapterInterface){
        this.adapterInterface =  adapterInterface;
    }


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
            //convertView = inflater.inflate(R.layout.mylist, parent, false);
            convertView = inflater.inflate(R.layout.mylist_forequipments, parent, false);
        }
        TextView main = (TextView) convertView.findViewById(R.id.textViewForEquipments);
        buttonInstallation = (Button) convertView.findViewById(R.id.button_Installation);
        buttondeinstallation = (Button) convertView.findViewById(R.id.button_Deinstallation);
        main.setText(data.get(position));

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterInterface.textViewClicked();
            }
        });

        buttonInstallation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CLICK", "button installation has clicked");
                adapterInterface.installationButtonClicked();
            }
        });
        buttondeinstallation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CLICK", "button de]installation has clicked");

            }
        });

        return convertView;
    }
}
