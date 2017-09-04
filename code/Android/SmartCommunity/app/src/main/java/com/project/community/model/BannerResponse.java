package com.project.community.model;

import com.ryane.banner_lib.AdPageInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizfeng on 17/8/29.
 */

public class BannerResponse implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    public List<AdPageInfo> imageList = new ArrayList<>();

    @Override
    public String toString() {
        return "BannerResponse{" +
                "imageList=" + imageList +
                '}';
    }
}
