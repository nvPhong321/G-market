package com.example.phong.g_market;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.phong.g_market.adapter.BannerAdapter;
import com.example.phong.g_market.adapter.CategoryAdapter;
import com.example.phong.g_market.adapter.ProductAdapter;
import com.example.phong.g_market.cart.MyCartActivity;
import com.example.phong.g_market.model.Category;
import com.example.phong.g_market.model.Product;
import com.example.phong.g_market.model.User;
import com.example.phong.g_market.myproduct.MyProductActivity;
import com.example.phong.g_market.product.CategoryProductActivity;
import com.example.phong.g_market.product.ViewProductActivity;
import com.example.phong.g_market.profile.EditProfileActivity;
import com.example.phong.g_market.profile.LoginActivity;
import com.example.phong.g_market.ultil.RecyclerItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Activity Login";

    public static ViewPager viewPager;
    private RecyclerView rcCategory, rcProduct;
    private CategoryAdapter adapterCateGory;
    private ProductAdapter adapterProduct;

    public ArrayList<Integer> imvBanner = null;
    private ArrayList<Category> arrdata;
    private ArrayList<Product> arrdataProduct;
    private static int currentPage = 0;
    CircleIndicator indicator;
    private static final Integer[] mBanner = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4};

    ImageView imvMenu;
    TextView tvName,tvEmail;
    private DrawerLayout drawerMenu;

    RelativeLayout listMenu;

    LinearLayout btnLogout;
    RelativeLayout btnMenuCart, btnMenuShop, btnMenuInfo, btnMenuOrder;
    String userID;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        imvBanner = new ArrayList<>();
        arrdata = new ArrayList<Category>();
        arrdataProduct = new ArrayList<>();

        imvMenu = (ImageView) findViewById(R.id.imv_open_list);
        drawerMenu = (DrawerLayout) findViewById(R.id.drawer_layout);
        btnLogout = (LinearLayout) findViewById(R.id.btn_logout);

        btnMenuCart = (RelativeLayout) findViewById(R.id.rel_cart);
        btnMenuInfo = (RelativeLayout) findViewById(R.id.rel_profile_info);
        btnMenuOrder = (RelativeLayout) findViewById(R.id.rel_order);
        btnMenuShop = (RelativeLayout) findViewById(R.id.rel_my_shop);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        tvEmail = (TextView) findViewById(R.id.tv_Email);
        tvName = (TextView) findViewById(R.id.tv_UserName);
        listMenu = (RelativeLayout) findViewById(R.id.listMenu);

        rcCategory = (RecyclerView) findViewById(R.id.rc_category);
        rcProduct = (RecyclerView) findViewById(R.id.rc_product);
        LinearLayoutManager lnCatgory = new LinearLayoutManager(this);
        lnCatgory.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager lnProduct = new LinearLayoutManager(this);
        lnProduct.setOrientation(LinearLayoutManager.VERTICAL);
        rcCategory.setLayoutManager(lnCatgory);
        rcProduct.setLayoutManager(lnProduct);

        setupViewPaper();
        setupCategory();
        setupProduct();

        settupMenu();
        setupFirebaseAuth();
    }

    private void settupMenu() {

        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
        Query jqQuery = dbreference.child(getString(R.string.dbname_users))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG,"user : " + dataSnapshot.getValue(User.class).toString());
                tvName.setText(dataSnapshot.getValue(User.class).getUsername());
                tvEmail.setText(dataSnapshot.getValue(User.class).getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerMenu.openDrawer(GravityCompat.START);
            }
        });

        btnMenuOrder.setOnClickListener(this);
        btnMenuInfo.setOnClickListener(this);
        btnMenuCart.setOnClickListener(this);
        btnMenuShop.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rel_cart:
                Intent intent1 = new Intent(MainActivity.this, MyCartActivity.class);
                startActivity(intent1);
                break;
            case R.id.rel_my_shop:
                Intent intent2 = new Intent(MainActivity.this, MyProductActivity.class);
                startActivity(intent2);
                break;
            case R.id.rel_order:
                break;
            case R.id.rel_profile_info:
                Intent intent3 = new Intent(MainActivity.this, EditProfileActivity.class);
                startActivity(intent3);
                break;
            case R.id.btn_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent4 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent4);
                finish();
                break;
        }
    }

    private boolean isStringNull(String string) {
        if (string.equals("")) {
            return true;
        } else {
            return false;
        }
    }


    private void setupViewPaper() {
        for (int i = 0; i < mBanner.length; i++)
            imvBanner.add(mBanner[i]);

        viewPager.setAdapter(new BannerAdapter(MainActivity.this, imvBanner));
        indicator.setViewPager(viewPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == mBanner.length) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }

    private void setupCategory() {

        mRef = FirebaseDatabase.getInstance().getReference();
        Query jqQuery = mRef.child(getString(R.string.dbname_category));
        jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: found use:" + ds.getValue(Category.class).toString());

                    arrdata.add(ds.getValue(Category.class));

                    adapterCateGory = new CategoryAdapter(arrdata, MainActivity.this);
                    adapterCateGory.notifyDataSetChanged();
                    rcCategory.setAdapter(adapterCateGory);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rcCategory.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rcCategory, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        String data = arrdata.get(position).getName();
                        Log.d(TAG, "Name category " + data);
                        Bundle bundle = new Bundle();
                        bundle.putString("data", data);
                        Intent intent = new Intent(MainActivity.this, CategoryProductActivity.class);
                        intent.putExtra("bundle", bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }

    private void setupProduct() {

        mRef = FirebaseDatabase.getInstance().getReference();
        Query jqQuery = mRef.child(getString(R.string.dbname_product));
        jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: found use:" + ds.getValue(Category.class).toString());

                    arrdataProduct.add(ds.getValue(Product.class));

                    adapterProduct = new ProductAdapter(arrdataProduct, MainActivity.this);
                    adapterProduct.notifyDataSetChanged();
                    rcProduct.setAdapter(adapterProduct);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rcProduct.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rcCategory, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        String data = arrdataProduct.get(position).getName();
                        Bundle bundle = new Bundle();
                        bundle.putString("data", data);
                        Intent intent = new Intent(MainActivity.this, ViewProductActivity.class);
                        intent.putExtra("bundle", bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }

    /* ------------------------- Fire Base ----------------------------*/

    private void checkCurrentUser(FirebaseUser user){
        if(user == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void setupFirebaseAuth() {

        mAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();
        userID = mAuth.getCurrentUser().getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in
                checkCurrentUser(user);

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
        checkCurrentUser(mAuth.getCurrentUser());
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
