package com.example.myapplication.Bean;

public class Music {
    private int id;

    private String musicname;

    private int fobjectid;

    private String singer;

    private String type;

    private String fobjectname;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }

    public String getMusicname(){
        return musicname;
    }
    public void setMusicname(String musicname){
        this.musicname=musicname;
    }

    public int getFobjectid(){
        return fobjectid;
    }
    public void setFobjectid(int fobjectid){
        this.fobjectid=fobjectid;
    }

    public String getSinger(){
        return singer;
    }
    public void setSinger(String singer){
        this.singer=singer;
    }

    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type=type;
    }

    public String getFobjectname(){
        return fobjectname;
    }
    public void setFobjectname(String fobjectname){
        this.fobjectname=fobjectname;
    }
}
