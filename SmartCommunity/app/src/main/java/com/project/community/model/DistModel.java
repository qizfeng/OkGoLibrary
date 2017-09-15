package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/31.
 * 小区
 */

public class DistModel implements Serializable{
    private static final long serialVersionUID = -4337711009801627866L;
    /* "id": "9",#ID
            "orgCode": "650422100007000",#小区编号
            "orgName": "阳光社区",#小区名称
            "orgSort": "0"#排序*/
    public String id;
    public String orgCode;
    public String orgName;
    public String orgSort;

    @Override
    public String toString() {
        return "DistModel{" +
                "id='" + id + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", orgName='" + orgName + '\'' +
                ", orgSort='" + orgSort + '\'' +
                '}';
    }
}
