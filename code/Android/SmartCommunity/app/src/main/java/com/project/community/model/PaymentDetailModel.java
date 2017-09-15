package com.project.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/9/5.
 * 缴费详情
 */

public class PaymentDetailModel implements Serializable {
    private static final long serialVersionUID = 6753210234564872868L;
    /*"id": "0de9c2882641409b9cf985a413636cc2",
      "room": {
      "roomNo": "100001",
      "address": "阳光社区1号楼2单元1808室"
      },
      "payMoney": "74",   #缴费金额
      "payMouth": "2017年3月" #应缴费时间
      paymentList": [ ] #历史记录
      */

    private String id;
    private String payMoney;
    private String payMouth;
    private HouseModel room = new HouseModel();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayMouth() {
        return payMouth;
    }

    public void setPayMouth(String payMouth) {
        this.payMouth = payMouth;
    }

    public HouseModel getRoom() {
        return room;
    }
    public void setRoom(HouseModel room) {
        this.room = room;
    }

}
