package com.example.myapplication.Bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DiscussList implements Serializable {
    private int code;

    private List<DiscussReturn> data;

    private Date time;

    public void Fobject() { }

    public void setData(List<DiscussReturn> data) {
        this.data = data;
    }

    public List<DiscussReturn> getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return time;
    }
}
