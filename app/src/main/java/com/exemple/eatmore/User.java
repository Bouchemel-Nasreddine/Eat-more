package com.exemple.eatmore;

import android.net.Uri;

public class User {
    private final String email;
    private final int user_ID;
    private final Uri picture;
    private final String phone;
    private boolean verifiedEmail;

    public User(String email, int user_ID, Uri picture, String phone) {
        this.email = email;
        this.user_ID = user_ID;
        this.picture = picture;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public int getUser_ID() {
        return user_ID;
    }

    public Uri getPicture() {
        return picture;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isVerifiedEmail() {
        return verifiedEmail;
    }

    public void setVerifiedEmail(boolean verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
    }
}
