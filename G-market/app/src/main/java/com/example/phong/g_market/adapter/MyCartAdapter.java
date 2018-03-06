package com.example.phong.g_market.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phong.g_market.R;
import com.example.phong.g_market.model.Cart;
import com.google.firebase.auth.FirebaseAuth;
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

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.RecyclerViewHolder> {

    private ArrayList<Cart> listData = new ArrayList<>();
    private Activity mContext;

    public MyCartAdapter(ArrayList<Cart> arrdata, Activity context) {
        this.listData = arrdata;
        this.mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemview = inflater.inflate(R.layout.item_recycler_my_cart, parent, false);
        return new RecyclerViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
        Query jqQuery = dbreference.child(mContext.getString(R.string.dbname_my_cart))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {

                    holder.tvNames.setText(ds.getValue(Cart.class).getNameProduct());
                    holder.tvAmmount.setText(String.valueOf(ds.getValue(Cart.class).getAmmount()));
                    Glide.with(mContext).load(ds.getValue(Cart.class).getImagesProduct()).into(holder.imvProduct);

                    String originalString = ds.getValue(Cart.class).getCost().toString();
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

                    String originalString1 = ds.getValue(Cart.class).getSummary().toString();
                    Long longval1;
                    if (originalString1.contains(",")) {
                        originalString1 = originalString1.replaceAll(",", "");
                    }
                    longval1 = Long.parseLong(originalString1);

                    DecimalFormat formatter1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter1.applyPattern("#,###,###,###");
                    String formattedString1 = formatter1.format(longval1);

                    holder.tvSummary.setText(formattedString1);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public Cart getItem(int position) {
        return listData.get(position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvNames,tvCost,tvSummary,tvAmmount;
        CheckBox chkCheck;
        Button btnPay;
        ImageView imvProduct;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvNames = (TextView) itemView.findViewById(R.id.tv_my_cart_name);
            tvCost = (TextView) itemView.findViewById(R.id.tv_my_cart_cost);
            tvSummary = (TextView) itemView.findViewById(R.id.tv_summary_cost_my_cart);
            tvAmmount = (TextView) itemView.findViewById(R.id.tv_amount_myCart);
            imvProduct = (ImageView) itemView.findViewById(R.id.imv_my_cart);
            btnPay = (Button) itemView.findViewById(R.id.btnPay);
            chkCheck = (CheckBox) itemView.findViewById(R.id.chkMyCart);
        }

    }
}
