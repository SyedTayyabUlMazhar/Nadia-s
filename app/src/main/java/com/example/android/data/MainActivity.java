package com.example.android.data;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.data.database.DataSource;
import com.example.android.data.model.DataItem;
import com.example.android.data.sample.SampleDataProvider;
import com.example.android.data.utils.JSONHelper;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private static final int SIGNIN_REQUEST = 1001;
    public static final String MY_GLOBAL_PREFS = "MY_GLOBAL_PREFS";
    @SuppressWarnings("FieldCanBeLocal")
    private TextView tvOut;
    private List<DataItem> dataItemArrayList = SampleDataProvider.dataItemArrayList;

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    public static final String FILE_NAME = "textFile.txt";
    private static final int WRITE_PERMISSION = 2414;
    private boolean permissionGranted;

//    private SQLiteDatabase database;
    private DataSource dataSource;
    private Context context;
    private String[] categories;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    SharedPreferences setting;
    com.example.android.data.recyclerView.DataItemAdapter dataItemAdapter;
                RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

//        SQLiteOpenHelper sqLiteOpenHelper = new DBHelper(this);
//        database = sqLiteOpenHelper.getWritableDatabase();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        categories = getResources().getStringArray(R.array.categories);
        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, categories));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String categorySelected = categories[position];

                List<DataItem> dataItemsFromDB = dataSource.getAllItems(categorySelected);

//                Toast.makeText(context, categories[position], Toast.LENGTH_SHORT).show();
                setting = PreferenceManager.getDefaultSharedPreferences(context);
                setting.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

                boolean grid = setting.getBoolean(getString(R.string.pref_display_grid), false);

                dataItemAdapter = new com.example.android.data.recyclerView.DataItemAdapter(context, dataItemsFromDB);
                recyclerView = findViewById(R.id.content_main_recyclerView);
                if (grid)
                {
                    recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                }
                recyclerView.setAdapter(dataItemAdapter);
                drawerLayout.closeDrawer(drawerList);
            }

        });

        dataSource = new DataSource(this);
        dataSource.open();
        Toast.makeText(this, "Database aquired", Toast.LENGTH_SHORT).show();

        long rowsInTable = dataSource.getRowCount();

        if(rowsInTable==0)
        {
            dataSource.insert(SampleDataProvider.dataItemArrayList);

            Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Data already inserted", Toast.LENGTH_SHORT).show();
        }

        List<DataItem> dataItemsFromDB = dataSource.getAllItems(null);

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener()
        {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
            {
                Log.d("preferences", "onSharedPreferencesChanged : " + key);
            }
        };

        permissionGranted = Build.VERSION.SDK_INT <= 22;

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

        com.example.android.data.recyclerView.DataItemAdapter dataItemAdapter =
                new com.example.android.data.recyclerView.DataItemAdapter(this, dataItemsFromDB);
        RecyclerView recyclerView = findViewById(R.id.content_main_recyclerView);
        if (grid)
        {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        recyclerView.setAdapter(dataItemAdapter);

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        dataSource.close();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        dataSource.open();
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

            case R.id.action_export:
                boolean result = JSONHelper.exportToJSON(this, dataItemArrayList);
                if(result)
                {
                    Toast.makeText(this, "Export successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Export failed", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_import:
                List<DataItem> dataItemList = JSONHelper.importFromJSON(this);
                for (DataItem dataItem :
                        dataItemList)
                {
                    Log.d("JSONHelper", "Imported : " + dataItem.getItemName());
                }
                return true;

            case R.id.action_categories:
                drawerLayout.openDrawer(drawerList);
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

    private File getFile()
    {
        return new File(Environment.getExternalStorageDirectory(), FILE_NAME);
    }

    public boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable()
    {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    public boolean checkPermissions()
    {
        Log.d("EVENT", "checkPermissions");
        if(!isExternalStorageWritable() || !isExternalStorageReadable())
        {
            Toast.makeText(this, "The External Storage is not usable.", Toast.LENGTH_SHORT).show();
            return false;
        }

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED)
        {
            Log.d("EVENT","REQUEST permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        Log.d("EVENT", "onRequestPermissionsResult");

        if(requestCode==WRITE_PERMISSION)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                permissionGranted = true;
                Toast.makeText(this, "Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "You must grant permission",Toast.LENGTH_SHORT).show();
            }
        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
