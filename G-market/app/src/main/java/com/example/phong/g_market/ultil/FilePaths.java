package com.example.phong.g_market.ultil;

import android.os.Environment;

/**
 * Created by phong on 9/9/2017.
 */

public class FilePaths {
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String PICTURES = ROOT_DIR + "/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM";

    public String FIREBASE_IMAGE_STORAGE = "Product/Users/";
}
