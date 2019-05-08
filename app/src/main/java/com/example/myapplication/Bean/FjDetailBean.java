package com.example.myapplication.Bean;

import java.io.Serializable;
import java.util.Date;

public class FjDetailBean implements Serializable {
    private int code;

    private String message;

    private Fobject data;

    private Date time;

    public int getCode() {
        return code;
    }
    public void setCode(int code){
        this.code = code;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message=message;
    }

    public Fobject getData(){
        return data;
    }
    public void setData(Fobject data){
        this.data=data;
    }

    public Date getTime(){
        return time;
    }
    public void setTime(Date time){
        this.time=time;
    }
}
