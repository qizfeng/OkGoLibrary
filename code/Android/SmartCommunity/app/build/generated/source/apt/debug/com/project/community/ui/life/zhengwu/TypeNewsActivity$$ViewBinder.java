// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life.zhengwu;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class TypeNewsActivity$$ViewBinder<T extends com.project.community.ui.life.zhengwu.TypeNewsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624105, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131624105, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624108, "field 'recyclerView'");
    target.recyclerView = finder.castView(view, 2131624108, "field 'recyclerView'");
    view = finder.findRequiredView(source, 2131624197, "field 'refreshLayout'");
    target.refreshLayout = finder.castView(view, 2131624197, "field 'refreshLayout'");
    view = finder.findRequiredView(source, 2131624198, "field 'fab'");
    target.fab = finder.castView(view, 2131624198, "field 'fab'");
  }

  @Override public void unbind(T target) {
    target.mToolBar = null;
    target.mTvTitle = null;
    target.recyclerView = null;
    target.refreshLayout = null;
    target.fab = null;
  }
}
