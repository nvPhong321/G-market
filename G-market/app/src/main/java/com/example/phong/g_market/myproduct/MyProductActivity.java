package com.example.phong.g_market.myproduct;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.phong.g_market.R;
import com.example.phong.g_market.adapter.MyProductAdapter;
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

public class MyProductActivity extends AppCompatActivity {

    public static final String TAG = "Activity MyProduct";

    ImageView imvBack;
    FloatingActionButton fabAddProduct;
    private RecyclerView rcMyProduct;

    private MyProductAdapter adapterMyProduct;
    private ArrayList<Product> arrdataProduct;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;

    private ArrayList<String> mProductId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_my_product);

        arrdataProduct = new ArrayList<>();
        mProductId = new ArrayList<>();

        imvBack = (ImageView) findViewById(R.id.imvBack);
        fabAddProduct = (FloatingActionButton) findViewById(R.id.btnAddProduct);

        rcMyProduct = (RecyclerView) findViewById(R.id.rc_my_product);
        LinearLayoutManager lnProduct = new LinearLayoutManager(this);
        lnProduct.setOrientation(LinearLayoutManager.VERTICAL);
        rcMyProduct.setLayoutManager(lnProduct);

        Button();
        setupProduct();
        setupFirebaseAuth();

    }

    private void getProductID() {
        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
        Query jqQuery = dbreference.child(getString(R.string.dbname_product));
        jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {

                    mProductId.add(ds.getValue(Product.class).getProductId());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupProduct() {
            DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
            Query jqQuery = dbreference.child(getString(R.string.dbname_my_product))
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {

                        arrdataProduct.add(ds.getValue(Product.class));
                        Log.d(TAG, "ArrData:" + arrdataProduct.toString());
                        adapterMyProduct = new MyProductAdapter(arrdataProduct, MyProductActivity.this);
                        adapterMyProduct.notifyDataSetChanged();
                        rcMyProduct.setAdapter(adapterMyProduct);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    private void Button() {
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProductActivity.this, AddProductActivity.class);
                startActivity(intent);
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
