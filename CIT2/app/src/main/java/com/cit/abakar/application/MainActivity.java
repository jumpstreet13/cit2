package com.cit.abakar.application;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    ListView listView;
    ArrayList<String> arr = new ArrayList<String>();
    YourAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!hasConnection(this)) {
            getActionBar().setTitle("Список узлов (оффлайн режим)");
        }else{
            getActionBar().setTitle("Список узлов (онлайн режим)");
        }
        for(int i = 0; i<10; i++){
            arr.add("Uzel " + i);
        }
        listView = (ListView) findViewById(R.id.listViewMain);
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.exampleForList));
        adapter = new YourAdapter(this,arr );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(MainActivity.this, EquipmentActivity.class);
                startActivity(intent);
            }
        });

    }

    public ArrayList<String> getBaseList(){
        return this.arr;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.searchmenu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_settings);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_settings).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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

        if(id == R.id.search_settings){
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
