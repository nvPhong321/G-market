package com.example.phong.g_market.cart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.phong.g_market.R;
import com.example.phong.g_market.adapter.MyCartAdapter;
import com.example.phong.g_market.model.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyCartActivity extends AppCompatActivity {

    public static final String TAG = "Activity MyCart";

    ImageView imvBack, imvDel;
    private RecyclerView rcMyCart;

    private MyCartAdapter adapterMyCart;
    private ArrayList<Cart> arrdataCart;
    private ArrayList<String> arrdataCartID;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        arrdataCart = new ArrayList<>();
        arrdataCartID = new ArrayList<>();

        imvBack = (ImageView) findViewById(R.id.imvBack);

        rcMyCart = (RecyclerView) findViewById(R.id.rc_my_cart);
        LinearLayoutManager lnProduct = new LinearLayoutManager(this);
        lnProduct.setOrientation(LinearLayoutManager.VERTICAL);
        rcMyCart.setLayoutManager(lnProduct);

        Button();
        setupProduct();
        setupFirebaseAuth();

    }

    private void getCartId() {
        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
        Query jqQuery = dbreference.child(getString(R.string.dbname_my_cart))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Log.d(TAG, "ArrDataID:" + ds.child(getString(R.string.field_cart_id)).getValue().toString());
                    arrdataCartID.add(ds.child(getString(R.string.field_cart_id)).getValue().toString());
                }

                setupProduct();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupProduct() {
            DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
            Query jqQuery = dbreference.child(getString(R.string.dbname_my_cart))
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {

                        Cart cart = new Cart();

                        Map<String, Object> objectsMap = (HashMap<String, Object>) ds.getValue();
                        cart.setCartId(objectsMap.get(getString(R.string.field_cart_id)).toString());
                        cart.setImagesProduct(objectsMap.get(getString(R.string.field_product_images)).toString());
                        cart.setSummary(objectsMap.get(getString(R.string.field_cart_summary)).toString());
                        cart.setAmmount(objectsMap.get(getString(R.string.field_product_ammount)).toString());
                        cart.setCost(objectsMap.get(getString(R.string.field_product_cost)).toString());
                        cart.setNameProduct(objectsMap.get(getString(R.string.field_product_name_cart)).toString());
                        cart.setProductId(objectsMap.get(getString(R.string.field_product_id)).toString());

                        arrdataCart.add(cart);
                        Log.d(TAG, "ArrData:" + arrdataCart.toString());

                    }

                    adapterMyCart = new MyCartAdapter(arrdataCart, MyCartActivity.this);
                    adapterMyCart.notifyDataSetChanged();
                    rcMyCart.setAdapter(adapterMyCart);
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
