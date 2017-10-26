package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/7/21.
 * 用户
 */

public class SubdomainAccountModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    
//
//     "id": "4cf1d65a72284d7a8c5846ad62c20328",
//             "phone": "15850518962",
//             "name": "xiaoming",
//             "sumMoney": 1,
//             "sumOrder": 1,
//             "addGoods": 1,
//             "goodsMge": 1,
//             "orderMge": 1,
//             "shopInfo": 1

    public String id;//用户ID
    public String name;//姓名
    public String phone;//
    public String sumMoney;//
    public String sumOrder;//
    public String addGoods;//
    public String goodsMge;//
    public String orderMge;//
    public String shopInfo;//


    @Override
    public String toString() {
        return "SubdomainAccountModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", sumMoney='" + sumMoney + '\'' +
                ", sumOrder='" + sumOrder + '\'' +
                ", addGoods='" + addGoods + '\'' +
                ", goodsMge='" + goodsMge + '\'' +
                ", orderMge='" + orderMge + '\'' +
                ", shopInfo='" + shopInfo + '\'' +
                '}';
    }
}
