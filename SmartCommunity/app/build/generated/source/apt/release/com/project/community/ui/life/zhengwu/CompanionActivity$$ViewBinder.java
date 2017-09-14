// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life.zhengwu;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CompanionActivity$$ViewBinder<T extends com.project.community.ui.life.zhengwu.CompanionActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624105, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131624105, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624122, "field 'mLayoutTheme'");
    target.mLayoutTheme = finder.castView(view, 2131624122, "field 'mLayoutTheme'");
    view = finder.findRequiredView(source, 2131624124, "field 'mLayoutDepart'");
    target.mLayoutDepart = finder.castView(view, 2131624124, "field 'mLayoutDepart'");
    view = finder.findRequiredView(source, 2131624121, "field 'layout_parent'");
    target.layout_parent = finder.castView(view, 2131624121, "field 'layout_parent'");
    view = finder.findRequiredView(source, 2131624125, "field 'mTvDepart'");
    target.mTvDepart = finder.castView(view, 2131624125, "field 'mTvDepart'");
    view = finder.findRequiredView(source, 2131624123, "field 'mTvTheme'");
    target.mTvTheme = finder.castView(view, 2131624123, "field 'mTvTheme'");
    view = finder.findRequiredView(source, 2131624108, "field 'recyclerView'");
    target.recyclerView = finder.castView(view, 2131624108, "field 'recyclerView'");
    view = finder.findRequiredView(source, 2131624126, "field 'mBtnSubmit'");
    target.mBtnSubmit = finder.castView(view, 2131624126, "field 'mBtnSubmit'");
  }

  @Override public void unbind(T target) {
    target.mToolBar = null;
    target.mTvTitle = null;
    target.mLayoutTheme = null;
    target.mLayoutDepart = null;
    target.layout_parent = null;
    target.mTvDepart = null;
    target.mTvTheme = null;
    target.recyclerView = null;
    target.mBtnSubmit = null;
  }
}
