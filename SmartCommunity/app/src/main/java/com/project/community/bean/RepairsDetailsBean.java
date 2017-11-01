package com.project.community.bean;

import java.util.List;

/**
 * author：fangkai on 2017/11/1 14:31
 * em：617716355@qq.com
 */
public class RepairsDetailsBean {


    /**
     * commentList : [{"createDate":"2017-10-19 10:49:51","starLevel":"4","content":"shi wo "}]
     * reply : [{"id":"1","createDate":"2017-10-19 14:45:58","orderNo":"870361815284547176","imagesUrl":"","content":""}]
     * process : [{"value":"提交成功","date":"2017/08/18 11:01:49"},{"value":"指派时间","date":"2017/08/18 11:01:49"}]
     * order : {"orderType":"水","imagesUrl":"jpg","coordinate":"116.4014310,39.9147485","handleTime":"","content":"宝宝😊","orderStatus":3,"submitTime":"2017/10/19 14:18:53","roomAddress":"阳光小区2号楼1单元1808室","isOwner":"1","bespeakDate":"2017/08/18 11:01:49","endTime":"","createDate":"2017-10-19 14:18:53","repairEndTime":"2017/11/01 10:24:01","repair":{"name":"说的","no":"撒打算","photo":"撒打算"}}
     */

    private OrderBean order;
    private List<CommentListBean> commentList;
    private List<ReplyBean> reply;
    private List<ProcessBean> process;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public List<CommentListBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentListBean> commentList) {
        this.commentList = commentList;
    }

    public List<ReplyBean> getReply() {
        return reply;
    }

    public void setReply(List<ReplyBean> reply) {
        this.reply = reply;
    }

    public List<ProcessBean> getProcess() {
        return process;
    }

    public void setProcess(List<ProcessBean> process) {
        this.process = process;
    }

    public static class OrderBean {
        /**
         * orderType : 水
         * imagesUrl : jpg
         * coordinate : 116.4014310,39.9147485
         * handleTime :
         * content : 宝宝😊
         * orderStatus : 3
         * submitTime : 2017/10/19 14:18:53
         * roomAddress : 阳光小区2号楼1单元1808室
         * isOwner : 1
         * bespeakDate : 2017/08/18 11:01:49
         * endTime :
         * createDate : 2017-10-19 14:18:53
         * repairEndTime : 2017/11/01 10:24:01
         * repair : {"name":"说的","no":"撒打算","photo":"撒打算"}
         */

        private String orderType;
        private String imagesUrl;
        private String coordinate;
        private String handleTime;
        private String content;
        private String orderStatus;
        private String submitTime;
        private String roomAddress;
        private String isOwner;
        private String bespeakDate;
        private String endTime;
        private String createDate;
        private String repairEndTime;
        private String orderNo;;
        private RepairBean repair;
        private String roomNo;
        private String repairStatus;
        private String userName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRepairStatus() {
            return repairStatus;
        }

        public void setRepairStatus(String repairStatus) {
            this.repairStatus = repairStatus;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getImagesUrl() {
            return imagesUrl;
        }

        public void setImagesUrl(String imagesUrl) {
            this.imagesUrl = imagesUrl;
        }

        public String getCoordinate() {
            return coordinate;
        }

        public void setCoordinate(String coordinate) {
            this.coordinate = coordinate;
        }

        public String getHandleTime() {
            return handleTime;
        }

        public void setHandleTime(String handleTime) {
            this.handleTime = handleTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getSubmitTime() {
            return submitTime;
        }

        public void setSubmitTime(String submitTime) {
            this.submitTime = submitTime;
        }

        public String getRoomAddress() {
            return roomAddress;
        }

        public void setRoomAddress(String roomAddress) {
            this.roomAddress = roomAddress;
        }

        public String getIsOwner() {
            return isOwner;
        }

        public void setIsOwner(String isOwner) {
            this.isOwner = isOwner;
        }

        public String getBespeakDate() {
            return bespeakDate;
        }

        public void setBespeakDate(String bespeakDate) {
            this.bespeakDate = bespeakDate;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getRepairEndTime() {
            return repairEndTime;
        }

        public void setRepairEndTime(String repairEndTime) {
            this.repairEndTime = repairEndTime;
        }

        public RepairBean getRepair() {
            return repair;
        }

        public void setRepair(RepairBean repair) {
            this.repair = repair;
        }

        public static class RepairBean {
            /**
             * name : 说的
             * no : 撒打算
             * photo : 撒打算
             */

            private String name;
            private String no;
            private String photo;
            private String userType;
            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }



            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNo() {
                return no;
            }

            public void setNo(String no) {
                this.no = no;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }
        }
    }

    public static class CommentListBean {
        /**
         * createDate : 2017-10-19 10:49:51
         * starLevel : 4
         * content : shi wo
         */

        private String createDate;
        private String starLevel;
        private String content;

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getStarLevel() {
            return starLevel;
        }

        public void setStarLevel(String starLevel) {
            this.starLevel = starLevel;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class ReplyBean {
        /**
         * id : 1
         * createDate : 2017-10-19 14:45:58
         * orderNo : 870361815284547176
         * imagesUrl :
         * content :
         */

        private String id;
        private String createDate;
        private String orderNo;
        private String imagesUrl;
        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getImagesUrl() {
            return imagesUrl;
        }

        public void setImagesUrl(String imagesUrl) {
            this.imagesUrl = imagesUrl;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class ProcessBean {
        /**
         * value : 提交成功
         * date : 2017/08/18 11:01:49
         */

        private String value;
        private String date;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
