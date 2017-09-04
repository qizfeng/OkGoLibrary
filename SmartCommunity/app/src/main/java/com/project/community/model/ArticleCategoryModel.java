package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/9/1.
 */

public class ArticleCategoryModel implements Serializable{
    /*"id": "3",#类别ID
      "name": "政府公告",#类别名称
      "sort": 30,#排序字段
      "module": "",#栏目模块
      "inMenu": "0",#是否在导航中显示
      "inList": "1",#是否在分类页中显示列表
      "showModes": "0",#展现方式
      "allowComment": "1",#是否允许评论
      "isAudit": "0",#是否需要审核
      "categoryType": "0",#文章分类类型：0：政务；1：物业
      "childList": [],
      "url": "/f/list-3.html",
      "ids": "3",
      "root": false,
      "parentId": "0"*/
    public String id;
    public String name;
    public String sort;
    public String module;
    public String inMenu;
    public String inList;
    public String showModes;
    public String allowComment;
    public String isAudit;
    public String categoryType;
    public String url;
    public String ids;

    @Override
    public String toString() {
        return "ArticleCategoryModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sort='" + sort + '\'' +
                ", module='" + module + '\'' +
                ", inMenu='" + inMenu + '\'' +
                ", inList='" + inList + '\'' +
                ", showModes='" + showModes + '\'' +
                ", allowComment='" + allowComment + '\'' +
                ", isAudit='" + isAudit + '\'' +
                ", categoryType='" + categoryType + '\'' +
                ", url='" + url + '\'' +
                ", ids='" + ids + '\'' +
                '}';
    }
}
