package com.cit.abakar.application;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


import com.cit.abakar.application.ExampleClasses.Dismantling;
import com.cit.abakar.application.ExampleClasses.Equipment;
import com.cit.abakar.application.ExampleClasses.Installation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import static com.cit.abakar.application.MainActivity.ID;
import static com.cit.abakar.application.MainActivity.MYURL;
import static com.cit.abakar.application.MainActivity.MY_PERMISSIONS_REQUEST_READ_CONTACTS;
import static com.cit.abakar.application.MainActivity.SHAREDNAME;
import static com.cit.abakar.application.MainActivity.USERNAME;
import static com.cit.abakar.application.MainActivity.hasConnection;

public class EquipmentActivity extends Activity implements AdapterInterface, MultiSelectionSpinner.MultiSpinnerListener {

    private Button buttonInspectionDone;
    private ListView listView;
    private ArrayList<Equipment> arr = new ArrayList<>();
    private MultiSelectionSpinner spinner;
    private MenuItem connection;
    private static RestApi restApi;
    private Retrofit retrofit;
    private MyAdapter adapter;
    private ProgressBar progressBar;
    private ArrayList<Integer> veryfied = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        requestMultiplePermissions();
        getActionBar().setTitle(R.string.ActionBarIsOnlineEquipmentActivity);
        listView = (ListView) findViewById(R.id.listViewEquipment);
        buttonInspectionDone = (Button) findViewById(R.id.buttonInspectionDone);
        progressBar = (ProgressBar) findViewById(R.id.progressBarInEquipmentActivity);
        veryfied.clear();
        try {
            veryfied.addAll(getIntent().getIntegerArrayListExtra("veryfied"));
        } catch (NullPointerException np) {
            veryfied.add(getIntent().getIntExtra("id", -5));
            np.printStackTrace();
        }
        buttonInspectionDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Work", "it works");
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(EquipmentActivity.this, MainActivity.class);
                intent.putExtra("veryfied", veryfied);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EquipmentActivity.this, "Инспекция прошла успешно", Toast.LENGTH_SHORT).show();
            }
        });

        refreshList();


    }


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


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.searchmenu, menu);
            connection = menu.findItem(R.id.conntection_settings);
            MenuItem searchItem = menu.findItem(R.id.search_settings);
            MenuItem http = menu.findItem(R.id.htttp_settings);
            MenuItem user = menu.findItem(R.id.user);
            MenuItem refresh = menu.findItem(R.id.refresh);
            refresh.setVisible(true);
            user.setVisible(false);
            http.setVisible(false);
            searchItem.setVisible(false);
            if (hasConnection(this)) {
                connection.setIcon(R.drawable.ic_network_cell_white_24dp);
            } else {
                connection.setIcon(R.drawable.ic_signal_cellular_off_white_24dp);
            }
            return true;
        }

        @Override
        protected void onResume () {
            super.onResume();
            invalidateOptionsMenu();
        }


    @Override
    public void onBackPressed() {

    }



        @Override
        public boolean onOptionsItemSelected (MenuItem item){

            int id = item.getItemId();

            switch (id) {
                case R.id.search_settings:
                    return true;

                case R.id.refresh:
                    refreshList();
                    Toast.makeText(this, "Обновлено", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.user:
                    final Dialog dialogUser = new Dialog(EquipmentActivity.this, R.style.DialogTheme);
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
                    final Dialog dialog = new Dialog(EquipmentActivity.this, R.style.DialogTheme);
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
                    Toast toast = Toast.makeText(EquipmentActivity.this, R.string.NeworkConnection, Toast.LENGTH_SHORT);
                    toast.show();

                    break;

            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public void textViewClicked ( int position){

            Intent intent = new Intent(EquipmentActivity.this, EquipmentStateActivity.class);
            intent.putExtra("id", getIntent().getIntExtra("id", -5));
            Log.e("PING", arr.get(position).id + "");
            intent.putExtra("visitId", getIntent().getIntExtra("visitId", -5));
            intent.putExtra("idOfEquipment", arr.get(position).id);
            veryfied.add(arr.get(position).id);
            intent.putExtra("veryfied", veryfied);
            startActivity(intent);

        }


        @Override
        public void installationButtonClicked ( final int position){
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            final Dialog dialog = new Dialog(EquipmentActivity.this, R.style.DialogTheme);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.equipment_dialog);
            dialog.setCancelable(true);
            Toolbar toolbar = (Toolbar) dialog.findViewById(R.id.toolbarInEquipmentDialog);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    dialog.dismiss();
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    return true;
                }
            });
            toolbar.inflateMenu(R.menu.dialog_menu);
            toolbar.setTitle(R.string.WriteNumberOfAkt);
            dialog.show();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            Button sendButton = (Button) dialog.findViewById(R.id.buttonSendNumberOfAkt);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText ed = (EditText) dialog.findViewById(R.id.editTextInInstallationDialog);
                    if (ed.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(EquipmentActivity.this, R.string.YouDidNotWriteNumberOfAkt, Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    Installation installation = new Installation();
                    installation.aktNumber = ed.getText().toString();
                    installation.equipmentId = arr.get(position).id;
                    Log.e("NEO", arr.get(position).id + "");
                    installation.visitId = getIntent().getIntExtra("visitId", -5);
                    Log.e("NEO", getIntent().getIntExtra("visitId", -5) + "");
                    progressBar.setVisibility(View.VISIBLE);
                    restApi.addInstallation(installation).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.e("ZEUS", "succes");
                            Log.e("ZEUS", response.isSuccessful() + "");
                            Log.e("ZEUS", response.code() + "");
                            sendIsSucces();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            dialog.dismiss();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("ZEUS", "wtf");
                            Log.e("ZEUS", t.toString());
                            progressBar.setVisibility(View.GONE);
                            sendIsNotSucces();
                        }
                    });
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    dialog.dismiss();

                }
            });
        }
    //aadass


    public void requestMultiplePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_SMS
                },
                MainActivity.MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void deinstallationButtonClicked(final int position) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final Dialog dialog = new Dialog(EquipmentActivity.this, R.style.DialogTheme);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.equipment_deinstallation_dialog);
        Toolbar toolbar = (Toolbar) dialog.findViewById(R.id.toolbarInEquipmentdeinstallationDialog);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dialog.dismiss();
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                return true;
            }
        });
        toolbar.inflateMenu(R.menu.dialog_menu);
        toolbar.setTitle(R.string.WriteNumberOfAkt);
        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
             TelephonyManager tMgr = (TelephonyManager) EquipmentActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
            SharedPreferences sharedPrefUser = getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
            String mPhoneNumber = sharedPrefUser.getString(USERNAME, getString(R.string.UserIsNotInstalledYet)) + "/" + tMgr.getLine1Number();
            TextView tx = (TextView) dialog.findViewById(R.id.textViewinEquipmentDeinstallation);
            String number = mPhoneNumber;
            tx.setText(number);
            Log.e("NUMBER", mPhoneNumber);

        Button sendButton = (Button) dialog.findViewById(R.id.buttoninEqupmentDeinstallation);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText ed = (EditText) dialog.findViewById(R.id.editTextinEquipmentDeinstallation);
                if (ed.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(EquipmentActivity.this, R.string.YouDidNotWriteNumberOfAkt, Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                TextView tx = (TextView) dialog.findViewById(R.id.textViewinEquipmentDeinstallation);

                Dismantling dismantling = new Dismantling();
                dismantling.aktNumber = ed.getText().toString();
                Log.e("Shox", ed.getText().toString());
                dismantling.user = tx.getText().toString();
                Log.e("Shox", tx.getText().toString());
                dismantling.visitId = getIntent().getIntExtra("visitId", -5);
                Log.e("Shox", getIntent().getIntExtra("visitId", -5) + "");
                dismantling.equipmentId = arr.get(position).id;
                Log.e("Shox", arr.get(position).id + "");

                progressBar.setVisibility(View.VISIBLE);
                restApi.addDismantling(dismantling).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.e("Shox", "succes");
                        Log.e("Shox", response.isSuccessful() + "");
                        Log.e("Shox", response.code() + "");
                        sendIsSucces();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        dialog.dismiss();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Shox", "wtf");
                        progressBar.setVisibility(View.GONE);
                        sendIsNotSucces();
                    }
                });
            }
        });

    }

    public void refreshList(){
        arr.clear();
        retrofit = new Retrofit.Builder().baseUrl(MYURL).addConverterFactory(GsonConverterFactory.create()).build();
        restApi = retrofit.create(RestApi.class);


        progressBar.setVisibility(View.VISIBLE);
        restApi.getEquipment().enqueue(new Callback<List<Equipment>>() {
            @Override
            public void onResponse(Call<List<Equipment>> call, Response<List<Equipment>> response) {
                for (Equipment eq : response.body()) {
                    if (eq.centerId == getIntent().getIntExtra("id", -5)) {
                        arr.add(eq);
                    }
                }
                Equipment equipment = new Equipment();
                equipment.id = 3;
                equipment.centerId = 1;
                equipment.serialNumber = "gvdfv";
                equipment.inventoryNumber  = "32fdsfs";
                equipment.name = "Очень длинное название оборудования";
                equipment.fgDismantled="false";
                equipment.fgNotInstall="false";

                arr.add(equipment);

                adapter = new MyAdapter(EquipmentActivity.this, arr, veryfied);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                int Shox = adapter.getCount();
                if(isInspectionDone(getIntent().getIntegerArrayListExtra("veryfied"), Shox)){
                    buttonInspectionDone.setEnabled(true);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Equipment>> call, Throwable t) {
                Toast.makeText(EquipmentActivity.this, "Нет соединения", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public boolean isInspectionDone(ArrayList<Integer> array, int Shox) {

        if(array == null){
            return false;
        }

        if(array.size() -1  == Shox){
            return true;
        }

        return false;
    }


    public void sendIsSucces() {
        Toast.makeText(this, R.string.SendIsSuccesful, Toast.LENGTH_SHORT).show();
    }

    public void sendIsNotSucces() {
        Toast.makeText(this, R.string.SendIsNotSucces, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemsSelected(boolean[] selected) {

    }


}
