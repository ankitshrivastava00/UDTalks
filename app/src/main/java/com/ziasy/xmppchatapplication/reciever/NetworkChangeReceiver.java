package com.ziasy.xmppchatapplication.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ziasy.xmppchatapplication.common.ChatApplication;
import com.ziasy.xmppchatapplication.common.ConnectionDetector;
import com.ziasy.xmppchatapplication.common.NetworkUtil;
import com.ziasy.xmppchatapplication.common.SessionManagement;

import io.socket.client.Socket;
import me.pushy.sdk.Pushy;

import static androidx.legacy.content.WakefulBroadcastReceiver.startWakefulService;


/**
 * Created by PnP on 01-03-2018.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    ConnectionDetector cd;
    private Socket mSocket;
    SessionManagement sd;

    @Override
    public void onReceive(Context context, Intent intent) {
        //context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()+"UDTalks")));
        sd = new SessionManagement(context);
        cd = new ConnectionDetector(context);
        ChatApplication app = ((ChatApplication) context.getApplicationContext());
        mSocket = app.getSocket();
        String status = NetworkUtil.getConnectivityStatusString(context);
        if (Build.VERSION.SDK_INT >= 23) {
            /*
            Intent serviceIntent = new Intent(context, SendDataService.class);
            Intent serviceGIntent = new Intent(context, SendGroupDataService.class);
            startWakefulService(context, serviceIntent);
            startWakefulService(context, serviceGIntent);
            */
        } else {
            /*
            Intent serviceIntent = new Intent(context, SendDataService.class);
            Intent serviceGIntent = new Intent(context, SendGroupDataService.class);
            context.startService(serviceIntent);
            context.startService(serviceGIntent);
            */
        }
        if (sd.getLoginStatus().equalsIgnoreCase("true")){
        if (status.equalsIgnoreCase("enabled")) {
            Pushy.listen(context);
            Log.e("INTERNET","YESS");
            Toast.makeText(app, "Internet enable", Toast.LENGTH_SHORT).show();
            /*
            CustomLogger.getInsatance(context).putLog("Network Connected");
            context.startService(new Intent(context, SendOfflineDataService.class));
            */
            Intent intent1 = new Intent("INTERNET");
            intent1.putExtra("key", "YES");
            //LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
            mSocket.connect();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", sd.getKeyId());
            jsonObject.addProperty("isChatEnable", "false");
            jsonObject.addProperty("isDelivered", "false");
            jsonObject.addProperty("isReaded", "false");
            jsonObject.addProperty("isOnlineStatus", "false");
            mSocket.emit("userid", jsonObject);
/*
            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.addProperty("id", sd.getKeyId());
            jsonObject1.addProperty("device_id", sd.getUserFcmId());
            mSocket.emit("notificationAlert", jsonObject1);
*/
        // Utils.cancelAlarm(context);
        // Utils.scheduleAlarm(context);
        } else {
            Pushy.listen(context);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", sd.getKeyId());
            jsonObject.addProperty("isOnlineStatus", "false");
            mSocket.emit("disableUser", jsonObject);
            Log.e("INTERNET","NO");
            Toast.makeText(app, "Internet disable", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent("INTERNET");
            intent1.putExtra("key", "NO");
            mSocket.disconnect();
         // LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
        }
    }
    }
}
