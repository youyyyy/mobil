package com.example.myapplication.Bean;

import java.util.Date;

public class Discuss {
    private int id;

    private String body;

    private int userid;

    private int fobjectid;

    private Date time=new Date();

    //private Date t=new Date();

    private int pid;

    private int puid;

    private User user;


    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }

    public String getBody(){
        return body;
    }
    public void setBody(String body){
        this.body=body;
    }

    public int getUserid(){
        return userid;
    }
    public void setUserid(int userid){
        this.userid=userid;
    }

    public int getFobjectid(){
        return fobjectid;
    }
    public void setFobjectid(int fobjectid){
        this.fobjectid=fobjectid;
    }

    public int getPid(){
        return pid;
    }
    public void setPid(int pid){
        this.pid=pid;
    }

    public Date getTime(){
        return time;
    }
    public void setTime(Date time){
        this.time=time;
    }

    public int getPuid(){
        return puid;
    }
    public void setPuid(int puid){
        this.puid=puid;
    }
}
