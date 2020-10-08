package com.example.masknotifier;

public class User {
    private String email;
    private String password;
    private String timeStamp;

    private static User userInstance;

    private User(){

    }

    public static User getInstance(){
        if (userInstance == null){ //if there is no instance available... create new one
            userInstance = new User();
        }

        return userInstance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String name) {
        this.email = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
