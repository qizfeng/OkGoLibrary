// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life.minsheng;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MinshengActivity$$ViewBinder<T extends com.project.community.ui.life.minsheng.MinshengActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624108, "field 'mRecyclerView'");
    target.mRecyclerView = finder.castView(view, 2131624108, "field 'mRecyclerView'");
    view = finder.findRequiredView(source, 2131624197, "field 'refreshLayout'");
    target.refreshLayout = finder.castView(view, 2131624197, "field 'refreshLayout'");
  }

  @Override public void unbind(T target) {
    target.mRecyclerView = null;
    target.refreshLayout = null;
  }
}
