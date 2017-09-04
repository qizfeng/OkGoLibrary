package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/28.
 * 问卷信息
 */
public class SurveyInfoModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    /*  "id": "",#问卷ID
             "isPublic": 0,#默认0：所有人可看；1：用户投票后不可看结果
             "timeInterval": 0#投票间隔 为0 则只能投票一次*/
    public String id;

    public String isPublic;
    public String timeInterval;

    @Override
    public String toString() {
        return "SurveyInfoModel{" +
                "id='" + id + '\'' +
                ", isPublic='" + isPublic + '\'' +
                ", timeInterval='" + timeInterval + '\'' +
                '}';
    }
}
