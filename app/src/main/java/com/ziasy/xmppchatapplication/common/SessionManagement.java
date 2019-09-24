package com.ziasy.xmppchatapplication.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ziasy.xmppchatapplication.R;

public class SessionManagement {

    SharedPreferences pref;
    Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "UDTALKS";
    private static final String LOGIN_STATUS = "false";
    private static final String BACKUP_STATUS = "false";
    private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_PASSWORD = "USER_PASSWORD";
    private static final String USER_MOBILE = "USER_MOBILE";
    private static final String USER_TROLLEY_ID = "USER_TROLLEY_ID";
    private static final String USER_FCM_ID = "USER_FCM_ID";
    private static final String USER_OTP = "USER_OTP";
    private static final String KEY_ID = "KEY_ID";
 //   private static final int WALLPAPER = R.drawable.wall_default;
  //  private static final int GROUP_WALLPAPER = R.drawable.background_profile;

    public SessionManagement(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getLoginStatus() {
        return pref.getString("LOGIN_STATUS", LOGIN_STATUS);
    }

    public void setLoginStatus(String s) {
        editor.putString("LOGIN_STATUS", s);
        editor.commit();

    }
    public String getBackupStatus() {
        return pref.getString("BACKUP_STATUS", BACKUP_STATUS);
    }

    public void setBackupStatus(String s) {
        editor.putString("BACKUP_STATUS", s);
        editor.commit();

    }

    public String getUserId() {
        return pref.getString("USER_ID", USER_ID);
    }

    public void setUserId(String s) {
        editor.putString("USER_ID", s);
        editor.commit();

    }public String getKeyId() {
        return pref.getString("KEY_ID", KEY_ID);
    }

    public void setKeyId(String s) {
        editor.putString("KEY_ID", s);
        editor.commit();

    }
   /* public int getWallpaper() {
        return pref.getInt("WALLPAPER", WALLPAPER);
    }

    public void setWallpaper(int s) {
        editor.putInt("WALLPAPER", s);
        editor.commit();

    }*/
/*
    public int getGroupWallpaper() {
        return pref.getInt("GROUP_WALLPAPER", GROUP_WALLPAPER);
    }

    public void setGroupWallpaper(int s) {
        editor.putInt("GROUP_WALLPAPER", s);
        editor.commit();

    }*/

    public String getUserPassword() {
        return pref.getString("USER_PASSWORD", USER_PASSWORD);
    }

    public void setUserPassword(String s) {
        editor.putString("USER_PASSWORD", s);
        editor.commit();

    }

    public String getUserName() {
        return pref.getString("USER_NAME", USER_NAME);
    }

    public void setUserName(String s) {
        editor.putString("USER_NAME", s);
        editor.commit();

    }

    public String getUserEmail() {
        return pref.getString("USER_EMAIL", USER_EMAIL);
    }

    public void setUserEmail(String s) {
        editor.putString("USER_EMAIL", s);
        editor.commit();

    }

    public String getUserTrolleyId() {
        return pref.getString("USER_TROLLEY_ID", USER_TROLLEY_ID);
    }

    public void setUserTrolleyId(String s) {
        editor.putString("USER_TROLLEY_ID", s);
        editor.commit();

    }

    public String getUserMobile() {
        return pref.getString("USER_MOBILE", USER_MOBILE);
    }

    public void setUserMobile(String s) {
        editor.putString("USER_MOBILE", s);
        editor.commit();

    }



    public String getUserOtp() {
        return pref.getString("USER_OTP", USER_OTP);
    }

    public void setUserOtp(String s) {
        editor.putString("USER_OTP", s);
        editor.commit();

    }

    public String getUserFcmId() {
        return pref.getString("USER_FCM_ID", USER_FCM_ID);
    }

    public void setUserFcmId(String s) {
        editor.putString("USER_FCM_ID", s);
        editor.commit();

    }
    public void clearSession(){
        editor.clear();
    }
}
