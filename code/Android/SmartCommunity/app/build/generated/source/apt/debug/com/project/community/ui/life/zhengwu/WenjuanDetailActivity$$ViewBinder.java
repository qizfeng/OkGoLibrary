// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life.zhengwu;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class WenjuanDetailActivity$$ViewBinder<T extends com.project.community.ui.life.zhengwu.WenjuanDetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624105, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131624105, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624108, "field 'mRecyclerView'");
    target.mRecyclerView = finder.castView(view, 2131624108, "field 'mRecyclerView'");
    view = finder.findRequiredView(source, 2131624289, "field 'mTvSubmit'");
    target.mTvSubmit = finder.castView(view, 2131624289, "field 'mTvSubmit'");
  }

  @Override public void unbind(T target) {
    target.mToolBar = null;
    target.mTvTitle = null;
    target.mRecyclerView = null;
    target.mTvSubmit = null;
  }
}
