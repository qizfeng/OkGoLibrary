package com.project.community.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zipingfang on 17/10/17.
 */
public class GoodsModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    public String goodsId;
    public String goodsImage;
    public int res;
    public String goodsName;
    public int goodsPrice;
    public int goodsCount;
    public int originalPrice;
    public int star;
    public int parentPosition;
    public int childPosition;
    public int partentId;
    public int childId;


    public String id;
    public String name;
    public String images;
    public String price;
    public int number;

// "id": "477c5c404d5f45829d0d00232be7e895",
//         "name": "酸菜鱼",
//         "images": "jpg",
//         "price": 10,
//         "number": 2

    @Override
    public String toString() {
        return "GoodsModel{" +
                "goodsId='" + goodsId + '\'' +
                ", goodsImage='" + goodsImage + '\'' +
                ", res=" + res +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice='" + goodsPrice + '\'' +
                ", originalPrice='" + originalPrice + '\'' +
                ", star='" + star + '\'' +
                ", goodsCount='" + goodsCount + '\'' +
                ", parentPosition='" + parentPosition + '\'' +
                ", childPosition='" + childPosition + '\'' +
                ", partentId='" + partentId + '\'' +
                ", childId='" + childId + '\'' +
                '}';

    }


}
