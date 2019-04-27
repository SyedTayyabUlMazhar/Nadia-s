package com.example.android.data;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.data.listView.DataItemAdapter;
import com.example.android.data.model.DataItem;
import com.example.android.data.sample.SampleDataProvider;

import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private static final int SIGNIN_REQUEST = 1001;
    public static final String MY_GLOBAL_PREFS = "MY_GLOBAL_PREFS";
    @SuppressWarnings("FieldCanBeLocal")
    private TextView tvOut;
    private List<DataItem> dataItemArrayList = SampleDataProvider.dataItemArrayList;

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener()
        {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
            {
                Log.d("preferences", "onSharedPreferencesChanged : " + key);
            }
        };

//        DataItem dataItem =
//                new DataItem(null, "My menu item", "a description",
//                        "a category", 1, 9.95, "apple_pie.jpg");

//        tvOut = (TextView) findViewById(R.id.out);
//        tvOut.setText("");
//
//        Collections.sort(dataItemArrayList, new Comparator<DataItem>() {
//            @Override
//            public int compare(DataItem o1, DataItem o2) {
//                return o1.getItemName().compareTo(o2.getItemName());
//            }
//        });
//        for(DataItem item : dataItemArrayList)
//        {
//            tvOut.append(item.getItemName()+"\n");
//        }

//        DataItemAdapter dataItemArrayAdapter = new DataItemAdapter(this, R.layout.data_item, dataItemArrayList);
//        ListView dataItemsListView = findViewById(R.id.content_main_listViewDataItemsView);
//        dataItemsListView.setAdapter(dataItemArrayAdapter);
        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this);
        setting.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        boolean grid = setting.getBoolean(getString(R.string.pref_display_grid), false);

        com.example.android.data.recyclerView.DataItemAdapter dataItemAdapter = new com.example.android.data.recyclerView.DataItemAdapter(this, dataItemArrayList);
        RecyclerView recyclerView = findViewById(R.id.content_main_recyclerView);
        if (grid)
        {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        recyclerView.setAdapter(dataItemAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signin:
                Intent intent = new Intent(this, SigninActivity.class);
                startActivityForResult(intent, SIGNIN_REQUEST);
                return true;

            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, PrefsActivity.class);
                startActivity(settingsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SIGNIN_REQUEST) {
            String email = data.getStringExtra(SigninActivity.EMAIL_KEY);
            Toast.makeText(this, "You signed in as " + email, Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor = getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE).edit();
            editor.putString(SigninActivity.EMAIL_KEY, email);
            editor.apply();
        }
    }
}
