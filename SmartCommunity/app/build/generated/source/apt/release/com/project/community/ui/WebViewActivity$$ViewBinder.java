// Generated code from Butter Knife. Do not modify!
package com.project.community.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class WebViewActivity$$ViewBinder<T extends com.project.community.ui.WebViewActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624105, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131624105, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131624287, "field 'pb'");
    target.pb = finder.castView(view, 2131624287, "field 'pb'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
  }

  @Override public void unbind(T target) {
    target.toolbar = null;
    target.pb = null;
    target.mTvTitle = null;
  }
}
