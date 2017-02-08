package com.cit.abakar.application;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.support.v7.widget.Toolbar;

import com.cit.abakar.application.ExampleClasses.Center;
import com.cit.abakar.application.ExampleClasses.CreatedId;
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

public class MainActivity extends Activity {

    public static String MYURL = "";
    public final static String URLSETTINS = "urlSettings";
    public final static String SHAREDNAME = "Preference";
    public final static String USERNAME = "user";
    public final static String KEYS = "keys";
    private ListView listView;
    private ArrayList<Center> arr = new ArrayList<Center>();
    private YourAdapter adapter;
    private MenuItem connection;
    public ProgressBar progressBar;
    private static RestApi restApi;
    private Retrofit retrofit;
    private Visit visit;
    //private ArrayList<ArrayList<Integer>> veryfied = new ArrayList<ArrayList<Integer>>();
    public static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    //Version for show


    public ArrayList<String> getArrString(ArrayList<Center> cr) {
        ArrayList<String> arr = new ArrayList<String>();
        for (Center cc : cr) {
            arr.add(cc.name);
            Log.e("NAME", cc.name);
        }
        return arr;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MYURL = getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE).getString(URLSETTINS, "http://10.39.5.76/apiv1/");
        getActionBar().setTitle(R.string.ActionBarIsOnlineMainActivity);
        progressBar = (ProgressBar) findViewById(R.id.progressBarInMainActivity);
        /*try {
            ArrayList<Integer> arr = new ArrayList<Integer>();
            arr.addAll(getIntent().getIntegerArrayListExtra("veryfied"));
            veryfied.add(arr);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }*/
        listView = (ListView) findViewById(R.id.listViewMain);
        try {
            retrofit = new Retrofit.Builder().baseUrl(MYURL.trim()).addConverterFactory(GsonConverterFactory.create()).build();
        } catch (Exception ex) {
            Toast.makeText(this, "Неккоректный адрес сервера, переход к адресу по умолчанию", Toast.LENGTH_LONG).show();
            MYURL = "http://10.39.5.76/apiv1/";
            retrofit = new Retrofit.Builder().baseUrl(MYURL).addConverterFactory(GsonConverterFactory.create()).build();
        }

        restApi = retrofit.create(RestApi.class);
        progressBar.setVisibility(View.VISIBLE);
        restApi.getAllCenters().enqueue(new Callback<List<Center>>() {
            @Override
            public void onResponse(Call<List<Center>> call, Response<List<Center>> response) {
                Log.e("REST", response.body().toString());
                arr.addAll(response.body());
               /* for (Center c : arr) {
                    Log.e("REST", c.name);
                    c.save();
                }*/
                adapter = new YourAdapter(MainActivity.this, arr);
                listView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Center>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Нет соединения", Toast.LENGTH_SHORT).show();
                //progressBar.setVisibility(View.GONE);

            }
        });

        //arr.addAll(Center.getAll());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                progressBar.setVisibility(View.VISIBLE);
                visit = new Visit();
                visit.centerId = arr.get(position).id;
                visit.dateVisit = getCurrentDate();
                visit.description = null;
                final Intent intent = new Intent(MainActivity.this, EquipmentActivity.class);
                //Log.e("PZD", arr.get(position).getId().toString());
                intent.putExtra("id", arr.get(position).id);

                restApi.addVisit(visit).enqueue(new Callback<CreatedId>() {
                    @Override
                    public void onResponse(Call<CreatedId> call, Response<CreatedId> response) {
                        Log.e("TAZ", response.headers().toString());
                        CreatedId createdId = response.body();
                        Log.e("Taz", response.code() + "");
                        intent.putExtra("visitId", createdId.getCreatedID());
                       /* for(ArrayList<Integer> arr : veryfied){
                            for(Integer in : arr ){
                                Log.e("Byali", in + " " + visit.centerId);
                                if(in == visit.centerId){
                                    arr.add(visit.centerId);
                                    Log.e("Byali","Succes");
                                    intent.putExtra("veryfied", arr);
                                }
                            }
                        }*/
                        startActivity(intent);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<CreatedId> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Нет соединения", Toast.LENGTH_SHORT).show();
                        Log.e("TAZ", "wtf");
                    }
                });
            }
        });

    }

    public String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return format.format(date);
    }

    public ArrayList<String> getBaseList() {
        return getArrString(arr);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        if(!hasConnection(this)){
            Toast.makeText(this, "Нет соединения", Toast.LENGTH_SHORT).show();
        }
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
                try {
                    adapter.filter(newText.trim());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
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

   /* public boolean isContain(ArrayList<String> arrayList, String str){

        for(int i = 0; i < arrayList.size(); i++){
            if(arrayList.get(i).equals(str)){
                return true;
            }
        }
        return false;
    }*/


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
            Log.e("WTF", "call filter");
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

    /*  class MyTask extends AsyncTask<Void ,Void, Void>{
        Activity activity;

        public MyTask(Activity activity){
            this.activity = activity;
        }
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.exampleForList));
        }
    }*/

    public int getVisitId(String location) {
        Log.e("TAZ", location);
        String[] s1 = location.split("/");
        Log.e("TAZ", s1[s1.length - 1]);
        return Integer.parseInt(s1[s1.length - 1]);
    }

}
