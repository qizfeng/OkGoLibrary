package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/9/4.
 * 热线
 */

public class HotlineModel implements Serializable{
    private static final long serialVersionUID = -4337711009801627866L;
    /*"work_start": "",
      "work_end": "",
      "contact": "010456789563",
      "photo": "",
      "org_name": "阳光小区",
      "org_code": "650422100007001"
      */
    public String work_start;
    public String work_end;
    public String contact;
    public String photo;
    public String org_name;
    public String org_code;

    @Override
    public String toString() {
        return "HotlineModel{" +
                "work_start='" + work_start + '\'' +
                ", work_end='" + work_end + '\'' +
                ", contact='" + contact + '\'' +
                ", photo='" + photo + '\'' +
                ", org_name='" + org_name + '\'' +
                ", org_code='" + org_code + '\'' +
                '}';
    }
}
