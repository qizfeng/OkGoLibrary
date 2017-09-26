package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/7/21.
 * 用户
 */

public class UserModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    
      /*"id": null,
      "isNewRecord": true,
      "remarks": null,
      "createDate": null,
      "updateDate": null,
      "userCodeLevel": null,
      "loginName": null,
      "no": null,
      "name": "系统管理员",
      "email": null,
      "phone": null,
      "mobile": null,
      "userType": null,
      "loginIp": null,
      "loginDate": null,
      "loginFlag": "0",
      "photo": null,
      "oldLoginName": null,
      "newPassword": null,
      "oldLoginIp": null,
      "oldLoginDate": null,
      "role": null,
      "auditStatus": "1",
      "thirdAccount": null,
      "idCard": null,
      "defaultPhoto": null,
      "sex": "1",
      "admin": false,
      "roleNames": ""
      orgCode:组织机构
      ownerAuditStatus:业主审核状态 true false
    */
    public String id;//用户ID
    public String loginName;//用户名
    public String name;//姓名
    public String mobileLogin;
    public String sessionid;

    public String isNewRecord;
    public String remarks;
    public String createDate;
    public String updateDate;
    public String userCodeLevel;
    public String no;
    public String email;
    public String phone;
    public String mobile;
    public String userType;
    public String loginIp;
    public String loginDate;
    public String loginFlag;
    public String newPassword;
    public String oldLoginIp;
    public String oldLoginDate;
    public String role;
    public String auditStatus;
    public String thirdAccount;
    public String idCard;
    public String defaultPhoto;
    public String sex;//性别 1：男 2：女
    public String admin;
    public String roleNames;
    public String photo;
    public String occupation;//职业
    public String religion;//宗教信仰
    public String party;//党派
    public String isOwner;//是否是业主
    public String nation;//民族
    public String roomNo;//房屋编号
    public String orgCode;
    public boolean ownerAuditStatus;
    public String roleType;//1普通用户 2管理员,可以进入社区
    @Override
    public String toString() {
        return "UserModel{" +
                "id='" + id + '\'' +
                ", loginName='" + loginName + '\'' +
                ", name='" + name + '\'' +
                ", mobileLogin='" + mobileLogin + '\'' +
                ", sessionid='" + sessionid + '\'' +
                ", isNewRecord='" + isNewRecord + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", userCodeLevel='" + userCodeLevel + '\'' +
                ", no='" + no + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", mobile='" + mobile + '\'' +
                ", userType='" + userType + '\'' +
                ", loginIp='" + loginIp + '\'' +
                ", loginDate='" + loginDate + '\'' +
                ", loginFlag='" + loginFlag + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", oldLoginIp='" + oldLoginIp + '\'' +
                ", oldLoginDate='" + oldLoginDate + '\'' +
                ", role='" + role + '\'' +
                ", auditStatus='" + auditStatus + '\'' +
                ", thirdAccount='" + thirdAccount + '\'' +
                ", idCard='" + idCard + '\'' +
                ", defaultPhoto='" + defaultPhoto + '\'' +
                ", sex='" + sex + '\'' +
                ", admin='" + admin + '\'' +
                ", roleNames='" + roleNames + '\'' +
                ", photo='" + photo + '\'' +
                ", occupation='" + occupation + '\'' +
                ", religion='" + religion + '\'' +
                ", party='" + party + '\'' +
                ", isOwner='" + isOwner + '\'' +
                ", nation='" + nation + '\'' +
                ", roomNo='" + roomNo + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", ownerAuditStatus='" + ownerAuditStatus + '\'' +
                ", roleType='" + roleType + '\'' +
                '}';
    }
}
