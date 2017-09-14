// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life.zhengwu;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ZhengwuActivity$$ViewBinder<T extends com.project.community.ui.life.zhengwu.ZhengwuActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624108, "field 'recyclerView'");
    target.recyclerView = finder.castView(view, 2131624108, "field 'recyclerView'");
    view = finder.findRequiredView(source, 2131624207, "field 'refreshLayout'");
    target.refreshLayout = finder.castView(view, 2131624207, "field 'refreshLayout'");
    view = finder.findRequiredView(source, 2131624208, "field 'fab'");
    target.fab = finder.castView(view, 2131624208, "field 'fab'");
    view = finder.findRequiredView(source, 2131624199, "field 'gridView'");
    target.gridView = finder.castView(view, 2131624199, "field 'gridView'");
  }

  @Override public void unbind(T target) {
    target.recyclerView = null;
    target.refreshLayout = null;
    target.fab = null;
    target.gridView = null;
  }
}
