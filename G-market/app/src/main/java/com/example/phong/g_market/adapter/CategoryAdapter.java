package com.example.phong.g_market.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phong.g_market.R;
import com.example.phong.g_market.model.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by phong on 2/2/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.RecyclerViewHolder> {

    private ArrayList<Category> listData = new ArrayList<>();
    private Activity mContext;

    public CategoryAdapter(ArrayList<Category> arrdata, Activity context) {
        this.listData = arrdata;
        this.mContext = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemview = inflater.inflate(R.layout.item_recycler_category, parent, false);
        return new RecyclerViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        holder.mCategory = getItem(position);

        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
        Query jqQuery = dbreference.child(mContext.getString(R.string.dbname_category))
                .orderByChild(mContext.getString(R.string.field_name_category))
                .equalTo(getItem(position).getName());
        jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {


                    holder.tvNames.setText(ds.getValue(Category.class).getName());
                    Glide.with(mContext).load(ds.getValue(Category.class).getImages()).into(holder.imvCategory);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public Category getItem(int position) {
        return listData.get(position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvNames;
        CircleImageView imvCategory;
        Category mCategory;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvNames = (TextView) itemView.findViewById(R.id.tv_list_category);
            imvCategory = (CircleImageView) itemView.findViewById(R.id.imv_list_category);
        }

    }
}
