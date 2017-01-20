package com.cit.abakar.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

import static com.cit.abakar.application.MainActivity.hasConnection;


public class EquipmentStateActivity extends Activity implements MultiSelectionSpinner.MultiSpinnerListener {

    Button button1, button2;
    Switch switch1, switch2;
    MultiSelectionSpinner spinner;
    ArrayList<String> ar = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_state);
        if(!hasConnection(this)) {
            getActionBar().setTitle("Состояние оборудования (оффлайн)");
        }else{
            getActionBar().setTitle("Состояние оборудования (онлайн)");
        }

        button1 = (Button) findViewById(R.id.buttonInEquipmentState);
        button2 = (Button) findViewById(R.id.button2InEquipmentState);
        switch1 = (Switch) findViewById(R.id.switch1InEquipmentState);
        switch2 = (Switch) findViewById(R.id.switch2InEquipmentState);
        spinner = (MultiSelectionSpinner) findViewById(R.id.spinnerInEquipmentActivity);
        ar.add("Сломан кулер");
        ar.add("Не работает автофокус");
        ar.add("Перегрев процессора");
        ar.add("Другое");
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.forSpinner, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // spinner.setAdapter(adapter);
        spinner.setItems(ar,"Выбрать причину",this);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button2.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.e("getSelected", s);
                        if(spinner.getSelectedItem().toString().equals("") ||
                                spinner.getSelectedItem().toString().equals("Выбрать причину")){
                            Toast toast = Toast.makeText(EquipmentStateActivity.this,"Вы ничего не выбрали",Toast.LENGTH_SHORT);
                            toast.show();
                            return;

                        }
                        Toast toast = Toast.makeText(EquipmentStateActivity.this,spinner.getSelectedItem().toString(),Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(EquipmentStateActivity.this, EquipmentActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }
    //gfdgfdff

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
        Intent intent = new Intent(this, EquipmentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
        Log.e("WTF", selected[0] + "");
    }
}
