package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/28.
 */

public class AgreementResponse implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    /*"id": "aeac5c79db1f4c6ca94b8db8d7534d47",
      "content": "<p style=\"text-align: center;\">\r\n\t服务条款</p>\r\n<p style=\"text-align: center;\">\r\n\t写点服务条款内容进行测试</p>",
      "copyfrom": "serviceItem",
      "allowComment": "1",
      "allowCollection": "1"*/
    public String id;
    public String content;
    public String copyfrom;
    public String allowComment;
    public String allowCollection;

    @Override
    public String toString() {
        return "AgreementResponse{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", copyfrom='" + copyfrom + '\'' +
                ", allowComment='" + allowComment + '\'' +
                ", allowCollection='" + allowCollection + '\'' +
                '}';
    }
}
