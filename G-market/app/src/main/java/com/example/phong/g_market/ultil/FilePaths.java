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
    public static String CLIENT_ID_PAYPAL = "ATJ3mlTXbGBO4ohSXEEXBGEm6JapF2F8uXxCzJDirLPv1XXVbj-3oIWxA_gUMHVy5ext0Fpqtm23LhA8";
}
