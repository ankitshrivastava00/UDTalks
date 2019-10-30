package com.ziasy.xmppchatapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.ziasy.xmppchatapplication.common.SessionManagement;
import com.ziasy.xmppchatapplication.model.ChatUserList;
import com.ziasy.xmppchatapplication.model.JsonModelForChat;
import com.ziasy.xmppchatapplication.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class LocalDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION=1;
    private static final String DB_NAME= "XMPPLocalDatabase.db";
    private static final String SINGLE_CHAT_TABLE ="single_chat";
    private static final String GROUP_CHAT_TABLE="group_chat";
    private static final String CHAT_LIST_TABLE ="chat_list";
    private static final String USERS_LIST="users_list";
    private static final String EMOJI_LIST="emoji_list";
    private static final String COLUMN_ID="id";
    String firstDate="";
    private SessionManagement sd;
    Context context;
  //  private LocalRecDownload localRecDownload;
    public LocalDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
//        SQLiteDatabase msqLiteDatabase=this.getReadableDatabase();
        sd = new SessionManagement(context);
        this.context=context;
     //   localRecDownload= new LocalRecDownload();
/*        try
        {
            File databaseFile = context.getDatabasePath(DB_NAME);
            SQLiteDatabase _db = SQLiteDatabase.openOrCreateDatabase(databaseFile,null);
        } catch (Exception e)
        {
            String databasePath =  context.getFilesDir().getPath() +  "/" + DB_NAME;
            File databaseFile = new File(databasePath);
            msqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(databaseFile,null);
        }finally {
            msqLiteDatabase.close();
        }*/
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + GROUP_CHAT_TABLE + " ( " + COLUMN_ID + " VARCHAR , send_id VARCHAR, rcv_id VARCHAR, date VARCHAR, message TEXT, isread VARCHAR, deliver VARCHAR, type VARCHAR, response VARCHAR, time VARCHAR, mid VARCHAR, lat VARCHAR, long VARCHAR, heading VARCHAR, datetime VARCHAR, is_select VARCHAR,status VARCHAR,uploading VARCHAR,did VARCHAR,extension VARCHAR,parent VARCHAR,list_position INTEGER,data TEXT,group_img TEXT,description TEXT, group_name TEXT, UNIQUE(mid) );");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + SINGLE_CHAT_TABLE + " ( " + COLUMN_ID + " VARCHAR , send_id VARCHAR, rcv_id VARCHAR, date VARCHAR, message TEXT, isread VARCHAR, deliver VARCHAR, type VARCHAR, response VARCHAR, time VARCHAR, mid VARCHAR, lat VARCHAR, long VARCHAR, heading VARCHAR, datetime VARCHAR, is_select VARCHAR,status VARCHAR,uploading VARCHAR,did VARCHAR,extension VARCHAR,parent VARCHAR,list_position INTEGER,data TEXT, UNIQUE(mid) );");
        sqLiteDatabase.execSQL( "CREATE TABLE IF NOT EXISTS " + USERS_LIST + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name" + " TEXT,"
                + "device_id" + " VARCHAR,"
                + "count" + " VARCHAR,"
                + "datetime" + " VARCHAR,"
                + "image" + " TEXT"
                + " );");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + CHAT_LIST_TABLE + " ( id VARCHAR, name VARCHAR , description TEXT,  lastMessage TEXT, datetime VARCHAR , time varchar , userstatus VARCHAR , photo TEXT , dtype VARCHAR , message TEXT , chattype VARCHAR , did VARCHAR , admin VARCHAR, mute VARCHAR DEFAULT 'false', UNIQUE(id, chattype, name) );");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + EMOJI_LIST + " ( id VARCHAR, name VARCHAR , path TEXT, UNIQUE(id) );");

       // sqLiteDatabase.close();
    }

    public void callOnCreate(){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        onCreate(msqLiteDatabase);
    }

    public String emojiPath(String ename){
        SQLiteDatabase msqLiteDatabase=this.getReadableDatabase();

        msqLiteDatabase=this.getWritableDatabase();
        Log.d("Background_ename", ename);
        String Query = "Select * from " + EMOJI_LIST + " where name = '" + ename+ "'";
        Cursor cursor = msqLiteDatabase.rawQuery(Query, null);
        cursor.moveToNext();
        String path=cursor.getString(2);
        cursor.close();
        msqLiteDatabase.close();
        Log.d("Background_ename",path);

        return path;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SINGLE_CHAT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GROUP_CHAT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CHAT_LIST_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USERS_LIST);

        onCreate(sqLiteDatabase);

    }

    public void insertEmoji(String emoji_name, String emoji_path, String id){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        //onCreate(msqLiteDatabase);
        String Query = "Select * from " + EMOJI_LIST + " where path = '" + emoji_path + "'";
        Cursor cursor= null;
        try{
            cursor = msqLiteDatabase.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
            /*msqLiteDatabase.execSQL("REPLACE INTO "+ SINGLE_CHAT_TABLE +" ( id, send_id, rcv_id, date, message, isread, deliver, type, mid, response, time, lat, long, heading, datetime, is_select "+") " +
                    "VALUES ( '" + rid + "','"+ singleChatList.getSendName()+"', '"+ singleChatList.getRecieverName() +"', '"+ singleChatList.getDate() +"' , '"+ singleChatList.getMessage() +"', '"+ singleChatList.getIsread() +"', '"+ singleChatList.getDeliver() +"', '"+ singleChatList.getType() +"'" +
                    ", '"+ singleChatList.getMid() +"', '"+ singleChatList.getResponse() +"', '"+ singleChatList.getTime() +"', '"+ singleChatList.getDocName() +"', '"+ singleChatList.getUname() +"', '"+ singleChatList.getHeading() +"', '"+ singleChatList.getTimedate() +"', '"+ singleChatList.isSelect() +"');");*/
                ContentValues values= new ContentValues();
                values.put("id", id);
                values.put("name", emoji_name);
                values.put("path", emoji_path);
                msqLiteDatabase.replace(EMOJI_LIST,null,values);
                //msqLiteDatabase.insertWithOnConflict(SINGLE_CHAT_TABLE,null,);
                Log.d("No_internet_data","inserted");
            }else{
                Log.d("No_internet_data","Already present");
            }        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        //msqLiteDatabase.close();
    }

    public void insertSingleChat(JsonModelForChat singleChatList, String rid){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        //onCreate(msqLiteDatabase);
        String Query = "Select * from " + SINGLE_CHAT_TABLE + " where mid = '" + singleChatList.getMid() + "'";
        Cursor cursor=null;
        try{
            cursor = msqLiteDatabase.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
            /*msqLiteDatabase.execSQL("REPLACE INTO "+ SINGLE_CHAT_TABLE +" ( id, send_id, rcv_id, date, message, isread, deliver, type, mid, response, time, lat, long, heading, datetime, is_select "+") " +
                    "VALUES ( '" + rid + "','"+ singleChatList.getSendName()+"', '"+ singleChatList.getRecieverName() +"', '"+ singleChatList.getDate() +"' , '"+ singleChatList.getMessage() +"', '"+ singleChatList.getIsread() +"', '"+ singleChatList.getDeliver() +"', '"+ singleChatList.getType() +"'" +
                    ", '"+ singleChatList.getMid() +"', '"+ singleChatList.getResponse() +"', '"+ singleChatList.getTime() +"', '"+ singleChatList.getDocName() +"', '"+ singleChatList.getUname() +"', '"+ singleChatList.getHeading() +"', '"+ singleChatList.getTimedate() +"', '"+ singleChatList.isSelect() +"');");*/
                ContentValues values= new ContentValues();
                values.put("id", rid);
                values.put("send_id", singleChatList.getSendName());
                values.put("rcv_id", singleChatList.getRecieverName());
                values.put("date", singleChatList.getDate());
                values.put("message", singleChatList.getMessage());
                values.put("isread", singleChatList.getIsread());
                values.put("deliver", singleChatList.getDeliver());
                values.put("type", singleChatList.getType());
                values.put("mid", singleChatList.getMid());
                values.put("response", singleChatList.getResponse());
                values.put("time", singleChatList.getTime());
                values.put("lat", singleChatList.getDocName());
                values.put("long", singleChatList.getUname());
                values.put("heading", singleChatList.getHeading());
                values.put("datetime", singleChatList.getTimedate());
                values.put("is_Select", singleChatList.isSelect());
                values.put("did", singleChatList.getDid());
                values.put("uploading", singleChatList.getUploading());
                values.put("extension", singleChatList.getExtension());
                values.put("parent", singleChatList.getParent());
                values.put("list_poistion", singleChatList.getList_position());
                msqLiteDatabase.replace(SINGLE_CHAT_TABLE,null,values);
                //msqLiteDatabase.insertWithOnConflict(SINGLE_CHAT_TABLE,null,);
                Log.d("No_internet_data","inserted");
            }else{
                Log.d("No_internet_data","Already present");
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
    }

/*
    public void insertChatList(ChatUserList chatUserList){
        Log.d("No_internet_data",chatUserList.toString());
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();

        //onCreate(msqLiteDatabase);
        if (!chatUserList.getId().equalsIgnoreCase(sd.getKeyId())){
            String Query = "Select * from " + CHAT_LIST_TABLE + " where id= '" + chatUserList.getId() + "' AND chattype = '" + chatUserList.getChattype() +"'";
            Cursor cursor=null;
            try{
                cursor = msqLiteDatabase.rawQuery(Query, null);
                if(cursor.getCount() <= 0){
                    msqLiteDatabase.execSQL("REPLACE INTO "+ CHAT_LIST_TABLE +" ( id, name , description,  lastMessage, datetime, userstatus, time , photo, dtype, message, chattype, did, admin "+") " +
                            "VALUES ( '"+ chatUserList.getId()+"','"+ chatUserList.getName()+"', '"+ chatUserList.getDescription() +"', '"+ chatUserList.getLastMessage() +"' , '"+ chatUserList.getDatetime() +"', '"+ chatUserList.getIsOnline() +"', '"+ chatUserList.getTime() +"', '"+ chatUserList.getImageUrl() +"'" +
                            ", '"+ chatUserList.getType() +"', '"+ chatUserList.getLastMessage() +"', '"+ chatUserList.getChattype() +"', '"+ chatUserList.getDid() +"', '"+ chatUserList.getAdmin() +"');");
                }else{
                    ContentValues contentValues= new ContentValues();
                    contentValues.put("did", chatUserList.getDid());
                    contentValues.put("dtype", chatUserList.getType());
                    contentValues.put("datetime", chatUserList.getDatetime());
                    contentValues.put("time", chatUserList.getTime());
                    contentValues.put("photo", chatUserList.getImageUrl());
                    contentValues.put("admin", chatUserList.getAdmin());
                    contentValues.put("message", chatUserList.getLastMessage());
                    contentValues.put("description", chatUserList.getDescription());
                    contentValues.put("lastMessage", chatUserList.getLastMessage());
                    msqLiteDatabase.update(CHAT_LIST_TABLE,contentValues,"id = '"+ chatUserList.getId()+ "' AND chattype = '" + chatUserList.getChattype() +"'",null );
                }
            }catch (Exception e){
                Log.d("EXCPTN", e.toString());
            }finally {
                if (cursor!=null){
                    cursor.close();
                }
                msqLiteDatabase.close();
            }

            if (chatUserList.getChattype().equalsIgnoreCase("indivisual")){
                updateChatListLastmessage("single", chatUserList.getId(),chatUserList.getType(), chatUserList.getLastMessage(),chatUserList.getTime(),chatUserList.getDatetime());
            }else{
                updateChatListLastmessage("group", chatUserList.getId(),chatUserList.getType(), chatUserList.getLastMessage(),chatUserList.getTime(),chatUserList.getDatetime());
            }
        }
        */
/*msqLiteDatabase.close();*//*

    }
*/

    public void insertUserList(User userList){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        msqLiteDatabase.execSQL("REPLACE INTO "+ USERS_LIST +" ( id, name , device_id,  count, datetime, image "+") " +
                "VALUES ( '"+ userList.getId()+"','"+ userList.getName()+"', '"+ userList.getDid() +"', '"+ userList.getCount() +"' , '"+ userList.getDatetime() +"', '"+ userList.getImageUrl() +"');");
        Log.d("No_internet_data","UL_inserted");
        msqLiteDatabase.close();
    }

    public void insertGroupMessages(JsonModelForChat groupChatList, String rid){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        //onCreate(msqLiteDatabase);
        String Query = "Select * from " + GROUP_CHAT_TABLE + " where mid = '" + groupChatList.getMid() + "'";
        Cursor cursor=null;
        try{
            cursor = msqLiteDatabase.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
                msqLiteDatabase.execSQL("REPLACE INTO "+ GROUP_CHAT_TABLE +" (id, send_id, rcv_id, date, message, isread, deliver, type, mid, response, time, lat, long, heading, datetime, is_select "+") " +
                        "VALUES ( '" + rid + "', '"+ groupChatList.getSendName()+"', '"+ groupChatList.getRecieverName() +"', '"+ groupChatList.getDate() +"' , '"+ groupChatList.getMessage() +"', '"+ groupChatList.getIsread() +"', '"+ groupChatList.getDeliver() +"', '"+ groupChatList.getType() +"'" +
                        ", '"+ groupChatList.getMid() +"', '"+ groupChatList.getResponse() +"', '"+ groupChatList.getTime() +"', '"+ groupChatList.getDocName() +"', '"+ groupChatList.getUname() +"', '"+ groupChatList.getHeading() +"', '"+ groupChatList.getTimedate() +"', '"+ groupChatList.isSelect() +"');");
                cursor.close();
                Log.d("No_internet_data","inserted");
            }else{
                Log.d("No_internet_data","Already present");
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }

        msqLiteDatabase.close();
    }

/*
    public JsonModelForChat insertGroupMessagesOffline(JsonModelForChat groupChatList, String rid){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        //onCreate(msqLiteDatabase);
        String Query2 = "Select * from " + GROUP_CHAT_TABLE + " where ( rcv_id = '"+ rid +"') ORDER BY date DESC LIMIT 1 ";
        Cursor cursor2=null;
        try{
            cursor2 = msqLiteDatabase.rawQuery(Query2, null);
            if (cursor2.getCount() > 0) {
                Log.d("Date", "here");
                cursor2.moveToNext();
                if (currentDate.equals(cursor2.getString(3))){
                    groupChatList.setDate("");
                }else{
                    groupChatList.setDate(currentDate);
                }
                Log.d("Date", "e"+groupChatList.getDate());
            }else{
                groupChatList.setDate(currentDate);
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor2!=null){
                cursor2.close();
            }
        }

        groupChatList.setMessage(groupChatList.getMessage().replace("'", "''"));
        groupChatList.setData(groupChatList.getData().replace("'", "''"));
        String Query = "Select * from " + GROUP_CHAT_TABLE + " where mid = '" + groupChatList.getMid() + "'";
        Cursor cursor=null;
        try{
            cursor = msqLiteDatabase.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
                msqLiteDatabase.execSQL("REPLACE INTO "+ GROUP_CHAT_TABLE+" (id, send_id, rcv_id, date, message, isread, deliver, type, mid, response, time, lat, long, heading, datetime, is_select, status, uploading, did, extension,parent, list_position, data, group_img, description, group_name "+") " +
                        "VALUES ( '" + rid + "', '"+ groupChatList.getSendName()+"', '"+ groupChatList.getRecieverName() +"', '"+ groupChatList.getDate() +"' , '"+ groupChatList.getMessage() +"', '"+ groupChatList.getIsread() +"', '"+ groupChatList.getDeliver() +"', '"+ groupChatList.getType() +"'" +
                        ", '"+ groupChatList.getMid() +"', '"+ groupChatList.getResponse() +"', '"+ groupChatList.getTime() +"', '"+ groupChatList.getDocName() +"', '"+ groupChatList.getUname() +"', '"+ groupChatList.getHeading() +"', '"+ groupChatList.getTimedate() +"', '"+
                        groupChatList.isSelect() +"', '"+ "offline" +"' , '"+ groupChatList.getUploading() +"', '"+ groupChatList.getDid()+"', '"+ groupChatList.getExtension() +"'" +
                        ", '"+ groupChatList.getParent()+"', '"+ groupChatList.getList_position() +"', '"+ groupChatList.getData() +"', '"+ groupChatList.getGroup_img() +"', '"+ groupChatList.getGroup_desc() +"', '"+ groupChatList.getGroup_name()+"');");
                cursor.close();
                Log.d("No_internet_data","GM_inserted");
                if (groupChatList.getSendName().equalsIgnoreCase(sd.getUserMobile())){

                }else{
                    Intent intent1 = new Intent("NEWGMSG");
                    intent1.putExtra("type", groupChatList.getType());
                    intent1.putExtra("thumb", groupChatList.getParent());
                    intent1.putExtra("uname", groupChatList.getUname());
                    intent1.putExtra("senderid",groupChatList.getSendName());
                    intent1.putExtra("reciverid", groupChatList.getRecieverName());
                    intent1.putExtra("message", groupChatList.getMessage());
                    intent1.putExtra("datetime", groupChatList.getTimedate());
                    intent1.putExtra("isread", groupChatList.getIsread());
                    intent1.putExtra("time", groupChatList.getTime());
                    intent1.putExtra("deliver", groupChatList.getDeliver());
                    intent1.putExtra("mid", groupChatList.getMid());
                    intent1.putExtra("date", groupChatList.getDate());
                    intent1.putExtra("chattype", "group");
                    intent1.putExtra("doc_name", groupChatList.getDocName());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
                    switch (groupChatList.getType()){
                        case "img":
                            localRecDownload.localRecDownloadMethod(groupChatList.getMessage(), LocalFileManager.image_folder_name,
                                    groupChatList.getType(),groupChatList.getMid(),groupChatList.getRecieverName(), context,"");
                            break;
                        case "video":
                            localRecDownload.localRecDownloadMethod(groupChatList.getMessage(), LocalFileManager.video_folder_name,
                                    groupChatList.getType(),groupChatList.getMid(),groupChatList.getRecieverName(), context,"");
                            break;
                        case "audio":
                            localRecDownload.localRecDownloadMethod(groupChatList.getMessage(), LocalFileManager.audio_folder_name,
                                    groupChatList.getType(),groupChatList.getMid(),groupChatList.getRecieverName(), context,"");
                            break;
                        case "pdf":
                            localRecDownload.localRecDownloadMethod(groupChatList.getMessage(), LocalFileManager.docs_folder_name,
                                    groupChatList.getType(),groupChatList.getMid(),groupChatList.getRecieverName(), context, groupChatList.getDocName());
                            break;
                    }
                }
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        updateChatListLastmessage("group",rid, groupChatList.getType(), groupChatList.getMessage(),
                groupChatList.getTime(),groupChatList.getTimedate());
        return groupChatList;
    }
*/

    public void updateChatListLastmessage(String table_name, String id, String type, String last_message,
                                          String time, String datetime) {
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        switch (type) {
            case "location":
                values.put("lastMessage","LOCATION");
                break;
            case "reply":
                values.put("lastMessage","REPLY");
                break;
            case "share":
                values.put("lastMessage","CONTACT SHARE");
                break;
            case "img":
                values.put("lastMessage", "IMAGE");
                break;
            case "audio":
                values.put("lastMessage", "AUDIO");
                break;
            case "video":
                values.put("lastMessage", "VIDEO");
                break;
            case "pdf":
                values.put("lastMessage", "DOC");
                break;
            case "emoji":
                values.put("lastMessage", "EMOJI");
                break;
            default:
                values.put("lastMessage", last_message);
                break;
        }
        values.put("time", time);
        values.put("datetime", datetime);
        if (table_name.equalsIgnoreCase("group")){
            msqLiteDatabase.update(CHAT_LIST_TABLE, values, "id= '" + id + "' AND chattype = 'group'" , null);
        }else{
            msqLiteDatabase.update(CHAT_LIST_TABLE, values, "id= '" + id + "' AND chattype = 'indivisual'" , null);
        }
        msqLiteDatabase.close();

        Intent intent1 = new Intent("LOCALRECL");
        Log.d("Background_service","No internet");
        //intent1.putExtra("key", "UPDATE");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
    }

/*
    public void updateGroupMessagesOffline(JsonModelForChat singleChatList, String rid) {
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        //onCreate(msqLiteDatabase);
        String Query = "Select * from " + GROUP_CHAT_TABLE + " where mid = '" + singleChatList.getMid() + "'";
        Cursor cursor=null;
        try{
            cursor = msqLiteDatabase.rawQuery(Query, null);
            if (cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put("uploading", singleChatList.getUploading());
                values.put("list_position", singleChatList.getList_position());
                if (singleChatList.getData()!=null && !singleChatList.getData().equalsIgnoreCase("")
                        && !singleChatList.getData().equalsIgnoreCase("null")){
                    singleChatList.setData(singleChatList.getData().replace("'", "''"));
                    values.put("data", singleChatList.getData());
                }
                if(singleChatList.getMessage()!=null && !singleChatList.getMessage().equalsIgnoreCase("")
                        && !singleChatList.getMessage().equalsIgnoreCase("null")){
                    singleChatList.setMessage(singleChatList.getMessage().replace("'", "''"));
                    values.put("message", singleChatList.getMessage());
                    Log.d("Background_service", "new message" + singleChatList.getMessage());
                }
                if (singleChatList.getParent() != null && !singleChatList.getParent().isEmpty() && !singleChatList.getParent().equalsIgnoreCase("null")) {
                    values.put("parent",singleChatList.getParent());
                }
                if (singleChatList.getHeading()!=null && !singleChatList.getHeading().isEmpty() && !singleChatList.getHeading().equalsIgnoreCase("null")){
                    values.put("heading", singleChatList.getHeading());
                }
                msqLiteDatabase.update(GROUP_CHAT_TABLE, values, "mid= '" + singleChatList.getMid() + "'", null);


                Log.d("Background_service", "updated" + singleChatList.getUploading());
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
    }
*/

    public JsonModelForChat insertSingleMessagesOffline(JsonModelForChat groupChatList, String rid){
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        //onCreate(msqLiteDatabase);
        Cursor cursor2=null;
        try{
            String Query2 = "Select * from " + SINGLE_CHAT_TABLE + " where (( rcv_id = '"+ rid +"' AND send_id= '"+ sd.getKeyId() +"' ) OR ( rcv_id = '"+ sd.getKeyId() +"' AND send_id= '"+ groupChatList.getSendName() +"' ) OR ( rcv_id = '"+ groupChatList.getSendName() +"' AND send_id= '"+ sd.getKeyId() +"' ) OR ( rcv_id = '"+ groupChatList.getSendName() +"' AND send_id= '"+ groupChatList.getRecieverName() +"' )) ORDER BY date DESC LIMIT 1 ";
            cursor2 = msqLiteDatabase.rawQuery(Query2, null);
            if (cursor2.getCount() > 0) {
                cursor2.moveToNext();
                Log.d("Date", cursor2.getString(3));
                if (currentDate.equals(cursor2.getString(3))){
                    groupChatList.setDate("");
                }else{
                    groupChatList.setDate(currentDate);
                }
                Log.d("Date", "e"+groupChatList.getDate());
            }else{
                groupChatList.setDate(currentDate);
            }

        }catch (Exception e){
            Log.d("EXCEPTION", e.toString());
        }finally {
            if (cursor2!=null){
                cursor2.close();
            }
        }
        groupChatList.setMessage(groupChatList.getMessage().replace("'", "''"));
        groupChatList.setData(groupChatList.getData().replace("'", "''"));
        String Query = "Select * from " + SINGLE_CHAT_TABLE + " where mid = '" + groupChatList.getMid() + "'";
        Cursor cursor=null;
        try{
            cursor = msqLiteDatabase.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
                msqLiteDatabase.execSQL("REPLACE INTO "+ SINGLE_CHAT_TABLE+" (id, send_id, rcv_id, date, message, isread, deliver, type, mid, response, time, lat, long, heading, datetime, is_select, status, uploading, did, extension,parent, list_position, data "+") " +
                        "VALUES ( '" + rid + "', '"+ groupChatList.getSendName()+"', '"+ groupChatList.getRecieverName() +"', '"+ groupChatList.getDate() +"' , '"+ groupChatList.getMessage() +"', '"+ groupChatList.getIsread() +"', '"+ groupChatList.getDeliver() +"', '"+ groupChatList.getType() +"'" +
                        ", '"+ groupChatList.getMid() +"', '"+ groupChatList.getResponse() +"', '"+ groupChatList.getTime() +"', '"+ groupChatList.getDocName() +"', '"+ groupChatList.getUname() +"', '"+ groupChatList.getHeading() +"', '"+ groupChatList.getTimedate() +"', '"+
                        groupChatList.isSelect() +"', '"+ "offline" +"' , '"+ groupChatList.getUploading() +"', '"+ groupChatList.getDid()+"', '"+ groupChatList.getExtension() +"'" +
                        ", '"+ groupChatList.getParent()+"', '"+ groupChatList.getList_position() +"', '"+ groupChatList.getData() +"');");
                Log.d("No_internet_data","inserted");
                Intent intent1 = new Intent("NEWSMSG");
                intent1.putExtra("type", groupChatList.getType());
                intent1.putExtra("thumb", groupChatList.getParent());
                intent1.putExtra("senderid", groupChatList.getSendName());
                intent1.putExtra("reciverid", groupChatList.getRecieverName());
                intent1.putExtra("message", groupChatList.getMessage());
                intent1.putExtra("datetime", groupChatList.getTimedate());
                intent1.putExtra("isread", groupChatList.getIsread());
                intent1.putExtra("time", groupChatList.getTime());
                intent1.putExtra("deliver", groupChatList.getDeliver());
                intent1.putExtra("mid", groupChatList.getMid());
                intent1.putExtra("date", groupChatList.getDate());
                intent1.putExtra("doc_name", groupChatList.getDocName());
                intent1.putExtra("chattype", "indivisual");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);

          /*      if (sd.getKeyId().equalsIgnoreCase(groupChatList.getRecieverName())){
                    switch (groupChatList.getType()){
                        case "img":
                            localRecDownload.localRecDownloadMethod(groupChatList.getMessage(), LocalFileManager.image_folder_name,
                                    groupChatList.getType(),groupChatList.getMid(),groupChatList.getRecieverName(), context,"");
                            break;
                        case "video":
                            localRecDownload.localRecDownloadMethod(groupChatList.getMessage(), LocalFileManager.video_folder_name,
                                    groupChatList.getType(),groupChatList.getMid(),groupChatList.getRecieverName(), context,"");
                            break;
                        case "audio":
                            localRecDownload.localRecDownloadMethod(groupChatList.getMessage(), LocalFileManager.audio_folder_name,
                                    groupChatList.getType(),groupChatList.getMid(),groupChatList.getRecieverName(), context,"");
                            break;
                        case "pdf":
                            localRecDownload.localRecDownloadMethod(groupChatList.getMessage(), LocalFileManager.docs_folder_name,
                                    groupChatList.getType(),groupChatList.getMid(),groupChatList.getRecieverName(), context,groupChatList.getDocName());
                            break;
                    }

                }*/
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        if (rid.equalsIgnoreCase(sd.getKeyId())){
            rid=groupChatList.getSendName();
        }
        updateChatListLastmessage("single",rid, groupChatList.getType(), groupChatList.getMessage(),
                groupChatList.getTime(),groupChatList.getTimedate());
        //msqLiteDatabase.close();

        return groupChatList;
    }
    public void updateSingleMessagesOffline(JsonModelForChat singleChatList, String rid) {
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        String Query = "Select * from " + SINGLE_CHAT_TABLE + " where mid = '" + singleChatList.getMid() + "'";
        Cursor cursor =null;
        try{
            cursor = msqLiteDatabase.rawQuery(Query, null);
            if (cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put("uploading", singleChatList.getUploading());
                values.put("list_position", singleChatList.getList_position());
                if (singleChatList.getData()!=null && !singleChatList.getData().equalsIgnoreCase("")
                        && !singleChatList.getData().equalsIgnoreCase("null")){
                    singleChatList.setData(singleChatList.getData().replace("'", "''"));
                    values.put("data", singleChatList.getData());
                }
                if(singleChatList.getMessage()!=null && !singleChatList.getMessage().equalsIgnoreCase("")
                        && !singleChatList.getMessage().equalsIgnoreCase("null")){
                    singleChatList.setMessage(singleChatList.getMessage().replace("'", "''"));
                    values.put("message", singleChatList.getMessage());
                    Log.d("Background_service", "new message" + singleChatList.getMessage());
                }
                if (singleChatList.getParent() != null && !singleChatList.getParent().isEmpty() && !singleChatList.getParent().equalsIgnoreCase("null")) {
                    values.put("parent",singleChatList.getParent());
                }
                if (singleChatList.getHeading()!=null && !singleChatList.getHeading().isEmpty() && !singleChatList.getHeading().equalsIgnoreCase("null")){
                    values.put("heading", singleChatList.getHeading());
                }
                msqLiteDatabase.update(SINGLE_CHAT_TABLE, values, "mid= '" + singleChatList.getMid() + "'", null);
                Log.d("Background_service", "updated" + singleChatList.getUploading());

                //msqLiteDatabase.close();
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
    }

    public ArrayList<JsonModelForChat> sendSingleMessageMid(String mid){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        //onCreate(msqLiteDatabase);
        String Query = "Select * from " + SINGLE_CHAT_TABLE + " where mid = '" + mid + "'";
        Cursor cursor = null;
        try{
            cursor = msqLiteDatabase.rawQuery(Query, null);
            ArrayList<JsonModelForChat> chats= new ArrayList<JsonModelForChat>();
            JsonModelForChat jsonModelForChat;
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                jsonModelForChat= new JsonModelForChat();
                jsonModelForChat.setId(cursor.getString(0));
                jsonModelForChat.setSendName(cursor.getString(1));
                jsonModelForChat.setRecieverName(cursor.getString(2));
                jsonModelForChat.setDate(cursor.getString(3));
                jsonModelForChat.setMessage(cursor.getString(4));
                jsonModelForChat.setIsread(cursor.getString(5));
                jsonModelForChat.setDeliver(cursor.getString(6));
                jsonModelForChat.setType(cursor.getString(7));
                jsonModelForChat.setResponse(cursor.getString(8));
                jsonModelForChat.setTime(cursor.getString(9));
                jsonModelForChat.setMid(cursor.getString(10));
                jsonModelForChat.setDocName(cursor.getString(11));
                jsonModelForChat.setUname(cursor.getString(12));
                jsonModelForChat.setHeading(cursor.getString(13));
                jsonModelForChat.setTimedate(cursor.getString(14));
                jsonModelForChat.setSelect(Boolean.parseBoolean(cursor.getString(15)));
                jsonModelForChat.setStatus(cursor.getString(16));
                jsonModelForChat.setUploading(cursor.getString(17));
                jsonModelForChat.setDid(cursor.getString(18));
                jsonModelForChat.setExtension(cursor.getString(19));
                jsonModelForChat.setParent(cursor.getString(20));
                jsonModelForChat.setList_position(cursor.getInt(21));
                jsonModelForChat.setData(cursor.getString(22));
                chats.add(jsonModelForChat);
                return chats;
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }

        //msqLiteDatabase.close();
        return null;
    }

/*
    public ArrayList<JsonModelForChat> sendSingleGroupMessageMid(String mid){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        //onCreate(msqLiteDatabase);
        String Query = "Select * from " + GROUP_CHAT_TABLE + " where mid = '" + mid + "'";
        Cursor cursor = null;
        try{
            cursor = msqLiteDatabase.rawQuery(Query, null);
            ArrayList<JsonModelForChat> chats= new ArrayList<JsonModelForChat>();
            JsonModelForChat jsonModelForChat;
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                jsonModelForChat= new JsonModelForChat();
                jsonModelForChat.setId(cursor.getString(0));
                jsonModelForChat.setSendName(cursor.getString(1));
                jsonModelForChat.setRecieverName(cursor.getString(2));
                jsonModelForChat.setDate(cursor.getString(3));
                jsonModelForChat.setMessage(cursor.getString(4));
                jsonModelForChat.setIsread(cursor.getString(5));
                jsonModelForChat.setDeliver(cursor.getString(6));
                jsonModelForChat.setType(cursor.getString(7));
                jsonModelForChat.setResponse(cursor.getString(8));
                jsonModelForChat.setTime(cursor.getString(9));
                jsonModelForChat.setMid(cursor.getString(10));
                jsonModelForChat.setDocName(cursor.getString(11));
                jsonModelForChat.setUname(cursor.getString(12));
                jsonModelForChat.setHeading(cursor.getString(13));
                jsonModelForChat.setTimedate(cursor.getString(14));
                jsonModelForChat.setSelect(Boolean.parseBoolean(cursor.getString(15)));
                jsonModelForChat.setStatus(cursor.getString(16));
                jsonModelForChat.setUploading(cursor.getString(17));
                jsonModelForChat.setDid(cursor.getString(18));
                jsonModelForChat.setExtension(cursor.getString(19));
                jsonModelForChat.setParent(cursor.getString(20));
                jsonModelForChat.setList_position(cursor.getInt(21));
                jsonModelForChat.setData(cursor.getString(22));
                jsonModelForChat.setGroup_img(cursor.getString(23));
                jsonModelForChat.setGroup_desc(cursor.getString(24));
                jsonModelForChat.setGroup_name(cursor.getString(25));
                chats.add(jsonModelForChat);
            }
            return chats;
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        return null;
    }
*/



/*
    public void deleteGroupChat(String mid, int method, ArrayList<JsonModelForChat> multiselect_list){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        StringTokenizer tokenizer = new StringTokenizer(mid, ",");
        String rid = null,sid;
        Cursor cursor=null;
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + GROUP_CHAT_TABLE+ " WHERE mid = " + tokenizer.nextToken() + " " , null);
            if (cursor.getCount()>0){
                cursor.moveToNext();
                rid= cursor.getString(2);
                sid= cursor.getString(1);
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }
        tokenizer = new StringTokenizer(mid, ",");
        if(method==0){
            while (tokenizer.hasMoreTokens()) {
                msqLiteDatabase.execSQL("DELETE FROM "+ GROUP_CHAT_TABLE + " WHERE mid "+ " = " + tokenizer.nextToken()+ "");
            }
        }else{
            for (JsonModelForChat jsonModelForChat:multiselect_list){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String timeStamp2 =jsonModelForChat.getTimedate();
                long diffMinutes = 3;
                try {
                    Date d1= format.parse(timeStamp);
                    Date d2= format.parse(timeStamp2);
                    long diff = d1.getTime() - d2.getTime();
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    diffMinutes= diff / (60 * 1000) % 60;
                    if (diffMinutes<2 && diffDays==0){
                        Log.d("Background_service","Bomb delete succedded"+diffMinutes+diffDays);
                        msqLiteDatabase.execSQL("DELETE FROM "+ GROUP_CHAT_TABLE + " WHERE mid"+ " = '" + jsonModelForChat.getMid()+ "'");
                    }else{
                        Log.d("Background_service","Bomb delete canceled"+diffMinutes+diffDays);
                    }
                } catch (ParseException e) {
                    Log.d("Background_service","Bomb delete canceled"+e.getMessage());
                }

            }
        }
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + GROUP_CHAT_TABLE+" WHERE " + " ( rcv_id = '"+ rid +"')" +
                    " ORDER BY datetime DESC LIMIT 1" , null);
            if (cursor.getCount()>0){
                cursor.moveToNext();
                updateChatListLastmessage("group",rid,cursor.getString(7),cursor.getString(4),
                        cursor.getString(9),cursor.getString(14));
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }
        msqLiteDatabase.close();
        //msqLiteDatabase.close();
    }
*/
    public void deleteSinglechat(String mid, int method, ArrayList<JsonModelForChat> multiselect_list){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        StringTokenizer tokenizer = new StringTokenizer(mid, ",");
        String rid = null,sid;
        Cursor cursor=null;
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + SINGLE_CHAT_TABLE+ " WHERE mid = " + tokenizer.nextToken() + " " , null);
            if (cursor.getCount()>0){
                cursor.moveToNext();
                rid= cursor.getString(2);
                sid= cursor.getString(1);
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }
        tokenizer = new StringTokenizer(mid, ",");
        if(method==0){

            while (tokenizer.hasMoreTokens()) {
                msqLiteDatabase.execSQL("DELETE FROM "+ SINGLE_CHAT_TABLE + " WHERE mid "+ " = " + tokenizer.nextToken()+ "");
            }



        }else{
            for (JsonModelForChat jsonModelForChat:multiselect_list){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String timeStamp2 =jsonModelForChat.getTimedate();
                long diffMinutes = 3;
                try {
                    Date d1= format.parse(timeStamp);
                    Date d2= format.parse(timeStamp2);
                    long diff = d1.getTime() - d2.getTime();
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    diffMinutes= diff / (60 * 1000) % 60;
                    if (diffMinutes<=2 && diffDays==0){
                        Log.d("Background_service","Bomb delete succedded"+diffMinutes+diffDays);
                        msqLiteDatabase.execSQL("DELETE FROM "+ SINGLE_CHAT_TABLE + " WHERE mid"+ " = '" + jsonModelForChat.getMid()+ "'");
                    }else{
                        Log.d("Background_service","Bomb delete canceled"+diffMinutes+diffDays);
                    }
                } catch (ParseException e) {
                    Log.d("Background_service","Bomb delete canceled"+e.getMessage());
                }

            }
        }
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + SINGLE_CHAT_TABLE+" WHERE " + " ( rcv_id = '"+ rid +"' AND send_id= '"+ sd.getKeyId() +"' )" +
                    " OR ( send_id = '"+ rid +"' AND rcv_id= '"+ sd.getKeyId() +"') ORDER BY datetime DESC LIMIT 1" , null);
            if (cursor.getCount()>0){
                cursor.moveToNext();
                if (cursor.getString(2).equalsIgnoreCase(sd.getKeyId())){
                    rid=cursor.getString(1);
                }else{
                    rid=cursor.getString(2);
                }
                updateChatListLastmessage("single",rid,cursor.getString(7),cursor.getString(4),
                        cursor.getString(9),cursor.getString(14));
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
    }

    public void deleteSinglechat(String mid, int method){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        StringTokenizer tokenizer = new StringTokenizer(mid,",");
        if(method==0){
            while (tokenizer.hasMoreTokens()) {
                msqLiteDatabase.execSQL("DELETE FROM "+ SINGLE_CHAT_TABLE + " WHERE mid " + " = '''" + tokenizer.nextToken()+ "'");
            }
        }
        msqLiteDatabase.close();
    }

    public void deleteGroupChat(String mid, int method){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        StringTokenizer tokenizer = new StringTokenizer(mid, ",");

        if(method==0){
            while (tokenizer.hasMoreTokens()) {
                msqLiteDatabase.execSQL("DELETE FROM "+ GROUP_CHAT_TABLE+ " WHERE mid " + " = '''" + tokenizer.nextToken()+ "'");
            }
        }
        msqLiteDatabase.close();
    }

    public void deleteChatList(String id, String chattype){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        msqLiteDatabase.execSQL("DELETE FROM "+ CHAT_LIST_TABLE + " WHERE id "+ " = '" + id + "' AND chattype "+ " = '" + chattype +"'");
        //msqLiteDatabase.close();
        if (chattype.equalsIgnoreCase("group")){
            msqLiteDatabase.execSQL("DELETE FROM "+ GROUP_CHAT_TABLE + " WHERE " + " rcv_id = '"+ id +"'");
        }else{
            msqLiteDatabase.execSQL("DELETE FROM "+ SINGLE_CHAT_TABLE + " WHERE " + " ( rcv_id = '"+ id +"' AND send_id= '"+ sd.getKeyId() +"' )" +
                    " OR ( send_id = '"+ id +"' AND rcv_id= '"+ sd.getKeyId() +"')");
        }
        msqLiteDatabase.close();
    }
   /* public ArrayList<ChatUserList> getAllChatList(){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        ChatUserList chatUserList;
        ArrayList<ChatUserList> chats= new ArrayList<ChatUserList>();

        Cursor cursor= null;
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + CHAT_LIST_TABLE + " ORDER BY datetime DESC ", null);
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    chatUserList= new ChatUserList();
                    chatUserList.setId(cursor.getString(0));
                    chatUserList.setName(cursor.getString(1));
                    chatUserList.setDescription(cursor.getString(2));
                    chatUserList.setLastMessage(cursor.getString(3));
                    chatUserList.setDatetime(cursor.getString(4));
                    chatUserList.setIsOnline(cursor.getString(5));
                    chatUserList.setTime(cursor.getString(6));
                    chatUserList.setImageUrl(cursor.getString(7));
                    chatUserList.setType(cursor.getString(8));
                    chatUserList.setData(cursor.getString(9));
                    chatUserList.setChattype(cursor.getString(10));
                    chatUserList.setDid(cursor.getString(11));
                    chatUserList.setAdmin(cursor.getString(12));
                    chatUserList.setMute(cursor.getString(13));
                    chats.add(chatUserList);
                }
            }
        }catch (Exception e){
            Log.d("EXCPTN",e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        return chats;
    }
*/
    public ArrayList<User> getAllUsersList(){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=null;
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + USERS_LIST, null);
            Log.d("CURSOR",cursor.toString());
            ArrayList<User> users= new ArrayList<User>();
            User userList;
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    userList= new User();
                    userList.setId(cursor.getString(0));
                    userList.setName(cursor.getString(1));
                    userList.setDid(cursor.getString(2));
                    userList.setCount(cursor.getString(3));
                    userList.setDatetime(cursor.getString(4));
                    userList.setImageUrl(cursor.getString(5));
                    users.add(userList);
                }
            }
            return users;
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        return null;
    }

  /*  public ArrayList<JsonModelForChat> getAllGroupMessage(String id){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=null;
        ArrayList<JsonModelForChat> chats= new ArrayList<JsonModelForChat>();
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + GROUP_CHAT_TABLE + " WHERE rcv_id = '" + id + "' " , null);
            JsonModelForChat jsonModelForChat;
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    jsonModelForChat= new JsonModelForChat();
                    jsonModelForChat.setId(cursor.getString(0));
                    jsonModelForChat.setSendName(cursor.getString(1));
                    jsonModelForChat.setRecieverName(cursor.getString(2));
                    String date = dateConverter(cursor.getString(14));
                    jsonModelForChat.setDate(cursor.getString(3));
                    jsonModelForChat.setMessage(cursor.getString(4));
                    jsonModelForChat.setIsread(cursor.getString(5));
                    jsonModelForChat.setDeliver(cursor.getString(6));
                    jsonModelForChat.setType(cursor.getString(7));
                    jsonModelForChat.setResponse(cursor.getString(8));
                    jsonModelForChat.setTime(cursor.getString(9));
                    jsonModelForChat.setMid(cursor.getString(10));
                    jsonModelForChat.setDocName(cursor.getString(11));
                    jsonModelForChat.setUname(cursor.getString(12));
                    jsonModelForChat.setHeading(cursor.getString(13));
                    jsonModelForChat.setTimedate(cursor.getString(14));
                    jsonModelForChat.setSelect(Boolean.parseBoolean(cursor.getString(15)));
                    jsonModelForChat.setStatus(cursor.getString(16));
                    jsonModelForChat.setUploading(cursor.getString(17));
                    jsonModelForChat.setDid(cursor.getString(18));
                    jsonModelForChat.setExtension(cursor.getString(19));
                    jsonModelForChat.setParent(cursor.getString(20));
                    jsonModelForChat.setList_position(cursor.getInt(21));
                    jsonModelForChat.setData(cursor.getString(22));
                    jsonModelForChat.setGroup_img(cursor.getString(23));
                    jsonModelForChat.setGroup_desc(cursor.getString(24));
                    jsonModelForChat.setGroup_name(cursor.getString(25));
                    chats.add(jsonModelForChat);
                }
            }
            return chats;
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        return chats;
    }

    public ArrayList<JsonModelForChat> getAllGroupMessageImages(String id){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=null;
        ArrayList<JsonModelForChat> chats= new ArrayList<JsonModelForChat>();
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + GROUP_CHAT_TABLE +" WHERE rcv_id = '" + id +"' AND type = 'img'", null);
            JsonModelForChat jsonModelForChat;
            Log.d("CURSOR",cursor.toString()+id);
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    jsonModelForChat= new JsonModelForChat();
                    jsonModelForChat.setId(cursor.getString(0));
                    jsonModelForChat.setSendName(cursor.getString(1));
                    jsonModelForChat.setRecieverName(cursor.getString(2));
                    String date = dateConverter(cursor.getString(14));
                    if (date.equalsIgnoreCase(firstDate)) {
                        firstDate = "";
                    } else {
                        date = firstDate;

                    }
                    jsonModelForChat.setDate(date);
                    jsonModelForChat.setMessage(cursor.getString(4));
                    jsonModelForChat.setIsread(cursor.getString(5));
                    jsonModelForChat.setDeliver(cursor.getString(6));
                    jsonModelForChat.setType(cursor.getString(7));
                    jsonModelForChat.setResponse(cursor.getString(8));
                    jsonModelForChat.setTime(cursor.getString(9));
                    jsonModelForChat.setMid(cursor.getString(10));
                    jsonModelForChat.setDocName(cursor.getString(11));
                    jsonModelForChat.setUname(cursor.getString(12));
                    jsonModelForChat.setHeading(cursor.getString(13));
                    jsonModelForChat.setTimedate(cursor.getString(14));
                    jsonModelForChat.setSelect(Boolean.parseBoolean(cursor.getString(15)));
                    jsonModelForChat.setStatus(cursor.getString(16));
                    jsonModelForChat.setUploading(cursor.getString(17));
                    jsonModelForChat.setDid(cursor.getString(18));
                    jsonModelForChat.setExtension(cursor.getString(19));
                    jsonModelForChat.setParent(cursor.getString(20));
                    jsonModelForChat.setList_position(cursor.getInt(21));
                    jsonModelForChat.setData(cursor.getString(22));
                    jsonModelForChat.setGroup_img(cursor.getString(23));
                    jsonModelForChat.setGroup_desc(cursor.getString(24));
                    jsonModelForChat.setGroup_name(cursor.getString(25));
                    chats.add(jsonModelForChat);
                }
                return chats;
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        return chats;
    }

    public ArrayList<JsonModelForChat> getAllGroupMessageVideos(String id){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=null;
        ArrayList<JsonModelForChat> chats= new ArrayList<JsonModelForChat>();
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + GROUP_CHAT_TABLE +" WHERE rcv_id = '" + id +"' AND type = 'video'", null);
            JsonModelForChat jsonModelForChat;
            Log.d("CURSOR",cursor.toString()+id);
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    jsonModelForChat= new JsonModelForChat();
                    jsonModelForChat.setId(cursor.getString(0));
                    jsonModelForChat.setSendName(cursor.getString(1));
                    jsonModelForChat.setRecieverName(cursor.getString(2));
                    String date = dateConverter(cursor.getString(14));
                    if (date.equalsIgnoreCase(firstDate)) {
                        firstDate = "";
                    } else {
                        date = firstDate;

                    }
                    jsonModelForChat.setDate(date);
                    jsonModelForChat.setMessage(cursor.getString(4));
                    jsonModelForChat.setIsread(cursor.getString(5));
                    jsonModelForChat.setDeliver(cursor.getString(6));
                    jsonModelForChat.setType(cursor.getString(7));
                    jsonModelForChat.setResponse(cursor.getString(8));
                    jsonModelForChat.setTime(cursor.getString(9));
                    jsonModelForChat.setMid(cursor.getString(10));
                    jsonModelForChat.setDocName(cursor.getString(11));
                    jsonModelForChat.setUname(cursor.getString(12));
                    jsonModelForChat.setHeading(cursor.getString(13));
                    jsonModelForChat.setTimedate(cursor.getString(14));
                    jsonModelForChat.setSelect(Boolean.parseBoolean(cursor.getString(15)));
                    jsonModelForChat.setStatus(cursor.getString(16));
                    jsonModelForChat.setUploading(cursor.getString(17));
                    jsonModelForChat.setDid(cursor.getString(18));
                    jsonModelForChat.setExtension(cursor.getString(19));
                    jsonModelForChat.setParent(cursor.getString(20));
                    jsonModelForChat.setList_position(cursor.getInt(21));
                    jsonModelForChat.setData(cursor.getString(22));
                    jsonModelForChat.setGroup_img(cursor.getString(23));
                    jsonModelForChat.setGroup_desc(cursor.getString(24));
                    jsonModelForChat.setGroup_name(cursor.getString(25));
                    chats.add(jsonModelForChat);
                }
                return chats;
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        return chats;
    }

    public ArrayList<JsonModelForChat> getAllGroupChatOffline(){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=null;
        ArrayList<JsonModelForChat> chats= new ArrayList<JsonModelForChat>();
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + GROUP_CHAT_TABLE + " WHERE uploading = '"+ "true" +"'", null);
            JsonModelForChat jsonModelForChat;
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    jsonModelForChat= new JsonModelForChat();
                    jsonModelForChat.setId(cursor.getString(0));
                    jsonModelForChat.setSendName(cursor.getString(1));
                    jsonModelForChat.setRecieverName(cursor.getString(2));
                    jsonModelForChat.setDate(cursor.getString(3));
                    jsonModelForChat.setMessage(cursor.getString(4));
                    jsonModelForChat.setIsread(cursor.getString(5));
                    jsonModelForChat.setDeliver(cursor.getString(6));
                    jsonModelForChat.setType(cursor.getString(7));
                    jsonModelForChat.setResponse(cursor.getString(8));
                    jsonModelForChat.setTime(cursor.getString(9));
                    jsonModelForChat.setMid(cursor.getString(10));
                    jsonModelForChat.setDocName(cursor.getString(11));
                    jsonModelForChat.setUname(cursor.getString(12));
                    jsonModelForChat.setHeading(cursor.getString(13));
                    jsonModelForChat.setTimedate(cursor.getString(14));
                    jsonModelForChat.setSelect(Boolean.parseBoolean(cursor.getString(15)));
                    jsonModelForChat.setStatus(cursor.getString(16));
                    jsonModelForChat.setUploading(cursor.getString(17));
                    jsonModelForChat.setDid(cursor.getString(18));
                    jsonModelForChat.setExtension(cursor.getString(19));
                    jsonModelForChat.setParent(cursor.getString(20));
                    jsonModelForChat.setList_position(cursor.getInt(21));
                    jsonModelForChat.setData(cursor.getString(22));
                    jsonModelForChat.setGroup_img(cursor.getString(23));
                    jsonModelForChat.setGroup_desc(cursor.getString(24));
                    jsonModelForChat.setGroup_name(cursor.getString(25));
                    chats.add(jsonModelForChat);
                }
                return chats;
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        return chats;
    }

    public ArrayList<JsonModelForChat> getAllGroupMessageImagesAndVideos(String id){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=null;
        ArrayList<JsonModelForChat> chats= new ArrayList<JsonModelForChat>();
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + GROUP_CHAT_TABLE +" WHERE rcv_id = '" + id +"' AND ( type = 'img' OR type ='video' ) ", null);
            JsonModelForChat jsonModelForChat;
            Log.d("CURSOR",cursor.toString()+id);
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    jsonModelForChat= new JsonModelForChat();
                    jsonModelForChat.setId(cursor.getString(0));
                    jsonModelForChat.setSendName(cursor.getString(1));
                    jsonModelForChat.setRecieverName(cursor.getString(2));
                    String date = dateConverter(cursor.getString(14));
                    if (date.equalsIgnoreCase(firstDate)) {
                        firstDate = "";
                    } else {
                        date = firstDate;

                    }
                    jsonModelForChat.setDate(date);
                    jsonModelForChat.setMessage(cursor.getString(4));
                    jsonModelForChat.setIsread(cursor.getString(5));
                    jsonModelForChat.setDeliver(cursor.getString(6));
                    jsonModelForChat.setType(cursor.getString(7));
                    jsonModelForChat.setResponse(cursor.getString(8));
                    jsonModelForChat.setTime(cursor.getString(9));
                    jsonModelForChat.setMid(cursor.getString(10));
                    jsonModelForChat.setDocName(cursor.getString(11));
                    jsonModelForChat.setUname(cursor.getString(12));
                    jsonModelForChat.setHeading(cursor.getString(13));
                    jsonModelForChat.setTimedate(cursor.getString(14));
                    jsonModelForChat.setSelect(Boolean.parseBoolean(cursor.getString(15)));
                    jsonModelForChat.setStatus(cursor.getString(16));
                    jsonModelForChat.setUploading(cursor.getString(17));
                    jsonModelForChat.setDid(cursor.getString(18));
                    jsonModelForChat.setExtension(cursor.getString(19));
                    jsonModelForChat.setParent(cursor.getString(20));
                    jsonModelForChat.setList_position(cursor.getInt(21));
                    jsonModelForChat.setData(cursor.getString(22));
                    jsonModelForChat.setGroup_img(cursor.getString(23));
                    jsonModelForChat.setGroup_desc(cursor.getString(24));
                    jsonModelForChat.setGroup_name(cursor.getString(25));
                    chats.add(jsonModelForChat);
                }
                return chats;
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        return chats;
    }
*/

    public ArrayList<JsonModelForChat> getAllSingleChat(String id){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=null;
        ArrayList<JsonModelForChat> chats= new ArrayList<JsonModelForChat>();
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + SINGLE_CHAT_TABLE + " WHERE " + " ( rcv_id = '"+ id +"' AND send_id= '"+ sd.getKeyId() +"' )" +
                    " OR ( send_id = '"+ id +"' AND rcv_id= '"+ sd.getKeyId() +"' )", null);
            JsonModelForChat jsonModelForChat;
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    jsonModelForChat= new JsonModelForChat();
                    jsonModelForChat.setId(cursor.getString(0));
                    jsonModelForChat.setSendName(cursor.getString(1));
                    jsonModelForChat.setRecieverName(cursor.getString(2));
                    //String date = dateConverter(cursor.getString(14));

                    jsonModelForChat.setDate(cursor.getString(3));
                    jsonModelForChat.setMessage(cursor.getString(4));
                    jsonModelForChat.setIsread(cursor.getString(5));
                    jsonModelForChat.setDeliver(cursor.getString(6));
                    jsonModelForChat.setType(cursor.getString(7));
                    jsonModelForChat.setResponse(cursor.getString(8));
                    jsonModelForChat.setTime(cursor.getString(9));
                    jsonModelForChat.setMid(cursor.getString(10));
                    jsonModelForChat.setDocName(cursor.getString(11));
                    jsonModelForChat.setUname(cursor.getString(12));
                    jsonModelForChat.setHeading(cursor.getString(13));
                    jsonModelForChat.setTimedate(cursor.getString(14));
                    jsonModelForChat.setSelect(Boolean.parseBoolean(cursor.getString(15)));
                    jsonModelForChat.setStatus(cursor.getString(16));
                    jsonModelForChat.setUploading(cursor.getString(17));
                    jsonModelForChat.setDid(cursor.getString(18));
                    jsonModelForChat.setExtension(cursor.getString(19));
                    jsonModelForChat.setParent(cursor.getString(20));
                    jsonModelForChat.setList_position(cursor.getInt(21));
                    jsonModelForChat.setData(cursor.getString(22));
                    chats.add(jsonModelForChat);
                }
                return chats;
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        return chats;
    }

    public ArrayList<JsonModelForChat> checkSingleMessagesDownload(){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        //onCreate(msqLiteDatabase);
        String Query = "Select * from " + SINGLE_CHAT_TABLE + " where (type = 'img' OR " +
                "type = 'video' OR type = 'audio' OR type = 'pdf') AND (list_position = '2' OR list_position = '0')";
        Cursor cursor=null;
        ArrayList<JsonModelForChat> chats= new ArrayList<JsonModelForChat>();
        try{
            cursor = msqLiteDatabase.rawQuery(Query, null);
            JsonModelForChat jsonModelForChat= new JsonModelForChat();
            if(cursor.getCount() <= 0){
                //msqLiteDatabase.close();
            }else{
                cursor.moveToNext();
                jsonModelForChat.setId(cursor.getString(0));
                jsonModelForChat.setSendName(cursor.getString(1));
                jsonModelForChat.setRecieverName(cursor.getString(2));
                jsonModelForChat.setDate(cursor.getString(3));
                jsonModelForChat.setMessage(cursor.getString(4));
                jsonModelForChat.setIsread(cursor.getString(5));
                jsonModelForChat.setDeliver(cursor.getString(6));
                jsonModelForChat.setType(cursor.getString(7));
                jsonModelForChat.setResponse(cursor.getString(8));
                jsonModelForChat.setTime(cursor.getString(9));
                jsonModelForChat.setMid(cursor.getString(10));
                jsonModelForChat.setDocName(cursor.getString(11));
                jsonModelForChat.setUname(cursor.getString(12));
                jsonModelForChat.setHeading(cursor.getString(13));
                jsonModelForChat.setTimedate(cursor.getString(14));
                jsonModelForChat.setSelect(Boolean.parseBoolean(cursor.getString(15)));
                jsonModelForChat.setStatus(cursor.getString(16));
                jsonModelForChat.setUploading(cursor.getString(17));
                jsonModelForChat.setDid(cursor.getString(18));
                jsonModelForChat.setExtension(cursor.getString(19));
                jsonModelForChat.setParent(cursor.getString(20));
                jsonModelForChat.setList_position(cursor.getInt(21));
                jsonModelForChat.setData(cursor.getString(22));
                chats.add(jsonModelForChat);
            }
            return chats;
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        return chats;
    }

    public ArrayList<JsonModelForChat> getAllSingleChatImagesAndVideo(String id){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        ArrayList<JsonModelForChat> chats= new ArrayList<JsonModelForChat>();
        Cursor cursor=null;
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + SINGLE_CHAT_TABLE + " WHERE (( rcv_id = '"+ id +"' AND send_id= '"+ sd.getKeyId() + "' ) OR ( send_id= '"+ id +"' AND rcv_id= '"+ sd.getKeyId() + "' )) AND ( type = 'img' OR type= 'video') ", null);
            JsonModelForChat jsonModelForChat;
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    jsonModelForChat= new JsonModelForChat();
                    jsonModelForChat.setId(cursor.getString(0));
                    jsonModelForChat.setSendName(cursor.getString(1));
                    jsonModelForChat.setRecieverName(cursor.getString(2));
                    String date = dateConverter(cursor.getString(14));
                    if (date.equalsIgnoreCase(firstDate)) {
                        firstDate = "";
                    } else {
                        date = firstDate;

                    }
                    jsonModelForChat.setDate(date);
                    jsonModelForChat.setMessage(cursor.getString(4));
                    jsonModelForChat.setIsread(cursor.getString(5));
                    jsonModelForChat.setDeliver(cursor.getString(6));
                    jsonModelForChat.setType(cursor.getString(7));
                    jsonModelForChat.setResponse(cursor.getString(8));
                    jsonModelForChat.setTime(cursor.getString(9));
                    jsonModelForChat.setMid(cursor.getString(10));
                    jsonModelForChat.setDocName(cursor.getString(11));
                    jsonModelForChat.setUname(cursor.getString(12));
                    jsonModelForChat.setHeading(cursor.getString(13));
                    jsonModelForChat.setTimedate(cursor.getString(14));
                    jsonModelForChat.setSelect(Boolean.parseBoolean(cursor.getString(15)));
                    jsonModelForChat.setStatus(cursor.getString(16));
                    jsonModelForChat.setUploading(cursor.getString(17));
                    jsonModelForChat.setDid(cursor.getString(18));
                    jsonModelForChat.setExtension(cursor.getString(19));
                    jsonModelForChat.setParent(cursor.getString(20));
                    jsonModelForChat.setList_position(cursor.getInt(21));
                    jsonModelForChat.setData(cursor.getString(22));
                    chats.add(jsonModelForChat);
                }
                return chats;
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        return chats;
    }


    public ArrayList<JsonModelForChat> getAllSingleChatVideo(String id){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        ArrayList<JsonModelForChat> chats= new ArrayList<JsonModelForChat>();
        Cursor cursor=null;
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + SINGLE_CHAT_TABLE + " WHERE (( rcv_id = '"+ id +"' AND send_id= '"+ sd.getKeyId() + "' ) OR ( send_id= '"+ id +"' AND rcv_id= '"+ sd.getKeyId() + "' )) AND type = 'video' ", null);
            JsonModelForChat jsonModelForChat;
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    jsonModelForChat= new JsonModelForChat();
                    jsonModelForChat.setId(cursor.getString(0));
                    jsonModelForChat.setSendName(cursor.getString(1));
                    jsonModelForChat.setRecieverName(cursor.getString(2));
                    String date = dateConverter(cursor.getString(14));
                    if (date.equalsIgnoreCase(firstDate)) {
                        firstDate = "";
                    } else {
                        date = firstDate;

                    }
                    jsonModelForChat.setDate(date);
                    jsonModelForChat.setMessage(cursor.getString(4));
                    jsonModelForChat.setIsread(cursor.getString(5));
                    jsonModelForChat.setDeliver(cursor.getString(6));
                    jsonModelForChat.setType(cursor.getString(7));
                    jsonModelForChat.setResponse(cursor.getString(8));
                    jsonModelForChat.setTime(cursor.getString(9));
                    jsonModelForChat.setMid(cursor.getString(10));
                    jsonModelForChat.setDocName(cursor.getString(11));
                    jsonModelForChat.setUname(cursor.getString(12));
                    jsonModelForChat.setHeading(cursor.getString(13));
                    jsonModelForChat.setTimedate(cursor.getString(14));
                    jsonModelForChat.setSelect(Boolean.parseBoolean(cursor.getString(15)));
                    jsonModelForChat.setStatus(cursor.getString(16));
                    jsonModelForChat.setUploading(cursor.getString(17));
                    jsonModelForChat.setDid(cursor.getString(18));
                    jsonModelForChat.setExtension(cursor.getString(19));
                    jsonModelForChat.setParent(cursor.getString(20));
                    jsonModelForChat.setList_position(cursor.getInt(21));
                    jsonModelForChat.setData(cursor.getString(22));
                    chats.add(jsonModelForChat);
                }
                return chats;
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        return chats;
    }

    public ArrayList<JsonModelForChat> getAllSingleChatImages(String id){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        ArrayList<JsonModelForChat> chats= new ArrayList<JsonModelForChat>();
        Cursor cursor=null;
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + SINGLE_CHAT_TABLE + " WHERE (( rcv_id = '"+ id +"' AND send_id= '"+ sd.getKeyId() + "' ) OR ( send_id= '"+ id +"' AND rcv_id= '"+ sd.getKeyId() + "' )) AND type = 'img' ", null);
            JsonModelForChat jsonModelForChat;
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    jsonModelForChat= new JsonModelForChat();
                    jsonModelForChat.setId(cursor.getString(0));
                    jsonModelForChat.setSendName(cursor.getString(1));
                    jsonModelForChat.setRecieverName(cursor.getString(2));
                    String date = dateConverter(cursor.getString(14));
                    if (date.equalsIgnoreCase(firstDate)) {
                        firstDate = "";
                    } else {
                        date = firstDate;

                    }
                    jsonModelForChat.setDate(date);
                    jsonModelForChat.setMessage(cursor.getString(4));
                    jsonModelForChat.setIsread(cursor.getString(5));
                    jsonModelForChat.setDeliver(cursor.getString(6));
                    jsonModelForChat.setType(cursor.getString(7));
                    jsonModelForChat.setResponse(cursor.getString(8));
                    jsonModelForChat.setTime(cursor.getString(9));
                    jsonModelForChat.setMid(cursor.getString(10));
                    jsonModelForChat.setDocName(cursor.getString(11));
                    jsonModelForChat.setUname(cursor.getString(12));
                    jsonModelForChat.setHeading(cursor.getString(13));
                    jsonModelForChat.setTimedate(cursor.getString(14));
                    jsonModelForChat.setSelect(Boolean.parseBoolean(cursor.getString(15)));
                    jsonModelForChat.setStatus(cursor.getString(16));
                    jsonModelForChat.setUploading(cursor.getString(17));
                    jsonModelForChat.setDid(cursor.getString(18));
                    jsonModelForChat.setExtension(cursor.getString(19));
                    jsonModelForChat.setParent(cursor.getString(20));
                    jsonModelForChat.setList_position(cursor.getInt(21));
                    jsonModelForChat.setData(cursor.getString(22));
                    chats.add(jsonModelForChat);
                }
                return chats;
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        return chats;
    }

    public ArrayList<JsonModelForChat> getAllSingleChatOffline(){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        ArrayList<JsonModelForChat> chats= new ArrayList<JsonModelForChat>();
        Cursor cursor = null;
        try{
            cursor = msqLiteDatabase.rawQuery("SELECT * FROM " + SINGLE_CHAT_TABLE + " WHERE uploading = '"+ "true" +"'", null);
            JsonModelForChat jsonModelForChat;
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    jsonModelForChat= new JsonModelForChat();
                    jsonModelForChat.setId(cursor.getString(0));
                    jsonModelForChat.setSendName(cursor.getString(1));
                    jsonModelForChat.setRecieverName(cursor.getString(2));
                    jsonModelForChat.setDate(cursor.getString(3));
                    jsonModelForChat.setMessage(cursor.getString(4));
                    jsonModelForChat.setIsread(cursor.getString(5));
                    jsonModelForChat.setDeliver(cursor.getString(6));
                    jsonModelForChat.setType(cursor.getString(7));
                    jsonModelForChat.setResponse(cursor.getString(8));
                    jsonModelForChat.setTime(cursor.getString(9));
                    jsonModelForChat.setMid(cursor.getString(10));
                    jsonModelForChat.setDocName(cursor.getString(11));
                    jsonModelForChat.setUname(cursor.getString(12));
                    jsonModelForChat.setHeading(cursor.getString(13));
                    jsonModelForChat.setTimedate(cursor.getString(14));
                    jsonModelForChat.setSelect(Boolean.parseBoolean(cursor.getString(15)));
                    jsonModelForChat.setStatus(cursor.getString(16));
                    jsonModelForChat.setUploading(cursor.getString(17));
                    jsonModelForChat.setDid(cursor.getString(18));
                    jsonModelForChat.setExtension(cursor.getString(19));
                    jsonModelForChat.setParent(cursor.getString(20));
                    jsonModelForChat.setList_position(cursor.getInt(21));
                    jsonModelForChat.setData(cursor.getString(22));
                    chats.add(jsonModelForChat);
                }
                return chats;
            }
        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            msqLiteDatabase.close();
        }
        return chats;
    }

    public void deleteSingleChatImage(String mid){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        msqLiteDatabase.execSQL("DELETE FROM "+ SINGLE_CHAT_TABLE + " WHERE mid"+ " = '" + mid+ "'");
        msqLiteDatabase.close();

    }

    public void deleteGroupChatImage(String mid){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        msqLiteDatabase.execSQL("DELETE FROM "+ GROUP_CHAT_TABLE+ " WHERE mid"+ " = '" + mid+ "'");
        msqLiteDatabase.close();
    }
  /*  public class GetChatData extends AsyncTask<Void,Void,ArrayList<Chat_Modal>>{
        @Override
        protected ArrayList<Chat_Modal> doInBackground(Void... voids) {
            ArrayList<Chat_Modal> allChat=getAllChat();
            return allChat;
        }
    }*/

    private String dateConverter(String input) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat currnetDate = new SimpleDateFormat("yyyy-MM-dd");
        String today = currnetDate.format(c.getTime());
        c.add(Calendar.DATE, -1);
        String yesterday = currnetDate.format(c.getTime());

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date/time pattern of desired output date
        DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        String output = null;
        try {
            //Conversion of input String to date
            date = df.parse(input);
            //old date format to new date format
            output = outputformat.format(date);
            if (today.equalsIgnoreCase(output)) {
                output = "TODAY";
            }
            if (yesterday.equalsIgnoreCase(output)) {
                output = "YESTERDAY";
            }

            System.out.println(output);
            Log.d("OUTPUT_DATE", output);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;
    }

    public void updateDateView(String mid_last, String date, String table) {
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        if (table.equals("single")){
            msqLiteDatabase.update(SINGLE_CHAT_TABLE, values, "mid= '" + mid_last + "'", null);
        }else{
            msqLiteDatabase.update(GROUP_CHAT_TABLE, values, "mid= '" + mid_last + "'", null);
        }
        msqLiteDatabase.close();
        Log.d("Background_service", "updated" + date);

    }

    public void updateNotification(String id, String chattype, String cond){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mute", cond);
        msqLiteDatabase.update(CHAT_LIST_TABLE, values, "id= '" + id+ "' AND chattype = '" + chattype +"'", null);
        msqLiteDatabase.close();
        Log.d("Background_service", "updated" + cond);
    }

    public void updateIsRead(String isread, String rid){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isread", isread);
        msqLiteDatabase.update(SINGLE_CHAT_TABLE, values, "id= '" + rid+ "'", null);
        msqLiteDatabase.close();
        Log.d("Background_service", "updated" + isread);
    }

    public void updateDeliver(String deliver, String rid){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("deliver", deliver);
        msqLiteDatabase.update(SINGLE_CHAT_TABLE, values, "id= '" + rid+ "'", null);
        msqLiteDatabase.close();
        Log.d("Background_service", "updated" + deliver);
    }

    public void updateResponse(String response, String rid){
        SQLiteDatabase msqLiteDatabase=this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("response", response);
            switch (response){
                case "delivered":
                    msqLiteDatabase.update(SINGLE_CHAT_TABLE, values, "id= '" + rid+ "' AND response = 'NA'", null);
                    break;
                case "NA":
                    msqLiteDatabase.update(SINGLE_CHAT_TABLE, values, "id= '" + rid+ "' AND ( response != 'delivered' OR resposne != 'read')", null);
                    break;
                case "read":
                    msqLiteDatabase.update(SINGLE_CHAT_TABLE, values, "id= '" + rid+ "'", null);
                    break;
            }

        }catch (Exception e){
            Log.d("EXCPTN", e.toString());
        }finally {
            msqLiteDatabase.close();
        }

        Log.d("Background_service", "updated" + response);
    }
}
