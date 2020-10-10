package com.example.masknotifier.model;

public class UserDetails {
    private String Uid;
    private static final UserDetails userInstance = new UserDetails();

    public static UserDetails getUserInstance(){
        return(userInstance);
    }

    public UserDetails(){

    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String Uid) {
        this.Uid = Uid;
    }
}
