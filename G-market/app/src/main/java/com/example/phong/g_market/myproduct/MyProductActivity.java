package com.example.phong.g_market.myproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.phong.g_market.R;

public class MyProductActivity extends AppCompatActivity {

    ImageView imvBack;
    FloatingActionButton fabAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_my_product);

        imvBack = (ImageView) findViewById(R.id.imvBack);
        fabAddProduct = (FloatingActionButton) findViewById(R.id.btnAddProduct);

        Button();

    }

    private void Button(){
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProductActivity.this,AddProductActivity.class);
                startActivity(intent);
            }
        });
    }
}
