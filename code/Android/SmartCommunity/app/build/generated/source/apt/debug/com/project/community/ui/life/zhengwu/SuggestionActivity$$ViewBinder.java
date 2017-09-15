// Generated code from Butter Knife. Do not modify!
package com.project.community.ui.life.zhengwu;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SuggestionActivity$$ViewBinder<T extends com.project.community.ui.life.zhengwu.SuggestionActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624105, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131624105, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131624106, "field 'mTvTitle'");
    target.mTvTitle = finder.castView(view, 2131624106, "field 'mTvTitle'");
    view = finder.findRequiredView(source, 2131624167, "field 'mBtnSubmit'");
    target.mBtnSubmit = finder.castView(view, 2131624167, "field 'mBtnSubmit'");
    view = finder.findRequiredView(source, 2131624191, "field 'mEtPhone'");
    target.mEtPhone = finder.castView(view, 2131624191, "field 'mEtPhone'");
    view = finder.findRequiredView(source, 2131624280, "field 'mEtTitle'");
    target.mEtTitle = finder.castView(view, 2131624280, "field 'mEtTitle'");
    view = finder.findRequiredView(source, 2131624282, "field 'mEtSuggest'");
    target.mEtSuggest = finder.castView(view, 2131624282, "field 'mEtSuggest'");
    view = finder.findRequiredView(source, 2131624283, "field 'mTvTxtCount'");
    target.mTvTxtCount = finder.castView(view, 2131624283, "field 'mTvTxtCount'");
    view = finder.findRequiredView(source, 2131624121, "field 'mLayoutRoot'");
    target.mLayoutRoot = finder.castView(view, 2131624121, "field 'mLayoutRoot'");
    view = finder.findRequiredView(source, 2131624067, "field 'mScrollView'");
    target.mScrollView = finder.castView(view, 2131624067, "field 'mScrollView'");
  }

  @Override public void unbind(T target) {
    target.mToolBar = null;
    target.mTvTitle = null;
    target.mBtnSubmit = null;
    target.mEtPhone = null;
    target.mEtTitle = null;
    target.mEtSuggest = null;
    target.mTvTxtCount = null;
    target.mLayoutRoot = null;
    target.mScrollView = null;
  }
}
