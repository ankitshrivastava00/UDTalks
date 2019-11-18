package com.ziasy.xmppchatapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.common.Permission;
import com.ziasy.xmppchatapplication.common.SessionManagement;
import com.ziasy.xmppchatapplication.single_chat.activity.SingleChatActivity;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private TextView tv_toolbar_title,tv_mobile_no;
    private Toolbar toolbar;
    private ImageView iv_back;
    private SessionManagement sd;
    private String userName,rid,did,mute;
    private ImageView iv_call,iv_chat,iv_add_to_contact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        toolbar=(Toolbar)findViewById(R.id.tb_base) ;
        sd=new SessionManagement(ProfileActivity.this);
        tv_mobile_no=(TextView)findViewById(R.id.tv_mobile_no);
        tv_toolbar_title=(TextView)findViewById(R.id.tv_toolbar_title);
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_call=(ImageView)findViewById(R.id.iv_call);
        iv_chat=(ImageView)findViewById(R.id.iv_chat);
        iv_add_to_contact=(ImageView)findViewById(R.id.iv_add_to_contact);
        userName=getIntent().getStringExtra("name");
        rid=getIntent().getStringExtra("rid");
        did=getIntent().getStringExtra("did");
        mute=getIntent().getStringExtra("mute");
        tv_mobile_no.setText(userName);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                }
        });

        iv_add_to_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                }
        });

        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean callPermission = Permission.checkPermisionForCALL_PHONE(ProfileActivity.this);
                if (callPermission) {
                    if (!sd.getKeyId().equalsIgnoreCase(rid)){
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + userName));
                    if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                }else {
                        Toast.makeText(ProfileActivity.this,"This Is Your Number", Toast.LENGTH_LONG).show();

                    }
                }

                }
        });

        iv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sd.getKeyId().equalsIgnoreCase(rid)){
                    Intent i = new Intent(ProfileActivity.this, SingleChatActivity  .class);
                    // Intent    i = new Intent(ProfileActivity.this, SingleChatActivity.class);
                    i.putExtra("name", userName);
                    i.putExtra("rid", rid);
                    i.putExtra("did", did);
                    i.putExtra("mute", mute);

                    i.putExtra("chattype", "indivisual");
                    i.putStringArrayListExtra("forwardString",new ArrayList<>());
                    startActivity(i);
                    finish();
                }else {
                    Toast.makeText(ProfileActivity.this,"This Is Your Number Not Allow Self Chat", Toast.LENGTH_LONG).show();
                }

                }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);

}

}
