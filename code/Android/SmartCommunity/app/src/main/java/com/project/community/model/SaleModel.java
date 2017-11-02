package com.project.community.model;


import java.io.Serializable;

/**
 * Created by zipingfang on 17/10/17.
 */

public class SaleModel implements Serializable {
    private static final long serialVersionUID = 6753210234564872868L;

    public String content ;
    public String imagesUrl ;
    public int handleStatus ;

    @Override
    public String toString() {
        return "SaleModel{" +
                "content='" + content + '\'' +
                "handleStatus='" + handleStatus + '\'' +
                ", imagesUrl='" + imagesUrl + '\'' +
                '}';
    }
}
