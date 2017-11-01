package com.project.community.Event;

import com.project.community.bean.AddressListBean;

/**
 * author：cj on 2017/10/26 15:09
 * em：617716355@qq.com
 */
public class ChangeAddressEvent {
    private AddressListBean addressListBean;

    public ChangeAddressEvent(AddressListBean addressListBean) {
        this.addressListBean = addressListBean;
    }

    public AddressListBean getAddressListBean() {
        return addressListBean;
    }

    public void setAddressListBean(AddressListBean addressListBean) {
        this.addressListBean = addressListBean;
    }
}
