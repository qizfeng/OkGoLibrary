// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.user;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ModifyPwdActivity$$ViewBinder<T extends com.project.community.ui.user.ModifyPwdActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624105, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131624105, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624242, "field 'mEtOldPwd'");
    target.mEtOldPwd = finder.castView(view, 2131624242, "field 'mEtOldPwd'");
    view = finder.findRequiredView(source, 2131624243, "field 'mEtNewPwd'");
    target.mEtNewPwd = finder.castView(view, 2131624243, "field 'mEtNewPwd'");
    view = finder.findRequiredView(source, 2131624244, "field 'mBtnSave' and method 'onSave'");
    target.mBtnSave = finder.castView(view, 2131624244, "field 'mBtnSave'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onSave(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624191, "field 'mIvEyes' and method 'onShowPwd'");
    target.mIvEyes = finder.castView(view, 2131624191, "field 'mIvEyes'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onShowPwd(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.mToolBar = null;
    target.mTvTitle = null;
    target.mEtOldPwd = null;
    target.mEtNewPwd = null;
    target.mBtnSave = null;
    target.mIvEyes = null;
  }
}
