package com.project.community.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ryane.banner_lib.AdPageInfo;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/29.
 * 轮播图
 */

public class BannerModel implements Serializable,Parcelable {
    private static final long serialVersionUID = -4337711009801627866L;
    /* "imageUrl": "",
     "linkType": 2,
     "link": "",
     "categoryId": "3",
     "articleId": "c3f4ccdf8db94c089f16d3cb3847b2b1"
     */
    public String imageUrl;//图片地址
    public String linkType;//0：无链接；1：链接；2：文章
    public String link;
    public String categoryId;//分类id
    public String articleId;//文章id


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

    protected BannerModel(Parcel in) {
        this.imageUrl = in.readString();
        this.linkType = in.readString();
        this.link = in.readString();
        this.categoryId = in.readString();
        this.articleId=in.readString();
    }

    public static final Creator<BannerModel> CREATOR = new Creator<BannerModel>() {
        @Override
        public BannerModel createFromParcel(Parcel source) {
            return new BannerModel(source);
        }

        @Override
        public BannerModel[] newArray(int size) {
            return new BannerModel[size];
        }
    };
}
