package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/7/27.
 * 分类
 */

public class CategoryModel implements Serializable{
    private static final long serialVersionUID = -4337711009801627866L;
    public String id;
    public String title;
    public int sort;
    public int state;//控制按钮显示状态 -1:默认不可编辑 0:我的分类 1:未添加的 2:已添加的
    public String icon;
    public int res;
    public boolean isShowEdit;
    public boolean isAdd;
    public CategoryModel(String id, String title, int sort,int state) {
        this.id = id;
        this.title = title;
        this.sort = sort;
        this.state=state;
    }

    public CategoryModel(String id, String title, int sort, int state, int res) {
        this.id = id;
        this.title = title;
        this.sort = sort;
        this.state = state;
        this.res = res;
    }

    public CategoryModel(String id, String title, int sort, int state, String icon, int res) {
        this.id = id;
        this.title = title;
        this.sort = sort;
        this.state = state;
        this.icon = icon;
        this.res = res;
    }

    public CategoryModel(String id, String title, int sort, int state, String icon, int res,boolean isShowEdit,boolean isAdd) {
        this.id = id;
        this.title = title;
        this.sort = sort;
        this.state = state;
        this.icon = icon;
        this.res = res;
        this.isShowEdit=isShowEdit;
        this.isAdd=isAdd;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", sort=" + sort +
                ", state=" + state +
                '}';
    }
}
