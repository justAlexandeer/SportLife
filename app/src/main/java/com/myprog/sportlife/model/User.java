package com.myprog.sportlife.model;

public class User {
    public static String id;
    public static int countTraining;
    private String login;
    private String password;

    public User(){
    }

    public User(String id, String login, String password){
        User.id = id;
        this.login = login;
        this.password = password;
        User.countTraining = 0;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getCountTraining(){
        return User.countTraining;
    }

}
