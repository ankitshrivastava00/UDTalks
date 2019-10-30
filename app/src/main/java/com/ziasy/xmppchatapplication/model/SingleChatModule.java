package com.ziasy.xmppchatapplication.model;

public class SingleChatModule {
    String Id;
    String SenderId;
    String RecieverId;
    String Datetime;
    String time;
    String date;
    String Message;
    String IsRead;
    String Deliver;
    String ChatType;
    String Response;
    String Heading;
    boolean IsSelect;
    String ChatStatus;
    String ChatUploading;
    String DeviceId;
    String ChatImage;
    String Extension;
    String ListPosition;
    String Parent;
    String Uid;

    public boolean isSelect() {
        return IsSelect;
    }

    public void setSelect(boolean select) {
        IsSelect = select;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getRecieverId() {
        return RecieverId;
    }

    public void setRecieverId(String recieverId) {
        RecieverId = recieverId;
    }

    public String getDatetime() {
        return Datetime;
    }

    public void setDatetime(String datetime) {
        Datetime = datetime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getIsRead() {
        return IsRead;
    }

    public void setIsRead(String isRead) {
        IsRead = isRead;
    }

    public String getDeliver() {
        return Deliver;
    }

    public void setDeliver(String deliver) {
        Deliver = deliver;
    }

    public String getChatType() {
        return ChatType;
    }

    public void setChatType(String chatType) {
        ChatType = chatType;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getHeading() {
        return Heading;
    }

    public void setHeading(String heading) {
        Heading = heading;
    }


    public String getChatStatus() {
        return ChatStatus;
    }

    public void setChatStatus(String chatStatus) {
        ChatStatus = chatStatus;
    }

    public String getChatUploading() {
        return ChatUploading;
    }

    public void setChatUploading(String chatUploading) {
        ChatUploading = chatUploading;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getChatImage() {
        return ChatImage;
    }

    public void setChatImage(String chatImage) {
        ChatImage = chatImage;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    public String getListPosition() {
        return ListPosition;
    }

    public void setListPosition(String listPosition) {
        ListPosition = listPosition;
    }

    public String getParent() {
        return Parent;
    }

    public void setParent(String parent) {
        Parent = parent;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
