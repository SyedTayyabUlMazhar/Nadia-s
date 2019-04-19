package com.example.android.data.listView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.data.R;
import com.example.android.data.model.DataItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DataItemAdapter extends ArrayAdapter<DataItem>
{
    private List<DataItem> dataItemArrayList;

    public DataItemAdapter(Context context, int resource, List<DataItem> objects)
    {
        super(context, resource, objects);

        this.dataItemArrayList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.data_item, null);
        }

        DataItem dataItem = dataItemArrayList.get(position);

        ImageView menuItemImage = convertView.findViewById(R.id.data_item_imageView);
        TextView itemName = convertView.findViewById(R.id.data_item_textViewItemName);

//        menuItemImage.setImageResource(R.drawable.apple_pie);
        try
        {
            menuItemImage.setImageDrawable(getDrawableFromAsset(dataItem.getImage()));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        itemName.setText(dataItem.getItemName());

        return convertView;
    }

    private Drawable getDrawableFromAsset(String name) throws IOException
    {
        Drawable drawable = null;

        InputStream inputStream = getContext().getResources().getAssets().open(name);
        drawable = Drawable.createFromStream(inputStream, null);

        return drawable;
    }
}
