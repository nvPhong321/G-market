package com.example.phong.g_market.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phong.g_market.R;
import com.example.phong.g_market.cart.AddCartActivity;
import com.example.phong.g_market.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewProductActivity extends AppCompatActivity {

    public static final String TAG = "Activity ViewProduct";

    ImageView imvBack,imvReview;
    TextView tvNameProduct,tvCostProduct,tvShopProduct;
    LinearLayout btnAddCart;

    private String mViewProduct;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        btnAddCart = (LinearLayout) findViewById(R.id.btn_addCart);
        imvBack = (ImageView) findViewById(R.id.imvBack);
        imvReview = (ImageView) findViewById(R.id.imvReview);

        tvNameProduct = (TextView) findViewById(R.id.tv_view_product_name);
        tvCostProduct = (TextView) findViewById(R.id.tv_view_product_cost);
        tvShopProduct = (TextView) findViewById(R.id.tv_view_product_shop);

        try {
            mViewProduct = getViewProduct();
            Log.d("data" , mViewProduct);
            setupProduct();
        } catch (NullPointerException e) {

        }
        Button();
        setupFirebaseAuth();
    }

    private String getViewProduct() {
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

    private void Button(){
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = tvNameProduct.getText().toString();
                Log.d(TAG, "data : " + data);
                Bundle bundle = new Bundle();
                bundle.putString("data",data);
                Intent intent = new Intent(ViewProductActivity.this, AddCartActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
    }

    private void setupProduct(){
        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
        Query jqQuery = dbreference.child(getString(R.string.dbname_product))
                .orderByChild(getString(R.string.field_product_name))
                .equalTo(mViewProduct);
        jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {

                    tvCostProduct.setText(ds.getValue(Product.class).getCost());
                    tvNameProduct.setText(ds.getValue(Product.class).getName());
                    tvShopProduct.setText(ds.getValue(Product.class).getShop());
                    Glide.with(ViewProductActivity.this).load(ds.getValue(Product.class).getImages()).into(imvReview);
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
