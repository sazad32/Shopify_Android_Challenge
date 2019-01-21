package com.example.saimoon.customproducts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ProductAdapter extends BaseAdapter {
    private Context c;
    private ArrayList<String> productNames;
    private ArrayList<Integer> total;
    public ProductAdapter(Context context, ArrayList<String> productList,ArrayList<Integer> totalAvailable){
        c = context;
        productNames = productList;
        total = totalAvailable;
        Log.i("NAME SIZE", Integer.toString(productNames.size()));
        Log.i("QUANTITY SIZE", Integer.toString(total.size()));
    }

    @Override
    public int getCount() {
        return productNames.size();
    }

    @Override
    public Object getItem(int i) {
        return productNames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(c);
        View rows = layoutInflater.inflate(R.layout.row_quantity,viewGroup,false);


        TextView Amount = rows.findViewById(R.id.quantity);
        Amount.setText("Total Available: " + Integer.toString(total.get(i)));


        TextView nameText = rows.findViewById(R.id.productName);
        nameText.setText(productNames.get(i));



        return rows;

    }
}
