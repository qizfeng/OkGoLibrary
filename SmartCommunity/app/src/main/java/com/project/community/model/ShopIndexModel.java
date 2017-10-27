package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/9/21.
 * 商铺信息
 */

public class ShopIndexModel implements Serializable {
    private static final long serialVersionUID = 6753210234564872868L;

//   "moneyTotal": 16546,#总金额
//        "isOpen": 0,#是否营业
//        "todayOrder": 23,#今天订单
//        "handleOrder": 4,#待处理订单
//        "shopId": "1",#商铺id
//        "orderTotal": 323,#总订单
//        "todayMoney": 1000#今天流水
    public String moneyTotal;
    public int isOpen;
    public int isChild;
    public String todayOrder;
    public int handleOrder;
    public String shopId;
    public String orderTotal;
    public String todayMoney;

    @Override
    public String toString() {
        return "ShopIndexModel{" +
                "moneyTotal='" + moneyTotal + '\'' +
                ", isOpen='" + isOpen + '\'' +
                ", todayOrder='" + todayOrder + '\'' +
                ", handleOrder='" + handleOrder + '\'' +
                ", shopId='" + shopId + '\'' +
                ", orderTotal='" + orderTotal + '\'' +
                ", todayMoney='" + todayMoney + '\'' +
                '}';
    }
}
