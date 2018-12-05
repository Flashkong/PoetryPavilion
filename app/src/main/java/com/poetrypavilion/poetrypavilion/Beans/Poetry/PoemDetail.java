package com.poetrypavilion.poetrypavilion.Beans.Poetry;

import java.io.Serializable;

public class PoemDetail implements Serializable {
    //用户的头像的网络地址
    private String UserImgSrcLink;
    //用户头像的本地缓存地址，到时候就根据这个判断是默认的还是加载用户自己的头像
    //另外还需要判断文件是不是被删除了，如果删除了的话，也需要加载默认的头像
    private String UserLocalLink;
    public String getUserLocalLink() {
        return UserLocalLink;
    }

    public void setUserLocalLink(String userLocalLink) {
        UserLocalLink = userLocalLink;
    }


    //诗歌的题目
    private String Title;
    //诗歌的注释
    private String Note;
    //诗歌的创作者（诗人）
    private String Editor;
    //诗歌的内容
    private String Poetry;
    //诗歌的译文
    private String Translate;
    //诗歌的赏析
    private String shangxi;
    //诗歌的创作时间（朝代）
    private String Dynasty;
    //诗歌的喜欢总数
    private int LikeTotal;

    public String getUserImgSrcLink() {
        return UserImgSrcLink;
    }

    public void setUserImgSrcLink(String userImgSrcLink) {
        UserImgSrcLink = userImgSrcLink;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getEditor() {
        return Editor;
    }

    public void setEditor(String editor) {
        Editor = editor;
    }

    public String getPoetry() {
        return Poetry;
    }

    public void setPoetry(String poetry) {
        Poetry = poetry;
    }

    public String getTranslate() {
        return Translate;
    }

    public void setTranslate(String translate) {
        Translate = translate;
    }

    public String getShangxi() {
        return shangxi;
    }

    public void setShangxi(String shangxi) {
        this.shangxi = shangxi;
    }

    public String getDynasty() {
        return Dynasty;
    }

    public void setDynasty(String dynasty) {
        Dynasty = dynasty;
    }

    public int getLikeTotal() {
        return LikeTotal;
    }

    public void setLikeTotal(int likeTotal) {
        LikeTotal = likeTotal;
    }
}
