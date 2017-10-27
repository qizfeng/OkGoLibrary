package com.project.community.model;


import android.os.Parcel;
import android.os.Parcelable;


import com.library.okgo.view.expandablerecyclerview.Parent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zipingfang on 17/10/17.
 */

public class ShoppingCartModel implements Serializable {
    private static final long serialVersionUID = 6753210234564872868L;
    public List<GoodsModel> goods = new ArrayList<>();
    public String cartId;
    public String shopName;
    public String totalPrice;
    public int goodsCount;

    public String id;
    public ShopModel shop;

//    {
//        "id": "1",
//            "shop": {
//        "id": "f99d9fc71bb4b01b90e42817ac955d6", #商铺id
//        "shopsName": "UGG图",#商铺名称
//        "goods": [
//        {
//            "id": "2607f45836b64194bd548e7b6a0022c0",
//                "name": "大盘鸡", #商品名称
//            "images": "jpg",#商品图片
//            "price": 20,#价格
//            "number": 1#数量
//        },
//        {
//            "id": "477c5c404d5f45829d0d00232be7e895",
//                "name": "酸菜鱼",
//                "images": "jpg",
//                "price": 10,
//                "number": 2
//        }
//                ]
//    }
//    }


    @Override
    public String toString() {
        return "ShoppingCartModel{" +
                "goods=" + goods +
                ", cartId='" + cartId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", goodsCount='" + goodsCount + '\'' +
                '}';
    }
}
