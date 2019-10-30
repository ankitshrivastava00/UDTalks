package com.ziasy.xmppchatapplication.single_chat.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.google.gson.JsonObject;
import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.activity.ChatUserListActivity;
import com.ziasy.xmppchatapplication.adapter.ChatUserListAdapter;
import com.ziasy.xmppchatapplication.common.ConnectionDetector;
import com.ziasy.xmppchatapplication.common.Permission;
import com.ziasy.xmppchatapplication.common.SessionManagement;
import com.ziasy.xmppchatapplication.database.DBUtil;
import com.ziasy.xmppchatapplication.model.JsonModelForChat;
import com.ziasy.xmppchatapplication.model.SingleChatModule;
import com.ziasy.xmppchatapplication.single_chat.adapter.SingleChatAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static com.ziasy.xmppchatapplication.common.Utils.dateConverter;
import static com.ziasy.xmppchatapplication.common.Utils.getSaltString;
import static com.ziasy.xmppchatapplication.common.Utils.timeConverter;

public class SingleChatActivity extends AppCompatActivity implements View.OnClickListener {
    private List<SingleChatModule>list;
    private SingleChatAdapter adapter;
    private RecyclerView recyclerView;
    private String rid, username,did;
    private TextView txt_name, txt_online_status, typingStatus;
    private RecordButton recordButton;
    private RecordView recordView;
    private LinearLayout hideEditText;
    private ConnectionDetector cd;
    private SessionManagement sd;
    private EditText msg_edittext;
    private boolean mTyping = false;
    private LinearLayoutManager layoutManager;
    private ImageView smileyBtn, ImageAttachment, ImageSetting, imageBack, sendButton, sendVoiceRecording;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        list=new ArrayList<>();
        rid = getIntent().getStringExtra("rid");
        username = getIntent().getStringExtra("name");
        txt_name = (TextView) findViewById(R.id.nameTv);
        txt_name.setText(username);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerId);
        hideEditText = (LinearLayout) findViewById(R.id.hideSliding);
        sendButton = (ImageView) findViewById(R.id.sendMessageBtn);
        msg_edittext = (EditText) findViewById(R.id.msgEditText);
        sendButton.setOnClickListener(this);
        cd=new ConnectionDetector(SingleChatActivity.this);
        sd=new SessionManagement(SingleChatActivity.this);
        scrollTodown();
        adapter=new SingleChatAdapter(SingleChatActivity.this,list,list,"");
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        msg_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* if (!mTyping) {
                    mTyping = true;
                    JsonObject object = new JsonObject();
                    object.addProperty("reciverid", rid);
                    object.addProperty("senderid", sd.getKeyId());
                    object.addProperty("msg", "typing...");
                    mSocket.emit("typing", object);
                }
                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);*/
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (msg_edittext.getText().toString().trim().isEmpty()) {
                    sendButton.setVisibility(View.GONE);
                    recordButton.setVisibility(View.VISIBLE);

                } else {
                    sendButton.setVisibility(View.VISIBLE);
                    recordButton.setVisibility(View.GONE);
                }
            }
        });

        // FOR ANIMATION RECORDING
         recordView = (RecordView) findViewById(R.id.record_view);
        recordButton = (RecordButton) findViewById(R.id.record_button);
        recordButton.setRecordView(recordView);

        recordView.setCancelBounds(8);
        recordView.setSmallMicColor(Color.parseColor("#c2185b"));
        //prevent recording under one Second
        recordView.setLessThanSecondAllowed(false);
        recordView.setSlideToCancelText("Slide To Cancel");
        recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //replyLinear.setVisibility(View.GONE);
              //  replyStatus=false;
                if (Permission.checkReadExternalStoragePermission(SingleChatActivity.this) &&
                        Permission.checkRecordPermission(SingleChatActivity.this) &&
                        Permission.permissionForExternal(SingleChatActivity.this)){
                    if (adapter != null) {
                        adapter.clearMusic();
                        for (SingleChatModule model : list) {
                            model.setSelect(false);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    hideEditText.setVisibility(View.GONE);
                    recordView.setVisibility(View.VISIBLE);
                    //    if (!mTyping) {
                    //      mTyping = true;
                   /* JsonObject object = new JsonObject();
                    object.addProperty("reciverid", rid);
                    object.addProperty("senderid", sd.getKeyId());
                    object.addProperty("msg", "recording");
                    mSocket.emit("typing", object);*/
                    //   }
                    //   mTypingHandler.removeCallbacks(onTypingTimeout);
                    //    mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
                    boolean attached_file6 = Permission.checkReadExternalStoragePermission(SingleChatActivity.this);
                    boolean recording = Permission.checkRecordPermission(SingleChatActivity.this);
                    boolean recordingWrite = Permission.permissionForExternal(SingleChatActivity.this);
                    if (attached_file6) {
                        if (recordingWrite) {
                            if (recording) {
                                try {
                                    new Handler().postDelayed(new Runnable(){
                                        @Override
                                        public void run(){
                                       //     startRecording();
                                        }
                                    }, 500);
                                    Log.d("RecordView", "onStart");
                                    //       Toast.makeText(SingleChatActivity.this, "OnStartRecord", Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }else{
                }
            }
            @Override
            public void onCancel() {
                hideEditText.setVisibility(View.VISIBLE);
                recordView.setVisibility(View.GONE);
                boolean attached_file6 = Permission.checkReadExternalStoragePermission(SingleChatActivity.this);
                boolean recording = Permission.checkRecordPermission(SingleChatActivity.this);
                boolean recordingWrite = Permission.permissionForExternal(SingleChatActivity.this);
                if (attached_file6) {
                    if (recordingWrite) {
                        if (recording) {
                            Log.d("RecordView", "onCancel");
                      //      cancelRecording();
                            /*JsonObject object = new JsonObject();
                            object.addProperty("reciverid", rid);
                            object.addProperty("senderid", sd.getKeyId());
                            mSocket.emit("stopTyping", object);*/
                        }
                    }
                }
            }

            @Override
            public void onFinish(long recordTime) {
                hideEditText.setVisibility(View.VISIBLE);
                recordView.setVisibility(View.GONE);
                boolean attached_file6 = Permission.checkReadExternalStoragePermission(SingleChatActivity.this);
                boolean recording = Permission.checkRecordPermission(SingleChatActivity.this);
                boolean recordingWrite = Permission.permissionForExternal(SingleChatActivity.this);
                if (attached_file6) {
                    if (recordingWrite) {
                        if (recording) {
                          /*  String time = getHumanTimeText(recordTime);

                            // Toast.makeText(SingleChatActivity.this, "onFinishRecord - Recorded Time is: " + time, Toast.LENGTH_SHORT).show();
                            Log.d("RecordView", "onFinish");

                            Log.d("RecordTime", time);
                            try{
                                JsonObject object = new JsonObject();
                                object.addProperty("reciverid", rid);
                                object.addProperty("senderid", sd.getKeyId());
                                mSocket.emit("stopTyping", object);
                                stopRecording();
                                insertInLocalDb(fileName,"audio", ".AMR", "NA");
                            }catch (Exception e){
                                Log.e(TAG,e.toString());
                            }*/

                        }
                    }
                }
            }

            @Override
            public void onLessThanSecond() {
                hideEditText.setVisibility(View.VISIBLE);
                recordView.setVisibility(View.GONE);
                boolean attached_file6 = Permission.checkReadExternalStoragePermission(SingleChatActivity.this);
                boolean recording = Permission.checkRecordPermission(SingleChatActivity.this);
                boolean recordingWrite = Permission.permissionForExternal(SingleChatActivity.this);
                if (attached_file6) {
                    if (recordingWrite) {
                        if (recording) {
                            //  Toast.makeText(SingleChatActivity.this, "OnLessThanSecond", Toast.LENGTH_SHORT).show();

                         //   cancelRecording();
                            Log.d("RecordView", "onLessThanSecond");
                        }
                    }
                }
            }
        });


     recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                hideEditText.setVisibility(View.VISIBLE);
                recordView.setVisibility(View.GONE);
                boolean attached_file6 = Permission.checkReadExternalStoragePermission(SingleChatActivity.this);
                boolean recording = Permission.checkRecordPermission(SingleChatActivity.this);
                boolean recordingWrite = Permission.permissionForExternal(SingleChatActivity.this);
                if (attached_file6) {
                    if (recordingWrite) {
                        if (recording) {
                            Log.d("RecordView", "Basket Animation Finished");

                        }
                    }
                }
            }
        });
        // FOR RECORDING END

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SingleChatActivity.this, ChatUserListActivity.class);
// set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.sendMessageBtn:
              //  replyLinear.setVisibility(View.GONE);
                String strMessage = msg_edittext.getText().toString().trim();
                /*if (replyStatus){
                    replyStatus=false;
                    if (strMessage.length() > 0) {
                        msg_edittext.setText("");
                        insertInLocalDb(replyMessage+"%@,%"+replyType+"%@,%"+strMessage, "reply", "NA", "NA");
                    }
                }else {*/
                 //   replyStatus=false;
                    if (strMessage.length() > 0) {
                        msg_edittext.setText("");
                        //DBUtil.singleChatInsert(strMessage, "msg", "NA", "NA");
                    }
               // }
                break;
        }
    }


    private String insertInLocalDb(String message, String type, String extension, String parent){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String time = timeConverter(formattedDate);
        String date = dateConverter(formattedDate);
        String mid= getSaltString();
        int list_position;
        if (cd.isConnectingToInternet()){
            list_position=2;
        }else{
            list_position=0;
        }
       /* if(list.size()>0){
            firstDate= list.get(list.size()-1).getDate();
        }
        Log.d("FISFSF",firstDate);
        if (date.equalsIgnoreCase(firstDate)) {
            firstDate = "";
        } else {
            date = firstDate;
        }*/
        msg_edittext.setText("");
        if (did==null){
            did=getIntent().getStringExtra("did");
        }
        switch (type){
           /* case "img":
                list.add(DBUtil.singleChatInsert(new JsonModelForChat(message, sd.getKeyId(), rid, date, "false", "All", time, "false", mid, "", type, "asdfa", "asdfsda", "", formattedDate, false, "true", did, extension,parent,list_position), rid));
                //list.add(new JsonModelForChat(message, sd.getKeyId(), rid, date, "false", "", time, "false", mid, "", type, "asdfa", "asdfsda", "", formattedDate, false, "true", did, extension,parent,list_position));
                break;
            case "audio":
                list.add(localDBHelper.insertSingleMessagesOffline(new JsonModelForChat(message, sd.getKeyId(), rid, date, "false", "All", time, "false", mid, "", type, "asdfa", "asdfsda", "", formattedDate, false, "true", did, extension,parent,list_position), rid));               //list.add(new JsonModelForChat(message, sd.getKeyId(), rid, date, "false", "", time, "false", mid, "", type, "asdfa", "asdfsda", "", formattedDate, false, "true", did, extension,parent,list_position));
                break;
            case "video":
                list.add(localDBHelper.insertSingleMessagesOffline(new JsonModelForChat(message, sd.getKeyId(), rid, date, "false", "All", time, "false", mid, "", type, "asdfa", "asdfsda", "", formattedDate, false, "true", did, extension,parent,list_position), rid));
                //list.add(new JsonModelForChat(message, sd.getKeyId(), rid, date, "false", "", time, "false", mid, "", type, "asdfa", "asdfsda", "", formattedDate, false, "true", did, extension,parent,list_position));
                break;
            case "pdf":
                File file1=new File(message);
                list.add(localDBHelper.insertSingleMessagesOffline(new JsonModelForChat(message, sd.getKeyId(), rid, date, "false", "All", time, "false", mid, "", type, file1.getName(), "asdfsda", "", formattedDate, false, "true", did, extension,parent,list_position), rid));
                //list.add(new JsonModelForChat(message, sd.getKeyId(), rid, date, "false", "", time, "false", mid, "", type, file1.getName(), "asdfsda", "", formattedDate, false, "true", did, extension,parent,list_position));
                break;*/
            default:
                list.add(DBUtil.singleChatInsert(SingleChatActivity.this,new SingleChatModule()));
                //list.add(new JsonModelForChat(message, sd.getKeyId(), rid, date, "false", "", time, "false", mid, message, type, "asdfa", "asdfsda", "", formattedDate, false, "true", did, extension,parent,list_position));
                break;
        }

        if (list.size()==1){
            adapter=new SingleChatAdapter(SingleChatActivity.this,list,list,"");
            recyclerView.setAdapter(adapter);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();

        } else {
            adapter=new SingleChatAdapter(SingleChatActivity.this,list,list,"");
            recyclerView.setAdapter(adapter);

        }
        scrollTodown();
        if(type.equals("msg") || type.equals("audio") || type.equals("pdf") || type.equals("emoji") || type.equals("location")
                || type.equals("share")){
           // sendData();
        }
        return mid;
    }

    private void scrollTodown() {
        recyclerView.setHasFixedSize(true);
        layoutManager =  new LinearLayoutManager(SingleChatActivity.this,LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(SingleChatActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
    }


}
