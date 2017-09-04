package com.project.community.model;

import java.io.Serializable;

/**
 * Created by zipingfang on 17/7/13.
 */

public class NewsModel implements Serializable{
    private static final long serialVersionUID = -4337711009801627866L;
    /* ctime: "2017-06-07 10:12",
     title: "舍不得媳妇套不住流氓！警察带女友约会“钓色魔”",
     description: "搜狐社会",
     picUrl: "",
     url: "http://news.sohu.com/20170607/n495984947.shtml"*/
    public String ctime;
    public String title;
    public String description;
    public String url;
    public String picUrl;
    public int zanNum;
    public int commentsNum;
    public boolean isCollect;

    @Override
    public String toString() {
        return "NewsModel{" +
                "ctime='" + ctime + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", zanNum=" + zanNum +
                ", commentsNum=" + commentsNum +
                ", isCollect=" + isCollect +
                '}';
    }
}
