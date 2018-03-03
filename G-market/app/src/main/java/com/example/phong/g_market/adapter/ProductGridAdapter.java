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

import com.bumptech.glide.Glide;
import com.example.phong.g_market.R;
import com.example.phong.g_market.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

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

        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
        Query jqQuery = dbreference.child(mActivity.getString(R.string.dbname_product))
                .orderByChild(mActivity.getString(R.string.field_product_id))
                .equalTo(arr.get(position).getProductId());
        jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {

                    viewHolder.tvName.setText(ds.getValue(Product.class).getName());
                    viewHolder.tvShop.setText(ds.getValue(Product.class).getShop());
                    Glide.with(mActivity).load(ds.getValue(Product.class).getImages()).into(viewHolder.imvProduct);

                    String originalString = ds.getValue(Product.class).getCost().toString();
                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    viewHolder.tvCost.setText(formattedString);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return convertView;
    }
}
