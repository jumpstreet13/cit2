package com.cit.abakar.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


public class EquipmentStateActivity extends Activity {

    Button button1, button2;
    Switch switch1, switch2;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_state);
        getActionBar().setTitle("Состояние оборудования");
        button1 = (Button) findViewById(R.id.buttonInEquipmentState);
        button2 = (Button) findViewById(R.id.button2InEquipmentState);
        switch1 = (Switch) findViewById(R.id.switch1InEquipmentState);
        switch2 = (Switch) findViewById(R.id.switch2InEquipmentState);
        spinner = (Spinner) findViewById(R.id.spinnerInEquipmentActivity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.forSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button2.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(EquipmentStateActivity.this,spinner.getSelectedItem().toString(),Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(EquipmentStateActivity.this, EquipmentActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, EquipmentActivity.class);
        startActivity(intent);
    }
}
