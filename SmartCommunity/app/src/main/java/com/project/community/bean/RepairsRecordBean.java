package com.project.community.bean;

/**
 * author：fangkai on 2017/10/30 11:31
 * em：617716355@qq.com
 */
public class RepairsRecordBean {


    /**
     * createDate : 2017-10-18 15:09:02
     * orderNo : 39028892731590527945
     * orderType : 水
     * typeIcon :
     * orderStatus : 已处理
     * roomNo : 100002
     * roomAddress : 阳光小区5号楼2单元202室
     * repairStatus : 0
     */

    private String createDate;
    private String orderNo;
    private String orderType;
    private String typeIcon;
    private String orderStatus;
    private String roomNo;
    private String roomAddress;
    private String repairStatus;
    private String handleStatus;

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTypeIcon() {
        return typeIcon;
    }

    public void setTypeIcon(String typeIcon) {
        this.typeIcon = typeIcon;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomAddress() {
        return roomAddress;
    }

    public void setRoomAddress(String roomAddress) {
        this.roomAddress = roomAddress;
    }

    public String getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus;
    }
}
