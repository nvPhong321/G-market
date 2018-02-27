package com.example.phong.g_market.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phong.g_market.R;
import com.example.phong.g_market.model.Product;

import java.util.ArrayList;

/**
 * Created by phong on 2/2/2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.RecyclerViewHolder> {

    private ArrayList<Product> listData = new ArrayList<>();

    public ProductAdapter(ArrayList<Product> arrdata) {
        this.listData = arrdata;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemview = inflater.inflate(R.layout.item_recycler_product, parent, false);
        return new RecyclerViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tvNames.setText(listData.get(position).getName());
        holder.tvCost.setText(listData.get(position).getCost());
        holder.tvShop.setText(listData.get(position).getShop());
        //holder.imvProduct.setImageBitmap(listData.get(position).getImages());
    }
    @Override
    public int getItemCount() {
        return listData.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvNames,tvCost,tvShop;
        ImageView imvProduct;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvNames = (TextView) itemView.findViewById(R.id.tv_product_name);
            tvCost = (TextView) itemView.findViewById(R.id.tv_product_cost);
            tvShop = (TextView) itemView.findViewById(R.id.tv_product_shop);
            imvProduct = (ImageView) itemView.findViewById(R.id.imv_product);
        }

    }
}
