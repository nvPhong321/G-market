package com.example.phong.g_market.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.phong.g_market.R;
import com.example.phong.g_market.cart.AddCartActivity;

public class ViewProductActivity extends AppCompatActivity {

    ImageView imvBack;
    LinearLayout btnAddCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        btnAddCart = (LinearLayout) findViewById(R.id.btn_addCart);

        imvBack = (ImageView) findViewById(R.id.imvBack);
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewProductActivity.this, AddCartActivity.class);
                startActivity(intent);
            }
        });
    }
}
