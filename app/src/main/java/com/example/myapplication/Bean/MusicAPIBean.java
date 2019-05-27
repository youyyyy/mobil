package com.example.myapplication.Bean;

import java.io.Serializable;

public class MusicAPIBean implements Serializable {
    private String result;

    private int code;

    private MusicAPI[] data;

    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    public MusicAPI getData() {
        return data[0];
    }
    public void setData(MusicAPI[] data) {
        this.data = data;
    }
}
