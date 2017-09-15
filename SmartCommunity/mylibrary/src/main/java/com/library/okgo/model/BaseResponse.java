package com.library.okgo.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/6/12.
 */

public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;

    public int code;
    public String msg;
    public String state;
    public int status;
    public String message;
    public String retMsg;
    public String errNum;
    public T data;
    /**
     * 泛型根据接口定义自行修改
     */
    public T result;
    public T newslist;
    public T retData;
}
