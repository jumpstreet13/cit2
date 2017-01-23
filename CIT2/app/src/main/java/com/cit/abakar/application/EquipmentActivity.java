package com.cit.abakar.application;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.cit.abakar.application.database.Directory_Equipment_Condition;

import java.util.ArrayList;
import java.util.zip.Inflater;

import static com.cit.abakar.application.MainActivity.hasConnection;

public class EquipmentActivity extends Activity implements AdapterInterface, MultiSelectionSpinner.MultiSpinnerListener {

   private ListView listView;
   private ArrayList<String> arr = new ArrayList<String>();
   private MyMediaPlayer myMediaPlayer;
   private MultiSelectionSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        if(!hasConnection(this)) {
            getActionBar().setTitle(R.string.ActionBarISOfflineEquipmentActivity);
        }else{
            getActionBar().setTitle(R.string.ActionBarIsOnlineEquipmentActivity);
        }
        for(int i = 0; i<10; i++){
            arr.add("Оборудование "+i+"");
        }
        listView = (ListView) findViewById(R.id.listViewEquipment);
        spinner = (MultiSelectionSpinner) findViewById(R.id.spinnerInEquipmentActivity);
        MyAdapter adapter = new MyAdapter(this, arr);
        adapter.setActivity(this);
        listView.setAdapter(adapter);
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(myMediaPlayer != null) {
                    myMediaPlayer.reset();
                }
                myMediaPlayer = new MyMediaPlayer(EquipmentActivity.this, "Button");
                myMediaPlayer.start();
                myMediaPlayer.setFree();
                Intent intent = new Intent(EquipmentActivity.this, EquipmentStateActivity.class);
                startActivity(intent);
            }
        });*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!hasConnection(this)) {
            getActionBar().setTitle(R.string.ActionBarISOfflineEquipmentActivity);
        }else{
            getActionBar().setTitle(R.string.ActionBarIsOnlineEquipmentActivity);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void textViewClicked() {
        if(myMediaPlayer != null) {
            myMediaPlayer.reset();
        }
        myMediaPlayer = new MyMediaPlayer(EquipmentActivity.this, "Button");
        myMediaPlayer.start();
        myMediaPlayer.setFree();
        Intent intent = new Intent(EquipmentActivity.this, EquipmentStateActivity.class);
        startActivity(intent);

    }

    @Override
    public void installationButtonClicked() {
        if(myMediaPlayer != null) {
            myMediaPlayer.reset();
        }
        myMediaPlayer = new MyMediaPlayer(EquipmentActivity.this, "Button");
        myMediaPlayer.start();
        myMediaPlayer.setFree();

        final Dialog dialog = new Dialog(EquipmentActivity.this);
        dialog.setContentView(R.layout.equipment_dialog);
        dialog.setTitle("Введите номер акта");
        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        Button sendButton = (Button) dialog.findViewById(R.id.buttonSendNumberOfAkt);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ed = (EditText) dialog.findViewById(R.id.editTextInInstallationDialog);
                dialog.dismiss();
            }
        });


    }

    @Override
    public void deinstallationButtonClicked() {
        if(myMediaPlayer != null) {
            myMediaPlayer.reset();
        }
        myMediaPlayer = new MyMediaPlayer(EquipmentActivity.this, "Button");
        myMediaPlayer.start();
        myMediaPlayer.setFree();

    }

    @Override
    public void onItemsSelected(boolean[] selected) {

    }
}
