// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.user;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ForgetPassActivity$$ViewBinder<T extends com.project.community.ui.user.ForgetPassActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624105, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131624105, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624190, "field 'mBtnVerify'");
    target.mBtnVerify = finder.castView(view, 2131624190, "field 'mBtnVerify'");
    view = finder.findRequiredView(source, 2131624188, "field 'mEtPhone'");
    target.mEtPhone = finder.castView(view, 2131624188, "field 'mEtPhone'");
    view = finder.findRequiredView(source, 2131624189, "field 'mEtVerify'");
    target.mEtVerify = finder.castView(view, 2131624189, "field 'mEtVerify'");
    view = finder.findRequiredView(source, 2131624191, "field 'mEtPwd'");
    target.mEtPwd = finder.castView(view, 2131624191, "field 'mEtPwd'");
    view = finder.findRequiredView(source, 2131624192, "field 'mIvEyes'");
    target.mIvEyes = finder.castView(view, 2131624192, "field 'mIvEyes'");
    view = finder.findRequiredView(source, 2131624167, "field 'mBtnConfirm'");
    target.mBtnConfirm = finder.castView(view, 2131624167, "field 'mBtnConfirm'");
    view = finder.findRequiredView(source, 2131624121, "field 'mLayoutRoot'");
    target.mLayoutRoot = finder.castView(view, 2131624121, "field 'mLayoutRoot'");
  }

  @Override public void unbind(T target) {
    target.mToolBar = null;
    target.mTvTitle = null;
    target.mBtnVerify = null;
    target.mEtPhone = null;
    target.mEtVerify = null;
    target.mEtPwd = null;
    target.mIvEyes = null;
    target.mBtnConfirm = null;
    target.mLayoutRoot = null;
  }
}