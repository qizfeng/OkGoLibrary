package com.project.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/8/28.
 * 政务首页Model
 */

public class ZhengwuIndexResponse implements Serializable{
    private static final long serialVersionUID = -4337711009801627866L;
    public List<MenuModel> menu = new ArrayList<>();
    public List<ArticleModel> artList = new ArrayList<>();

    @Override
    public String toString() {
        return "ZhengwuIndexResponse{" +
                "menu=" + menu +
                ", artList=" + artList +
                '}';
    }
}
