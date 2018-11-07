package com.poetrypavilion.poetrypavilion.Beans.Poetry;

public class PoemConcision {
    private String ImgSrc;
    private String UserName;
    private String UpLodeTime;
    private String PoemTitle;
    private String PoemText;

    public String getPoemTitle() {
        return PoemTitle;
    }

    public void setPoemTitle(String poemTitle) {
        PoemTitle = poemTitle;
    }

    public String getPoemText() {
        return PoemText;
    }

    public void setPoemText(String poemText) {
        PoemText = poemText;
    }

    public String getImgSrc() {
        return ImgSrc;
    }

    public void setImgSrc(String imgSrc) {
        ImgSrc = imgSrc;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUpLodeTime() {
        return UpLodeTime;
    }

    public void setUpLodeTime(String upLodeTime) {
        UpLodeTime = upLodeTime;
    }

    public PoemConcision(String UserName,String UpLodeTime, String PoemTitle,String PoemText){
        this.UserName=UserName;
        this.PoemTitle=PoemTitle;
        this.PoemText=PoemText;
        this.UpLodeTime=UpLodeTime;
    }
}
