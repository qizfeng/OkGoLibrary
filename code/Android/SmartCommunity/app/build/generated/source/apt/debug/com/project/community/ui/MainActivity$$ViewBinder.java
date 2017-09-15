// Generated code from Butter Knife. Do not modify!
package com.project.community.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.project.community.ui.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624221, "field 'bottom_navigation'");
    target.bottom_navigation = finder.castView(view, 2131624221, "field 'bottom_navigation'");
  }

  @Override public void unbind(T target) {
    target.bottom_navigation = null;
  }
}
