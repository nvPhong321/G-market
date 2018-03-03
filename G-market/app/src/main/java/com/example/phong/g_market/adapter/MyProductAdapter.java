package com.example.phong.g_market.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by phong on 2/2/2018.
 */

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.RecyclerViewHolder> {

    private ArrayList<Product> listData = new ArrayList<>();
    private Activity mContext;

    public MyProductAdapter(ArrayList<Product> arrdata, Activity context) {
        this.listData = arrdata;
        this.mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemview = inflater.inflate(R.layout.item_recycler_my_product, parent, false);
        return new RecyclerViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
        Query jqQuery = dbreference.child(mContext.getString(R.string.dbname_product))
                .orderByChild(mContext.getString(R.string.field_product_id))
                .equalTo(getItem(position).getProductId());
        jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {

                    holder.tvNames.setText(ds.getValue(Product.class).getName());
                    holder.tvShop.setText(ds.getValue(Product.class).getShop());
                    holder.tvAmmount.setText(String.valueOf(ds.getValue(Product.class).getNumber()));
                    holder.tvCategories.setText(ds.getValue(Product.class).getCategories());
                    Glide.with(mContext).load(ds.getValue(Product.class).getImages()).into(holder.imvProduct);

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
                    holder.tvCost.setText(formattedString);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public Product getItem(int position) {
        return listData.get(position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvNames,tvCost,tvShop,tvAmmount,tvCategories;
        ImageView imvProduct;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvNames = (TextView) itemView.findViewById(R.id.tv_my_product_name);
            tvCost = (TextView) itemView.findViewById(R.id.tv_my_product_cost);
            tvShop = (TextView) itemView.findViewById(R.id.tv_my_product_shop);
            tvAmmount = (TextView) itemView.findViewById(R.id.tv_amount_myProduct);
            tvCategories = (TextView) itemView.findViewById(R.id.tv_my_product_categories);
            imvProduct = (ImageView) itemView.findViewById(R.id.imv_my_product);
        }

    }
}
