// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.index;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class IndexFragment$$ViewBinder<T extends com.project.community.ui.index.IndexFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624108, "field 'recyclerView'");
    target.recyclerView = finder.castView(view, 2131624108, "field 'recyclerView'");
    view = finder.findRequiredView(source, 2131624215, "field 'refreshLayout'");
    target.refreshLayout = finder.castView(view, 2131624215, "field 'refreshLayout'");
    view = finder.findRequiredView(source, 2131624216, "field 'fab'");
    target.fab = finder.castView(view, 2131624216, "field 'fab'");
    view = finder.findRequiredView(source, 2131624105, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131624105, "field 'toolbar'");
  }

  @Override public void unbind(T target) {
    target.recyclerView = null;
    target.refreshLayout = null;
    target.fab = null;
    target.toolbar = null;
  }
}
