// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life.zhengwu;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class WenjuanDetailActivity$$ViewBinder<T extends com.project.community.ui.life.zhengwu.WenjuanDetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624105, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131624105, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131624288, "field 'pb'");
    target.pb = finder.castView(view, 2131624288, "field 'pb'");
    view = finder.findRequiredView(source, 2131624172, "field 'mAppbar'");
    target.mAppbar = finder.castView(view, 2131624172, "field 'mAppbar'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
  }

  @Override public void unbind(T target) {
    target.toolbar = null;
    target.pb = null;
    target.mAppbar = null;
    target.mTvTitle = null;
  }
}
