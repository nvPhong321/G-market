package com.example.phong.g_market.myproduct;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.phong.g_market.R;
import com.example.phong.g_market.model.Category;
import com.example.phong.g_market.ultil.FirebaseMethod;
import com.example.phong.g_market.ultil.Permissions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {

    public static final String TAG = "Activity Add Product";

    ImageView imvBack,imvChooseImage,imvShowImage;
    EditText edtNameProduct,edtCost,edtShop;
    Spinner spnCategory;
    TextView tvAmount;
    Button btnMinus,btnPlus,btnComplete;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private FirebaseMethod mFirebaseMethods;

    private static final int VERIFY_PERMISSION_REQUEST = 1;
    private String mAppend = "file:/";
    private Bitmap bmp;
    private int imageCount = 0;
    private Intent intent;

    private String name,cost,shop,spinner;
    private int amount;
    private Uri imageUri;

    ArrayList<String> directoriesName;
    ArrayAdapter arrayAdapter;

    private static final int RESULT_LOAD_IMG = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mFirebaseMethods = new FirebaseMethod(AddProductActivity.this);

        directoriesName = new ArrayList<>();

        if(checkPermissionArray(Permissions.PERMISSIONS)){

        }else {
            verifyPermissions(Permissions.PERMISSIONS);
        }

        imvBack = (ImageView) findViewById(R.id.imvBack);
        imvChooseImage = (ImageView) findViewById(R.id.imvChooseImagesAddProduct);
        imvShowImage = (ImageView) findViewById(R.id.imvShowImagesAddProduct);

        edtCost = (EditText) findViewById(R.id.edtAddCostProduct);
        edtShop = (EditText) findViewById(R.id.edtMyShop);
        edtNameProduct = (EditText) findViewById(R.id.edtNameAddProduct);

        spnCategory = (Spinner) findViewById(R.id.spncategoryAddProduct);

        tvAmount = (TextView) findViewById(R.id.tv_amount);

        btnPlus = (Button) findViewById(R.id.btn_plus_add_shop);
        btnMinus = (Button) findViewById(R.id.btn_plus_add_shop);
        btnComplete = (Button) findViewById(R.id.btnComplete);

        getCategory();
        Button();
        setupFirebaseAuth();

    }

    private void Button(){
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imvChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInfoProduct();
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imvShowImage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else {
        }
    }

    private void getCategory(){
        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference();
        Query jqQuery = dbreference.child(getString(R.string.dbname_category));
        jqQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: found use:" + ds.getValue(Category.class).toString());

                    directoriesName.add(ds.getValue(Category.class).getName());
                    arrayAdapter  = new ArrayAdapter(AddProductActivity.this, android.R.layout.simple_spinner_item, directoriesName);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnCategory.setAdapter(arrayAdapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void setInfoProduct(){
        name = edtNameProduct.getText().toString();
        cost = edtCost.getText().toString();
        shop = edtShop.getText().toString();
        String a = tvAmount.getText().toString();
        amount = Integer.parseInt(a);

        String text = spnCategory.getSelectedItem().toString();

        mFirebaseMethods.uploadFile(imageCount,name,cost,amount,text,shop,imageUri,null);
    }

    private void verifyPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(
                AddProductActivity.this,
                permissions,
                VERIFY_PERMISSION_REQUEST
        );
    }

    private boolean checkPermissionArray(String[] permissions) {
        for(int i = 0 ; i <permissions.length ; i++){
            String check = permissions[i];
            if(!checkPermission(check)){
                return false;
            }
        }
        return true;
    }

    public boolean checkPermission(String permission) {
        int permissionRequest = ActivityCompat.checkSelfPermission(AddProductActivity.this,permission);
        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            return false;
        }else {
            return true;
        }
    }

    /* ------------------------- Fire Base ----------------------------*/
    private void setupFirebaseAuth() {

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
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
