package com.ziasy.xmppchatapplication.common;

import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Utils {


    public static void downloadFile(Context context, String path, String fileName) {
        try {
            // String str = list.get(position).getPath() + list.get(position).getFile_name();
            //String str = list.get(position).getFile_name();
            String str = path.replaceAll(" ", "%20");
            Uri source = Uri.parse(str);
            //  Uri source = Uri.parse(list1.get(position).getUrl() + list1.get(position).getFilename());

            DownloadManager.Request request = new DownloadManager.Request(
                    source);
            request.setDescription("Description for the DownloadManager Bar");

            request.setTitle("Upload Download History: " + fileName);
            request.setAllowedOverRoaming(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
          /*  Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
            context.startActivity(intent);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static  boolean checkautoStartPermissionNeeded(final Context context, String manufacturer){
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        if ("oppo".equalsIgnoreCase(manufacturer)) {

            return true;
        }
        if ("vivo".equalsIgnoreCase(manufacturer)) {

            return true;
        }
        if ("huawei".equalsIgnoreCase(manufacturer)) {

            return true;
        }
        if ("Honor".equalsIgnoreCase(manufacturer)) {

            return true;
        }
        if ("Letv".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        if ("oneplus".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }
    public static boolean autoStartPermission(final Context context, String manufacturer) {

        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            try {
                final Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(new ComponentName("com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        if ("oppo".equalsIgnoreCase(manufacturer)) {
            try {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClassName("com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity");
                context.startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClassName("com.oppo.safe",
                            "com.oppo.safe.permission.startup.StartupAppListActivity");
                    context.startActivity(intent);

                } catch (Exception ex) {
                    try {
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClassName("com.coloros.safecenter",
                                "com.coloros.safecenter.startupapp.StartupAppListActivity");
                        context.startActivity(intent);
                    } catch (Exception exx) {

                    }
                }
            }
            return true;
        }
        if ("vivo".equalsIgnoreCase(manufacturer)) {
            try {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(new ComponentName("com.iqoo.secure",
                        "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
                context.startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                            "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                    context.startActivity(intent);
                } catch (Exception ex) {
                    try {
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClassName("com.iqoo.secure",
                                "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                        context.startActivity(intent);
                    } catch (Exception exx) {
                        ex.printStackTrace();
                    }
                }
            }
            return true;
        }
        if ("huawei".equalsIgnoreCase(manufacturer)) {
            try {
                final Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                context.startActivity(intent);
            } catch (Exception e) {

            }
            return true;
        }
        if ("Honor".equalsIgnoreCase(manufacturer)) {
            try {
                final Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                context.startActivity(intent);
            } catch (Exception e) {

            }
            return true;
        }
        if ("Letv".equalsIgnoreCase(manufacturer)) {
            try {
                final Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
                context.startActivity(intent);
            } catch (Exception e) {

            }
            return true;
        }
        if ("oneplus".equalsIgnoreCase(manufacturer)) {
            try {
                final Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(new ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListAct‌​ivity"));
                context.startActivity(intent);
            } catch (Exception e) {

            }
            return true;
        }
        return false;
    }
    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    public static String dateConverter(String input) {
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
            /*if (today.equalsIgnoreCase(output)) {
                output = "TODAY";
            }
            if (yesterday.equalsIgnoreCase(output)) {
                output = "YESTERDAY";
            }*/

            System.out.println(output);
            Log.d("OUTPUT_DATE", output);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;
    }
    public static String timeConverter(String input) {
        String output = null;
        DateFormat outputformat = null;
        Date date = null;
        if (!input.equalsIgnoreCase("null")) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat currnetDate = new SimpleDateFormat("yyyy-MM-dd");
            String today = currnetDate.format(c.getTime());
            c.add(Calendar.DATE, -1);
            String yesterday = currnetDate.format(c.getTime());

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //Date/time pattern of desired output date
            outputformat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                //Conversion of input String to date
                date = df.parse(input);
                output = outputformat.format(date);
                if (today.equalsIgnoreCase(output)) {
                    outputformat = new SimpleDateFormat("hh:mm aa");
                    output = outputformat.format(date);
                } else if (yesterday.equalsIgnoreCase(output)) {
                    output = "YESTERDAY";
                } else {
                    output = outputformat.format(date);
                }

                //old date format to new date format

                System.out.println(output);
                Log.d("OUTPUT_DATE", output);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
        return output;
    }

}
