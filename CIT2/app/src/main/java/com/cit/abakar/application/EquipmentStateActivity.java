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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cit.abakar.application.ExampleClasses.Condition;
import com.cit.abakar.application.ExampleClasses.CreatedId;
import com.cit.abakar.application.ExampleClasses.Equipment;
import com.cit.abakar.application.ExampleClasses.Inspection;
import com.cit.abakar.application.ExampleClasses.Malfunction;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.cit.abakar.application.MainActivity.ID;
import static com.cit.abakar.application.MainActivity.IDOFEQUIPMENT;
import static com.cit.abakar.application.MainActivity.MYURL;
import static com.cit.abakar.application.MainActivity.SHAREDNAME;
import static com.cit.abakar.application.MainActivity.USERNAME;
import static com.cit.abakar.application.MainActivity.VERYFIED;
import static com.cit.abakar.application.MainActivity.VISITID;
import static com.cit.abakar.application.MainActivity.hasConnection;


public class EquipmentStateActivity extends Activity implements MultiSelectionSpinner.MultiSpinnerListener {


    private RadioGroup radioGroup;
    private Button button;
    private EditText editTextIsWorking;
    private Switch switch1, switch2, switch3;
    private MultiSelectionSpinner spinner;
    private ArrayList<Condition> ar = new ArrayList<Condition>();
    private MenuItem connection;
    private Equipment equipment;
    private static RestApi restApi;
    private Retrofit retrofit;
    private ArrayList<Boolean> selectedItem = new ArrayList<Boolean>();
    private String location;
    private ProgressBar progressBar;
    private String note = "";
    private Boolean succes;
    private ArrayList<Integer> veryfied = new ArrayList<Integer>();
    private boolean network;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_state);
        getActionBar().setTitle(R.string.ActionBarISOfflineEquipmentStateActivity);
        if(hasConnection(this)){
            network = true;
        }else {
            network = false;
        }

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        button = (Button) findViewById(R.id.buttonInEquipmentState);
        switch1 = (Switch) findViewById(R.id.switch1InEquipmentState);
        switch2 = (Switch) findViewById(R.id.switch2InEquipmentState);
        switch3 = (Switch) findViewById(R.id.switchMalfunc);
        spinner = (MultiSelectionSpinner) findViewById(R.id.spinnerInEquipmentStateActivity);
        progressBar = (ProgressBar) findViewById(R.id.progressBarInEquipmentStateActivity);
        spinner.registerRadioGroup(radioGroup);
        spinner.setContext(this);
        spinner.setBackgroundColor(getResources().getColor(R.color.material));

        veryfied.clear();
        veryfied.addAll(getIntent().getIntegerArrayListExtra(VERYFIED));

        retrofit = new Retrofit.Builder().baseUrl(MYURL).
                addConverterFactory(GsonConverterFactory.create()).build();
        restApi = retrofit.create(RestApi.class);
        getConditions();


        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        switch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch3.isChecked()) {
                    spinner.setVisibility(View.VISIBLE);
                    switch2.setChecked(false);
                    switch2.setEnabled(false);
                }
                else {
                    spinner.setVisibility(View.INVISIBLE);
                    switch2.setEnabled(true);
                }


            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switch3.isChecked()) {
                    sendReport("YES");
                } else {
                    sendReport("");
                }

            }
        });
    }


    public void sendReport(final String key) {
                final Inspection inspection = new Inspection();
                inspection.equipmentId = getIntent().getIntExtra(IDOFEQUIPMENT, -5);
                inspection.visitId = getIntent().getIntExtra(VISITID, -5);
                inspection.fgAvailability = switch1.isChecked();
                inspection.fgUsings = switch2.isChecked();
                inspection.note = note;

                progressBar.setVisibility(View.VISIBLE);
                restApi.addInspection(inspection).enqueue(new Callback<CreatedId>() {
                    @Override
                    public void onResponse(Call<CreatedId> call, Response<CreatedId> response) {
                        CreatedId createdId = response.body();
                        if (key.equals("YES")) {
                            final String[] reasons = spinner.getSelectedItem().toString().split(",");
                            ArrayList<Condition> result = new ArrayList<Condition>();
                            result.addAll(compare(reasons, ar));

                            for (Condition cc : result) {
                                Malfunction malfunctions = new Malfunction();
                                malfunctions.inspectionId = createdId.getCreatedId();
                                malfunctions.conditionId = cc.id;
                                restApi.addMalfunction(malfunctions).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        progressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(EquipmentStateActivity.this, "Не удалось отправить неисправности", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                            Intent intent = new Intent(EquipmentStateActivity.this, EquipmentActivity.class);
                            intent.putExtra(ID, getIntent().getIntExtra(ID, -5));
                            intent.putExtra(VISITID, getIntent().getIntExtra(VISITID, -5));
                            intent.putExtra(VERYFIED, veryfied);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(EquipmentStateActivity.this, EquipmentActivity.class);
                            intent.putExtra(ID, getIntent().getIntExtra(ID, -5));
                            intent.putExtra(VISITID, getIntent().getIntExtra(VISITID, -5));
                            intent.putExtra(VERYFIED, veryfied);
                            startActivity(intent);
                        }
                        sendIsSuccesful();
                    }

                    @Override
                    public void onFailure(Call<CreatedId> call, Throwable t) {
                        sendIsNotSucces();
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.search_settings:
                return true;

            case R.id.note:
                final InputMethodManager immUserU = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                final Dialog dialogUserU = new Dialog(EquipmentStateActivity.this, R.style.DialogTheme);
                dialogUserU.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialogUserU.setContentView(R.layout.urldialog);
                Toolbar toolbar = (Toolbar) dialogUserU.findViewById(R.id.toolbarInUrlDialog);
                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        dialogUserU.dismiss();
                        immUserU.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        return true;
                    }
                });
                toolbar.inflateMenu(R.menu.dialog_menu);
                toolbar.setTitle(R.string.Note);
                dialogUserU.show();
                dialogUserU.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                immUserU.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                Button button = (Button) dialogUserU.findViewById(R.id.buttoninMainActivityDialog);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) dialogUserU.findViewById(R.id.editTextinMainActivityDialog);
                        note = editText.getText().toString();
                        immUserU.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        TextView textView = (TextView) findViewById(R.id.Note);
                        textView.setText(note);
                        dialogUserU.dismiss();
                    }
                });
                return true;

            case R.id.user:
                final Dialog dialogUser = new Dialog(EquipmentStateActivity.this, R.style.DialogTheme);
                dialogUser.setContentView(R.layout.urldialog);
                dialogUser.setTitle(R.string.WriteNameOfUser);
                SharedPreferences sharedPrefUser = getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
                TextView textViewUser = (TextView) dialogUser.findViewById(R.id.textViewinMainActivityDialog);
                textViewUser.setText(sharedPrefUser.getString(USERNAME, getString(R.string.UserIsNotInstalledYet)));
                dialogUser.show();
                dialogUser.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                final InputMethodManager immUser = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                immUser.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                Button userButton = (Button) dialogUser.findViewById(R.id.buttoninMainActivityDialog);
                userButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) dialogUser.findViewById(R.id.editTextinMainActivityDialog);
                        SharedPreferences sharedPref = getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(USERNAME, editText.getText().toString());
                        editor.apply();
                        immUser.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        dialogUser.dismiss();
                    }
                });

                return true;

            case R.id.htttp_settings:
                final Dialog dialog = new Dialog(EquipmentStateActivity.this, R.style.DialogTheme);
                dialog.setContentView(R.layout.urldialog);
                dialog.setTitle(R.string.WriteNewUrl);
                SharedPreferences sharedPref = getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
                TextView textView = (TextView) dialog.findViewById(R.id.textViewinMainActivityDialog);
                textView.setText(sharedPref.getString(MainActivity.URLSETTINS, getString(R.string.Adress_is_not_set_yet)));
                dialog.show();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                Button sendButton = (Button) dialog.findViewById(R.id.buttoninMainActivityDialog);
                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) dialog.findViewById(R.id.editTextinMainActivityDialog);
                        SharedPreferences sharedPref = getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(MainActivity.URLSETTINS, editText.getText().toString());
                        editor.apply();
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        dialog.dismiss();
                    }
                });
                return true;

            case R.id.conntection_settings:
                Toast toast = Toast.makeText(EquipmentStateActivity.this, R.string.NeworkConnection, Toast.LENGTH_SHORT);
                toast.show();
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
        MenuItem http = menu.findItem(R.id.htttp_settings);
        MenuItem note = menu.findItem(R.id.note);
        note.setVisible(true);
        http.setVisible(false);
        MenuItem user = menu.findItem(R.id.user);
        user.setVisible(false);
        searchItem.setVisible(false);
        if (hasConnection(this)) {
            connection.setIcon(R.drawable.ic_network_cell_white_24dp);
        } else {
            connection.setIcon(R.drawable.ic_signal_cellular_off_white_24dp);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        if(!hasConnection(this)) {
            Toast.makeText(this, "Нет соединения", Toast.LENGTH_SHORT).show();
        }
        if(hasConnection(this) && !network){
            getConditions();
            network = true;
        }
    }

    @Override
    public void onBackPressed() {
        if(!hasConnection(this)){
            Toast.makeText(this, "Нет соединения", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, EquipmentActivity.class);
        intent.putExtra(ID, getIntent().getIntExtra(ID, -5));
        intent.putExtra(VISITID, getIntent().getIntExtra(VISITID, -5));
        veryfied.remove(veryfied.size() - 1);
        intent.putExtra(VERYFIED, veryfied);
        startActivity(intent);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemsSelected(boolean[] selected){

    }

    public void getConditions(){

        progressBar.setVisibility(View.VISIBLE);
        restApi.getConditios().enqueue(new Callback<List<Condition>>() {
            @Override
            public void onResponse(Call<List<Condition>> call, Response<List<Condition>> response) {
                ar.addAll(response.body());
                spinner.setItems(getConditionsTitle(ar), getString(R.string.ChooseTheReason), EquipmentStateActivity.this);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Condition>> call, Throwable t) {
                Toast.makeText(EquipmentStateActivity.this, "Не удалось загрузить список ошибок", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public ArrayList<Condition> compare(String[] reasons, List<Condition> conditions) {

        ArrayList<Condition> result = new ArrayList<Condition>();
        for (int i = 0; i < conditions.size(); i++) {
            for (int j = 0; j < reasons.length; j++) {
                if (conditions.get(i).name.trim().toLowerCase().equals(reasons[j].trim().toLowerCase())) {
                    result.add(conditions.get(i));
                    break;
                }
            }
        }
        return result;
    }

    public ArrayList<String> getConditionsTitle(List<Condition> list) {
        ArrayList<String> arr = new ArrayList<String>();
        for (Condition condition : list) {
            arr.add(condition.name);
        }
        return arr;
    }

   /* public int setInspectionId(String location) {
        String[] s1 = location.split("/");
        return Integer.parseInt(s1[s1.length - 1]);
    }*/

    public void sendIsSuccesful() {
        Toast.makeText(this, R.string.SendIsSuccesful, Toast.LENGTH_SHORT).show();
    }

    public void sendIsNotSucces() {
        Toast.makeText(this, R.string.SendIsNotSucces, Toast.LENGTH_SHORT).show();
    }
}
