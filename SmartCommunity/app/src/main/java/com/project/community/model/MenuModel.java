package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/28.
 * 功能菜单Model
 */

public class MenuModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    /*"id": "31c6b5ddfd314baf9ede768295356477", #菜单ID
                "isNewRecord": false,
                        "remarks": "", #备注
                "createDate": "2017-08-08 06:54:17", #创建时间
                "updateDate": "2017-08-08 06:56:03", #更新时间
                "userCodeLevel": null,
                        "value": "1", #菜单编号
                "label": "公告", #菜单名称
                "type": "gov_menu", #菜单类型
                "description": "政务菜单", #描述
                "sort": 10, #排序
                "parentId": "0" #上级菜单 0：最上级*/
    public String id;//菜单id
    public String isNewRecord;
    public String remarks;//备注
    public String createDate;//创建时间
    public String updateDate;//更新时间
    public String userCodeLevel;
    public String value;//菜单编号
    public String label;//菜单名称
    public String type;//菜单名称
    public String description;//描述
    public String sort;//排序
    public String parentId;//上级菜单 0最上级

    @Override
    public String toString() {
        return "MenuModel{" +
                "id='" + id + '\'' +
                ", isNewRecord='" + isNewRecord + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", userCodeLevel='" + userCodeLevel + '\'' +
                ", value='" + value + '\'' +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", sort='" + sort + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}
