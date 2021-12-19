package com.example.login_signup;

public class user {
    public String  name ;
    public String id;
    public String  email;
    public String pw;
    public String phno ;
    public String about ;
    public String imageuri ;
    public String lastseen;
    public String lastmsg;
    public String lasttyp;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getLasttyp() {
        return lasttyp;
    }

    public void setLasttyp(String lasttyp) {
        this.lasttyp = lasttyp;
    }

    public user(String name, String email, String pw, String phno, String about, String id, String imageuri, String lastseen,
                String lastmsg, String lasttyp) {
        this.name = name;
        this.email = email;
        this.pw = pw;
        this.phno = phno;
        this.about = about;
        this.imageuri = imageuri;
        this.id = id;
        this.lastseen = lastseen;
        this.lastmsg = lastmsg;
        this.lasttyp = lasttyp;

    }

    public String getLastmsg() {
        return lastmsg;
    }

    public void setLastmsg(String lastmsg) {
        this.lastmsg = lastmsg;
    }

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    public String getId() {
        return id;
    }

    public String setId(String id) {
        this.id = id;
        return id;
    }

    public user(){}

}
