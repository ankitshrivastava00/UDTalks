package com.ziasy.xmppchatapplication.model;

import java.util.Comparator;

/**
 * Created by Khushvinders on 18-Nov-16.
 */

public class User {
    private String username;
    private String name;
    private String email;
    private String did;
    private String flag = "false";
    String lastMessage = "";
    String imageUrl;
    String isOnline = "false";
    String time;
    String count = "0";
    String id;
    String datetime;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public static Comparator<User> getStuNameComparator() {
        return StuNameComparator;
    }

    public static void setStuNameComparator(Comparator<User> stuNameComparator) {
        StuNameComparator = stuNameComparator;
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

    public static Comparator<User> StuNameComparator = new Comparator<User>() {

    public int compare(User s1, User s2) {
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
}