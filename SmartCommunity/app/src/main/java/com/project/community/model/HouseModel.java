package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/9/5.
 * 房屋
 */

public class HouseModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    public String id;
    public String roomNo;
    public String address;
//    public HouseModel room = new HouseModel();

    public HouseModel() {
    }

    public HouseModel(String id, String roomNo, String address) {
        this.id = id;
        this.roomNo = roomNo;
        this.address = address;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "HouseModel{" +
                "id='" + id + '\'' +
                ", roomNo='" + roomNo + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
