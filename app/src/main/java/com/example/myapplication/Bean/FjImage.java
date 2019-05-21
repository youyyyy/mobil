package com.example.myapplication.Bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class FjImage implements Serializable {
    private Bitmap image;

    public FjImage(){}

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }
}
