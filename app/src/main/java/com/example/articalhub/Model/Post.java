package com.example.articalhub.Model;

public class Post
{
    private String Title;
    private String Content;
    private String UserId;
    private String imageURL;
    Post()
    {

    }
    public Post(String Title, String Content, String UserId,String imageURL)
    {
    this.Title=Title;
    this.Content=Content;
    this.UserId=UserId;
    this.imageURL=imageURL;
    }

    public String getTitle()
    {
        return Title;
    }

    public void setTitle(String title)
    {
        Title = title;
    }

    public String getContent()
    {
        return Content;
    }

    public void setContent(String content)
    {
        Content = content;
    }

    public String getUserId()
    {
        return UserId;
    }

    public void setUserId(String userId)
    {
        UserId = userId;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }
}
