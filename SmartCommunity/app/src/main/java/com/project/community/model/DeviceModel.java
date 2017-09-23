package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/9/21.
 * 物业设施
 */

public class DeviceModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    /*"id": "1ed133b1b9c44404bfbe44b3814de967",
      "facilitiesType": "1",#设施类型 	prop_facilities_type
      "dutyPhone": "123",#手机号
      "dutyPeople": "tony",#负责人
      "coordinate": "116.405293,39.912583",#坐标
      "installDate": "2017-09-15 00:00:00",#安装时间
      "dutyPhoto": "/static/images/d54_tx@3x.png"#头像

        "remarks": "",#备注
        "ownerCompany": "",所属单位
        "equipmentStatus": "正常",
        "facilitiesNo": ""设施编号，
        "scrapDate": ""#报废时间
      */
    public String id;
    public String facilitiesType;
    public String dutyPhone;
    public String dutyPeople;
    public String coordinate;
    public String installDate;
    public String dutyPhoto;
    public String remarks;
    public String ownerCompany;
    public String equipmentStatus;
    public String facilitiesNo;
    public String scrapDate;

    @Override
    public String toString() {
        return "DeviceModel{" +
                "id='" + id + '\'' +
                ", facilitiesType='" + facilitiesType + '\'' +
                ", dutyPhone='" + dutyPhone + '\'' +
                ", dutyPeople='" + dutyPeople + '\'' +
                ", coordinate='" + coordinate + '\'' +
                ", installDate='" + installDate + '\'' +
                ", dutyPhoto='" + dutyPhoto + '\'' +
                ", remarks='" + remarks + '\'' +
                ", ownerCompany='" + ownerCompany + '\'' +
                ", equipmentStatus='" + equipmentStatus + '\'' +
                ", facilitiesNo='" + facilitiesNo + '\'' +
                ", scrapDate='" + scrapDate + '\'' +
                '}';
    }
}
