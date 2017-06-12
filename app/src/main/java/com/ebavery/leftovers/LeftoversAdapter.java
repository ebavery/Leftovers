package com.ebavery.leftovers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Elizabeth on 5/5/2017.
 */

public class LeftoversAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<FridgeItem> list;

    public LeftoversAdapter(Context context, ArrayList<FridgeItem> list){
        this.mContext = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FridgeItem item = list.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_fridgeitem, null);
        }
        final ImageView label = (ImageView)convertView.findViewById(R.id.imgFridgeLabel);
        final TextView name = (TextView)convertView.findViewById(R.id.txtItemName);
        final TextView expDate = (TextView)convertView.findViewById(R.id.txtItemExpDate);

        label.setImageResource(item.getImageResource());
        name.setText(item.getName());
        expDate.setText(item.getExpDate());

        return convertView;
    }
}
