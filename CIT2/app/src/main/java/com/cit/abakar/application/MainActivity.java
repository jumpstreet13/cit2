package com.cit.abakar.application;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.cit.abakar.application.database.Center;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private ListView listView;
    private ArrayList<Center> arr = new ArrayList<Center>();
    private YourAdapter adapter;
    private MyMediaPlayer myMediaPlayer;
    private MenuItem connection;
    public ProgressBar progressBar;
    public final static String URLSETTINS = "urlSettings";

    class MyTask extends AsyncTask<Void ,Void, Void>{
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
    }

    public ArrayList<String> getArrString(ArrayList<Center> cr){
        ArrayList<String> arr = new ArrayList<String>();
        for(Center cc : cr){
            arr.add(cc.name);
            Log.e("NAME", cc.name);
        }
        return arr;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(this);
        getActionBar().setTitle(R.string.ActionBarIsOnlineMainActivity);
        progressBar = (ProgressBar) findViewById(R.id.progressBarInActivityMain);
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < 10; i++) {
                Center center = new Center();
                center.name = "Узел " + i;
                center.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
        arr.addAll(Center.getAll());
        listView = (ListView) findViewById(R.id.listViewMain);
        adapter = new YourAdapter(MainActivity.this,getArrString(arr));
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(myMediaPlayer != null){
                    myMediaPlayer.reset();
                }
                myMediaPlayer = new MyMediaPlayer(MainActivity.this, "Button");
                myMediaPlayer.start();
                myMediaPlayer.setFree();
                Intent intent = new Intent(MainActivity.this, EquipmentActivity.class);
                Log.e("PZD", arr.get(position).getId().toString());
                intent.putExtra("id", arr.get(position).getId().toString());
                startActivity(intent);
            }
        });

    }

    public ArrayList<String> getBaseList(){
        return getArrString(arr);
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
        if(hasConnection(this)){
            connection.setIcon(R.drawable.ic_network_cell_white_24dp);
        }else{
            connection.setIcon(R.drawable.ic_signal_cellular_off_white_24dp);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                    adapter.filter(newText.toString().trim());
                    listView.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener(){

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

        return true;
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
                final Dialog dialog = new Dialog(MainActivity.this, R.style.DialogTheme);
                dialog.setContentView(R.layout.urldialog);
                dialog.setTitle("Введите новый URL");
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                TextView textView = (TextView) dialog.findViewById(R.id.textViewinMainActivityDialog);
                textView.setText(sharedPref.getString(URLSETTINS, getString(R.string.Adress_is_not_set_yet)));
                dialog.show();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                Button sendButton = (Button) dialog.findViewById(R.id.buttoninMainActivityDialog);
                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myMediaPlayer = new MyMediaPlayer(MainActivity.this, "Button");
                        myMediaPlayer.start();
                        myMediaPlayer.setFree();
                        EditText editText = (EditText) dialog.findViewById(R.id.editTextinMainActivityDialog);
                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(URLSETTINS, editText.getText().toString());
                        editor.commit();
                        dialog.dismiss();
                    }
                });
                return true;

            case R.id.conntection_settings:
                Toast toast = Toast.makeText(MainActivity.this,"Подключение к сети",Toast.LENGTH_SHORT);
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


    class YourAdapter extends BaseAdapter{

       private ArrayList<String> arr;
        private ArrayList<String> arr2;
       private Context context;

        public YourAdapter(Context context, ArrayList<String> arr){
            this.arr = arr;
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

            if(convertView == null){
                convertView = inflater.inflate(R.layout.mylist, parent, false);
            }
            TextView main = (TextView) convertView.findViewById(R.id.myListTextView);

            main.setText(arr.get(position));

            return convertView;
        }

        public void filter(String charText){
            Log.e("WTF", "call filter");
            charText = charText.toLowerCase();
            arr.clear();
            if(charText.length() == 0){
                arr.addAll(arr2);
            }else{
                for(String ss : arr2){
                    if(charText.length() != 0 && ss.toLowerCase().contains(charText)){
                        arr.add(ss);
                    }
                }
            }
            notifyDataSetChanged();

        }


    }

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

}
