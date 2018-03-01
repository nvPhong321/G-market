package com.example.phong.g_market.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.GridView;

import com.example.phong.g_market.R;
import com.example.phong.g_market.adapter.ProductAdapter;
import com.example.phong.g_market.adapter.ProductGridAdapter;
import com.example.phong.g_market.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryProductActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "Activity CategoryProductActivity";

    private ViewStub stubGrid,stubRecycler;
    private GridView gridView;
    private RecyclerView rcProduct;
    private ProductAdapter adapterProduct;
    private ProductGridAdapter adapterGrid;
    private FloatingActionButton fabGrid,fabList;

    private int currentView = 0;
    static final int VIEW_MODE_RECYCLER = 1;
    static final int VIEW_MODE_GRID = 0;

    private ArrayList<Product> arrdataProduct;

    private String mCategory;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);

        arrdataProduct = new ArrayList<>();

        fabGrid = (FloatingActionButton) findViewById(R.id.btn_grid);
        fabList = (FloatingActionButton) findViewById(R.id.btn_list);

        stubGrid = (ViewStub) findViewById(R.id.stub_grid);
        stubRecycler = (ViewStub) findViewById(R.id.stub_list);

        stubGrid.inflate();
        stubRecycler.inflate();

        gridView = (GridView) findViewById(R.id.grid__category_product);
        rcProduct = (RecyclerView) findViewById(R.id.rc__category_product);
        LinearLayoutManager lnProduct = new LinearLayoutManager(this);
        lnProduct.setOrientation(LinearLayoutManager.VERTICAL);
        rcProduct.setLayoutManager(lnProduct);

        try {
            mCategory = getCategory();
            Log.d("data" , mCategory);
            setupProduct();
        } catch (NullPointerException e) {

        }

        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode",MODE_PRIVATE);
        currentView = sharedPreferences.getInt("CurrentViewMode",VIEW_MODE_RECYCLER);

        switchView();

        fabList.setOnClickListener(this);
        fabGrid.setOnClickListener(this);

        setupFirebaseAuth();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_grid:
                currentView = VIEW_MODE_GRID;
                fabList.setVisibility(View.VISIBLE);
                fabGrid.setVisibility(View.GONE);
                break;
            case R.id.btn_list:
                currentView = VIEW_MODE_RECYCLER;
                fabList.setVisibility(View.GONE);
                fabGrid.setVisibility(View.VISIBLE);
                break;
        }

        switchView();
        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("CurrentViewMode",currentView);
        editor.commit();
    }

    private void switchView(){
        if(VIEW_MODE_RECYCLER == currentView){
            stubRecycler.setVisibility(View.VISIBLE);
            stubGrid.setVisibility(View.GONE);
        }else {
            stubGrid.setVisibility(View.VISIBLE);
            stubRecycler.setVisibility(View.GONE);
        }

        setAdapter();
    }

    private void setAdapter(){

        if(VIEW_MODE_RECYCLER == currentView) {
            adapterProduct = new ProductAdapter(arrdataProduct, CategoryProductActivity.this);
            adapterProduct.notifyDataSetChanged();
            rcProduct.setAdapter(adapterProduct);
        }else {
            adapterGrid = new ProductGridAdapter(this,R.layout.item_gridview_product,arrdataProduct);
            adapterGrid.notifyDataSetChanged();
            gridView.setAdapter(adapterGrid);
        }
    }

    private String getCategory() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("bundle");
            if (bundle != null) {
                return bundle.getString("data");
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    private void setupProduct(){

        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
        Query jqQuery = dbreference.child(getString(R.string.dbname_product))
                .orderByChild(getString(R.string.field_product_category))
                .equalTo(mCategory);
        jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    arrdataProduct.add(ds.getValue(Product.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /* ------------------------- Fire Base ----------------------------*/

    private void setupFirebaseAuth() {

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
