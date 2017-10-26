package com.project.community.bean;

/**
 * author：fangkai on 2017/10/26 17:22
 * em：617716355@qq.com
 */
public class MerchantBean {


    /**
     * id : 1
     * shopId : 1f99d9fc71bb4b01b90e42817ac955d6
     * shopsName : UGG图
     * shopPhoto : /upload/shequ_P9294Cm3KDY820009D.1507631053499.jpg
     * latitude : 30.548741
     * longitude : 30.548741
     * coordinate :
     * businessAddress : 中国四川省成都市武侯区天府大道中段-800号
     * starLevel : 0.0
     * auditStatus : 1
     */

    private String id;
    private String shopId;
    private String shopsName;
    private String shopPhoto;
    private double latitude;
    private double longitude;
    private String coordinate;
    private String businessAddress;
    private double starLevel;
    private String auditStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopsName() {
        return shopsName;
    }

    public void setShopsName(String shopsName) {
        this.shopsName = shopsName;
    }

    public String getShopPhoto() {
        return shopPhoto;
    }

    public void setShopPhoto(String shopPhoto) {
        this.shopPhoto = shopPhoto;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public double getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(double starLevel) {
        this.starLevel = starLevel;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
}
