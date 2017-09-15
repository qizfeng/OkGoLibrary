// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.index;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CategoryActivity$$ViewBinder<T extends com.project.community.ui.index.CategoryActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624108, "field 'mRecyclerView'");
    target.mRecyclerView = finder.castView(view, 2131624108, "field 'mRecyclerView'");
    view = finder.findRequiredView(source, 2131624105, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131624105, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624107, "field 'mTvCancel' and method 'onCancel'");
    target.mTvCancel = finder.castView(view, 2131624107, "field 'mTvCancel'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onCancel(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.mRecyclerView = null;
    target.mToolBar = null;
    target.mTvTitle = null;
    target.mTvCancel = null;
  }
}
