package com.ziasy.xmppchatapplication.database;

/**
 * Created by ANDROID on 14-Sep-17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
import static com.ziasy.xmppchatapplication.database.DBConstants.EMOJI_LIST;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_DATE;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_DATETIME;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_DELIVER;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_DESCRIPTION;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_DID;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_EXTENSION;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_GROUPIMAGE;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_GROUPNAME;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_HEADING;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_ID;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_ISREAD;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_ISSELECT;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_LIST_POSITION;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_MESSAGE;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_PARENT;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_RECIEVER_ID;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_RESPONSE;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_SENDER_ID;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_STATUS;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_TABLE;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_TIME;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_TYPE;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_UID;
import static com.ziasy.xmppchatapplication.database.DBConstants.GROUP_CHAT_UPLOADING;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_DATE;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_DATETIME;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_DELIVER;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_DID;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_EXTENSION;
import static com.ziasy.xmppchatapplication.database.DBConstants.SINGLE_CHAT_GRAVITY;
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
 * @author "Sudar Muthu (http://sudarmuthu.com)"
 */
public class DBData extends SQLiteOpenHelper {
    private static final String TAG = "DBData";
    private static final String DATABASE_NAME = "UDChat.db";
    private static final int DATABASE_VERSION = 1;

    // SIINGLE_LIST_TABLE
    private static final String CREATE_CHAT_LIST_TABLE =
            "CREATE TABLE " + CHAT_LIST_TABLE + " ("
                    + CHAT_USER_ID + " INTEGER , "
                    + CHAT_USER_NAME + " TEXT,"
                    + CHAT_USER_DESCRIPTION + " TEXT,"
                    + CHAT_USER_LASTMESSAGE + " TEXT,"
                    + CHAT_USER_DATETIME + " TEXT,"
                    + CHAT_USER_TIME + " TEXT,"
                    + CHAT_USER_USERSTATUS + " TEXT,"
                    + CHAT_USER_PHOTO + " TEXT,"
                    + CHAT_USER_DTYPE + " TEXT,"
                    + CHAT_USER_MESSAGE + " TEXT,"
                    + CHAT_USER_CHATTYPE + " TEXT,"
                    + CHAT_USER_DID + " TEXT,"
                    + CHAT_USER_ADMIN + " TEXT,"
                    + CHAT_USER_LIST_COUNT + " TEXT,"
                    + CHAT_USER_MUTE + " VARCHAR DEFAULT 'false'"
                    + " );";


    //SINGLE_CHAT_TABLE
    private static final String CREATE_SINGLE_CHAT_TABLE =
            "CREATE TABLE " + SINGLE_CHAT_TABLE + " ("
                    + SINGLE_CHAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SINGLE_CHAT_SENDER_ID + " INTEGER,"
                    + SINGLE_CHAT_RECIEVER_ID + " TEXT,"
                    + SINGLE_CHAT_DATETIME + " TEXT,"
                    + SINGLE_CHAT_TIME + " TEXT,"
                    + SINGLE_CHAT_DATE + " TEXT,"
                    + SINGLE_CHAT_MESSAGE + " TEXT,"
                    + SINGLE_CHAT_ISREAD + " TEXT,"
                    + SINGLE_CHAT_DELIVER + " TEXT,"
                    + SINGLE_CHAT_TYPE + " TEXT,"
                    + SINGLE_CHAT_RESPONSE + " TEXT,"
                    + SINGLE_CHAT_HEADING + " TEXT,"
                    + SINGLE_CHAT_ISSELECT + " TEXT,"
                    + SINGLE_CHAT_STATUS + " TEXT,"
                    + SINGLE_CHAT_UPLOADING + " TEXT,"
                    + SINGLE_CHAT_DID + " TEXT,"
                    + SINGLE_CHAT_EXTENSION + " TEXT,"
                    + SINGLE_CHAT_LIST_POSITION + " TEXT,"
                    + SINGLE_CHAT_IMAGE + " TEXT,"
                    + SINGLE_CHAT_PARENT + " TEXT,"
                    + SINGLE_CHAT_UID + " TEXT,"
                    + SINGLE_CHAT_GRAVITY + " TEXT"
                    + " );";


    //GROUP_CHAT_TABLE
    private static final String CREATE_GROUP_CHAT_TABLE =
            "CREATE TABLE " + GROUP_CHAT_TABLE + " ("
                    + GROUP_CHAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + GROUP_CHAT_SENDER_ID + " INTEGER,"
                    + GROUP_CHAT_RECIEVER_ID + " TEXT,"
                    + GROUP_CHAT_DATETIME + " TEXT,"
                    + GROUP_CHAT_TIME + " TEXT,"
                    + GROUP_CHAT_DATE + " TEXT,"
                    + GROUP_CHAT_MESSAGE + " TEXT,"
                    + GROUP_CHAT_ISREAD + " TEXT,"
                    + GROUP_CHAT_DELIVER + " TEXT,"
                    + GROUP_CHAT_TYPE + " TEXT,"
                    + GROUP_CHAT_RESPONSE + " TEXT,"
                    + GROUP_CHAT_HEADING + " TEXT,"
                    + GROUP_CHAT_ISSELECT + " TEXT,"
                    + GROUP_CHAT_STATUS + " TEXT,"
                    + GROUP_CHAT_UPLOADING + " TEXT,"
                    + GROUP_CHAT_DID + " TEXT,"
                    + GROUP_CHAT_EXTENSION + " TEXT,"
                    + GROUP_CHAT_PARENT + " TEXT,"
                    + GROUP_CHAT_LIST_POSITION + " TEXT,"
                    + GROUP_CHAT_GROUPIMAGE + " TEXT,"
                    + GROUP_CHAT_GROUPNAME + " TEXT,"
                    + GROUP_CHAT_DESCRIPTION + " TEXT,"
                    + GROUP_CHAT_UID + " TEXT"
                    + " );";

    /**
     * @param context
     */
    public DBData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // When the app is installed for the first time

        db.execSQL(CREATE_CHAT_LIST_TABLE);
        db.execSQL(CREATE_SINGLE_CHAT_TABLE);

        db.execSQL(CREATE_GROUP_CHAT_TABLE);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + EMOJI_LIST + " ( id VARCHAR, name VARCHAR , path TEXT, UNIQUE(id) );");

//        db.execSQL(CREATE_TROLLEY_TABLE);
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_CHAT_LIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_SINGLE_CHAT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_GROUP_CHAT_TABLE);
        // db.execSQL("DROP TABLE IF EXISTS " + CREATE_TROLLEY_TABLE);
        onCreate(db);
    }
}