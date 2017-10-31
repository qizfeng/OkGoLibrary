package com.project.community.bean;

/**
 * author：fangkai on 2017/10/31 16:58
 * em：617716355@qq.com
 */
public class RepairListBean {


    /**
     * orderType : 水
     * orderNo : 870361815284547176
     * roomAddress : 阳光小区2号楼1单元1808室
     * orderId : 3
     * phone : 15850518962
     * createDate : 2017-10-19 14:18
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
