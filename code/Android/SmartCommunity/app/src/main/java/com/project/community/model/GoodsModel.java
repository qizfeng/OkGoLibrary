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
