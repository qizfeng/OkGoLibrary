// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LifeFragment$$ViewBinder<T extends com.project.community.ui.life.LifeFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624105, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131624105, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131624184, "field 'tabLayout'");
    target.tabLayout = finder.castView(view, 2131624184, "field 'tabLayout'");
    view = finder.findRequiredView(source, 2131624210, "field 'viewPager'");
    target.viewPager = finder.castView(view, 2131624210, "field 'viewPager'");
  }

  @Override public void unbind(T target) {
    target.toolbar = null;
    target.tabLayout = null;
    target.viewPager = null;
  }
}
