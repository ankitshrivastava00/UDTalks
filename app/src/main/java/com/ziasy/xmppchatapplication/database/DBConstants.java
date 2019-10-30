package com.ziasy.xmppchatapplication.database;

import android.provider.BaseColumns;

/**
 * Created by ANDROID on 14-Sep-17.
 */

public interface DBConstants extends BaseColumns {

    //EMPLOYEE_LIST_TABLE
    public static final String CHAT_LIST_TABLE = "chat_list";
    //columns in the entry table
    public static final String CHAT_USER_ID = "id";
    public static final String CHAT_USER_NAME = "name";
    public static final String CHAT_USER_DESCRIPTION = "description";
    public static final String CHAT_USER_LASTMESSAGE = "lastMessage";
    public static final String CHAT_USER_DATETIME = "datetime";
    public static final String CHAT_USER_TIME = "time";
    public static final String CHAT_USER_USERSTATUS = "userstatus";
    public static final String CHAT_USER_PHOTO = "photo";
    public static final String CHAT_USER_DTYPE = "dtype";
    public static final String CHAT_USER_MESSAGE = "message";
    public static final String CHAT_USER_CHATTYPE = "chattype";
    public static final String CHAT_USER_DID = "did";
    public static final String CHAT_USER_ADMIN = "admin";
    public static final String CHAT_USER_LIST_COUNT = "count";
    public static final String CHAT_USER_MUTE = "mute";


    //SINGLE_CHAT_TABLE
    public static final String SINGLE_CHAT_TABLE = "single_chat";
    //columns in the entry table
    public static final String SINGLE_CHAT_ID = "ID";
    public static final String SINGLE_CHAT_SENDER_ID = "SINGLE_CHAT_SENDER_ID";
    public static final String SINGLE_CHAT_RECIEVER_ID = "SINGLE_CHAT_RECIEVER_ID";
    public static final String SINGLE_CHAT_DATETIME = "SINGLE_CHAT_DATETIME";
    public static final String SINGLE_CHAT_TIME = "SINGLE_CHAT_TIME";
    public static final String SINGLE_CHAT_DATE = "SINGLE_CHAT_DATE";
    public static final String SINGLE_CHAT_MESSAGE = "SINGLE_CHAT_MESSAGE";
    public static final String SINGLE_CHAT_ISREAD = "SINGLE_CHAT_ISREAD";
    public static final String SINGLE_CHAT_DELIVER = "SINGLE_CHAT_DELIVER";
    public static final String SINGLE_CHAT_TYPE = "SINGLE_CHAT_TYPE";
    public static final String SINGLE_CHAT_RESPONSE = "response";
    public static final String SINGLE_CHAT_HEADING = "SINGLE_CHAT_heading";
    public static final String SINGLE_CHAT_ISSELECT = "SINGLE_CHAT_is_select";
    public static final String SINGLE_CHAT_STATUS = "SINGLE_CHAT_status";
    public static final String SINGLE_CHAT_UPLOADING = "SINGLE_CHAT_uploading";
    public static final String SINGLE_CHAT_DID = "SINGLE_CHAT_did";
    public static final String SINGLE_CHAT_IMAGE = "SINGLE_CHAT_image";
    public static final String SINGLE_CHAT_EXTENSION = "SINGLE_CHAT_extension";
    public static final String SINGLE_CHAT_LIST_POSITION = "SINGLE_CHAT_list_position";
    public static final String SINGLE_CHAT_PARENT = "SINGLE_CHAT_parent";
    public static final String SINGLE_CHAT_UID = "SINGLE_CHAT_UID";


    //GROUP_CHAT_TABLE
    public static final String GROUP_CHAT_TABLE = "group_chat";
    //columns in the entry table
    public static final String GROUP_CHAT_ID = "GROUP_CHAT_ID";
    public static final String GROUP_CHAT_SENDER_ID = "GROUP_CHAT_SENDER_ID";
    public static final String GROUP_CHAT_RECIEVER_ID = "GROUP_CHAT_RECIEVER_ID";
    public static final String GROUP_CHAT_DATETIME = "GROUP_CHAT_DATETIME";
    public static final String GROUP_CHAT_TIME = "GROUP_CHAT_TIME";
    public static final String GROUP_CHAT_DATE = "GROUP_CHAT_DATE";
    public static final String GROUP_CHAT_MESSAGE = "GROUP_CHAT_MESSAGE";
    public static final String GROUP_CHAT_ISREAD = "GROUP_CHAT_ISREAD";
    public static final String GROUP_CHAT_DELIVER = "GROUP_CHAT_DELIVER";
    public static final String GROUP_CHAT_TYPE = "GROUP_CHAT_TYPE";
    public static final String GROUP_CHAT_RESPONSE = "GROUP_CHAT_response";
    public static final String GROUP_CHAT_HEADING = "GROUP_CHAT_heading";
    public static final String GROUP_CHAT_ISSELECT = "GROUP_CHAT_is_select";
    public static final String GROUP_CHAT_STATUS = "GROUP_CHAT_status";
    public static final String GROUP_CHAT_UPLOADING = "GROUP_CHAT_uploading";
    public static final String GROUP_CHAT_DID = "GROUP_CHAT_did";
    public static final String GROUP_CHAT_EXTENSION = "GROUP_CHAT_extension";
    public static final String GROUP_CHAT_PARENT = "GROUP_CHAT_parent";
    public static final String GROUP_CHAT_LIST_POSITION = "GROUP_CHAT_list_position";
    public static final String GROUP_CHAT_GROUPIMAGE = "GROUP_CHAT_group_img";
    public static final String GROUP_CHAT_GROUPNAME = "GROUP_CHAT_group_name";
    public static final String GROUP_CHAT_DESCRIPTION = "GROUP_CHAT_description";
    public static final String GROUP_CHAT_UID = "GROUP_CHAT_UID";
}