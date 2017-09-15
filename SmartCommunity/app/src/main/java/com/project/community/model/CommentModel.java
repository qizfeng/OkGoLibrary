package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/7/24.
 * 评论数据模型
 */

public class CommentModel implements Serializable{
    private static final long serialVersionUID = -4337711009801627866L;

    /*
       "articleId": "d71e40f0bbaa41c8b2fb0e971b97f680",#文章ID
       "id": "b6de7502b8924bb59e071da689f2e1e7",#评论ID
       "photo","/upload/shequ_T6Y6n3V7u18O6Q4T92.crop_photo.jpg",#用户头像
       "title": "气温骤降 注意保暖",#文章标题
       "userId": "",#用户姓名
       "content": "评论测试context",#评论内容
       "createDate": 1502700551000#评论时间
       "targetName": "tinnate",#回复目标昵称
       "targetId": "ab260af47403479dbed969eb81fc9953",#回复目标ID
    */

    public String id;
    public String articleId;
    public String photo;
    public String userId;
    public String userName;
    public String content;
    public String createDate;
    public String targetName;
    public String targetId;

    @Override
    public String toString() {
        return "CommentModel{" +
                "id='" + id + '\'' +
                ", articleId='" + articleId + '\'' +
                ", photo='" + photo + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", createDate='" + createDate + '\'' +
                ", targetName='" + targetName + '\'' +
                ", targetId='" + targetId + '\'' +
                '}';
    }
}
