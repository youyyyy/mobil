package com.example.myapplication.Bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class FJList implements Serializable {

    private String name;
    private Bitmap image;

    public FJList (){}

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
