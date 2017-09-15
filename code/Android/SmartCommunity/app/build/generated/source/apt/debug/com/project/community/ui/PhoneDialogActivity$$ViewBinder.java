// Generated code from Butter Knife. Do not modify!
package com.project.community.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PhoneDialogActivity$$ViewBinder<T extends com.project.community.ui.PhoneDialogActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624168, "field 'mIvClose'");
    target.mIvClose = finder.castView(view, 2131624168, "field 'mIvClose'");
    view = finder.findRequiredView(source, 2131624124, "field 'mIvHeader'");
    target.mIvHeader = finder.castView(view, 2131624124, "field 'mIvHeader'");
    view = finder.findRequiredView(source, 2131624172, "field 'mTvPhoneNumber'");
    target.mTvPhoneNumber = finder.castView(view, 2131624172, "field 'mTvPhoneNumber'");
    view = finder.findRequiredView(source, 2131624173, "field 'mTvTime'");
    target.mTvTime = finder.castView(view, 2131624173, "field 'mTvTime'");
    view = finder.findRequiredView(source, 2131624174, "field 'mBtnCall'");
    target.mBtnCall = finder.castView(view, 2131624174, "field 'mBtnCall'");
  }

  @Override public void unbind(T target) {
    target.mTvTitle = null;
    target.mIvClose = null;
    target.mIvHeader = null;
    target.mTvPhoneNumber = null;
    target.mTvTime = null;
    target.mBtnCall = null;
  }
}
