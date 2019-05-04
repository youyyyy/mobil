package com.example.myapplication.Bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FobjectList implements Serializable {

    private int code;

    private List<Fobject> data;

    private Date time;

    public void Fobject() { }

    public void setData(List<Fobject> data) {
        this.data = data;
    }

    public List<Fobject> getData() {
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
