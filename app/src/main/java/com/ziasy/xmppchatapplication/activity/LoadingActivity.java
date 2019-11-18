package com.ziasy.xmppchatapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;
import com.ziasy.xmppchatapplication.R;
import com.ziasy.xmppchatapplication.common.SessionManagement;
import com.ziasy.xmppchatapplication.database.DBUtil;
import com.ziasy.xmppchatapplication.database.LocalFileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LoadingActivity extends AppCompatActivity {
    private SessionManagement sd;
    private TextView mtextClock;
    private DownloadRequest downloadRequest;
    private ThinDownloadManager downloadManager;
    private ProgressBar mpb;
    private int iloop=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
        mtextClock=findViewById(R.id.textClock);
        mpb=findViewById(R.id.pb);
        sd=new SessionManagement(LoadingActivity.this);
        sd.setBackupStatus("true");
        downloadManager=new ThinDownloadManager();
        mtextClock.setText("Setting up app for you!");

        File root= Environment.getExternalStorageDirectory();
        File file = new File(getFilesDir(), LocalFileManager.image_folder_name+
                "/Emojis");
        try{
            file.delete();
        }catch (Exception e){
            Log.e("EXCPTN", e.toString());
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        downloadEmojis();

    }

    private void downloadEmojis()
    {
        String url="https://sgp1.digitaloceanspaces.com/udtalks/emoji"+String.valueOf(iloop)+".png";
        //Log.d("Background_demoji","i"+iloop+"/fi"+iloop+"/iloop"+iloop+"/url"+url);
        Uri downloadUri = Uri.parse(url);
        Uri destinationUri = Uri.parse(
                getFilesDir()+ LocalFileManager.image_folder_name+
                        "/Emojis"+"/"+"emoji"+ String.valueOf(iloop)+".png");
        downloadRequest = new DownloadRequest(downloadUri)
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        DBUtil.insertEmoji(LoadingActivity.this,"emoji"+ String.valueOf(iloop),
                                getFilesDir()+ LocalFileManager.image_folder_name+
                                        "/Emojis"+"/"+"emoji"+ String.valueOf(iloop)+".png",
                                String.valueOf(iloop));
                        Log.d("Background_de","completed");

                        if (iloop==79){
                            sd.setLoginStatus("true");

                            Intent i = new Intent(LoadingActivity.this, ChatUserListActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            iloop++;
                            downloadEmojis();
                        }
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                        Log.d("Background_de",errorMessage+downloadRequest);
                        retryDownload(downloadRequest);
                    }

                    private void retryDownload(DownloadRequest downloadRequest) {
                        downloadManager.add(downloadRequest);
                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {

                    }
                });
        downloadManager.add(downloadRequest);


    }

}
