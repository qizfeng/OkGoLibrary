package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/9/21.
 * 商铺信息
 */

public class ChildShopIndexModel implements Serializable {
    private static final long serialVersionUID = 6753210234564872868L;
//  0：有；1：否
//     "sumMoney": 1,#查看价格
//                    "sumOrder": 1,#总订单数
//                    "addGoods": 1,#添加商品
//                    "goodsMge": 1,#商品管理
//                    "orderMge": 1,#订单管理
//                    "shopInfo": 1#商铺资料

    public int sumMoney;
    public int sumOrder;
    public int addGoods;
    public int goodsMge;
    public int orderMge;
    public int shopInfo;


    @Override
    public String toString() {
        return "ChildShopIndexModel{" +
                "sumMoney=" + sumMoney +
                ", sumOrder=" + sumOrder +
                ", addGoods=" + addGoods +
                ", goodsMge=" + goodsMge +
                ", orderMge=" + orderMge +
                ", shopInfo=" + shopInfo +
                '}';
    }
}
