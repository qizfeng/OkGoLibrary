// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life.family;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FamilyAddActivity$$ViewBinder<T extends com.project.community.ui.life.family.FamilyAddActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624105, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131624105, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624102, "field 'mEtHouseName'");
    target.mEtHouseName = finder.castView(view, 2131624102, "field 'mEtHouseName'");
    view = finder.findRequiredView(source, 2131624103, "field 'mEtHouseNo'");
    target.mEtHouseNo = finder.castView(view, 2131624103, "field 'mEtHouseNo'");
    view = finder.findRequiredView(source, 2131624104, "field 'mBtnNext' and method 'onNext'");
    target.mBtnNext = finder.castView(view, 2131624104, "field 'mBtnNext'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onNext(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.mToolBar = null;
    target.mTvTitle = null;
    target.mEtHouseName = null;
    target.mEtHouseNo = null;
    target.mBtnNext = null;
  }
}
