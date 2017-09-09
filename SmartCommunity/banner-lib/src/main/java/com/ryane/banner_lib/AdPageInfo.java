package com.ryane.banner_lib;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Creator: lijianchang
 * Create Time: 2017/6/17.
 * Email: lijianchang@yy.com
 */

public class AdPageInfo implements Parcelable,Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
//    public String title;    // 广告标题
//    public String picUrl;   // 广告图片url
//    public String clickUlr; // 图片点击url
//    public int order;       // 顺序
//
//    public AdPageInfo(String title, String picUrl, String clickUlr, int order) {
//        this.title = title;
//        this.picUrl = picUrl;
//        this.clickUlr = clickUlr;
//        this.order = order;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getPicUrl() {
//        return picUrl;
//    }
//
//    public String getClickUlr() {
//        return clickUlr;
//    }
//
//    public int getOrder() {
//        return order;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public void setPicUrl(String picUrl) {
//        this.picUrl = picUrl;
//    }
//
//    public void setClickUlr(String clickUlr) {
//        this.clickUlr = clickUlr;
//    }
//
//    public void setOrder(int order) {
//        this.order = order;
//    }
//
//    @Override
//    public String toString() {
//        return "AdPageInfo{" +
//                "title='" + title + '\'' +
//                ", picUrl='" + picUrl + '\'' +
//                ", clickUlr='" + clickUlr + '\'' +
//                ", order=" + order +
//                '}';
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.title);
//        dest.writeString(this.picUrl);
//        dest.writeString(this.clickUlr);
//        dest.writeInt(this.order);
//    }
//
//    protected AdPageInfo(Parcel in) {
//        this.title = in.readString();
//        this.picUrl = in.readString();
//        this.clickUlr = in.readString();
//        this.order = in.readInt();
//    }
//
//    public static final Creator<AdPageInfo> CREATOR = new Creator<AdPageInfo>() {
//        @Override
//        public AdPageInfo createFromParcel(Parcel source) {
//            return new AdPageInfo(source);
//        }
//
//        @Override
//        public AdPageInfo[] newArray(int size) {
//            return new AdPageInfo[size];
//        }
//    };



    public String imageUrl;//图片地址
    public String linkType;//0：无链接；1：链接；2：文章
    public String link;//外链接
    public String categoryId;//分类id
    public String articleId;//文章id

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.linkType);
        dest.writeString(this.link);
        dest.writeString(this.categoryId);
        dest.writeString(this.articleId);
    }

    protected AdPageInfo(Parcel in) {
        this.imageUrl = in.readString();
        this.linkType = in.readString();
        this.link = in.readString();
        this.categoryId = in.readString();
        this.articleId=in.readString();
    }

    public static final Creator<AdPageInfo> CREATOR = new Creator<AdPageInfo>() {
        @Override
        public AdPageInfo createFromParcel(Parcel source) {
            return new AdPageInfo(source);
        }

        @Override
        public AdPageInfo[] newArray(int size) {
            return new AdPageInfo[size];
        }
    };

    @Override
    public String toString() {
        return "AdPageInfo{" +
                "imageUrl='" + imageUrl + '\'' +
                ", linkType='" + linkType + '\'' +
                ", link='" + link + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", articleId='" + articleId + '\'' +
                '}';
    }
}
