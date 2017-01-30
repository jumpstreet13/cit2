package com.cit.abakar.application;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cit.abakar.application.ExampleClasses.Equipment;

import java.util.ArrayList;


public class MyAdapter extends BaseAdapter {

    private ArrayList<Equipment> data = new ArrayList<Equipment>();
    private Context context;
    private Button buttonInstallation;
    private Button buttondeinstallation;
    private AdapterInterface adapterInterface;

   /* public void setActivity(AdapterInterface adapterInterface){
        this.adapterInterface =  adapterInterface;
    }*/


    public MyAdapter(Context context, ArrayList<Equipment> data){
        this.data = data;
        this.context = context;
        adapterInterface = (AdapterInterface) context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if(convertView == null){
            //convertView = inflater.inflate(R.layout.mylist, parent, false);
            convertView = inflater.inflate(R.layout.mylist_forequipments, parent, false);
        }
        Button main = (Button) convertView.findViewById(R.id.textViewForEquipments);
        buttonInstallation = (Button) convertView.findViewById(R.id.button_Installation);
        buttondeinstallation = (Button) convertView.findViewById(R.id.button_Deinstallation);
       // buttondeinstallation.setElevation(5);
       // buttonInstallation.setElevation(5);
        main.setText(data.get(position).name);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterInterface.textViewClicked(position);
            }
        });

        buttonInstallation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CLICK", "button installation has clicked");
                adapterInterface.installationButtonClicked(position);
            }
        });
        buttondeinstallation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CLICK", "button de]installation has clicked");
                adapterInterface.deinstallationButtonClicked(position);

            }
        });

        if(data.get(position).fgNotInstall.equals("true")){
            buttondeinstallation.setEnabled(false);
            main.setEnabled(false);
        }

        if(data.get(position).fgDismantled.equals("true")){
            main.setEnabled(false);
            buttondeinstallation.setEnabled(false);
            buttonInstallation.setEnabled(false);
        }

        return convertView;
    }

}
