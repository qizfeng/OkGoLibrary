package com.project.community.bean;

/**
 * author：fangkai on 2017/10/30 13:59
 * em：617716355@qq.com
 */
public class RoomList {


    /**
     * id : 1
     * roomNo : 100001
     */

    private String id;
    private String roomNo;

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

    @Override
    public String toString() {
        return "RoomList{" +
                "id='" + id + '\'' +
                ", roomNo='" + roomNo + '\'' +
                '}';
    }
}
