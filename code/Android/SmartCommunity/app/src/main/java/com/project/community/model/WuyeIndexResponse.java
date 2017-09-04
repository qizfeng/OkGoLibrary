package com.project.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/9/4.
 */

public class WuyeIndexResponse implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    public List<MenuModel> menu = new ArrayList<>();
    public List<ArticleModel> artList = new ArrayList<>();

    @Override
    public String toString() {
        return "WuyeIndexResponse{" +
                "menu=" + menu +
                ", artList=" + artList +
                '}';
    }
}
