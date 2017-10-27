package com.project.community.bean;

import java.util.List;

/**
 * author：fangkai on 2017/10/27 15:12
 * em：617716355@qq.com
 */
public class BbsBean {

    private List<?> category;
    private List<ArtListBean> artList;

    public List<?> getCategory() {
        return category;
    }

    public void setCategory(List<?> category) {
        this.category = category;
    }

    public List<ArtListBean> getArtList() {
        return artList;
    }

    public void setArtList(List<ArtListBean> artList) {
        this.artList = artList;
    }

    public static class ArtListBean {
        /**
         * imagesUrl : eeee
         * comments : 1
         * userPhoto : /upload/shequ_707m9zVm94851fyS80.crop_photo.jpg
         * collections : 0
         * id : 74c074fc7e164c5082e6d9321f930dc3
         * title : cu chulainn
         * userName : chulain
         * categoryId : 1
         * content : i deep to
         * isShow : 0
         * createDate : 10-18 09:17
         * status : 0
         */

        private String imagesUrl;
        private int comments;
        private String userPhoto;
        private int collections;
        private String id;
        private String title;
        private String userName;
        private int categoryId;
        private String content;
        private int isShow;
        private String createDate;
        private int status;

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

        public String getUserPhoto() {
            return userPhoto;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
        }

        public int getCollections() {
            return collections;
        }

        public void setCollections(int collections) {
            this.collections = collections;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
