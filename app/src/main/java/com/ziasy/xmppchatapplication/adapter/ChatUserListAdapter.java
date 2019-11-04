package com.ziasy.xmppchatapplication.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.activity.ChatUserListActivity;
import com.ziasy.xmppchatapplication.common.Permission;
import com.ziasy.xmppchatapplication.model.ChatUserList;
import com.ziasy.xmppchatapplication.single_chat.activity.SingleChatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatUserListAdapter extends RecyclerView.Adapter<ChatUserListAdapter.ViewHolder>{

    private List<ChatUserList> list;
    private List<ChatUserList> searchList;

    private Context context;
    public ChatUserListAdapter(Context context, List<ChatUserList> listdata) {
        this.context = context;
        this.list = listdata;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.showuser_swipe_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        /*if (list.get(position).getChattype().equalsIgnoreCase("group")) {
            viewHolder.linear_group.setVisibility(View.VISIBLE);
            viewHolder.linearIndivitual.setVisibility(View.GONE);


        } else {*/
            viewHolder.linear_group.setVisibility(View.GONE);
            viewHolder.linearIndivitual.setVisibility(View.VISIBLE);

        //}
     /*   if (list.get(position).getChattype().equalsIgnoreCase("group")) {
            if (list.get(position).getImageUrl().equalsIgnoreCase("No Image Found")){
                viewHolder.iv_profilePic.setImageResource(R.drawable.team);
            }else {
                Glide.with(context).load(list.get(position).getImageUrl()).into(viewHolder.iv_profilePic);
            }
        } else if (list.get(position).getChattype().equalsIgnoreCase("indivisual")) {
            viewHolder.iv_profilePic.setImageResource(R.drawable.user_placeholder);
        }*/
        if (position % 2 == 0) {
            viewHolder.view_career.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
        } else {
            viewHolder.view_career.setBackgroundColor(ContextCompat.getColor(context, R.color.ud_blue));
        }



        viewHolder.view_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Block User", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.view_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // chatUserListActivity.deleteUser(list.get(position).getId(),position);
            }
        });
        viewHolder.ll_view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("name", list.get(position).getName());
                intent.putExtra("rid", list.get(position).getId());
                intent.putExtra("chattype", "indivisual");
                intent.putExtra("did", list.get(position).getDid());
                intent.putStringArrayListExtra("forwardString", new ArrayList<>());
                intent.putExtra("type", "");
                intent.putExtra("mute", list.get(position).getMute());
                activity.startActivity(intent);*/
            }
        });
        viewHolder.view_profile_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(context, GroupProfileActivity.class);
                intent.putExtra("name", list.get(position).getName());
                intent.putExtra("rid", list.get(position).getId());
                intent.putExtra("image", list.get(position).getImageUrl());
                intent.putExtra("description", list.get(position).getDescription());
                intent.putExtra("chattype", "group");
                intent.putExtra("did", list.get(position).getDid());
                intent.putStringArrayListExtra("forwardString", new ArrayList<>());
                intent.putExtra("type", "");
                intent.putExtra("mute", list.get(position).getMute());

                context.startActivity(intent);*/
            }
        });
        viewHolder.view_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean callPermission = Permission.checkPermisionForCALL_PHONE(context);
                if (callPermission) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + list.get(position).getName()));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    context.startActivity(callIntent);
                }
            }
        });

        viewHolder.view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.more_layout);
                dialog.show();
                dialog.setCancelable(false);
                TextView textCancel = (TextView) dialog.findViewById(R.id.textCancel);
                textCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });*/

            }
        });


        viewHolder.view_add_new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   chatUserListActivity.leftGroup(list.get(position).getId(),list.get(position).getDid(),list.get(position).getDescription(),list.get(position).getAdmin(),list.get(position).getImageUrl(),position);
            }
        });

        viewHolder.userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i;
               /* if (list.get(position).getChattype().equalsIgnoreCase("group")) {
                    i = new Intent(activity, GroupChatActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    i.putExtra("image", list.get(position).getImageUrl());
                    i.putExtra("description", list.get(position).getDescription());
                    i.putExtra("admin", list.get(position).getAdmin());

                } else if (list.get(position).getChattype().equalsIgnoreCase("indivisual")) {
                    //i = new Intent(activity, SingleChatActivity.class);
                    i = new Intent(activity, ChatActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                }*/
                i = new Intent(context, SingleChatActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("mute", list.get(position).getMute());
                i.putExtra("name", list.get(position).getName());
                i.putExtra("rid", list.get(position).getId());
                i.putExtra("chattype", list.get(position).getChattype());
                i.putExtra("did", list.get(position).getDid());
                i.putStringArrayListExtra("forwardString", new ArrayList<String>());
                context.startActivity(i);
                ((ChatUserListActivity)context).finish();

            }
        });

        viewHolder.userName.setText(list.get(position).getName());

         viewHolder.msgtText.setText(list.get(position).getLastMessage());

    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(searchList);
        } else {
            for (ChatUserList wp : searchList) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    list.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //  NetworkImageView status;
        private RelativeLayout view_career;
        private TextView userName;
        //TextView dateText;
        // private ImageView statusIV;
        private RoundedImageView iv_profilePic;
        private TextView msgtText;
        private LinearLayout linear_group, linearIndivitual, userLayout, ll_view_profile, view_call, view_delete, view_block, view_more, view_add_new_user, view_profile_group;
        private SwipeLayout swipeLayout;
        public ViewHolder(View vi) {
            super(vi);
            this.userName = (TextView) vi.findViewById(R.id.tv_name);
            this.msgtText = (TextView) vi.findViewById(R.id.tv_products);
            this.view_career = (RelativeLayout) vi.findViewById(R.id.view_career);
            this.iv_profilePic = (RoundedImageView) vi.findViewById(R.id.iv_profilePic);
            this.linearIndivitual = (LinearLayout) vi.findViewById(R.id.linearIndivitual);
            this.linear_group = (LinearLayout) vi.findViewById(R.id.linear_group);
            this.userLayout = (LinearLayout) vi.findViewById(R.id.mainLayout);
            this.view_call = (LinearLayout) vi.findViewById(R.id.view_call);
            this.ll_view_profile = (LinearLayout) vi.findViewById(R.id.ll_view_profile);
            this.view_profile_group = (LinearLayout) vi.findViewById(R.id.view_profile_group);
            this.view_delete = (LinearLayout) vi.findViewById(R.id.view_delete);
            this.view_block = (LinearLayout) vi.findViewById(R.id.view_block);
            this.view_more = (LinearLayout) vi.findViewById(R.id.view_more);
            this.view_add_new_user = (LinearLayout) vi.findViewById(R.id.view_add_new_user);
            this.swipeLayout = (SwipeLayout) vi.findViewById(R.id.sample1);
            this.msgtText.setVisibility(View.VISIBLE);

        }
    }
}
