package com.project.community.bean;

import java.io.Serializable;

/**
 * Created by fang on 2017/10/29.
 */

public class ArticleIndexBean implements Serializable {


    /**
     * id : 2
     * userName : 系统管理员
     * userPhoto : /upload/shequ_t32qroX7u03Y661kEV.jpg
     * title : 测试接口发布论坛
     * content : 测试接口发布论坛内容
     * imagesUrl : /static/images/userinfo.jpg
     * comments : 1
     * weight : 0
     * isShow : 0
     * audit_content : 审核回复内容
     * createDate : 10-18 16:45
     */

    private String id;
    private String userName;
    private String userPhoto;
    private String title;
    private String content;
    private String imagesUrl;
    private int comments;
    private int weight;
    private int isShow;
    private String audit_content;
    private String createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getAudit_content() {
        return audit_content;
    }

    public void setAudit_content(String audit_content) {
        this.audit_content = audit_content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
