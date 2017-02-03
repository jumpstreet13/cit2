package com.cit.abakar.application;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cit.abakar.application.ExampleClasses.Center;
import com.cit.abakar.application.ExampleClasses.Visit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//begin refactoring
public class MainActivity extends Activity {

    public static String MYURL = "";
    public final static String URLSETTINS = "urlSettings";
    public final static String SHAREDNAME = "Preference";
    public final static String USERNAME = "user";
    public final static String ID = "id";
    public final static String VISITID = "visitId";
    public final static String VERYFIED = "verfied";
    public final static String IDOFEQUIPMENT = "idOfEquipment";
    public final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private static RestApi restApi;
    private ListView listView;
    private ArrayList<Center> arr = new ArrayList<Center>();
    private YourAdapter adapter;
    private MenuItem connection;
    public ProgressBar progressBar;
    private Retrofit retrofit;
    private Visit visit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MYURL = getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE).getString(URLSETTINS, "http://10.39.5.76/apiv1/");
        getActionBar().setTitle(R.string.ActionBarIsOnlineMainActivity);
        progressBar = (ProgressBar) findViewById(R.id.progressBarInMainActivity);


        listView = (ListView) findViewById(R.id.listViewMain);
        try {
            retrofit = new Retrofit.Builder().baseUrl(MYURL.trim()).addConverterFactory(GsonConverterFactory.create()).build();
        } catch (Exception ex) {
            Toast.makeText(this, "Неккоректный адрес сервера, переход к адресу по умолчанию", Toast.LENGTH_SHORT).show();
            MYURL = "http://10.39.5.76/apiv1/";
            retrofit = new Retrofit.Builder().baseUrl(MYURL).addConverterFactory(GsonConverterFactory.create()).build();
        }

        restApi = retrofit.create(RestApi.class);
        progressBar.setVisibility(View.VISIBLE);
        restApi.getAllCenters().enqueue(new Callback<List<Center>>() {
            @Override
            public void onResponse(Call<List<Center>> call, Response<List<Center>> response) {
                arr.addAll(response.body());
                adapter = new YourAdapter(MainActivity.this, arr);
                listView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Center>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Нет соединения", Toast.LENGTH_SHORT).show();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                visit = new Visit();
                visit.centerId = arr.get(position).id;
                visit.dateVisit = getCurrentDate();
                visit.description = null;
                final Intent intent = new Intent(MainActivity.this, EquipmentActivity.class);
                intent.putExtra(ID, arr.get(position).id);

                restApi.addVisit(visit).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        intent.putExtra(VISITID, getVisitId(response.headers().get("Location")));
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Нет соединения", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.searchmenu, menu);
        connection = menu.findItem(R.id.conntection_settings);
        MenuItem searchItem = menu.findItem(R.id.search_settings);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_settings).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        if (hasConnection(this)) {
            connection.setIcon(R.drawable.ic_network_cell_white_24dp);
        } else {
            connection.setIcon(R.drawable.ic_signal_cellular_off_white_24dp);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText.trim());
                listView.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

        searchView.setFocusableInTouchMode(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocus();
        searchView.requestFocusFromTouch();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.search_settings:
                return true;

            case R.id.user:
                final InputMethodManager immUser = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                final Dialog dialogUser = new Dialog(MainActivity.this, R.style.DialogTheme);
                dialogUser.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialogUser.setContentView(R.layout.urldialog);
                Toolbar toolbar = (Toolbar) dialogUser.findViewById(R.id.toolbarInUrlDialog);
                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        dialogUser.dismiss();
                        immUser.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        return true;
                    }
                });
                toolbar.inflateMenu(R.menu.dialog_menu);
                toolbar.setTitle(R.string.WriteNameOfUser);
                SharedPreferences sharedPrefUser = getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
                TextView textViewUser = (TextView) dialogUser.findViewById(R.id.textViewinMainActivityDialog);
                textViewUser.setText(sharedPrefUser.getString(USERNAME, getString(R.string.UserIsNotInstalledYet)));
                dialogUser.show();
                dialogUser.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                final Dialog dialog = new Dialog(MainActivity.this, R.style.DialogTheme);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.urldialog);
                Toolbar toolbarz = (Toolbar) dialog.findViewById(R.id.toolbarInUrlDialog);
                toolbarz.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        dialog.dismiss();
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        return true;
                    }
                });
                toolbarz.inflateMenu(R.menu.dialog_menu);
                toolbarz.setTitle(R.string.WriteNewUrl);
                SharedPreferences sharedPref = getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
                TextView textView = (TextView) dialog.findViewById(R.id.textViewinMainActivityDialog);
                textView.setText(sharedPref.getString(URLSETTINS, getString(R.string.Adress_is_not_set_yet)));
                dialog.show();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                Button sendButton = (Button) dialog.findViewById(R.id.buttoninMainActivityDialog);
                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText editText = (EditText) dialog.findViewById(R.id.editTextinMainActivityDialog);
                        SharedPreferences sharedPref = getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(URLSETTINS, editText.getText().toString());
                        editor.apply();
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                return true;

            case R.id.conntection_settings:
                Toast toast = Toast.makeText(MainActivity.this, R.string.NeworkConnection, Toast.LENGTH_SHORT);
                toast.show();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }


    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public ArrayList<String> getArrString(ArrayList<Center> cr) {
        ArrayList<String> arr = new ArrayList<String>();
        for (Center cc : cr) {
            arr.add(cc.name);
        }
        return arr;
    }


    public int getVisitId(String location) {
        String[] s1 = location.split("/");
        return Integer.parseInt(s1[s1.length - 1]);
    }

    public String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public ArrayList<String> getBaseList() {
        return getArrString(arr);
    }


    class YourAdapter extends BaseAdapter {

        private ArrayList<String> arr;
        private ArrayList<String> arr2;
        private Context context;

        public YourAdapter(Context context, ArrayList<Center> arr) {
            this.arr = getArrString(arr);
            arr2 = new ArrayList<String>();
            arr2.addAll(this.arr);
            this.context = context;
        }

        @Override
        public int getCount() {
            return arr.size();
        }

        @Override
        public Object getItem(int position) {
            return arr.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.mylist, parent, false);
            }
            TextView main = (TextView) convertView.findViewById(R.id.myListTextView);

            main.setText(arr.get(position));

            return convertView;
        }

        public void filter(String charText) {
            charText = charText.toLowerCase();
            arr.clear();
            if (charText.length() == 0) {
                arr.addAll(arr2);
            } else {
                for (String ss : arr2) {
                    if (charText.length() != 0 && ss.toLowerCase().contains(charText)) {
                        arr.add(ss);
                    }
                }
            }
            notifyDataSetChanged();

        }

    }

}
