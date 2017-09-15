package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/9/7.
 * 审核状态
 */

public class AuditStatusModel implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    public String auditStatus;

    @Override
    public String toString() {
        return "AuditStatusModel{" +
                "auditStatus='" + auditStatus + '\'' +
                '}';
    }
}
