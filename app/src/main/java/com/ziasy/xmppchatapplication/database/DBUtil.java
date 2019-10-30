package com.ziasy.xmppchatapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ziasy.xmppchatapplication.common.SessionManagement;
import com.ziasy.xmppchatapplication.model.ChatUserList;
import com.ziasy.xmppchatapplication.model.SingleChatModule;

import java.util.ArrayList;
import java.util.List;

import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_LIST_TABLE;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_ADMIN;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_CHATTYPE;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_DATETIME;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_DESCRIPTION;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_DID;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_DTYPE;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_ID;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_LASTMESSAGE;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_LIST_COUNT;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_MESSAGE;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_MUTE;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_NAME;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_PHOTO;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_TIME;
import static com.ziasy.xmppchatapplication.database.DBConstants.CHAT_USER_USERSTATUS;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_DATE;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_DATETIME;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_DELIVER;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_DID;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_EXTENSION;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_HEADING;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_ID;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_IMAGE;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_ISREAD;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_ISSELECT;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_LIST_POSITION;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_MESSAGE;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_PARENT;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_RECIEVER_ID;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_RESPONSE;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_SENDER_ID;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_STATUS;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_TABLE;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_TIME;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_TYPE;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_UID;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_UPLOADING;

/**
 * Created by ANDROID on 14-Sep-17.
 */

public class DBUtil {
    public DBUtil() {

    }

    private static final String TAG = "DBUtil";

    /**
     * Insert type into db
     *
     * @param context
     * @param
     */

    public static List<SingleChatModule> fetchAllSingleChatList(Context context,SingleChatModule module) {
        String[] FROM = {SINGLE_CHAT_ID, SINGLE_CHAT_SENDER_ID, SINGLE_CHAT_RECIEVER_ID, SINGLE_CHAT_DATETIME, SINGLE_CHAT_TIME, SINGLE_CHAT_DATE,
                SINGLE_CHAT_MESSAGE,SINGLE_CHAT_ISREAD,SINGLE_CHAT_DELIVER,SINGLE_CHAT_TYPE,SINGLE_CHAT_RESPONSE,SINGLE_CHAT_HEADING,SINGLE_CHAT_ISSELECT
        ,SINGLE_CHAT_STATUS,SINGLE_CHAT_UPLOADING,SINGLE_CHAT_DID,SINGLE_CHAT_IMAGE,SINGLE_CHAT_EXTENSION,SINGLE_CHAT_LIST_POSITION,SINGLE_CHAT_PARENT,SINGLE_CHAT_UID};
        SQLiteDatabase db = new DBData(context).getReadableDatabase();
        List<SingleChatModule> day = new ArrayList<SingleChatModule>();
        //Cursor cursor = db.query(SINGLE_CHAT_TABLE, FROM, null, null, null, null, SINGLE_CHAT_SENDER_ID);
        Cursor cursor = db.rawQuery("SELECT * FROM " + SINGLE_CHAT_TABLE + " WHERE (( " +SINGLE_CHAT_RECIEVER_ID+ " = '"+ module.getSenderId() +"' AND " +SINGLE_CHAT_SENDER_ID+ " = '"+ new SessionManagement(context).getKeyId() + "' ) OR ( " +SINGLE_CHAT_SENDER_ID+ " = '"+ module.getSenderId() +"' AND " +SINGLE_CHAT_RECIEVER_ID+ " = '"+ new SessionManagement(context).getKeyId() + "' )) ", null);;
        while (cursor.moveToNext()) {
            SingleChatModule temp = new SingleChatModule();
            day.add(temp);
        }
        cursor.close();
        db.close();
        return day;
    }

    public static SingleChatModule fetchSingleChatList(Context context, int id) {
        String[] FROM = {SINGLE_CHAT_ID, SINGLE_CHAT_SENDER_ID, SINGLE_CHAT_RECIEVER_ID, SINGLE_CHAT_DATETIME, SINGLE_CHAT_TIME, SINGLE_CHAT_DATE,
                SINGLE_CHAT_MESSAGE,SINGLE_CHAT_ISREAD,SINGLE_CHAT_DELIVER,SINGLE_CHAT_TYPE,SINGLE_CHAT_RESPONSE,SINGLE_CHAT_HEADING,SINGLE_CHAT_ISSELECT
                ,SINGLE_CHAT_STATUS,SINGLE_CHAT_UPLOADING,SINGLE_CHAT_DID,SINGLE_CHAT_IMAGE,SINGLE_CHAT_EXTENSION,SINGLE_CHAT_LIST_POSITION,SINGLE_CHAT_PARENT,SINGLE_CHAT_UID};
        String where = SINGLE_CHAT_ID + "=" + id;
        SQLiteDatabase db = new DBData(context).getReadableDatabase();
        Cursor cursor = db.query(SINGLE_CHAT_TABLE, FROM, where, null, null, null, SINGLE_CHAT_SENDER_ID);
        cursor.moveToNext();
        SingleChatModule temp = new SingleChatModule();
        cursor.close();
        db.close();
        return temp;
    }

    public static SingleChatModule singleChatInsert(Context context,SingleChatModule model) {
        SQLiteDatabase db = new DBData(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        //  values.put(SINGLE_ID, EmployeeId);
        values.put(SINGLE_CHAT_ID, model.getId());
        values.put(SINGLE_CHAT_SENDER_ID, model.getSenderId());
        values.put(SINGLE_CHAT_RECIEVER_ID, model.getRecieverId());
        values.put(SINGLE_CHAT_DATETIME, model.getDatetime());
        values.put(SINGLE_CHAT_TIME, model.getTime());
        values.put(SINGLE_CHAT_DATE, model.getDate());
        values.put(SINGLE_CHAT_MESSAGE, model.getMessage());
        values.put(SINGLE_CHAT_ISREAD, model.getIsRead());
        values.put(SINGLE_CHAT_DELIVER, model.getDeliver());
        values.put(SINGLE_CHAT_TYPE, model.getChatType());
        values.put(SINGLE_CHAT_RESPONSE, model.getResponse());
        values.put(SINGLE_CHAT_HEADING, model.getHeading());
        values.put(SINGLE_CHAT_ISSELECT, model.getIsSelect());
        values.put(SINGLE_CHAT_STATUS, model.getChatStatus());
        values.put(SINGLE_CHAT_UPLOADING, model.getChatUploading());
        values.put(SINGLE_CHAT_DID, model.getDeviceId());
        values.put(SINGLE_CHAT_IMAGE, model.getChatImage());
        values.put(SINGLE_CHAT_EXTENSION, model.getExtension());
        values.put(SINGLE_CHAT_LIST_POSITION, model.getListPosition());
        values.put(SINGLE_CHAT_PARENT, model.getParent());
        values.put(SINGLE_CHAT_UID, model.getUid());

        long id = db.insertOrThrow(SINGLE_CHAT_TABLE, null, values);
        db.close();
        SingleChatModule newType = fetchSingleChatList(context, (int) id);
        return newType;
    }


    public static List<ChatUserList> fetchAllChatList(Context context) {
        String[] FROM = {CHAT_USER_ID, CHAT_USER_NAME, CHAT_USER_DESCRIPTION, CHAT_USER_LASTMESSAGE, CHAT_USER_DATETIME, CHAT_USER_TIME,
                CHAT_USER_USERSTATUS,CHAT_USER_PHOTO,CHAT_USER_DTYPE,CHAT_USER_MESSAGE,CHAT_USER_CHATTYPE,CHAT_USER_DID,CHAT_USER_ADMIN,
                CHAT_USER_LIST_COUNT,CHAT_USER_MUTE};
        SQLiteDatabase db = new DBData(context).getReadableDatabase();
        List<ChatUserList> day = new ArrayList<ChatUserList>();
        //Cursor cursor = db.query(CHAT_LIST_TABLE, FROM, null, null, null, null, SINGLE_NAME);
        Cursor cursor = db.rawQuery("SELECT * FROM " + CHAT_LIST_TABLE + " " , null);;
        while (cursor.moveToNext()) {
            ChatUserList temp = new ChatUserList();
            temp.setId(cursor.getString(0));
            temp.setName(cursor.getString(1));
            temp.setDescription(cursor.getString(2));
            temp.setLastMessage(cursor.getString(3));
            temp.setDatetime(cursor.getString(4));
            temp.setTime(cursor.getString(5));
            temp.setUserstatus(cursor.getString(6));
            temp.setPhoto(cursor.getString(7));
            temp.setDtype(cursor.getString(8));
            temp.setMessage(cursor.getString(9));
            temp.setChattype(cursor.getString(10));
            temp.setDid(cursor.getString(11));
            temp.setAdmin(cursor.getString(12));
            temp.setCount(cursor.getString(13));
            temp.setMute(cursor.getString(14));
            day.add(temp);
        }
        cursor.close();
        db.close();
        return day;
    }

    public static ChatUserList fetchSingleUserChatList(Context context, int id) {
        String[] FROM = {CHAT_USER_ID, CHAT_USER_NAME, CHAT_USER_DESCRIPTION, CHAT_USER_LASTMESSAGE, CHAT_USER_DATETIME, CHAT_USER_TIME,
                CHAT_USER_USERSTATUS,CHAT_USER_PHOTO,CHAT_USER_DTYPE,CHAT_USER_MESSAGE,CHAT_USER_CHATTYPE,CHAT_USER_DID,CHAT_USER_ADMIN,CHAT_USER_LIST_COUNT,CHAT_USER_MUTE};

        String where = CHAT_USER_ID + "=" + id;
        SQLiteDatabase db = new DBData(context).getReadableDatabase();
        Cursor cursor = db.query(CHAT_LIST_TABLE, FROM, where, null, null, null, CHAT_USER_NAME);
        cursor.moveToNext();
        ChatUserList temp = new ChatUserList();
        temp.setId(cursor.getString(0));
        temp.setName(cursor.getString(1));
        temp.setDescription(cursor.getString(2));
        temp.setLastMessage(cursor.getString(3));
        temp.setDatetime(cursor.getString(4));
        temp.setTime(cursor.getString(5));
        temp.setUserstatus(cursor.getString(6));
        temp.setPhoto(cursor.getString(7));
        temp.setDtype(cursor.getString(8));
        temp.setMessage(cursor.getString(9));
        temp.setChattype(cursor.getString(10));
        temp.setDid(cursor.getString(11));
        temp.setAdmin(cursor.getString(12));
        temp.setCount(cursor.getString(13));
        temp.setMute(cursor.getString(14));
        cursor.close();
        db.close();
        return temp;
    }

    public static ChatUserList chatUserListInsert(Context context,ChatUserList model) {

        SQLiteDatabase db = new DBData(context).getWritableDatabase();
        ChatUserList newType;
        if (checkChatList(context,model.getId())){
            ContentValues values = new ContentValues();

            values.put(CHAT_USER_LASTMESSAGE, model.getLastMessage());
            values.put(CHAT_USER_DATETIME, model.getDatetime());
            values.put(CHAT_USER_TIME, model.getTime());
            values.put(CHAT_USER_USERSTATUS, model.getUserstatus());
            values.put(CHAT_USER_DTYPE, model.getDtype());
            values.put(CHAT_USER_MESSAGE, model.getMessage());
            values.put(CHAT_USER_CHATTYPE, model.getChattype());
            values.put(CHAT_USER_DID, model.getDid());

            long id = db.update(CHAT_LIST_TABLE, values,"'"+CHAT_USER_LASTMESSAGE+"' = '"+model.getId()+"'",null);
            db.close();
             newType = fetchSingleUserChatList(context, Integer.parseInt(model.getId()));
        }else {
            ContentValues values = new ContentValues();
            //  values.put(SINGLE_ID, EmployeeId);

            values.put(CHAT_USER_ID, model.getId());
            values.put(CHAT_USER_NAME, model.getName());
            values.put(CHAT_USER_DESCRIPTION, model.getDescription());
            values.put(CHAT_USER_LASTMESSAGE, model.getLastMessage());
            values.put(CHAT_USER_DATETIME, model.getDatetime());
            values.put(CHAT_USER_TIME, model.getTime());
            values.put(CHAT_USER_USERSTATUS, model.getUserstatus());
            values.put(CHAT_USER_PHOTO, model.getPhoto());
            values.put(CHAT_USER_DTYPE, model.getDtype());
            values.put(CHAT_USER_MESSAGE, model.getMessage());
            values.put(CHAT_USER_CHATTYPE, model.getChattype());
            values.put(CHAT_USER_DID, model.getDid());
            values.put(CHAT_USER_ADMIN, model.getAdmin());
            values.put(CHAT_USER_LIST_COUNT, model.getCount());

            long id = db.insertOrThrow(CHAT_LIST_TABLE, null, values);
            db.close();
             newType = fetchSingleUserChatList(context, (int) id);
        }

        return newType;
    }

    public static void deleteChatListEmlopyee(Context context, int id) {

       /* SQLiteDatabase db = new DBData(context).getWritableDatabase();
        String where = SINGLE_CHAT_ID + "=" + id;
        db.delete(SINGLE_LIST_TABLE, where, null);
        db.close();*/
    }

    public static boolean checkChatList(Context context, String id) {
        // array of columns to fetch
        String[] columns = {CHAT_USER_ID};
        SQLiteDatabase db = new DBData(context).getReadableDatabase();
        // selection criteria
        String selection = CHAT_USER_ID + " = ?";
        // selection argument
        String[] selectionArgs = {id};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(CHAT_LIST_TABLE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

}