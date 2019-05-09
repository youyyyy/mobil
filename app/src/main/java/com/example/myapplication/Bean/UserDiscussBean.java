package com.example.myapplication.Bean;

import java.io.Serializable;
import java.util.Date;

public class UserDiscussBean implements Serializable {
    private int code;

    private String message;

    private User data;

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

    public User getData(){
        return data;
    }
    public void setData(User data){
        this.data=data;
    }

    public Date getTime(){
        return time;
    }
    public void setTime(Date time){
        this.time=time;
    }

}
