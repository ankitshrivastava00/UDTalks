package com.ziasy.xmppchatapplication.common;

/**
 * Created by PnP on 12-02-2018.
 */

import android.app.Application;

import com.ziasy.xmppchatapplication.reciever.PushReceiver;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

//import com.splunk.mint.Mint;

public class ChatApplication extends Application {

    private static ChatApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized ChatApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(PushReceiver.RecievingMessageInterface listener) {
        PushReceiver.singleChatInterface = listener;
    }

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Confiq.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    public Socket getSocket() {
        return mSocket;
    }

}
