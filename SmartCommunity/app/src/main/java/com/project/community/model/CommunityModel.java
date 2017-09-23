package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/9/20.
 * 小区Model
 */

public class CommunityModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    /*"orgCode": "650422100007001",#组织机构编码
      "orgName": "阳光小区",#组织机构名称
      "neCoordinate": "116.412178,39.911051",#东北坐标
      "swCoordinate": "116.4119,39.91094"#西南坐标
      "color":"#5cffffff"小区背景色
      */
    public String orgCode;
    public String orgName;
    public String neCoordinate;
    public String  swCoordinate;
    public String color;

    @Override
    public String toString() {
        return "CommunityModel{" +
                "orgCode='" + orgCode + '\'' +
                ", orgName='" + orgName + '\'' +
                ", neCoordinate=" + neCoordinate +
                ", swCoordinate=" + swCoordinate +
                ", color=" + color +
                '}';
    }
}
