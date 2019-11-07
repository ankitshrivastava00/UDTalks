package com.ziasy.xmppchatapplication.activity;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.google.gson.JsonObject;
import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.adapter.AddContatctListAdapter;
import com.ziasy.xmppchatapplication.common.ConnectionDetector;
import com.ziasy.xmppchatapplication.common.SessionManagement;
import com.ziasy.xmppchatapplication.database.DBUtil;
import com.ziasy.xmppchatapplication.model.ChatUserList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class CareerContactListActivity extends AppCompatActivity implements OnClickListener {
    private List<ChatUserList>chatUserLists;

   private AddContatctListAdapter adapter;
   private ListView userList;
    private ConnectionDetector cd;
    private SessionManagement sd;
    private ProgressDialog pd;
    private TextView tv_toolbar_title;
    private Toolbar toolbar;
    private ImageView iv_back;
    private LinearLayout searchLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlist_layout);
        toolbar = (Toolbar) findViewById(R.id.tb_base);
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("Share Contact");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        userList = (ListView) findViewById(R.id.userList);
        chatUserLists=new ArrayList<>();
        cd = new ConnectionDetector(CareerContactListActivity.this);
        sd = new SessionManagement(CareerContactListActivity.this);
        pd = new ProgressDialog(CareerContactListActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Please Wait");

        //  pd.show();
       // if (!cd.isConnectingToInternet()) {
            /*Snackbar.make(findViewById(android.R.id.content), "Internet Connection not available..", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();*/
        chatUserLists.addAll(DBUtil.fetchAllChatList(CareerContactListActivity.this));
        adapter = new AddContatctListAdapter(CareerContactListActivity.this, chatUserLists);
        userList.setAdapter(adapter);
        /*} else {
            ChatApplication app = (ChatApplication) getApplication();
            mSocket = app.getSocket();
            mSocket.on("employeeListDetail", onSendMyMessage);*/

       userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              // Toast.makeText(CareerContactListActivity.this, "Position " + users.get(i), Toast.LENGTH_SHORT).show();
               StringBuffer stringBuffer = new StringBuffer();
               stringBuffer.append(chatUserLists.get(i).getName());
               stringBuffer.append("," + chatUserLists.get(i).getPhoto());
               stringBuffer.append("," + chatUserLists.get(i).getId());
               stringBuffer.append("," + chatUserLists.get(i).getDid());
               Intent intent = new Intent();
               intent.putExtra("share", (CharSequence) stringBuffer);
               setResult(RESULT_OK, intent);
               finish();
           }
       });
        //}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

}