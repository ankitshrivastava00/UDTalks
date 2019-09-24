package com.ziasy.xmppchatapplication.common;

/**
 * Created by PnP on 12-02-2018.
 */

import android.app.Application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

//import com.splunk.mint.Mint;

public class ChatApplication extends Application {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Confiq.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
      //  Mint.initAndStartSession(this, "fbfacc1b");
        // TODO: Update with your HEC token
        // Mint.initAndStartSessionHEC(this.getApplication(), "MINT_HEC_URL", "YOUR_HEC_TOKEN");

      //  Mint.enableDebugLog();

    }

    public Socket getSocket() {
        return mSocket;
    }
}
