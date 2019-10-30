package com.ziasy.xmppchatapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.adapter.ChatUserListAdapter;
import com.ziasy.xmppchatapplication.common.CustomEditText;
import com.ziasy.xmppchatapplication.common.Utils;
import com.ziasy.xmppchatapplication.database.DBUtil;
import com.ziasy.xmppchatapplication.listner.DrawableClickListener;
import com.ziasy.xmppchatapplication.model.ChatUserList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.pushy.sdk.Pushy;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

public class ChatUserListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ChatUserListAdapter adapter;
    private List<ChatUserList>chatUserLists;
    private Toolbar toolbar;
    private ImageView iv_tab_user, iv_emoji, iv_search;
    private LinearLayout userLayout,mautoStartPermissionUser;
    private PopupWindow popupWindow;
    private View back_view;
    private RelativeLayout ll_emoji;
    private LinearLayout addGroup, addUserFromUD, addUserFromPhone, scanUser, searchLayout;
    private boolean clickStatus = true;
    private CustomEditText searchEt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list);
        toolbar = (Toolbar) findViewById(R.id.tb_chatlist);
        iv_tab_user = (ImageView) findViewById(R.id.iv_add_user);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        searchEt = (CustomEditText) findViewById(R.id.searchEt);
        iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
        back_view = (View) findViewById(R.id.back_view);
        ll_emoji = (RelativeLayout) findViewById(R.id.ll_emoji);
        userLayout = (LinearLayout) findViewById(R.id.userLayout);
        searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
        chatUserLists=new ArrayList<>();
        chatUserLists.addAll(DBUtil.fetchAllChatList(ChatUserListActivity.this));
        recyclerView=(RecyclerView)findViewById(R.id.userList) ;
        Pushy.listen(ChatUserListActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(ChatUserListActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatUserListActivity.this,LinearLayoutManager.VERTICAL, false));

        if (chatUserLists.size() == 0) {
            ll_emoji.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            //  tv_contacts_count.setText(users.size()+" Chat");
        } else {
            ll_emoji.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            //   tv_contacts_count.setText(users.size()+" Chat");
        //    Collections.sort(users, ChatUserList.StuNameComparator);
            adapter=new ChatUserListAdapter(ChatUserListActivity.this,chatUserLists);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickStatus) {
                    clickStatus = false;
                    iv_search.setImageResource(R.drawable.search_activte);
                    searchLayout.setVisibility(View.VISIBLE);

                } else {
                    iv_search.setImageResource(R.drawable.search_inactive);
                    searchLayout.setVisibility(View.GONE);
                    clickStatus = true;
                    searchEt.setText("");
                    if (TextUtils.isEmpty(searchEt.getText().toString().trim())) {
                        if (adapter != null) {
                            adapter.filter("");
                        }
                    }

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


        iv_tab_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.add_user, null,false);
                // Create popup window.

                //close the popup window on button click
                iv_tab_user.setImageResource(R.drawable.dot_activite);
                iv_search.setImageResource(R.drawable.search_inactive);
                searchLayout.setVisibility(View.GONE);

                clickStatus = true;
                back_view.setVisibility(View.VISIBLE);
                popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                // Set popup window animation style.
                popupWindow.setAnimationStyle(R.style.popup_window_animation_sms);

                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                popupWindow.setFocusable(true);

                popupWindow.setOutsideTouchable(true);

                popupWindow.update();
                // Show popup window offset 1,1 to smsBtton.
                popupWindow.showAsDropDown(toolbar, 1, 1);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        back_view.setVisibility(View.GONE);
                        iv_tab_user.setImageResource(R.drawable.dot_inactivity);
                        iv_search.setImageResource(R.drawable.search_inactive);
                        searchLayout.setVisibility(View.GONE);
                        clickStatus = true;
                    }
                });

                addGroup = (LinearLayout) customView.findViewById(R.id.addGroup);
                addUserFromUD = (LinearLayout) customView.findViewById(R.id.addUserFromUD);
                addUserFromPhone = (LinearLayout) customView.findViewById(R.id.addUserFromPhone);
                scanUser = (LinearLayout) customView.findViewById(R.id.scanUser);
                mautoStartPermissionUser=customView.findViewById(R.id.autoStartPermissionUser);

                if (Utils.checkautoStartPermissionNeeded(ChatUserListActivity.this, Build.MANUFACTURER)){
                    mautoStartPermissionUser.setVisibility(View.VISIBLE);
                    mautoStartPermissionUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.autoStartPermission(ChatUserListActivity.this, Build.MANUFACTURER);
                        }
                    });
                }
                addGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                     /*   Intent i = new Intent(ChatUserListActivity.this, CreateGroupActivity.class);
                        startActivity(i);
                        finish();*/
                    }
                });
                addUserFromUD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        Intent i = new Intent(ChatUserListActivity.this, UserListActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                addUserFromPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                scanUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });


    }

    @Override
    public void onClick(View view) {

    }
}