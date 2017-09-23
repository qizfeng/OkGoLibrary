package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/9/20.
 * 小区统计
 */

public class CommunityCensusModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    /* "roomCount": "4户", #总户数
       "coordinate": "116.400439,39.915046", #坐标
       "memberCount": "4人",   #总人数
       "id": 4,#楼栋id
       "floor": "1号楼" #楼栋名称
       */
    public String roomCount;
    public String coordinate;
    public String memberCount;
    public String id;
    public String floor;

    @Override
    public String toString() {
        return "CommunityCensusModel{" +
                "roomCount='" + roomCount + '\'' +
                ", coordinate='" + coordinate + '\'' +
                ", memberCount='" + memberCount + '\'' +
                ", id='" + id + '\'' +
                ", floor='" + floor + '\'' +
                '}';
    }
}
