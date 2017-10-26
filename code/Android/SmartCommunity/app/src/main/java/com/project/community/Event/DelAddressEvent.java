package com.project.community.Event;

import com.project.community.bean.AddressListBean;

/**
 * author：fangkai on 2017/10/26 14:21
 * em：617716355@qq.com
 */
public class DelAddressEvent {
    private AddressListBean addressListBean;

    public DelAddressEvent(AddressListBean addressListBean) {
        this.addressListBean = addressListBean;
    }

    public AddressListBean getAddressListBean() {
        return addressListBean;
    }

    public void setAddressListBean(AddressListBean addressListBean) {
        this.addressListBean = addressListBean;
    }
}
