package com.project.community.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zipingfang on 17/10/17.
 */

public class MerchantDeailModel implements Serializable {
    private static final long serialVersionUID = 6753210234564872868L;
    public int cartTotal ;

    public ShopModel shop;

    public List<GoodsModel> goodsList;

    @Override
    public String toString() {
        return "MerchantDeailModel{" +
                "cartTotal=" + cartTotal +
                ", shop=" + shop +
                ", goodsList=" + goodsList +
                '}';
    }
}
