package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/11.
 * 办事指南
 */

public class GuideModel implements Serializable{
    private static final long serialVersionUID = -4337711009801627866L;
    public String id;
    public String content;

    /* "id": "16c83a22cbcf4e528af14949cb3b0742", #指南ID
       "remarks": "", #备注
       "createDate": "2017-08-15 06:31:39", #创建时间
       "updateDate": "2017-08-15 06:36:58", #更新时间
       "guideContent": "\r\n\t下次", #内容
       "orgCode": "阳光社区", #社区名称
       "title": "说的", #标题
       "guideTheme": "gov_theme_2", #指南主题
       "guidePart": "gov_theme_2_part_1" #指南部门
    */
    public String remarks;
    public String createDate;
    public String updateDate;
    public String guideContent;
    public String orgCode;
    public String title;
    public String guideTheme;
    public String guidePart;

    @Override
    public String toString() {
        return "GuideModel{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", guideContent='" + guideContent + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", title='" + title + '\'' +
                ", guideTheme='" + guideTheme + '\'' +
                ", guidePart='" + guidePart + '\'' +
                '}';
    }
}
