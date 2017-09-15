package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/7/31.
 * 问卷模型
 */

public class WenjuanModel implements Serializable{
    private static final long serialVersionUID = -4337711009801627866L;
    public int id;

    /*"id": "5f5adcac4f9949d0830cb3fcc415d705",#问卷ID
      "remarks": "",
      "endTime": 1503417600000, #问卷截至时间
      "surveyName": "每两天统计", #文件名称
      "orgCode": "650422100007000", #问卷所属机构
      "isPublic": 0,#默认0：所有人可看；1：用户投票后不可看结果
      "timeUnit": 2,#投票间隔单位 字典维护
      "timeInterval": 2#投票间隔 为0 则只能投票一次
      */
    public String remarks;
    public String endTime;
    public String surveyName;
    public String orgCode;
    public String isPublic;
    public String timeUnit;
    public String timeInterval;

    @Override
    public String toString() {
        return "WenjuanModel{" +
                "id=" + id +
                ", remarks='" + remarks + '\'' +
                ", endTime='" + endTime + '\'' +
                ", surveyName='" + surveyName + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", isPublic='" + isPublic + '\'' +
                ", timeUnit='" + timeUnit + '\'' +
                ", timeInterval='" + timeInterval + '\'' +
                '}';
    }
}
