package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/28.
 * 组织机构Model
 */

public class SysOrgModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    /* "id": null,
       "isNewRecord": true,
       "remarks": null,
       "createDate": null,
       "updateDate": null,
       "userCodeLevel": null,
       "orgCode": "650422100007000", #机构代码
       "orgName": "阳光社区", #机构名称
       "orgSort": null,
       "parent": null,
       "parentId": null*/
    public String id;
    public String isNewRecord;
    public String createDate;
    public String updateDate;
    public String userCodeLevel;
    public String orgCode;//机构代码
    public String orgName;//机构名称
    public String orgSort;
    public String parent;
    public String partentId;

    @Override
    public String toString() {
        return "SysOrgModel{" +
                "id='" + id + '\'' +
                ", isNewRecord='" + isNewRecord + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", userCodeLevel='" + userCodeLevel + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", orgName='" + orgName + '\'' +
                ", orgSort='" + orgSort + '\'' +
                ", parent='" + parent + '\'' +
                ", partentId='" + partentId + '\'' +
                '}';
    }
}
