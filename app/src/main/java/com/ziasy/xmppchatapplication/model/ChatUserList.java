package com.ziasy.xmppchatapplication.model;

import java.util.Comparator;

/**
 * Created by Khushvinders on 18-Nov-16.
 */

public class ChatUserList {
    private String username;
    private String mute;

    private String admin;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
    private String name;
    private String email;
    private String did;
    private String chattype;
    private String flag = "false";
    String id;
    String lastMessage = "";
    String isOnline = "false";
    String time;

    public String getMute() {
        return mute;
    }

    public void setMute(String mute) {
        this.mute = mute;
    }

    String imageUrl;
    String datetime;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String description;
    String count = "0";

    private String image;
    private String FROM;
    private String type;
    private String data;

    public String getChattype() {
        return chattype;
    }

    public void setChattype(String chattype) {
        this.chattype = chattype;
    }


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static Comparator<ChatUserList> StuNameComparator = new Comparator<ChatUserList>() {

        public int compare(ChatUserList s1, ChatUserList s2) {
            String StudentName1 = s1.getDatetime().toUpperCase();
            String StudentName2 = s2.getDatetime().toUpperCase();
            //ascending order
            //return StudentName1.compareTo(StudentName2);
            //descending order
            return StudentName2.compareTo(StudentName1);
        }
    };

    @Override
    public String toString() {
        return "[ name=" + name + ", datetime=" + datetime + ", count=" + count + ", id=" + id + "]";
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFROM() {
        return FROM;
    }

    public void setFROM(String FROM) {
        this.FROM = FROM;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }


    public static Comparator<ChatUserList> getStuNameComparator() {
        return StuNameComparator;
    }

    public static void setStuNameComparator(Comparator<ChatUserList> stuNameComparator) {
        StuNameComparator = stuNameComparator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}