package com.example.phong.g_market.cart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.phong.g_market.R;
import com.example.phong.g_market.adapter.MyCartAdapter;
import com.example.phong.g_market.model.Cart;
import com.example.phong.g_market.ultil.FilePaths;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.util.ArrayList;

public class MyCartActivity extends AppCompatActivity {

    public static final String TAG = "Activity MyCart";

    ImageView imvBack, imvDel;
    private RecyclerView rcMyCart;

    private MyCartAdapter adapterMyCart;
    private ArrayList<Cart> arrdataCart;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;

    public static final int PAYPAL_REQUEST_CODE = 7171;

    public static PayPalConfiguration pay = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(FilePaths.CLIENT_ID_PAYPAL);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,pay);
        startService(intent);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();

        arrdataCart = new ArrayList<>();

        imvBack = (ImageView) findViewById(R.id.imvBack);
        imvDel = (ImageView) findViewById(R.id.imv_delete);

        rcMyCart = (RecyclerView) findViewById(R.id.rc_my_cart);
        LinearLayoutManager lnProduct = new LinearLayoutManager(this);
        lnProduct.setOrientation(LinearLayoutManager.VERTICAL);
        rcMyCart.setLayoutManager(lnProduct);
        adapterMyCart = new MyCartAdapter(arrdataCart, MyCartActivity.this);
        rcMyCart.setAdapter(adapterMyCart);

        Button();
        setupProduct();
        setupFirebaseAuth();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation != null){

                    String paymentDetail = confirmation.getProofOfPayment().getState();
                    if(paymentDetail.equals("approved")){
                        Toast.makeText(MyCartActivity.this,"Thanh toán thành công ", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < arrdataCart.size(); i++) {
                            if (arrdataCart.get(i).isCheckClick()) {
                                mRef.child(getString(R.string.dbname_my_cart))
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(arrdataCart.get(i).getCartId())
                                        .removeValue();
                                arrdataCart.remove(i);
                                i--;
                                adapterMyCart.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show();
            }else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(this,"Invalid",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupProduct() {
            DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
            Query jqQuery = dbreference.child(getString(R.string.dbname_my_cart))
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {

                        arrdataCart.add(ds.getValue(Cart.class));
                        adapterMyCart.notifyDataSetChanged();
                         Log.d(TAG,"Data " + arrdataCart);

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

        imvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG,"Size arr : " + arrdataCart.size());

                for (int i = 0; i < arrdataCart.size(); i++) {
                    Log.d(TAG,"get position : " + arrdataCart.get(i).getCheck());
                    if (arrdataCart.get(i).getCheck()) {
                        mRef.child(getString(R.string.dbname_my_cart))
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(arrdataCart.get(i).getCartId())
                                .removeValue();
                        arrdataCart.remove(i);
                        i--;
                        adapterMyCart.notifyDataSetChanged();
                    }
                }
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
