package com.example.myapplication.Bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class FJList implements Serializable {

    private String name;
    private Bitmap image;
    private int id;

    public FJList (){}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

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
