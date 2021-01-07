package com.example.articalhub.Model;

public class Muser
{
    private String email;
    private String userName;
    private String imageURL;
    private String userId;




    public Muser(String email, String userName, String imageURL, String userId)
    {
        this.email = email;
        this.userName = userName;
        this.imageURL = imageURL;
        this.userId = userId;


    }
    public Muser()
    {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
