package com.example.myapplication.Bean;

public class Collect {
    private int id;

    private int userid;

    private int fjid;

    private String fjname;

    private int updatetime;

    public Collect(){}

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setFjid(int fjid) {
        this.fjid = fjid;
    }
    public int getFjid() {
        return fjid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
    public int getUserid() {
        return userid;
    }

    public void setFjname(String fjname) {
        this.fjname = fjname;
    }
    public String getFjname() {
        return fjname;
    }

    public void setUpdatetime(int updatetime) {
        this.updatetime = updatetime;
    }
    public int getUpdatetime() {
        return updatetime;
    }
}
