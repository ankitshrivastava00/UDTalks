package com.ziasy.xmppchatapplication.single_chat.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.activity.ProfileActivity;
import com.ziasy.xmppchatapplication.callback.ScrollInterface;
import com.ziasy.xmppchatapplication.common.Confiq;
import com.ziasy.xmppchatapplication.common.ConnectionDetector;
import com.ziasy.xmppchatapplication.common.Permission;
import com.ziasy.xmppchatapplication.common.SessionManagement;
import com.ziasy.xmppchatapplication.database.DBUtil;
import com.ziasy.xmppchatapplication.mapbox.MapBoxActivity;
import com.ziasy.xmppchatapplication.model.JsonModelForChat;
import com.ziasy.xmppchatapplication.model.SingleChatModule;
import com.ziasy.xmppchatapplication.single_chat.activity.SingleChatActivity;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SingleChatAdapter extends RecyclerView.Adapter<SingleChatAdapter.ViewHolder> {

    public List<SingleChatModule> selected_usersList;
    public List<SingleChatModule> chatMessageList;
    private List<SingleChatModule> searchList;
    private SessionManagement sd;
    private Activity context;
    private SingleChatActivity chatActivity;
    private static MediaPlayer mediaPlayer;
    private Timer timer;
    private SingleChatModule message;
    private SeekBar seekBar;
    private ImageView outgoing_image_view_audio,outgoing_image_view_audio_stop,incoming_image_view_audio,incoming_image_view_audio_stop;
    int position;
    private String mode;
    ConnectionDetector connectionDetector;

    public SingleChatAdapter(Activity activity, List<SingleChatModule> list, List<SingleChatModule> selected_usersList, String mode) {
        this.chatMessageList = list;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(chatMessageList);
        this.selected_usersList = selected_usersList;
        this.context = activity;
        this.chatActivity = (SingleChatActivity) activity;
        this.mode= mode;
        connectionDetector=new ConnectionDetector(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item, null, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final SingleChatModule message = (SingleChatModule) chatMessageList.get(position);
        viewHolder.layout.setVisibility(View.GONE);
        viewHolder.parent_layout.setVisibility(View.GONE);
        viewHolder.outgoing_location_relative.setVisibility(View.GONE);
        viewHolder.incoming_location_relative.setVisibility(View.GONE);
        viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
        viewHolder.incoming_linear_contact.setVisibility(View.GONE);
        viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
        viewHolder.incoming_relative_emoji.setVisibility(View.GONE);

       if (message.getSenderId().equalsIgnoreCase(new SessionManagement(context).getKeyId())) {

           switch (message.getChatType()){
               case "msg":
                   viewHolder.outgoingTextImage.setText(message.getMessage());
                   viewHolder.outgoing_text_time.setText(message.getTime());
                   viewHolder.layout.setVisibility(View.VISIBLE);


                   break;
               case "location":
                   viewHolder.outgoing_location_relative.setVisibility(View.VISIBLE);
                   viewHolder.outgoing_location_time.setText(message.getTime());
                   String strLocation[] = message.getMessage().split(",@,");
                   viewHolder.outgoing_location_address.setText(strLocation[2]);

                   viewHolder.outgong_linear_click.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           boolean fine_location = Permission.checkPermisionForACCESS_FINE_LOCATION(context);

                           if (fine_location){
                               chatActivity.GPSStatus();
                               if(chatActivity.GpsStatus == true)
                               {
                                   String strL[] = message.getMessage().split(",@,");
                                   Intent i = new Intent(context, MapBoxActivity.class);
                                   i.putExtra("lat", Double.valueOf(strL[0]));
                                   i.putExtra("lang", Double.valueOf(strL[1]));
                                   i.putExtra("title", strL[2]);
                                   context.startActivity(i);
                               }else
                               {
                                   Intent  intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                   context.startActivity(intent1);
                               }
                           }
                       }
                   });

                   break;
               case "share":
                   viewHolder.outgoing_linear_contact.setVisibility(View.VISIBLE);
                   viewHolder.outgoing_contact_time.setText(message.getTime());
                   String str[] = message.getMessage().split(",");
                   viewHolder.outgoing_contact_number.setText(str[0]);
                   viewHolder.outgoing_contact_view.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent i = new Intent(context, ProfileActivity.class);
                           i.putExtra("name", str[0]);
                           i.putExtra("rid", str[2]);
                           i.putExtra("did", str[3]);
                           context.startActivity(i);
                       }
                   });
                   break;

                   case "emoji":
                       viewHolder.outgoing_relative_emoji.setVisibility(View.VISIBLE);
                       viewHolder.outgoing_emoji_time.setText(message.getTime());
                       /*Resources res =  context.getResources();*/
                       File imgFile=new File(DBUtil.emojiPath(context,message.getMessage()));
                       Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    /*int resID = res.getIdentifier(mDrawableName , "drawable", context.getPackageName());
                    Drawable drawable = res.getDrawable(resID );*/
                       viewHolder.outgoing_emoji_image.setImageBitmap(myBitmap);
                   break;
                   default:
                       viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                       viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                       viewHolder.layout.setVisibility(View.GONE);
                       viewHolder.parent_layout.setVisibility(View.GONE);
                       viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                       viewHolder.incoming_location_relative.setVisibility(View.GONE);
                       viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                       viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                       break;
           }
        }
        else {
           switch (message.getChatType()){
               case "msg":
                   viewHolder.parent_layout.setVisibility(View.VISIBLE);
                   viewHolder.incomming_text.setText(message.getMessage());
                   viewHolder.incoming_text_time.setText(message.getTime());
                   break;
               case "location":
                   viewHolder.incoming_location_relative.setVisibility(View.VISIBLE);
                   viewHolder.incoming_location_time.setText(message.getTime());
                   String strLocation[] = message.getMessage().split(",@,");
                   //String currLocation = "&daddr=" + strLocation[2];
                   viewHolder.incoming_location_address.setText(strLocation[2]);

                   viewHolder.incoming_location_click.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           boolean fine_location = Permission.checkPermisionForACCESS_FINE_LOCATION(context);

                           if (fine_location){

                               chatActivity.GPSStatus();
                               if(chatActivity.GpsStatus == true)
                               {
                                   String strL[] = message.getMessage().split(",@,");

                                   Intent i = new Intent(context, MapBoxActivity.class);
                                   i.putExtra("lat", Double.valueOf(strL[0]));
                                   i.putExtra("lang", Double.valueOf(strL[1]));
                                   i.putExtra("title", strL[2]);
                                   context.startActivity(i);
                               }else
                               {
                                   Intent  intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                   context.startActivity(intent1);
                               }

                           }
                       }
                   });
                   break;
               case "share":

                   viewHolder.incoming_linear_contact.setVisibility(View.VISIBLE);
                   String[] strData = message.getMessage().split(",");
                   viewHolder.incoming_contact_number.setText(strData[0]);
                   viewHolder.incoming_view_image.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent i = new Intent(context, ProfileActivity.class);
                           i.putExtra("name", strData[0]);
                           i.putExtra("rid", strData[2]);
                           i.putExtra("did", strData[3]);
                           context.startActivity(i);
                       }
                   });
                   break;

               case "emoji":
                   viewHolder.incoming_emoji_time.setText(message.getTime());

                  viewHolder.incoming_relative_emoji.setVisibility(View.VISIBLE);
                   File imgFile=new File(DBUtil.emojiPath(context,message.getMessage()));
                   Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    /*int resID = res.getIdentifier(mDrawableName , "drawable", context.getPackageName());
                    Drawable drawable = res.getDrawable(resID );*/
                   viewHolder.incoming_emoji_image.setImageBitmap(myBitmap);
                   break;
               default:
                   viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                   viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                   viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                   viewHolder.layout.setVisibility(View.GONE);
                   viewHolder.incoming_location_relative.setVisibility(View.GONE);
                   viewHolder.parent_layout.setVisibility(View.GONE);
                   viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                   viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                   break;
           }

        }
      /*  switch (message.getChatType()) {
        }
        *//*  final SingleChatModule message = (SingleChatModule) chatMessageList.get(position);
        sd = new SessionManagement(context);
        // Log.e("SELLELE",selected_usersList+"");
        if (selected_usersList.contains(chatMessageList.get(position))) {
            viewHolder.mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.grey_light));
        } else {
            viewHolder.mainLinearLayout.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }        // if message is mine then align to right
        if (message.getSenderId().equalsIgnoreCase(sd.getKeyId())) {

            //   if (message.getResponse().equalsIgnoreCase("All")) {
        *//*    if (message.getIsread().equalsIgnoreCase("true") && message.getDeliver().equalsIgnoreCase("true")) {
                viewHolder.tickMarkFirstOut.setImageResource(R.drawable.viewed);
                viewHolder.tickMarkFirstOutImage.setImageResource(R.drawable.viewed);
                viewHolder.tickMarkFirstOutVideo.setImageResource(R.drawable.viewed);
                viewHolder.tickMarkFirstOutEmoji.setImageResource(R.drawable.viewed);
                viewHolder.tickMarkFirstOutPDF.setImageResource(R.drawable.viewed);
                viewHolder.tickMarkContactOut.setImageResource(R.drawable.viewed);
                viewHolder.tickMarkLocation.setImageResource(R.drawable.viewed);
                viewHolder.tickMarkFirstOutAudio.setImageResource(R.drawable.viewed);
                viewHolder.reply_tickMarkFirstOut.setImageResource(R.drawable.viewed);
            } else if (message.getIsread().equalsIgnoreCase("false") && message.getDeliver().equalsIgnoreCase("true")) {
                viewHolder.tickMarkFirstOut.setImageResource(R.drawable.delivered);
                viewHolder.tickMarkFirstOutImage.setImageResource(R.drawable.delivered);
                viewHolder.tickMarkFirstOutVideo.setImageResource(R.drawable.delivered);
                viewHolder.tickMarkFirstOutEmoji.setImageResource(R.drawable.delivered);
                viewHolder.tickMarkFirstOutPDF.setImageResource(R.drawable.delivered);
                viewHolder.tickMarkContactOut.setImageResource(R.drawable.delivered);
                viewHolder.tickMarkLocation.setImageResource(R.drawable.delivered);
                viewHolder.tickMarkFirstOutAudio.setImageResource(R.drawable.delivered);
                viewHolder.reply_tickMarkFirstOut.setImageResource(R.drawable.delivered);
            } else if (message.getIsread().equalsIgnoreCase("false") && message.getDeliver().equalsIgnoreCase("false")) {
                viewHolder.tickMarkFirstOut.setImageResource(R.drawable.uploaded);
                viewHolder.tickMarkFirstOutImage.setImageResource(R.drawable.uploaded);
                viewHolder.tickMarkFirstOutVideo.setImageResource(R.drawable.uploaded);
                viewHolder.tickMarkFirstOutEmoji.setImageResource(R.drawable.uploaded);
                viewHolder.tickMarkContactOut.setImageResource(R.drawable.uploaded);
                viewHolder.tickMarkFirstOutPDF.setImageResource(R.drawable.uploaded);
                viewHolder.tickMarkLocation.setImageResource(R.drawable.uploaded);
                viewHolder.tickMarkFirstOutAudio.setImageResource(R.drawable.uploaded);
                viewHolder.reply_tickMarkFirstOut.setImageResource(R.drawable.uploaded);
            }
*//*
            *//*} else if (message.getResponse().equalsIgnoreCase("read")) {
                viewHolder.tickMarkFirstOut.setImageResource(R.drawable.viewed);
                viewHolder.tickMarkFirstOutImage.setImageResource(R.drawable.viewed);
                viewHolder.tickMarkFirstOutEmoji.setImageResource(R.drawable.viewed);
                viewHolder.tickMarkFirstOutVideo.setImageResource(R.drawable.viewed);
                viewHolder.tickMarkFirstOutPDF.setImageResource(R.drawable.viewed);
                viewHolder.tickMarkContactOut.setImageResource(R.drawable.viewed);
                viewHolder.tickMarkLocation.setImageResource(R.drawable.viewed);
                viewHolder.tickMarkFirstOutAudio.setImageResource(R.drawable.viewed);
                viewHolder.reply_tickMarkFirstOut.setImageResource(R.drawable.viewed);
            } else if (message.getResponse().equalsIgnoreCase("delivered")) {
                viewHolder.tickMarkFirstOut.setImageResource(R.drawable.delivered);
                viewHolder.tickMarkFirstOutImage.setImageResource(R.drawable.delivered);
                viewHolder.tickMarkFirstOutVideo.setImageResource(R.drawable.delivered);
                viewHolder.tickMarkFirstOutEmoji.setImageResource(R.drawable.delivered);
                viewHolder.tickMarkFirstOutPDF.setImageResource(R.drawable.delivered);
                viewHolder.tickMarkContactOut.setImageResource(R.drawable.delivered);
                viewHolder.tickMarkLocation.setImageResource(R.drawable.delivered);
                viewHolder.tickMarkFirstOutAudio.setImageResource(R.drawable.delivered);
                viewHolder.reply_tickMarkFirstOut.setImageResource(R.drawable.delivered);
            } else if (message.getResponse().equalsIgnoreCase("NA")) {
                viewHolder.tickMarkFirstOut.setImageResource(R.drawable.uploaded);
                viewHolder.tickMarkFirstOutImage.setImageResource(R.drawable.uploaded);
                viewHolder.tickMarkFirstOutVideo.setImageResource(R.drawable.uploaded);
                viewHolder.tickMarkFirstOutEmoji.setImageResource(R.drawable.uploaded);
                viewHolder.tickMarkFirstOutPDF.setImageResource(R.drawable.uploaded);
                viewHolder.tickMarkContactOut.setImageResource(R.drawable.uploaded);
                viewHolder.tickMarkLocation.setImageResource(R.drawable.uploaded);
                viewHolder.tickMarkFirstOutAudio.setImageResource(R.drawable.uploaded );
                viewHolder.reply_tickMarkFirstOut.setImageResource(R.drawable.uploaded );
            }*//*

            if (message.getDate().equalsIgnoreCase("")) {
                viewHolder.relativeDate.setVisibility(View.GONE);
                viewHolder.dateTextView.setText(message.getDate());
            } else {
                viewHolder.relativeDate.setVisibility(View.VISIBLE);
                viewHolder.dateTextView.setText(message.getDate());
            }

            switch (message.getChatType()) {
             *//*   case "audio":

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.VISIBLE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_time.setText(message.getTime());
                    switch (message.getList_position()){
                        case 0:
                            viewHolder.outgoing_image_view_audio_retry.setVisibility(View.VISIBLE);
                            viewHolder.outgoing_image_view_icon_audio.setVisibility(View.GONE);
                            viewHolder.outgoing_image_view_audio_pb.setVisibility(View.GONE);
                            break;
                        case 1:
                            viewHolder.outgoing_image_view_icon_audio.setVisibility(View.VISIBLE);
                            viewHolder.outgoing_image_view_audio_pb.setVisibility(View.GONE);
                            viewHolder.outgoing_image_view_audio_retry.setVisibility(View.GONE);
                            break;
                        case 2:
                            viewHolder.outgoing_image_view_icon_audio.setVisibility(View.GONE);
                            viewHolder.outgoing_image_view_audio_pb.setVisibility(View.VISIBLE);
                            viewHolder.outgoing_image_view_audio_retry.setVisibility(View.GONE);
                            break;
                    }

                    viewHolder.outgoing_image_view_audio_retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (connectionDetector.isConnectingToInternet()){
                                chatMessageList.get(position).setList_position(2);
                                notifyDataSetChanged();
                                viewHolder.outgoing_image_view_icon_audio.setVisibility(View.GONE);
                                viewHolder.outgoing_image_view_audio_pb.setVisibility(View.VISIBLE);
                                viewHolder.outgoing_image_view_audio_retry.setVisibility(View.GONE);
                                Intent intent= new Intent(context, SendSingleMessageService.class);
                                intent.putExtra("mid",message.getMid());
                                context.startService(intent);
                            }else{
                                Toast.makeText(context, "Please turn on your internet!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    if (message.isSelect()) {
                        viewHolder.outgoing_image_view_audio.setVisibility(View.GONE);
                        viewHolder.incoming_image_view_audio_stop.setVisibility(View.GONE);
                        viewHolder.incoming_image_view_audio.setVisibility(View.GONE);
                        viewHolder.outgoing_image_view_audio_stop.setVisibility(View.VISIBLE);

                        if (mediaPlayer != null) {
                            viewHolder.outgoing_seekbar.setProgress(mediaPlayer.getCurrentPosition());
                        }
                    } else if (!message.isSelect()) {
                        viewHolder.outgoing_seekbar.setProgress(0);
                        viewHolder.incoming_image_view_audio.setVisibility(View.GONE);
                        viewHolder.incoming_image_view_audio_stop.setVisibility(View.GONE);
                        viewHolder.outgoing_image_view_audio.setVisibility(View.VISIBLE);
                        viewHolder.outgoing_image_view_audio_stop.setVisibility(View.GONE);
                    }

                    viewHolder.outgoing_image_view_audio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            for (int i = 0;i < chatMessageList.size();i++){
                                if (chatMessageList.get(i).isSelect == true) {
                                    chatMessageList.get(i).setSelect(false);
                                    notifyItemChanged(i);
                                    break;
                                }
                            }
                            message.setSelect(true);


                            playSong(message,message.getMessage(), viewHolder.outgoing_seekbar, position, viewHolder.outgoing_image_view_audio, viewHolder.outgoing_image_view_audio_stop, viewHolder.incoming_image_view_audio_stop, viewHolder.incoming_image_view_audio);
                            if (message.isSelect()) {
                                viewHolder.outgoing_image_view_audio.setVisibility(View.GONE);
                                viewHolder.outgoing_image_view_audio_stop.setVisibility(View.VISIBLE);
                                viewHolder.incoming_image_view_audio_stop.setVisibility(View.GONE);
                                viewHolder.incoming_image_view_audio.setVisibility(View.GONE);
                            }
                            // Toast.makeText(context, "PLAY" + position, Toast.LENGTH_SHORT).show();
                        }
                    });
                    viewHolder.outgoing_image_view_audio_stop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            message.setSelect(false);
                            //  notifyDataSetChanged();

                            viewHolder.outgoing_seekbar.setProgress(0);
                            try {
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                mediaPlayer = null;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //   Toast.makeText(context, "STOPs" + position, Toast.LENGTH_SHORT).show();
                            if (!message.isSelect()) {

                                viewHolder.outgoing_image_view_audio.setVisibility(View.VISIBLE);
                                viewHolder.outgoing_image_view_audio_stop.setVisibility(View.GONE);
                                viewHolder.incoming_image_view_audio_stop.setVisibility(View.GONE);
                                viewHolder.incoming_image_view_audio.setVisibility(View.GONE);
                            }
                        }
                    });
                    break;
                case "video":

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.VISIBLE);
                    *//**//*Glide.with(context)
                            .load(message.getMessage())
                            .into(viewHolder.outgoing_video_IV);*//**//*
                    *//**//*if (mode.equals("offline")){

                    }else{
                        Glide.with(context)
                                .load(message.getMessage())
                                .into(viewHolder.outgoing_video_IV);
                    }*//**//*
                    switch (message.getList_position()){
                        case 0:
                            viewHolder.outgoing_video_progress_bar.setVisibility(View.GONE);
                            viewHolder.btnVideoUpload.setVisibility(View.VISIBLE);
                            viewHolder.outgoing_play_btn.setVisibility(View.GONE);
                            break;
                        case 1:
                            viewHolder.btnVideoUpload.setVisibility(View.GONE);
                            viewHolder.outgoing_play_btn.setVisibility(View.VISIBLE);
                            viewHolder.outgoing_video_progress_bar.setVisibility(View.GONE);
                            break;
                        case 2:
                            viewHolder.btnVideoUpload.setVisibility(View.GONE);
                            viewHolder.outgoing_play_btn.setVisibility(View.GONE);
                            viewHolder.outgoing_video_progress_bar.setVisibility(View.VISIBLE);
                            break;
                    }
                    Glide.with(context)
                            .load(new File(message.getMessage()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .dontAnimate()
                            .into(viewHolder.outgoing_video_IV);
                    viewHolder.outgoing_video_time.setText(message.getTime());
                    viewHolder.outgoing_play_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            *//**//*if (message.getUploading().equals("true")){
                                Toast.makeText(context, "Video is not uploaded yet!", Toast.LENGTH_SHORT).show();
                            }else{*//**//*
                            Intent i = new Intent(context, VideoAvtivity.class);
                            i.putExtra("video", message.getMessage());
                            context.startActivity(i);
                            //}
                        }
                    });

                    viewHolder.outgoing_video_progress_bar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try{
                                localRecDownload.cancelDownload(Integer.parseInt(message.getHeading()));
                            }catch (Exception e){

                            }
                        }
                    });
                    viewHolder.btnVideoUpload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (connectionDetector.isConnectingToInternet()){
                                chatMessageList.get(position).setList_position(2);
                                notifyDataSetChanged();
                                viewHolder.btnVideoUpload.setVisibility(View.GONE);
                                viewHolder.outgoing_play_btn.setVisibility(View.GONE);
                                viewHolder.outgoing_video_progress_bar.setVisibility(View.VISIBLE);
                                Intent intent=  new Intent(context,SendSingleMessageService.class);
                                intent.putExtra("mid",message.getMid());
                                context.startService(intent);
                            }else{
                                Toast.makeText(context, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;

                case "emoji":

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.VISIBLE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    viewHolder.outgoing_emoji_time.setText(message.getTime());
                    *//**//*Resources res =  context.getResources();*//**//*
                    File imgFile=new File(localDBHelper.emojiPath(message.getMessage()));
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    *//**//*int resID = res.getIdentifier(mDrawableName , "drawable", context.getPackageName());
                    Drawable drawable = res.getDrawable(resID );*//**//*
                    viewHolder.outgoing_emoji_image.setImageBitmap(myBitmap);
                    break;
                case "share":

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);

                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);

                    viewHolder.outgoing_linear_contact.setVisibility(View.VISIBLE);
                    viewHolder.outgoing_contact_time.setText(message.getTime());
                    String str[] = message.getMessage().split(",");
                    viewHolder.outgoing_contact_number.setText(str[0]);
                    viewHolder.outgoing_contact_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(context, ProfileActivity.class);
                            i.putExtra("name", str[0]);
                            i.putExtra("rid", str[2]);
                            i.putExtra("did", str[3]);
                            context.startActivity(i);
                        }
                    });
                    break;*//*
                case "msg":

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.outgoingTextImage.setText(message.getMessage());
                    viewHolder.outgoing_text_time.setText(message.getTime());
                    viewHolder.layout.setVisibility(View.VISIBLE);

                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);

                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);

                    break; *//* case "reply":
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);

                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);

                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);

                    viewHolder.out_reply_cab.setVisibility(View.VISIBLE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.outgoing_reply_text_time.setText(message.getTime());
                    String strRply[]=message.getMessage().split("%@,%");
                    try{
                        switch (strRply[1]){
                            case "img":

                                //viewHolder.incoming_reply_image.se
                                viewHolder.outgoing_reply_get_text.setText("Image");
                                viewHolder.outgoing_reply_text.setText(strRply[2]);

                                Glide.with(context)
                                        .load(new File(strRply[0]))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .centerCrop()
                                        .dontAnimate()
                                        .into(viewHolder.outgoing_reply_image);
                                break;
                            case "msg":
                                viewHolder.outgoing_reply_get_text.setText(strRply[0]);
                                viewHolder.outgoing_reply_text.setText(strRply[2]);
                                viewHolder.outgoing_reply_image.setImageResource(R.drawable.agent);

                                break;
                            case "pdf":
                                viewHolder.outgoing_reply_image.setImageResource(R.drawable.pdf);

                                viewHolder.outgoing_reply_get_text.setText("Document");
                                viewHolder.outgoing_reply_text.setText(strRply[2]);
                                break;
                            case "emoji":
                                // viewHolder.incoming_reply_image.
                                viewHolder.outgoing_reply_get_text.setText("Emoji");
                                viewHolder.outgoing_reply_text.setText(strRply[2]);
                                File imgFile123=new File(localDBHelper.emojiPath(strRply[0]));

                                Bitmap myBitmap123 = BitmapFactory.decodeFile(imgFile123.getAbsolutePath());
                    *//**//*int resID = res.getIdentifier(mDrawableName , "drawable", context.getPackageName());
                    Drawable drawable = res.getDrawable(resID );*//**//*
                                viewHolder.outgoing_reply_image.setImageResource(R.drawable.white_back);
                                viewHolder.outgoing_reply_image.setImageBitmap(myBitmap123);

                                break;
                            case "audio":
                                viewHolder.outgoing_reply_get_text.setText("Audio");
                                viewHolder.outgoing_reply_text.setText(strRply[2]);
                                viewHolder.outgoing_reply_image.setImageResource(R.drawable.audio_file);

                                break;
                            case "share":
                                viewHolder.outgoing_reply_get_text.setText("Contact");
                                viewHolder.outgoing_reply_image.setImageResource(R.drawable.agent);

                                viewHolder.outgoing_reply_text.setText(strRply[2]);
                                break;
                            case "location":
                                String l []=strRply[0].split(",@,");
                                viewHolder.outgoing_reply_get_text.setText(l[0]);
                                viewHolder.outgoing_reply_text.setText(strRply[2]);
                                viewHolder.outgoing_reply_image.setImageResource(R.drawable.fake_mapmin);

                                break;
                            case "video":

                                Glide.with(context)
                                        .load(new File(strRply[0]))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .centerCrop()
                                        .dontAnimate()
                                        .into(viewHolder.outgoing_reply_image);
                                viewHolder.outgoing_reply_get_text.setText("Video");
                                viewHolder.outgoing_reply_text.setText(strRply[2]);
                                break;
                            case "reply":
                                String r[]=strRply[0].split("%@,%");
                                viewHolder.outgoing_reply_get_text.setText(r[0]);
                                viewHolder.outgoing_reply_image.setImageResource(R.drawable.agent);

                                viewHolder.outgoing_reply_text.setText(strRply[2]);
                                break;
                            default:
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case "img":

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.VISIBLE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);

                    *//**//*Glide.with(context)
                            .load(message.getMessage())
                            .into(viewHolder.outgoing_image_IV);*//**//*
                    switch (message.getList_position()){
                        case 0:
                            viewHolder.btnImageUpload.setVisibility(View.VISIBLE);
                            viewHolder.outgoing_progress_bar.setVisibility(View.GONE);
                            break;
                        case 1:
                            viewHolder.btnImageUpload.setVisibility(View.GONE);
                            viewHolder.outgoing_progress_bar.setVisibility(View.GONE);
                            break;
                        case 2:
                            viewHolder.btnImageUpload.setVisibility(View.GONE);
                            viewHolder.outgoing_progress_bar.setVisibility(View.VISIBLE);
                            break;
                    }
                    Glide.with(context)
                            .load(new File(message.getMessage()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .dontAnimate()
                            .into(viewHolder.outgoing_image_IV);
                    *//**//*if (mode.equals("offline")){
                        Glide.with(context)
                                .load(new File(message.getMessage()))
                                .into(viewHolder.outgoing_image_IV);

                    }else{

                        Glide.with(context)
                                .load(message.getMessage())
                                .into(viewHolder.outgoing_image_IV);
                    }*//**//*

                    viewHolder.outgoing_image_time.setText(message.getTime());
                    viewHolder.outgoing_image_IV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, FullScreenViewActivity.class);
                            i.putExtra("id", message.getMid());
                            i.putExtra("rid", message.getRecieverName());
                            context.startActivityForResult(i,888);
                        }
                    });

                    viewHolder.btnImageUpload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (connectionDetector.isConnectingToInternet()){
                                chatMessageList.get(position).setList_position(2);
                                notifyDataSetChanged();
                                viewHolder.btnImageUpload.setVisibility(View.GONE);
                                viewHolder.outgoing_progress_bar.setVisibility(View.VISIBLE);
                                Intent intent=new Intent(context,SendSingleMessageService.class);
                                intent.putExtra("mid", message.getMid());
                                context.startService(intent);
                            }else{
                                Toast.makeText(context, "Please check your interet connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                case "pdf":

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.VISIBLE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    String str1 = message.getDocName();
                    if (message.getUploading().equals("true")){
                        viewHolder.outgoing_doc_icon_pb.setVisibility(View.VISIBLE);
                        viewHolder.outgoing_doc_icon.setVisibility(View.GONE);
                    }else{
                        viewHolder.outgoing_doc_icon_pb.setVisibility(View.GONE);
                        viewHolder.outgoing_doc_icon.setVisibility(View.VISIBLE);
                    }

                    switch (message.getList_position()){
                        case 0:
                            viewHolder.outgoing_doc_icon_pb.setVisibility(View.GONE);
                            viewHolder.outgoing_doc_icon.setVisibility(View.GONE);
                            viewHolder.outgoing_doc_icon_retry.setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            viewHolder.outgoing_doc_icon_retry.setVisibility(View.GONE);
                            viewHolder.outgoing_doc_icon_pb.setVisibility(View.GONE);
                            viewHolder.outgoing_doc_icon.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            viewHolder.outgoing_doc_icon_retry.setVisibility(View.GONE);
                            viewHolder.outgoing_doc_icon_pb.setVisibility(View.VISIBLE);
                            viewHolder.outgoing_doc_icon.setVisibility(View.GONE);
                            break;
                    }

                    if (str1.toLowerCase().endsWith(".xls") || str1.toLowerCase().endsWith(".xlsb") || str1.toLowerCase().endsWith(".xlsm") || str1.toLowerCase().endsWith(".xlsx") || str1.toLowerCase().endsWith(".xlt") || str1.toLowerCase().endsWith(".xltx")) {
                        viewHolder.txtoutgoing_pdf.setText(message.getDocName());
                        viewHolder.outgoing_doc_icon.setImageResource(R.drawable.excel);
                    } else if (str1.toLowerCase().endsWith(".pdf")) {
                        viewHolder.txtoutgoing_pdf.setText(message.getDocName());
                        viewHolder.outgoing_doc_icon.setImageResource(R.drawable.pdf);
                    } else {
                        viewHolder.txtoutgoing_pdf.setText(message.getDocName());
                        viewHolder.outgoing_doc_icon.setImageResource(R.drawable.word);
                    }
                    viewHolder.outgoing_pdf_time.setText(message.getTime());
                    viewHolder.out_lineat_bubble.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (message.getList_position() == 1){
                                *//**//*Content is ready to display*//**//*
                                if (message.getExtension().equals(".pdf")){

                                    Intent intent= new Intent(context, PdfRender.class);
                                    intent.putExtra("keyPath",message.getMessage());
                                    intent.putExtra("keyName",message.getData().replaceAll(
                                            Confiq.DOCUMNMENT_URL_AWS,""));
                                    context.startActivity(intent);
                                }else{

                                    if (connectionDetector.isConnectingToInternet() ){

                                        if(message.getList_position()==1){
                                            Intent intent= new Intent(context, DocViewr.class);
                                            intent.putExtra("pdf","no");
                                            intent.putExtra("url",message.getData());
                                            context.startActivity(intent);
                                        }else{
                                            Toast.makeText(context, "File is not uploaded yet!", Toast.LENGTH_SHORT).show();
                                        }


                                    }else{
                                        Permission.checkReadExternalStoragePermission(context);
                                        File file = new File(message.getMessage());
                                        Intent target = new Intent(Intent.ACTION_VIEW);
                                        target.setDataAndType(Uri.fromFile(file),"application/pdf");
                                        target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        Intent intent = Intent.createChooser(target, "Open File");
                                        try {
                                            context.startActivity(intent);
                                        } catch (ActivityNotFoundException e) {
                                            // Instruct the user to install a PDF reader here, or something
                                        }

                                    }
                                }
                            }else{
                                Toast.makeText(context, "Please download the file!", Toast.LENGTH_SHORT).show();
                            }
                          *//**//*String pad= message.getMessage().replace(Confiq.RETURN_IMAGE_URL,"");
                            Utils.downloadFile(context,  message.getMessage(), pad);*//**//*
                        }
                    });

                    viewHolder.outgoing_doc_icon_retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (connectionDetector.isConnectingToInternet()){
                                chatMessageList.get(position).setList_position(2);
                                notifyDataSetChanged();
                                viewHolder.outgoing_doc_icon_retry.setVisibility(View.GONE);
                                viewHolder.outgoing_doc_icon_pb.setVisibility(View.VISIBLE);
                                viewHolder.outgoing_doc_icon.setVisibility(View.GONE);
                                Intent intent= new Intent(context, SendSingleMessageService.class);
                                intent.putExtra("mid",message.getMid());
                                context.startService(intent);
                            }else{
                                Toast.makeText(context, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                case "location":
                    try {

                        viewHolder.out_reply_cab.setVisibility(View.GONE);
                        viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                        viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                        viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                        viewHolder.outgoing_location_relative.setVisibility(View.VISIBLE);
                        viewHolder.incoming_location_relative.setVisibility(View.GONE);
                        viewHolder.out_image.setVisibility(View.GONE);
                        viewHolder.out_video_cab.setVisibility(View.GONE);
                        viewHolder.incoming_image.setVisibility(View.GONE);
                        viewHolder.layout.setVisibility(View.GONE);
                        viewHolder.incoming_pdf.setVisibility(View.GONE);

                        viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                        viewHolder.incoming_video_cab.setVisibility(View.GONE);
                        viewHolder.out_pdf.setVisibility(View.GONE);
                        viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                        viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                        viewHolder.parent_layout.setVisibility(View.GONE);

                        viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                        viewHolder.outgoing_location_time.setText(message.getTime());
                        String strLocation[] = message.getMessage().split(",@,");
                        viewHolder.outgoing_location_address.setText(strLocation[2]);

                        viewHolder.outgong_linear_click.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean fine_location = Permission.checkPermisionForACCESS_FINE_LOCATION(context);

                                if (fine_location){
                                    chatActivity.GPSStatus();
                                    if(chatActivity.GpsStatus == true)
                                    {
                                        String strL[] = message.getMessage().split(",@,");
                                        Intent i = new Intent(context, MapBoxActivity.class);
                                        i.putExtra("lat", Double.valueOf(strL[0]));
                                        i.putExtra("lang", Double.valueOf(strL[1]));
                                        i.putExtra("title", strL[2]);
                                        context.startActivity(i);
                                    }else
                                    {

                                        Intent  intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        context.startActivity(intent1);

                                    }

                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    break;*//*
                default:

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    break;
            }
        }
        // If not mine then align to left
        else {
            if (message.getDate().equalsIgnoreCase("")) {
                viewHolder.relativeDate.setVisibility(View.GONE);
                viewHolder.dateTextView.setText(message.getDate());
            } else {
                viewHolder.relativeDate.setVisibility(View.VISIBLE);
                viewHolder.dateTextView.setText(message.getDate());
            }
            switch (message.getChatType()) {
               *//* case "img":

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.VISIBLE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);

                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);

                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    if (new File(message.getMessage()).exists()){
                        message.setList_position(1);
                    }
                    Uri source = Uri.parse(message.getParent());
                    switch (message.getList_position()){
                        case 0:
                            viewHolder.btnImageDownload.setVisibility(View.VISIBLE);
                            viewHolder.incoming_progress_bar.setVisibility(View.GONE);
                            Glide.with(context)
                                    .load(source)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .centerCrop()
                                    .dontAnimate()
                                    .into(viewHolder.incoming_image_IV);
                            break;
                        case 1:
                            Glide.with(context)
                                    .load(new File(message.getMessage()))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .centerCrop()
                                    .dontAnimate()
                                    .into(viewHolder.incoming_image_IV);
                            viewHolder.btnImageDownload.setVisibility(View.GONE);
                            viewHolder.incoming_progress_bar.setVisibility(View.GONE);
                            break;
                        case 2:
                            Glide.with(context)
                                    .load(source)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .centerCrop()
                                    .dontAnimate()
                                    .into(viewHolder.incoming_image_IV);
                            viewHolder.btnImageDownload.setVisibility(View.GONE);
                            viewHolder.incoming_progress_bar.setVisibility(View.VISIBLE);
                            break;
                    }

                    viewHolder.incoming_progress_bar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try{
                                localRecDownload.cancelDownload(Integer.parseInt(message.getHeading()));
                            }catch (Exception e){

                            }
                            viewHolder.incoming_progress_bar.setVisibility(View.GONE);
                            viewHolder.btnImageDownload.setVisibility(View.VISIBLE);
                        }
                    });


                    viewHolder.incoming_image_time.setText(message.getTime());
                    viewHolder.incoming_image_IV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (message.getList_position()==1){

                                Intent i = new Intent(context, FullScreenViewActivity.class);
                                i.putExtra("rid", message.getSendName());
                                i.putExtra("id", message.getMid());
                                context.startActivityForResult(i,88);
                            }else{
                                Toast.makeText(context, "Image is not downloaded yet", Toast.LENGTH_SHORT).show();
                            }
                            //  i.putStringArrayListExtra("image", imgage);

                        }
                    });
                    viewHolder.btnImageDownload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (connectionDetector.isConnectingToInternet()){
                                viewHolder.btnImageDownload.setVisibility(View.GONE);
                                viewHolder.incoming_progress_bar.setVisibility(View.VISIBLE);
                                *//**//*chatMessageList.get(position).setList_position(2);
                                notifyDataSetChanged();*//**//*
                                localRecDownload.localRecDownloadMethod(message.getData(), LocalFileManager.image_folder_name,
                                        message.getType(),message.getMid(),message.getRecieverName(),context, "");
                                //downloadMethod(viewHolder.incoming_progress_bar,viewHolder.btnImageDownload,position,message.getData(),LocalFileManager.image_folder_name);
                            }else{
                                Toast.makeText(context, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                case "pdf":

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);

                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);

                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.VISIBLE);
                    try{
                        if (new File(message.getMessage()).exists()){
                            message.setList_position(1);
                        }

                    }catch (Exception e){
                        Log.d("Background_",e.toString());
                    }

                    switch (message.getList_position()){
                        case 0:
                            viewHolder.incoming_doc_icon_pb.setVisibility(View.GONE);
                            viewHolder.incoming_doc_icon_download.setVisibility(View.VISIBLE);
                            viewHolder.incoming_doc_icon.setVisibility(View.GONE);
                            break;
                        case 1:
                            viewHolder.incoming_doc_icon_pb.setVisibility(View.GONE);
                            viewHolder.incoming_doc_icon_download.setVisibility(View.GONE);
                            viewHolder.incoming_doc_icon.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            viewHolder.incoming_doc_icon_pb.setVisibility(View.VISIBLE);
                            viewHolder.incoming_doc_icon_download.setVisibility(View.GONE);
                            viewHolder.incoming_doc_icon.setVisibility(View.GONE);
                            break;
                    }
                    String str = message.getData().replace(Confiq.RETURN_IMAGE_URL, "");
                    if (str.toLowerCase().endsWith(".xls") || str.toLowerCase().endsWith(".xlsb") || str.toLowerCase().endsWith(".xlsm") || str.toLowerCase().endsWith(".xlsx") || str.toLowerCase().endsWith(".xlt") || str.toLowerCase().endsWith(".xltx")) {
                        viewHolder.txtincomming_pdf.setText(message.getDocName());
                        viewHolder.incoming_doc_icon.setImageResource(R.drawable.excel);
                    } else if (str.toLowerCase().endsWith(".pdf")) {
                        viewHolder.txtincomming_pdf.setText(message.getDocName());
                        viewHolder.incoming_doc_icon.setImageResource(R.drawable.pdf);
                    } else {
                        viewHolder.txtincomming_pdf.setText(message.getDocName());
                        viewHolder.incoming_doc_icon.setImageResource(R.drawable.word);
                    }
                    viewHolder.incoming_pdf_time.setText(message.getTime());

                    viewHolder.incoming_doc_icon_pb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewHolder.incoming_doc_icon_pb.setVisibility(View.GONE);
                            viewHolder.incoming_doc_icon_download.setVisibility(View.VISIBLE);
                            viewHolder.incoming_doc_icon.setVisibility(View.GONE);
                        }
                    });

                    viewHolder.incoming_linear_click.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          *//**//*  Intent i = new Intent(context, DocViewr.class);
                            i.putExtra("pdf", message.getMessage());
                            context.startActivity(i);*//**//*
                            if (message.getList_position() == 1){
                                *//**//*Content is ready to display*//**//*
                                if (message.getExtension().equals(".pdf")){

                                    Intent intent= new Intent(context, PdfRender.class);
                                    intent.putExtra("keyPath",message.getMessage());
                                    intent.putExtra("keyName",message.getDocName());
                                    context.startActivity(intent);
                                }else{

                                    if (connectionDetector.isConnectingToInternet()){

                                        Intent intent= new Intent(context, DocViewr.class);
                                        intent.putExtra("pdf","no");
                                        intent.putExtra("url",message.getData());
                                        context.startActivity(intent);


                                    }else{
                                        Permission.checkReadExternalStoragePermission(context);
                                        File file = new File(message.getMessage());
                                        Intent target = new Intent(Intent.ACTION_VIEW);
                                        target.setDataAndType(Uri.fromFile(file),"application/pdf");
                                        target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        Intent intent = Intent.createChooser(target, "Open File");
                                        try {
                                            context.startActivity(intent);
                                        } catch (ActivityNotFoundException e) {
                                            // Instruct the user to install a PDF reader here, or something
                                        }

                                    }
                                }
                            }else{
                                Toast.makeText(context, "Please download the file!", Toast.LENGTH_SHORT).show();
                            }
                            *//**//*String pad= message.getMessage().replace(Confiq.RETURN_IMAGE_URL,"");
                            Utils.downloadFile(context,  message.getMessage(), pad);*//**//*
                        }
                    });

                    viewHolder.incoming_doc_icon_download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (connectionDetector.isConnectingToInternet()){

                                viewHolder.incoming_doc_icon_pb.setVisibility(View.VISIBLE);
                                viewHolder.incoming_doc_icon_download.setVisibility(View.GONE);
                                localRecDownload.localRecDownloadMethod(message.getMessage(),LocalFileManager.docs_folder_name,message.getType(),message.getMid()
                                        ,message.getRecieverName(),context,message.getDocName());
                            }else {
                                Toast.makeText(context, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                case "video":

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.VISIBLE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.incoming_video_time.setText(message.getTime());

                    if (new File(message.getMessage()).exists()){
                        message.setList_position(1);
                    }
                    *//**//*AFter thumb add pass getParent here*//**//*
                    Uri source_video = Uri.parse(message.getParent());
                    switch (message.getList_position()){
                        case 0:
                            viewHolder.incoming_video_progress_bar.setVisibility(View.GONE);
                            viewHolder.incoming_play_btn.setVisibility(View.GONE);
                            viewHolder.btnVideoDownload.setVisibility(View.VISIBLE);
                            Glide.with(context)
                                    .load(source_video)
                                    .dontAnimate()
                                    .into(viewHolder.incoming_video_IV);
                            break;
                        case 1:
                            Glide.with(context)
                                    .load(new File(message.getMessage()))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .centerCrop()
                                    .dontAnimate()
                                    .into(viewHolder.incoming_video_IV);
                            viewHolder.incoming_play_btn.setVisibility(View.VISIBLE);
                            viewHolder.incoming_video_progress_bar.setVisibility(View.GONE);
                            viewHolder.btnVideoDownload.setVisibility(View.GONE);
                            break;
                        case 2:
                            Glide.with(context)
                                    .load(source_video)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .centerCrop()
                                    .dontAnimate()
                                    .into(viewHolder.incoming_video_IV);
                            viewHolder.incoming_video_progress_bar.setVisibility(View.VISIBLE);
                            viewHolder.incoming_play_btn.setVisibility(View.GONE);
                            viewHolder.btnVideoDownload.setVisibility(View.GONE);
                            break;
                    }

                    viewHolder.incoming_video_progress_bar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewHolder.incoming_video_progress_bar.setVisibility(View.GONE);
                            viewHolder.btnVideoDownload.setVisibility(View.VISIBLE);
                        }
                    });

                    viewHolder.incoming_play_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (message.getList_position()==1){
                                Intent i = new Intent(context, VideoAvtivity.class);
                                i.putExtra("video", message.getMessage());
                                context.startActivity(i);
                            }else{
                                Toast.makeText(context, "Video is not downloaded yet!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    viewHolder.btnVideoDownload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (connectionDetector.isConnectingToInternet()){
                                chatMessageList.get(position).setList_position(2);
                                notifyDataSetChanged();
                                viewHolder.incoming_video_progress_bar.setVisibility(View.VISIBLE);
                                viewHolder.incoming_play_btn.setVisibility(View.GONE);
                                viewHolder.btnVideoDownload.setVisibility(View.GONE);
                                localRecDownload.localRecDownloadMethod(message.getData(),LocalFileManager.video_folder_name,
                                        message.getType(),message.getMid(),message.getRecieverName(),context,"");
                            }else{
                                Toast.makeText(context, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                case "audio":

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.VISIBLE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    viewHolder.incoming_audio_time.setText(message.getTime());

                    if (message.isSelect()) {
                        viewHolder.outgoing_image_view_audio.setVisibility(View.GONE);
                        viewHolder.incoming_image_view_audio_stop.setVisibility(View.VISIBLE);
                        viewHolder.incoming_image_view_audio.setVisibility(View.GONE);
                        viewHolder.outgoing_image_view_audio_stop.setVisibility(View.GONE);

                        if (mediaPlayer != null) {
                            viewHolder.incoming_seekbar.setProgress(mediaPlayer.getCurrentPosition());
                        }
                    } else if (!message.isSelect()) {

                        viewHolder.incoming_seekbar.setProgress(0);
                        viewHolder.incoming_image_view_audio.setVisibility(View.VISIBLE);
                        viewHolder.incoming_image_view_audio_stop.setVisibility(View.GONE);
                        viewHolder.outgoing_image_view_audio.setVisibility(View.GONE);
                        viewHolder.outgoing_image_view_audio_stop.setVisibility(View.GONE);
                    }
                    if (new File(message.getMessage()).exists()){
                        message.setList_position(1);
                    }
                    switch (message.getList_position()){
                        case 0:
                            viewHolder.incoming_image_view_icon_audio.setVisibility(View.GONE);
                            //   viewHolder.incoming_image_view_audio.setVisibility(View.GONE);
                            viewHolder.incoming_audio_icon_download.setVisibility(View.VISIBLE);
                            viewHolder.incoming_audio_icon_pb.setVisibility(View.GONE);
                            break;
                        case 1:
                            viewHolder.incoming_image_view_icon_audio.setVisibility(View.VISIBLE);
                            viewHolder.incoming_audio_icon_download.setVisibility(View.GONE);
                            viewHolder.incoming_audio_icon_pb.setVisibility(View.GONE);
                            //    viewHolder.incoming_image_view_audio.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            viewHolder.incoming_image_view_icon_audio.setVisibility(View.GONE);
                            viewHolder.incoming_audio_icon_download.setVisibility(View.GONE);
                            viewHolder.incoming_audio_icon_pb.setVisibility(View.VISIBLE);
                            //     viewHolder.incoming_image_view_audio.setVisibility(View.GONE);
                            break;
                    }

                    viewHolder.incoming_audio_icon_pb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewHolder.incoming_image_view_icon_audio.setVisibility(View.GONE);
                            viewHolder.incoming_image_view_audio.setVisibility(View.GONE);
                            viewHolder.incoming_audio_icon_download.setVisibility(View.VISIBLE);
                            viewHolder.incoming_audio_icon_pb.setVisibility(View.GONE);
                        }
                    });
                    viewHolder.incoming_image_view_audio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (message.getList_position()==1){


                                for (int i = 0;i < chatMessageList.size();i++){
                                    if (chatMessageList.get(i).isSelect == true) {
                                        chatMessageList.get(i).setSelect(false);
                                        notifyItemChanged(i);
                                        break;
                                    }
                                }
                                message.setSelect(true);
                                playSong(message,message.getMessage(), viewHolder.incoming_seekbar, position,viewHolder.outgoing_image_view_audio, viewHolder.outgoing_image_view_audio_stop, viewHolder.incoming_image_view_audio_stop, viewHolder.incoming_image_view_audio);
                                if (message.isSelect()) {
                                    viewHolder.outgoing_image_view_audio.setVisibility(View.GONE);
                                    viewHolder.outgoing_image_view_audio_stop.setVisibility(View.GONE);
                                    viewHolder.incoming_image_view_audio_stop.setVisibility(View.VISIBLE);
                                    viewHolder.incoming_image_view_audio.setVisibility(View.GONE);
                                }
                            }else{
                                Toast.makeText(context, "Either file is not downloaded yet or the file is deleted!", Toast.LENGTH_SHORT).show();
                            }

                            // Toast.makeText(context, "PLAY" + position, Toast.LENGTH_SHORT).show();

                        }
                    });
                    viewHolder.incoming_image_view_audio_stop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            message.setSelect(false);
                            //  notifyDataSetChanged();

                            viewHolder.incoming_seekbar.setProgress(0);
                            try {
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                mediaPlayer = null;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //   Toast.makeText(context, "STOPs" + position, Toast.LENGTH_SHORT).show();
                            if (!message.isSelect()) {

                                viewHolder.outgoing_image_view_audio.setVisibility(View.GONE);
                                viewHolder.outgoing_image_view_audio_stop.setVisibility(View.GONE);
                                viewHolder.incoming_image_view_audio_stop.setVisibility(View.GONE);
                                viewHolder.incoming_image_view_audio.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    viewHolder.incoming_audio_icon_download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (connectionDetector.isConnectingToInternet()){
                                chatMessageList.get(position).setList_position(2);
                                notifyDataSetChanged();
                                viewHolder.incoming_audio_icon_download.setVisibility(View.GONE);
                                viewHolder.incoming_audio_icon_pb.setVisibility(View.VISIBLE);
                                viewHolder.incoming_image_view_audio.setVisibility(View.GONE);

                                localRecDownload.localRecDownloadMethod(message.getData(), LocalFileManager.audio_folder_name,
                                        message.getType(),message.getMid(),message.getRecieverName(),context,"");
                            }else{
                                Toast.makeText(context, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                case "emoji":

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_emoji_time.setText(message.getTime());
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);

                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.VISIBLE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    *//**//*Resources res =  context.getResources();*//**//*
                    File imgFile=new File(localDBHelper.emojiPath(message.getMessage()));
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    *//**//*int resID = res.getIdentifier(mDrawableName , "drawable", context.getPackageName());
                    Drawable drawable = res.getDrawable(resID );*//**//*
                    viewHolder.incoming_emoji_image.setImageBitmap(myBitmap);
                    break;
                case "share":

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.incoming_contact_time.setText(message.getTime());
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);

                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_linear_contact.setVisibility(View.VISIBLE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    String[] strData = message.getMessage().split(",");
                    viewHolder.incoming_contact_number.setText(strData[0]);
                    viewHolder.incoming_view_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(context, ProfileActivity.class);
                            i.putExtra("name", strData[0]);
                            i.putExtra("rid", strData[2]);
                            i.putExtra("did", strData[3]);
                            context.startActivity(i);
                        }
                    });
                    break;
                case "location":
                    try {

                        viewHolder.out_reply_cab.setVisibility(View.GONE);
                        viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                        viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                        viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                        viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                        viewHolder.incoming_location_relative.setVisibility(View.VISIBLE);

                        viewHolder.out_image.setVisibility(View.GONE);
                        viewHolder.out_video_cab.setVisibility(View.GONE);
                        viewHolder.incoming_image.setVisibility(View.GONE);
                        viewHolder.layout.setVisibility(View.GONE);
                        viewHolder.incoming_pdf.setVisibility(View.GONE);
                        viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                        viewHolder.incoming_video_cab.setVisibility(View.GONE);
                        viewHolder.out_pdf.setVisibility(View.GONE);
                        viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                        viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                        viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                        viewHolder.parent_layout.setVisibility(View.GONE);

                        viewHolder.incoming_location_time.setText(message.getTime());
                        String strLocation[] = message.getMessage().split(",@,");
                        //String currLocation = "&daddr=" + strLocation[2];
                        viewHolder.incoming_location_address.setText(strLocation[2]);

                        viewHolder.incoming_location_click.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean fine_location = Permission.checkPermisionForACCESS_FINE_LOCATION(context);

                                if (fine_location){

                                    chatActivity.GPSStatus();
                                    if(chatActivity.GpsStatus == true)
                                    {
                                        String strL[] = message.getMessage().split(",@,");

                                        Intent i = new Intent(context, MapBoxActivity.class);
                                        i.putExtra("lat", Double.valueOf(strL[0]));
                                        i.putExtra("lang", Double.valueOf(strL[1]));
                                        i.putExtra("title", strL[2]);
                                        context.startActivity(i);
                                    }else
                                    {

                                        Intent  intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        context.startActivity(intent1);

                                    }

                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;*//*
                case "msg":

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.VISIBLE);
                    viewHolder.incomming_text.setText(message.getMessage());
                    viewHolder.incoming_text_time.setText(message.getTime());
                    break;
*//*
                case "reply":
                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.VISIBLE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    viewHolder.incoming_reply_text_time.setText(message.getTime());
                    String strRply[]=message.getMessage().split("%@,%");
                    try {


                        switch (strRply[1]){
                            case "img":
                                viewHolder.incomin_reply_get_text.setText("Image");
                                viewHolder.incomming_reply_text.setText(strRply[2]);

                                Glide.with(context)
                                        .load(new File(strRply[0]))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .centerCrop()
                                        .dontAnimate()
                                        .into(viewHolder.incoming_reply_image);
                                break;
                            case "msg":
                                viewHolder.incoming_reply_image.setImageResource(R.drawable.agent);
                                viewHolder.incomin_reply_get_text.setText(strRply[0]);
                                viewHolder.incomming_reply_text.setText(strRply[2]);
                                break;
                            case "pdf":
                                viewHolder.incoming_reply_image.setImageResource(R.drawable.pdf);
                                viewHolder.incomin_reply_get_text.setText("Document");
                                viewHolder.incomming_reply_text.setText(strRply[2]);
                                break;
                            case "emoji":
                                // viewHolder.incoming_reply_image.
                                viewHolder.incomin_reply_get_text.setText("Emoji");
                                viewHolder.incomming_reply_text.setText(strRply[2]);
                                File imgFile123=new File(localDBHelper.emojiPath(strRply[0]));

                                Bitmap myBitmap123 = BitmapFactory.decodeFile(imgFile123.getAbsolutePath());
                    *//**//*int resID = res.getIdentifier(mDrawableName , "drawable", context.getPackageName());
                    Drawable drawable = res.getDrawable(resID );*//**//*
                                viewHolder.incoming_reply_image.setImageResource(R.drawable.white_back);

                                viewHolder.incoming_reply_image.setImageBitmap(myBitmap123);
                                break;
                            case "audio":
                                viewHolder.incoming_reply_image.setImageResource(R.drawable.audio_file);
                                viewHolder.incomin_reply_get_text.setText("Audio");
                                viewHolder.incomming_reply_text.setText(strRply[2]);
                                break;
                            case "share":
                                viewHolder.incoming_reply_image.setImageResource(R.drawable.agent);
                                viewHolder.incomin_reply_get_text.setText("Contact");
                                viewHolder.incomming_reply_text.setText(strRply[2]);
                                break;
                            case "location":
                                viewHolder.incoming_reply_image.setImageResource(R.drawable.fake_mapmin);
                                String l []=strRply[0].split(",@,");
                                viewHolder.incomin_reply_get_text.setText(l[0]);
                                viewHolder.incomming_reply_text.setText(strRply[2]);
                                break;
                            case "video":
                                viewHolder.incomin_reply_get_text.setText("Video");
                                viewHolder.incomming_reply_text.setText(strRply[2]);
                                Glide.with(context)
                                        .load(new File(strRply[0]))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .centerCrop()
                                        .dontAnimate()
                                        .into(viewHolder.incoming_reply_image);
                                break;
                            case "reply":
                                viewHolder.incoming_reply_image.setImageResource(R.drawable.agent);
                                String r[]=strRply[0].split("%@,%");
                                viewHolder.incomin_reply_get_text.setText(r[0]);
                                viewHolder.incomming_reply_text.setText(strRply[2]);
                                break;
                            default:
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;*//*

                default:

                    viewHolder.out_reply_cab.setVisibility(View.GONE);
                    viewHolder.incoming_reply_txt_cab.setVisibility(View.GONE);
                    viewHolder.incoming_audio_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_audio_relative.setVisibility(View.GONE);
                    viewHolder.incoming_location_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_location_relative.setVisibility(View.GONE);
                    viewHolder.outgoing_relative_emoji.setVisibility(View.GONE);
                    viewHolder.incoming_relative_emoji.setVisibility(View.GONE);
                    viewHolder.out_image.setVisibility(View.GONE);
                    viewHolder.incoming_linear_contact.setVisibility(View.GONE);
                    viewHolder.out_video_cab.setVisibility(View.GONE);
                    viewHolder.incoming_image.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.incoming_pdf.setVisibility(View.GONE);
                    viewHolder.outgoing_linear_contact.setVisibility(View.GONE);
                    viewHolder.incoming_video_cab.setVisibility(View.GONE);
                    viewHolder.out_pdf.setVisibility(View.GONE);
                    viewHolder.parent_layout.setVisibility(View.GONE);
                    break;
            }


        }*/
    }


    public void playSong(SingleChatModule message1,String url, SeekBar seekBar1, int position,ImageView outgoing_image_view_audio1,
                         ImageView outgoing_image_view_audio_stop1
            ,ImageView incoming_image_view_audio_stop1,ImageView incoming_image_view_audio1) {
        this.seekBar = seekBar1;
        this.message = message1;
        this.outgoing_image_view_audio = outgoing_image_view_audio1;
        this.outgoing_image_view_audio_stop = outgoing_image_view_audio_stop1;
        this.incoming_image_view_audio_stop = incoming_image_view_audio_stop1;
        this.incoming_image_view_audio = incoming_image_view_audio1;

        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer = null;
            }
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            //fab.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.ic_media_pause));
            //  AssetFileDescriptor descriptor = getAssets().openFd("suits.mp3");
            //    mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            ///   descriptor.close();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.getAudioSessionId();
            mediaPlayer.setVolume(0.5f, 0.5f);
            mediaPlayer.setLooping(false);
            final int duration = mediaPlayer.getDuration();
            seekBar.setMax(duration);
            mediaPlayer.start();
            Log.e("Playsong", "Running");
            Log.e("duration", "" + mediaPlayer.getDuration());
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (mediaPlayer != null && mediaPlayer.getCurrentPosition() != 0) {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            if (duration <= mediaPlayer.getCurrentPosition()) {
                                try {
                                    Log.e("stop media", "stoped");
                                    timer.cancel();
                                    timer = null;

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        clearMusic();
                    }
                }
            }, 1, 1000);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    //  seekBarHint.setVisibility(View.VISIBLE);
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                    // seekBarHint.setVisibility(View.VISIBLE);
                    int x = (int) Math.ceil(progress / 1000f);
                  /*  if (x < 10)
                   //     seekBarHint.setText("0:0" + x);
                    else
                        seekBarHint.setText("0:" + x);*/
                    double percent = progress / (double) seekBar.getMax();
                    int offset = seekBar.getThumbOffset();
                    int seekWidth = seekBar.getWidth();
                    int val = (int) Math.round(percent * (seekWidth - 2 * offset));
                  /*  int labelWidth = seekBarHint.getWidth();
                    seekBarHint.setX(offset + seekBar.getX() + val
                            - Math.round(percent * offset)
                            - Math.round(percent * labelWidth / 2));*/
                    if (progress > 0 && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        clearMusic();
                        seekBar.setProgress(0);
                        outgoing_image_view_audio.setVisibility(View.VISIBLE);
                        outgoing_image_view_audio_stop.setVisibility(View.GONE);
                        incoming_image_view_audio_stop.setVisibility(View.GONE);
                        incoming_image_view_audio.setVisibility(View.VISIBLE);
                        message.setSelect(false);

                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.seekTo(seekBar.getProgress());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            clearMusic();
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    private static String getFileSizeMegaBytes(String file) {
        try {
            URL url = new URL(file);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            int file_size = urlConnection.getContentLength();
            // return  (double) file_size / (1024 * 1024) + " MB".trim();
            return (double) file_size + " MB".trim();

        } catch (MalformedURLException e) {

        } catch (IOException e) {

        }
        return "0.0 MB".trim();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // SELECTION LINEAR LAYOUT
        private LinearLayout mainLinearLayout;
        //
        private TextView incoming_contact_time, incoming_contact_number, outgoing_contact_name, outgoing_contact_number, outgoing_contact_time, incoming_video_time, outgoing_video_time, incoming_emoji_time, outgoing_emoji_time, dateTextView, incoming_text_time, outgoing_text_time, outgoing_pdf_time, incoming_pdf_time, outgoing_image_time, incoming_image_time;
        private ImageView incoming_doc_icon, outgoing_doc_icon, incoming_emoji_image, outgoing_emoji_image;
        private ImageView incoming_play_btn, outgoing_play_btn, incoming_video_IV,outgoing_reply_image;
        private RoundedImageView outgoing_image_IV, outgoing_video_IV, incoming_image_IV;
        private LinearLayout incoming_reply_txt_cab,incoming_linear_contact, incoming_video_cab, parent_layout, incoming_image, out_lineat_bubble,out_pdf, incoming_linear_click,incoming_pdf, outgoing_linear_contact;

        ////////AUDIO/////////////
        private RelativeLayout container_list_item,out_reply_cab;
        private RelativeLayout outgoing_audio_relative, incoming_audio_relative,rlForImageUploadingPanel;
        private TextView incoming_reply_text_time,outgoing_reply_text_time,outgoing_audio_time, incoming_audio_time;
        private ImageView incoming_reply_image,reply_tickMarkFirstOut,tickMarkFirstOutAudio, incoming_image_view_audio, incoming_image_view_audio_stop, outgoing_image_view_audio, outgoing_image_view_audio_stop;
        private SeekBar outgoing_seekbar, incoming_seekbar;

        ///////
        private RelativeLayout  incoming_location_relative;
        private LinearLayout incoming_location_click,outgoing_location_relative,outgong_linear_click;
        private TextView incomin_reply_get_text,outgoing_reply_get_text,outgoing_location_time, incoming_location_time,incoming_location_address,outgoing_location_address;
        private ImageView tickMarkLocation,incoming_image_view_icon_audio;

        //Loader
        private ProgressBar outgoing_progress_bar,progress_bar, outgoing_video_progress_bar, incoming_progress_bar, incoming_video_progress_bar,outgoing_image_view_audio_pb,outgoing_doc_icon_pb,incoming_doc_icon_pb, incoming_audio_icon_pb;
        private RelativeLayout out_image, relativeDate, layout, out_video_cab, outgoing_relative_emoji, incoming_relative_emoji,rlForVideoUploadingPanel;
        private TextView outgoing_reply_text,incomming_reply_text,incomming_text, outgoingTextImage, txtoutgoing_pdf, txtincomming_pdf;

        private ImageView tickMarkContactOut, tickMarkFirstOut, tickMarkFirstOutImage,tickMarkFirstOutVideo, tickMarkFirstOutPDF, tickMarkFirstOutEmoji, outgoing_image_view_icon_audio, outgoing_image_view_audio_retry,outgoing_doc_icon_retry,incoming_doc_icon_download, incoming_audio_icon_download;
        // VIEW CONTACT
        private Button outgoing_contact_view, incoming_view_image,btnVideoUpload,btnImageUpload,btnImageDownload,btnVideoDownload;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            /*uploading image*/
            btnImageUpload=itemView.findViewById(R.id.btnImageUpload);
            outgoing_progress_bar=itemView.findViewById(R.id.outgoing_progress_bar);

            /*uploading video*/
            outgoing_video_progress_bar=itemView.findViewById(R.id.outgoing_video_progress_bar);
            btnVideoUpload=itemView.findViewById(R.id.btnVideoUpload);

            /*Uploading audio*/
            outgoing_image_view_audio_pb=itemView.findViewById(R.id.outgoing_image_view_audio_pb);
            outgoing_image_view_icon_audio=itemView.findViewById(R.id.outgoing_image_view_icon_audio);
            outgoing_image_view_audio_retry=itemView.findViewById(R.id.outgoing_image_view_audio_retry);

            /*uploading doc*/
            outgoing_doc_icon_pb=itemView.findViewById(R.id.outgoing_doc_icon_pb);
            outgoing_doc_icon_retry=itemView.findViewById(R.id.outgoing_doc_icon_retry);

            /*downloading image*/
            incoming_progress_bar=itemView.findViewById(R.id.incoming_progress_bar);
            btnImageDownload=itemView.findViewById(R.id.btnImageDownload);

            /*downloading docs*/
            incoming_doc_icon_download=itemView.findViewById(R.id.incoming_doc_icon_download);
            incoming_doc_icon_pb=itemView.findViewById(R.id.incoming_doc_icon_pb);

            /*downloading video*/
            incoming_video_progress_bar=itemView.findViewById(R.id.incoming_video_progress_bar);
            btnVideoDownload=itemView.findViewById(R.id.btnVideoDownload);

            /*downloading audio*/
            incoming_audio_icon_pb =itemView.findViewById(R.id.incoming_image_view_icon_pb);
            incoming_audio_icon_download =itemView.findViewById(R.id.incoming_image_view_icon_download);
            incoming_image_view_icon_audio=itemView.findViewById(R.id.incoming_image_view_icon_audio);

            // get the reference of item view's
            container_list_item = (RelativeLayout) itemView.findViewById(R.id.container_list_item);
            outgoing_emoji_time = (TextView) itemView.findViewById(R.id.outgoing_emoji_time);
            incoming_emoji_time = (TextView) itemView.findViewById(R.id.incoming_emoji_time);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);

            incoming_text_time = (TextView) itemView.findViewById(R.id.incoming_text_time);
            outgoing_text_time = (TextView) itemView.findViewById(R.id.outgoing_text_time);
            outgoing_pdf_time = (TextView) itemView.findViewById(R.id.outgoing_pdf_time);
            incoming_pdf_time = (TextView) itemView.findViewById(R.id.incoming_pdf_time);
            outgoing_image_time = (TextView) itemView.findViewById(R.id.outgoing_image_time);
            incoming_image_time = (TextView) itemView.findViewById(R.id.incoming_image_time);

            outgoing_video_time = (TextView) itemView.findViewById(R.id.outgoing_video_time);
            outgoing_contact_name = (TextView) itemView.findViewById(R.id.outgoing_contact_name);
            incoming_video_time = (TextView) itemView.findViewById(R.id.incoming_video_time);
            outgoing_contact_time = (TextView) itemView.findViewById(R.id.outgoing_contact_time);
            outgoing_contact_number = (TextView) itemView.findViewById(R.id.outgoing_contact_number);
            incoming_contact_number = (TextView) itemView.findViewById(R.id.incoming_contact_number);
            incoming_contact_time = (TextView) itemView.findViewById(R.id.incoming_contact_number);
            incomming_text = (TextView) itemView.findViewById(R.id.incomming_text);
            outgoing_reply_text = (TextView) itemView.findViewById(R.id.outgoing_reply_text);
            incomming_reply_text = (TextView) itemView.findViewById(R.id.incomming_reply_text);
            outgoingTextImage = (TextView) itemView.findViewById(R.id.outgoing_text);

            txtoutgoing_pdf = (TextView) itemView.findViewById(R.id.txtoutgoing_pdf);
            txtincomming_pdf = (TextView) itemView.findViewById(R.id.txtincomming_pdf);

            incoming_play_btn = (ImageView) itemView.findViewById(R.id.incoming_play_btn);
            incoming_doc_icon = (ImageView) itemView.findViewById(R.id.incoming_doc_icon);
            outgoing_doc_icon = (ImageView) itemView.findViewById(R.id.outgoing_doc_icon);
            outgoing_image_IV = (RoundedImageView) itemView.findViewById(R.id.outgoing_image_IV);
            incoming_image_IV = (RoundedImageView) itemView.findViewById(R.id.incoming_image_IV);
            incoming_emoji_image = (ImageView) itemView.findViewById(R.id.incoming_emoji_image);
            outgoing_emoji_image = (ImageView) itemView.findViewById(R.id.outgoing_emoji_image);

            outgoing_video_IV = (RoundedImageView) itemView.findViewById(R.id.outgoing_video_IV);
            outgoing_play_btn = (ImageView) itemView.findViewById(R.id.outgoing_play_btn);

            incoming_video_IV = (ImageView) itemView.findViewById(R.id.incoming_video_IV);


            mainLinearLayout = (LinearLayout) itemView.findViewById(R.id.mainLinearLayout);
            incoming_video_cab = (LinearLayout) itemView.findViewById(R.id.incoming_video_cab);

            incoming_linear_contact = (LinearLayout) itemView.findViewById(R.id.incoming_linear_contact);

            layout = (RelativeLayout) itemView.findViewById(R.id.out_tv_cab);
            parent_layout = (LinearLayout) itemView.findViewById(R.id.incoming_txt_cab);
            incoming_image = (LinearLayout) itemView.findViewById(R.id.incoming_img_cab);
            out_pdf = (LinearLayout) itemView.findViewById(R.id.out_pdf);
            incoming_pdf = (LinearLayout) itemView.findViewById(R.id.incoming_pdf);
            out_lineat_bubble = (LinearLayout) itemView.findViewById(R.id.out_lineat_bubble);
            incoming_linear_click = (LinearLayout) itemView.findViewById(R.id.incoming_linear_click);
            outgoing_linear_contact = (LinearLayout) itemView.findViewById(R.id.outgoing_linear_contact);
            out_image = (RelativeLayout) itemView.findViewById(R.id.out_image_cab);
            relativeDate = (RelativeLayout) itemView.findViewById(R.id.relativeDate);
            out_video_cab = (RelativeLayout) itemView.findViewById(R.id.out_video_cab);
            outgoing_relative_emoji = (RelativeLayout) itemView.findViewById(R.id.outgoing_relative_emoji);
            incoming_relative_emoji = (RelativeLayout) itemView.findViewById(R.id.incoming_relative_emoji);
            // CONTACT VIEW
            outgoing_contact_view = (Button) itemView.findViewById(R.id.outgoing_contact_view);
            incoming_view_image = (Button) itemView.findViewById(R.id.incoming_view_image);

            incoming_location_click = (LinearLayout) itemView.findViewById(R.id.incoming_location_click);
            outgong_linear_click = (LinearLayout) itemView.findViewById(R.id.outgong_linear_click);
            outgoing_location_relative = (LinearLayout) itemView.findViewById(R.id.outgoing_location_relative);
            incoming_location_relative = (RelativeLayout) itemView.findViewById(R.id.incoming_location_relative);

            outgoing_location_address = (TextView) itemView.findViewById(R.id.outgoing_location_address);
            incoming_location_address = (TextView) itemView.findViewById(R.id.incoming_location_address);
            incoming_location_time = (TextView) itemView.findViewById(R.id.incoming_location_time);
            outgoing_location_time = (TextView) itemView.findViewById(R.id.outgoing_location_time);
            tickMarkLocation = (ImageView) itemView.findViewById(R.id.tickMarkLocation);
            outgoing_audio_relative = (RelativeLayout) itemView.findViewById(R.id.outgoing_audio_relative);
            incoming_audio_relative = (RelativeLayout) itemView.findViewById(R.id.incoming_audio_relative);
            outgoing_audio_time = (TextView) itemView.findViewById(R.id.outgoing_audio_time);
            incoming_audio_time = (TextView) itemView.findViewById(R.id.incoming_audio_time);
            tickMarkFirstOutAudio = (ImageView) itemView.findViewById(R.id.tickMarkFirstOutAudio);
            outgoing_image_view_audio_stop = (ImageView) itemView.findViewById(R.id.outgoing_image_view_audio_stop);
            outgoing_image_view_audio = (ImageView) itemView.findViewById(R.id.outgoing_image_view_audio);
            incoming_image_view_audio = (ImageView) itemView.findViewById(R.id.incoming_image_view_audio);
            incoming_image_view_audio_stop = (ImageView) itemView.findViewById(R.id.incoming_image_view_audio_stop);
            incoming_seekbar = (SeekBar) itemView.findViewById(R.id.incoming_seekbar);
            outgoing_seekbar = (SeekBar) itemView.findViewById(R.id.outgoing_seekbar);

            //REPLY TO MESSAGE
            out_reply_cab=(RelativeLayout) itemView.findViewById(R.id.out_reply_cab);
            incoming_reply_txt_cab=(LinearLayout)itemView.findViewById(R.id.incoming_reply_txt_cab);
            reply_tickMarkFirstOut=(ImageView) itemView.findViewById(R.id.reply_tickMarkFirstOut);
            incoming_reply_image=(ImageView) itemView.findViewById(R.id.incoming_reply_image);
            outgoing_reply_text_time=(TextView) itemView.findViewById(R.id.outgoing_reply_text_time);
            incoming_reply_text_time=(TextView) itemView.findViewById(R.id.incoming_reply_text_time);
            outgoing_reply_get_text=(TextView) itemView.findViewById(R.id.outgoing_reply_get_text_adapter);
            incomin_reply_get_text=(TextView) itemView.findViewById(R.id.incomin_reply_get_text);
            outgoing_reply_image=(ImageView)itemView.findViewById(R.id.outgoing_reply_image);
            incoming_reply_image=(ImageView)itemView.findViewById(R.id.incoming_reply_image);

            //TICKUI OPTIOPN FOR CHAT
            tickMarkFirstOut = (ImageView) itemView.findViewById(R.id.tickMarkFirstOut);
            tickMarkContactOut = (ImageView) itemView.findViewById(R.id.tickMarkContactOut);
            tickMarkFirstOutImage = (ImageView) itemView.findViewById(R.id.tickMarkFirstOutImage);
            tickMarkFirstOutVideo = (ImageView) itemView.findViewById(R.id.tickMarkFirstOutVideo);
            tickMarkFirstOutPDF = (ImageView) itemView.findViewById(R.id.tickMarkFirstOutPDF);
            tickMarkFirstOutEmoji = (ImageView) itemView.findViewById(R.id.tickMarkFirstOutEmoji);

        }
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        chatMessageList.clear();
        if (charText.trim().length() == 0) {
            chatMessageList.addAll(searchList);
        } else {
            for (SingleChatModule wp : searchList) {
                if (wp.getMessage().toLowerCase(Locale.getDefault()).contains(charText) && wp.getChatType().equalsIgnoreCase("msg")) {
                    chatMessageList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void clearMusic() {
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}