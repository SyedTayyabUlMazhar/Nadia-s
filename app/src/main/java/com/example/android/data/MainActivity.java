package com.example.android.data;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.data.listView.DataItemAdapter;
import com.example.android.data.model.DataItem;
import com.example.android.data.sample.SampleDataProvider;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @SuppressWarnings("FieldCanBeLocal")
    private TextView tvOut;
    private List<DataItem> dataItemArrayList = SampleDataProvider.dataItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        com.example.android.data.recyclerView.DataItemAdapter dataItemAdapter = new com.example.android.data.recyclerView.DataItemAdapter(this, dataItemArrayList);
        RecyclerView recyclerView = findViewById(R.id.content_main_recyclerView);
        recyclerView.setAdapter(dataItemAdapter);

    }
}
