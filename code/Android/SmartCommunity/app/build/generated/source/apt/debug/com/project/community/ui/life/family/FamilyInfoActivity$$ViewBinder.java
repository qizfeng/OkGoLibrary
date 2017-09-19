// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life.family;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FamilyInfoActivity$$ViewBinder<T extends com.project.community.ui.life.family.FamilyInfoActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624105, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131624105, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624191, "field 'mTabLayout'");
    target.mTabLayout = finder.castView(view, 2131624191, "field 'mTabLayout'");
    view = finder.findRequiredView(source, 2131624108, "field 'mRecyclerView'");
    target.mRecyclerView = finder.castView(view, 2131624108, "field 'mRecyclerView'");
    view = finder.findRequiredView(source, 2131624193, "field 'mIvAdd'");
    target.mIvAdd = finder.castView(view, 2131624193, "field 'mIvAdd'");
  }

  @Override public void unbind(T target) {
    target.mToolBar = null;
    target.mTvTitle = null;
    target.mTabLayout = null;
    target.mRecyclerView = null;
    target.mIvAdd = null;
  }
}
