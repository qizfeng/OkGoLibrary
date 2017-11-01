package com.project.community.model;


import java.io.Serializable;
import java.util.List;

/**
 * Created by zipingfang on 17/10/17.
 */

public class OrderModel implements Serializable {
    private static final long serialVersionUID = 6753210234564872868L;
//    {
//        "createDate": "2017-10-26 14:46:41",
//            "orderNo": "14440475711889781680",
//            "orderStatus": "0", #未发货:1：已发货:2：已完成,3：退货申请,4：退货中,5：已退货,
//            "shopId": "22a30c4787094e1d9e0935c7fda70e55",
//            "shopsName": "居家旅行",
//            "goodsCount": 1,
//            "orderAmountTotal": 10,
//            "address": { #收货地址
//        "consignee": "张文文",#收货人
//        "contactPhone": "",#联系方式
//        "address": "营策巷26号1"#地址
//    },
//        "detailList": [#商品列表
//        {
//            "goodId": "2607f45836b64194bd548e7b6a0022c0",
//                "goodName": "酸菜鱼",
//                "goodImage": "imddg",
//                "goodPrice": 10,
//                "number": 1
//        }
//            ]
//    }
    public String createDate ;
    public String orderNo ;
    public String orderStatus ;
    public String shopId ;
    public String shopsName ;
    public int goodsCount ;
    public int orderAmountTotal ;
    public AddressModel address ;
    public List<GoodsModel> detailList ;

    @Override
    public String toString() {
        return "OrderModel{" +
                "createDate='" + createDate + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", shopId='" + shopId + '\'' +
                ", shopsName='" + shopsName + '\'' +
                ", goodsCount=" + goodsCount +
                ", orderAmountTotal=" + orderAmountTotal +
                ", address=" + address +
                ", detailList=" + detailList +
                '}';
    }
}
