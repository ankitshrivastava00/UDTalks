package com.ziasy.xmppchatapplication.model;

public class EmojiMadel {
    String name;
    String image;

    public EmojiMadel(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
