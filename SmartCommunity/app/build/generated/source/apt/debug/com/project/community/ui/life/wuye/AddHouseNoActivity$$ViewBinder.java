// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life.wuye;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AddHouseNoActivity$$ViewBinder<T extends com.project.community.ui.life.wuye.AddHouseNoActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624105, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131624105, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624103, "field 'mEtHouseNo'");
    target.mEtHouseNo = finder.castView(view, 2131624103, "field 'mEtHouseNo'");
    view = finder.findRequiredView(source, 2131624104, "field 'mBtnNext' and method 'onNextClick'");
    target.mBtnNext = finder.castView(view, 2131624104, "field 'mBtnNext'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onNextClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.mToolBar = null;
    target.mTvTitle = null;
    target.mEtHouseNo = null;
    target.mBtnNext = null;
  }
}
