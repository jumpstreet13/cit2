package com.cit.abakar.application;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import com.cit.abakar.application.ExampleClasses.Equipment;

import java.util.ArrayList;



public class MyAdapter extends BaseAdapter {

    private ArrayList<Equipment> data = new ArrayList<Equipment>();
    private ArrayList<Integer> veryfied = new ArrayList<Integer>();
    private Context context;
    private ImageButton buttonInstallation;
    private ImageButton buttondeinstallation;
    private AdapterInterface adapterInterface;

   /* public void setActivity(AdapterInterface adapterInterface){
        this.adapterInterface =  adapterInterface;
    }*/


    public MyAdapter(Context context, ArrayList<Equipment> data, ArrayList<Integer> veryfied) {
        this.data = data;
        this.context = context;
        this.veryfied = veryfied;
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

        if (convertView == null) {
            //convertView = inflater.inflate(R.layout.mylist, parent, false);
            convertView = inflater.inflate(R.layout.mylist_forequipments, parent, false);
        }
        Button main = (Button) convertView.findViewById(R.id.textViewForEquipments);
        buttonInstallation = (ImageButton) convertView.findViewById(R.id.button_Installation);
        buttondeinstallation = (ImageButton) convertView.findViewById(R.id.button_Deinstallation);
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

        if (data.get(position).fgNotInstall.equals("false")) {
            buttonInstallation.setEnabled(false);
            //   buttonInstallation.setBackground(context.getDrawable(R.drawable.ic_build_black_24dpdisabled));
            buttondeinstallation.setEnabled(true);
            main.setEnabled(true);
        }

        if (data.get(position).fgNotInstall.equals("true")) {
            buttonInstallation.setEnabled(true);
            buttondeinstallation.setEnabled(false);
            //buttondeinstallation.setBackground(context.getDrawable(R.drawable.ic_delete_forever_black_24dpcopy));
            main.setEnabled(false);
        }

        for(int i = 1; i<veryfied.size(); i++){
            if(data.get(position).id == veryfied.get(i)){
                main.setEnabled(false);
                break;
            }
        }

        return convertView;
    }

}
