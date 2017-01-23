package com.cit.abakar.application;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.cit.abakar.application.database.Directory_Equipment_Condition;

import java.util.ArrayList;
import java.util.zip.Inflater;

import static com.cit.abakar.application.MainActivity.hasConnection;

public class EquipmentActivity extends Activity implements AdapterInterface, MultiSelectionSpinner.MultiSpinnerListener {

   private ListView listView;
   private ArrayList<String> arr = new ArrayList<String>();
   private MyMediaPlayer myMediaPlayer;
   private MultiSelectionSpinner spinner;
   private MenuItem connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        getActionBar().setTitle(R.string.ActionBarIsOnlineEquipmentActivity);

        for(int i = 0; i<10; i++){
            arr.add("Оборудование "+i+"");
        }
        listView = (ListView) findViewById(R.id.listViewEquipment);
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

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        myMediaPlayer = new MyMediaPlayer(EquipmentActivity.this, "Button");
        myMediaPlayer.start();
        myMediaPlayer.setFree();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void textViewClicked() {
        myMediaPlayer = new MyMediaPlayer(EquipmentActivity.this, "Button");
        myMediaPlayer.start();
        myMediaPlayer.setFree();
        Intent intent = new Intent(EquipmentActivity.this, EquipmentStateActivity.class);
        startActivity(intent);

    }

    @Override
    public void installationButtonClicked() {
        myMediaPlayer = new MyMediaPlayer(EquipmentActivity.this, "Button");
        myMediaPlayer.start();
        myMediaPlayer.setFree();

        final Dialog dialog = new Dialog(EquipmentActivity.this, R.style.DialogTheme);
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
                myMediaPlayer = new MyMediaPlayer(EquipmentActivity.this, "Button");
                myMediaPlayer.start();
                myMediaPlayer.setFree();
                EditText ed = (EditText) dialog.findViewById(R.id.editTextInInstallationDialog);
                if(ed.getText().toString().equals("")){
                    Toast toast = Toast.makeText(EquipmentActivity.this,"Вы не ввели номер акта", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();

            }
        });


    }

    @Override
    public void deinstallationButtonClicked() {
        myMediaPlayer = new MyMediaPlayer(EquipmentActivity.this, "Button");
        myMediaPlayer.start();
        myMediaPlayer.setFree();
        final Dialog dialog = new Dialog(EquipmentActivity.this, R.style.DialogTheme);
        dialog.setContentView(R.layout.equipment_deinstallation_dialog);
        dialog.setTitle("Введите номер акта");
        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        TelephonyManager tMgr = (TelephonyManager)EquipmentActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        TextView tx = (TextView) dialog.findViewById(R.id.textViewinEquipmentDeinstallation);
        tx.setText(mPhoneNumber);
        Log.e("NUMBER", mPhoneNumber);

        Button sendButton = (Button) dialog.findViewById(R.id.buttoninEqupmentDeinstallation);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer = new MyMediaPlayer(EquipmentActivity.this, "Button");
                myMediaPlayer.start();
                myMediaPlayer.setFree();
                EditText ed = (EditText) dialog.findViewById(R.id.editTextinEquipmentDeinstallation);
                if(ed.getText().toString().equals("")){
                    Toast toast = Toast.makeText(EquipmentActivity.this,"Вы не ввели номер акта", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onItemsSelected(boolean[] selected) {

    }
}
