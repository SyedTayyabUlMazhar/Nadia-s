package com.example.android.data.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import com.example.android.data.DetailActivity;
import com.example.android.data.Helper;
import com.example.android.data.R;
import com.example.android.data.model.DataItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder>
{

    public static final String ITEM_ID = "ITEM_ID";
    public static final String ITEM = "ITEM";
    public static int layoutId;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView menuItemImage;
        public TextView menuItemName;
        public View view;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            if(DataItemAdapter.layoutId==R.layout.data_item)
            {
                this.menuItemImage = itemView.findViewById(R.id.data_item_imageView);
                this.menuItemName = itemView.findViewById(R.id.data_item_textViewItemName);
            }
            else
            {
                this.menuItemImage = itemView.findViewById(R.id.data_item_grid_imageView);
                this.menuItemName = itemView.findViewById(R.id.data_item_grid_textViewItemName);
            }

            this.view = itemView;
        }
    }

    private Context context;
    private List<DataItem> dataItemList;

    public DataItemAdapter(Context context, List<DataItem> items)
    {
        this.context = context;
        this.dataItemList = items;
    }

    @NonNull
    @Override
    public DataItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
        boolean grid = setting.getBoolean(context.getString(R.string.pref_display_grid), false);
        this.layoutId = grid ? R.layout.data_item_grid : R.layout.data_item;

        View itemView = layoutInflater.inflate(layoutId, viewGroup, false);

        DataItemAdapter.ViewHolder viewHolder = new DataItemAdapter.ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i)
    {

        final DataItem dataItem = dataItemList.get(i);

        viewHolder.menuItemName.setText(dataItem.getItemName());
        try
        {
            viewHolder.menuItemImage.setImageDrawable(Helper.getDrawableFromAsset(dataItem.getImage(), context));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        viewHolder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Toast.makeText(context, "You touched : " + dataItem.getItemName(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, DetailActivity.class);
//                intent.putExtra(ITEM_ID, dataItem.getItemId());
                intent.putExtra(ITEM, dataItem);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return dataItemList.size();
    }

//    private Drawable getDrawableFromAsset(String name) throws IOException
//    {
//        Drawable drawable = null;
//
//        InputStream inputStream = context.getResources().getAssets().open(name);
//        drawable = Drawable.createFromStream(inputStream, null);
//
//        return drawable;
//    }


}

