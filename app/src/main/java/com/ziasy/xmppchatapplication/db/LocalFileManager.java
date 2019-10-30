package com.ziasy.xmppchatapplication.db;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocalFileManager {
    public static final String folder_name= "XMPPChat";
    public static final String image_folder_name= "/UDTalks/Images";
    public static final String audio_folder_name= "/UDTalks/Audio";
    public static final String video_folder_name= "/UDTalks/Video";
    public static final String docs_folder_name= "/UDTalks/Docs";
    Context context;
    SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
    String format = s.format(new Date());
    public LocalFileManager(Context context){
        this.context=context;
    }

    public void createFolder(){
        File myDir = new File(context.getCacheDir(), folder_name);
        myDir.mkdir();
        Log.d("Folder", "Created");
    }

    public File createAudioFolder(){
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + audio_folder_name);
        if (!file.exists()) {
            file.mkdirs();
        }
        return root;
    }

    public void createVideoFolder(){
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + video_folder_name);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void createImageFolder(){
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + image_folder_name);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void createDocsFolder(){
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + docs_folder_name);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void deleteAudioFile(String filename){
        try{
            File file= new File(filename);
            boolean result=file.delete();
            if (result){
                Log.d("AuidoCancel", "File deleted");
            }else{
                Log.d("AudioCancel", "Unable to delete the file");
            }
        }catch (Exception e){
            Log.d("Background_service", e.toString());
        }
    }

    public File saveMedia(File sourceFile, String ext) throws IOException {
        File root = Environment.getExternalStorageDirectory();
        File destFile = null;
        if (ext.equals(".jpg")){
            destFile= new File(root.getAbsolutePath() + image_folder_name+"/"+sourceFile.getName());
        }
        if (ext.equals(".mp4")){
            destFile= new File(root.getAbsolutePath() + video_folder_name+"/"+sourceFile.getName());
        }
        if (ext.equals(".mp3")){
            destFile= new File(root.getAbsolutePath() + audio_folder_name+"/"+sourceFile.getName());
        }
        Log.d("Background_service", "fname-"+sourceFile.getName());
        Log.d("Media-copy", destFile.getAbsolutePath());
        if (!sourceFile.exists()) {

        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
        return destFile;
    }


    public File saveDocs (File sourceFile, String filename) throws IOException {
        File root = Environment.getExternalStorageDirectory();
        File destFile = null;
            destFile= new File(root.getAbsolutePath() + docs_folder_name+"/"+filename);

        Log.d("Background_service", "fname-"+filename);
        /*if (!sourceFile.exists()) {
            return;
        }*/

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }

        return destFile;
    }
}
