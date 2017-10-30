package com.project.community.bean;

/**
 * Created by fang on 2017/10/29.
 */

public class ArticleBean {


    /**
     * userPhoto : /upload/shequ_707m9zVm94851fyS80.crop_photo.jpg
     * imageUrl : jpg
     * userName : chulain
     * title : cu chulainn
     * content : i deep to
     * createDate : 10-17 16:29
     */

    private String userPhoto;
    private String imageUrl;
    private String userName;
    private String title;
    private String content;
    private String createDate;
    private int comments;
    private int status;
    private int categoryId;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "ArticleBean{" +
                "userPhoto='" + userPhoto + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createDate='" + createDate + '\'' +
                ", comments=" + comments +
                ", status=" + status +
                '}';
    }
}
