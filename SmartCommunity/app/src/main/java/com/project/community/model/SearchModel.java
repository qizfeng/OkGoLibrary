package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qifeng on 17/8/13.
 * 搜搜结果模型
 */

public class SearchModel implements Serializable{
    private static final long serialVersionUID = 6753210234564872868L;
    /*"imageWidth": 0,#图片宽度
     "image": "",#图片路径
     "articleName": "政府公告",#类型名称
     "comments": 2,#评论数量
     "collections": 0,#收藏数量
     "description": "不缴费就停电了",#文章描述
     "weight": 0,#权重；999hot
     "stauts": 0,#收藏状态 搜索不查询
     "id": "362dce2917544782836420f29cb3071f",#文章ID
     "title": "交水电费了",#标题
     "categoryId": "3",#类型ID
     "imageHeight": 0#图片高度
     "createDate": 1503969902000,#发布时间
     "weightDate": ""#过期时间
     */
    public String imageWidth;
    public String image;
    public String articleName;
    public String comments;
    public String collections;
    public String description;
    public String weight;
    public String status;
    public String id;
    public String title;
    public String categoryId;
    public String imageHeight;
    public String createDate;
    public String weightDate;

    @Override
    public String toString() {
        return "SearchModel{" +
                "imageWidth='" + imageWidth + '\'' +
                ", image='" + image + '\'' +
                ", articleName='" + articleName + '\'' +
                ", comments='" + comments + '\'' +
                ", collections='" + collections + '\'' +
                ", description='" + description + '\'' +
                ", weight='" + weight + '\'' +
                ", status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", imageHeight='" + imageHeight + '\'' +
                ", createDate='" + createDate + '\'' +
                ", weightDate='" + weightDate + '\'' +
                '}';
    }
}
