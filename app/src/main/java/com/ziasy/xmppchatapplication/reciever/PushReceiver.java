package com.ziasy.xmppchatapplication.reciever;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;
/*
import com.ziasy.xmppchatapplication.ChatActivity;
import com.ziasy.xmppchatapplication.ChatUserList;
import com.ziasy.xmppchatapplication.common.SessionManagement;
import com.ziasy.xmppchatapplication.group_chat.activity.GroupChatActivity;
import com.ziasy.xmppchatapplication.localDB.LocalDBHelper;
import com.ziasy.xmppchatapplication.localDB.LocalRecDownload;
import com.ziasy.xmppchatapplication.model.JsonModelForChat;
import com.ziasy.xmppchatapplication.util.Utils;
import static com.ziasy.xmppchatapplication.util.Utils.timeConverter;
*/
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.ziasy.xmppchatapplication.R;

public class PushReceiver extends BroadcastReceiver {
    private String pusddid = null,reciverid = null,uid = null, image = null, description = null, admin = null, chattype = null, message = null, dtype = null, senderid = null, isread = null, deliver = null,
            sname = null, did = null, datetime = null, doc_name=null, uname= null;
  //  SessionManagement sd;
    Intent intent;
   // private LocalRecDownload localRecDownload;
    String thumb=null;
  //  LocalDBHelper localDBHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        /*Intent intent1= new Intent(context, SendDataService.class);
        context.startService(intent1);*/
        String notificationTitle = "MyApp";
        String notificationText = "Test notification";
      /*  sd = new SessionManagement(context);
        localDBHelper= new LocalDBHelper(context);
        //new LocalDBHelper(context).callOnCreate();
        localRecDownload=new LocalRecDownload();
        if (sd.getLoginStatus().equalsIgnoreCase("true")) {
            // Attempt to extract the "message" property from the payload: {"message":"Hello World!"}
            if (intent.getStringExtra("message") != null) {
                notificationText = intent.getStringExtra("message");
            }
            // Log.e("MessageseartsertBody", notificationText.toString());
            if (!TextUtils.isEmpty(notificationText)) {
                JSONObject customJson = null;
//                Log.d("JDONVJDBDFD",customJson.toString());
                try {
                    customJson = new JSONObject(notificationText);
                    Log.d("JDONVJDBDFD",customJson.toString());
                    //  if (!customJson.isNull("obj")) {
                    image = customJson.optString("image");
                    description = customJson.optString("description");
                    admin = customJson.optString("admin");
                    chattype = customJson.optString("chattype");
                    reciverid = customJson.optString("reciverid");
                    pusddid = customJson.optString("pusddid");
                    uid = customJson.optString("uid");
                    doc_name = customJson.optString("doc_name");
                    //thumbnail
                    dtype = customJson.getString("dtype");

                    if (dtype.equalsIgnoreCase("img")|| dtype.equalsIgnoreCase("video")){
                        thumb=customJson.getString("thumbnail");
                    }

                    if (thumb== null){
                        thumb="";
                    }
                    message = customJson.getString("message");
                    if (chattype.equalsIgnoreCase("group")){
                        sname=customJson.getString("g_name");
                        description=customJson.getString("g_desc");
                        image=customJson.getString("g_img");
                        uname=customJson.optString("uname");
                    }else{
                        sname = customJson.getString("sname");
                    }
                    did = customJson.getString("did");
                    datetime = customJson.getString("datetime");
                    isread = customJson.getString("isread");
                    deliver = customJson.getString("deliver");
                    senderid = customJson.getString("senderid");
                    ChatUserList allPrdctData = new ChatUserList();
                    allPrdctData.setId(reciverid);
                    allPrdctData.setName(sname);
                    allPrdctData.setDescription(description);
                    allPrdctData.setLastMessage(message);
                    allPrdctData.setDatetime(datetime);
                    allPrdctData.setIsOnline("false");
                    allPrdctData.setTime(timeConverter(datetime));
                    allPrdctData.setImageUrl(image);
                    allPrdctData.setType(dtype);
                    allPrdctData.setData(message);
                    allPrdctData.setChattype(chattype);
                    allPrdctData.setDid(did);
                    allPrdctData.setAdmin(admin);
                    localDBHelper.insertChatList(allPrdctData);

                    Log.d("Message_per ", reciverid + " : " + dtype + sname + did + datetime + isread + deliver + senderid + message);
                    // }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.d("Pushy",e.toString());
                    e.printStackTrace();
                }
            }
            try {
                if (chattype.equalsIgnoreCase("group")&& !senderid.equals(sd.getKeyId())){
                    new LocalDBHelper(context).updateChatListLastmessage("group", reciverid,dtype,message, timeConverter(datetime),
                            datetime);
                    switch (dtype){
                        case "img":
                            *//*localRecDownload.localRecDownloadMethod(message, LocalFileManager.image_folder_name,
                                    dtype,uid,reciverid, context);*//*
                            new LocalDBHelper(context).insertGroupMessagesOffline(new JsonModelForChat(message, senderid, reciverid, Utils.dateConverter(datetime), isread, "", timeConverter(datetime), deliver, uid, message, dtype, "asdfa", uname, "", datetime, false, "false", "", message.substring(message.lastIndexOf(".")),thumb,0), reciverid);
                            break;
                        case "audio":
                            *//*localRecDownload.localRecDownloadMethod(message, LocalFileManager.audio_folder_name,
                                    dtype,uid,reciverid, context);*//*
                            new LocalDBHelper(context).insertGroupMessagesOffline(new JsonModelForChat(message, senderid, reciverid, Utils.dateConverter(datetime), isread, "", timeConverter(datetime), deliver, uid, message, dtype, "asdfa", uname, "", datetime, false, "false", "", message.substring(message.lastIndexOf(".")),"",0), reciverid);
                            break;
                        case "video":
                            *//*localRecDownload.localRecDownloadMethod(message, LocalFileManager.video_folder_name,
                                    dtype,uid,reciverid, context);*//*
                            new LocalDBHelper(context).insertGroupMessagesOffline(new JsonModelForChat(message, senderid, reciverid, Utils.dateConverter(datetime), isread, "", timeConverter(datetime), deliver, uid, message, dtype, "asdfa", uname, "", datetime, false, "false", "", message.substring(message.lastIndexOf(".")),thumb,0), reciverid);
                            break;
                        case "pdf":
                            *//*localRecDownload.localRecDownloadMethod(message, LocalFileManager.docs_folder_name,
                                    dtype,uid,reciverid, context);*//*
                            new LocalDBHelper(context).insertGroupMessagesOffline(new JsonModelForChat(message, senderid, reciverid, Utils.dateConverter(datetime), isread, "", timeConverter(datetime), deliver, uid, message, dtype, doc_name, uname, "", datetime, false, "false", "", message.substring(message.lastIndexOf(".")),"",0), reciverid);
                            break;
                        default:
                            new LocalDBHelper(context).insertGroupMessagesOffline(new JsonModelForChat(message, senderid, reciverid, Utils.dateConverter(datetime), isread, "", timeConverter(datetime), deliver, uid, message, dtype, "asdfa", uname, "", datetime, false, "false", "", "","",2), reciverid);
                            break;
                    }
                }else if (chattype.equalsIgnoreCase("indivisual")){
                    new LocalDBHelper(context).updateChatListLastmessage("single", senderid,dtype,message, timeConverter(datetime),
                            datetime);
                    switch (dtype){
                        case "img":
                            *//*localRecDownload.localRecDownloadMethod(message, LocalFileManager.image_folder_name,
                                    dtype,uid,reciverid, context);*//*
                            new LocalDBHelper(context).insertSingleMessagesOffline(new JsonModelForChat(message, senderid, reciverid, Utils.dateConverter(datetime), isread, "", timeConverter(datetime), deliver, uid, message, dtype, "asdfa", "asdfsda", "", datetime, false, "false", "", message.substring(message.lastIndexOf(".")),thumb,0), reciverid);
                            break;
                        case "audio":
                            *//*localRecDownload.localRecDownloadMethod(message, LocalFileManager.audio_folder_name,
                                    dtype,uid,reciverid, context);*//*
                            new LocalDBHelper(context).insertSingleMessagesOffline(new JsonModelForChat(message, senderid, reciverid, Utils.dateConverter(datetime), isread, "", timeConverter(datetime), deliver, uid, message, dtype, "asdfa", "asdfsda", "", datetime, false, "false", "", message.substring(message.lastIndexOf(".")),"",0), reciverid);
                            break;
                        case "video":
                            *//*localRecDownload.localRecDownloadMethod(message, LocalFileManager.video_folder_name,
                                    dtype,uid,reciverid, context);*//*
                            new LocalDBHelper(context).insertSingleMessagesOffline(new JsonModelForChat(message, senderid, reciverid, Utils.dateConverter(datetime), isread, "", timeConverter(datetime), deliver, uid, message, dtype, "asdfa", "asdfsda", "", datetime, false, "false", "", message.substring(message.lastIndexOf(".")),thumb,0), reciverid);
                            break;
                        case "pdf":
                            *//*localRecDownload.localRecDownloadMethod(message, LocalFileManager.docs_folder_name,
                                    dtype,uid,reciverid, context);*//*
                            new LocalDBHelper(context).insertSingleMessagesOffline(new JsonModelForChat(message, senderid, reciverid, Utils.dateConverter(datetime), isread, "", timeConverter(datetime), deliver, uid, message, dtype, doc_name, "asdfsda", "", datetime, false, "false", "", message.substring(message.lastIndexOf(".")),"",0), reciverid);
                            break;
                        default:
                            new LocalDBHelper(context).insertSingleMessagesOffline(new JsonModelForChat(message, senderid, reciverid, Utils.dateConverter(datetime), isread, "", timeConverter(datetime), deliver, uid, message, dtype, "asdfa", "asdfsda", "", datetime, false, "false", "", "","",2), reciverid);
                            break;
                    }
                }

                for (ChatUserList model : new LocalDBHelper(context).getAllChatList()){

                    if(model.getMute().equalsIgnoreCase("false") && model.getId().equalsIgnoreCase(senderid)){
                        if (!senderid.equalsIgnoreCase(sd.getKeyId())){
                            sendNotification(Integer.parseInt(senderid), sname, message, context, dtype, chattype);
                        }
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Notification_Expre", e.toString());

            }
        }*/
    }

    private void sendNotification(int id, String Name, String messageBody, Context context, String Type, String ChatType) {
        try {

            Log.e("Message_Body", messageBody.toString());


           /* if (ChatType.equalsIgnoreCase("group")) {
                intent = new Intent(context, GroupChatActivity.class);
*//*
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
*//*
                intent.putExtra("image", image);
                intent.putExtra("description", description);
                intent.putExtra("admin", admin);
                intent.putExtra("rid", senderid);

            } else if (ChatType.equalsIgnoreCase("indivisual")) {
                intent = new Intent(context, ChatActivity.class);
                intent.putExtra("rid", senderid);
*//*
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
*//*
            }
            intent.putExtra("name", Name);
            intent.putExtra("chattype", dtype);
            intent.putExtra("did", pusddid);
            intent.putExtra("mute", "false");
            intent.putStringArrayListExtra("forwardString", new ArrayList<>());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
      */      Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon);
            if (Type.equalsIgnoreCase("msg")) {
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(Name)
                        .setContentText(messageBody)
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                        .setLargeIcon(bm)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                      ;//  .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(id, notificationBuilder.build());
                //  ((Activity)context).finish();
            } else if (Type.equalsIgnoreCase("img")) {

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(Name)
                        //.setContentText(messageBody)
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(getBitmapfromUrl(messageBody)))
                        .setLargeIcon(bm)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        ;//.setContentIntent(pendingIntent);
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(id, notificationBuilder.build());
            } else if (Type.equalsIgnoreCase("pdf")) {
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(Name)
                        .setContentText("Document")
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Document"))
                        .setLargeIcon(bm)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        ;//.setContentIntent(pendingIntent);
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(id, notificationBuilder.build());
            } else if (Type.equalsIgnoreCase("video")) {
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(Name)
                        .setContentText("Video")
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Video"))
                        .setLargeIcon(bm)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        ;//.setContentIntent(pendingIntent);
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(id, notificationBuilder.build());
            } else if (Type.equalsIgnoreCase("share")) {
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(Name)
                        .setContentText("Contact Details")
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Contact Details"))
                        .setLargeIcon(bm)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                       ;// .setContentIntent(pendingIntent);
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(id, notificationBuilder.build());
            } else if (Type.equalsIgnoreCase("audio")) {
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(Name)
                        .setContentText("Audio File")
                        .setColor(context.getResources().getColor(R.color.colorPrimary))

                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Audio File"))
                        .setLargeIcon(bm)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                       ;// .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(id, notificationBuilder.build());
            } else if (Type.equalsIgnoreCase("location")) {
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(Name)
                        .setContentText("Location")
                        .setColor(context.getResources().getColor(R.color.colorPrimary))

                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Location"))
                        .setLargeIcon(bm)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        ;//.setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(id, notificationBuilder.build());
            }else if (Type.equalsIgnoreCase("emoji")) {
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(Name)
                        .setContentText("Emoji")
                        .setColor(context.getResources().getColor(R.color.colorPrimary))

                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Emoji"))
                        .setLargeIcon(bm)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        ;//.setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(id, notificationBuilder.build());
            }

        } catch (Exception e) {
            Log.d("Pushy",e.toString());
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }


}