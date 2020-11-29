package com.example.loginapp.Model;

public class Sign_Up {
    private String userName,userNumber,userEmail;

    public Sign_Up() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Sign_Up(String userName, String userNumber, String userEmail) {
        this.userName = userName;
        this.userNumber = userNumber;
        this.userEmail = userEmail;
    }
}
