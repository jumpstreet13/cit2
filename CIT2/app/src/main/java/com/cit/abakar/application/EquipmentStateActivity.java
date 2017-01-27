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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.cit.abakar.application.ExampleClasses.Condition;
import com.cit.abakar.application.ExampleClasses.Equipment;
import com.cit.abakar.application.ExampleClasses.Inspection;
import com.cit.abakar.application.ExampleClasses.Malfunction;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.cit.abakar.application.MainActivity.hasConnection;


public class EquipmentStateActivity extends Activity implements MultiSelectionSpinner.MultiSpinnerListener {

    private Button button1, button2, buttonIsWorking;
    private EditText editTextIsWorking;
    private Switch switch1, switch2;
    private MultiSelectionSpinner spinner;
    private ArrayList<Condition> ar = new ArrayList<Condition>();
    private MyMediaPlayer myMediaPlayer;
    private MenuItem connection;
    private Equipment equipment;
    private static RestApi restApi;
    private Retrofit retrofit;
    private ArrayList<Boolean> selectedItem = new ArrayList<Boolean>();
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_state);
        getActionBar().setTitle(R.string.ActionBarISOfflineEquipmentStateActivity);
        button1 = (Button) findViewById(R.id.buttonInEquipmentState);
        button2 = (Button) findViewById(R.id.button2InEquipmentState);
        switch1 = (Switch) findViewById(R.id.switch1InEquipmentState);
        switch2 = (Switch) findViewById(R.id.switch2InEquipmentState);
        buttonIsWorking = (Button) findViewById(R.id.buttonWhenIsWorking);
        editTextIsWorking = (EditText) findViewById(R.id.editTextWhenIsWorking);
        spinner = (MultiSelectionSpinner) findViewById(R.id.spinnerInEquipmentStateActivity);
        retrofit = new Retrofit.Builder().baseUrl("http://10.39.5.76/apiv1/").
                addConverterFactory(GsonConverterFactory.create()).build();
        restApi = retrofit.create(RestApi.class);
        restApi.getEquipment().enqueue(new Callback<List<Equipment>>() {
            @Override
            public void onResponse(Call<List<Equipment>> call, Response<List<Equipment>> response) {
                for (Equipment eq : response.body()) {
                    Log.e("FOX", eq.id + "");
                    Log.e("FOX", eq.fgDismantled);
                    Log.e("FOX", getIntent().getIntExtra("idOfEquipment", -5) + "");
                    if (eq.id == getIntent().getIntExtra("idOfEquipment", -5)) {
                        equipment = eq;
                        if (equipment.fgDismantled.equals("true")) {
                            switch1.setChecked(false);
                        } else {
                            switch1.setChecked(true);
                        }
                        if (equipment.fgNotInstall.equals("true")) {
                            switch2.setChecked(false);
                        } else {
                            switch2.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Equipment>> call, Throwable t) {

            }
        });
        //Log.e("ZZ", getIntent().getIntExtra("idOfEquipment", -5) + "");
        // Log.e("ZZ", equipment.fg_dismantled + " " + equipment.fg_not_install + " ");


        restApi.getConditios().enqueue(new Callback<List<Condition>>() {
            @Override
            public void onResponse(Call<List<Condition>> call, Response<List<Condition>> response) {
                ar.addAll(response.body());
                spinner.setItems(getConditionsTitle(ar), getString(R.string.ChooseTheReason), EquipmentStateActivity.this);
            }

            @Override
            public void onFailure(Call<List<Condition>> call, Throwable t) {

            }
        });
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.forSpinner, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);
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
                buttonIsWorking.setVisibility(View.INVISIBLE);
                buttonIsWorking.setEnabled(false);
                editTextIsWorking.setVisibility(View.INVISIBLE);
                editTextIsWorking.setEnabled(false);
                button2.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myMediaPlayer = new MyMediaPlayer(EquipmentStateActivity.this, "Button");
                        myMediaPlayer.start();
                        myMediaPlayer.setFree();
                        //Log.e("getSelected", s);
                        //inspection.fgAvailability =
                        //restApi.addInspection()
                        if (spinner.getSelectedItem().toString().equals("") ||
                                spinner.getSelectedItem().toString().equals("Выбрать причину")) {
                            Toast toast = Toast.makeText(EquipmentStateActivity.this, R.string.ToastYouDoNotChosenAnything, Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }

                        final String[] reasons = spinner.getSelectedItem().toString().split(",");
                        ArrayList<Condition> result = new ArrayList<Condition>();
                        result.addAll(compare(reasons, ar));
                        Log.e("NBA", result.size() + "");

                        for (Condition cc : result) {
                            Malfunction malfunctions = new Malfunction();
                            malfunctions.inspectionId = setInspectionId(location);
                            Log.e("Device malfunc", malfunctions.inspectionId + "");
                            malfunctions.conditionId = cc.id;
                            Log.e("Device condi", malfunctions.conditionId + "");
                            restApi.addMalfunction(malfunctions).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Log.e("Device", "succes");
                                    Log.e("Device", response.isSuccessful() + "");
                                    Log.e("Device", response.code() + "");

                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.e("Device", "wtf");
                                }
                            });
                        }
                        //Refactor
                        
                        Toast toast = Toast.makeText(EquipmentStateActivity.this, spinner.getSelectedItem().toString(),
                                Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(EquipmentStateActivity.this, EquipmentActivity.class);
                        intent.putExtra("id", getIntent().getIntExtra("id", -5));
                        intent.putExtra("visitId", getIntent().getIntExtra("visitId", -5));
                        startActivity(intent);
                    }
                });
            }
        });


        buttonIsWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer = new MyMediaPlayer(EquipmentStateActivity.this, "Button");
                myMediaPlayer.start();
                myMediaPlayer.setFree();
                Inspection inspection = new Inspection();
                inspection.equipmentId = getIntent().getIntExtra("idOfEquipment", -5);
                inspection.visitId = getIntent().getIntExtra("visitId", -5);
                inspection.fgAvailability = switch1.isChecked();
                inspection.fgUsings = switch2.isChecked();
                inspection.note = editTextIsWorking.getText().toString();

                restApi.addInspection(inspection).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.e("Device", response.headers().get("Location"));
                        Log.e("Device", "success");
                        Log.e("Device", response.isSuccessful() + "");
                        Log.e("Device", response.code() + "");
                        location = response.headers().get("Location");

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Device", "wtf");
                    }
                });
                button1.setVisibility(View.VISIBLE);
                editTextIsWorking.setVisibility(View.INVISIBLE);
                editTextIsWorking.setEnabled(false);
                buttonIsWorking.setEnabled(false);
                buttonIsWorking.setVisibility(View.INVISIBLE);
            }
        });

    }

    public ArrayList<Condition> compare(String[] reasons, List<Condition> conditions) {

        ArrayList<Condition> result = new ArrayList<Condition>();
        for (int i = 0; i < conditions.size(); i++) {
            for (int j = 0; j < reasons.length; j++) {
                Log.e("Condition name", conditions.get(i).name);
                Log.e("Reason name", reasons[j]);
                if (conditions.get(i).name.trim().toLowerCase().equals(reasons[j].trim().toLowerCase())) {
                    Log.e("Added", "added");
                    result.add(conditions.get(i));
                    break;
                }
            }
        }
        Log.e("Result size", result.size() + "");
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
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
        if (hasConnection(this)) {
            connection.setIcon(R.drawable.ic_network_cell_white_24dp);
        } else {
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
        intent.putExtra("id", getIntent().getIntExtra("id", -5));
        intent.putExtra("visitId", getIntent().getIntExtra("visitId", -5));
        startActivity(intent);
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
        myMediaPlayer = new MyMediaPlayer(EquipmentStateActivity.this, "Button");
        myMediaPlayer.start();
        myMediaPlayer.setFree();
        Log.e("WTF", selected[0] + "");
    }

    public ArrayList<String> getConditionsTitle(List<Condition> list) {
        ArrayList<String> arr = new ArrayList<String>();
        for (Condition condition : list) {
            arr.add(condition.name);
        }
        return arr;
    }

    public int setInspectionId(String location) {
        Log.e("Device", location);
        String[] s1 = location.split("/");
        Log.e("Device", s1[s1.length - 1]);
        return Integer.parseInt(s1[s1.length - 1]);
    }
}
