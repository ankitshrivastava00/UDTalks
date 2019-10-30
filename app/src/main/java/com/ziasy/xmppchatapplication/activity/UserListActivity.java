package com.ziasy.xmppchatapplication.activity;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.adapter.UserListAdapter;
import com.ziasy.xmppchatapplication.common.ChatApplication;
import com.ziasy.xmppchatapplication.common.ConnectionDetector;
import com.ziasy.xmppchatapplication.common.CustomEditText;
import com.ziasy.xmppchatapplication.common.SessionManagement;
import com.ziasy.xmppchatapplication.listner.DrawableClickListener;
import com.ziasy.xmppchatapplication.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class UserListActivity extends AppCompatActivity implements OnClickListener {
    private RequestQueue requestQueue;
    private UserListAdapter adapter;
    private List<User> users;
    private ListView userList;
    private ConnectionDetector cd;
    private SessionManagement sd;
    private ProgressDialog pd;
    private TextView tv_toolbar_title;
    private Toolbar toolbar;
    private Socket mSocket;
    private ImageView iv_back;
    private CustomEditText searchEt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlist_layout);
        toolbar = (Toolbar) findViewById(R.id.tb_base);
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        searchEt = (CustomEditText) findViewById(R.id.searchEt);

        tv_toolbar_title.setText("Add Contact");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        userList = (ListView) findViewById(R.id.userList);
        users = new ArrayList<User>();
        cd = new ConnectionDetector(UserListActivity.this);
        sd = new SessionManagement(UserListActivity.this);
        pd = new ProgressDialog(UserListActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Please Wait");

        //  pd.show();
        if (!cd.isConnectingToInternet()) {
            Snackbar.make(findViewById(android.R.id.content), "Internet Connection not available..", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
          /*  users=DBUtil.fetchAllEmployeeList(UserListActivity.this);
            Collections.sort(users, EmployeeModel.StuNameComparator);

            adapter = new UserListAdapter(UserListActivity.this, users);
            userList.setAdapter(adapter);*/
        } else {
            ChatApplication app = (ChatApplication) getApplication();
            mSocket = app.getSocket();
            mSocket.on("employeeListDetail", onSendMyMessage);

            //getUsrList();

        }
        searchEt.setDrawableClickListener(new DrawableClickListener() {

            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        //Do something here
                        searchEt.setText("");
                        if (TextUtils.isEmpty(searchEt.getText().toString().trim())) {
                            if (adapter != null) {
                                adapter.filter("");
                            }
                        }
                        break;

                    default:
                        break;
                }
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(searchEt.getText().toString().trim())) {
                    if (adapter != null) {
                        adapter.filter("");
                    }
                } else {
                    if (adapter != null) {
                        adapter.filter(searchEt.getText().toString().trim());
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket!=null){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", sd.getKeyId());
        jsonObject.addProperty("isOnlineStatus", "false");
        mSocket.emit("disableUser", jsonObject);
    }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!cd.isConnectingToInternet()) {
            Snackbar.make(findViewById(android.R.id.content), "Internet Connection not available..", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
        } else {
            if (mSocket!=null){

                mSocket.connect();
            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.addProperty("senderid", sd.getKeyId());
            mSocket.emit("employeeList", jsonObject1);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", sd.getKeyId());
            jsonObject.addProperty("isChatEnable", "false");
            jsonObject.addProperty("isDelivered", "false");
            jsonObject.addProperty("isReaded", "false");
            jsonObject.addProperty("isOnlineStatus", "true");

            mSocket.emit("userid", jsonObject);
        }
    }
    }
    private Emitter.Listener onSendMyMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        users.clear();
                        pd.dismiss();
                        JSONObject data = (JSONObject) args[0];
                        Log.d("USERLISTDETA", data.toString());
                        JSONArray arr = data.getJSONArray("result");
                        for (int i = 0; i < arr.length(); i++) {
                            User allPrdctData = new User();
                            JSONObject obj1 = arr.getJSONObject(i);
                            String id = obj1.getString("id");
                            String name = obj1.getString("name");
                            String deviceid = obj1.getString("deviceid");
                            String photo = obj1.getString("photo");
                            String path = obj1.getString("path");
                            //String datetime = obj1.getString("datetime");
                            if (!id.equalsIgnoreCase(sd.getKeyId()) && !name.trim().equalsIgnoreCase("")) {
                                allPrdctData.setId(id);
                                allPrdctData.setName(name);
                                allPrdctData.setDid(deviceid);
                                allPrdctData.setCount("0");
                                allPrdctData.setDatetime("");
                                allPrdctData.setImageUrl(path + photo);
                                users.add(allPrdctData);
                             //   DBUtil.employeeInsert(UserListActivity.this,0,id,name,name,photo,deviceid,"'0'","false","false","0");

                            }
                        }
                        //    users=DBUtil.fetchAllEmployeeList(UserListActivity.this);
                        Collections.sort(users, User.StuNameComparator);
                        adapter = new UserListAdapter(UserListActivity.this, users);
                        userList.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                        pd.dismiss();
                    }
                }
            });
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(UserListActivity.this,ChatUserListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}