package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/8/29.
 * 上传图片
 */

public class FileUploadModel implements Serializable{
    private static final long serialVersionUID = -4337711009801627866L;
    public String filePath;

    @Override
    public String toString() {
        return "FileUploadModel{" +
                "filePath='" + filePath + '\'' +
                '}';
    }
}
