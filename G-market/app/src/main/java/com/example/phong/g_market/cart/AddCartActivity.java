package com.example.phong.g_market.cart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phong.g_market.R;
import com.example.phong.g_market.model.Product;
import com.example.phong.g_market.ultil.FirebaseMethod;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddCartActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Activity AddCart";

    ImageView imvBack;
    TextView tvName,tvCost,tvShop,tvSummary,tvAmount;
    Button btnMinus,btnPlus,btnNext;

    String mViewProduct;
    int kq = 0;
    int number = 1;
    int summaryCost = 0;
    String summary;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private FirebaseMethod mFirebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);

        imvBack = (ImageView) findViewById(R.id.imvBack);

        btnMinus = (Button) findViewById(R.id.btn_minus_add_cart);
        btnPlus = (Button) findViewById(R.id.btn_plus_add_cart);
        btnNext = (Button) findViewById(R.id.btnNext);

        tvAmount = (TextView) findViewById(R.id.tv_amount_cart);
        tvCost = (TextView) findViewById(R.id.tv_view_cart_product_cost);
        tvName = (TextView) findViewById(R.id.tv_view_cart_product_name);
        tvShop = (TextView) findViewById(R.id.tv_view_cart_product_shop);
        tvSummary = (TextView) findViewById(R.id.tv_summary_cost_cart);

        try {
            mViewProduct = getViewProduct();
            Log.d(TAG,"data" + mViewProduct);
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

    private void setupProduct(){
        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
        Query jqQuery = dbreference.child(getString(R.string.dbname_product))
                .orderByChild(getString(R.string.field_product_name))
                .equalTo(mViewProduct);
        jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {

                    tvCost.setText(ds.getValue(Product.class).getCost());
                    tvName.setText(ds.getValue(Product.class).getName());
                    tvShop.setText(ds.getValue(Product.class).getShop());

                    Log.d(TAG,"Ammount " + kq);

                    if(kq > ds.getValue(Product.class).getNumber()){
                        btnPlus.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void Button(){
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnMinus.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String s = tvAmount.getText().toString();
        summary = tvCost.getText().toString();

        switch (view.getId()){
            case R.id.btn_minus_add_cart:

                kq = Integer.parseInt(s) - number;
                tvAmount.setText(String.valueOf(kq));

                DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
                Query jqQuery = dbreference.child(getString(R.string.dbname_product))
                        .orderByChild(getString(R.string.field_product_name))
                        .equalTo(mViewProduct);
                jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                            Log.d(TAG,"Ammount " + kq);

                            if(kq < ds.getValue(Product.class).getNumber()){
                                btnPlus.setEnabled(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case R.id.btn_plus_add_cart:

                kq = Integer.parseInt(s) + number;
                tvAmount.setText(String.valueOf(kq));

                DatabaseReference dbreferences = FirebaseDatabase.getInstance().getReference();
                Query jqQuerys = dbreferences.child(getString(R.string.dbname_product))
                        .orderByChild(getString(R.string.field_product_name))
                        .equalTo(mViewProduct);
                jqQuerys.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                            Log.d(TAG,"Ammount " + kq);

                            if(kq == ds.getValue(Product.class).getNumber()){
                                btnPlus.setEnabled(false);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
        }
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
