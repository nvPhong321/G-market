package com.example.phong.g_market;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.phong.g_market.adapter.BannerAdapter;
import com.example.phong.g_market.adapter.CategoryAdapter;
import com.example.phong.g_market.adapter.ProductAdapter;
import com.example.phong.g_market.model.Category;
import com.example.phong.g_market.model.Product;
import com.example.phong.g_market.product.CategoryProductActivity;
import com.example.phong.g_market.product.ViewProductActivity;
import com.example.phong.g_market.profile.EditProfileActivity;
import com.example.phong.g_market.ultil.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static ViewPager viewPager;
    private RecyclerView rcCategory,rcProduct;
    private CategoryAdapter adapterCateGory;
    private ProductAdapter adapterProduct;

    public ArrayList<Integer> imvBanner = null;
    private ArrayList<Category> arrdata;
    private ArrayList<Product> arrdataProduct;
    private static int currentPage = 0;
    CircleIndicator indicator;
    private static final Integer[] mBanner= {R.drawable.banner1,R.drawable.banner2,R.drawable.banner3,R.drawable.banner4};

    ImageView imvMenu;
    private DrawerLayout drawerMenu;

    ViewStub stubLogin,stubLogout;

    LinearLayout btnLogout;
    RelativeLayout btnMenuCart,btnMenuShop,btnMenuInfo,btnMenuOrder;


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

        stubLogin = (ViewStub) findViewById(R.id.stub_login);
        stubLogout = (ViewStub) findViewById(R.id.stub_no_login);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);

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
    }

    private void settupMenu(){
        imvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerMenu.openDrawer(GravityCompat.START);
                switchView();
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
        switch (view.getId()){
            case R.id.rel_cart:
                break;
            case R.id.rel_my_shop:
                break;
            case R.id.rel_order:
                break;
            case R.id.rel_profile_info:
                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_logout:
                break;
        }
    }

    private void switchView(){
//        if(VIEW_MODE_RECYCLER == currentView){
//            stubRecycler.setVisibility(View.VISIBLE);
//            stubGrid.setVisibility(View.GONE);
//        }else {
//            stubGrid.setVisibility(View.VISIBLE);
//            stubRecycler.setVisibility(View.GONE);
//        }

        stubLogout.setVisibility(View.VISIBLE);
        stubLogin.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
    }

    private void setupViewPaper(){
        for(int i=0;i<mBanner.length;i++)
            imvBanner.add(mBanner[i]);

        viewPager.setAdapter(new BannerAdapter(MainActivity.this,imvBanner));
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

    private void setupCategory(){
        Bitmap clock = BitmapFactory.decodeResource(getResources(),R.drawable.clock);
        Bitmap tivi = BitmapFactory.decodeResource(getResources(),R.drawable.tivi);
        Bitmap smartphone = BitmapFactory.decodeResource(getResources(),R.drawable.smart_phone);
        Bitmap camera = BitmapFactory.decodeResource(getResources(),R.drawable.camera);
        Bitmap laptop = BitmapFactory.decodeResource(getResources(),R.drawable.laptop);
        Bitmap headphone = BitmapFactory.decodeResource(getResources(),R.drawable.headphone);


        arrdata.add(new Category(clock,"Đồng hồ"));
        arrdata.add(new Category(tivi,"Ti vi"));
        arrdata.add(new Category(smartphone,"Điện thoại"));
        arrdata.add(new Category(camera,"Máy ảnh"));
        arrdata.add(new Category(laptop,"Laptop"));
        arrdata.add(new Category(headphone,"Tai nghe"));

        adapterCateGory = new CategoryAdapter(arrdata);
        adapterCateGory.notifyDataSetChanged();
        rcCategory.setAdapter(adapterCateGory);

        rcCategory.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rcCategory ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        String data = arrdata.get(position).getName();
                        Bundle bundle = new Bundle();
                        bundle.putString("data",data);
                        Intent intent = new Intent(MainActivity.this, CategoryProductActivity.class);
                        intent.putExtra("bundle",bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }

    private void setupProduct(){
        Bitmap clock = BitmapFactory.decodeResource(getResources(),R.drawable.clock);
        Bitmap tivi = BitmapFactory.decodeResource(getResources(),R.drawable.tivi);
        Bitmap smartphone = BitmapFactory.decodeResource(getResources(),R.drawable.smart_phone);
        Bitmap camera = BitmapFactory.decodeResource(getResources(),R.drawable.camera);
        Bitmap laptop = BitmapFactory.decodeResource(getResources(),R.drawable.laptop);
        Bitmap headphone = BitmapFactory.decodeResource(getResources(),R.drawable.headphone);

        arrdataProduct.add(new Product("Đồng hồ đeo tay abcxyz", "10.750.000 Đ", "ABC shop",clock));
        arrdataProduct.add(new Product("Ti vi sony G9756", "14.500.000 Đ", "Nguyễn Kim",tivi));
        arrdataProduct.add(new Product("Điện thoại Asus Zenfone 3 ZE012", "8.000.000 Đ", "Thế giới di động",smartphone));
        arrdataProduct.add(new Product("Máy ảnh sony","14.500.000 Đ", "Nguyễn Kim",camera));
        arrdataProduct.add(new Product("Laptop Asus P550l","15.500.000 Đ", "Nguyễn Kim",laptop));
        arrdataProduct.add(new Product("Tai nghe sony N1","4.990.000 Đ", "Nguyễn Kim",headphone));

        adapterProduct = new ProductAdapter(arrdataProduct);
        adapterProduct.notifyDataSetChanged();
        rcProduct.setAdapter(adapterProduct);

        rcProduct.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rcCategory ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
//                        String data = arrdataProduct.get(position).getName();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("data",data);
                        Intent intent = new Intent(MainActivity.this, ViewProductActivity.class);
//                        intent.putExtra("bundle",bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }
}
