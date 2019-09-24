package com.ziasy.xmppchatapplication.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.adapter.ChatUserListAdapter;
import me.pushy.sdk.Pushy;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

public class ChatUserListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ChatUserListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list);
        recyclerView=(RecyclerView)findViewById(R.id.userList) ;
        Pushy.listen(ChatUserListActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(ChatUserListActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatUserListActivity.this,LinearLayoutManager.VERTICAL, false));
        adapter=new ChatUserListAdapter(ChatUserListActivity.this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {

    }
}