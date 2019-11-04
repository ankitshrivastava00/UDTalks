package com.ziasy.xmppchatapplication.single_chat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.google.gson.JsonObject;
import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.activity.ChatUserListActivity;
import com.ziasy.xmppchatapplication.common.ChatApplication;
import com.ziasy.xmppchatapplication.common.ConnectionDetector;
import com.ziasy.xmppchatapplication.common.Permission;
import com.ziasy.xmppchatapplication.common.SessionManagement;
import com.ziasy.xmppchatapplication.database.DBUtil;
import com.ziasy.xmppchatapplication.mapbox.MapBoxCurrentLocation;
import com.ziasy.xmppchatapplication.model.ChatUserList;
import com.ziasy.xmppchatapplication.model.SingleChatModule;
import com.ziasy.xmppchatapplication.multiimage.activities.AlbumSelectActivity;
import com.ziasy.xmppchatapplication.multiimage.helpers.Constants;
import com.ziasy.xmppchatapplication.reciever.PushReceiver;
import com.ziasy.xmppchatapplication.single_chat.adapter.SingleChatAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.socket.client.Socket;

import static com.ziasy.xmppchatapplication.common.Utils.dateConverter;
import static com.ziasy.xmppchatapplication.common.Utils.getSaltString;
import static com.ziasy.xmppchatapplication.common.Utils.timeConverter;

public class SingleChatActivity extends AppCompatActivity implements View.OnClickListener,  PushReceiver.RecievingMessageInterface {

    // PopLayoutForAddAttachment
    private LinearLayout SellPost, BuyPost, MomentPick, ImageUpload, TakePicture, Video, TakeVideo, MediaTools, Location, ContactInfo, FileUpload, AudioFile;
    //PopLayoutForChatSetting
    private LinearLayout clearChatLinear, addContactLinear, searchLinear, mediaLinear, notificationLinear, wallpaperLinear;
    private List<SingleChatModule>list;
    private SingleChatAdapter adapter;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private String receiverId, receiverName,did;
    private TextView txt_name, txt_online_status, typingStatus;
    private RecordButton recordButton;
    private RecordView recordView;
    private LinearLayout hideEditText;
    private ConnectionDetector cd;
    private SessionManagement sd;
    private EditText msg_edittext;
    private boolean mTyping = false;
    private static Socket mSocket;
    private LinearLayoutManager layoutManager;
    private ImageView smileyBtn, ImageAttachment, ImageSetting, imageBack, sendButton, sendVoiceRecording;
    private PopupWindow popupWindow, popupWindowForSetting;
    private boolean GpsStatus ;
    private LocationManager locationManager ;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 170;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        list=new ArrayList<>();

        receiverId = getIntent().getStringExtra("rid");
        receiverName = getIntent().getStringExtra("name");
        did=getIntent().getStringExtra("did");

        txt_name = (TextView) findViewById(R.id.nameTv);
        txt_name.setText(receiverName);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerId);
        hideEditText = (LinearLayout) findViewById(R.id.hideSliding);
        sendButton = (ImageView) findViewById(R.id.sendMessageBtn);

        ImageAttachment = (ImageView) findViewById(R.id.attachmentBtn);
        imageBack = (ImageView) findViewById(R.id.imageBack);
        ImageSetting = (ImageView) findViewById(R.id.settingIv);
        msg_edittext = (EditText) findViewById(R.id.msgEditText);
        imageBack.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        cd=new ConnectionDetector(SingleChatActivity.this);
        sd=new SessionManagement(SingleChatActivity.this);
        scrollTodown();


        ChatApplication app = (ChatApplication) getApplication();
        mSocket = app.getSocket();
        mSocket.connect();

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
        ImageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customViewSetting = layoutInflater.inflate(R.layout.setting_lauyout, null);
                // Create popup window.
                ImageSetting.setImageResource(R.drawable.setting_active);
                //close the popup window on button click

                popupWindowForSetting = new PopupWindow(customViewSetting, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                // Set popup window animation style.
                popupWindowForSetting.setAnimationStyle(R.style.popup_window_animation_sms);
                popupWindowForSetting.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                popupWindowForSetting.setFocusable(true);
                popupWindowForSetting.setOutsideTouchable(true);
                popupWindowForSetting.update();
                popupWindowForSetting.showAsDropDown(toolbar, 1, 1);
                popupWindowForSetting.update();

                // Show popup window offset 1,1 to smsBtton.
                popupWindowForSetting.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        ImageSetting.setImageResource(R.drawable.setting_inactive);
                    }
                });

                addContactLinear = (LinearLayout) customViewSetting.findViewById(R.id.addContactLinear);
                searchLinear = (LinearLayout) customViewSetting.findViewById(R.id.searchLinear);
                mediaLinear = (LinearLayout) customViewSetting.findViewById(R.id.mediaLinear);
                notificationLinear = (LinearLayout) customViewSetting.findViewById(R.id.notificationLinear);
                wallpaperLinear = (LinearLayout) customViewSetting.findViewById(R.id.wallpaperLinear);
                clearChatLinear = (LinearLayout) customViewSetting.findViewById(R.id.clearChatLinear);
                final Switch switchOne = (Switch) customViewSetting.findViewById(R.id.switchNotification);
               /* if (muteString!=null){
                    if (muteString.equalsIgnoreCase("true")) {
                        switchOne.setChecked(true);
                    }else {
                        switchOne.setChecked(false);
                    }
                }*/
                switchOne.setOnCheckedChangeListener(
                        new CompoundButton.OnCheckedChangeListener() {
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    //muteString = "true";
                                    switchOne.setChecked(true);
                                    Toast.makeText(SingleChatActivity.this, "Mute", Toast.LENGTH_SHORT).show();
                                } else {
                                   // muteString = "false";
                                    switchOne.setChecked(false);

                                    Toast.makeText(SingleChatActivity.this,
                                            "UnMute", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                addContactLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindowForSetting.dismiss();
                        // Toast.makeText(ChatActivity.this,"OnWorking",Toast.LENGTH_SHORT).show();
                        boolean cantact = Permission.checkPermisionForREAD_CONTACTS(SingleChatActivity.this);
                        if (cantact) {
                          //  addContact(username, rid);
                        }
                    }
                });
                searchLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindowForSetting.dismiss();
                        //searchLayout.setVisibility(View.VISIBLE);

                    }
                });
                mediaLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindowForSetting.dismiss();
                        /*Intent i =new Intent(SingleChatActivity.this, MediaActivity.class);
                        i.putExtra("id",receiverId);
                        i.putExtra("class_type","single");
                        startActivity(i);*/

                    }
                });

                notificationLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindowForSetting.dismiss();
                    }
                });
                wallpaperLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindowForSetting.dismiss();
                      /*  Intent i = new Intent(SingleChatActivity.this, WallpaperActivity.class);
                        startActivity(i);*/
                    }
                });
                clearChatLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindowForSetting.dismiss();
                       // new DeleteAllChatTask().execute();
                    }
                });
            }
        });


     ImageAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.popup, null);
                // Create popup window.

                //close the popup window on button click
                ImageAttachment.setImageResource(R.drawable.menu_active);
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
                        ImageAttachment.setImageResource(R.drawable.menu_inactive);
                    }
                });

                SellPost = (LinearLayout) customView.findViewById(R.id.SellPost);
                BuyPost = (LinearLayout) customView.findViewById(R.id.BuyPost);
                MomentPick = (LinearLayout) customView.findViewById(R.id.MomentPick);
                ImageUpload = (LinearLayout) customView.findViewById(R.id.ImageUpload);
                TakePicture = (LinearLayout) customView.findViewById(R.id.TakePicture);
                Video = (LinearLayout) customView.findViewById(R.id.Video);
                TakeVideo = (LinearLayout) customView.findViewById(R.id.TakeVideo);
                MediaTools = (LinearLayout) customView.findViewById(R.id.MediaTools);
                Location = (LinearLayout) customView.findViewById(R.id.Location);
                ContactInfo = (LinearLayout) customView.findViewById(R.id.ContactInfo);
                FileUpload = (LinearLayout) customView.findViewById(R.id.FileUpload);
                AudioFile = (LinearLayout) customView.findViewById(R.id.AudioFile);

                SellPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
//                        attachment = false;
//                        ImageAttachment.setImageResource(R.drawable.menu_inactive);
                        // Toast.makeText(ChatActivity.this,"OnWorking",Toast.LENGTH_SHORT).show();
                    }
                });
                BuyPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        // attachment = false;
                        // ImageAttachment.setImageResource(R.drawable.menu_inactive);
                    }
                });
                MomentPick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        //  attachment = false;
                        // ImageAttachment.setImageResource(R.drawable.menu_inactive);
                    }
                });
                ImageUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        //  attachment = false;
                        // ImageAttachment.setImageResource(R.drawable.menu_inactive);
                        boolean attached_file1 = Permission.checkReadExternalStoragePermission(SingleChatActivity.this);
                        boolean image_permission_write = Permission.permissionForExternal(SingleChatActivity.this);
                        if (attached_file1) {
                            if (image_permission_write) {
                                try {
                                    Intent intent = new Intent(SingleChatActivity.this, AlbumSelectActivity.class);
                                    intent.putExtra(Constants.INTENT_EXTRA_LIMIT, Constants.DEFAULT_LIMIT);
                                    startActivityForResult(intent, Constants.REQUEST_CODE_IMAGE);
/*
                                    Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(galleryintent, 143);*/
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                TakePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        // attachment = false;
                        //   ImageAttachment.setImageResource(R.drawable.menu_inactive);
                        boolean attached_file4 = Permission.checkReadExternalStoragePermission(SingleChatActivity.this);
                        boolean camera_permission = Permission.checkCameraPermission(SingleChatActivity.this);
                        boolean camera_permission_write = Permission.permissionForExternal(SingleChatActivity.this);
                        if (camera_permission_write) {
                            if (camera_permission) {
                                if (attached_file4) {
                                    try {
                                      //  cameraUpView();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                });
                Video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        boolean attached_file8 = Permission.checkReadExternalStoragePermission(SingleChatActivity.this);
                        boolean video_permission_write = Permission.permissionForExternal(SingleChatActivity.this);
                        if (video_permission_write) {
                            if (attached_file8) {
                                try {
/*                                    if (Build.VERSION.SDK_INT <19){
                                        Intent intent = new Intent();
                                        intent.setType("video/mp4");
                                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent.createChooser(intent, "Select videos"),SELECT_VIDEOS);
                                    } else {
                                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                        intent.setType("video/mp4");
                                        startActivityForResult(intent, SELECT_VIDEOS_KITKAT);
                                    }*/
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                TakeVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean recording = Permission.checkRecordPermission(SingleChatActivity.this);
                        boolean attached_file4 = Permission.checkReadExternalStoragePermission(SingleChatActivity.this);
                        boolean camera_permission = Permission.checkCameraPermission(SingleChatActivity.this);
                        boolean camera_permission_write = Permission.permissionForExternal(SingleChatActivity.this);
                        if (camera_permission_write) {
                            if (camera_permission) {
                                if (attached_file4) {
                                    if (recording) {
                                        try {
                                           /* Intent captureVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                            captureVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 50000);
                                            captureVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
                                            startActivityForResult(captureVideoIntent, VIDEO_CAPTURED);*/
                                            //cameraIntent();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                        popupWindow.dismiss();
                    }
                });
                MediaTools.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        boolean attached_file1 = Permission.checkReadExternalStoragePermission(SingleChatActivity.this);
                        boolean image_permission_write = Permission.permissionForExternal(SingleChatActivity.this);
                        if (attached_file1) {
                            if (image_permission_write) {
                                try {
                                 /*   Intent i = new Intent(SingleChatActivity.this, EditImageActivity.class);

                                    startActivityForResult(i, 420);*/
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                });
                Location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        //  attachment = false;
                        //  ImageAttachment.setImageResource(R.drawable.menu_inactive);
                        boolean fine_location = Permission.checkPermisionForACCESS_FINE_LOCATION(SingleChatActivity.this);
                        boolean attached_file3 = Permission.permissionAccessNetwork(SingleChatActivity.this);
                        if (fine_location) {
                            if (attached_file3) {

                                GPSStatus();

                                if(GpsStatus == true)
                                {
                                    Intent i =new Intent(SingleChatActivity.this, MapBoxCurrentLocation.class);
                                    startActivityForResult(i,REQUEST_CODE_AUTOCOMPLETE);
                                }else
                                {

                                    Intent  intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent1);

                                }
                            }
                        }
                    }
                });
                ContactInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popupWindow.dismiss();
                       /* Intent i = new Intent(SingleChatActivity.this, CareerContactListActivity.class);
                        startActivityForResult(i, MY_PERMISSIONS_CONTACT_SHARE);*/
                    }
                });

                FileUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  attachment = false;
                        popupWindow.dismiss();

                       /* if (!checkPermission()) {
                            openActivity();
                        } else {
                            if (checkPermission()) {
                                requestPermissionAndContinue();
                            } else {
                                openActivity();
                            }
                        }*/
                    }
                });

                AudioFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        boolean attached_file7 = Permission.checkReadExternalStoragePermission(SingleChatActivity.this);
                        boolean record_permission_write = Permission.permissionForExternal(SingleChatActivity.this);
                        if (attached_file7) {
                            if (record_permission_write) {
                              /*  Intent intent = new Intent(SingleChatActivity.this, AudioFileList.class);
                                startActivityForResult(intent, MY_PERMISSIONS_GET_AUDIO_PERMISSION);*/
                            }
                        }
                    }
                });
            }
        });


        SingleChatModule singleChatModule = new SingleChatModule();
        singleChatModule.setSenderId(sd.getKeyId());
        singleChatModule.setRecieverId(receiverId);
        add(singleChatModule);

        // FOR ANIMATION RECORDING

        // FOR RECORDING END

    }
    public void GPSStatus(){
        locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    public  void add(SingleChatModule singleChatModule){
        list =  DBUtil.fetchAllSingleChatList(SingleChatActivity.this,singleChatModule);
        adapter=new SingleChatAdapter(SingleChatActivity.this,list,list,"");
        recyclerView.setAdapter(adapter);
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
                case R.id.imageBack:
                onBackPressed();
                break;

            case R.id.sendMessageBtn:
              //  replyLinear.setVisibility(View.GONE);
                String strMessage = msg_edittext.getText().toString().trim();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                String time = timeConverter(formattedDate);
                String date = dateConverter(formattedDate);
                String mid= getSaltString();

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
                        SingleChatModule singleChatModule = new SingleChatModule();
                        singleChatModule.setSenderId(sd.getKeyId());
                        singleChatModule.setRecieverId(receiverId);
                        singleChatModule.setDatetime(formattedDate.toString());
                        singleChatModule.setTime(time);
                        singleChatModule.setDate(date);
                        singleChatModule.setMessage(strMessage);
                        singleChatModule.setIsRead("");
                        singleChatModule.setDeliver("");
                        singleChatModule.setChatType("singlechat");
                        singleChatModule.setResponse("");
                        singleChatModule.setHeading(receiverName);
                        singleChatModule.setSelect(true);
                        singleChatModule.setChatStatus("");
                        singleChatModule.setChatUploading("");
                        singleChatModule.setDeviceId(did);
                        singleChatModule.setChatImage("");
                        singleChatModule.setExtension("");
                        singleChatModule.setListPosition("");
                        singleChatModule.setParent("");
                        singleChatModule.setUid(mid);
                        singleChatModule.setGravitystatus("1");
                        list.add(DBUtil.singleChatInsert(SingleChatActivity.this,singleChatModule));
                        adapter.notifyDataSetChanged();
                        scrollTodown();

                        ChatUserList allPrdctData = new ChatUserList();
                        allPrdctData.setId(receiverId);
                        allPrdctData.setName(receiverName);
                        allPrdctData.setDescription("");
                        allPrdctData.setLastMessage(strMessage);
                        allPrdctData.setDatetime(formattedDate);
                        allPrdctData.setUserstatus("false");
                        allPrdctData.setTime(time);
                        allPrdctData.setPhoto("");
                        allPrdctData.setDtype("");
                        allPrdctData.setChattype("");
                        allPrdctData.setChattype("indivisual");
                        allPrdctData.setDid(did);
                        allPrdctData.setAdmin("");

                        DBUtil.chatUserListInsert(SingleChatActivity.this, allPrdctData);

                        Log.d("AttemptByService","process");
                        JsonObject object = new JsonObject();
                        object.addProperty("reciverid", receiverId);
                        object.addProperty("message", strMessage);
                        object.addProperty("dtype", "msg");
                        object.addProperty("sname", sd.getUserName());
                        object.addProperty("did", did);
                        object.addProperty("pusddid", sd.getUserFcmId());
                        object.addProperty("uid", sd.getKeyId());
                        object.addProperty("datetime", formattedDate);
                        object.addProperty("isread", "false");
                        object.addProperty("deliver", "false");
                        object.addProperty("senderid", sd.getKeyId());
                        object.addProperty("receivername", receiverName);
                        object.addProperty("sender_name",sd.getUserName());
                        mSocket.emit("sendMessage", object);
                        Log.d("SEND_SINGLE", object.toString());

                    }
               // }
                break;
                    default:
                        break;
         }
    }


    private void scrollTodown() {
        recyclerView.setHasFixedSize(true);
        layoutManager =  new LinearLayoutManager(SingleChatActivity.this,LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);

       // recyclerView.addItemDecoration(new DividerItemDecoration(SingleChatActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);

    }


    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        ChatApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void getChatList(ChatUserList chatModule) {

    }

    @Override
    public void getSingleChat( final SingleChatModule chatModule) {

        SingleChatActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (chatModule.getSenderId().equalsIgnoreCase(receiverId)&& chatModule.getRecieverId().equalsIgnoreCase(sd.getKeyId())){
                    list.add(chatModule);
                    if (adapter!=null) {
                        adapter.notifyDataSetChanged();
                        scrollTodown();
                    }else {
                        adapter=new SingleChatAdapter(SingleChatActivity.this,list,list,"");
                        recyclerView.setAdapter(adapter);
                        scrollTodown();
                    }
            }
        }
    });
        }
    }