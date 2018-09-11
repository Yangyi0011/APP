package com.example.administrator.mimovie.util;



import com.example.administrator.mimovie.bean.Comment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YangYi on 2018/1/10.
 */

public class CommentUtil {

    public static Comment getComment(JSONObject jsonObject){
        Comment comment = new Comment();
        try {
            String userName = jsonObject.getString("nickname");
            String headImgUrl = jsonObject.getString("headImg");
            String commentContent = jsonObject.getString("content");

            comment.setUserName(userName);      //用户昵称
            comment.setHeadImgUrl(headImgUrl);  //用户头像
            comment.setCommentContent(commentContent);  //评论内容
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comment;
    }
}
