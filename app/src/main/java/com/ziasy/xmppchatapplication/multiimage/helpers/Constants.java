package com.ziasy.xmppchatapplication.multiimage.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Darshan on 5/26/2015.
 */
public class Constants {
    public static final int PERMISSION_REQUEST_CODE = 1000;
    public static final int PERMISSION_GRANTED = 1001;
    public static final int PERMISSION_DENIED = 1002;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final int REQUEST_CODE_IMAGE = 2000;
    public static final int REQUEST_CODE_PDF = 2001;
    public static final int REQUEST_CODE_Capture = 2002;

    public static final int FETCH_STARTED = 2001;
    public static final int FETCH_COMPLETED = 2002;
    public static final int ERROR = 2005;

    /**
     * Request code for permission has to be < (1 << 8)
     * Otherwise throws java.lang.IllegalArgumentException: Can only use lower 8 bits for requestCode
     */
    public static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 23;

    public static final String INTENT_EXTRA_ALBUM = "album";
    public static final String INTENT_EXTRA_IMAGES = "images";
    public static final String INTENT_EXTRA_LIMIT = "limit";
    public static int DEFAULT_LIMIT = 10;

    //Maximum number of images that can be selected at a time
    public static int limit;



    /*public static void setDefaultLimit(int limit){
        DEFAULT_LIMIT = limit;
    }*/

    public static Bitmap decodeURI(String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Only scale if we need to
        // (16384 buffer for img processing)
        Boolean scaleByHeight = Math.abs(options.outHeight - 200) >= Math.abs(options.outWidth - 200);
        if (options.outHeight * options.outWidth * 2 >= 16384) {
            // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
            double sampleSize = scaleByHeight
                    ? options.outHeight / 200
                    : options.outWidth / 200;
            options.inSampleSize =
                    (int) Math.pow(2d, Math.floor(
                            Math.log(sampleSize) / Math.log(2d)));
        }

        // Do the actual decoding
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[1024];
        Bitmap output = BitmapFactory.decodeFile(filePath, options);

        return output;
    }



}
