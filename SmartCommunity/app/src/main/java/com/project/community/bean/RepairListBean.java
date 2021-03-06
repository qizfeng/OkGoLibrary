package com.project.community.bean;

import java.util.List;

/**
 * author：fangkai on 2017/10/31 16:58
 * em：617716355@qq.com
 */
public class RepairListBean {


    /**
     * list : [{"orderType":"水","orderNo":"38710001063316626581","roomAddress":"阳光小区5号楼2单元202室","orderId":4,"phone":"15850518962","createDate":"2017-10-19 14:46"},{"orderType":"水","orderNo":"870361815284547176","roomAddress":"阳光小区2号楼1单元1808室","orderId":3,"phone":"15850518962","createDate":"2017-10-19 14:18"}]
     * workStatus : 1
     */

    private String workStatus;
    private List<ListBean> list;

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * orderType : 水
         * orderNo : 38710001063316626581
         * roomAddress : 阳光小区5号楼2单元202室
         * orderId : 4
         * phone : 15850518962
         * createDate : 2017-10-19 14:46
         */

        private String orderType;
        private String orderNo;
        private String roomAddress;
        private int orderId;
        private String phone;
        private String createDate;

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getRoomAddress() {
            return roomAddress;
        }

        public void setRoomAddress(String roomAddress) {
            this.roomAddress = roomAddress;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}
