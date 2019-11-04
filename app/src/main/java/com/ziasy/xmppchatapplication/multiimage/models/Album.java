package com.ziasy.xmppchatapplication.multiimage.models;

/**
 * Created by Darshan on 4/14/2015.
 */
public class Album {
    public String name;
    public String cover;
    public String imageCount;

    public Album(String name, String cover, String imageCount) {
        this.name = name;
        this.imageCount = imageCount;
        this.cover = cover;
    }
}
