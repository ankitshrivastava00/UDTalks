package com.ziasy.xmppchatapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.common.Confiq;
import com.ziasy.xmppchatapplication.common.ConnectionDetector;
import com.ziasy.xmppchatapplication.common.Permission;
import com.ziasy.xmppchatapplication.common.SessionManagement;
//import com.ziasy.xmppchatapplication.localDB.LocalDBHelper;
//import com.ziasy.xmppchatapplication.localDB.LocalFileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.pushy.sdk.Pushy;

public class LoginActivity extends AppCompatActivity {

    public static boolean create = false;
    //  public static ManualScannerSession session = null;

    //  TextView mtvCountryCode;
    // ImageView selectCode;
    private Button btnSubmitMobileNo;
    private EditText etMobileNo;
    private String mVerificationCode;
    private Button backBtn;
    private String countryCode = "86";
    private LinearLayout udLayout;
    private EditText edtMobile;
    private String strEmail;
    private Button btnSubmit;
    private SessionManagement sd;
    private ProgressDialog pd;
    private ConnectionDetector cd;
    private String eid, number, status, name;
    //LocalFileManager localFileManager;
   // LocalDBHelper localDBHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sd = new SessionManagement(LoginActivity.this);
        cd = new ConnectionDetector(LoginActivity.this);
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("please wait..");
        pd.setCancelable(false);
        edtMobile = (EditText) findViewById(R.id.mobile_no);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
      //  localDBHelper=new LocalDBHelper(this);
        //localDBHelper.callOnCreate();

        boolean externalStroage = Permission.permissionForExternal( LoginActivity.this);
       // boolean storagepermission = Permission.checkReadExternalStoragePermission(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail = edtMobile.getText().toString().toLowerCase().trim();

                if (strEmail.equalsIgnoreCase("") || strEmail.length()!=10) {
                    edtMobile.setError("Please Enter Mobile Number Of 10 Digits");
                } /*else if (strEmail.length()!=10) {
                    edtMobile.setError("Please Enter 10 Digit Number");
                }*/ else {
                    Log.e("FCMKEY",sd.getUserFcmId());
                    if (sd.getUserFcmId() == null || sd.getUserFcmId().equalsIgnoreCase("USER_FCM_ID")) {
                        // Register with Pushy
                        new RegisterForPushNotificationsAsync().execute();
                     }
                    else {
                        // Start Pushy notification service if not already running
                        Pushy.listen(LoginActivity.this);
                        loginSocket();

                    }

                }
            }
        });
        udLayout = (LinearLayout) findViewById(R.id.layout);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //testTask();
    }


    private class RegisterForPushNotificationsAsync extends AsyncTask<String, Void, Exception> {
       // ProgressDialog mLoading;

        public RegisterForPushNotificationsAsync() {
            // Create progress dialog and set it up
          /*  mLoading = new ProgressDialog(LoginActivity.this);
            mLoading.setMessage("registeringDevice");
            mLoading.setCancelable(false);

            // Show it
            mLoading.show();*/
pd.show();
        }

        @Override
        protected Exception doInBackground(String... params) {
            try {
                // Assign a unique token to this device
                String deviceToken = Pushy.register(LoginActivity.this);

                // Save token locally / remotely
                sd.setUserFcmId(deviceToken);
                Log.e("FCMKEY",sd.getUserFcmId()+" : "+deviceToken);            }
            catch (Exception exc) {
                // Return exc to onPostExecute
                return exc;
            }

            // Success
            return null;
        }

        @Override
        protected void onPostExecute(Exception exc) {
            Pushy.listen(LoginActivity.this);
            loginSocket();
            pd.dismiss();
            // Activity died?
            if (isFinishing()) {
                return;
            }


            // Hide progress bar
         //   mLoading.dismiss();

            // Registration failed?
            if (exc != null) {
                // Write error to logcat
                Log.e("Pushy", "Registration failed: " + exc.getMessage());
                loginSocket();
                // Display error dialog
               /* new AlertDialog.Builder(SplashScreen.this).setTitle("Title")
                        .setMessage(exc.getMessage())
                        .setPositiveButton(R.string.ok, null)
                        .create()
                        .show();*/
            }

            // Update UI with registration result

        }
    }
    private void loginSocket() {
        try {
            if (!cd.isConnectingToInternet()) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
                        .setAction("Ok", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.BLUE);
                snackbar.show();

            } else {
                pd.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Confiq.LOGIN,
                   new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                             /*   try {
                                    JSONObject object = new JSONObject(response);
                                    String id = object.getString("id");
                                    String number = object.getString("number");
                                    String status = object.getString("status");
                                    String name = object.getString("name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/

                                try {
                                    JSONObject object = new JSONObject(response.trim());
                                    JSONArray jsonArray = object.getJSONArray("result");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                        eid = object1.getString("id");
                                        number = object1.getString("number");
                                        status = object1.getString("status");
                                        name = object1.getString("name");
                                    }

                                    pd.dismiss();
                                    /*localFileManager= new LocalFileManager(LoginActivity.this);
                                    localFileManager.createVideoFolder();
                                    localFileManager.createAudioFolder();
                                    localFileManager.createImageFolder();
                                    localFileManager.createDocsFolder();*/
                                    if (status.equalsIgnoreCase("Already Registered")) {
                                        sd.setUserName(name);
                                        sd.setKeyId(eid);
                                        sd.setUserMobile(number);
                                        sd.setLoginStatus("true");
                                        Intent i = new Intent(LoginActivity.this, ChatUserListActivity.class);
                                        startActivity(i);
                                        finish();

                                    } else {
                                        sd.setUserName(name);
                                        sd.setKeyId(eid);
                                        sd.setUserMobile(number);
                                        sd.setLoginStatus("true");
                                        Intent i = new Intent(LoginActivity.this, ChatUserListActivity.class);
                                        startActivity(i);
                                        finish();
                                        // registration failed.
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pd.dismiss();
                            }
                        })
                {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("name", strEmail);
                        params.put("num", strEmail);
                        params.put("did", sd.getUserFcmId());
                        return params;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(stringRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            pd.dismiss();

        }
    }
}