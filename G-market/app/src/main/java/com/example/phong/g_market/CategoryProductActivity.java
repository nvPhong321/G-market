package com.example.phong.g_market;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.GridView;

import com.example.phong.g_market.adapter.ProductAdapter;
import com.example.phong.g_market.adapter.ProductGridAdapter;
import com.example.phong.g_market.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CategoryProductActivity extends AppCompatActivity implements View.OnClickListener{

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

        setupProduct();

        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode",MODE_PRIVATE);
        currentView = sharedPreferences.getInt("CurrentViewMode",VIEW_MODE_RECYCLER);

        switchView();

        fabList.setOnClickListener(this);
        fabGrid.setOnClickListener(this);

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
            adapterProduct = new ProductAdapter(arrdataProduct);
            adapterProduct.notifyDataSetChanged();
            rcProduct.setAdapter(adapterProduct);
        }else {
            adapterGrid = new ProductGridAdapter(this,R.layout.item_gridview_product,arrdataProduct);
            adapterGrid.notifyDataSetChanged();
            gridView.setAdapter(adapterGrid);
        }
    }



    private List<Product> setupProduct(){
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
        return arrdataProduct;
    }
}
