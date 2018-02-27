package com.example.phong.g_market.ultil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.phong.g_market.R;
import com.example.phong.g_market.model.Product;
import com.example.phong.g_market.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by phong on 2/6/2018.
 */

public class FirebaseMethod {

    public static final String TAG = "Firebase method";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private StorageReference mStorageReference;

    private String userID;
    private Activity mContext;

    public FirebaseMethod(Activity context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }

    }

    public int getImageCount(DataSnapshot dataSnapshot){
        int count = 0;
        for (DataSnapshot ds: dataSnapshot
                .child(mContext.getString(R.string.dbname_my_product))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .getChildren()){
            count ++;
        }
        return count;
    }

    public void uploadFile(int count, final String nameProduct,final String cost,final int amount,final String category,final String shop, final Uri imgUrl, Bitmap bm) {
        FilePaths filePaths = new FilePaths();
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageReference = mStorageReference
                .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/photo" +  (count + 1));
//        if(bm == null) {
//            bm = ImageManager.getBitMap(imgUrl);
//        }
//        byte [] bytes = ImageManager.getByteFromBitMap(bm,100);
        UploadTask uploadTask = null;
        uploadTask  = storageReference.putFile(imgUrl);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri firebaseURI = taskSnapshot.getDownloadUrl();

                // add the new photo to "photos" node and  'userphotos' node
                addPhotoToDatabase(nameProduct,cost,shop,amount,category,firebaseURI.toString());
                // navigate to the main feed so the user can see their photo
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

    }

    public void addPhotoToDatabase(String nameproduct, String cost, String shop, int amount, String categories, String imgUrl){

        String tag = StringManupulation.getTags(nameproduct);
        String newPhotoKey = mRef.child(mContext.getString(R.string.dbname_product)).push().getKey();
        Product product = new Product();
        product.setCost(cost);
        product.setName(tag);
        product.setCategories(categories);
        product.setNumber(amount);
        product.setShop(shop);
        product.setImages(imgUrl);
        product.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        product.setProductId(newPhotoKey);

        mRef.child(mContext.getString(R.string.dbname_my_product))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(newPhotoKey)
                .setValue(product);
        mRef.child(mContext.getString(R.string.dbname_product))
                .child(newPhotoKey)
                .setValue(product);
    }

    public void registerNewEmail(final String email, final String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, "that bai",
                                    Toast.LENGTH_SHORT).show();
                        } else if (task.isSuccessful()) {
                            userID = mAuth.getCurrentUser().getUid();
                            Toast.makeText(mContext, "thanh cong",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void addNewUser(String email, String username) {
        User user = new User(userID, email, username);

        mRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);
    }
}
