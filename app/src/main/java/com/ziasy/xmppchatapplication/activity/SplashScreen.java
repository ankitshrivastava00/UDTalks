package com.ziasy.xmppchatapplication.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.common.ChatApplication;
import com.ziasy.xmppchatapplication.common.SessionManagement;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_APN_SETTINGS;


/**
 * Created by PnP on 04-10-2016.
 */
public class SplashScreen extends Activity {
    public static int TIME = 5000;
    private final int PERMISSION_REQUEST_CODE = 123;
    PowerManager pm;
    String pkg;
    String DeviceModel, DeviceName;
    SessionManagement sd;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        sd = new SessionManagement(SplashScreen.this);
        start();
        /*if (!checkPermission()) {
            start();
        } else {
            if (checkPermission()) {
                requestPermissionAndContinue();
            } else {*/

        // Not registered yet?

         /*   }
        }*/


    }

    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, WRITE_APN_SETTINGS) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_APN_SETTINGS) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_APN_SETTINGS) && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_PHONE_STATE)
                    ) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("TITLE");
                alertBuilder.setMessage("WRITE SETTINGS is nencessary to wrote event");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SplashScreen.this, new String[]{WRITE_APN_SETTINGS, READ_PHONE_STATE
                        }, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.d("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(SplashScreen.this, new String[]{
                        WRITE_APN_SETTINGS, READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    start();
                } else {
                    finish();
                }

            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void start() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
           /*     if (sd.getLoginStatus().equalsIgnoreCase("true")&&sd.getLoginStatus().equalsIgnoreCase("false")) {
                    Intent i = new Intent(SplashScreen.this, LoadingActivity.class);
                    startActivity(i);
                    finish();
                } else    */
           if (sd.getLoginStatus().equalsIgnoreCase("true")) {
                    Intent i = new Intent(SplashScreen.this, ChatUserListActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        }, TIME);
    }

}