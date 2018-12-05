package com.poetrypavilion.poetrypavilion.Beans.HttpBeans;

public class HttpGetPoemBean {
    //诗人头像连接
    private String user_avatar;
    //诗歌的题目

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

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
