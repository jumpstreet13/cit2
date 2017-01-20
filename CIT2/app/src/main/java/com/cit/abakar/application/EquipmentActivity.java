package com.cit.abakar.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import static com.cit.abakar.application.MainActivity.hasConnection;

public class EquipmentActivity extends Activity {

   private ListView listView;
   private ArrayList<String> arr = new ArrayList<String>();
   private MyMediaPlayer myMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        if(!hasConnection(this)) {
            getActionBar().setTitle("Список оборудования (оффлайн)");
        }else{
            getActionBar().setTitle("Список оборудования (онлайн)");
        }
        for(int i = 0; i<10; i++){
            arr.add("Оборудование "+i+"");
        }
        listView = (ListView) findViewById(R.id.listViewEquipment);
        MyAdapter adapter = new MyAdapter(this, arr);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myMediaPlayer = new MyMediaPlayer(EquipmentActivity.this, R.raw.button_sound);
                myMediaPlayer.start();
                myMediaPlayer.setFree();
                Intent intent = new Intent(EquipmentActivity.this, EquipmentStateActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!hasConnection(this)) {
            getActionBar().setTitle("Список узлов (оффлайн)");
        }else{
            getActionBar().setTitle("Список узлов (онлайн)");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
