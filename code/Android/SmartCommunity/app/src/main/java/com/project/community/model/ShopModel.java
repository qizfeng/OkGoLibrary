package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/9/21.
 * 商铺信息
 */

public class ShopModel implements Serializable {
    private static final long serialVersionUID = 6753210234564872868L;

    /*"id": "1",
            "shopsName": "霹雳之家",#商铺名称
        "shopPhoto":""#商铺照片
        "contactName": "三口剑",#联系人
        "contactPhone": "15823456783",#联系人电话
        "businessAddress": "",#营业地址
        "coordinate": "116.490865,39.948629",#坐标
        "shopsCategory": "餐饮",#分类
        "mainBusiness": "",#主营业务
        "entName": "霹雳国际",#企业名称
        "licenseNo": "100001",#营业执照
        "licensePositive": "",# 营业执照正面
        "licenseReverse": "",#营业执照反面
        "legalPerson": "黄文择",#法人姓名
        "legalCardPositive": "",#法人身份证正面
        "legalCardReverse": "",#法人身份证反面
        "auditStatus": 1#审核状态
        */
    public String id;
    public String shopsName;
    public String coordinate;
    public String auditStatus;
    public String shopPhoto;
    public String contactName;
    public String contactPhone;
    public String businessAddress;
    public String shopsCategory;
    public String mainBusiness;
    public String licenseNo;
    public String entName;
    public String licensePositive;
    public String licenseReverse;
    public String legalPerson;
    public String legalCardPositive;
    public String legalCardReverse;
    public String distance;
    public double starLevel;

    @Override
    public String toString() {
        return "ShopModel{" +
                "id='" + id + '\'' +
                ", shopsName='" + shopsName + '\'' +
                ", coordinate='" + coordinate + '\'' +
                ", auditStatus='" + auditStatus + '\'' +
                ", shopPhoto='" + shopPhoto + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", businessAddress='" + businessAddress + '\'' +
                ", shopsCategory='" + shopsCategory + '\'' +
                ", mainBusiness='" + mainBusiness + '\'' +
                ", licenseNo='" + licenseNo + '\'' +
                ", entName='" + entName + '\'' +
                ", licensePositive='" + licensePositive + '\'' +
                ", licenseReverse='" + licenseReverse + '\'' +
                ", legalPerson='" + legalPerson + '\'' +
                ", legalCardPositive='" + legalCardPositive + '\'' +
                ", legalCardReverse='" + legalCardReverse + '\'' +
                ", distance='" + distance + '\'' +
                ", starLevel='" + starLevel + '\'' +
                '}';
    }
}
