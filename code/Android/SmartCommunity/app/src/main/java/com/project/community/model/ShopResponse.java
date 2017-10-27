package com.project.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/10/26.
 * 民生-附近商家
 */

public class ShopResponse implements Serializable {
    private static final long serialVersionUID = 6753210234564872868L;
    public int cartTotal;
    public List<ShopModel> shopList= new ArrayList<>();

    @Override
    public String toString() {
        return "ShopResponse{" +
                "cartTotal=" + cartTotal +
                ", shopList=" + shopList +
                '}';
    }
}
