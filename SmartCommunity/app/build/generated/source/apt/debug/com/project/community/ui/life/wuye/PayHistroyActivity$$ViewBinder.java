// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life.wuye;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PayHistroyActivity$$ViewBinder<T extends com.project.community.ui.life.wuye.PayHistroyActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624108, "field 'mRecyclerView'");
    target.mRecyclerView = finder.castView(view, 2131624108, "field 'mRecyclerView'");
    view = finder.findRequiredView(source, 2131624105, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131624105, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624250, "field 'mBtnAdd' and method 'onAddClick'");
    target.mBtnAdd = finder.castView(view, 2131624250, "field 'mBtnAdd'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onAddClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.mRecyclerView = null;
    target.mToolBar = null;
    target.mTvTitle = null;
    target.mBtnAdd = null;
  }
}
