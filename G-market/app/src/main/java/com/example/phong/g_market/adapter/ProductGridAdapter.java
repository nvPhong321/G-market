package com.example.phong.g_market.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phong.g_market.R;
import com.example.phong.g_market.model.Product;

import java.util.ArrayList;

/**
 * Created by phong on 2/3/2018.
 */

public class ProductGridAdapter extends ArrayAdapter<Product> {

    private AppCompatActivity mActivity;
    private ArrayList<Product> arr;
    private int LayoutID;

    public ProductGridAdapter(@NonNull AppCompatActivity activity, int resource, @NonNull ArrayList<Product> objects) {
        super(activity, resource, objects);
        this.mActivity = activity;
        this.arr = objects;
        this.LayoutID = resource;
    }

    public static class ViewHolder{
        TextView tvName,tvCost,tvShop;
        ImageView imvProduct;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder;
        LayoutInflater inflater = mActivity.getLayoutInflater();

        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(LayoutID,parent,false);

            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_grid_product_name);
            viewHolder.tvCost = (TextView) convertView.findViewById(R.id.tv_grid_product_cost);
            viewHolder.tvShop = (TextView) convertView.findViewById(R.id.tv__grid_product_shop);
            viewHolder.imvProduct = (ImageView) convertView.findViewById(R.id.imv_grid_product);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(arr.get(position).getName());
        viewHolder.tvCost.setText(arr.get(position).getCost());
        viewHolder.tvShop.setText(arr.get(position).getShop());
        //viewHolder.imvProduct.setImageBitmap(arr.get(position).getImages());

        return convertView;
    }
}
