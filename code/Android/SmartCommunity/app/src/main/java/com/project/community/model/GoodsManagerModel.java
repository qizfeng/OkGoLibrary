package com.project.community.model;

import java.io.Serializable;

/**
 * Created by cj on 17/10/26.
 */
public class GoodsManagerModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;

//    {
//        "id": "4",
//            "name": "酸菜鱼",
//            "images": "jpg",#图片
//        "price": 10,#价格
//        "stock": 999,#库存
//        "sales": 0#销量
//     'description':""#内容
//            "unit":""#单位
//            "originalPrice":10#原价
//    }

    public String id;
    public String name;
    public String images;
    public String price;
    public String stock;
    public String sales;
    public String description;
    public String unit;
    public String originalPrice;
    public boolean open=false;

    @Override
    public String toString() {
        return "GoodsManagerModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", images='" + images + '\'' +
                ", price='" + price + '\'' +
                ", stock='" + stock + '\'' +
                ", sales='" + sales + '\'' +
                '}';
    }
}
