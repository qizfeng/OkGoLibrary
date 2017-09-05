package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/28.
 * 文章Model
 */

public class ArticleModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;

    /*"id": "299f2f4f5e84415f98b03606bf6427d9",#文章ID
       "createDate": "2017-08-29 01:23:38",#创建时间
       "updateDate": "2017-08-29 01:23:38",#更新时间
       "category": {#类别信息
       "id": "3",#类别ID
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
               "parentId": "0"
   },
           "title": "123231313",#文章标题
       "image": "",#文章图片地址
       "description": "TUEIR",#文章描述
       "weight": 0,#权重，越大越靠前
       "hits": 0,#点击数
       "posid": "",#推荐位，多选
       "sysOrg": {#小区信息
       "orgCode": "650422100007000",#小区编号
       "orgName": "阳光社区"#小区名称
   },
           "surveyInfo": {#问卷信息
       "id": "",#问卷ID
       "isPublic": 0,#默认0：所有人可看；1：用户投票后不可看结果
       "timeInterval": 0#投票间隔 为0 则只能投票一次
   },
           "imageWidth": "0",#图片宽度
       "imageHeight": "0",#图片高度
       "user": {
       "name": "系统管理员",#用户名称
       "loginFlag": "0",#是否可登录 0：禁用；1：正常  字典：sys_user_login_status
       "sex": "1",#性别1：男；2女  字典：sex
       "admin": false,
               "roleNames": ""
   },
           "collections": 1,#收藏数量
       "comments": 0,#评论数量
       "allowComment":1,#是否允许评论
       "allowCollection":1,#是否允许收藏
       "status",0,#收藏状态
       "url": "/f/view-3-299f2f4f5e84415f98b03606bf6427d9.html",#文章URL地址
       "posidList": []
       surveyId 问卷id
       */
    public String id;
    public String image;
    public String articleName;
    public int comments;
    public int collections;
    public int weight;
    public int status;//收藏状态 0未收藏 1已收藏
    public String title;
    public String categoryId;
    public String imageHeight;
    public String url;
    public int allowComment;
    public int allowCollection;

    public String createDate;
    public String weightDate;
    public String updateDate;
    public ArticleCategoryModel category = new ArticleCategoryModel();
    public String description;
    public int hits;
    public String posid;
    public String surveyId;
    public SysOrgModel sysOrg = new SysOrgModel();
    public SurveyInfoModel surveyInfo = null;
}
