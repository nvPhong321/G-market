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
import com.example.phong.g_market.model.Cart;
import com.example.phong.g_market.model.Product;
import com.example.phong.g_market.ultil.FirebaseMethod;
import com.example.phong.g_market.ultil.StringManupulation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class AddCartActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Activity AddCart";

    ImageView imvBack;
    TextView tvName,tvCost,tvShop,tvSummary,tvAmount;
    Button btnMinus,btnPlus,btnNext;

    String mViewProduct;
    int kq = 0;
    int number = 1;
    int summaryCost;
    String numberA,numberB;
    private String name,cost,summary,ammount,productId,images;
    private Cart cart;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private FirebaseMethod mFirebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);

        cart = new Cart();

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

        setupFirebaseAuth();
        Button();
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

                    tvName.setText(ds.getValue(Product.class).getName());
                    tvShop.setText(ds.getValue(Product.class).getShop());

                    productId = ds.getValue(Product.class).getProductId();
                    images = ds.getValue(Product.class).getImages();

                    String originalString = ds.getValue(Product.class).getCost().toString();
                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    tvCost.setText(formattedString);

                    numberA = ds.getValue(Product.class).getCost();
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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMyCart();
                Intent intent = new Intent(AddCartActivity.this,MyCartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addMyCart(){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();
        name = tvName.getText().toString();
        cost = tvCost.getText().toString();
        ammount = tvAmount.getText().toString();
        summary = tvSummary.getText().toString();

        String costs = StringManupulation.expandCost(cost);
        String summarys = StringManupulation.expandCost(summary);
        String newPhotoKey = mRef.child(getString(R.string.dbname_my_cart)).push().getKey();

        cart.setCartId(newPhotoKey);
        cart.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        cart.setProductId(productId);
        cart.setImagesProduct(images);
        cart.setNameProduct(name);
        cart.setCost(costs);
        cart.setAmmount(ammount);
        cart.setSummary(summarys);

        mRef.child(getString(R.string.dbname_my_cart))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(newPhotoKey)
                .setValue(cart);
    }

    @Override
    public void onClick(View view) {

        final String s = tvAmount.getText().toString();

        Log.d(TAG,"number b " + numberB);
        Log.d(TAG,"number a " + numberA);

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

                summaryCost = summaryCost - Integer.parseInt(numberA);
                String originalString1 = String.valueOf(summaryCost);
                Long longval1;
                if (originalString1.contains(",")) {
                    originalString1 = originalString1.replaceAll(",", "");
                }
                longval1 = Long.parseLong(originalString1);

                DecimalFormat formatter1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter1.applyPattern("#,###,###,###");
                String formattedString1 = formatter1.format(longval1);

                //setting text after format to EditText
                tvSummary.setText( String.valueOf(formattedString1));

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

                            if(kq == ds.getValue(Product.class).getNumber()){
                                btnPlus.setEnabled(false);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                summaryCost = summaryCost + Integer.parseInt(numberA);

                String originalString = String.valueOf(summaryCost);
                Long longval;
                if (originalString.contains(",")) {
                    originalString = originalString.replaceAll(",", "");
                }
                longval = Long.parseLong(originalString);

                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);

                //setting text after format to EditText
                tvSummary.setText( String.valueOf(formattedString));

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
