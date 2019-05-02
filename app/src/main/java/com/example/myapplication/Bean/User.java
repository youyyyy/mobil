package com.example.myapplication.Bean;

public class User {
    private int id;
    //用户昵称
    private String username;
    //密码
    private String password;
    //用户性别
    private int gender;
    //用户电话
    private String phonenum;
    //用户级别
    private String levels;
    //用户邮箱
    private String email;
    //时间
    // private Date regtime;
    //兴趣类别
    private String type;
    //个性签名
    private String csignature;

    public User(){}


    public int getId() { return id; }
    public void setId(int id) { this.id=id; }


    public String getUsername() { return username; }
    public void setUsername(String username) { this.username=username; }


    public String getPassword() { return password; }
    public void setPassword(String password) { this.password=password; }


    public int getGender() { return gender; }
    public void setGender(int gender) { this.gender=gender; }


    public String getPhonenum() { return phonenum; }
    public void setPhonenum(String phonenum) { this.phonenum=phonenum;}


    public String getLevels() { return levels;}
    public void setLevels(String levels) { this.levels=levels; }


    public String getEmail() { return email; }
    public void setEmail(String email) { this.email=email; }

    public String getType(){return type;}
    public void setType(String type){this.type=type;}

    public String getCsignature(){
        return csignature;
    }
    public void setCsignature(String csignature){
        this.csignature=csignature;
    }
}
