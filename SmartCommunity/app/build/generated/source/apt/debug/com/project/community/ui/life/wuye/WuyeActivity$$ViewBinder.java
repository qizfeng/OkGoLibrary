// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life.wuye;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class WuyeActivity$$ViewBinder<T extends com.project.community.ui.life.wuye.WuyeActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624108, "field 'recyclerView'");
    target.recyclerView = finder.castView(view, 2131624108, "field 'recyclerView'");
    view = finder.findRequiredView(source, 2131624211, "field 'refreshLayout'");
    target.refreshLayout = finder.castView(view, 2131624211, "field 'refreshLayout'");
    view = finder.findRequiredView(source, 2131624212, "field 'fab'");
    target.fab = finder.castView(view, 2131624212, "field 'fab'");
  }

  @Override public void unbind(T target) {
    target.recyclerView = null;
    target.refreshLayout = null;
    target.fab = null;
  }
}
