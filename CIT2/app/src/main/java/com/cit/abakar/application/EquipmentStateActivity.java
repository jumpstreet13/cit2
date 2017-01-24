package com.cit.abakar.application;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cit.abakar.application.database.Equipment;

import java.util.ArrayList;

import static com.cit.abakar.application.MainActivity.hasConnection;


public class EquipmentStateActivity extends Activity implements MultiSelectionSpinner.MultiSpinnerListener {

   private Button button1, button2;
   private Switch switch1, switch2;
   private MultiSelectionSpinner spinner;
   private ArrayList<String> ar = new ArrayList<String>();
   private MyMediaPlayer myMediaPlayer;
   private MenuItem connection;
   private Equipment equipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_state);
        getActionBar().setTitle(R.string.ActionBarISOfflineEquipmentStateActivity);
        Log.e("ZZ",getIntent().getStringExtra("idOfEquipment"));
        equipment = Equipment.getById(getIntent().getStringExtra("idOfEquipment"));
        Log.e("ZZ", equipment.fg_dismantled + " " + equipment.fg_not_install+ " ");

        button1 = (Button) findViewById(R.id.buttonInEquipmentState);
        button2 = (Button) findViewById(R.id.button2InEquipmentState);
        switch1 = (Switch) findViewById(R.id.switch1InEquipmentState);
        switch2 = (Switch) findViewById(R.id.switch2InEquipmentState);
        spinner = (MultiSelectionSpinner) findViewById(R.id.spinnerInEquipmentStateActivity);
        if(equipment.fg_dismantled.equals("true")){
            switch1.setChecked(false);
        }else{
            switch1.setChecked(true);
        }
        if(equipment.fg_not_install.equals("true")){
            switch2.setChecked(false);
        }else{
            switch1.setChecked(true);
        }
        ar.add("Сломан кулер");
        ar.add("Не работает автофокус");
        ar.add("Перегрев процессора");
        ar.add("Другое");
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.forSpinner, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       //spinner.setAdapter(adapter);
        spinner.setItems(ar,getString(R.string.ChooseTheReason),this);
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer = new MyMediaPlayer(EquipmentStateActivity.this, "Switch");
                myMediaPlayer.start();
                myMediaPlayer.setFree();
            }
        });

        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer = new MyMediaPlayer(EquipmentStateActivity.this, "Switch");
                myMediaPlayer.start();
                myMediaPlayer.setFree();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer = new MyMediaPlayer(EquipmentStateActivity.this, "Button");
                myMediaPlayer.start();
                myMediaPlayer.setFree();
                button1.setEnabled(false);
                switch1.setEnabled(false);
                switch2.setEnabled(false);
                switch2.setChecked(true);
                button2.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myMediaPlayer = new MyMediaPlayer(EquipmentStateActivity.this,"Button");
                        myMediaPlayer.start();
                        myMediaPlayer.setFree();
                        //Log.e("getSelected", s);
                        if(spinner.getSelectedItem().toString().equals("") ||
                                spinner.getSelectedItem().toString().equals("Выбрать причину")){
                            Toast toast = Toast.makeText(EquipmentStateActivity.this, R.string.ToastYouDoNotChosenAnything,Toast.LENGTH_SHORT);
                            toast.show();
                            return;

                        }
                        Toast toast = Toast.makeText(EquipmentStateActivity.this,spinner.getSelectedItem().toString(),Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(EquipmentStateActivity.this, EquipmentActivity.class);
                        intent.putExtra("id", getIntent().getStringExtra("id"));
                        startActivity(intent);
                    }
                });
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.search_settings:
                return true;
            case R.id.synchronize:
                // ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarInMainActivity);
                // progressBar.setVisibility(View.VISIBLE);
                Log.e("Sync", "syncronize is going on");
                return true;
            case R.id.htttp_settings:
                final Dialog dialog = new Dialog(EquipmentStateActivity.this, R.style.DialogTheme);
                dialog.setContentView(R.layout.urldialog);
                dialog.setTitle("Введите новый URL");
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                TextView textView = (TextView) dialog.findViewById(R.id.textViewinMainActivityDialog);
                textView.setText(sharedPref.getString(MainActivity.URLSETTINS, getString(R.string.Adress_is_not_set_yet)));
                dialog.show();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                Button sendButton = (Button) dialog.findViewById(R.id.buttoninMainActivityDialog);
                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myMediaPlayer = new MyMediaPlayer(EquipmentStateActivity.this, "Button");
                        myMediaPlayer.start();
                        myMediaPlayer.setFree();
                        EditText editText = (EditText) dialog.findViewById(R.id.editTextinMainActivityDialog);
                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(MainActivity.URLSETTINS, editText.getText().toString());
                        editor.commit();
                        dialog.dismiss();
                    }
                });
                return true;

            case R.id.conntection_settings:

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.searchmenu, menu);
        connection = menu.findItem(R.id.conntection_settings);
        MenuItem searchItem = menu.findItem(R.id.search_settings);
        searchItem.setVisible(false);
        if(hasConnection(this)){
            connection.setIcon(R.drawable.ic_network_cell_white_24dp);
        }else{
            connection.setIcon(R.drawable.ic_signal_cellular_off_white_24dp);
        }
        return true;
    }
    //gfdgfdff

    @Override
    protected void onResume() {
        super.onResume();
       invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        myMediaPlayer = new MyMediaPlayer(EquipmentStateActivity.this, "Button");
        myMediaPlayer.start();
        myMediaPlayer.setFree();
        Intent intent = new Intent(this, EquipmentActivity.class);
        intent.putExtra("id", getIntent().getStringExtra("id"));
        startActivity(intent);
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
        myMediaPlayer = new MyMediaPlayer(EquipmentStateActivity.this, "Button");
        myMediaPlayer.start();
        myMediaPlayer.setFree();
        Log.e("WTF", selected[0] + "");
    }
}
