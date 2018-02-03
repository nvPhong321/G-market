package com.example.phong.g_market.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phong.g_market.R;
import com.example.phong.g_market.model.Category;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by phong on 2/2/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.RecyclerViewHolder> {

    private ArrayList<Category> listData = new ArrayList<>();

    public CategoryAdapter(ArrayList<Category> arrdata) {
        this.listData = arrdata;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemview = inflater.inflate(R.layout.item_recycler_category, parent, false);
        return new RecyclerViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tvNames.setText(listData.get(position).getName());
        holder.imvCategory.setImageBitmap(listData.get(position).getImages());
    }
    @Override
    public int getItemCount() {
        return listData.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvNames;
        CircleImageView imvCategory;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvNames = (TextView) itemView.findViewById(R.id.tv_list_category);
            imvCategory = (CircleImageView) itemView.findViewById(R.id.imv_list_category);
        }

    }
}
