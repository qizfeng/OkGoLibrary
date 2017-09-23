package com.project.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/9/21.
 * 社区商铺筛选
 */

public class CommunityShopFilterModel implements Serializable{
    private static final long serialVersionUID = -4337711009801627866L;
    public List<DictionaryModel> range = new ArrayList<>();
    public List<DictionaryModel> status=new ArrayList<>();

    @Override
    public String toString() {
        return "CommunityShopFilterModel{" +
                "range=" + range +
                ", status=" + status +
                '}';
    }
}
