package com.example.administrator.mimovie.bean;

/**
 * Created by YangYi on 2018/1/10.
 */

public class Comment {
    String userName;
    String headImgUrl;
    String commentContent;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "userName='" + userName + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", commentContent='" + commentContent + '\'' +
                '}';
    }
}
