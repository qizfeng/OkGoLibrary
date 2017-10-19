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
