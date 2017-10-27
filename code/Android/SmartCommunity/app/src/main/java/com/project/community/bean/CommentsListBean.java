package com.project.community.bean;

import java.util.List;

/**
 * author：fangkai on 2017/10/27 17:31
 * em：617716355@qq.com
 */
public class CommentsListBean {


    /**
     * total : 2
     * comments : [{"targetName":"tinnate","targetId":"ab260af47403479dbed969eb81fc9953","articleId":"66015af9bd1a4c99918e134208ad8ccf","photo":"/wisdomCommunitys/userfiles/1/images/photo/2017/08/20140528171306_hhsPm.jpeg","id":"1c9c84bf23724a988d588abf344ccc08","userName":"wisdom_admin","userId":"1","content":"11111111","createDate":"2017-10-18 09:17"}]
     */

    private int total;
    private List<CommentsBean> comments;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        /**
         * targetName : tinnate
         * targetId : ab260af47403479dbed969eb81fc9953
         * articleId : 66015af9bd1a4c99918e134208ad8ccf
         * photo : /wisdomCommunitys/userfiles/1/images/photo/2017/08/20140528171306_hhsPm.jpeg
         * id : 1c9c84bf23724a988d588abf344ccc08
         * userName : wisdom_admin
         * userId : 1
         * content : 11111111
         * createDate : 2017-10-18 09:17
         */

        private String targetName;
        private String targetId;
        private String articleId;
        private String photo;
        private String id;
        private String userName;
        private String userId;
        private String content;
        private String createDate;

        public String getTargetName() {
            return targetName;
        }

        public void setTargetName(String targetName) {
            this.targetName = targetName;
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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
            return "CommentsBean{" +
                    "targetName='" + targetName + '\'' +
                    ", targetId='" + targetId + '\'' +
                    ", articleId='" + articleId + '\'' +
                    ", photo='" + photo + '\'' +
                    ", id='" + id + '\'' +
                    ", userName='" + userName + '\'' +
                    ", userId='" + userId + '\'' +
                    ", content='" + content + '\'' +
                    ", createDate='" + createDate + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CommentsListBean{" +
                "total=" + total +
                ", comments=" + comments +
                '}';
    }
}
