package com.library.okgo.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/6/12.
 */

public class SimpleResponse implements Serializable {

    private static final long serialVersionUID = -1477609349345966116L;

    public int code;
    public String msg;

    public BaseResponse toLzyResponse() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.code = code;
        baseResponse.msg = msg;
        return baseResponse;
    }
}